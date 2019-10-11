package cn.edu.zucc.model;

import java.util.ArrayList;
import java.util.List;

public class Station {

    private String stationName;     //站点名称

    private String lineName;        //线路名称

    private List<Station> linkStations = new ArrayList<>();     //相邻站点

    public Station(String stationName){
        this.stationName = stationName;
    }

    public Station(String stationName,String lineName){
        this.stationName = stationName;
        this.lineName = lineName;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public List<Station> getLinkStations() {
        return linkStations;
    }

    public void setLinkStations(List<Station> linkStations) {
        this.linkStations = linkStations;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        } else if(obj instanceof Station){
            Station station = (Station) obj;
            if(station.getStationName().equals(this.getStationName())){
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


}
