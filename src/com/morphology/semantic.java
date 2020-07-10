package com.morphology;

import java.util.LinkedList;
import java.util.Stack;

public class semantic {
 //   public static boolean grammerflag=false; //是否为该文法的语句
    private String[] strstack;   //字符数组存放一个个单独的语句
    private LinkedList<String> strlist=new LinkedList<String>(); //单词 列表
    private int tcount=1;                 //t记录数
    private Stack tstack=new Stack();     //t记录栈
    private int count=0;                //记录字符位置
    private int semcount=1;             //语句记录  第几条语句
    private int geticount(LinkedList<String> strarray,String a){
        int icount=0;  //获取字符a在字符列表的下表（位置）
        while (!a.equals(strarray.get(icount))){
            icount++;
        }
        return icount;
    }
    /*
    * 生成四元式的主要函数
    * getquad(LinkedList<String> strarray,int start,int end)
    * @param strarray 存放当前进行语义分析的词列表
    * @param start    递归函数进行的开始下标
    * @param end      递归函数进行的结束下标
    *
    * 因为 if else while do将语句分割成两块，一块是表达式，一块是任意多的语句
    * 所以使用递归函数分别对两块进行分析。
    * 语义分析中，在语句分析的时候，* / %的优先级 >  + -的优先级 > 逻辑运算符优先级 > 赋值
    * 所以循环中先将* / %生成四元式，以此类推。
    * */
    public void getquad(LinkedList<String> strarray,int start,int end){
        for(int i=start;i<end;i++) {   // 语句 进入递归
            String str = strarray.get(i);
            if(str.equals("if")||str.equals("while")){
                strarray.remove(i);
                int icount=0;
                String a="";
                switch(str){
                    case "if":a="then";break;
                    case "while":a="do";break;
                }
                icount=this.geticount(strarray,a);
                getquad(strarray,i,icount-1); //if或while语句前半部分的递归

                icount=this.geticount(strarray,a);
                strarray.remove(icount);            //if或while语句后半部分的递归
                getquad(strarray,icount,strarray.size()-1);
                end=strarray.size();
            }
        }
        for(int i=start;i<end;i++) {   //输出 * / % 的四元式
            String str = strarray.get(i);
            if (str.equals("*") || str.equals("/") || str.equals("%")) {
            String tstr = this.gettcount();
            System.out.print("<"+semcount+">  ");
                switch (str) {
                    case "*":
                        System.out.println("{*," + strarray.get(i - 1) + "," +
                                strarray.get(i + 1) + "," + tstr + "}");
                        break;
                    case "/":
                        System.out.println("{/," + strarray.get(i - 1) + "," +
                                strarray.get(i + 1) + "," + tstr + "}");
                        break;
                    case "%":
                        System.out.println("{%," + strarray.get(i - 1) + "," +
                                strarray.get(i + 1) + "," + tstr + "}");
                        break;
                }
                semcount++;
                this.removestrlist(i,tstr);
                i-=1;
                end-=2;
            }
        }
        for(int i=start;i<end;i++) {  //输出 + - 的四元式
            String str = strarray.get(i);
            if(str.equals("+")||str.equals("-")) {
                String tstr = this.gettcount();
                System.out.print("<"+semcount+">  ");
                switch (str) {
                    case "+":System.out.println("{+," + strarray.get(i - 1) + "," +
                            strarray.get(i + 1) + "," + tstr + "}");break;
                    case "-":System.out.println("{-," + strarray.get(i - 1) + "," +
                            strarray.get(i + 1) + "," + tstr + "}");break;
                    }
                this.removestrlist(i,tstr);
                i-=1;    //删除掉 + - 后需要改变start和end对应的下表
                end-=2;
                semcount++;//输出后使语句数+1
                }
        }
        for(int i=start;i<end;i++) {     //输出 > >= < <= 的四元式
            String str = strarray.get(i);
            if (str.equals(">") || str.equals("<") || str.equals(">=")||str.equals("<=")) {
                System.out.print("<"+semcount+">  ");
                System.out.println("{j"+str+"," + strarray.get(i - 1) + "," +
                        strarray.get(i + 1) + ",<" + (semcount+1) + ">}");
                this.removelist(i);
                i-=1;
                end-=2;
                semcount++;
            }
        }
        for(int i=start;i<end;i++) {   //输出 赋值 的四元式
            String str = strarray.get(i);
            if(str.equals(":=")){
                String str1=strarray.get(i+1);
                boolean flag1=false;
                for(int j=0;j<tstack.size();j++){  //判断赋值是中间代码t* 还是数字 标识符等
                    if(str1.equals(tstack.get(j))){
                        flag1=true;
                    }
                }
                if(flag1){
                    String tstr = (String)this.tstack.peek();//this.gettcount(); 取t栈第一个 t*
                    System.out.print("<"+semcount+">  ");
                    System.out.println("{:=," + tstr + "," + "_" + "," + strarray.get(i - 1) + "}");
                    this.removestrlist(i,tstr);
                }else {
                    System.out.print("<"+semcount+">  ");
                    System.out.println("{:=," + str1 + "," + "_" + "," + strarray.get(i - 1) + "}");
                    removelist(i);
                }
                i-=1;
                end-=2;
                semcount++;
            }
        }
    }
    private void removestrlist(int i,String tstr){ //对字符列表进行操作
        this.strlist.remove(i+1);
        this.strlist.remove(i);
        this.strlist.add(i,tstr);       //添加中间代码t*  如 运算 a+b 由t3表示 t3要加入列表 会改变字符列表的长度
        this.strlist.remove(i-1);
    }
    private void removelist(int i){   //识别完成的不需要添加 如 if语句 while语句等
        this.strlist.remove(i+1);
        this.strlist.remove(i);
        this.strlist.remove(i-1);
    }
    public String gettcount(){           //获取当前的t个数，并把使用的t*压入栈中
        this.tstack.push("t"+tcount);
        return "t"+tcount++;
    }
    public void init(String txtpath){
        Grammer g=new Grammer();   //语法分析对象
        lexical l=new lexical();    //词法分析对象
        l.lexicalanalysis(txtpath);  //词法分析 读取txt文件转化为单个的词 并记录
        String[] str1=l.getStringArray();

        int i=0;
        for(String str:str1){
            g.setListstack(l.getListstack()[i]);
            g.cut(str);
            this.strstack=g.getStrstack();
            int strstacklen=this.strstack.length;
            for(int ij=0;ij<strstacklen;ij++){
                strlist.add(ij,this.strstack[ij]);
            }
            g.praseS();    //语法分析的开始状态

            if(g.getgrammerflag()&&g.getCount()==str.length()&&g.getStackcount()==g.getStrstacklength()){
                System.out.println();
                this.getquad(this.strlist,0,this.strlist.size()-1);
            }else
                System.out.println(str+"语法有错!不进行语义分析");
            i++;
            g.init();
            this.strlist.clear();
        }
    }
    public static void main(String args[]) {
        semantic s=new semantic();
        s.init("src/com/morphology/test1.txt");
//        s.init("src/com/morphology/ex3-2.txt");
    }

}
class quad{
    String result;
    String arg2;
    String arg1;
    String op;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getArg2() {
        return arg2;
    }

    public void setArg2(String arg2) {
        this.arg2 = arg2;
    }

    public String getArg1() {
        return arg1;
    }

    public void setArg1(String arg1) {
        this.arg1 = arg1;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    @Override
    public String toString() {
        return "{" + op + "," +arg1 + "," + arg2 +","+result + "}";
    }
}