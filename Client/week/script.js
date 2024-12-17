
const USER_UID = 2;

document.addEventListener('DOMContentLoaded', function() {
    var weekDaysElement = document.getElementById('weekDays');
    var daysElement = document.querySelector('.days');
    var calendarTitle = document.getElementById('calendarTitle');
    var eventForm = document.getElementById('eventForm');
    var eventNameInput = document.getElementById('eventName');
    var saveEventButton = document.getElementById('saveEvent');
    var cancelEventButton = document.getElementById('cancelEvent');
    var eventDescriptionInput = document.getElementById('eventDescription');
    var eventConferenceUrlInput = document.getElementById('eventConferenceUrl');

    var currentDate = new Date();
    var isDragging = false;
    var startSlot = null;

    
function getWeekStart(date) {
    const day = date.getDay();
    const diff = (day === 0 ? -6 : 1) - day; 
    var weekStart = new Date(date);
    weekStart.setDate(date.getDate() + diff);
    //weekStart.setDate(weekStart.getDate() + 1);
    weekStart.setHours(3, 0, 0, 0); 
    return weekStart;
}


    function formatDate(date) {
        var day = String(date.getDate()).padStart(2, '0');
        var month = String(date.getMonth() + 1).padStart(2, '0');
        return day + '.' + month;
    }

    function updateCalendarTitle(startDate) {
        var endDate = new Date(startDate);
        endDate.setDate(startDate.getDate() + 6);
        calendarTitle.textContent = 'Календарь: ' + formatDate(startDate) + ' - ' + formatDate(endDate);
    }

    function renderWeek(startDate) {
        weekDaysElement.innerHTML = '';
        daysElement.innerHTML = '';
    
        updateCalendarTitle(startDate);
    
        // Определите конец недели (всегда на 6 дней позднее startDate)
        const endDate = new Date(startDate);
        endDate.setDate(endDate.getDate() + 6);
    
        // Форматируйте startDate и endDate как ISO строки
        const formattedStartDate = startDate.toISOString().split('T')[0] + 'T00:00:00Z';
        const formattedEndDate = endDate.toISOString().split('T')[0] + 'T23:59:59Z';
    
        // Запросите события
        fetchAllUserEvents(USER_UID, formattedStartDate, formattedEndDate)
            .then(events => {
                for (var i = 0; i < 7; i++) {
                    var currentDay = new Date(startDate);
                    currentDay.setDate(currentDay.getDate() + i);

                    var cd = currentDay;
    
                    var dayName = currentDay.toLocaleDateString('ru-RU', { weekday: 'short' });
                    var formattedDate = formatDate(currentDay);
    
                    var dayHeader = document.createElement('div');
                    dayHeader.textContent = dayName + ', ' + formattedDate;
                    weekDaysElement.appendChild(dayHeader);
    
                    var dayColumn = document.createElement('div');
                    dayColumn.className = 'day';
                    dayColumn.style.position = 'relative';
                    dayColumn.style.display = 'flex';
                    dayColumn.style.flexDirection = 'column';
    
                    for (var j = 0; j < 96; j++) {
                        var timeSlot = document.createElement('div');
                        timeSlot.className = 'time-slot';

                        const curDay = cd.toISOString().split('T')[0];
                        const hours = cd.getHours().toString().padStart(2, '0');
                        const minutes = cd.getMinutes().toString().padStart(2, '0');
                        
                        timeSlot.dataset.datetime = `${curDay} ${hours}:${minutes}`;
                        console.log(`${curDay} ${hours}:${minutes}`)

                        cd.setMinutes(cd.getMinutes() + 15);
                        if (cd.getHours() === 0 && cd.getMinutes() === 0) {
                            cd.setHours(0, 0, 0, 0);
                        }

                        timeSlot.addEventListener('mousedown', function(e) {
                            isDragging = true;
                            startSlot = e.target;
                            console.log(startSlot)
                            e.target.style.backgroundColor = '#cfe3ff';
                        });
    
                        timeSlot.addEventListener('mouseenter', function(e) {
                            if (isDragging && startSlot) {
                                e.target.style.backgroundColor = '#cfe3ff';
                            }
                        });
    
                        timeSlot.addEventListener('mouseup', function(e) {
                            if (isDragging && startSlot) {
                                endSlot = e.target;
                                console.log(endSlot)
                                handleEventCreation(startSlot, endSlot);
                                startSlot = null;
                            }
                            isDragging = false;
                        });
    
                        dayColumn.appendChild(timeSlot);
                    }
                    //initializeTimeSlots();

    
                    daysElement.appendChild(dayColumn);
                }
                
                // Отрисовка событий после получения данных
                renderWeekEvents(events, startDate);
    
            })
            .catch(error => {
                console.error('Ошибка при получении событий:', error);
            });
    
        document.addEventListener('mouseup', function() {
            isDragging = false;
            startSlot = null;
        });
    }
    
    
function renderWeekEvents(events, startDate) {
    events.forEach(event => {  
        const eventStart = new Date(event.from);
        const eventEnd = new Date(event.to);
        
  console.log(`Processing event: ${event.title} from ${eventStart} to ${eventEnd}`);
  


        // Проверьте, что события находятся в пределах отображаемой недели
        if (eventStart < startDate || eventEnd > new Date(startDate.getTime() + 6 * 24 * 60 * 60 * 1000)) {
            return;  // Пропустите события, которые не попадают в данную неделю
        }

        const dayIndex = (eventStart.getDay() + 6) % 7; // Сдвиг, чтобы неделя начиналась с понедельника
        const minutesFromMidnight = eventStart.getHours() * 60 + eventStart.getMinutes();
        const startSlotIndex = Math.floor(minutesFromMidnight / 15);

        const eventDurationMinutes = (eventEnd - eventStart) / (1000 * 60);
        const slotCount = Math.ceil(eventDurationMinutes / 15);

        // Получаем соответствующий столбец дня
        const dayColumns = document.querySelectorAll('.day');
        console.log(dayColumns)
        const dayColumn = dayColumns[dayIndex];

        if (dayColumn) {
            var startSlot = dayColumn.children[startSlotIndex];
            var endSlot = dayColumn.children[startSlotIndex + slotCount]
            createEvent(startSlot, endSlot, event.title, event.description, event.conferenceUrl, event.eventId)
        }
    });
}

    

    

function initializeTimeSlots() {
    const slots = document.querySelectorAll('.time-slot');
    let cd = getWeekStart(currentDate); 
    cd.setHours(0, 0, 0, 0);

    slots.forEach(slot => {
        const currentDay = cd.toISOString().split('T')[0];
        const hours = cd.getHours().toString().padStart(2, '0');
        const minutes = cd.getMinutes().toString().padStart(2, '0');
        
        slot.dataset.datetime = `${currentDay} ${hours}:${minutes}`;
        console.log(slot.dataset.datetime)
        
        cd.setMinutes(cd.getMinutes() + 15);
        
        if (cd.getHours() === 0 && cd.getMinutes() === 0) {
            cd.setHours(0, 0, 0, 0);
        }
    });
}



    

//HUI

let currentlySelectedEvent = null;

function handleEventCreation(start, end) {
    eventForm.style.display = 'block';

    saveEventButton.onclick = function() {
        const eventName = eventNameInput.value.trim();
        if (eventName !== "") {
            const description = eventDescriptionInput.value.trim();
            const conferenceUrl = eventConferenceUrlInput.value.trim();

            const eventData = {
                title: eventName,
                description: description,
                conferenceUrl: conferenceUrl,
                from: new Date(start.dataset.datetime).getTime() / 1000 - 10800,
                to: new Date(end.dataset.datetime).getTime() / 1000 - 10800,
                uid: USER_UID,
                period: "NONE",
                opennessPolicy: "OPENED",
            };
            const id = createEventRequest(eventData);
            console.log(id)
            createEvent(start, end, eventName, description, conferenceUrl, id);
            hideEventForm(start);
        } else {
            alert('Имя события не может быть пустым');
        }
    };

    cancelEventButton.onclick = () => hideEventForm(start);
}

function hideEventForm(start) {
    eventForm.style.display = 'none';
    eventNameInput.value = '';
    eventDescriptionInput.value = '';
    eventConferenceUrlInput.value = '';

    const slots = Array.from(start.parentNode.children);
    slots.forEach(slot => {
        if (slot.classList.contains('time-slot')) {
            slot.style.backgroundColor = 'white';
        }
    });
}





function createEvent(start, end, eventName, description, conferenceUrl, id, attendees = []) {
    const event = document.createElement('div');
    event.className = 'event';
    event.textContent = eventName;

    // Запись дополнительных данных как атрибутов data-*
    event.dataset.id = id;  // Сохранение уникального идентификатора события
    event.dataset.description = description;
    event.dataset.conferenceUrl = conferenceUrl;
    event.dataset.attendees = JSON.stringify(attendees);

    const slots = Array.from(start.parentNode.children);
    const startIndex = slots.indexOf(start);
    const endIndex = slots.indexOf(end);

    const minIndex = Math.min(startIndex, endIndex);
    const maxIndex = Math.max(startIndex, endIndex);

    event.dataset.startDatetime = slots[minIndex].dataset.datetime;
    event.dataset.endDatetime = slots[maxIndex].dataset.datetime;

    const duration = maxIndex - minIndex + 1;
    event.style.height = (duration * 16.67) + 'px';
    event.style.top = (minIndex * 16.67) + 'px';

    // Привязываем событие клика для показа информации о событии
    event.onclick = () => showEventInfo(event);

    // Добавление события в подсказку 
    start.parentNode.appendChild(event);

    // Подобная логика для корректировки лэйаута элементов
    adjustEventLayout(start.parentNode);

    // Обратная настройка стиля временных слотов
    slots.forEach(slot => {
        if (slot.classList.contains('time-slot')) {
            slot.style.backgroundColor = 'white';
        }
    });
}


function showEventInfo(event) {
    currentlySelectedEvent = event;
    modalEventName.textContent = event.textContent;
    modalEventDescription.textContent = event.dataset.description || 'Нет описания';
    modalEventConferenceUrl.textContent = event.dataset.conferenceUrl ? 'Присоединиться: ' + event.dataset.conferenceUrl : 'Нет ссылки на конференцию';

    const startDatetime = event.dataset.startDatetime || 'N/A';
    const endDatetime = event.dataset.endDatetime || 'N/A';
    const eventDatetimeInfo = 'Дата и время: ' + startDatetime + ' - ' + endDatetime;
    const datetimeInfoParagraph = document.createElement('p');
    datetimeInfoParagraph.textContent = eventDatetimeInfo;

    const attendees = JSON.parse(event.dataset.attendees || '[]');
    
    // Обновление выпадающего списка участников
    const attendeesList = document.getElementById('attendeesList');
    attendeesList.innerHTML = ''; // Очищаем предыдущие опции
    attendees.forEach((attendee, index) => {
        const option = document.createElement('option');
        option.value = index;
        option.textContent = attendee;
        attendeesList.appendChild(option);
    });

    const modalContent = document.getElementById('modalContent');
    modalContent.innerHTML = '';
    modalContent.appendChild(datetimeInfoParagraph);

    eventInfoModal.style.display = 'block';

    closeModalButton.onclick = () => eventInfoModal.style.display = 'none';
    editEventButton.onclick = editEvent;
    deleteEventButton.onclick = deleteEvent;
    addAttendeeButton.onclick = addAttendee;
    removeAttendeeButton.onclick = removeAttendee;
    toggleAttendeesButton.onclick = toggleAttendeesVisibility;
}

function toggleAttendeesVisibility() {
    const attendeesContainer = document.getElementById('attendeesContainer');
    if (attendeesContainer.style.display === 'none') {
        attendeesContainer.style.display = 'block';
        toggleAttendeesButton.textContent = 'Спрятать участников';
    } else {
        attendeesContainer.style.display = 'none';
        toggleAttendeesButton.textContent = 'Показать участников';
    }
}

function addAttendee() {
    const attendeeName = prompt("Введите имя участника:");
    if (attendeeName) {
        const event = currentlySelectedEvent;
        let attendees = JSON.parse(event.dataset.attendees || '[]');
        attendees.push(attendeeName);
        event.dataset.attendees = JSON.stringify(attendees);

        // Обновление выпадающего списка
        showEventInfo(event);
    }
}

function removeAttendee() {
    const attendeesList = document.getElementById('attendeesList');
    const selectedOptionIndex = attendeesList.selectedIndex;
    if (selectedOptionIndex !== -1) {
        const event = currentlySelectedEvent;
        let attendees = JSON.parse(event.dataset.attendees || '[]');
        attendees.splice(selectedOptionIndex, 1);
        event.dataset.attendees = JSON.stringify(attendees);

        // Обновление выпадающего списка
        showEventInfo(event);
    } else {
        alert("Пожалуйста, выберите участника для удаления.");
    }
}

function adjustEventLayout(container) {
    const allEvents = Array.from(container.querySelectorAll('.event'));

    allEvents.forEach(function(currentEvent) {
        const overlappingEvents = allEvents.filter(function(ev) {
            const evTop = parseFloat(ev.style.top);
            const evBottom = evTop + parseFloat(ev.style.height);
            const currentTop = parseFloat(currentEvent.style.top);
            const currentBottom = currentTop + parseFloat(currentEvent.style.height);

            return (currentTop < evBottom && currentBottom > evTop);
        });

        const totalEvents = overlappingEvents.length;
        const widthPercentage = 100 / totalEvents;

        overlappingEvents.forEach(function(ev, index) {
            ev.style.width = 'calc(' + widthPercentage + '% - 5px)';
            ev.style.left = 'calc(' + (index * widthPercentage) + '%)';
        });
    });
}

function editEvent() {
    if (currentlySelectedEvent) {
        eventNameInput.value = currentlySelectedEvent.textContent;
        eventDescriptionInput.value = currentlySelectedEvent.dataset.description || '';
        eventConferenceUrlInput.value = currentlySelectedEvent.dataset.conferenceUrl || '';
        
        eventForm.style.display = 'block';
        eventInfoModal.style.display = 'none';

        saveEventButton.onclick = function() {
            if (eventNameInput.value.trim() !== "") {
                currentlySelectedEvent.textContent = eventNameInput.value;
                currentlySelectedEvent.dataset.description = eventDescriptionInput.value;
                currentlySelectedEvent.dataset.conferenceUrl = eventConferenceUrlInput.value;
            }
            hideEventForm();
        };
    }
}

function deleteEvent() {
    console.log(currentlySelectedEvent.id)
    if (currentlySelectedEvent && currentlySelectedEvent.parentNode) {
        currentlySelectedEvent.parentNode.removeChild(currentlySelectedEvent);
        eventInfoModal.style.display = 'none';
        currentlySelectedEvent = null;
    }
}

//HUI

    document.getElementById('prevWeek').addEventListener('click', function() {
        currentDate.setDate(currentDate.getDate() - 7);
        renderWeek(getWeekStart(currentDate));
    });
    document.getElementById('nextWeek').addEventListener('click', function() {
        currentDate.setDate(currentDate.getDate() + 7);
        renderWeek(getWeekStart(currentDate));
    });


    function renderHourLabels() {
        var hourLabelsElement = document.querySelector('.hour-labels');
        hourLabelsElement.innerHTML = '';
        for (var i = 0; i < 24; i++) {
            var hourBlock = document.createElement('div');
            var hourText = i < 10 ? '0' + i : i;
            hourBlock.textContent = hourText + ':00';
            hourLabelsElement.appendChild(hourBlock);
        }
    }

    renderHourLabels();

    console.log(getWeekStart(currentDate))
    renderWeek(getWeekStart(currentDate));
});


const AUTH_TOKEN = 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhYm9idXMyIiwiaWF0IjoxNzMzMTAxMzI1fQ.t28SmR3scu4X99z510-Tqa8NUqYRLP4rxtY4B202MrE';

// Function to fetch an event by ID
async function fetchEventById(eventId) {
    const response = await fetch(`http://localhost:8380/api/v2/events/${eventId}`, {
        method: 'GET',
        headers: {
            'Authorization': AUTH_TOKEN
        }
    });

    if (!response.ok) {
        throw new Error(`Ошибка HTTP: ${response.status}`);
    }

    return await response.json();
}

// Function to create a new event
async function createEventRequest(eventData) {
    console.log(eventData)
    const response = await fetch(`http://localhost:8380/api/v2/events`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': AUTH_TOKEN
        },
        body: JSON.stringify(eventData)
    });

    if (!response.ok) {
        throw new Error(`Ошибка HTTP: ${response.status}`);
    }

    return await response;
}

// Function to update an existing event
async function updateEvent(updateEventRequest) {
    const response = await fetch(`http://localhost:8380/api/v2/events`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': AUTH_TOKEN
        },
        body: JSON.stringify(updateEventRequest)
    });

    if (!response.ok) {
        throw new Error(`Ошибка HTTP: ${response.status}`);
    }

    return await response.json();
}

// Function to delete an event
async function deleteEvent(eventId) {
    const response = await fetch(`http://localhost:8380/api/v2/events/${eventId}`, {
        method: 'DELETE',
        headers: {
            'Authorization': AUTH_TOKEN
        }
    });

    if (!response.ok) {
        throw new Error(`Ошибка HTTP: ${response.status}`);
    }

    return await response.json();
}

// Function to fetch all user events
async function fetchAllUserEvents(userUid, from, to) {
    const url = `http://localhost:8380/api/v2/events/users/${userUid}&${encodeURIComponent(from)}&${encodeURIComponent(to)}`;

    const response = await fetch(url, {
        method: 'GET',
        headers: {
            'Authorization': AUTH_TOKEN
        }
    });

    if (!response.ok) {
        throw new Error(`Ошибка HTTP: ${response.status}`);
    }

    const eventViewList = await response.json();

    const frontendEvents = eventViewList.eventViews.map(eventView => ({
        id: eventView.id,
        title: eventView.title,
        from: new Date(eventView.from),
        to: new Date(eventView.to)
    }));

    return frontendEvents;
}