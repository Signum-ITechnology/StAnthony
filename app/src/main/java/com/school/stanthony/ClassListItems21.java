package com.school.stanthony;

import java.util.ArrayList;

public class ClassListItems21 {
    public String srno,question,answer,correct;
    ArrayList<ClassListItems9> arraylist;

    public ClassListItems21(String srno, String question, String answer, String correct)
    {
        this.srno = srno;
        this.question = question;
        this.answer=answer;
        this.correct=correct;
    }

    public String getSrno() {
        return srno;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public String getCorrect() {
        return correct;
    }


}
