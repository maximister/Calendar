package ru.mirea.maximister.notificator.service;

import org.springframework.stereotype.Service;
import ru.mirea.maximister.notificator.model.NotificationData;

import java.util.Date;

@Service
public class MailTemplateService {
    private static final String CREATE_TEMPLATE =
            """
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Новая встреча запланирована</title>
    <style>
        body { font-family: Arial, sans-serif; }
        .container { max-width: 600px; margin: 0 auto; border: 1px solid #eee; padding: 20px; }
        .header { background-color: #4CAF50; color: white; padding: 10px 0; text-align: center; }
        .body { margin-top: 20px; }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h2>Новая встреча запланирована</h2>
        </div>
        <div class="body">
            <p>Уважаемый(ая) %s,</p>
            <p>Ваша встреча "<strong%s</strong>" успешно запланирована на %s.</p>
            <p>С уважением,<br/>Ваш Организатор</p>
        </div>
    </div>
</body>
</html>
""";

    private static final String UPDATE_TEMPLATE =
            """
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Встреча обновлена</title>
    <style>
        body { font-family: Arial, sans-serif; }
        .container { max-width: 600px; margin: 0 auto; border: 1px solid #eee; padding: 20px; }
        .header { background-color: #2196F3; color: white; padding: 10px 0; text-align: center; }
        .body { margin-top: 20px; }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h2>Встреча обновлена</h2>
        </div>
        <div class="body">
            <p>Уважаемый(ая) %s,</p>
            <p>Ваша встреча "<strong>%s</strong>" была обновлена.</p>
            <p>С уважением,<br/>Ваш Организатор</p>
        </div>
    </div>
</body>
</html>
""";

    private final static String DELETE_TEMPLATE =
            """
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Встреча отменена</title>
    <style>
        body { font-family: Arial, sans-serif; }
        .container { max-width: 600px; margin: 0 auto; border: 1px solid #eee; padding: 20px; }
        .header { background-color: #f44336; color: white; padding: 10px 0; text-align: center; }
        .body { margin-top: 20px; }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h2>Встреча отменена</h2>
        </div>
        <div class="body">
            <p>Уважаемый(ая) %s,</p>
            <p>К сожалению, мы должны сообщить вам, что ваша встреча "<strong>%s</strong>" была отменена.</p>
            <p>С уважением,<br/>Ваш Организатор</p>
        </div>
    </div>
</body>
</html>
""";

    private static final String REMINDER_TEMPLATE =
            """
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Напоминание о встрече</title>
    <style>
        body { font-family: Arial, sans-serif; }
        .container { max-width: 600px; margin: 0 auto; border: 1px solid #eee; padding: 20px; }
        .header { background-color: #ff9800; color: white; padding: 10px 0; text-align: center; }
        .body { margin-top: 20px; }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h2>Напоминание о встрече</h2>
        </div>
        <div class="body">
            <p>Уважаемый(ая) %s,</p>
            <p>Напоминаем, что встреча "<strong>%s</strong>" начнется через 15 минут.</p>
            <p>С уважением,<br/>Ваш Организатор</p>
        </div>
    </div>
</body>
</html>
""";

    public String getTemplate(NotificationData data) {
        return switch (data.notificationType()) {
            case CREATE ->  getCreateTemplate(data);
            case DELETE -> getDeleteTemplate(data);
            case UPDATE -> getUpdateTemplate(data);
            case REMINDER -> getReminderTemplate(data);
            default -> throw new IllegalStateException();
        };
    }

    private String getCreateTemplate(NotificationData data) {
        return String.format(
                CREATE_TEMPLATE,
                data.user().getName(),
                data.event().getTitle(),
                Date.from(data.event().getFrom())
        );
    }

    private String getUpdateTemplate(NotificationData data) {
        return String.format(
                UPDATE_TEMPLATE,
                data.user().getName(),
                data.event().getTitle()
        );
    }

    private String getDeleteTemplate(NotificationData data) {
        return String.format(
                DELETE_TEMPLATE,
                data.user().getName(),
                data.event().getTitle()
        );
    }

    private String getReminderTemplate(NotificationData data) {
        return String.format(
                REMINDER_TEMPLATE,
                data.user().getName(),
                data.event().getTitle()
        );
    }
}
