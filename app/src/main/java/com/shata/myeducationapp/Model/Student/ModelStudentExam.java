package com.shata.myeducationapp.Model.Student;

import java.io.Serializable;

public class ModelStudentExam implements Serializable {

    String professorID , examID, examName, examScore;

    public ModelStudentExam() {
    }

    public ModelStudentExam(String professorID, String examID, String examName, String examScore) {
        this.professorID = professorID;
        this.examID = examID;
        this.examName = examName;
        this.examScore = examScore;
    }

    public String getProfessorID() {
        return professorID;
    }

    public void setProfessorID(String professorID) {
        this.professorID = professorID;
    }

    public String getExamID() {
        return examID;
    }

    public void setExamID(String examID) {
        this.examID = examID;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public String getExamScore() {
        return examScore;
    }

    public void setExamScore(String examScore) {
        this.examScore = examScore;
    }
}
