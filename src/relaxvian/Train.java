/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package relaxvian;

/**
 *
 * @author Ayas
 */
public class Train {
    private String village;
    private String placeId;
    private long End = 0;
    private int time;
    private String unitType;
    private boolean training = false;

    public boolean isTraining() {
        return training;
    }

    public void setTraining(boolean training) {
        this.training = training;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public void setEnd(long End) {
        this.End = End;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }
    
    public Train(String village, String place, int time, String unittype){
        this.village = village;
        this.placeId = place;
        this.time = time;
        this.unitType = unittype;
       
    }
    
    public void ReNew(String place, int time, String unittype){
        this.placeId = place;
        this.time = time;
        this.unitType = unittype;
         this.training = true;
    }

    public String getVillage() {
        return village;
    }

    public String getPlaceId() {
        return placeId;
    }

    public long getEnd() {
        return End;
    }

    public int getTime() {
        return time;
    }

    public String getUnitType() {
        return unitType;
    }
    
    
}
