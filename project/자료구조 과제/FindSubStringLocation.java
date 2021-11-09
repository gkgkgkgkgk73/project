
import java.io.*;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

public class Matching
{
	public static void main(String args[])
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while (true)
		{
			try
			{
				String input = br.readLine();
				if (input.compareTo("QUIT") == 0)
					break;

				command(input);
			}
			catch (IOException e)
			{
				System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
			}
		}
	}

	private static void command(String input) throws IOException {

		if(input.charAt(0)=='<'){
			getData(input.substring(2));
		}
		else if(input.charAt(0)=='@'){
			printData(input.substring(2));
		}
		else if(input.charAt(0)=='?'){
			searchString(input.substring(2));
		}
		else throw new IOException();
	}
	static Hashtable<Integer, AVLtree<String>> hashtable = new Hashtable(100);
	static int hashFunction(String value){
		int total=0;
		for(int i=0;i<value.length();i++){
			total+=value.charAt(i);
		}
		return total%100;
	}
	static void getData(String fileName) throws IOException {
		hashtable.clear();
		for(int i=0;i<100;i++){
		hashtable.put(i,new AVLtree<>());
		}
		FileReader fileReader = new FileReader(fileName);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		int lineNum=0;
		while(bufferedReader.ready()){
			lineNum++;
			String tmp = bufferedReader.readLine();
			for(int i=0;i<tmp.length()-5;i++){
				String tmpSixLength = tmp.substring(i,i+6);
				if(hashtable.get(hashFunction(tmpSixLength)).search(tmpSixLength)==hashtable.get(hashFunction(tmpSixLength)).NIL){
					hashtable.get(hashFunction(tmpSixLength)).insert(tmpSixLength);
					hashtable.get(hashFunction(tmpSixLength)).search(tmpSixLength).patternLocation.add(new Pair<>(lineNum,i+1));
				}else{
					hashtable.get(hashFunction(tmpSixLength)).search(tmpSixLength).patternLocation.add(new Pair<>(lineNum,i+1));
				}
			}
		}
	}
	static void printData(String slotString){
		int slotNum = Integer.parseInt(slotString);
		if(hashtable.get(slotNum).root==hashtable.get(slotNum).NIL){
			System.out.println("EMPTY");
		}else{
			hashtable.get(slotNum).printInPreOrder(hashtable.get(slotNum).root);
			System.out.println();
		}
	}
	static void searchString(String patternString){
		LinkedList<Pair<Integer,Integer>> Candidate = new LinkedList<>();
		Candidate.clear();
		String targetString;
		targetString = patternString.substring(0,6);
		if(hashtable.get(hashFunction(targetString)).search(targetString)!=hashtable.get(hashFunction(targetString)).NIL){
			for(int k=0;k<hashtable.get(hashFunction(targetString)).search(targetString).patternLocation.size();k++){
			Candidate.add(new Pair<>(hashtable.get(hashFunction(targetString)).search(targetString).patternLocation.get(k).key,hashtable.get(hashFunction(targetString)).search(targetString).patternLocation.get(k).value));
			}
			for(int i=1;i<patternString.length()-5;i++){
				targetString = patternString.substring(i,i+6);
				LinkedList<Pair<Integer,Integer>> tempCandidate = new LinkedList<>();
				if(hashtable.get(hashFunction(targetString)).search(targetString)!=hashtable.get(hashFunction(targetString)).NIL){
					for(int m=0;m<hashtable.get(hashFunction(targetString)).search(targetString).patternLocation.size();m++){
						Iterator<Pair<Integer,Integer>> iterator = Candidate.iterator();
						while (iterator.hasNext()){
							Pair<Integer,Integer> candidatePair= iterator.next();
						if(hashtable.get(hashFunction(targetString)).search(targetString).patternLocation.get(m).key.equals(candidatePair.key)){
							if(hashtable.get(hashFunction(targetString)).search(targetString).patternLocation.get(m).value.equals(candidatePair.value+1)){
								tempCandidate.add(new Pair<>(hashtable.get(hashFunction(targetString)).search(targetString).patternLocation.get(m).key,hashtable.get(hashFunction(targetString)).search(targetString).patternLocation.get(m).value));
							}
						}
					}}
					Candidate.clear();
					Candidate.addAll(tempCandidate);
				}else{
					System.out.println("(0, 0)");
					return;
				}

			}
		}else{
			System.out.println("(0, 0)");
			return;
		}
		if(Candidate.size()!=0){
			for(int l=0;l<Candidate.size();l++){
				Candidate.get(l).value=Candidate.get(l).value-(patternString.length()-6);
				System.out.print(Candidate.get(l));
				if(l!=(Candidate.size())-1){System.out.print(" ");}
			}
			System.out.println();
		}else{
			System.out.println("(0, 0)");
		}

	}
}


class Pair<K,V>{
	K key;
	V value;

	Pair(K k,V v){
		key=k;
		value=v;
	}

	K getKey(){
		return key;
	}
	V getValue(){
		return value;
	}

	void setKey(K k){
		key=k;
	}
	void setValue(V v){
		value=v;
	}

	public String toString(){
		return "("+key.toString()+", "+value.toString()+")";
	}
}
/*AVLTree,AVLNode는 수업 자료 참고*/
class AVLNode<T extends Comparable<T>>{
	T item;
	LinkedList<Pair<Integer,Integer>> patternLocation = new LinkedList<>();
	AVLNode<T> left, right;
	int height;
	AVLNode(T x, AVLNode<T> leftChild, AVLNode<T> rightChild){
		item = x;
		left=leftChild;
		right=rightChild;
		height=1;
	}
	AVLNode(T x, AVLNode leftChild, AVLNode rightChild, int h){
		item = x;
		left = leftChild;
		right = rightChild;
		height=h;
	}
}

class AVLtree<T extends Comparable<T>>{
	AVLNode<T> root;
	final AVLNode<T> NIL = new AVLNode<>(null, null, null, 0);
	AVLtree(){
		root = NIL;
	}
	AVLNode<T> search(T x){
		return searchItem(root,x);
	}
	AVLNode<T> searchItem(AVLNode<T> tNode, T x){
		if(tNode == NIL) return NIL;
		else if(x.compareTo(tNode.item)==0) return tNode;
		else if(x.compareTo(tNode.item)<0) return searchItem(tNode.left,x);
		else return searchItem(tNode.right,x);
	}

	void insert(T x){
		root = insertItem(root, x);
	}
	AVLNode<T> insertItem(AVLNode<T> tNode, T x){
		int type;
		if(tNode==NIL){
			tNode = new AVLNode<T>(x, NIL,NIL,1);
		}else if(x.compareTo(tNode.item)<0){
			tNode.left = insertItem(tNode.left,x);
			tNode.height = 1+ Math.max(tNode.right.height,tNode.left.height);
			type = needBalance(tNode);
			if(type!=NO_NEED){
				tNode = balanceAVL(tNode,type);
			}
		}else{
			tNode.right = insertItem(tNode.right,x);
			tNode.height = 1+ Math.max(tNode.right.height,tNode.left.height);
			type = needBalance(tNode);
			if(type!=NO_NEED){
				tNode = balanceAVL(tNode,type);
			}
		}
		return tNode;
	}
	void delete(T x){
		root = deleteItem(root,x);
	}
	AVLNode<T> deleteItem(AVLNode<T> tNode, T x){
		if(tNode==NIL){
			return NIL;
		}else{
			if(x.compareTo(tNode.item)==0){
				tNode = deleteNode(tNode);
			}else if(x.compareTo(tNode.item)<0){
				tNode.left = deleteItem(tNode.left,x);
				tNode.height = 1+Math.max(tNode.right.height,tNode.left.height);
				int type = needBalance(tNode);
				if(type!=NO_NEED){
					tNode = balanceAVL(tNode,type);
				}
			}else{
				tNode.right = deleteItem(tNode.right,x);
				tNode.height = 1+Math.max(tNode.right.height,tNode.left.height);
				int type = needBalance(tNode);
				if(type!=NO_NEED){
					tNode = balanceAVL(tNode,type);
				}
			}
			return tNode;
		}
	}
	AVLNode<T> deleteNode(AVLNode<T> tNode){
		if((tNode.left==NIL)&&(tNode.right==NIL)){
			return NIL;
		}else if(tNode.left==NIL){
			return tNode.right;
		}else if(tNode.right==NIL){
			return tNode.left;
		}else{
			returnPair<T> rPair = deleteMinItem(tNode.right);
			tNode.item = rPair.item;
			tNode.right=rPair.node;
			tNode.height = 1+Math.max(tNode.right.height,tNode.left.height);
			int type = needBalance(tNode);
			if(type!=NO_NEED){
				tNode = balanceAVL(tNode,type);
			}	return tNode;
		}
	}

	returnPair deleteMinItem(AVLNode<T> tNode){
		if(tNode.left==NIL){
			return new returnPair(tNode.item,tNode.right);
		}else{
			returnPair rPair = deleteMinItem(tNode.left);
			tNode.left = rPair.node;
			tNode.height = 1+Math.max(tNode.right.height,tNode.left.height);
			int type = needBalance(tNode);
			if(type!=NO_NEED){
				tNode=balanceAVL(tNode,type);
			}
			rPair.node = tNode;
			return rPair;
		}
	}
	class returnPair<T extends Comparable<T>>{
		T item;
		AVLNode<T> node;
		returnPair(T it, AVLNode<T> nd){
			item=it;
			node=nd;
		}
	}
	final int LL=1,LR=2,RR=3,RL=4,NO_NEED=0,ILLEGAL=-1;
	int needBalance(AVLNode<T> t){
		int type = ILLEGAL;
		if(t.left.height+2<=t.right.height){
			if(t.right.left.height<=t.right.right.height) type = RR;
			else type =RL;
		}else if(t.left.height>=t.right.height+2){
			if(t.left.left.height>=t.left.right.height) type=LL;
			else type=LR;
		}else type=NO_NEED;
		return type;
	}
	AVLNode<T> balanceAVL(AVLNode<T> tNode, int type){
		AVLNode<T> returnNode = NIL;
		switch(type){
			case LL:
				returnNode = rightRotate(tNode);
				break;
			case LR:
				tNode.left = leftRotate(tNode.left);
				returnNode = rightRotate(tNode);
				break;
			case RR:
				returnNode = leftRotate(tNode);
				break;
			case RL:
				tNode.right=rightRotate(tNode.right);
				returnNode=leftRotate(tNode);
				break;
			default:
				System.out.println("Impossible type! Should be one of LL,LR,RR,RL");
				break;
		}
		return returnNode;
	}

	AVLNode<T> leftRotate(AVLNode<T> t){
		AVLNode<T> RChild = t.right;
		if(RChild==NIL){
			System.out.println("t's RChild shouldn't be NIL!");
		}
		AVLNode<T> RLChild = RChild.left;
		RChild.left=t;
		t.right=RLChild;
		t.height=1+Math.max(t.left.height,t.right.height);
		RChild.height=1+Math.max(RChild.left.height,RChild.right.height);
		return RChild;
	}
	AVLNode<T> rightRotate(AVLNode<T> t){
		AVLNode<T> LChild=t.left;
		if(LChild==NIL){
			System.out.println("t's LChild shouldn't be NIL!");
		}
		AVLNode<T> LRChild = LChild.right;
		LChild.right=t;
		t.left=LRChild;
		t.height=1+Math.max(t.left.height,t.right.height);
		LChild.height=1+Math.max(LChild.left.height,LChild.right.height);
		return LChild;
	}

	void printInPreOrder(AVLNode<T> root){
		if(root!=NIL){
			if(root.equals(this.root)){}
			else{System.out.print(" ");}
			System.out.print(root.item);
			printInPreOrder(root.left);
			printInPreOrder(root.right);
		}
	}

}

