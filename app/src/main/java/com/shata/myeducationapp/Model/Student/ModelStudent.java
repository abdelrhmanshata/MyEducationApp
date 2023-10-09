package com.shata.myeducationapp.Model.Student;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ModelStudent implements Serializable {

    String studentID,
            studentName,
            studentEmail;

    List<ModelStudentExam> studentExams = new ArrayList<>();
    List<ModelStudentHomeWork> studentHomeWorks = new ArrayList<>();

    List<String> studentTasksID = new ArrayList<>();

    public ModelStudent() {
    }

    public ModelStudent(String studentID, String studentName, String studentEmail) {
        this.studentID = studentID;
        this.studentName = studentName;
        this.studentEmail = studentEmail;
    }

    public ModelStudent(String studentID, String studentName, String studentEmail, List<ModelStudentExam> studentExams, List<ModelStudentHomeWork> studentHomeWorks, List<String> studentTasksID) {
        this.studentID = studentID;
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.studentExams = studentExams;
        this.studentHomeWorks = studentHomeWorks;
        this.studentTasksID = studentTasksID;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public List<ModelStudentExam> getStudentExams() {
        return studentExams;
    }

    public void setStudentExams(List<ModelStudentExam> studentExams) {
        this.studentExams = studentExams;
    }

    public List<ModelStudentHomeWork> getStudentHomeWorks() {
        return studentHomeWorks;
    }

    public void setStudentHomeWorks(List<ModelStudentHomeWork> studentHomeWorks) {
        this.studentHomeWorks = studentHomeWorks;

    }

    public List<String> getStudentTasksID() {
        return studentTasksID;
    }

    public void setStudentTasksID(List<String> studentTasksID) {
        this.studentTasksID = studentTasksID;
    }

    public void addStudentExam(ModelStudentExam studentExam) {
        this.studentExams.add(studentExam);
    }

    public void addStudentHomeWork(ModelStudentHomeWork studentHomeWork) {
        this.studentHomeWorks.add(studentHomeWork);
    }

    public void addStudentTasksID(String ID) {
        this.studentTasksID.add(ID);
    }

    public boolean taskIDIsFound(String s) {
        return studentTasksID.contains(s);
    }
}
