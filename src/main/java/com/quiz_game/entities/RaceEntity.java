package com.quiz_game.entities;

public class RaceEntity extends BaseEntity{
    private SessionEntity session;
    private StudentEntity student;
    private int score;
    private int path;
    // 0 - regular 1 - highway 2 - dirt road
    private int pathChance;
    private int powerUp;
    private int position;

    public SessionEntity getSession() {
        return session;
    }

    public void setSession(SessionEntity session) {
        this.session = session;
    }

    public StudentEntity getStudent() {
        return student;
    }

    public void setStudent(StudentEntity student) {
        this.student = student;
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

    public int getPathChance() {
        return pathChance;
    }

    public void setPathChance(int pathChance) {
        this.pathChance = pathChance;
    }

    public int getPowerUp() {
        return powerUp;
    }

    public void setPowerUp(int powerUp) {
        this.powerUp = powerUp;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
