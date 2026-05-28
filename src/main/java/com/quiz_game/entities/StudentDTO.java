package com.quiz_game.entities;

public class StudentDTO {
    private int id;
    private String fullName;
    private int score;
    private int path;
    private int powerUp;

    public StudentDTO(int id, String fullname, int score, int path, int powerUp) {
        this.id = id;
        this.fullName = fullname;
        this.score = score;
        this.path = path;
        this.powerUp = powerUp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullname() {
        return fullName;
    }

    public void setFullname(String fullname) {
        this.fullName = fullname;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getPath() {
        return path;
    }

    public void setPath(int path) {
        this.path = path;
    }

    public int getPowerUp() {
        return powerUp;
    }

    public void setPowerUp(int powerUp) {
        this.powerUp = powerUp;
    }
}
