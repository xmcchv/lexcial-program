package com.predictive;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PredictiveJpanel extends JPanel{
    private DefaultTableModel model;//表格模型
    public JTextArea grammerarea;    //文法输入框
    public JButton genter,greset,preenter;
    public JTextArea anatable;
    public JTextField anastring;
    public JTable selecttable;
    public JTable fftable;
    private prelexical l=new prelexical();
    public PredictiveJpanel(){
        super();
        grammerarea=new JTextArea(15,20);
        grammerarea.setText("E::=E+T|T\nT::=T*F|F\nF::=(E)|i");
        grammerarea.setLineWrap(true);
        grammerarea.setWrapStyleWord(true);
        genter=new JButton("确认");
        genter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String txt=grammerarea.getText();
                l.lexicalanalysis(txt);
                String str[][]=l.getFftable();
                String[] tmp={" ","first","follow"};
                String[][] select=l.getselecttable();
                String[] selectheader=l.getselecttableheader();
                model=new DefaultTableModel(str,tmp);
                fftable.setModel(model);

                selecttable.setModel(new DefaultTableModel(select,selectheader));
                l.init();
            }
        });
        greset=new JButton("重置");
        greset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                grammerarea.setText("");
            }
        });
        preenter=new JButton("确定");
        preenter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                l.lexicalanalysis(grammerarea.getText());
                l.predictivelabel(anastring.getText());
                String selecttable1=l.getAnatable();
                anatable.setText(selecttable1);
            }
        });
        selecttable=new JTable();
        fftable=new JTable();
        anatable=new JTextArea(15,20);

        anastring=new JTextField("+*(i)");
        anastring.setColumns(10);

        this.setLayout(new GridLayout(1,2,5,5));
        this.setBounds(100,100,800,500);
        JPanel p1=new JPanel();
        p1.setLayout(new BoxLayout(p1,BoxLayout.Y_AXIS));
        p1.add(new JLabel("请输入文法："));
        p1.add(new JScrollPane(grammerarea));
        JPanel p11=new JPanel();
        p11.add(genter);
        p11.add(greset);
        p1.add(p11);
        p1.add(new JLabel("文法的first-follow集为："));
        p1.add(new JScrollPane(fftable));
        p1.setVisible(true);
//        p1.setBounds(0,0,200,200);
        this.add(p1);
        JPanel p2=new JPanel();
        p2.setLayout(new BoxLayout(p2,BoxLayout.Y_AXIS));
        p2.add(new JLabel("预测分析表："));
        p2.add(new JScrollPane(selecttable));
        JPanel p22=new JPanel();
        p22.add(anastring);
        p22.add(preenter);
        p2.add(p22);
        p2.add(new JLabel("分析过程如下："));
        p2.add(new JScrollPane(anatable));
        this.add(p2);
        System.out.println(grammerarea.getText());
        p1.setBounds(210,0,200,200);
        p2.setVisible(true);
        this.setVisible(true);
    }
}
