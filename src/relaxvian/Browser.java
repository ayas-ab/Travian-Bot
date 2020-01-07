package relaxvian;

/**
 *
 * @author Ayas
 */
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlImageInput;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import java.io.File;
import java.io.IOException;

import java.util.*;

import javax.swing.JOptionPane;
import relaxvian.Bot;

public class Browser {

    private String username = "", password = "";
    private String server = "";
    private final static WebClient webClient = new WebClient(BrowserVersion.CHROME);
    private static HtmlPage page;
    private String thisvillage = "0";

    public Browser() {
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setJavaScriptEnabled(true);


    }

    public void loggedout() throws IOException, Exception {
        page = (HtmlPage) page.refresh();
        //System.out.print(page.getUrl()+"disshit");
        if (page.getUrl().toString().equals(this.server + "login.php")|| page.getUrl().toString().equals(this.server + "index.php")) {

            JOptionPane.showMessageDialog(null, "You have been logged out! Click button below to log in again");
            this.login(server, this.username, this.password);
        }

    }

    public void connect() throws Exception {
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        page = webClient.getPage(server);
        // Bot.log("Connected to " + server);
        // HtmlImage image = page.<HtmlImage>getFirstByXPath("//img[@src='captcha.php']");
        // File imageFile = new File("captcha.jpg");
        // image.saveAs(imageFile);
        //final HtmlImageInput button = page.getElementByName("s1");
        //  button.setAttribute("onclick", "");
    }

    public ArrayList<String> getVillageInfo() throws IOException {
        ArrayList<String> Vilarray = new ArrayList<String>();
        page = webClient.getPage(server + "village1.php");
        List<HtmlAnchor> vills = (List<HtmlAnchor>) page.getAnchors();
        for (int i = 0; i < vills.size(); i++) {
            if (vills.get(i).getAttribute("href").contains("?vid=")) {
                String s = vills.get(i).getAttribute("href").replaceAll("[^0-9]", "");
                Vilarray.add(s);
            }
        }
        return Vilarray;
    }

    public boolean login(String server, String username, String pass) throws IOException, Exception {
        this.server = server;
        page = webClient.getPage(server);
        try {

            HtmlForm form = page.getFormByName("login");
            final HtmlTextInput user = (HtmlTextInput) form.getInputByName("name");
            user.setValueAttribute(username);
            final HtmlPasswordInput pwd = (HtmlPasswordInput) form
                    .getInputByName("password");
            pwd.setValueAttribute(pass);
        final HtmlInput button = (HtmlInput) form.getInputByName("s1");
            page =  button.click();
           
            if (page.getUrl().toString().equals(server + "index.php")) {
               // webClient.closeAllWindows();
                // Bot.log("Wrong username or password");
                return false;
            }
        } catch (Exception e) {
            //  Bot.log("wrong username or password");
            //webClient.closeAllWindows();
            System.out.println("this shit -->"+e);
            return true;
        }
        //webClient.closeAllWindows();
        this.username = username;
        this.password = pass;
        return true;
    }

    public void CloseBrowser() {
        webClient.close();
    }

    public boolean upgrade(String id, String vid) throws IOException, Exception {
        
        // System.out.println(thisvillage+" id = "+id);
        try {
            loggedout();
        if (!vid.equals(this.thisvillage)) {
            this.thisvillage = vid;
            changeVillage(vid + "");
        }

            /*page = webClient.getPage(server + "village1.php");
            HtmlElement finish = (HtmlElement) page.getByXPath("//a[@title='إستكمال أوامر البناء والبحث في هذه القرية فورا\n"
                    + "التكلفة 2 ذهب']").get(0);
            finish.click();*/
        } catch (Exception e) {

        }

        /*try {
            page = webClient.getPage(server + "build.php?id=" + id);
            HtmlElement click = (HtmlElement) page.getByXPath("//a[@title='تاجر المبادلة']").get(0);
            page = click.click();
            // System.out.print(page.asText());
            HtmlElement click2 = (HtmlElement) page.getByXPath("//a[@href='javascript:portionOut();']").get(0);
            page = click2.click();
            //System.out.print(page.asText());
            List<HtmlAnchor> click3 = (List<HtmlAnchor>) page.getAnchors();
            for (int i = 0; i < click3.size(); i++) {
                //  System.out.println(click3.get(i).getTextContent());
                if (click3.get(i).getTextContent().equals("إتمام المبادلة")) {
                    page = click3.get(i).click();
                }
            }
            // page = click3.click();

        } catch (Exception a) {

        } */

        try {

            page = webClient.getPage(server + "build.php?id=" + id);
            HtmlElement click = (HtmlElement) page.getByXPath("//a[@class='build']").get(0);
            click.click();
        } catch (Exception e) {
            try {
                page = webClient.getPage(server + "build.php?id=" + id);
                     HtmlElement time = (HtmlElement) page.getByXPath("//span[@class='none']").get(0);
            String newTime = time.getTextContent();
            newTime = newTime.replace("الموارد كافية في خلال ", "");
            newTime = newTime.replace(" ساعة", "");
            String[] newTime2 = newTime.split(":");
            //long time3 = ((Integer.parseInt(newTime2[0])*60*60)+(Integer.parseInt(newTime2[1])*60+(Integer.parseInt(newTime2[2]))))/2;
            long setTime = System.currentTimeMillis()+(60*1000);
            Bot.getQueue().setVillageWait(setTime, vid);
            Bot.log("Resources for upgrading available in "+newTime);
            
            
                 
            } catch (Exception ee) {
                 long setTime = System.currentTimeMillis()+(10*1000);
                  Bot.getQueue().setVillageWait(setTime, vid);
            

            }

            return false;
        }
        return true;
    }

    public void changeVillage(String vid) {

        try {

            page = webClient.getPage(server + "village1.php?vid=" + vid);
            Bot.log("village is now " + vid);

        } catch (Exception e) {

        }

    }

    public boolean sendRaid(Farm f) throws IOException, Exception {
      
        try {
              loggedout();
           if (!f.getVillageID().equals(this.thisvillage)) {
            this.thisvillage = f.getVillageID();
            changeVillage(f.getVillageID() + "");
        }
              
        page = webClient.getPage(server + "v2v.php?id=" + f.getId());
            HtmlForm form = page.getFormByName("snd");
            HtmlTextInput troops1 = (HtmlTextInput) form.getInputByName("t["+f.getType()+"]");
            //JOptionPane.showMessageDialog(null, f.getType());
            troops1.setText(f.gettroops() + "");
            HtmlRadioButtonInput raid = (HtmlRadioButtonInput) form.getInputByValue("4");
            raid.click();
            //HtmlTExtInput ammount = (HtmlTextInput) form.getInputByName("t["+f.getType()+"]");
            String t = "t["+f.getType()+"]";
             HtmlInput button = form.getInputByName("s1");
            page = button.click();
            HtmlElement ammount = (HtmlElement) page.getByXPath("//input[@name='"+t+"']").get(0);
            if (ammount.getAttribute("value").equals(f.gettroops()+"")){
            //JOptionPane.showMessageDialog(null, ammount.getAttribute("value"));
                 
            button = (HtmlInput) page.getElementById("btn_ok");
            page = button.click();
            }else{
                return false;
            }
            
          
            //Bot.log(f.gettroops() + " troops sent to " + f.getId());

        } catch (Exception e) {
            //System.out.print(e);
            return false;
        }
        return true;
    }

    public void research(String id) {
        try {
            page = webClient.getPage(server + "plus.php?t=2");

            List<HtmlAnchor> links = page.getAnchors();
            for (int i = 0; i < links.size(); i++) {
                if (links.get(i).getAttribute("href").contains("plus.php?t=2&a=5")) {
                    links.get(i).click();
                }

            }
            page = webClient.getPage(server + "build.php?id=" + id);
            HtmlElement clickit = (HtmlElement) page.getByXPath("//a[@class='build']").get(0);
            clickit.click();
            Bot.log("Researched in" + id);
        } catch (Exception e) {

        }

    }

     public void train(Train t) {
     try {
         loggedout();
        if (!t.getVillage().equals(this.thisvillage)) {
            this.thisvillage = t.getVillage();
            changeVillage(t.getVillage() + "");
        }
     page = webClient.getPage(server + "build.php?id=" + t.getPlaceId());
     
     String troopsID = t.getUnitType().charAt(1)+"";
     HtmlElement npc = (HtmlElement) page.getByXPath("//a[@title='تاجر المبادلة']").get(Integer.parseInt(troopsID) - 1);

     if (Integer.parseInt(troopsID) >= 3){
      npc = (HtmlElement) page.getByXPath("//a[@title='تاجر المبادلة']").get(Integer.parseInt(troopsID) - 2);
     }
      page = npc.click();
     HtmlElement click2 = (HtmlElement) page.getByXPath("//a[@href='javascript:portionOut();']").get(0);
     page = click2.click();

     List<HtmlAnchor> click3 = (List<HtmlAnchor>) page.getAnchors();
     for (int i = 0; i < click3.size(); i++) {
     //System.out.println(click3.get(i).getTextContent());
     if (click3.get(i).getTextContent().equals("إتمام المبادلة")) {
     page = click3.get(i).click();
     }
     }
     page = webClient.getPage(server + "build.php?id=" + t.getPlaceId());

     List<?> fill = page.getByXPath("//a[@href='#']");
     HtmlAnchor fillar = (HtmlAnchor) page.getByXPath("//a[@href='#']").get(0);
     for (int i = 0; i < fill.size(); i++){
         HtmlAnchor a = (HtmlAnchor) fill.get(i);
         if (a.getAttribute("onclick").toString().contains("f"+t.getUnitType())){
             fillar = a;
         }
         
         fillar.click();
         
     
         
     }
     HtmlForm form = page.getFormByName("snd");
     //HtmlInput i;
    
    // i = (HtmlInput) form.getInputByName("tf[" + t.getUnitType() + "]");
   
     //i = (HtmlInput) form.getInputByName("tf[2" + (Integer.parseInt(t.getUnitType())+2) + "]");
     
    // i.setValueAttribute("2000");
     //  System.out.println(page.asText()); */

     HtmlImageInput d = form.getInputByName("s1");
     page = (HtmlPage) d.click();
     Bot.log("troops Trained"); 

     } catch (Exception e) {

     }

     }
     
}
