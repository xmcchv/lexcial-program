package com.morphology;

import java.util.Stack;

/*
*   @author:202171011
*   @time:2020/5/28
* */
public class Grammer{
    private String[] letter;     //存放字符
    private int count=0;         //记录字符位置
    private int stackcount=0;    //记录栈的位置
    private int length;          //字符数组长度
    private String[] strstack;   //划分好的字符数组
    private Stack liststack;      //划分好的字符栈
    private boolean grammerflag=true;//是否为该文法的语句
    public boolean getgrammerflag(){
        return this.grammerflag;
    }
    public int getLength() {
            return this.length;
        }
    public void setListstack(Stack liststack) {
        this.liststack = liststack;
    }
    public String[] getStrstack(){
        return this.strstack;
    }
    public int getStackcount(){
        return this.stackcount;
    }
    public int getCount(){
        return this.count;
    }
    public int getlength(){
        return this.length;
    }
    public int getStrstacklength() {
        return strstack.length;
    }
    public void init(){ //初始化
        letter=null;
        count=0;
        stackcount=0;
        length=0;
        strstack=null;
        liststack=null;
        grammerflag=true;
    }
    public void showerror(String a){
        System.out.println("\n[ERROR]: 缺少"+a+" ,导致语法分析失败！");
        this.grammerflag=false;
    }
    void cut(String str){ //分割字符
        length=str.length();
        letter=new String[length];
        for(int i=0;i<length;i++){
            letter[i]=str.substring(0,1);
            str=str.substring(1);
        }
        int size=this.liststack.size();
        strstack=new String[size];
        for(int i=size-1;i>=0;i--){
            strstack[i]=(String)this.liststack.get(i);
        }
    }
    /*
     *      对原来的文法进行修改使得先文法能够同时识别if语句和while语句 以及算术运算符，逻辑运算符
     *       S::=if A then S|while A do S|B     B::=EB'     B'::= :=EB'|ε       A::=EA'     A'::= >EA'|<EA'|>=EA'|<=EA'|ε
     *       E::=TE'     E'::=+TE'|-TE'|ε        T::=FT'     T'::=*FT'|/FT'|%FT'|ε       F::=i
     * */
    void praseS(){      //S::=if A then S|while A do S|B
        boolean flags=true;
        if(count<length&&letter[count].equals("i")){
            if(count+1<length&&letter[count+1].equals("f")){
                System.out.print("if ");
                flags=false;
                stackcount++;
                count+=2;
                praseA();
                if(count+3<length&&letter[count].equals("t")&&letter[count+1].equals("h")
                        &&letter[count+2].equals("e")&&letter[count+3].equals("n")){
                    System.out.print(" then ");
                    stackcount++;
                    count+=4;
                    praseS();
                }else {
                    showerror("then");
                    count=letter.length;
                }
            }
        }
        if(flags&&count<length&&letter[count].equals("w")){
            String str1="w";
            try{
                for(int i=1;i<5;i++)
                    str1=str1.concat(letter[count+i]);
            }catch (NullPointerException ex){
                return;
            }
            if(str1.equals("while")){
                System.out.print(" while ");
                flags=false;
                stackcount++;
                count+=5;
                praseA();
                if(count<length&&letter[count].equals("d")&&count+1<length&&letter[count+1].equals("o")){
                    System.out.print(" do ");
                    stackcount++;
                    count+=2;
                    praseS();
                }else {
                    showerror("do");
                    count=letter.length;
                }
            }
        }
        if(flags){
            praseB();
        }

    }
    void praseA(){
        praseE();
        praseA1();
    }
    void praseA1(){
        boolean flagA=true;
        if(count<length&&(letter[count].equals(">")||letter[count].equals("<"))){
            if(count+1<length&&letter[count+1].equals("=")){
                switch (letter[count]){
                    case ">" : System.out.print(" >= ");break;
                    case "<" : System.out.print(" <= ");break;
                }
                count+=2;
            }else{
                switch (letter[count]){
                    case ">" : System.out.print(" > ");break;
                    case "<" : System.out.print(" < ");break;
                }
                count+=1;
            }
            stackcount++;
            praseE();
            praseA1();
        }
    }
    void praseB(){
        praseE();
        praseB1();
    }
    void praseB1(){
        if(count<length&&letter[count].equals(":")&&count+1<length&&letter[count+1].equals("=")){
            System.out.print(" := ");
            stackcount++;
            count+=2;
            praseE();
            praseB1();
        }
    }
    void praseE(){
        praseT();
        praseE1();
    }
    void praseE1(){
        if(count<length&&(letter[count].equals("+")||letter[count].equals("-"))){
            switch (letter[count]){
                case "+" : System.out.print(" + ");break;
                case "-" : System.out.print(" - ");break;
            }
            stackcount++;
            count+=1;
            praseT();
            praseE1();
        }
    }
    void praseT(){
        praseF();
        praseT1();
    }
    void praseT1(){
        if(count<length&&(letter[count].equals("*")||letter[count].equals("/")||letter[count].equals("%"))){
            switch (letter[count]){
                case "*" : System.out.print("*");break;
                case "/" : System.out.print("/");break;
                case "%" : System.out.print("%");break;
            }
            stackcount++;
            count+=1;
            praseF();
            praseT1();
        }
    }
    /*
     *   S::=if A then S|while A do S|B     B::=EB'     B'::= :=EB'|ε       A::=EA'     A'::= >EA'|<EA'|>=EA'|<=EA'|ε
     *   E::=TE'     E'::=+TE'|-TE'|ε        T::=FT'     T'::=*FT'|/FT'|%FT'|ε       F::=i
     * */
    void praseF(){
        if(stackcount<this.strstack.length){
            String str=this.strstack[stackcount];
            int strlength1=str.length();
            int i=0,j=count;
            boolean flag=false;
            for(;i<strlength1;i++){
                if(letter[j].equals(str.substring(0,1)))
                    flag=true;
                else
                    flag=false;
                str=str.substring(1);//may have bug
                j++;
            }
            if(flag){
                count=j;
                System.out.print(this.strstack[stackcount]);
                stackcount++;
            }
        }else{
            stackcount++;
            count=letter.length;
            this.showerror("标识符");
        }
    }
    public static void main(String args[]){
        Grammer g=new Grammer();
        lexical l=new lexical();
        l.lexicalanalysis("src/com/morphology/test1.txt");
//        l.lexicalanalysis("src/com/morphology/ex3-2.txt");
        String[] str1=l.getStringArray();
        int i=0;
        for(String str:str1){
            g.setListstack(l.getListstack()[i]);
            g.cut(str);
            g.praseS();
//            System.out.println();
            if(g.count==str.length()&&g.stackcount==g.strstack.length){
                System.out.println("\n"+str+"是该文法的语句！");
            }else{
                System.out.println(str+"不是该文法的语句！");
            }
            i++;
            g.init();
            System.out.println();
        }
    }
}



class Grammer1 {
    /*
    *   G1[E]:
    *   E::=TE'     E'::=+TE'|ε     T::=FT'
    *   T'::=*FT'|ε     F=(E)|i
    * */
    private static String[] letter;     // String array
    private int count=0;                // record char position
    private static int length;          // length of array
    void parseE(){
        parseT();
        parseE1();
    }
    void parseT(){
        parseF();
        parseT1();
    }
    void parseE1(){
        if(count<length&&letter[count].equals("+")) {       //判断计数是否超过长度，第一个字符是否为加号
            count++;
            System.out.print("+");
            parseT();
            parseE1();
        }
    }
    void parseF(){
        if(count<length&&letter[count].equals("(")){
            count++;
            System.out.print("(");
            parseE();
            System.out.print(")");
            count++;
        }
        else if(count<length&&letter[count].equals("i")){
            count++;
            System.out.print("i");
        }
    }
    void parseT1(){
        if(count<length&&letter[count].equals("*")){
            count++;
            System.out.print("*");
            parseF();
            parseT1();
        }
    }
    static void cut(String str){
        length=str.length();
        letter=new String[length];
        for(int i=0;i<length;i++){
            letter[i]=str.substring(0,1);
            str=str.substring(1);
        }
    }
    public static void main(String args[]){
        Grammer1 g=new Grammer1();
        String str="i-i";
        g.cut(str);
        g.parseE();
        if(g.count==str.length()){
            System.out.println("\n"+str+"是该文法的语句！");
        }else
            System.out.println("\n"+str+"不是该文法的语句！");
    }

}
class Grammer2{
    private static String[] letter;     //存放字符
    private int count=0;                //记录字符位置
    private static int length;          //字符数组长度
    public static int getLength() {
        return length;
    }
    /*
    *   S::=TS'     S'::=;TS'|ε      T::=if e then ST'|a
    *   T'::=else S|ε
    * */
    void parseS(){
        parseT();
        parseS1();
    }
    void parseT(){
        if(count<length&&letter[count].equals("i")){
            String str1="i";
            try{
                for(int i=1;i<7;i++)
                    str1=str1.concat(letter[count+i]);
            }catch (NullPointerException ex){
                return;
            }
            if(str1.equals("ifethen")){
                System.out.print("if e then ");
                count+=7;
                parseS();
                parseT1();
            }
        }else if(count<length&&letter[count].equals("a")){
            count++;
            System.out.print("a");
        }
    }
    void parseS1(){
        if(count<length&&letter[count].equals(";")){
            count++;
            System.out.print(";");
            parseT();
            parseS1();
        }
    }
    void parseT1(){
        if(count<length&&letter[count].equals("e")){
            String str1="e";
            try{
                for(int i=1;i<4;i++)
                    str1=str1.concat(letter[count+i]);
            }catch (NullPointerException ex){
                return;
            }
            if(str1.equals("else")){
                System.out.print(" else ");
                count+=4;
                parseS();
            }

        }
    }
    static void cut(String str){
        length=str.length();
        letter=new String[length];      //allocate space
        for(int i=0;i<length;){
            String str1=str.substring(0,1);
            if(!str1.equals(" ")){      //remove space
                letter[i]=str1;
                i++;
            }
            else
                length--;
            str=str.substring(1);
        }
    }
    public static void main(String args[]){
        Grammer2 g=new Grammer2();
      //  String str="if e then a else a";
        String str="if e";
        g.cut(str);         //character segment
        g.parseS();         //start state
        if(g.count==getLength()){
            System.out.println("\n"+str+"是该文法的语句！");
        }else
            System.out.println("\n"+str+"不是该文法的语句！");
    }
}
class Grammer3{
    private static String[] letter;     //存放字符
    private int count=0;                //记录字符位置
    private static int length;          //字符数组长度
    public static int getLength() {
        return length;
    }
    /*
    *   P::=begin S end     S::=V:=E|if E then S
    *   E::=VE'   E'::=+VE'|ε     V::=i
    * */
    void parseP(){
        if((count<length)&&letter[count].equals("b")){
            String str1="b";
            String str2="e";
            try{
                for(int i=1;i<5;i++)
                    str1=str1.concat(letter[count+i]);
            }catch (NullPointerException ex){
                return;
            }
            if(str1.equals("begin")){
                System.out.print("begin ");
                count+=5;
                parseS();
            }
            try{
                for(int i=1;i<3;i++)
                    str2=str2.concat(letter[count+i]);
            }catch (NullPointerException ex){
                return;
            }
            if(str2.equals("end")){
                System.out.print(" end");
                count+=3;
            }else
            {

            }
        }
    }
    void parseS(){
        String str1="t";
        if((count<length)&&letter[count].equals("i")&&((count+1)<length)&&letter[count+1].equals("f")){
            count+=2;
            System.out.print("if ");
            parseE();
            try{
                for(int i=1;i<4;i++)
                    str1=str1.concat(letter[count+i]);
            }catch (NullPointerException ex){
                return;
            }
            if(str1.equals("then")){
                System.out.print(" then ");
                count+=4;
                parseS();
            }
        }
        else{
            str1=":";
            parseV();
            if((count<length)&&letter[count].equals(":")){
                if(((count+1)<length)&&letter[count+1].equals("=")){
                    count+=2;
                    System.out.print(" := ");
                    parseE();
                }
            }
        }
    }
//      *   E::=VE'   E'::=+VE'|ε     V::=i
    void parseE(){
        parseV();
        parseE1();
    }
    void parseE1(){
        if(count<length&&letter[count].equals("+")){
            count++;
            System.out.print("+");
            parseV();
            parseE1();
        }
    }
    void parseV(){
        if(count<length&&letter[count].equals("i")){
            count++;
            System.out.print("i");
        }
    }
    static void cut(String str){
        length=str.length();
        letter=new String[length];
        for(int i=0;i<length;){
            String str1=str.substring(0,1);
            if(!str1.equals(" ")){
                letter[i]=str1;
                i++;
            }
            else
                length--;
            str=str.substring(1);
        }
    }
    public static void main(String args[]){
        Grammer3 g=new Grammer3();
        String str="begin if i then i:=i+i+i end";
        g.cut(str);
        g.parseP();
        if(g.count==getLength()){
            System.out.println("\n"+str+"是该文法的语句！");
        }else
            System.out.println("\n"+str+"不是该文法的语句！");
    }

}
