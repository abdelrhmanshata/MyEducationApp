package com.shata.myeducationapp.Model.ModelLecuter;

import java.io.Serializable;

public class ModelLecuter implements Serializable {

    String lecID, lecName, lecDate, lecURL, lecUrlPdf;

    public ModelLecuter() {
    }

    public ModelLecuter(String lecID, String lecName, String lecDate, String lecURL, String lecUrlPdf) {
        this.lecID = lecID;
        this.lecName = lecName;
        this.lecDate = lecDate;
        this.lecURL = lecURL;
        this.lecUrlPdf = lecUrlPdf;
    }

    public String getLecID() {
        return lecID;
    }

    public void setLecID(String lecID) {
        this.lecID = lecID;
    }

    public String getLecName() {
        return lecName;
    }

    public void setLecName(String lecName) {
        this.lecName = lecName;
    }

    public String getLecDate() {
        return lecDate;
    }

    public void setLecDate(String lecDate) {
        this.lecDate = lecDate;
    }

    public String getLecURL() {
        return lecURL;
    }

    public void setLecURL(String lecURL) {
        this.lecURL = lecURL;
    }

    public String getLecUrlPdf() {
        return lecUrlPdf;
    }

    public void setLecUrlPdf(String lecUrlPdf) {
        this.lecUrlPdf = lecUrlPdf;
    }
}
