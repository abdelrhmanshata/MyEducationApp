package com.shata.myeducationapp.Model.ModelHomeWork;

import java.io.Serializable;

public class ModelHomeWork implements Serializable {

    String HW_ID,
            HW_Name,
            HW_StartDate,
            HW_EndDate,
            HW_PDF_URL;

    boolean isAllowed;

    public ModelHomeWork() {
    }

    public ModelHomeWork(String HW_ID, String HW_Name, String HW_StartDate, String HW_EndDate, String HW_PDF_URL, boolean isAllowed) {
        this.HW_ID = HW_ID;
        this.HW_Name = HW_Name;
        this.HW_StartDate = HW_StartDate;
        this.HW_EndDate = HW_EndDate;
        this.HW_PDF_URL = HW_PDF_URL;
        this.isAllowed = isAllowed;
    }

    public String getHW_ID() {
        return HW_ID;
    }

    public void setHW_ID(String HW_ID) {
        this.HW_ID = HW_ID;
    }

    public String getHW_Name() {
        return HW_Name;
    }

    public void setHW_Name(String HW_Name) {
        this.HW_Name = HW_Name;
    }
    public String getHW_StartDate() {
        return HW_StartDate;
    }

    public void setHW_StartDate(String HW_StartDate) {
        this.HW_StartDate = HW_StartDate;
    }

    public String getHW_EndDate() {
        return HW_EndDate;
    }

    public void setHW_EndDate(String HW_EndDate) {
        this.HW_EndDate = HW_EndDate;
    }

    public String getHW_PDF_URL() {
        return HW_PDF_URL;
    }

    public void setHW_PDF_URL(String HW_PDF_URL) {
        this.HW_PDF_URL = HW_PDF_URL;
    }

    public boolean isAllowed() {
        return isAllowed;
    }

    public void setAllowed(boolean allowed) {
        isAllowed = allowed;
    }
}
