package com.shata.myeducationapp.Model.ModelExams;

import java.io.Serializable;

public class ModelQuestion implements Serializable {

    int questionID;
    String questionText;
    String choice_1 , choice_2 , choice_3, choice_4;
    int answerNumber;
    int choiceNumber;

    public ModelQuestion() {
    }

    public ModelQuestion(int questionID, String questionText, String choice_1, String choice_2, String choice_3, String choice_4, int answerNumber, int choiceNumber) {
        this.questionID = questionID;
        this.questionText = questionText;
        this.choice_1 = choice_1;
        this.choice_2 = choice_2;
        this.choice_3 = choice_3;
        this.choice_4 = choice_4;
        this.answerNumber = answerNumber;
        this.choiceNumber = choiceNumber;
    }

    public int getQuestionID() {
        return questionID;
    }

    public void setQuestionID(int questionID) {
        this.questionID = questionID;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getChoice_1() {
        return choice_1;
    }

    public void setChoice_1(String choice_1) {
        this.choice_1 = choice_1;
    }

    public String getChoice_2() {
        return choice_2;
    }

    public void setChoice_2(String choice_2) {
        this.choice_2 = choice_2;
    }

    public String getChoice_3() {
        return choice_3;
    }

    public void setChoice_3(String choice_3) {
        this.choice_3 = choice_3;
    }

    public String getChoice_4() {
        return choice_4;
    }

    public void setChoice_4(String choice_4) {
        this.choice_4 = choice_4;
    }

    public int getAnswerNumber() {
        return answerNumber;
    }

    public void setAnswerNumber(int answerNumber) {
        this.answerNumber = answerNumber;
    }

    public int getChoiceNumber() {
        return choiceNumber;
    }

    public void setChoiceNumber(int choiceNumber) {
        this.choiceNumber = choiceNumber;
    }

    @Override
    public String toString() {
        return "ModelQuestion{" +
                "questionID=" + questionID +
                ", questionText='" + questionText + '\'' +
                ", choice_1='" + choice_1 + '\'' +
                ", choice_2='" + choice_2 + '\'' +
                ", choice_3='" + choice_3 + '\'' +
                ", choice_4='" + choice_4 + '\'' +
                ", answerNumber=" + answerNumber +
                ", choiceNumber=" + choiceNumber +
                '}';
    }
}
