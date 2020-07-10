package com.predictive;

import javax.swing.*;
import java.awt.*;
public class PredictiveJFrame extends JFrame {
    PredictiveJpanel prejpanel=new PredictiveJpanel();
    public PredictiveJFrame(){
        super();
        this.setTitle("预测分析法");//设置窗体的标题
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//设置窗体退出时操作
        this.add(prejpanel,BorderLayout.CENTER);
        this.setBounds(100,100,800,500);
        this.setVisible(true);
    }
    public static void main(String args[]){
        PredictiveJFrame jframe=new PredictiveJFrame();
    }

}
