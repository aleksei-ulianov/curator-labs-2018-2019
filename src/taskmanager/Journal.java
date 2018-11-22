/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package taskmanager;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.io.*;
import java.util.*;




public class Journal {
    
    
    private List<Task> list = new ArrayList<>();    
    
    void add (String name, String description, Date date, String contacts) 
            throws ParseException{
        Task node = new Task(name,description,date,contacts);        
        list.add(node);
    }
    
    void deleteElement(String name){
       list.remove(getNode(name));       
    }
    
    void deleteElement(int num){        
        list.remove(num);        
    }
    
    Task getNode(int num){
        try{
            return list.get(num);
        }
        catch (IndexOutOfBoundsException e){
            System.out.println("No Such Element");
        return null;
        }
    }
    
    Task getNode(String name){
        for(Task task : list){
            if(task.getName().equals(name)){
                return task;
            }
        }
        return null;        
    }
       
    void printList(){ 
       for (Task task : list){   
           System.out.println(task.name);
           System.out.println(task.description);
           System.out.println(task.date);
           System.out.println(task.contacts);           
       }       
    }
    
    List<Date> scanDate(){
        List <Date> dateList = new LinkedList();
        for(Task task : list){
            dateList.add(task.date);
        }
        return dateList;
    }    
    
    void writeFile(String fileName)throws IOException, ClassNotFoundException{        
        FileWriter writer = new FileWriter(fileName);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH-mm");         
        PrintWriter printWriter = new PrintWriter(writer);
        for(Task task : list){
            printWriter.println(task.name);
            printWriter.println(task.description);            
            printWriter.println(format.format(task.date));
            printWriter.println(task.contacts);
            printWriter.println();
        }
        writer.close();
       
    }
    
    void readFile(String fileName) throws IOException, ParseException{
        File file = new File(fileName);
        Scanner scan = new Scanner(file); 
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH-mm");       
        while (scan.hasNextLine()) {            
            String name=scan.nextLine();
            String description=scan.nextLine();           
            Date date = format.parse(scan.nextLine());
            String contacts=scan.nextLine();
            String emptyln = scan.nextLine();
            add(name,description,date,contacts);            
        }
    }
}

