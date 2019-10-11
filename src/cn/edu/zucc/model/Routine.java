package cn.edu.zucc.model;

import java.util.ArrayList;
import java.util.List;

public class Routine {

    private Station beginStation;   //出发站点

    private Station endStation;     //结束站点

    private List<Station> passStations = new ArrayList<>();     //最短路径上的站点

    public Station getBeginStation() {
        return beginStation;
    }

    public void setBeginStation(Station beginStation) {
        this.beginStation = beginStation;
    }

    public Station getEndStation() {
        return endStation;
    }

    public void setEndStation(Station endStation) {
        this.endStation = endStation;
    }

    public List<Station> getPassStations() {
        return passStations;
    }

    public void setPassStations(List<Station> passStations) {
        this.passStations = passStations;
    }
}
