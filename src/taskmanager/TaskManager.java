
package taskmanager;


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
        date = format.parse("2018-11-22 20-00");
        Journal jObject = new Journal();
        jObject.add("Task0","Cook dinner",date,"me");
        date = format.parse("2018-11-22 21-00");
        jObject.add("Task1","Eat dinner",date,"me");
        date = format.parse("2018-11-22 23-00");
        jObject.add("Task2","Sleep",date,"me");
        date = format.parse("2018-11-22 18-22");
        jObject.add("Task3","Call sanya",date,"+79382383838");
        date = format.parse("2018-11-22 10-07");
        jObject.add("Task4","Do it",date,"LaBuff");
        jObject.writeFile("Journal.txt");
        Journal jObject2 = new Journal();
        jObject2.readFile("Journal.txt");
        jObject2.printList(); 
        /*System.out.println("===================");
        jObject2.deleteElement("Task3");*/        
       
        
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
