package com.quiz_game.responses;

import com.quiz_game.entities.QuestionEntity;
import com.quiz_game.entities.QuestionTemplateEntity;

import java.util.Date;

public class QuestionResponse extends BasicResponse {
    private final QuestionEntity question;

    public QuestionResponse(QuestionEntity question) {
        super(true,null);
        this.question = question;
    }

    public QuestionEntity getQuestion() {
        return question;
    }
}
