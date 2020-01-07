/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package relaxvian;

import java.util.ArrayList;
import javax.swing.JTable;
import static relaxvian.Bot.buildingQueues;

/**
 *
 * @author Ayas
 */
public class village {
    private String id;
    private int woodProd,clayProd,ironProd,cropProd;
    private ArrayList<Farm> farms = new ArrayList<Farm>();
    private ArrayList<String> build = new ArrayList<String>();
    private int[] fieldLevels = new int[41];
    private long EndTime = 0;
    private boolean WaitForTiles = false;
    private Train train;
            
    
    public village(String id){
        train = new Train(id, "0",0,"none");
        this.id = id;
    }
    
    public Train getTrain(){
        return this.train;
    }
    
    
    public long getEndTime(){
        return EndTime;
    }
    
    public void setEnd(long s){
        this.EndTime = s;
    }
    
    public boolean getWait(){
        return this.WaitForTiles;
    }
    
    public void setWait(boolean a){
        this.WaitForTiles = a;
    }
    
    public ArrayList<String> getBuilds(){
        return build;
    }
    
    public String getFirstBuild(){
        return build.get(0);
    }
    
    public void removeFirstBuild(){
        build.remove(0);
       
    }
    public void addBuild(String place){
        build.add(place);   
    }
    
    
    public void updateField(int id){
        this.fieldLevels[id]+=1;
    }
    
    public int[] getLevels(){
        return this.fieldLevels;
    }
    
    public ArrayList<Farm> getFarms(){
        return this.farms;
    }
    
    public void deleteFarm(int id){
        boolean loop = true;
        for(int i = 0; i < farms.size() && loop; i++){
            if(farms.get(i).getId()==id){
                farms.remove(i);
                loop = false;
        }
    }
    }
    
    
    public String getId(){
        return this.id;
    }
        
    }
            
            
    

