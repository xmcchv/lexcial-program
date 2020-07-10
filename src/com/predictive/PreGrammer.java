package com.predictive;

import java.util.Stack;

public class PreGrammer {
    private String[] letter;     //划分好的字符数组
    private int count=0;         //记录字符位置
    private int stackcount=0;    //记录栈的位置
    private int length;          //字符数组长度
    private Stack liststack;      //划分好的字符栈
    private boolean grammerflag=true;//是否为该文法的语句
    public boolean getgrammerflag(){
        return this.grammerflag;
    }
    public void setLetter(String[] letter) {
        this.letter = letter;
    }

}
