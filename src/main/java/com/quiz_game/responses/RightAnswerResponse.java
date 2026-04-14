package com.quiz_game.responses;

import com.quiz_game.entities.QuestionEntity;

public class RightAnswerResponse extends BasicResponse {
    private final boolean rightAnswer;
    private final QuestionEntity question;

    public RightAnswerResponse(boolean rightAnswer, QuestionEntity question) {
        super(true, null);
        this.rightAnswer = rightAnswer;
        this.question = question;
    }

    public boolean isRightAnswer() {
        return rightAnswer;
    }

    public QuestionEntity getQuestion() {
        return question;
    }
}
