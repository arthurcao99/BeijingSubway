package cn.edu.zucc;

import cn.edu.zucc.core.DijkstraUtil;
import cn.edu.zucc.data.FileManager;
import cn.edu.zucc.model.Routine;

import java.io.File;

public class Main {

    public static void main(String[] args)  {
	// write your code here

        switch (args[0]){
            //读入subway.txt文件数据
            case "-map":
                //-map subway.txt
                if(args.length==2){
                    FileManager.READ_FILE = System.getProperty("user.dir") + File.separator + args[1];
                    //根据路径，读取地铁信息，并打印。
                    FileManager.readSubway();
                    System.out.println("成功读取subway.txt文件");
                }else{
                    System.out.println("验证参数格式！");
                }
                break;
             //查询指定线路的文件数据，并写入到station.txt文件中
            case "-a":
                //-a 1号线 -map subway.txt -o station.txt
                if(args.length==6){
                    FileManager.READ_FILE = System.getProperty("user.dir") + File.separator  + args[3];
                    FileManager.WRITE_FILE = System.getProperty("user.dir") + File.separator  + args[5];

                    FileManager.readSubway();
                    if (FileManager.subwayLineinfo.keySet().contains(args[1])){
                        FileManager.writeStation(args[1]);
                        System.out.println("已将地铁"+args[1]+"的各站点信息写入station.txt文件");
                    }else{
                        System.out.println("线路不存在");
                    }
                }else{

                    System.out.println("验证参数格式！");
                }
                break;
             //查询指定站点之间的最短路径信息
            case "-b":
                //-b 天安门西 北京大学东门 -map subway.txt -o routine.txt

                if(args.length==7){
                    FileManager.READ_FILE = System.getProperty("user.dir") + File.separator + args[4];
                    FileManager.WRITE_FILE = System.getProperty("user.dir") + File.separator + args[6];
                    FileManager.readSubway();
                    Routine routine = DijkstraUtil.getRoutine(args[1],args[2]);
                    if (routine!=null){
                        FileManager.writePassStation(routine);
                        System.out.println("已将"+args[1]+"到"+args[2]+"最短路径的结果写入routine. txt文件");
                    }
                }else{
                    System.out.println("验证参数格式！");
                }
                break;
        }
    }
}
