package com.quiz_game.responses;

import com.quiz_game.entities.QuestionTemplateEntity;

import javax.print.attribute.standard.RequestingUserName;

public class RightAnswerResponse extends BasicResponse {
    private final boolean rightAnswer;
    private final QuestionTemplateEntity newQuestion;

    public RightAnswerResponse(boolean rightAnswer, QuestionTemplateEntity newQuestion) {
        super(true, null);
        this.rightAnswer = rightAnswer;
        this.newQuestion = newQuestion;
    }

    public boolean isRightAnswer() {
        return rightAnswer;
    }
}
