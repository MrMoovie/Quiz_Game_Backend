package com.quiz_game.entities;

public class ActionEntity extends BaseEntity{
    private String actionName;
    private String actionOperation;

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getActionOperation() {
        return actionOperation;
    }

    public void setActionOperation(String actionOperation) {
        this.actionOperation = actionOperation;
    }


}
