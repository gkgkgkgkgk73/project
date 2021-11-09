import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class Subway {
    static class DirectionNTimeNode{
        String neighborNum;
        int time;
        DirectionNTimeNode(String nNum, int t){
            neighborNum = nNum;
            time = t;
        }
    }
    static class Station implements Comparable{
        String stationName;
        String stationNum;
        String stationLine;
        int listNum;
        Station prev;
        ArrayList<DirectionNTimeNode> neighborStationList = new ArrayList<>();
        Station(String sName, String sNum, int lNum, String sLine){
            stationName = sName;
            stationNum = sNum;
            stationLine = sLine;
            listNum = lNum;
        }

        @Override
        public int compareTo(Object o) {
             Station s = (Station) o;
             if(stationName.equals(s.stationName) && stationNum.equals(s.stationNum) && stationLine.equals(stationLine)){
                 return 0;
             }else if(listNum<s.listNum){
                 return -1;
             }else return 1;
        }
    }

    public static void main(String[] args) throws IOException {
        String input;
        BufferedReader b = new BufferedReader(new InputStreamReader(System.in));
        /*data 파일 처리*/
        BufferedReader br = new BufferedReader(new FileReader(new File(args[0])));
        //BufferedReader br = new BufferedReader(new FileReader(new File("C:/Users/gkgkg/IdeaProjects/Subway/src/subway.txt")));
        //이름이 같고, 고유번호가 다른애가 있으면 edge 형성하고 소요시간 5로
        String tmp;
        ArrayList<Station> stationList = new ArrayList<>();
        while(!(tmp=br.readLine()).equals("")){
            String stationName = tmp.split("\\s")[1];
            String stationNum = tmp.split("\\s")[0];
            String stationLine = tmp.split("\\s")[2];
            Station s = new Station(stationName,stationNum, stationList.size(), stationLine);
            stationList.add(s);
            for(int i=0;i<stationList.size()-1;i++){
                if(stationList.get(i).stationName.equals(stationName)){
                    stationList.get(stationList.size()-1).neighborStationList.add(new DirectionNTimeNode(stationList.get(i).stationNum, 5));
                    stationList.get(i).neighborStationList.add(new DirectionNTimeNode(stationNum, 5));
                }
            }
        }
        while(br.ready() && !(tmp=br.readLine()).equals("")){
            String startStationNum,destinationStationNum;
            int time;
            startStationNum = tmp.split("\\s")[0];
            destinationStationNum = tmp.split("\\s")[1];
            time = Integer.parseInt(tmp.split("\\s")[2]);
            int i=0;
            while(i<stationList.size() && !stationList.get(i).stationNum.equals(startStationNum)){
                i++;
            }
            if(i<stationList.size()){
                stationList.get(i).neighborStationList.add(new DirectionNTimeNode(destinationStationNum, time));
            }
        }
        /*input 받고 처리*/
        do{
            input = b.readLine();
            if(!input.equals("QUIT")){
            String startName, startNum, destinationName,destinationNum;
            startName = input.split("\\s")[0];
            destinationName = input.split("\\s")[1];
            findShortestPath(startName, destinationName, stationList);}
        }while(!"QUIT".equals(input));

    }

    static LinkedList<Station> findStationsUsingName(ArrayList<Station> stationList, String stationName){
        LinkedList<Station> result = new LinkedList<>();
        for(int i=0;i<stationList.size();i++){
            if(stationList.get(i).stationName.equals(stationName)){
                result.add(stationList.get(i));
            }
        }
        return result;
    }

    static Station findStationUsingName(ArrayList<Station> stationList, String sName){
        for(int i=0;i<stationList.size();i++){
            if(stationList.get(i).stationName.equals(sName)){
                return stationList.get(i);
            }
        }
        return null;
    }
    static void findShortestPath(String startName, String destinationName, ArrayList<Station> stationList) {
        int minTime = 999999999;
        int min = 0;
        int totalTime = 0;
        LinkedList<Station> startStationCandidate = findStationsUsingName(stationList, startName);
        LinkedList<String>[] result = new LinkedList[startStationCandidate.size()];
        int[] totalT = new int[startStationCandidate.size()];
            for (int q = 0; q < startStationCandidate.size(); q++) {
                int[] d = new int[stationList.size()];
                for (int i = 0; i < stationList.size(); i++) {
                    stationList.get(i).prev = null;
                }
                ArrayList<Station> S = new ArrayList<>();
                int startLocation = startStationCandidate.get(q).listNum;
                for (int u = 0; u < d.length; u++) {
                    d[u] = minTime;
                }
                d[startLocation] = 0;
                while (S.size() != stationList.size()) {
                    Station tmpMinStation = findMinW(stationList, d, S);
                    S.add(tmpMinStation);
                    for (int l = 0; l < tmpMinStation.neighborStationList.size(); l++) {
                        int t = findstationLocation(tmpMinStation.neighborStationList.get(l).neighborNum, stationList);
                        if (!S.contains(stationList.get(t)) && (d[tmpMinStation.listNum] + tmpMinStation.neighborStationList.get(l).time <= d[t])) {
                            d[t] = d[tmpMinStation.listNum] + tmpMinStation.neighborStationList.get(l).time;
                            stationList.get(t).prev = tmpMinStation;
                        }
                    }
                }
                LinkedList<Station> destStationCandidate = findStationsUsingName(stationList, destinationName);
                LinkedList<String> re = new LinkedList<>();
                int tmtotalTime=999999999;
                Station dest= destStationCandidate.get(0);
                for (int w = 0; w < destStationCandidate.size(); w++) {
                    if(tmtotalTime>d[destStationCandidate.get(w).listNum]){
                        tmtotalTime=d[destStationCandidate.get(w).listNum];
                        dest = destStationCandidate.get(w);
                    }
                }
                {
                    totalTime = d[dest.listNum];
                    re.add(dest.stationName);
                    Station tmp = dest;
                    while (!tmp.prev.equals(stationList.get(startLocation))) {
                        if (tmp.prev.stationName.equals(tmp.stationName)) {
                            if (tmp.stationName.equals(dest.stationName)) {
                                totalTime -= 5;
                            } else {
                                re.removeLast();
                                re.add("[" + tmp.prev.stationName + "]");
                            }
                        } else {
                            re.add(tmp.prev.stationName);
                        }
                        tmp = tmp.prev;
                    }
                    if (re.get(0).equals(startName)) {
                        re.remove(0);
                        totalTime -= 5;
                    }
                    totalT[q] = totalTime;
                    result[q] = re;
                }
            }
            min = findMinT(totalT);
            System.out.print(startName);
            for (int p = 0; p < result[min].size(); p++) {
                System.out.print(" " + result[min].get(result[min].size() - p - 1));
            }
            System.out.println();
            System.out.println(totalT[min]);
            return;
        }

    static int findMinT(int[] totalT){
        int minLocation=0;
        int min=999999999;
        for(int i=0;i<totalT.length;i++){
            if(min>totalT[i]) {
                min=totalT[i];
                minLocation=i;
            }
        }
        return minLocation;
    }
    static Station findMinW(ArrayList<Station> stationList, int[] d, ArrayList<Station> S){
        int minTime=999999999;
        Station minStation = stationList.get(0);
        for(int i=0;i<stationList.size();i++){
            if(!S.contains(stationList.get(i))){
                if(minTime>d[stationList.get(i).listNum]){
                    minTime=d[stationList.get(i).listNum];
                    minStation = stationList.get(i);
                }
            }
        }
        return minStation;
    }

    static int findstationLocation(String stationNum, ArrayList<Station> stationList){
        for(int i=0;i<stationList.size();i++){
            if(stationList.get(i).stationNum.equals(stationNum)){
                return stationList.get(i).listNum;
            }
        }
        return -1;
    }

}
