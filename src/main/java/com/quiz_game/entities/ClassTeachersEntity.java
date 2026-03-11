package com.quiz_game.entities;

public class ClassTeachersEntity extends BaseEntity{
    private ClassroomEntity classroom;

    private TeacherEntity teacher;

    public TeacherEntity getTeacher() {
        return teacher;
    }

    public void setTeacher(TeacherEntity teacher) {
        this.teacher = teacher;
    }
    public ClassroomEntity getClassroom() {
        return classroom;
    }

    public void setClassroom(ClassroomEntity classroom) {
        this.classroom = classroom;
    }
}
