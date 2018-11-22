
package taskmanager;

import java.util.Scanner;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.io.*;
import java.util.*;
import java.net.MalformedURLException;
import javax.sound.sampled.*;

public class TaskManager {

 
    public static void main(String[] args)throws IOException, ClassNotFoundException, ParseException{
        
       //тестовые данные
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH-mm");
        date = format.parse("1997-08-02 22-00");
        Journal jObject = new Journal();
        jObject.add("kek","kek ek ek",date,"11111111");
        date = format.parse("1621-12-31 19-00");
        jObject.add("lel3","2222222222",date,"22222");
        date = format.parse("1451-12-31 20-30");
        jObject.add("lel212","3333333333333333",date,"33333333");
        date = new Date();//format.parse("2018-11-14 18-28");
        jObject.add("lel3552","4444444 44444444444444",date,"4444444");
        date = format.parse("1111-12-31 22-00");
        jObject.add("lel1242","555555555555",date,"555555");
        jObject.writeFile("kekushin.txt");
        Journal jObject2 = new Journal();
        jObject2.readFile("kekushin.txt");
        jObject2.printList(); /*
        jObject2.deleteElement("kek");
        jObject2.printList(); */
        
        System.out.println("===================");
        Alert alarm = new Alert();
        alarm.recieveTime(jObject2);
        try{
            while(alarm.alarm(jObject2) == false){
            
                alarm.alarm(jObject2);
            }
        }
        catch(LineUnavailableException e){
            System.err.println("LineUnavailableException");
        }
        catch(UnsupportedAudioFileException e){
            System.err.println("UnsupportedAudioFileException"); //пока так
        }
        catch(MalformedURLException e){
            System.err.println("MalformedURLException");
        }    
    }  
}
