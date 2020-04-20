package com.team.capestone.test;

public class QuestionBean {
    private int qNo;
    private String ques;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String correctAns;
    private String selectedAns;

    public QuestionBean(int qNo, String ques, String optionA,
                        String optionB, String optionC, String optionD, String correctAns) {
        this.qNo = qNo;
        this.ques = ques;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctAns = correctAns;
    }

    public int getqNo() {
        return qNo;
    }

    public String getQues() {
        return ques;
    }

    public String getOptionA() {
        return optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public String getSelectedAns() {
        return selectedAns;
    }

    public void setSelectedAns(String selectedAns) {
        this.selectedAns = selectedAns;
    }

    public String getCorrectAns() {
        return correctAns;
    }
}
