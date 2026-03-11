package com.quiz_game.entities;

public class ClassStudentsEntity extends BaseEntity{
    private ClassroomEntity classroom;
    private StudentEntity student;

    public StudentEntity getStudent() {
        return student;
    }

    public void setStudent(StudentEntity student) {
        this.student = student;
    }
    public ClassroomEntity getClassroom() {
        return classroom;
    }

    public void setClassroom(ClassroomEntity classroom) {
        this.classroom = classroom;
    }
}
