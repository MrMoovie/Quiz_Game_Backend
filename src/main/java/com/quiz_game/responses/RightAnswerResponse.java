package com.quiz_game.responses;

import com.quiz_game.entities.QuestionEntity;

public class RightAnswerResponse extends BasicResponse {
    private final boolean rightAnswer;
    private final QuestionEntity newQuestion;

    public RightAnswerResponse(boolean rightAnswer, QuestionEntity newQuestion) {
        super(true, null);
        this.rightAnswer = rightAnswer;
        this.newQuestion = newQuestion;
    }

    public boolean isRightAnswer() {
        return rightAnswer;
    }

    public QuestionEntity getNewQuestion() {
        return newQuestion;
    }
}
