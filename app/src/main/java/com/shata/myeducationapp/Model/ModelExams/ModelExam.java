package com.shata.myeducationapp.Model.ModelExams;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ModelExam implements Serializable {

    String ExamID;
    String examName;
    int examDegree;
    String examDate;
    List<ModelQuestion> questions = new ArrayList<>();

    public ModelExam() {
    }

    public ModelExam(String examID, String examName, int examDegree, String examDate, List<ModelQuestion> questions) {
        ExamID = examID;
        this.examName = examName;
        this.examDegree = examDegree;
        this.examDate = examDate;
        this.questions = questions;
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

    public int getExamDegree() {
        return examDegree;
    }

    public void setExamDegree(int examDegree) {
        this.examDegree = examDegree;
    }

    public String getExamDate() {
        return examDate;
    }

    public void setExamDate(String examDate) {
        this.examDate = examDate;
    }

    public List<ModelQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<ModelQuestion> questions) {
        this.questions = questions;
    }

    @Override
    public String  toString() {
        return "ModelExam{" +
                "ExamID=" + ExamID +
                ", examName='" + examName + '\'' +
                ", examDegree=" + examDegree +
                ", examDate='" + examDate + '\'' +
                ", questions=" + questions +
                '}';
    }
}
