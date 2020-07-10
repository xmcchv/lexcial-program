package com.morphology;

public class stzb {
    private double a=0.45;
    private double b=0.55;
    double probability(double a,double b){
        double a1=a*a*a*a;
        System.out.println("发动四次概率为:"+a1);
        double a2=4*a*a*a*b+6*a*a*a*b*b;
        System.out.println("发动三次概率为:"+a2);
        double a3=10*a*a*b*b*b+5*a*a*b*b*b*b;
        System.out.println("发动两次次概率为:"+a3);
        double a4=6*a*b*b*b*b*b+a*b*b*b*b*b*b;
        System.out.println("发动一次概率为:"+a4);
        double a5=b*b*b*b*b*b*b*b;
        System.out.println("不发动概率为:"+a5);
        return (a1*4+a2*3+a3*2+a4+a5);
    }
/*
* 发动四次概率为:0.09150625000000004
发动三次概率为:0.5016206250000002
发动两次次概率为:0.3376750781250001
发动一次概率为:0.06546135234375
不发动概率为:0.0016815125390625006
8回合发动的数学期望为:2.613379896132813
* */
    public static void main(String args[]){
        stzb st=new stzb();
        System.out.println("8回合发动的数学期望为:"+st.probability(0.45,0.55));
    }
}
