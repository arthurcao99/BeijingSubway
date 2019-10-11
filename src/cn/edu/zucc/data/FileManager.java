package cn.edu.zucc.data;

import cn.edu.zucc.model.Routine;
import cn.edu.zucc.model.Station;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FileManager {

    public  static String READ_FILE;
    public  static String WRITE_FILE;
    public  static HashMap<String, List<Station>> subwayLineinfo = new HashMap<>();


    public static void readSubway(){    //读取subway.txt文件
        File file = new File(READ_FILE);

        BufferedReader reader = null;

        List<Station> stations;
        Station station;

        try {
            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file),"UTF-8");

            reader = new BufferedReader(inputStreamReader);
            String line = null;
            while ((line = reader.readLine()) != null) {    //根据文件数据，读分别读取各线路站点信息

                String[] subwayLine = line.trim().split(" ");

                stations = new ArrayList<>();

                for(int i = 1;i<subwayLine.length;i++){     //将读入 new Station()并加入到对应线路的List中
                    station = new Station(subwayLine[i],subwayLine[0]);

                    stations.add(station);
                }
                subwayLineinfo.put(subwayLine[0],stations);//将对应线路存储站点的List作为value并以线路名称为key存入subwayLineinfo中
            }

        } catch (UnsupportedEncodingException e) {  //异常处理
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }

    public static void writeStation(String lineName) {  //写入指定地铁线路的站点信息
        File file = new File(WRITE_FILE);
        BufferedWriter writer = null;
        List<Station> list;

        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file),"UTF-8");
            writer = new BufferedWriter(outputStreamWriter);

            list = subwayLineinfo.get(lineName);    //获取subwayLineinfo指定线路全部的站点List，并进行输出
            writer.write(lineName+"\n");
            for(int i=0;i<list.size();i++){
                writer.write(list.get(i).getStationName()+"\n");
            }

            writer.close();

        } catch (IOException e) {   //异常处理
            e.printStackTrace();
        }
    }

    public static void writePassStation(Routine routine){   //写入指定站点之间的最短路径上的站点信息
        File file = new File(WRITE_FILE);
        BufferedWriter writer = null;

        try{
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file),"UTF-8");
            writer = new BufferedWriter(outputStreamWriter);

            writer.write(routine.getPassStations().size()-1+"\n");  //输出最短路径需要经过的站点数量


            String linename = getLineNmae(routine.getPassStations().get(0),routine.getPassStations().get(1));

            for (int i = 0; i < routine.getPassStations().size();i++)   //写入最短路径上各个站点的信息
            {
                writer.write(routine.getPassStations().get(i).getStationName()+"\n");

                if (i<routine.getPassStations().size()-1){
                    if (!linename.equals(getLineNmae(routine.getPassStations().get(i),routine.getPassStations().get(i+1)))){    //如果线路发生变化，则进行更新并将换乘后的线路名称写入routine.txt
                        linename = getLineNmae(routine.getPassStations().get(i),routine.getPassStations().get(i+1));
                        writer.write("--->换乘地铁--<"+linename+">--\n");
                    }
                }


            }

            writer.close();

        }catch (IOException e){
            e.printStackTrace();
        }
    }


    public static String getLineNmae(Station station1,Station station2){        //判断两个站点是否在同一条线路上
        String res = null;

        List<Station> item;


        for (String key : subwayLineinfo.keySet()){
            item = subwayLineinfo.get(key);
            if (item.contains(station1)&&item.contains(station2)){
                return key;     //如果是，返回线路名称,否则，返回null
            }

        }

        return res;
    }
}
