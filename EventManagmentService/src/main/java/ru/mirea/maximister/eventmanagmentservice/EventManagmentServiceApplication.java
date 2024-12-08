package ru.mirea.maximister.eventmanagmentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
})
public class EventManagmentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventManagmentServiceApplication.class, args);
    }

}
