
package taskmanager;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.*;
import java.io.*;
import taskmanager.AlertFrame.*;
import javax.sound.sampled.*;
import java.net.MalformedURLException;


public class Alert  {
    
    class AlarmSound {        
        Clip clip;
        File file = new File("a.wav");
        
        void initSound() throws MalformedURLException,LineUnavailableException,
                UnsupportedAudioFileException,IOException {
            clip = AudioSystem.getClip();
            AudioInputStream ais = AudioSystem.getAudioInputStream(file.toURI().toURL());
            clip.open(ais);
        }
        void play(){
            clip.start();
        }
    }
    
    List<Date> alarmTimeList = new ArrayList<>();
    
    void recieveTime(Journal jObject)throws ParseException{
        alarmTimeList=jObject.scanDate();              
    }
    
    boolean alarm(Journal jObject)
            throws MalformedURLException, LineUnavailableException, 
            UnsupportedAudioFileException, IOException{         
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm");
        Date currentTime = new Date();
        AlarmSound sound = new AlarmSound();
        int count=0;
        for(Date date : alarmTimeList){
            if (sdf.format(date).equals(sdf.format(currentTime))){
                Task node = jObject.getNode(count);                
                AlertJFrame ajf = new AlertJFrame();
                ajf.setTextName(node.name);
                ajf.setTextDesc(node.description);
                ajf.setTextDate(sdf.format(node.date));
                ajf.setTextContacts(node.contacts);                
                sound.initSound();
                sound.play();
                ajf.setVisible(true);
                return true;
            }
        count++;    
        }
        return false;
    }
   
}
