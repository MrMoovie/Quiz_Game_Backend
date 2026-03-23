package com.quiz_game.responses;

import com.quiz_game.entities.QuestionTemplateEntity;

import java.util.Date;

public class QuestionTemplateResponse extends BasicResponse {
    private final String questionTemplate, difficultyLevel;
    private final Integer maxNumber;
    private final Date date;
    private final boolean deleted;
    public QuestionTemplateResponse(boolean success, QuestionTemplateEntity question, Integer errorCode) {
        super(success,errorCode);
        this.questionTemplate =  question.getTemplate();
        this.difficultyLevel = question.getDifficultyLevel();
        this.maxNumber = question.getMaxNumber();
        this.date = question.getCreationDate();
        this.deleted = question.isDeleted();
    }

    public String getQuestionTemplate() {
        return questionTemplate;
    }

    public String getDifficultyLevel() {
        return difficultyLevel;
    }

    public Integer getMaxNumber() {
        return maxNumber;
    }
    public Date getDate() {
        return date;
    }

    public boolean isDeleted() {
        return deleted;
    }


}
