import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.stream.StreamSupport;

public class BigInteger
{
    public final int plustoInt = '+';
    public final int minustoInt ='-';
    public final int multitoInt = '*';
    public static final String QUIT_COMMAND = "quit";
    public static final String MSG_INVALID_INPUT = "Input is wrong";
  
    // implement this
    public static final Pattern EXPRESSION_PATTERN = Pattern.compile("");
    public int[] bigInteger;

    public BigInteger(int i)
    {
        bigInteger = new int[2];
        if(i<0){
            bigInteger[0] = '-';
            bigInteger[1]=-i;
        }
        else{
            bigInteger[0] = '+';
            bigInteger[1]=i;
        }
    }

    public BigInteger(BigInteger big){
        bigInteger = big.bigInteger;
    }

    public BigInteger(int[] a){
        bigInteger = a;
    }

    public BigInteger(String s){
        bigInteger = new int[s.length()];
        bigInteger[0]=s.charAt(0);
        for(int i=1;i<s.length();i++){
            bigInteger[i]=s.charAt(i)-'0';
        }
    }
    public int addThree(int x, int y, int z){
        return (x+y+z)%10;
    }
    public int addThreeC(int x, int y, int z){
        if((x+y+z)>=10) return 1;
        else return 0;
    }

    public int findSignLocation(int[] target){
        int i=0;
        while((i< target.length) && (target[i] !=plustoInt) && (target[i] !=minustoInt)){
            i++;
        }
        return i;
    }

    public BigInteger add(BigInteger big)
    {
        BigInteger res;
        int c = 0;
        int[] result = new int[103];
        int[] temp;
        if((big.bigInteger[0]==plustoInt)&&(bigInteger[0]==plustoInt)){
            for(int i=1;(i<bigInteger.length&&i<big.bigInteger.length);i++){
                if(i==1){
                    result[i-1] = addThree(big.bigInteger[big.bigInteger.length-i],bigInteger[bigInteger.length-i],0);
                    c = addThreeC(big.bigInteger[big.bigInteger.length-i],bigInteger[bigInteger.length-i],0);
                }else{
                    result[i-1] = addThree(big.bigInteger[big.bigInteger.length-i],bigInteger[bigInteger.length-i],c);
                    c = addThreeC(big.bigInteger[big.bigInteger.length-i],bigInteger[bigInteger.length-i],c);
                }
            }
            if(bigInteger.length>big.bigInteger.length){
                for(int i=big.bigInteger.length;i<bigInteger.length;i++) {
                    result[i - 1] = addThree(c, bigInteger[bigInteger.length - i], 0);
                    c = addThreeC(c, bigInteger[bigInteger.length - i], 0);
                }
                if(c==0) {
                    result[bigInteger.length - 1] = '+';
                }else{
                    result[bigInteger.length-1]=c;
                    result[bigInteger.length]='+';
                }
            }else if(big.bigInteger.length>bigInteger.length){
                for(int i=bigInteger.length;i<big.bigInteger.length;i++) {
                    result[i - 1] = addThree(c,big.bigInteger[big.bigInteger.length-i],0);
                    c=addThreeC(c,big.bigInteger[big.bigInteger.length-i],0);
                }
                if(c==0) {
                    result[big.bigInteger.length - 1] = '+';
                }else{
                    result[big.bigInteger.length-1]=c;
                    result[big.bigInteger.length]='+';
                }

            }else{
                if(c==1){
                result[bigInteger.length-1]=c;
                result[bigInteger.length]='+';
                }else{
                    result[bigInteger.length-1]='+';
                }
            }
            int realArraySize = findSignLocation(result)+1;
            temp = new int[realArraySize];
            for(int m=0;m<realArraySize;m++){
                temp[m]=result[realArraySize-1-m];
            }
            res = new BigInteger(temp);
        }else if((big.bigInteger[0]==minustoInt)&&(bigInteger[0]==minustoInt)){
            for(int i=1;(i<bigInteger.length&&i<big.bigInteger.length);i++){
                if(i==1){
                    result[i-1] = addThree(big.bigInteger[big.bigInteger.length-i],bigInteger[bigInteger.length-i],0);
                    c = addThreeC(big.bigInteger[big.bigInteger.length-i],bigInteger[bigInteger.length-i],0);
                }else{
                    result[i-1] = addThree(big.bigInteger[big.bigInteger.length-i],bigInteger[bigInteger.length-i],c);
                    c = addThreeC(big.bigInteger[big.bigInteger.length-i],bigInteger[bigInteger.length-i],c);
                }
            }
            if(bigInteger.length>big.bigInteger.length){
                for(int i=big.bigInteger.length;i<bigInteger.length;i++) {
                    result[i - 1] = addThree(c, bigInteger[bigInteger.length - i], 0);
                    c = addThreeC(c, bigInteger[bigInteger.length - i], 0);
                }
                if(c==0) {
                    result[bigInteger.length - 1] = '-';
                }else{
                    result[bigInteger.length-1]=c;
                    result[bigInteger.length]='-';
                }
            }else if(big.bigInteger.length>bigInteger.length){
                for(int i=bigInteger.length;i<big.bigInteger.length;i++) {
                    result[i - 1] = addThree(c,big.bigInteger[big.bigInteger.length-i],0);
                    c=addThreeC(c,big.bigInteger[big.bigInteger.length-i],0);
                }
                if(c==0) {
                    result[big.bigInteger.length - 1] = '-';
                }else{
                    result[big.bigInteger.length-1]=c;
                    result[big.bigInteger.length]='-';
                }

            }else{
                if(c==1){
                    result[bigInteger.length-1]=c;
                    result[bigInteger.length]='-';
                }else{
                    result[bigInteger.length-1]='-';
                }
            }
            int realArraySize = findSignLocation(result)+1;
            temp = new int[realArraySize];
            for(int m=0;m<realArraySize;m++){
                temp[m]=result[realArraySize-1-m];
            }
            res = new BigInteger(temp);
        }else if((bigInteger[0]==plustoInt)&&(big.bigInteger[0]==minustoInt)){
            big.bigInteger[0]='+';
            res = new BigInteger(subtract(big));
        }else{
            big.bigInteger[0]='-';
            res = new BigInteger(subtract(big));
        }

        return res;
    }

    public int compare(int[] a, int[] b){
        for(int i=1;i<a.length;i++){
            if(a[i]>b[i]){
                return 1;
            }else if(b[i]>a[i]){
                return 2;
            }
        }
        return 0;
    }
  
    public BigInteger subtract(BigInteger big)
    {
        int c=0;
        int[] result = new int[103];
        BigInteger res;
        if((bigInteger[0]==plustoInt)&&(big.bigInteger[0]==minustoInt)){
            big.bigInteger[0]='+';
            res = new BigInteger(add(big));
            return res;
        }else if((bigInteger[0]==minustoInt)&&(big.bigInteger[0]==plustoInt)){
            big.bigInteger[0]='-';
            res = new BigInteger(add(big));
            return res;
        }else if((big.bigInteger[0]==plustoInt)&&(bigInteger[0]==plustoInt)){
            if(big.bigInteger.length>bigInteger.length){
                if(big.bigInteger[big.bigInteger.length-1]>=bigInteger[bigInteger.length-1]){
                    result[0]=big.bigInteger[big.bigInteger.length-1]-bigInteger[bigInteger.length-1];
                }else if(big.bigInteger[big.bigInteger.length-1]<bigInteger[bigInteger.length-1]){
                    c=-1;
                    result[0]=10+big.bigInteger[big.bigInteger.length-1]-bigInteger[bigInteger.length-1];
                }
                for(int i=2;i<bigInteger.length;i++){
                    if((big.bigInteger[big.bigInteger.length-i]+c)>=bigInteger[bigInteger.length-i]){
                        result[i-1]=big.bigInteger[big.bigInteger.length-i]+c-bigInteger[bigInteger.length-i];
                        c=0;
                    }else{
                        result[i-1]=10+big.bigInteger[big.bigInteger.length-i]+c-bigInteger[bigInteger.length-i];
                        c=-1;
                    }
                }
                for(int i=bigInteger.length;i<big.bigInteger.length;i++){
                    if(big.bigInteger[big.bigInteger.length-i]+c>=0){
                        result[i-1]=big.bigInteger[big.bigInteger.length-i]+c;
                        c=0;
                    }else{
                        result[i-1]=big.bigInteger[big.bigInteger.length-i]+c+10;
                        c=-1;
                    }
                }
                result[big.bigInteger.length-1]='-';

            }else if(bigInteger.length>big.bigInteger.length){

                 if(bigInteger[bigInteger.length-1]>=big.bigInteger[big.bigInteger.length-1]){
                    result[0]=bigInteger[bigInteger.length-1]-big.bigInteger[big.bigInteger.length-1];
                }else if(bigInteger[bigInteger.length-1]<big.bigInteger[big.bigInteger.length-1]) {
                    c=-1;
                    result[0]=10+bigInteger[bigInteger.length-1]-big.bigInteger[big.bigInteger.length-1];
                }
                for(int i=2;i<big.bigInteger.length;i++){
                    if((bigInteger[bigInteger.length-i]+c)>=big.bigInteger[big.bigInteger.length-i]){
                        result[i-1]=bigInteger[bigInteger.length-i]+c-big.bigInteger[big.bigInteger.length-i];
                        c=0;
                    }else{
                        result[i-1]=10+bigInteger[bigInteger.length-i]+c-big.bigInteger[big.bigInteger.length-i];
                        c=-1;
                    }
                }
                for(int i=big.bigInteger.length;i<bigInteger.length;i++){
                    if(bigInteger[bigInteger.length-i]+c>=0){
                        result[i-1]=bigInteger[bigInteger.length-i]+c;
                        c=0;
                    }else{
                        result[i-1]=bigInteger[bigInteger.length-i]+c+10;
                        c=-1;
                    }
                    }
                    result[bigInteger.length-1]='+';

            }else {
                if(compare(big.bigInteger,bigInteger)==1){
                    if(big.bigInteger[big.bigInteger.length-1]>=bigInteger[bigInteger.length-1]){
                    result[0]=big.bigInteger[big.bigInteger.length-1]-bigInteger[bigInteger.length-1];
                }else if(big.bigInteger[big.bigInteger.length-1]<bigInteger[bigInteger.length-1]) {
                    c=-1;
                    result[0]=10+big.bigInteger[bigInteger.length-1]-bigInteger[bigInteger.length-1];
                }
                for(int i=2;i<bigInteger.length;i++){
                    if((big.bigInteger[bigInteger.length-i]+c)>=bigInteger[bigInteger.length-i]){
                        result[i-1]=big.bigInteger[bigInteger.length-i]+c-bigInteger[bigInteger.length-i];
                        c=0;
                    }else{
                        result[i-1]=10+big.bigInteger[big.bigInteger.length-i]+c-bigInteger[bigInteger.length-i];
                        c=-1;
                    }
                }

                result[big.bigInteger.length-1]='-';

                }else if(compare(big.bigInteger,bigInteger)==2){
                    if(bigInteger[bigInteger.length-1]>=big.bigInteger[bigInteger.length-1]){
                    result[0]=bigInteger[bigInteger.length-1]-big.bigInteger[bigInteger.length-1];
                }else if(bigInteger[bigInteger.length-1]<big.bigInteger[bigInteger.length-1]) {
                    c=-1;
                    result[0]=10+bigInteger[bigInteger.length-1]-big.bigInteger[bigInteger.length-1];
                }
                for(int i=2;i<bigInteger.length;i++){
                    if((bigInteger[bigInteger.length-i]+c)>=big.bigInteger[bigInteger.length-i]){
                        result[i-1]=bigInteger[bigInteger.length-i]+c-big.bigInteger[bigInteger.length-i];
                        c=0;
                    }else{
                        result[i-1]=10+bigInteger[bigInteger.length-i]+c-big.bigInteger[bigInteger.length-i];
                        c=-1;
                    }

                }
                    result[bigInteger.length-1]='+';
                }
                else{
                result[0]=0;
                result[1]='+';
                }
            }
        }else if((big.bigInteger[0]==minustoInt)&&(bigInteger[0]==minustoInt)){


            if(big.bigInteger.length>bigInteger.length){
                if(big.bigInteger[big.bigInteger.length-1]>=bigInteger[bigInteger.length-1]){
                    result[0]=big.bigInteger[big.bigInteger.length-1]-bigInteger[bigInteger.length-1];
                }else if(big.bigInteger[big.bigInteger.length-1]<bigInteger[bigInteger.length-1]){
                    c=-1;
                    result[0]=10+big.bigInteger[big.bigInteger.length-1]-bigInteger[bigInteger.length-1];
                }
                for(int i=2;i<bigInteger.length;i++){
                    if((big.bigInteger[big.bigInteger.length-i]+c)>=bigInteger[bigInteger.length-i]){
                        result[i-1]=big.bigInteger[big.bigInteger.length-i]+c-bigInteger[bigInteger.length-i];
                        c=0;
                    }else{
                        result[i-1]=10+big.bigInteger[big.bigInteger.length-i]+c-bigInteger[bigInteger.length-i];
                        c=-1;
                    }
                }
                for(int i=bigInteger.length;i<big.bigInteger.length;i++){
                    if(big.bigInteger[big.bigInteger.length-i]+c>=0){
                        result[i-1]=big.bigInteger[big.bigInteger.length-i]+c;
                        c=0;
                    }else{
                        result[i-1]=big.bigInteger[big.bigInteger.length-i]+c+10;
                        c=-1;
                    }
                }
                result[big.bigInteger.length-1]='+';

            }else if(bigInteger.length>big.bigInteger.length){

                 if(bigInteger[bigInteger.length-1]>=big.bigInteger[big.bigInteger.length-1]){
                    result[0]=bigInteger[bigInteger.length-1]-big.bigInteger[big.bigInteger.length-1];
                }else if(bigInteger[bigInteger.length-1]<big.bigInteger[big.bigInteger.length-1]) {
                    c=-1;
                    result[0]=10+bigInteger[bigInteger.length-1]-big.bigInteger[big.bigInteger.length-1];
                }
                for(int i=2;i<big.bigInteger.length;i++){
                    if((bigInteger[bigInteger.length-i]+c)>=big.bigInteger[big.bigInteger.length-i]){
                        result[i-1]=bigInteger[bigInteger.length-i]+c-big.bigInteger[big.bigInteger.length-i];
                        c=0;
                    }else{
                        result[i-1]=10+bigInteger[bigInteger.length-i]+c-big.bigInteger[big.bigInteger.length-i];
                        c=-1;
                    }
                }
                for(int i=big.bigInteger.length;i<bigInteger.length;i++){
                    if(bigInteger[bigInteger.length-i]+c>=0){
                        result[i-1]=bigInteger[bigInteger.length-i]+c;
                        c=0;
                    }else{
                        result[i-1]=bigInteger[bigInteger.length-i]+c+10;
                        c=-1;
                    }
                    }
                    result[bigInteger.length-1]='-';

            }else {
                if(compare(big.bigInteger,bigInteger)==1){
                    if(big.bigInteger[big.bigInteger.length-1]>=bigInteger[bigInteger.length-1]){
                    result[0]=big.bigInteger[big.bigInteger.length-1]-bigInteger[bigInteger.length-1];
                }else if(big.bigInteger[big.bigInteger.length-1]<bigInteger[bigInteger.length-1]) {
                    c=-1;
                    result[0]=10+big.bigInteger[bigInteger.length-1]-bigInteger[bigInteger.length-1];
                }
                for(int i=2;i<bigInteger.length;i++){
                    if((big.bigInteger[bigInteger.length-i]+c)>=bigInteger[bigInteger.length-i]){
                        result[i-1]=big.bigInteger[bigInteger.length-i]+c-bigInteger[bigInteger.length-i];
                        c=0;
                    }else{
                        result[i-1]=10+big.bigInteger[big.bigInteger.length-i]+c-bigInteger[bigInteger.length-i];
                        c=-1;
                    }
                }

                result[big.bigInteger.length-1]='+';

                }else if(compare(big.bigInteger,bigInteger)==2){
                    if(bigInteger[bigInteger.length-1]>=big.bigInteger[bigInteger.length-1]){
                    result[0]=bigInteger[bigInteger.length-1]-big.bigInteger[bigInteger.length-1];
                }else if(bigInteger[bigInteger.length-1]<big.bigInteger[bigInteger.length-1]) {
                    c=-1;
                    result[0]=10+bigInteger[bigInteger.length-1]-big.bigInteger[bigInteger.length-1];
                }
                for(int i=2;i<bigInteger.length;i++){
                    if((bigInteger[bigInteger.length-i]+c)>=big.bigInteger[bigInteger.length-i]){
                        result[i-1]=bigInteger[bigInteger.length-i]+c-big.bigInteger[bigInteger.length-i];
                        c=0;
                    }else{
                        result[i-1]=10+bigInteger[bigInteger.length-i]+c-big.bigInteger[bigInteger.length-i];
                        c=-1;
                    }

                }
                    result[bigInteger.length-1]='-';
                }
                else{
                result[0]=0;
                result[1]='+';
                }
            }
        }
        int signLocation = findSignLocation(result);
        int startingPoint = findStartingPointIndex(result,signLocation);
        int[] temp = new int[startingPoint+2];
        temp[0]=result[signLocation];
        for(int n=startingPoint;n>=0;n--){
            temp[startingPoint-n+1] = result[n];
        }
        res= new BigInteger(temp);
        return res;
    }
    public int findStartingPointIndex(int[] target,int signLocation){
        for(int i=signLocation;i>=0;i--){
            if(target[i]!=plustoInt && target[i]!=minustoInt && target[i]!=0){
                return i;
            }
        }
        return 0;
    }
    public int multiNadd(int a, int b, int c){
        return (a*b+c)%10;
    }
    public int multiNaddC(int a, int b, int c){
        return (a*b+c)/10;
    }

    public BigInteger multiply(BigInteger big)
    {
        BigInteger res;
        int[] result = new int[201];
        int[] temp;
        int tp;
        int c;
        BigInteger valueZero = new BigInteger(0);
        if((compare(big.bigInteger,valueZero.bigInteger)==0)||(compare(bigInteger,valueZero.bigInteger)==0)){
            return valueZero;
        }else{
        for(int i=big.bigInteger.length-1;i>0;i--){
            c=0;
            for(int j=bigInteger.length-1;j>0;j--){
                tp = result[bigInteger.length-1-j+big.bigInteger.length-1-i];
                result[bigInteger.length-1-j+big.bigInteger.length-1-i] = multiNadd(bigInteger[j],big.bigInteger[i],(c+tp));
                c= multiNaddC(bigInteger[j],big.bigInteger[i],(c+tp));
            }
            result[big.bigInteger.length-1-i+bigInteger.length-1]=c;
        }
        if(result[big.bigInteger.length+bigInteger.length-3]==0){
            if(big.bigInteger[0]==bigInteger[0]){result[bigInteger.length+big.bigInteger.length-3]='+';}
            else {
                result[bigInteger.length+big.bigInteger.length - 3]='-';
            }
            temp = new int[big.bigInteger.length+bigInteger.length-2];
            for(int k=0;k<temp.length;k++){
                temp[k]=result[temp.length-1-k];
            }
            res = new BigInteger(temp);
            return res;
        }else{
        if(big.bigInteger[0]==bigInteger[0]){result[bigInteger.length+big.bigInteger.length-2]='+';}
        else {
            result[bigInteger.length+big.bigInteger.length - 2]='-';
        }
            temp = new int[big.bigInteger.length+bigInteger.length-1];
            for(int k=0;k<temp.length;k++){
                temp[k]=result[temp.length-1-k];
            }
            res = new BigInteger(temp);
            return res;
        }}

    }
  
    @Override
    public String toString()
    {
        String result = new String("");
        if(bigInteger[0]==plustoInt){
            for(int i=1;i<bigInteger.length;i++){
                result+=bigInteger[i];
            }
        }
        else{
            result="-";
            for(int i=1;i<bigInteger.length;i++){
                result+=bigInteger[i];
            }
        }
        return result;
    }
  
    static BigInteger evaluate(String input) throws IllegalArgumentException
    {
        int[] operatorNsignPoint = new int[3];
        int numOfoperatorNsign=0;
        char[] operatorNsign = new char[3];
        char operator;
        input = input.replaceAll(" ","");
        String num1, num2;
        for(int i=0;i<input.length();i++){
            if(input.charAt(i)=='+'||input.charAt(i)=='-'||input.charAt(i)=='*'){
                operatorNsignPoint[numOfoperatorNsign]=i;
                operatorNsign[numOfoperatorNsign++]=input.charAt(i);
            }
        }
        if(numOfoperatorNsign==1){
            num1 = "+" + input.substring(0,operatorNsignPoint[0]);
            operator=operatorNsign[0];
            num2 = "+" + input.substring(operatorNsignPoint[0]+1);
        }
        else if(numOfoperatorNsign==2){
            if(operatorNsignPoint[0]==0){
                num1=input.substring(0,operatorNsignPoint[1]);
                operator=operatorNsign[1];
                num2="+" + input.substring(operatorNsignPoint[1]+1);
            }
            else{
                num1="+" + input.substring(0,operatorNsignPoint[0]);
                operator=operatorNsign[0];
                num2=input.substring(operatorNsignPoint[1]);
            }
        }
        else{
            num1=input.substring(0,operatorNsignPoint[1]);
            operator=operatorNsign[1];
            num2=input.substring(operatorNsignPoint[2]);
        }
        BigInteger operand1 = new BigInteger(num1);
        BigInteger operand2 = new BigInteger(num2);
        BigInteger result = null;
        switch (operator){
            case '+':
                result=operand1.add(operand2);
                break;
            case '-':
                result=operand1.subtract(operand2);
                break;
            case '*':
                result=operand1.multiply(operand2);
                break;
            default:
                break;
        }

        return result;

    }


    public static void main(String[] args) throws Exception {
        try (InputStreamReader isr = new InputStreamReader(System.in)) {
            try (BufferedReader reader = new BufferedReader(isr)) {
                boolean done = false;
                while (!done) {
                    String input = reader.readLine();

                    try {
                        done = processInput(input);
                    } catch (IllegalArgumentException e) {
                        System.err.println(MSG_INVALID_INPUT);
                    }
                }
            }
        }
    }
  
    static boolean processInput(String input) throws IllegalArgumentException
    {
        boolean quit = isQuitCmd(input);
  
        if (quit)
        {
            return true;
        }
        else
        {
            BigInteger result = evaluate(input);
            System.out.println(result.toString());
  
            return false;
        }
    }
  
    static boolean isQuitCmd(String input)
    {
        return input.equalsIgnoreCase(QUIT_COMMAND);
    }
}
