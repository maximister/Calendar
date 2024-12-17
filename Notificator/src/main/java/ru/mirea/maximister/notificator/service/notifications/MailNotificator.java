package ru.mirea.maximister.notificator.service.notifications;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.mirea.maximister.notificator.model.Notification;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailNotificator implements Notificator {
    private final JavaMailSender mailSender;

    @Value("${app.mail.sender.enabled}")
    private boolean sendingEmailAvailable;

    @Override
    public void notify(Notification notification) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(notification.to());
        message.setSubject(notification.title());
        message.setText(notification.message());

        message.setFrom("your-email@gmail.com");

        if (sendingEmailAvailable) {
            mailSender.send(message);
        }

        log.info("Send email with title {} to {}", notification.title(), notification.to());
    }
}
