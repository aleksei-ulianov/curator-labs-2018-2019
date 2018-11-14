
package taskmanager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.*;
import java.io.*;
import javax.swing.JFrame;
import taskmanager.AlertFrame.*;
import javax.sound.sampled.*;
import java.net.MalformedURLException;
import java.applet.*;

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
    
    List <Date> alarmTimeList = new LinkedList();
    
    void recieveTime(Journal jObject)throws ParseException{
        alarmTimeList=jObject.scanDate();              
    }
    
    boolean alarm(Journal jObject)
            throws MalformedURLException, LineUnavailableException, 
            UnsupportedAudioFileException, IOException{ 
        ListIterator <Date> iterator = alarmTimeList.listIterator();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm");
        Date currentTime = new Date();
        AlarmSound sound = new AlarmSound();
        int count=0;
        while (iterator.hasNext()){
            if (sdf.format(iterator.next()).equals(sdf.format(currentTime))){
                ListElement node = jObject.getNode(count);                
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
