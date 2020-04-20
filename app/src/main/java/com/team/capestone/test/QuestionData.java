package com.team.capestone.test;

import java.util.ArrayList;
import java.util.List;

public class QuestionData {

    private static QuestionData insatnce;
    private List<QuestionBean> beans;
    public QuestionData() {
        beans = new ArrayList<>();
    }

    public synchronized static QuestionData getInstance() {
        if (insatnce == null){
            insatnce = new QuestionData();
        }
        return insatnce;
    }

    public QuestionBean get(int pos){
        return beans.get(pos);
    }

    public void  set(int index, QuestionBean bean){
        beans.set(index, bean);
    }


    public void addAll(List<QuestionBean> ques) {
        beans.clear();
        beans.addAll(ques);
    }

    public List<QuestionBean> getList(){
        return beans;
    }
}
