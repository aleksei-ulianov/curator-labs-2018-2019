/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package taskmanager;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;



public class Journal {
    
    
    private List<ListElement> list = new LinkedList<ListElement>();    
    
    void add (String name, String description, Date date, String contacts)
            throws ParseException{
        ListElement node = new ListElement();
        node.name=name;
        node.description=description;
        node.date=date;
        node.contacts=contacts;
        list.add(node);
    }
    
    void deleteElement(String name){
        ListIterator<ListElement> iterator = list.listIterator();
        Pattern pattern = Pattern.compile(name);
        Matcher matcher;
        while (iterator.hasNext()){
            ListElement nextIt =iterator.next();
            matcher=pattern.matcher(nextIt.name);
            if(matcher.find()){
                iterator.remove();
                System.out.println("Deleted");}            
        }
    }
    
    void deleteElement(int num){
        ListIterator<ListElement> iterator = list.listIterator();
        while (iterator.hasNext()&& num!=-1){
            if (num == 0){
                iterator.next();
                iterator.remove();
                System.out.println("Deleted");
            } else{
                iterator.next();
            }
            num--;
        }
    }
    
    ListElement getNode(int num){
        try{
            return list.get(num);
        }
        catch (IndexOutOfBoundsException e){
            System.out.println("No Such Element");
        return null;
        }
    }
    
    ListElement getNode(String name){
        ListIterator<ListElement> iterator = list.listIterator();
        Pattern pattern = Pattern.compile(name);
        Matcher matcher;
        while (iterator.hasNext()){
            ListElement nextIt =iterator.next();
            matcher=pattern.matcher(nextIt.name);
            if(matcher.find()){
                return iterator.next(); 
            }
        }
        return null;
    }
       
    void printList(){
       ListIterator<ListElement> iterator = list.listIterator();
       while (iterator.hasNext()){
           System.out.println(iterator.next().name);
           System.out.println(iterator.previous().description);
           System.out.println(iterator.next().date);
           System.out.println(iterator.previous().contacts);
           iterator.next();
       }
       
    }
    
    List<Date> scanDate(){
        List <Date> dateList = new LinkedList();
        ListIterator<ListElement> iterator = list.listIterator();
        while (iterator.hasNext()){
            dateList.add(iterator.next().date);
        }
        return dateList;
    }    
    
    void writeFile(String fileName)throws IOException, ClassNotFoundException{
        ListIterator<ListElement> iterator = list.listIterator();
        FileWriter writer = new FileWriter(fileName);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH-mm");
        PrintWriter printWriter = new PrintWriter(writer);
        while (iterator.hasNext()){
            printWriter.println(iterator.next().name);
            printWriter.println(iterator.previous().description);            
            printWriter.println(format.format(iterator.next().date));
            printWriter.println(iterator.previous().contacts);
            iterator.next();
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
            add(name,description,date,contacts);
        }
    }
}

