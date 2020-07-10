package com.predictive;

import com.predictive.lexical;

import java.io.*;
import java.security.Signature;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Stack;

public class predictivelexical {
    public static final String EPSILON = "ε";
    public static final String SINGLE_ANGLE = "'";
    public static final String REGEX = "|";
    public static final String ASSIGN = "::=";
    private static int length=0;        //词法分析程序使用的长度用来记录读取的字符串的长度
    private static int num;             //词法分析程序当前字符所在位置
//    public static Stack[] liststack;    //一句一个栈存放字符
    private static int len=0;           //多少句语句
    private LinkedList<String>[] list; //词法分析使用的结构存放字符
    //非终结符栈
    private Stack<String> Nonterminal=new Stack();
    //终结符栈
    private Stack<String> terminal=new Stack();
    //非终结符
    String[] Vn;
    //终结符
    String[] Vt;
    //产生式
    public Map<String,String> P= new HashMap<>();
    //first集
    public Map<String,Stack> first=new HashMap<>();
    //follow集
    public Map<String,Stack> follow=new HashMap<>();
    //select集
    public String[][] select;

    public int typeword(String str){   //判断当前读取的字符的类型
        if(str.matches("^[A-Z]")) //非终结符 正则表达式
            return 1;
        if(str.matches("^[a-z]")||str.equals(">")||str.equals("=")||str.equals("<")||str.equals("!")||str.equals(",")
                ||str.equals(";")||str.equals("(")||str.equals(")")||
                str.equals("{")||str.equals("}")||str.equals("+")||str.equals("-")||
                str.equals("*")||str.equals("/")||str.equals(":")||str.equals("%")||
                str.equals("[")||str.equals("]")) //终结符或者：：=
            return 2;
        if(str.equals("ε")||str.equals("|"))//判断空和分割符
            return 3;
        return 0;
    }
    public String[] getread(String txt){  //读取txt文件
//        String[] str=null;
        String tmp="";
        int icount=0;
        String[] str=txt.split("\n");
//        char i;
//        while ((i = txt.charAt(icount)) != 0) {
//            if((char)i!=' '&&(char)i!='\n'&&(char)i!='\r'&&(char)i!='\t'){
//                 icount++;//去除空格回车和制表符
//
//            }
//        }
        return str;
    }
    public String[] read(String fpath){  //读取txt文件
        String[] str=null;
        try {
            BufferedReader fin1 = new BufferedReader(new FileReader(fpath));
            int counti=0;
            String i;
            while ((i = fin1.readLine()) != null) {
                counti++;
            }
            str=new String[counti];
            counti=0;
            BufferedReader fin2 = new BufferedReader(new FileReader(fpath));
            while ((i = fin2.readLine()) != null) {
                str[counti]=i;
                counti++;
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
        boolean flag3=false;
        switch (s){
            case ">":case "=":case "<": case ":"://判断两个符号的运算符 > = < : !后都接=
            case "!":if (letter[j+1].equals("=")){ flag=true;break; }
            else if (s.equals(":")&&letter[j+1].equals(":")&&letter[j+2].equals("=")){ flag3=true;break; }
            case "+":if (letter[j+1].equals("+")){ flag=true;break; }//++
            case "-":if (letter[j+1].equals("-")){ flag=true;break; }//--
        }
        if(flag==true){
            j = n + 1;
            s = s + letter[j];
        }
        if (flag3==true){
            s = s + letter[j+1]+letter[j+2];
            j = n + 2;
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
    public String identifier(String str,int n,String[] letter){ //判断非终结符
        int j=n+1;
        if(j<letter.length&&letter[j].equals("'")){
            str+=letter[j];
            j+=1;
        }
        num=j;
        return str;
    }
    static String[] cut(String str){
        length=str.length();
        String[] letter=new String[length];
        for(int i=0;i<length;i++){
            letter[i]=str.substring(0,1);
            str=str.substring(1);
        }
        return letter;
    }
    public void pushterminalstring(String str){
        if(this.terminal.search(str)==-1){
            this.terminal.push(str);
        }
    }
    public void lexicalanalysis(String txt){ //词法分析主程序
        String[] string=read(txt);  // read txt into program
//        String[] string=txt.split("\n");
        this.len=string.length;         // get string length
        this.list=new LinkedList[len];
        for(int i=0;i<this.len;i++){
//            System.out.println(string[i]);
            this.list[i]=new LinkedList<>();
            String strlist=string[i];
            this.length=strlist.length();
            String[] letter=this.cut(strlist);
            int count=0;
            num=0;
            Nonterminal.push(letter[0]);
            for(;num<length;){
                String str,str1;
                str=letter[num];
                int k=this.typeword(str);
                switch (k){
                    case 1:
                        str1=this.identifier(str,num,letter);
                        this.list[i].add(str1);
                        break;
                    case 2:
                        str1=this.symbolStr(str,num,letter);
                        this.list[i].add(str1);
                        if(!str1.equals("::=")&&this.terminal.search(str1)==-1)
                            this.terminal.push(str1);
                        break;
                    case 3:
                        this.list[i].add(str);
                        num++;
                        break;

                }
                count++;
            }
            System.out.print("[");
            for(int j=0;j<count-1;j++)
                System.out.print(list[i].get(j)+",");
            System.out.println(list[i].get(count-1)+"]");
        }
        this.grammer();

        //设置终结符和非终结符数组
        setVn();
        setVt();
        //输出消除递归后的文法
        this.MaptoString(P);
        //first集
        this.first();
        //first集输出
        int flen=Vn.length;
        for(int i=0;i<flen;i++){
            System.out.println(Vn[i]+"的first集为"+first.get(Vn[i]).toString());
        }
        //follow集
        this.follow();
        //输出follow集
        int followlen=Vn.length;
        for(int i=0;i<followlen;i++){
            System.out.println(Vn[i]+"的follow集为"+follow.get(Vn[i]).toString());
        }
        //终结符栈 非终结符栈输出
        System.out.print("Vn:[");
        for(int q=0;q<Vn.length-1;q++)
            System.out.print(Vn[q]+",");
        System.out.println(Vn[Vn.length-1]+"]");
        System.out.print("Vt:[");
        for(int q=0;q<Vt.length-1;q++)
            System.out.print(Vt[q]+",");
        System.out.println(Vt[Vt.length-1]+"]");

        //select集
        this.select();

        //输出select集
        int vnlen=Vn.length;
        int vtlen=Vt.length+1;
        System.out.println("预测分析表");
        System.out.printf("%-10s"," ");
        for(int i=0;i<vtlen-1;i++){
            System.out.printf("%-10s",Vt[i]);
        }
        System.out.printf("%-10s\n","#");
        for(int i=0;i<vnlen;i++){
            System.out.printf("%-10s",Vn[i]);
            for(int j=0;j<vtlen;j++){
                if(select[i][j]!=null)
                    System.out.printf("%-10s",select[i][j]);
                else
                    System.out.printf("%-10s"," ");
            }
            System.out.println();
        }

        //分析过程
        this.predictivelabel("+*i");



    }
    public void MaptoString(Map<String,String> p){
        int len=Vn.length;
        for(int i=0;i<len;i++){
            System.out.println(Vn[i]+ASSIGN+p.get(Vn[i]));
        }
    }
    public void grammer(){     //替换产生式
        for(int i=0;i<len;i++){  //直接左递归
            if(this.Nonterminal.search(this.list[i].get(0))!=-1&&this.list[i].get(1).equals("::=")){
                if(this.list[i].get(0).equals(list[i].get(2))){
                    int index=list[i].indexOf("|");
                    String X="",Y="";
                    if(index!=-1){
                        for(int j=3;j<index;j++){
                            X+=list[i].get(j);
                        }
                        int listlength=list[i].size();
                        for(int j=index+1;j<listlength;j++){
                            Y+=list[i].get(j);
                        }
                        if(list[i].get(0).indexOf("'")==-1){
                            String left=list[i].get(0)+this.SINGLE_ANGLE;
                            this.Nonterminal.push(left);
//                            this.Nonterminal.push(left+this.SINGLE_ANGLE);
                            P.put(list[i].get(0),X+left+this.REGEX+Y);
                            P.put(left,X+left+this.REGEX+this.EPSILON);
    //                        System.out.println(list[i].get(0)+"::="+X+list[i].get(0)+"'");
    //                        System.out.println(list[i].get(0)+"'::="+Y+list[i].get(0)+"'|@");
                        }
                    }else{
                        int listlength=list[i].size();
                        for(int j=3;j<listlength;j++){
                            Y+=list[i].get(j);
                        }
//                        int indexstack=this.Nonterminal.search(list[i].get(0));
                        String left=list[i].get(0)+this.SINGLE_ANGLE;
//                        this.Nonterminal.add(indexstack-1,left);
                        this.Nonterminal.push(left);
                        P.put(list[i].get(0),Y+left);
                        P.put(left,Y+left+this.REGEX+this.EPSILON);
                    }
                }
                else {  //间接左递归
                    int listlength=list[i].size();
                    String str="";
                    for(int inum=2;inum<listlength;inum++)
                        str+=list[i].get(inum);
                    P.put(list[i].get(0),str);
                }
            }else{
                System.out.println(list[i].toString()+"文法错误！");
                len-=1;
            }
        }
    }
    public void putnextfirst(String left,String rightfirst){
        Stack<String> tmp=(Stack)first.get(rightfirst);
        int tmpl=tmp.size();
        String[] strstack=new String[tmpl];
        for(int tmpi=0;tmpi<tmpl;tmpi++){
            strstack[tmpi]=tmp.get(tmpi);
        }
        for(String a:strstack) {
            first.get(left).push(a);
        }
    }
    public void first(){
        int Vnlen=this.Vn.length;
        for(int i=Vnlen-1;i>=0;i--){
            String left=this.Vn[i];
            String right=this.P.get(left);
            String rightfirst=right.substring(0,1);
            int index=right.indexOf(predictivelexical.REGEX);
            int k=typeword(rightfirst);
            if(first.get(left)==null){
                first.put(left,new Stack());
            }
            switch (k){
                case 1:
                    if(index==-1){
                        putnextfirst(left,rightfirst);
                    }else{
                        String tmpright=right.substring(index+1).substring(0,1);
                        int l=typeword(tmpright);
                        if(l==1)
                            putnextfirst(left,tmpright);
                        if(l==2)
                            first.get(left).push(tmpright);
                    }
                case 2:
                    if(index==-1)
                        first.get(left).push(rightfirst);
                    else{
                        first.get(left).push(rightfirst);
                        String tmpright=right.substring(index+1).substring(0,1);
                        int l=typeword(tmpright);
                        if(l==1)
                            putnextfirst(left,tmpright);
                        if(l==2||tmpright.equals(EPSILON))
                            first.get(left).push(tmpright);
                    }
            }
        }
    }
    public void follow(){
        int Vnlen=this.Vn.length;
        for(int i=0;i<Vnlen;i++){
            String left=Vn[i];
            if(follow.get(left)==null)
                follow.put(left,new Stack());
            if(i==0){
                follow.get(left).push("#");
            }
            for(int j=0;j<Vnlen;j++) {
                String right=P.get(Vn[j]);
                int index=right.indexOf(left);
                if(index!=-1){
                    String tmpright="";
                    if(index+1<right.length()){
                        tmpright=right.substring(index+1,index+2);
                        int k=typeword(tmpright);

                        switch (k){
                            case 0:
                                tmpright=right.substring(index+0,index+1)+SINGLE_ANGLE;
                                if(tmpright.equals(left)) {
                                    int index0=index+1;
                                    if(index0+2<=right.length()) {
                                        String next=right.substring(index0+1,index0+2);
                                        Stack<String> tmpstack;
                                        switch (this.typeword(next)) {
                                            case 3: //E'|
                                                tmpstack=follow.get(Vn[j]);
                                                for (String a:tmpstack){
                                                    if(!a.equals(EPSILON)&&follow.get(left).search(a)==-1)
                                                        follow.get(left).push(a);
                                                }break;
                                            case 2: //E'a
                                                if(follow.get(left).search(tmpright)==-1)
                                                    follow.get(left).push(tmpright);break;
                                            case 1://E'T      E'T'
                                                if(index0+3<right.length()&&right.substring(index0+2,index0+3).equals(SINGLE_ANGLE)) {
                                                    tmpright =right.substring(index0+1,index0+2)+ SINGLE_ANGLE;
                                                }else {
                                                    tmpright =right.substring(index0+1,index0+2);
                                                }
                                                addfirstfollow(tmpright,left);
                                                break;
                                        }
                                    }else if(index0+1<=right.length()){//  E'
                                        addffollow(Vn[j],left);
                                    }
                                }
                                break;
                            case 1://非终结符      T'
                                if(index+3<=right.length()){
                                    if(Vn[j].indexOf(SINGLE_ANGLE)!=-1&&right.substring(index+2,index+3).equals(SINGLE_ANGLE)){
                                        tmpright+=SINGLE_ANGLE;
                                        Stack<String> tmpstack=first.get(tmpright);
                                        for (String a:tmpstack){
                                            if(!a.equals(EPSILON)&&follow.get(left).search(a)==-1)
                                                follow.get(left).push(a);
                                            else {
                                                addffollow(Vn[j],left);
                                            }
                                        }
                                    }
                                }else if(index+2<right.length())  // T
                                    addfirstfollow(tmpright,left);
                                break;
                            case 2:// 终结符
                                if(follow.get(left).search(tmpright)==-1)
                                    follow.get(left).push(tmpright);break;
                            case 3:
                                //空e        follow left -> follow right     T|
                                if((index+1<=right.length()&&right.substring(index+1,index+2).equals(REGEX))){
                                    Stack<String> tmpstack=follow.get(Vn[j]);
                                    for (String a:tmpstack){
                                        if(!a.equals(EPSILON)&&follow.get(left).search(a)==-1)
                                            follow.get(left).push(a);
                                    }
                                }break;
                        }
                    }
                    //空e    E  follow(E)
                    if(index+1==right.length()){
                        addffollow(Vn[j],left);
                    }
                }
            }
        }
    }
    private void addffollow(String tmpright,String left){
        Stack<String> tmpstack=follow.get(tmpright);
        for (String a:tmpstack){
            if(!a.equals(EPSILON))
                follow.get(left).push(a);
        }
    }
    private void addfirstfollow(String tmpright,String left){
        Stack<String> tmpstack=first.get(tmpright);
        for (String a:tmpstack){
            if(!a.equals(EPSILON)&&follow.get(left).search(a)==-1)
                follow.get(left).push(a);
        }
    }
    public void select(){
        this.select=new String[this.Vn.length][this.Vt.length+1];
        int Vnlen=this.Vn.length;
        for(int i=0;i<Vnlen;i++){  //Vn:[E,E',T,T',F]
            String left=Vn[i];
            String right=P.get(left);
            if(right.indexOf(REGEX)!=-1){
                int index=right.indexOf(REGEX);
                String right1=right.substring(0,index);
                String right2=right.substring(index+1);
                selectrec(left,right1);
                selectrec(left,right2);
            }else{
                selectrec(left,right);
            }
        }
    }
    public void selectrec(String left,String right){
        int leftpostion=0;
        while(!left.equals(Vn[leftpostion])){
            leftpostion++;
        }
        String rightfirst=right.substring(0,1);    // 终结符  非终结符  空e
        int k=typeword(rightfirst);
        int vnlen=this.Vn.length;
        int vtlen=this.Vt.length;
        switch (k){
            case 1:
                if(right.length()>1&&right.substring(1,2).equals(SINGLE_ANGLE))  //T'  else T
                    rightfirst=right.substring(0,2);
                Stack<String> tmp=this.first.get(rightfirst);
                for(String a:tmp){
                    int i=0;
                    while (!a.equals(Vt[i]))
                        i++;
                    select[leftpostion][i]=left+ASSIGN+right;
                    System.out.println(Vn[leftpostion]+"  "+Vt[i]+"  "+select[leftpostion][i]);
                }
                break;
            case 2:
                int q=0;
                while (!rightfirst.equals(Vt[q]))
                    q++;
                select[leftpostion][q]=left+ASSIGN+right;
                System.out.println(Vn[leftpostion]+"  "+Vt[q]+"  "+select[leftpostion][q]);
                break;
            case 3:
                Stack<String> followleft=this.follow.get(left);
                for(String a:followleft){
                    int j=0;
                    if(!a.equals("#")){
                        while (!a.equals(Vt[j])){
                            j++; }
                        select[leftpostion][j]=left+ASSIGN+right;
                        System.out.println(Vn[leftpostion]+"  "+Vt[j]+"  "+select[leftpostion][j]);
                    }else{
                        select[leftpostion][vtlen]=left+ASSIGN+right;
                        System.out.println(Vn[leftpostion]+"  #"+"  "+select[leftpostion][vtlen]);
                    }
                }
                break;
        }
    }
    public void predictivelabel(String letter){
        lexical l=new lexical();
        String[] str=this.getStringArray(letter);  //输入缓冲区
        int xcount=0;
//        int len=str.length;
        String start=Vn[0];
        int vtlen=this.Vt.length;
        int vnlen=Vn.length;
        String[] vt=new String[vtlen+1];
        for(int i=0;i<vtlen;i++){
            vt[i]=Vt[i];
        }
        vt[vtlen]="#";
//        for(String a:str)
//            System.out.print(a);
        Stack<String> perstack=new Stack<>();  //分析栈
        perstack.push("#");
        perstack.push(start);
        System.out.printf("%-25s%-25s%-25s\n","栈","剩余输入","输出");
        while(str.length!=1){
            String first=perstack.peek();  //栈顶
            int k=typeword(first);
            if(first.indexOf(SINGLE_ANGLE)!=-1)
                k=1;
//            if(first.equals("i"))
//                k=2;
            switch (k){
                case 1:
                    int xi=0,yi=0;
                    while(!first.equals(Vn[xi])){
                        xi++;    //非终结符下标
                    }
                    while(!str[xcount].equals(Vt[yi])){
                        yi++;    //终结符下标
                    }
                    String gramm=this.select[xi][yi];
//                    System.out.println("非终结符下标xi"+xi+"非终结符"+Vn[xi]+"终结符下标xi"+yi+"终结符"+Vt[yi]+"语句"+gramm);
                    String tmpstr="";
                    for(String a:str){
                        tmpstr+=a;
                    }
                    System.out.printf("%-25s%-25s%-25s\n",perstack.toString(),tmpstr,((gramm==null)?" ":gramm));
                    perstack.pop();
                    String[] tmpletter=this.cutStringArray(gramm);
                    int tmplen=tmpletter.length;
                    for(int tmpi=tmplen-1;tmpi>=2;tmpi--){
                        perstack.push(tmpletter[tmpi]);
                    }
                    break;
                case 2:
                    if(first.equals(str[xcount])){
                        String[] tmpstrarray=new String[str.length-1];
                        for(int i=0;i<tmpstrarray.length;i++){
                            tmpstrarray[i]=str[i+1];
                        }
                        tmpstr="";
                        for(String a:str){
                            tmpstr+=a;
                        }
                        str=tmpstrarray;
                        System.out.printf("%-25s%-25s%-25s\n",perstack.toString(),tmpstr," ");
                        perstack.pop();
                        tmpstr="";
                        for(String a:str){
                            tmpstr+=a;
                        }
                        System.out.printf("%-25s%-25s%-25s\n",perstack.toString(),tmpstr," ");
                    }
                    else{
                        System.out.println("语句错误无法判断");
                        return;
                    }
                    break;
            }
        }
            while(perstack.size()!=1){
                String first=perstack.peek();
                int xi=0;
                while(!first.equals(Vn[xi])){
                    xi++;    //非终结符下标
                }
                String gramm=this.select[xi][Vt.length];
//                    System.out.println("非终结符下标xi"+xi+"非终结符"+Vn[xi]+"终结符下标xi"+yi+"终结符"+Vt[yi]+"语句"+gramm);
//                String tmpstr="";
//                for(String a:str){
//                    tmpstr+=a;
//                }
                System.out.printf("%-25s%-25s%-25s\n",perstack.toString(),"#",((gramm==null)?" ":gramm));
                perstack.pop();
                System.out.printf("%-25s%-25s%-25s\n",perstack.toString(),"#"," ");
            }
        if(perstack.size()==1&&"#".equals(perstack.peek())&&"#".equals(str[0]))
            System.out.println("acc!");
//        reletter=letter;
//        preprocess(start,vt);
//        while(letter!=null||letter!=""){
//
//        }
    }
//    private String reletter;
//    // start 左部符号   letter  当前匹配字符串    vt 终结符数组
//    public void preprocess(String start,String[] vt){
//        if(reletter==null) {
//            return;
//        }
//        String first;
//        try{
//             first=reletter.substring(0,1);
//        }catch (Exception e){
//            return ;
//        }
//        int vtlen=vt.length;
//        int startpos=0;
//        while (!start.equals(Vn[startpos])&&startpos<Vn.length-1)
//            startpos++;
//        for(int i=0;i<vtlen;i++){
//            if(first.equals(vt[i])){
//                String str=select[startpos][i];
//                if(str!=null){
//                    System.out.printf("当前值%-10s",str);
//                    String tmpfirst=str.substring(4,5);//E
//                    String[] strarray=this.getStringArray(str);
//                    int k=typeword(tmpfirst);
//                    switch (k){
//                        case 1: // str = E::=TE'
//                            for(int j=2;j<strarray.length;j++)
//    //                            if(strarray[j].length()==2||strarray[j].indexOf(SINGLE_ANGLE)!=-1)
//                                if(this.typeword(strarray[j].substring(0,1))==1)
//                                    preprocess(strarray[j],vt);
//                            break;
//                        case 2:
//                            System.out.printf("取出%-10s \n",reletter.substring(0,1));
//                            reletter=reletter.substring(1);
//                            for(int j=3;j<strarray.length;j++)
////                                if(this.typeword(strarray[j].substring(0,1))==1)
//                                    preprocess(strarray[j],vt);
//                            break;
//                        case 3:
//                            System.out.printf("空 %-10s",reletter.substring(0,1));
//                            break;
//                    }
//                }
//            }
//        }
//    }
    public String[] cutStringArray(String string){
        int len=string.length();         // get string length
        LinkedList<String> list1=new LinkedList<>();
//            System.out.println(string[i]);
        String[] letter=this.cut(string);
        int count=0;
        num=0;
        for(;num<len;){
            String str,str1;
            str=letter[num];
            int k=this.typeword(str);
            switch (k){
                case 1:
                    str1=this.identifier(str,num,letter);
                    list1.add(str1);
                    break;
                case 2:
                    str1=this.symbolStr(str,num,letter);
                    list1.add(str1);
                    break;
                case 3:
                    list1.add(str);
                    num++;
                    break;

            }
            count++;
        }
        int countnum=0;
        String[] str=new String[list1.size()];
        for(String a:list1){
            str[countnum++]=a;
        }
        return str;
    }
    public String[] getStringArray(String string){
        int len=string.length();         // get string length
        LinkedList<String> list1=new LinkedList<>();
//            System.out.println(string[i]);
        String[] letter=this.cut(string);
        int count=0;
        num=0;
        for(;num<len;){
            String str,str1;
            str=letter[num];
            int k=this.typeword(str);
            switch (k){
                case 1:
                    str1=this.identifier(str,num,letter);
                    list1.add(str1);
                    break;
                case 2:
                    str1=this.symbolStr(str,num,letter);
                    list1.add(str1);
                    break;
                case 3:
                    list1.add(str);
                    num++;
                    break;

            }
            count++;
        }
        int countnum=0;
        String[] str=new String[list1.size()+1];
        for(String a:list1){
            str[countnum++]=a;
        }
        str[str.length-1]="#";
        return str;
    }
    public void setVn(){
        int noterlen=this.Nonterminal.size();
        int count=0;
        Vn=new String[noterlen];
        for(String a:this.Nonterminal){
            if(a.indexOf(predictivelexical.SINGLE_ANGLE)==-1){
                Vn[count]=a;
                if(this.Nonterminal.search(a+SINGLE_ANGLE)!=-1){
                    Vn[count+1]=a+predictivelexical.SINGLE_ANGLE;
                    count+=1;
                }
                count+=1;
            }
        }
    }
    public void setVt(){
        int noterlen=this.terminal.size();
        Vt=new String[noterlen];
        for(int i=0;i<noterlen;i++){
            Vt[i]=(String)terminal.get(i);
        }
    }
    public static void main(String args[]){
        predictivelexical l=new predictivelexical();
//        l.lexicalanalysis("src/com/predictive/a.txt");
//        l.lexicalanalysis("src/com/predictive/b.txt");
//        l.lexicalanalysis("src/com/predictive/c.txt");
        l.lexicalanalysis("src/com/predictive/d.txt");
//        l.predictivelabel("+*(i)");
        System.out.println("终结符栈"+l.terminal.toString());
        System.out.println("非终结符栈"+l.Nonterminal.toString());
    }





}
