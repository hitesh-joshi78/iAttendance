package com.example.iattendance.Dashboard;

public class Subject_modal {
    String subj_type, subj_name, class_in_no, div_txt, batch_txt, sem_txt, year_txt;

    public Subject_modal() {
    }

    public Subject_modal(String subj_type, String subj_name, String class_in_no, String div_txt, String batch_txt, String sem_txt, String year_txt) {
        this.subj_type = subj_type;
        this.subj_name = subj_name;
        this.class_in_no = class_in_no;
        this.div_txt = div_txt;
        this.batch_txt = batch_txt;
        this.sem_txt = sem_txt;
        this.year_txt = year_txt;
    }

    public String getSubj_type() {
        return subj_type;
    }

    public void setSubj_type(String subj_type) {
        this.subj_type = subj_type;
    }

    public String getSubj_name() {
        return subj_name;
    }

    public void setSubj_name(String subj_name) {
        this.subj_name = subj_name;
    }

    public String getClass_in_no() {
        return class_in_no;
    }

    public void setClass_in_no(String class_in_no) {
        this.class_in_no = class_in_no;
    }

    public String getDiv_txt() {
        return div_txt;
    }

    public void setDiv_txt(String div_txt) {
        this.div_txt = div_txt;
    }

    public String getBatch_txt() {
        return batch_txt;
    }

    public void setBatch_txt(String batch_txt) {
        this.batch_txt = batch_txt;
    }

    public String getSem_txt() {
        return sem_txt;
    }

    public void setSem_txt(String sem_txt) {
        this.sem_txt = sem_txt;
    }

    public String getYear_txt() {
        return year_txt;
    }

    public void setYear_txt(String year_txt) {
        this.year_txt = year_txt;
    }
}
