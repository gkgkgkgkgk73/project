import java.io.*;
import java.util.*;


public class Selection {
    public static void main(String[] args) throws IOException {

        boolean randCorrectness;
        boolean deterCorrectness;
        int randOutput;
        int deterOutput;
        int[] inputArray;
        int arraySize;
        int I;
        long randStart;
        long randEnd;
        long deterStart;
        long deterEnd;

        //get input array, array size and i;
        //You should put testcasesname.txt in ./input/
        String tempString;
        String filePath = "./src/input/";
        File directoryFile = new File(filePath);
        File[] testFileList = directoryFile.listFiles();
        for(int f=0;f<testFileList.length;f++){
            FileReader fileReader = new FileReader(testFileList[f].getPath());
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            tempString = bufferedReader.readLine();
            String[] tpInputArray = tempString.split("\\s");
            arraySize = Integer.parseInt(tpInputArray[0]);
            I = Integer.parseInt(tpInputArray[1]);
            tempString = bufferedReader.readLine();
            inputArray = new int[arraySize];
            int[] deterInputArray = new int[arraySize];
            int[] randTestInputArray = new int[arraySize];
            int[] deterTestInputArray = new int[arraySize];
            tpInputArray = tempString.split("\\s");
            for (int i = 0; i < arraySize; i++) {
                inputArray[i] = Integer.parseInt(tpInputArray[i]);
                deterInputArray[i] = Integer.parseInt(tpInputArray[i]);
                randTestInputArray[i] = Integer.parseInt(tpInputArray[i]);
                deterTestInputArray[i] = Integer.parseInt(tpInputArray[i]);

            }
            bufferedReader.close();
            fileReader.close();

            //get randstart, Run Randomized-select, get randend, check correctness using a checker program, and store in randcorrectness
            System.out.println(testFileList[f].getName());
            randStart = System.currentTimeMillis();
            randOutput = randomizedSelect(inputArray, 0, arraySize - 1, I);
            randEnd = System.currentTimeMillis();
            randCorrectness = checkProgram(randTestInputArray, I, randOutput);

            //get deterstart, Run linear-select, get deterend, check correctness using a checker program, and store in randcorrectness
            deterStart = System.currentTimeMillis();
            deterOutput = linearSelect(deterInputArray, 0, arraySize - 1, I);
            deterEnd = System.currentTimeMillis();
            deterCorrectness = checkProgram(deterTestInputArray, I, deterOutput);

            //print Randomized-Select: i-th smallest element (randcorrectness) (randEnd-randStart)
            System.out.println("Randomized-Select: " + randOutput + " (" + randCorrectness + ")" + " (" + ((randEnd - randStart) / 1000.0) + " sec)");
            //print Linear-Select: i-th smallest element (detercorrectness) (deterEnd-deterStart)
            System.out.println("Linear-Select: " + deterOutput + " (" + deterCorrectness + ")" + " (" + ((deterEnd - deterStart) / 1000.0) + " sec)");
        }
    }

    public static int randomizedSelect(int[] target, int firstIndex, int lastIndex, int I){

        int pivotIndex;
        if(target.length==1){
            return target[0];
        }else{
            pivotIndex = partition(target, firstIndex, lastIndex);
            if(I<pivotIndex-firstIndex+1){
                if(firstIndex<pivotIndex-1)
                return randomizedSelect(target,firstIndex,pivotIndex-1,I);
                else return target[pivotIndex-1];
            }else if(I==pivotIndex-firstIndex+1){
                return target[pivotIndex];
            }else{
                if(pivotIndex+1<lastIndex)
                return randomizedSelect(target,pivotIndex+1,lastIndex,I-pivotIndex+firstIndex-1);
                else return target[pivotIndex+1];
            }
        }
    }

    public static int linearSelect(int[] target, int firstIndex, int lastIndex, int I){
        if(target.length<=5){
            return insertionSort(target,lastIndex-firstIndex+1,I);
        }else{
            int numberOfGroup = (int) Math.ceil((double)(lastIndex-firstIndex)/5);
            int[][] divideGroup = new int[numberOfGroup][5];
            for(int i=0;i<numberOfGroup;i++){
                for(int j=0;j<5;j++){
                    if(lastIndex-firstIndex>=(5*i+j)) {
                        divideGroup[i][j] = target[5*i+j];
                    }
                }
            }
            int[] medianValues = new int[numberOfGroup];
            int numLastGroup = target.length%5;
            if(numLastGroup==0){
            for(int i=0;i<numberOfGroup;i++){
                medianValues[i]=insertionSort(divideGroup[i],5,3);
            }}
            else{
                for(int i=0;i<numberOfGroup-1;i++){
                 medianValues[i]=insertionSort(divideGroup[i],5,3);
                }
            medianValues[numberOfGroup-1]=insertionSort(divideGroup[numberOfGroup-1],numLastGroup,numLastGroup/2+1);
        }
            int M = linearSelect(medianValues,0,medianValues.length-1,medianValues.length/2+1);
            int q =partition(target,firstIndex,lastIndex,M);
            if(q-firstIndex+1==I){
                return target[q];
            }else if(I<q-firstIndex+1){
                if(firstIndex<q-1)
                return randomizedSelect(target,firstIndex,q-1,I);
                else return target[q-1];
            }else{
                if(q+1<lastIndex)
                return randomizedSelect(target,q+1,lastIndex,I-q+firstIndex-1);
                else return target[1+q];
            }
        }
    }

    //we use insertionSort function in linear-selection when n is under 5
    public static int insertionSort(int[] target, int n, int I){
        int[] temp = new int[n];
        temp[0]=target[0];
        for(int i=1;i<n;i++){
            int j=0;
            while(j<i && target[i]>temp[j]){
                j++;
            }
            if(i==j){
                temp[i]=target[i];
            }
            else if(target[i]<=temp[j]){
                for(int k=i-1;k>=j;k--){
                    temp[k+1]=temp[k];
                }
                temp[j]=target[i];
            }
        }
        return temp[I-1];
    }

    //We use this function in randomized-selection
    public static int partition(int[] target, int firstIndex, int lastIndex){
        int pivotIndex = makeRandomPivot(lastIndex-firstIndex+1,firstIndex);
        int realpivotIndex;
        int lastIndexInSmallest=firstIndex;
        int lastIndexInLargest=lastIndex;
        int[] temp = new int[target.length];
        for(int i=firstIndex;i<=lastIndex;i++){
            if(i!=pivotIndex){
            if(target[i]<=target[pivotIndex]){
                temp[lastIndexInSmallest++]=target[i];
            }else{
                temp[lastIndexInLargest--]=target[i];
            }
        }}
        temp[lastIndexInSmallest]=target[pivotIndex];
        for(int k=firstIndex;k<=lastIndex;k++){
            target[k]=temp[k];
        }
        realpivotIndex=lastIndexInSmallest;
        return realpivotIndex;
    }

    //We use the following fuction in deterministic-select
    public static int partition(int[] target,int firstIndex,int lastIndex, int pivot){
        int i=0;
        while(i<target.length && target[i]!=pivot){
            i++;
        }
        int pivotIndex = i;
        int lastIndexInSmallest=firstIndex;
        int lastIndexInLargest=lastIndex;
        int[] temp = new int[target.length];
        for(int k=firstIndex;k<=lastIndex;k++){
            if(k!=pivotIndex){
                if(target[k]<=target[pivotIndex]){
                    temp[lastIndexInSmallest++]=target[k];
                }else{
                    temp[lastIndexInLargest--]=target[k];
                }
            } }
        temp[lastIndexInSmallest]=target[pivotIndex];
        for(int j=firstIndex;j<=lastIndex;j++){
            target[j]=temp[j];
        }
        return lastIndexInSmallest;
    }

    //we use the following function in randomized-selection(using for getting randompivot)
    public static int makeRandomPivot(int arraysize,int firstIndex){
        Random random = new Random();
        random.setSeed(System.nanoTime());
        return random.nextInt(arraysize)+firstIndex;
    }

    //put input array and i, get i-th smallest element
    public static int selectionInCheckProgram(int[] array, int I){
        int[] minus = new int[array.length];
        int lastElementIndexOfMinus = 0;
        int[] plus = new int[array.length];
        int lastElementIndexOfPlus = 0;
        int[] temp = new int[array.length];
        for(int k=0;k<array.length;k++){
            temp[k]=array[k];
        }
        for(int i=0;i<11;i++){
            int[] numZero = new int[array.length];
            int lastElementOfNumZero = 0;
            int[] numOne = new int[array.length];
            int lastElementOfNumOne = 0;
            int[] numTwo = new int[array.length];
            int lastElementOfNumTwo = 0;
            int[] numThree= new int[array.length];
            int lastElementOfNumThree = 0;
            int[] numFour = new int[array.length];
            int lastElementOfNumFour = 0;
            int[] numFive = new int[array.length];
            int lastElementOfNumFive = 0;
            int[] numSix = new int[array.length];
            int lastElementOfNumSix = 0;
            int[] numSeven = new int[array.length];
            int lastElementOfNumSeven = 0;
            int[] numEight = new int[array.length];
            int lastElementOfNumEight = 0;
            int[] numNine = new int[array.length];
            int lastElementOfNumNine = 0;
            if(i<9){
                int divisor = (int) Math.pow(10,i);
            for(int k=0;k<temp.length;k++){
                if(temp[k]>=0){
                    switch ((temp[k]%(divisor*10) -temp[k]%divisor)/divisor) {
                        case 0:
                            numZero[lastElementOfNumZero++] = temp[k];
                            break;
                        case 1:
                            numOne[lastElementOfNumOne++] = temp[k];
                            break;
                        case 2:
                            numTwo[lastElementOfNumTwo++] = temp[k];
                            break;
                        case 3:
                            numThree[lastElementOfNumThree++] = temp[k];
                            break;
                        case 4:
                            numFour[lastElementOfNumFour++] =temp[k];
                            break;
                        case 5:
                            numFive[lastElementOfNumFive++] =temp[k];
                            break;
                        case 6:
                            numSix[lastElementOfNumSix++] = temp[k];
                            break;
                        case 7:
                            numSeven[lastElementOfNumSeven++] = temp[k];
                            break;
                        case 8:
                            numEight[lastElementOfNumEight++] = temp[k];
                            break;
                        case 9:
                            numNine[lastElementOfNumNine++] = temp[k];
                            break;
                    }
                }else{
                    switch (((temp[k]%(divisor*10) - temp[k]%divisor)/divisor)) {
                        case 0:
                            numZero[lastElementOfNumZero++] = temp[k];
                            break;
                        case -1:
                            numOne[lastElementOfNumOne++] = temp[k];
                            break;
                        case -2:
                            numTwo[lastElementOfNumTwo++] = temp[k];
                            break;
                        case -3:
                            numThree[lastElementOfNumThree++] = temp[k];
                            break;
                        case -4:
                            numFour[lastElementOfNumFour++] =temp[k];
                            break;
                        case -5:
                            numFive[lastElementOfNumFive++] =temp[k];
                            break;
                        case -6:
                            numSix[lastElementOfNumSix++] = temp[k];
                            break;
                        case -7:
                            numSeven[lastElementOfNumSeven++] =temp[k];
                            break;
                        case -8:
                            numEight[lastElementOfNumEight++] = temp[k];
                            break;
                        case -9:
                            numNine[lastElementOfNumNine++] = temp[k];
                            break;
                    }
                }
            }
            for(int j=0;j<lastElementOfNumZero;j++){
                temp[j]=numZero[j];
            }
            for(int j=0;j<lastElementOfNumOne;j++){
               temp[j+lastElementOfNumZero]=numOne[j];
            }
            for(int j=0;j<lastElementOfNumTwo;j++){
                temp[j+lastElementOfNumZero+lastElementOfNumOne]=numTwo[j];
            }
            for(int j=0;j<lastElementOfNumThree;j++){
                temp[j+lastElementOfNumZero+lastElementOfNumOne+lastElementOfNumTwo]=numThree[j];
            }
            for(int j=0;j<lastElementOfNumFour;j++){
               temp[j+lastElementOfNumZero+lastElementOfNumOne+lastElementOfNumTwo+lastElementOfNumThree]=numFour[j];
            }
            for(int j=0;j<lastElementOfNumFive;j++){
                temp[j+lastElementOfNumZero+lastElementOfNumOne+lastElementOfNumTwo+lastElementOfNumThree+lastElementOfNumFour]=numFive[j];
            }
            for(int j=0;j<lastElementOfNumSix;j++){
                temp[j+lastElementOfNumZero+lastElementOfNumOne+lastElementOfNumTwo+lastElementOfNumThree+lastElementOfNumFour+lastElementOfNumFive]=numSix[j];
            }
            for(int j=0;j<lastElementOfNumSeven;j++){
               temp[j+lastElementOfNumZero+lastElementOfNumOne+lastElementOfNumTwo+lastElementOfNumThree+lastElementOfNumFour+lastElementOfNumFive+lastElementOfNumSix]=numSeven[j];
            }
            for(int j=0;j<lastElementOfNumEight;j++){
                temp[j+lastElementOfNumZero+lastElementOfNumOne+lastElementOfNumTwo+lastElementOfNumThree+lastElementOfNumFour+lastElementOfNumFive+ lastElementOfNumSix+lastElementOfNumSeven]=numEight[j];
            }
            for(int j=0;j<lastElementOfNumNine;j++){
                temp[j+lastElementOfNumZero+lastElementOfNumOne+lastElementOfNumTwo+lastElementOfNumThree+lastElementOfNumFour+lastElementOfNumFive+ lastElementOfNumSix+lastElementOfNumSeven+lastElementOfNumEight]=numNine[j];
            } }
            else if(i==10){
              for(int k=0;k<temp.length;k++){
                    if(temp[k]>=0){
                        switch ((temp[k]/1000000000)) {
                            case 0:
                                numZero[lastElementOfNumZero++] = temp[k];
                                break;
                            case 1:
                                numOne[lastElementOfNumOne++] = temp[k];
                                break;
                            case 2:
                                numTwo[lastElementOfNumTwo++] = temp[k];
                                break;
                            case 3:
                                numThree[lastElementOfNumThree++] = temp[k];
                                break;
                            case 4:
                                numFour[lastElementOfNumFour++] =temp[k];
                                break;
                            case 5:
                                numFive[lastElementOfNumFive++] =temp[k];
                                break;
                            case 6:
                                numSix[lastElementOfNumSix++] = temp[k];
                                break;
                            case 7:
                                numSeven[lastElementOfNumSeven++] = temp[k];
                                break;
                            case 8:
                                numEight[lastElementOfNumEight++] = temp[k];
                                break;
                            case 9:
                                numNine[lastElementOfNumNine++] = temp[k];
                                break;
                        }
                    }else{
                        switch ((temp[k]/1000000000)) {
                            case 0:
                                numZero[lastElementOfNumZero++] = temp[k];
                                break;
                            case -1:
                                numOne[lastElementOfNumOne++] = temp[k];
                                break;
                            case -2:
                                numTwo[lastElementOfNumTwo++] = temp[k];
                                break;
                            case -3:
                                numThree[lastElementOfNumThree++] = temp[k];
                                break;
                            case -4:
                                numFour[lastElementOfNumFour++] =temp[k];
                                break;
                            case -5:
                                numFive[lastElementOfNumFive++] =temp[k];
                                break;
                            case -6:
                                numSix[lastElementOfNumSix++] = temp[k];
                                break;
                            case -7:
                                numSeven[lastElementOfNumSeven++] =temp[k];
                                break;
                            case -8:
                                numEight[lastElementOfNumEight++] = temp[k];
                                break;
                            case -9:
                                numNine[lastElementOfNumNine++] = temp[k];
                                break;
                        }
                    }
                }
                for(int j=0;j<lastElementOfNumZero;j++){
                    temp[j]=numZero[j];
                }
                for(int j=0;j<lastElementOfNumOne;j++){
                    temp[j+lastElementOfNumZero]=numOne[j];
                }
                for(int j=0;j<lastElementOfNumTwo;j++){
                    temp[j+lastElementOfNumZero+lastElementOfNumOne]=numTwo[j];
                }
                for(int j=0;j<lastElementOfNumThree;j++){
                    temp[j+lastElementOfNumZero+lastElementOfNumOne+lastElementOfNumTwo]=numThree[j];
                }
                for(int j=0;j<lastElementOfNumFour;j++){
                    temp[j+lastElementOfNumZero+lastElementOfNumOne+lastElementOfNumTwo+lastElementOfNumThree]=numFour[j];
                }
                for(int j=0;j<lastElementOfNumFive;j++){
                    temp[j+lastElementOfNumZero+lastElementOfNumOne+lastElementOfNumTwo+lastElementOfNumThree+lastElementOfNumFour]=numFive[j];
                }
                for(int j=0;j<lastElementOfNumSix;j++){
                    temp[j+lastElementOfNumZero+lastElementOfNumOne+lastElementOfNumTwo+lastElementOfNumThree+lastElementOfNumFour+lastElementOfNumFive]=numSix[j];
                }
                for(int j=0;j<lastElementOfNumSeven;j++){
                    temp[j+lastElementOfNumZero+lastElementOfNumOne+lastElementOfNumTwo+lastElementOfNumThree+lastElementOfNumFour+lastElementOfNumFive+lastElementOfNumSix]=numSeven[j];
                }
                for(int j=0;j<lastElementOfNumEight;j++){
                    temp[j+lastElementOfNumZero+lastElementOfNumOne+lastElementOfNumTwo+lastElementOfNumThree+lastElementOfNumFour+lastElementOfNumFive+ lastElementOfNumSix+lastElementOfNumSeven]=numEight[j];
                }
                for(int j=0;j<lastElementOfNumNine;j++){
                    temp[j+lastElementOfNumZero+lastElementOfNumOne+lastElementOfNumTwo+lastElementOfNumThree+lastElementOfNumFour+lastElementOfNumFive+ lastElementOfNumSix+lastElementOfNumSeven+lastElementOfNumEight]=numNine[j];
                }
            }
        }
        for(int k=0;k<temp.length;k++){
            if(temp[k]<0){
                minus[lastElementIndexOfMinus++]=temp[k];
            }else{
                plus[lastElementIndexOfPlus++]=temp[k];
            }
        }
        for(int k=lastElementIndexOfMinus;k>0;k--){
            temp[k-1]=minus[lastElementIndexOfMinus-k];
        }
        for(int k=0;k<lastElementIndexOfPlus;k++){
            temp[k+lastElementIndexOfMinus]=plus[k];
        }
        return temp[I-1];
    }



    public static Boolean checkProgram(int[] inputArray, int I, int output){
        int correctOutput = selectionInCheckProgram(inputArray,I);
        if(output==correctOutput) return true;
        else return false;
    }



}
