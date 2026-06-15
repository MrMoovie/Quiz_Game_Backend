package com.quiz_game.controllers;

import com.quiz_game.entities.*;
import com.quiz_game.responses.*;
import com.quiz_game.service.Persist;
import com.quiz_game.utils.GeneralUtils;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.*;
import javax.annotation.PostConstruct;

import java.awt.*;
import java.util.*;
import java.util.List;

import static com.quiz_game.utils.Constants.*;
import static com.quiz_game.utils.Errors.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.transaction.annotation.Transactional;

@RestController
public class TEST {
    @Autowired
    private Persist persist;
    @Autowired
    private DataSource dataSource;

    @PostConstruct
    public void init() {
    }

    private Point generateDynamicNumbers(String operation, String level) {
        Random random = new Random();
        int num1 = 0, num2 = 0;

        switch (operation) {
            case "+":
                if ("Easy".equals(level)) {
                    num1 = random.nextInt(9) + 1; // 1 to 9
                    num2 = random.nextInt(9) + 1;
                } else if ("Medium".equals(level)) {
                    num1 = random.nextInt(90) + 10; // 10 to 99
                    num2 = random.nextInt(90) + 10;
                } else { // Hard
                    num1 = random.nextInt(900) + 100; // 100 to 999
                    num2 = random.nextInt(900) + 100;
                }
                break;

            case "-":
                if ("Easy".equals(level)) {
                    num1 = random.nextInt(9) + 2; // 2 to 10
                    num2 = random.nextInt(num1 - 1) + 1; // Assures positive answer
                } else if ("Medium".equals(level)) {
                    num1 = random.nextInt(90) + 10; // 10 to 99
                    num2 = random.nextInt(num1 - 1) + 1;
                } else { // Hard
                    num1 = random.nextInt(900) + 100; // 100 to 999
                    num2 = random.nextInt(num1 - 1) + 1;
                }
                break;

            case "*":
                if ("Easy".equals(level)) {
                    // Focus: Core times tables up to 10x10
                    num1 = random.nextInt(9) + 2; // 2 to 10
                    num2 = random.nextInt(9) + 2; // 2 to 10
                } else if ("Medium".equals(level)) {
                    // Focus: Small 2-digit numbers by a single digit (e.g., 14 x 4 or 25 x 3)
                    num1 = random.nextInt(15) + 11; // 11 to 25
                    num2 = random.nextInt(5) + 2;   // 2 to 6
                } else { // Hard
                    // Focus: Harder 2-digit by 1-digit, forcing mental grouping (e.g., 84 x 7)
                    num1 = random.nextInt(75) + 15; // 15 to 89
                    num2 = random.nextInt(7) + 3;   // 3 to 9
                }
                break;

            case "/":
                int divisor, answer;
                if ("Easy".equals(level)) {
                    // Focus: Clean reversals of basic times tables (e.g., 42 / 6 = 7)
                    divisor = random.nextInt(9) + 2; // 2 to 10
                    answer = random.nextInt(9) + 2;  // 2 to 10
                } else if ("Medium".equals(level)) {
                    // Focus: Divisor is a single digit, answer is a clean teen (e.g., 72 / 4 = 18)
                    divisor = random.nextInt(7) + 2;  // 2 to 8
                    answer = random.nextInt(10) + 11; // 11 to 20
                } else { // Hard
                    // Focus: Standard middle school limits (e.g., 144 / 12 = 12 or 225 / 9 = 25)
                    divisor = random.nextInt(11) + 2; // 2 to 12
                    answer = random.nextInt(25) + 11; // 11 to 35
                }
                num1 = divisor * answer;
                num2 = divisor;
                break;
        }

        return new Point(num1, num2);
    }

    @RequestMapping("/getNewQuestion-test")
    public BasicResponse getNewQuestionTest(int pathChoice) {
        String level = pathChoice == 0 ? "Medium" : pathChoice == 1 ? "Easy" : "Hard";

        QuestionTemplateEntity questionTemplate = persist.getRandomTemplate(level);

        ObjectEntity object = persist.getRandomObject();

        ActionEntity action = persist.getRandomAction();

        NameEntity name = persist.getRandomName();

        Random random = new Random();

//        List<Point> points = listOfPoints.get(action.getActionOperation()).get(level);
//        int index = random.nextInt(points.size());
//        int num1 = points.get(index).x;
//        int num2 = points.get(index).y;
        Point generatedNumbers = generateDynamicNumbers(action.getActionOperation(), level);
        int num1 = generatedNumbers.x;
        int num2 = generatedNumbers.y;


        String newQuestionTemplate = questionTemplate.getTemplate()
                .replace("{name}", name.getName())
                .replace("{object}", object.getObjectName())
                .replace("{action}", action.getActionName())
                .replace("{NUM1}", String.valueOf(num1))
                .replace("{NUM2}", String.valueOf(num2));


        int answer = switch (action.getActionOperation()) {
            case "+" -> num1 + num2;
            case "-" -> num1 - num2;
            case "*" -> num1 * num2;
            case "/" -> num1 / num2;
            default -> 0;
        };


        QuestionEntity newQuestion = new QuestionEntity();

        newQuestion.setQuestion(newQuestionTemplate);
        newQuestion.setAnswer(answer);

        newQuestion.setCreationDate(new Date());
        int score = pathChoice == 0 ? MEDIUM_Q_SCORE : pathChoice == 1 ? EASY_Q_SCORE : HARD_Q_SCORE;
        newQuestion.setScore(score);
        persist.save(newQuestion);

        return new QuestionResponse(newQuestion);
    }






}
