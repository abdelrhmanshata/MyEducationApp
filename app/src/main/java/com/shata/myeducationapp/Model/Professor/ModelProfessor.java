package com.shata.myeducationapp.Model.Professor;

import java.io.Serializable;

public class ModelProfessor implements Serializable {

    String professorID,
            professorName,
            professorEmail;

    public ModelProfessor() {
    }

    public ModelProfessor(String professorID, String professorName, String professorEmail) {
        this.professorID = professorID;
        this.professorName = professorName;
        this.professorEmail = professorEmail;
    }

    public String getProfessorID() {
        return professorID;
    }

    public void setProfessorID(String professorID) {
        this.professorID = professorID;
    }

    public String getProfessorName() {
        return professorName;
    }

    public void setProfessorName(String professorName) {
        this.professorName = professorName;
    }

    public String getProfessorEmail() {
        return professorEmail;
    }

    public void setProfessorEmail(String professorEmail) {
        this.professorEmail = professorEmail;
    }
}
