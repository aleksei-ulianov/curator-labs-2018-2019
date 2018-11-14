/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package taskmanager;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Nikita
 */
 public class ListElement implements Serializable{
        static final long serialVersionUID = -7588980448693010399L;
        String name;
        String description;
        Date date = new Date();
        String contacts;
        
        public String getName(){
            return name;
        }
        
        public String getDescription(){
            return description;
        }
        
        public Date getDate(){
            return date;
        }
        
        public String getContacts(){
            return contacts;
        }
    }       

