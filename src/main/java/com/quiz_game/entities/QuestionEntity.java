package com.quiz_game.entities;

public class QuestionEntity extends BaseEntity {
    private TrackEntity track;
    private String question;
    private int answer;
    private boolean answerRight;
    private int score;

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public TrackEntity getTrack() {
        return track;
    }

    public void setTrack(TrackEntity track) {
        this.track = track;
    }
    public boolean isAnswerRight() {
        return answerRight;
    }

    public void setAnswerRight(boolean answerRight) {
        this.answerRight = answerRight;
    }
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
