package com.quiz_game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = HibernateJpaAutoConfiguration.class)
@EnableScheduling
public class Main {
    public static boolean applicationStarted = false;
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static long startTime;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Main.class, args);
        LOGGER.info("Application started.");
        applicationStarted = true;
        startTime = System.currentTimeMillis();
        System.out.println("hello");

    }

}

/*
    Login [*]
    SignUP [*]
    Menu [*]
    Game [!]
        fix the flow
        clear the input

        add SSE
        Add teachers side
    Issues:
        double join [*]
        add 'in progress' join for student when the game has already started
        make 'create race' real time for teacher
        display entry code [*]

        disable the button if the input is empty
 */
