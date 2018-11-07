/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package taskmanager;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Scanner;
import java.io.*;

public class Journal implements Serializable {
    static final long serialVersionUID = -7588980448693010399L;
    private class ListElement implements Serializable{
        static final long serialVersionUID = -7588980448693010399L;
        ListElement next;
        String name;
        String description;
        Date date = new Date();
        String contacts;
    }
    
    private class JournalList implements Serializable{
        static final long serialVersionUID = -7588980448693010399L;
        private ListElement head;
        private ListElement tail;
        
        private void addElement(){
            ListElement node = new ListElement();
            Scanner scan = new Scanner(System.in);
            node.name = scan.nextLine();
            node.description = scan.nextLine();
            try{
                node.date = new SimpleDateFormat("yyyy-mm-dd").parse(scan.nextLine());
            }
            catch(ParseException e){
                System.err.println("Wront date format");
            }
            node.contacts = scan.nextLine();
            
            if (tail == null){
                head = node;
                tail = node;
            }
            else{
                tail.next=node;
                tail=node;
            }
        }
        
        private ListElement getNodeByNum(int num){            
            ListElement lister = head;
            int count = 0;
            while(lister.next !=null && num>count){
                lister = lister.next;
                count++;
            }
            if(num>count){
                System.out.println("There is no element with this num");
                return null;
            }
            else return lister;
        }
        
        private void deleteNode(int num){
            if(head == null)       
            return;            
 
            if (head == tail) {     
                head = null;       
                tail = null;
                return;             
            }
            if(num==0){
                head=head.next;
                return;
            }
            ListElement lister = head;
            int count=0;
            while(lister.next != null && num != -1){
                if(num==0){
                    if(lister.next==tail){
                        tail=lister;
                    }
                lister.next=lister.next.next;
                return;
                }                
                lister=lister.next;
                num--;
            }          
        }        
    }
    
    private JournalList jList = new JournalList();
    
    void addRecord(){
        jList.addElement();
    }
    void deleteRecord(int num){
        
    }
    void deleteRecord(String nameToDelete){
        
    }
    void deleteRecord(Date dateToDelete){
        
    }
    
    void editRecord(int num){
        ListElement lister = jList.head;
         System.out.print("NUM"+num);
        while(lister!=null && num!=-1){
            System.out.print("NUM"+num);
            if(num==0){
                Scanner scan = new Scanner(System.in);
                System.out.print("\n Input name of record: ");
                lister.name = scan.nextLine();
                System.out.print("\n Input description: ");
                lister.description = scan.nextLine();
                try{
                    System.out.print("\n Input date (yyyy-mm-dd): ");
                    lister.date = new SimpleDateFormat("yyyy-mm-dd").parse(scan.nextLine());
                }
                catch(ParseException e){
                    System.err.println("Wront date format");
                }
            }
            lister=lister.next;
            num--;
        }
        System.out.print("NUM"+num);
    }
    
   // Вывод для проверки, в дальнейшем уйдет в интерфейс
    void printRecords(){
        ListElement lister = jList.head;
        int counter=0;               
        while(lister != null){
            System.out.println("======| "+counter+" |======");
            System.out.println(lister.name);
            System.out.println(lister.description);
            System.out.println(lister.date);
            System.out.println(lister.contacts);
            System.out.println("=================");
            lister = lister.next;
            counter++;
        }
    }
   
    
   
    void writeFile(String fileName)throws IOException, ClassNotFoundException{
        Journal jObject = new Journal();
        jObject.jList = this.jList;
        FileOutputStream fos = new FileOutputStream(fileName);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(jObject);
        oos.flush();
        oos.close();
    }
    void readFile(String fileName) throws IOException, ClassNotFoundException{
        FileInputStream fis = new FileInputStream(fileName);
        ObjectInputStream oin = new ObjectInputStream(fis);
        Journal jObject = (Journal) oin.readObject();  
        this.jList = jObject.jList;
    }
}
