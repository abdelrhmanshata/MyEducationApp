package com.shata.myeducationapp.Model.ModelExams;

import java.io.Serializable;

public class ModelExamID implements Serializable {

    String ExamID;
    String examName;
    String examDate;
    int examDegree;
    public ModelExamID() {
    }

    public ModelExamID(String examID, String examName, String examDate, int examDegree) {
        ExamID = examID;
        this.examName = examName;
        this.examDate = examDate;
        this.examDegree = examDegree;
    }

    public String getExamID() {
        return ExamID;
    }

    public void setExamID(String examID) {
        ExamID = examID;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public String getExamDate() {
        return examDate;
    }

    public void setExamDate(String examDate) {
        this.examDate = examDate;
    }

    public int getExamDegree() {
        return examDegree;
    }

    public void setExamDegree(int examDegree) {
        this.examDegree = examDegree;
    }
}
