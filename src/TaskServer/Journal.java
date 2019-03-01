/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TaskServer;

import java.io.*;
import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Класс журнала задач содержащий список задач {@link Journal#list}
 * @author Nikita
 */
public class Journal {
    
    /**
     * Поле список задач {@link Task}
    */
    private static List<Task> list = new ArrayList<>(); 
    /**
     * Поле путь к файлу XML
     */
    public static final String xmlFilePath = "Journal.xml";
    
    /**
     * {@link DefaultHanlder}
     */
    private static class XMLHandler extends DefaultHandler {
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if (qName.equals("Task")) {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String name = attributes.getValue("name");
                String description = attributes.getValue("description");
                Date date = new Date();
                try{
                    date = format.parse(attributes.getValue("date"));
                } catch(ParseException ex){
                }
                String contacts = attributes.getValue("contacts");
                list.add(new Task(name, description, date, contacts));
            }
        }
    }
    
    /**
     * Метод добавления новой задачи в журнал
     * @param name имя задачи
     * @param description описание задачи
     * @param date дата задачи
     * @param contacts контакты задачи
     */
    void add (String name, String description, Date date, String contacts) 
            throws ParseException{
        Task node = new Task(name,description,date,contacts);        
        list.add(node);
    }
    
    /**
     * Метод удаления задачи по её имени
     * @param name имя задачи
     */
    void deleteElement(String name){
       if(getNode(name)==null){
           System.out.println("Nothing to delete, there is no task with \""+name+"\" name.");
       }
       list.remove(getNode(name));  
       System.out.println("Task \""+name+"\" deleted successfully.");
    }
    
    /**
     * Метод удаления задачи из списка по её номеру
     * @param num номер задачи
     */
    void deleteElement(int num){        
        list.remove(num);        
    }
    
    /**
     * Метод получения задачи из списка по её номеру
     * @param num номер задачи
     * @return возвращает найденную задачу, если задача не найдена null
     */
    Task getNode(int num){
        try{
            return list.get(num);
        }
        catch (IndexOutOfBoundsException e){
            System.out.println("No Such Element");
        return null;
        }
    }
    
    /**
     * Метод получения задачи из списка по её имени
     * @param name имя задачи
     * @return возвращает найденную задачу, если задача не найдена null.
     */
    Task getNode(String name){
        for(Task task : list){
            if(task.getName().equals(name)){
                return task;
            }
        }
        return null;        
    }       

    /**
     * Метод возвращает количество задач журнале
     * @return возвращает количество задач
     */
    int getCount(){
        int count = 0;
        for(Task task: list){
            count++;
        }
        return count;
    }
    
    /**
     * Метод возвращающий список дат всех задач в журнале
     * @return возвращает список дат {@link Date}
     */
    List<Date> scanDate(){
        List <Date> dateList = new LinkedList();
        for(Task task : list){
            dateList.add(task.date);
        }
        return dateList;
    }    
    
    /**
     * Метод записи журнала в текстовый файл
     * @param fileName имя файла
     */
    void writeFile(String fileName)throws IOException, ClassNotFoundException{        
        FileWriter writer = new FileWriter(fileName);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm"); 
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
    /**
     * Метод чтения журнала из текстового файла
     * @param fileName имя файла
     */
    void readFile(String fileName) throws IOException, ParseException{
        File file = new File(fileName);
        Scanner scan = new Scanner(file, "utf-8");
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");   
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
    /**
     * Метод обработки задач, проверяется соответствует ли задача условиям, если нет, то она отбрасывается
     * @param tasks массив строк с полями {@link Task}
     */
    String[] checkTask (String[] tasks){
        String[] checkedTask = new String[tasks.length];
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");   
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
            System.err.println("Wrong date");
            return null;
        }
        checkedTask[3]=tasks[3];

        return checkedTask;
    }
    
    /**
     * Метод для записи журнала в файл в формате XML
     */
    public void writeXML() throws ParserConfigurationException, TransformerException{
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();
        Element root = document.createElement("Tasks");
        document.appendChild(root);        
         for(Task task:list){
             Element newTask = document.createElement("Task");
             root.appendChild(newTask);
             newTask.setAttribute("name", task.getName());
             Attr attr = document.createAttribute("description");
             attr.setValue(task.getDescription());
             newTask.setAttributeNode(attr);
             attr = document.createAttribute("date");
             attr.setValue(format.format(task.getDate()));
             newTask.setAttributeNode(attr);
             attr = document.createAttribute("contacts");
             attr.setValue(task.getContacts());
             newTask.setAttributeNode(attr);            
         }       
         TransformerFactory transformerFactory = TransformerFactory.newInstance();
         Transformer transformer = transformerFactory.newTransformer();
         transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
         transformer.setOutputProperty(OutputKeys.INDENT, "yes");
         DOMSource domSource = new DOMSource(document);
         StreamResult streamResult = new StreamResult(new File(xmlFilePath));
         transformer.transform(domSource, streamResult);
    }
    /**
     * Метод чтения журнала из файла в формате XML
     * @param fileName имя файла для чтения
     */
    public void readXML(String fileName){
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try{
            SAXParser parser = factory.newSAXParser();
            XMLHandler handler = new XMLHandler();
            parser.parse(new File(fileName), handler);
        } catch(ParserConfigurationException | SAXException | IOException ex){
        }       
    }
    
}

