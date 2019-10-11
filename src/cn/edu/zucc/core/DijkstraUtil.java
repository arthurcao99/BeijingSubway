package cn.edu.zucc.core;

import cn.edu.zucc.data.FileManager;
import cn.edu.zucc.model.Routine;
import cn.edu.zucc.model.Station;

import java.util.HashMap;
import java.util.List;

public class DijkstraUtil {

    public static HashMap<String,Station> allStation = new HashMap<>();     //存储全部的站点信息

    public Station FindMinDist( Routine routine,HashMap<HashMap<Station,Station>,Integer> distance , HashMap<Station,Integer> collected){

        Station MinV = null;
        Station item = null;
        int MinDist = 10000;

        for(HashMap<Station,Station> key:distance.keySet()){
            for (Station id: key.keySet()){
                item = key.get(id);
            }
            if(collected.get(item)==0&&distance.get(key)<MinDist){
                MinDist = distance.get(key);
                MinV = item;
            }
        }
        if (MinDist<10000)
            return MinV;
        else
            return new Station("-1");

    }

    public Station getFinStation(HashMap<Station,Station> t){
        Station res = null;

        for (Station key:t.keySet()){
            res = t.get(key);
        }

        return res;
    }

    public HashMap<Station,Station> getFromtoFin (Routine routine,Station station){
        HashMap<Station,Station> res = new HashMap<>();
        res.put(routine.getBeginStation(),station);

        return res;

    }

    public Routine Dijkstra_algorithm ( Routine routine ) {

        HashMap<HashMap<Station,Station>,Integer> distance = new HashMap<>();   //储存各站点之间的最短距离，以Integer为单位，表示站点数
        HashMap<Station,Integer> collected = new HashMap<>();                   //判断该Station是否被访问过
        HashMap<Station,Station> path = new HashMap<>();                        //存储某个指定站点的前一个站点
        HashMap<Station,Station> disitem = new HashMap<>();
        Station item;
        Station V;

        for (String key :allStation.keySet()){      //初始化distance、collected与path
            item = allStation.get(key);

            collected.put(item,new Integer(0));

            if (!routine.getBeginStation().equals(item)){
                if (routine.getBeginStation().getLinkStations().contains(item)){    //若与起点相邻，则将距离设置为1，并将对应的path设置为起点
                    disitem = new HashMap<>();
                    disitem.put(routine.getBeginStation(),item);
                    distance.put(disitem,new Integer(1));
                    path.put(item,routine.getBeginStation());
                }
                else{
                    disitem = new HashMap<>();                              //若未与起点相邻，则将初值设置为10000
                    disitem.put(routine.getBeginStation(),item);
                    distance.put(disitem,new Integer(10000));
                }
            }
            else{
                disitem = new HashMap<>();
                disitem.put(routine.getBeginStation(),item);
                distance.put(disitem,new Integer(0));
            }
        }

        collected.put(routine.getBeginStation(),1);

        while (true){
            V = FindMinDist(routine,distance,collected);    //取未被访问顶点中distance最小者
            if (V.getStationName().equals("-1"))            //若这样的V不存在，算法结束
                break;

            collected.put(V,1);

            for (String key:allStation.keySet()){           //遍历每个站点
                if (V.getLinkStations().contains(allStation.get(key))&&collected.get(allStation.get(key))==0){
                    if (distance.get(getFromtoFin(routine,V))+1<distance.get(getFromtoFin(routine,allStation.get(key)))){   //若收录的顶点使distance变小，则进行更新
                        distance.put(getFromtoFin(routine,allStation.get(key)),distance.get(getFromtoFin(routine,V))+1);
                        path.put(allStation.get(key),V);
                    }
                }
            }
        }
        V = path.get(routine.getEndStation());

        while(!V.equals(routine.getBeginStation())){        //将最短路径各站点数据存入routine之中
            routine.getPassStations().add(0,V);
            V = path.get(V);
        }
        routine.getPassStations().add(0,routine.getBeginStation());
        routine.getPassStations().add(routine.getEndStation());


        return routine;         //返回
    }



    public static Routine getRoutine ( String begin,String end)  {
        Routine routine = new Routine();
        List<Station> lineStationlist = null;


        Station station = null;
        Station repeatstation = null;

        for(String key:FileManager.subwayLineinfo.keySet()){    //遍历各条线路信息，将station去除重后加入allStation之中

            lineStationlist = FileManager.subwayLineinfo.get(key);

            for(int i = 0;i<lineStationlist.size();i++){
                station = lineStationlist.get(i);
                if(allStation.keySet().contains(station.getStationName())){ //判断是否重复
                    repeatstation = allStation.get(station.getStationName());

                    if (i==0){          //完善各个Sation中相邻站点LinkStation信息
                        repeatstation.getLinkStations().add(lineStationlist.get(i+1));
                    }else if(i==lineStationlist.size()-1){
                        repeatstation.getLinkStations().add(lineStationlist.get(i-1));
                    }else{
                        repeatstation.getLinkStations().add(lineStationlist.get(i+1));
                        repeatstation.getLinkStations().add(lineStationlist.get(i-1));
                    }
                    continue;
                }
                else{

                    if (i==0){
                        station.getLinkStations().add(lineStationlist.get(i+1));
                    }else if(i==lineStationlist.size()-1){
                        station.getLinkStations().add(lineStationlist.get(i-1));
                    }else{
                        station.getLinkStations().add(lineStationlist.get(i+1));
                        station.getLinkStations().add(lineStationlist.get(i-1));
                    }

                    allStation.put(station.getStationName(),station);

                    if(station.getStationName().equals(begin)){     //根据传入参数，确定起点，并加入到routine之中
                        routine.setBeginStation(station);
                        continue;
                    }

                    if(station.getStationName().equals(end)){       //根据传入参数，确定终点，并加入到routine之中
                        routine.setEndStation(station);
                        continue;
                    }
                }
            }
        }

        if (routine.getBeginStation().equals(routine.getEndStation())){ //一些异常情况的处理
            System.out.println("起点与终点相同，请重新输入");
            return null;
        }
        else if (routine.getBeginStation() == null){
            System.out.println("起点不存在");
            return null;
        }
        else if (routine.getEndStation() == null){
            System.out.println("终点不存在");
            return null;
        }
        else{
            routine = new DijkstraUtil().Dijkstra_algorithm(routine);
        }

        return routine;
    }
}
