import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class CalculatorTest
{
	public static void main(String args[])
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while (true)
		{
			try
			{
				String input = br.readLine();
				if (input.compareTo("q") == 0)
					break;

				command(input);
			}
			catch (Exception e)
			{
				System.out.println("ERROR");
			}
		}
	}

	private static void command(String input)
	{

		Stack<Long> operatorStack = new Stack<>();
		LinkedList<Long> tempPostfix = new LinkedList<>();
		LinkedList<Character> digitOrOperPostFix = new LinkedList<>();
		boolean digitPreviously = false;
		boolean popOperator = false;
		String tempTrimInput = "";
		/*String -> Long 변환 시작*/
		for(int i=0;i< input.length();i++){
			if(tempTrimInput.isEmpty()){
				if(input.charAt(i)==' '||input.charAt(i)=='\t'){}
				else{tempTrimInput = input.charAt(i)+"";}
			}else{
				if(input.charAt(i)==' '||input.charAt(i)=='\t'){
					if(tempTrimInput.charAt(tempTrimInput.length()-1)==' '||tempTrimInput.charAt(tempTrimInput.length()-1)=='\t'){}
					else{ tempTrimInput+=" ";}
				}else if(input.charAt(i)>='0'&&input.charAt(i)<='9'){
					if(tempTrimInput.charAt(tempTrimInput.length()-1)>='0'&&tempTrimInput.charAt(tempTrimInput.length()-1)<='9'){
						tempTrimInput+=input.charAt(i);
					}else if(tempTrimInput.charAt(tempTrimInput.length()-1)==' '||tempTrimInput.charAt(tempTrimInput.length()-1)=='\t'){
						tempTrimInput+=input.charAt(i);
					}else{
						tempTrimInput+=" "+input.charAt(i);
					}
				}
				else{
					if(tempTrimInput.charAt(tempTrimInput.length()-1)>='0'&&tempTrimInput.charAt(tempTrimInput.length()-1)<='9'){
						tempTrimInput+=" "+input.charAt(i);
					}else if(tempTrimInput.charAt(tempTrimInput.length()-1)==' '||tempTrimInput.charAt(tempTrimInput.length()-1)=='\t'){
						tempTrimInput+=input.charAt(i);
					}else{
						tempTrimInput+=" "+input.charAt(i);
					}
				}
			}
		}

		String[] tempInputArray = tempTrimInput.split("\\s");
		char[] digitOrOper = new char[tempInputArray.length];
			long[] inputArray = new long[tempInputArray.length];
		for(int i=0;i<inputArray.length;i++){
			if(tempInputArray[i].equals("(")||tempInputArray[i].equals(")")||tempInputArray[i].equals("^")||tempInputArray[i].equals("-")||tempInputArray[i].equals("*")||tempInputArray[i].equals("/")||tempInputArray[i].equals("%")||tempInputArray[i].equals("+")){
				inputArray[i] = tempInputArray[i].charAt(0)-'0';
				digitOrOper[i]='o';
			}else{
				inputArray[i]=Long.parseLong(tempInputArray[i]);
				digitOrOper[i]='d';
			}
		}
		/*변환 끝*/

		for(int i=0;i<inputArray.length;i++){
			//TODO ch
			long ch = inputArray[i];
					if(digitOrOper[i]=='d'){
						//여기는 숫자 받았음
						if(digitPreviously==true){
							// 숫자 + 숫자
							System.out.println("ERROR");
							return;
						}else{
							// 연산자 + 숫자
							if(!operatorStack.empty()&&(operatorStack.peek()=='%'-'0'|| operatorStack.peek()=='/'-'0')){
								if(ch==0){
									System.out.println("ERROR");
									return;
								}
							}
							tempPostfix.add(ch);
							digitOrOperPostFix.add('d');
						}
						digitPreviously = true;
						popOperator = true;
					}else{
						//연산자가 들어왔을 때
						digitPreviously=false;
						if(popOperator==true){
							popOperator=false;
							//숫자 + 연산자(=ch)
							if(ch=='('-'0'){
								System.out.println("ERROR");
								return;
							}else if(ch==')'-'0'){
								// 닫히는 괄호 올때
								while(!operatorStack.empty()&&(operatorStack.peek()!='('-'0')){
									tempPostfix.addLast(operatorStack.pop());
									digitOrOperPostFix.addLast('o');
								}
								if(operatorStack.empty()){
									System.out.println("ERROR");
									return;}
								else{
									operatorStack.pop();
									popOperator=true;
									digitPreviously=true;
								}
							}else if(ch=='^'-'0'){
								operatorStack.push(ch);
							}else if(ch=='*'-'0'||ch=='/'-'0'||ch=='%'-'0'){
								while(!operatorStack.empty() && (operatorStack.peek()=='^'-'0'||operatorStack.peek()=='~'-'0'||operatorStack.peek()=='*'-'0'||operatorStack.peek()=='/'-'0'||operatorStack.peek()=='%'-'0')){
									tempPostfix.addLast(operatorStack.pop());
									digitOrOperPostFix.addLast('o');
								}
								operatorStack.push(ch);
							}else{
								while(!operatorStack.empty() && (operatorStack.peek()=='^'-'0'||operatorStack.peek()=='*'-'0'||operatorStack.peek()=='/'-'0'||operatorStack.peek()=='%'-'0'||operatorStack.peek()=='~'-'0'||operatorStack.peek()=='+'-'0'||operatorStack.peek()=='-'-'0')){
									tempPostfix.addLast(operatorStack.pop());
									digitOrOperPostFix.addLast('o');
								}
								operatorStack.push(ch);

							}
							}else{
							//연산자 + 연산자(=ch) or 맨 앞(ch)
							popOperator=false;
							if(ch=='-'-'0'){
								if(!operatorStack.empty() && (operatorStack.peek()=='^'-'0')&&tempPostfix.getLast()==0){
									System.out.println("ERROR");
									return;
								}
								operatorStack.push((long)'~'-'0');
							}
							else if(ch=='('-'0'){
								operatorStack.push(ch);
							}
							else if(ch==')'-'0'){
								if(!operatorStack.empty() && operatorStack.peek()=='('-'0'){
									operatorStack.pop();
									popOperator=true;
									digitPreviously = true;
								}else{
								System.out.println("ERROR");
								return;}
							}else if(operatorStack.empty()){
								operatorStack.push(ch);
							}else if(ch=='+'-'0'){
								while(!operatorStack.empty() && (operatorStack.peek()=='^'-'0'||operatorStack.peek()=='*'-'0'||operatorStack.peek()=='/'-'0'||operatorStack.peek()=='%'-'0'||operatorStack.peek()=='~'-'0'||operatorStack.peek()=='+'-'0'||operatorStack.peek()=='-'-'0')){
									tempPostfix.addLast(operatorStack.pop());
									digitOrOperPostFix.addLast('o');
								}
								operatorStack.push(ch);
							}else{
								System.out.println("ERROR");
								return;
							}
						}
					}
		}
		while(!operatorStack.empty()){
			tempPostfix.addLast(operatorStack.pop());
			digitOrOperPostFix.addLast('o');
		}
		int k=0;
		while(k<digitOrOperPostFix.size()&&digitOrOperPostFix.get(k)=='o'){
			k++;
		}
		if(k==digitOrOperPostFix.size()){
			System.out.println("ERROR");
			return;
		}
		long result = evaluate(tempPostfix,digitOrOperPostFix);

		for(int i=0;i<tempPostfix.size();i++){
			if(i==tempPostfix.size()-1){
				if(digitOrOperPostFix.get(i)=='o'){
				System.out.print( (char) (tempPostfix.get(i)+'0'));
			}else System.out.print(tempPostfix.get(i).toString());
			}else{
			if(digitOrOperPostFix.get(i)=='o'){
				System.out.print( (char) (tempPostfix.get(i)+'0') + " ");
			}else System.out.print(tempPostfix.get(i).toString()+" ");
		}}
		System.out.println();
		System.out.println(result);

	}
	/*postfix 계산 수업 자료 참고*/
	private static long evaluate(LinkedList<Long> target,LinkedList<Character> digitOrOper){
		long a,b;
		Stack<Long> s = new Stack<>();
		for(int i=0;i<target.size();i++){
			long ch = target.get(i);
			if(digitOrOper.get(i)=='d'){
				s.push(ch);
			}
			else{
				long val =0;
				if(ch=='~'-'0'){
					a=s.pop();
					val = (-1)*a;
				}
				else{
				a=s.pop();
				b=s.pop();
				if(a==0){
					if(ch=='/'-'0'||ch=='%'-'0'){
						throw new ArithmeticException();
					}
				}
				else if(b==0){
					if(ch=='^'-'0'&&a<0){
						throw new ArithmeticException();
					}
				}
				val = operation(a,b,ch);
				}
				s.push(val);
			}

		}
		return s.pop();
	}

	private static long operation(long a, long b, long ch){
		long val =0;
		if(ch=='^'-'0'){
			val = (long) Math.pow(b,a);
		}else if(ch == '*'-'0'){
			val = b*a;
		}else if(ch == '/'-'0'){
			val = b/a;
		}else if(ch == '%'-'0'){
			val = b%a;
		}else if(ch=='-'-'0'){
			val = b-a;
		}else{
			val = b+a;
		}
		return val;
	}

}

