
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
async function createEvent(eventData) {
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

    return await response.json();
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