package com.shata.myeducationapp.Model.Student;

import java.io.Serializable;

public class ModelStudentHomeWork implements Serializable {

    String professorID ,homeworkID, homeworkName, homeworkPDFLink;

    public ModelStudentHomeWork() {
    }

    public ModelStudentHomeWork(String professorID, String homeworkID, String homeworkName, String homeworkPDFLink) {
        this.professorID = professorID;
        this.homeworkID = homeworkID;
        this.homeworkName = homeworkName;
        this.homeworkPDFLink = homeworkPDFLink;
    }

    public String getProfessorID() {
        return professorID;
    }

    public void setProfessorID(String professorID) {
        this.professorID = professorID;
    }

    public String getHomeworkID() {
        return homeworkID;
    }

    public void setHomeworkID(String homeworkID) {
        this.homeworkID = homeworkID;
    }

    public String getHomeworkName() {
        return homeworkName;
    }

    public void setHomeworkName(String homeworkName) {
        this.homeworkName = homeworkName;
    }

    public String getHomeworkPDFLink() {
        return homeworkPDFLink;
    }

    public void setHomeworkPDFLink(String homeworkPDFLink) {
        this.homeworkPDFLink = homeworkPDFLink;
    }
}
