
package taskmanager;

import java.util.Scanner;
import java.io.*;

public class TaskManager {

 
    public static void main(String[] args)throws IOException, ClassNotFoundException{
        /*Journal jObject = new Journal();
        jObject.addRecord();
        jObject.addRecord();
        jObject.writeFile("kek.jdb");
        jObject.printRecords();*/
        Journal jObject2 = new Journal();
        jObject2.readFile("kek.jdb");
        jObject2.printRecords();
     
    }
    
}
