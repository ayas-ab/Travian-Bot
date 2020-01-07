package relaxvian;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Ayas
 */
public class queue implements Runnable {

    private boolean status = false;
    private Thread ProgramThread;
    private ArrayList<village> villages = new ArrayList<village>();
    private DefaultTableModel modelTable = (DefaultTableModel) Bot.buildingQueues.getModel();
    private DefaultTableModel modelFarmList = (DefaultTableModel) Bot.FarmList.getModel();

    public queue() throws IOException {
        for (int i = 0; i < Login.htmlBrowser.getVillageInfo().size(); i++) {
            villages.add(new village(Login.htmlBrowser.getVillageInfo().get(i)));
        }
        ProgramThread = new Thread(this);
        ProgramThread.start();
    }

    public village getVillage(String id) {
        try {
            for (int i = 0; i < villages.size(); i++) {
                if (villages.get(i).getId().equals(id)) {
                    return villages.get(i);
                }
            }
        } catch (Exception e) {

        }
        return null;
    }

    public void AddFarm(Farm f, String id) {
        this.getVillage(id).getFarms().add(f);

        this.modelFarmList.addRow(new Object[]{f.getId(), f.gettroops(), f.getType(), f.getLoop() + " mins"});
    }

    public void QueueBuilds() throws IOException {
        try {
            //this.UpdateQueues();
            for (int vills = 0; vills < this.villages.size(); vills++) {

                //checking and raiding farms
                for (int farm = 0; farm < villages.get(vills).getFarms().size(); farm++) {
                    if (villages.get(vills).getFarms().get(farm).Sendable()) {

                        if (!Login.htmlBrowser.sendRaid(villages.get(vills).getFarms().get(farm))) {
                            Bot.log("There was an error raiding " + villages.get(vills).getFarms().get(farm).getId() + ", it has been reset to 30 seconds");
                            villages.get(vills).getFarms().get(farm).cooldown();
                        } else {
                            Bot.log("Raid sent to " + villages.get(vills).getFarms().get(farm).getId() + ", it has been reset");
                            villages.get(vills).getFarms().get(farm).reset();
                        }
                        //villages.get(vills).getFarms().get(farm).reset();

                    }
                }
                
                if (villages.get(vills).getTrain().isTraining() && 
                        villages.get(vills).getTrain().getEnd() <= System.currentTimeMillis()){
                    villages.get(vills).getTrain().setEnd(System.currentTimeMillis()+60000);
                    Login.htmlBrowser.train(villages.get(vills).getTrain());
                    
                }

                if (System.currentTimeMillis() >= villages.get(vills).getEndTime()) {
                    villages.get(vills).setWait(false);
                }
                if (villages.get(vills).getBuilds().size() > 0 && !this.villages.get(vills).getWait()) {
                    boolean built = Login.htmlBrowser.upgrade(this.villages.get(vills).getFirstBuild(), this.villages.get(vills).getId());
                    if (built) {

                        Bot.log("tile " + this.villages.get(vills).getFirstBuild() + " upgraded..");
                        villages.get(vills).removeFirstBuild();
                        if (villages.get(vills).getId().equals(Bot.getCurVillage())){
                            modelTable.removeRow(0);
                        }
                        

                    }
                }
            }

        } catch (Exception e) {

        }
    }

    public void AddTile(String n) {
        try {
            String[] s = n.split("/");

            if (s[1].contains(",")) {
                String[] queues = s[1].split(",");
                for (int i = 0; i < queues.length; i++) {
                    this.getVillage(s[0]).addBuild(queues[i]);
                    Bot.log("Tile " + queues[i] + " added to queue");
                    modelTable.addRow(new Object[]{(i + 1)});
                }
            } else {
                this.getVillage(s[0]).addBuild(s[1]);
                modelTable.addRow(new Object[]{s[1]});
                Bot.log("Village " + s[0] + " Tile " + s[1] + " added to queue");
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void run() {
        while (true) {
            if (status) {
                try {
                    //System.out.print("works1");
                    QueueBuilds();

                    //System.out.print("works2");
                } catch (IOException ex) {
                    Logger.getLogger(queue.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

            try {

                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(queue.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void UpdateQueues() {

        try {
            modelTable.getDataVector().removeAllElements();
            modelTable.fireTableDataChanged();
            if (getVillage(Bot.getCurVillage()).getBuilds().size() > 0) {

                for (int i = 0; i < getVillage(Bot.getCurVillage()).getBuilds().size(); i++) {
                    modelTable.addRow(new Object[]{getVillage(Bot.getCurVillage()).getBuilds().get(i)});
                }

            }

        } catch (Exception e) {
            //  JOptionPane.showMessageDialog(null, "Didnt work");
        }
        //  this.buildingQueues.getModel()
    }

    public void UpdateFarmList() {

        try {
            modelFarmList.getDataVector().removeAllElements();
            modelFarmList.fireTableDataChanged();
            if (getVillage(Bot.getCurVillage()).getFarms().size() > 0) {

                for (int i = 0; i < getVillage(Bot.getCurVillage()).getFarms().size(); i++) {
                    Farm f = getVillage(Bot.getCurVillage()).getFarms().get(i);
                    modelFarmList.addRow(new Object[]{f.getId(), f.gettroops(), f.getType(), f.getLoop() + " mins"});
                }

            }

        } catch (Exception e) {
            //  JOptionPane.showMessageDialog(null, "Didnt work");
        }
        //  this.buildingQueues.getModel()
    }

    public void setVillageWait(long a, String vid) {
        this.getVillage(vid).setEnd(a);
        this.getVillage(vid).setWait(true);
    }

    public void setStatus(boolean a) {

        this.status = a;

    }
    
    public boolean getStat(){
        return this.status;
    }

}
