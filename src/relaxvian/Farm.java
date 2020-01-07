package relaxvian;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ayas
 */
public class Farm {
    
    private int id;
    private int troops;
    private int troopId;
    private int loopTime;
    private long timestamp;
    private String villageID;
    public Farm(int id, int troops, int troopId, int looptime, String villageID){
        this.id = id;
        this.troops = troops;
        this.troopId= troopId;
        this.timestamp = 0;
        this.loopTime = looptime;
        this.villageID = villageID;
        
    }
    public void cooldown(){
        this.timestamp = System.currentTimeMillis() + 30000;
        
    }
    public String getVillageID(){
        return this.villageID;
    }
    
    public void restart(){
        this.timestamp = 0;
    }
    
    public boolean Sendable(){
        if (timestamp <= System.currentTimeMillis()){
            return true;
        }
        return false;
    }
    
    public void reset(){
        this.timestamp = System.currentTimeMillis()+((this.loopTime*60)*1000);
    }
    
    public int getId(){
        return this.id;
    }
    
   
    
    public int getLoop(){
        return this.loopTime;
    }
    
     public int gettroops(){
        return this.troops;
    }
    
    public int getType(){
        return this.troopId;
    }
    
}
