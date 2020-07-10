package com.predictive;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Stack;

public class lexical {
    public static final int lex_num=11; //数字
    public static final int lex_id=12;  //标识符
    public static Stack[] liststack;  //一句一个栈存放字符
    private static int countlength=0;  //记录
    private static Stack[] listnum;   //记录对应字符的码
    private LinkedList<String> list=new LinkedList<String>(); //词法分析使用的结构存放字符
    private int[] list1;    //词法分析使用的记录对应字符的码
    private static int length=0;   //词法分析程序使用的长度用来记录读取的字符串的长度
    private static int num;  //词法分析程序当前字符所在位置
    public static String[] key={"main","int","void","if","else","while","do","return","for","then"};//关键字
    public static int[] keyNum={1,2,3,4,5,6,7,8,9,10};
    public static String[] symbol={"+","-","*","/","<","<=",">",">=","==",//符号
            "!=","=","(",")","[","]","{","}",";","++","--","%",":=","::=","|"};//21个
    public static int[] symbolNum={13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34};
    //数字   11   标识符   12
    private int isSymbol(String s){ //判断是否为符号
        int len=symbol.length;
        for(int i=0;i<len;i++){
            if(s.equals(symbol[i]))
                return symbolNum[i];
        }
        return 0;
    }
    private int isKey(String s){    //判断是否为关键字
        int len=key.length;
        for(int i=0;i<len;i++){
            if(s.equals(key[i]))
                return keyNum[i];
        }
        return 0;
    }
    public int typeword(String str){   //判断当前读取的字符的类型
        if(str.matches("^[A-Za-z]")) // 字母 正则表达式
            return 1;
        if(str.matches("^[0-9]")) //数字
            return 2;
        if(str.equals(">")||str.equals("=")||str.equals("<")||str.equals("!")||str.equals(",")
                ||str.equals(";")||str.equals("(")||str.equals(")")||
                str.equals("{")||str.equals("}")||str.equals("+")||str.equals("-")||
                str.equals("*")||str.equals("/")||str.equals(":")||str.equals("%")||
                str.equals("[")||str.equals("]")||str.equals("|"))//判断运算符和界符
            return 3;
        return 0;
    }
    public String read(String fpath){  //读取txt文件
        String str = "";
        try {
            InputStream fin = new FileInputStream(fpath);
            int i;
            while ((i = fin.read()) != -1) {
                if((char)i!=' '&&(char)i!='\n'&&(char)i!='\r'&&(char)i!='\t'){
                    str = str + (char)i; //去除空格回车和制表符
                    length++;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }
    public String symbolStr(String s,int n,String[] letter){ //判断符号
        int j=n;
        boolean flag=false;
        switch (s){
            case ">":case "=":case "<": case ":"://判断两个符号的运算符 > = < : !后都接=
            case "!":if (letter[j+1].equals("=")){ flag=true;break; }
            case "+":if (letter[j+1].equals("+")){ flag=true;break; }//++
            case "-":if (letter[j+1].equals("-")){ flag=true;break; }//--
        }
        if(flag==true){
            j = n + 1;
            String str = letter[j];
            s = s + letter[j];
        }
        num=++j;
        return s;
    }
    public String Number(String s, int n, String[] letter){ //判断数字
        int j=n+1;
        boolean flag=true;
        while(flag){
            if(letter[j].matches("^[0-9]")&&j<length){
                s=s+letter[j];
                j++;
            }
            else
                flag=false;
        }
        num=j;
        return s;
    }
    public int keyjudge(String start,String[] letter,int count){ //判断是否是保留的关键字
        int keylength=key.length;
        for(int i=0;i<keylength;i++){
            int strlength=key[i].length();
            String str=start;
            if(str.equals(key[i].substring(0,1))){//对每一个是保留关键字的首字符的进行判断
                for(int j=1;j<strlength;j++){
                    str+=letter[count+j];
                }
                if(str.equals(key[i]))
                    return keyNum[i];
            }
        }
        return 0;
    }
    public String identifier(String s,int n,String[] letter){ //判断标识符还是关键字
        int j=n+1;
        boolean flag=true;
        while(flag){
            if((j<length)&&((letter[j].matches("^[A-Za-z]")||letter[j].matches("^[0-9]")))){
                if(letter[j].matches("^[A-Za-z]")){
                    if(keyjudge(letter[j],letter,j)!=0){
                        num=j;
                        return s;
                    }
                }
                s=s+letter[j];
                if(isKey(s)!=0){
                    j++;
                    num=j;
                    return s;
                }
                j++;
            }
            else{
                flag=false;
            }
        }
        num=j;
        return s;
    }
    public String[] lexicalanalysis(String txtpath){ //词法分析主程序
//        String string=read(txtpath); // read txt into program
        String string=txtpath;
        int len=string.length();   //get string length
        String[] letter =new String[len]; //allocate space
        LinkedList<String> letterlist=new LinkedList<>();
//        list1=new int[len];      //save the number of key or symbol
        for(int i=0;i<len;i++){
            letter[i]=string.substring(0,1);  //character segment
            string=string.substring(1);
        }
        int k;
        for(num=0;num<length;){
            String str1,str;
            str=letter[num];
            k=typeword(str);   //judge type
            int i;
            switch(k){       //using 1,2,3 to identifier the char
                case 1:
                    str1=this.identifier(str,num,letter);
                    if(((i=isKey(str1))!=0))
//                        System.out.println("关键字："+str1+"\t优先级："+i);
//                        System.out.println("(关键字："+i+","+str1+")");
                    {
                        letterlist.add(str1);
//                        list1[countlength]=i;
                    }
                    else
//                        System.out.println("标识符："+str1+"\t优先级：28");
//                        System.out.println("(标识符：12,"+str1+")");
                    {
                        letterlist.add(str1);
//                        list1[countlength]=12;
                    }
                    break;
                case 2:
                    str1=this.Number(str,num,letter);
//                    System.out.println("数字："+str1+"\t优先级：29");
//                    System.out.println("(数字：11,"+str1+")");   实验一使用
                    letterlist.add(str1);    //实验三使用
//                    list1[countlength]=11;
                    break;
                case 3:
                    str1=this.symbolStr(str,num,letter);
                    i=isSymbol(str1);
//                    System.out.println("符号："+str1+"\t优先级："+i);
//                    System.out.println("(符号："+i+","+str1+")");
                    letterlist.add(str1);
//                    list1[countlength]=i;
                    break;
            }
            countlength++;
        }
        int letterlen=letterlist.size();
        String[] str=new String[letterlen];
        for(int i=0;i<letterlen;i++){
            str[i]=letterlist.get(i);
        }
        return str;
    }
    public Stack[] getListstack(){  //获取栈数组
        return liststack;
    }
    public String[] getStringArray(){  //将栈中存放的字符转换成字符串数组 给语义分析程序使用
        int strlength=0;
        for (String str1:list) {
            if(str1.equals(";"))
               strlength++;
        }
        liststack=new Stack[strlength];
        listnum=new Stack[strlength];
        for(int i=0;i<strlength;i++){
            liststack[i]=new Stack();
            listnum[i]=new Stack();
        }
        String[] str=new String[strlength];
        String strtmp="";
        int i=0;
        int j=0;
        for(String str1:list){
            if(!str1.equals(";")){
                strtmp+=str1;
                liststack[i].push(str1);
                listnum[i].push(list1[j]);
            }else {
                str[i]=strtmp;//+";";
//                liststack[i].push(";");
                i++;
                strtmp="";
            }
            j++;
        }
        return str;
    }
        public static void main(String args[]){
            lexical l=new lexical();
            l.lexicalanalysis("src/com/morphology/ex3-2.txt");
//            System.out.println("key的长度为:"+key.length);
//            System.out.println("keyNum的长度为:"+keyNum.length);
//            System.out.println("symbol的长度为:"+symbol.length);
//            System.out.println("symbolNum的长度为:"+symbolNum.length);
//            System.out.println("链表的长度为："+l.list.size());
//            System.out.println(l.list.toString());
            String[] str=l.getStringArray();
            for(String st1r:str)
                System.out.println(st1r);
            for(Stack a:liststack){
                System.out.println(a.toString());
            }
            for(Stack a:listnum){
                System.out.println(a.toString());
            }
        }





}
