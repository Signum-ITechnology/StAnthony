package com.school.stanthony;

import java.util.ArrayList;

public class ClassListItems8 {
    public String srno,question,opta,optb,optc,optd,correct;
    ArrayList<ClassListItems8> arraylist;

    public ClassListItems8(String srno, String question, String opta, String optb, String optc, String optd, String correct)
    {
        this.srno = srno;
        this.question = question;
        this.opta = opta;
        this.optb=optb;
        this.optc=optc;
        this.optd=optd;
        this.correct=correct;
    }

    public String getSrno() {
        return srno;
    }

    public String getQuestion() {
        return question;
    }

    public String getOpta() {
        return opta;
    }

    public String getOptb() {
        return optb;
    }

    public String getOptc() {
        return optc;
    }

    public String getOptd() {
        return optd;
    }

    public String getCorrect() {
        return correct;
    }

}
