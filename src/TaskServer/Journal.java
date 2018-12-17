/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TaskServer;
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
    int getCount(){
        int count = 0;
        for(Task task: list){
            count++;
        }
        return count;
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
        System.setProperty("file.encoding","utf-8");
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
        Scanner scan = new Scanner(file, "utf-8");
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH-mm");   
        int checkCounter = 0;
        int taskCounter = 0;
        String[] taskFields = new String[5];
        while (scan.hasNextLine()) {            
            while(true){
                taskFields[checkCounter]=scan.nextLine();                
                if(taskFields[checkCounter].equals("")){
                    break;
                }     
                checkCounter++;
            }           
        if(checkCounter==4){ 
            String[] checkedTask = checkTask(taskFields);
            if(checkedTask!=null){
                add(checkedTask[0],checkedTask[1],format.parse(checkedTask[2]),checkedTask[3]);
            }
        } else{
            System.err.println("Task number " + taskCounter + " incorrect and did not added to journal");
        }             
        checkCounter = 0;
        taskCounter++;
        }
    }
    
    String[] checkTask (String[] tasks){
        String[] checkedTask = new String[tasks.length];
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH-mm");   
        if(tasks[0].length()<15){
            checkedTask[0]=tasks[0];
        }else{
            System.err.println("Too much symbols in task name");
            return null;
        }
        checkedTask[1]=tasks[1];
        try{
            date = format.parse(tasks[2]);
            checkedTask[2]=format.format(date);
        }
        catch(ParseException e){
            System.err.println("Wrong date, changed to default");
            checkedTask[2]="2018-01-01 00-00";
        }
        checkedTask[3]=tasks[3];

        return checkedTask;
    }
}

