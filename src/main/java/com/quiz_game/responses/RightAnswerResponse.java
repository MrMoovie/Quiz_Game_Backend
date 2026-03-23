package com.quiz_game.responses;

public class RightAnswerResponse extends BasicResponse {
    private final boolean rightAnswer;

    public RightAnswerResponse(boolean rightAnswer) {
        super(true, null);
        this.rightAnswer = rightAnswer;
    }

    public boolean isRightAnswer() {
        return rightAnswer;
    }
}
