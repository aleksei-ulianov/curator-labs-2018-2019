/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TaskServer;

import java.util.Date;

/**
 * Класс задачи со свойствами <b>name</b>, <b>description</b>, <b>date</b>, <b>contacts</b>, <b>checked</b>.
 * @author Nikita
 */
 public class Task{
        /** Поле имя задачи */
        String name;
        /** Поле описание задачи */
        String description;
        /** Поле дата задачи */
        Date date = new Date();
        /** Поле контакты задачи */
        String contacts;
        /** Поле флаг проверки задачи */
        boolean checked = false;
        
        Task(String name, String description, Date date, String contacts){
            this.name=name;
            this.description=description;
            this.date=date;
            this.contacts=contacts;
        }
        
    /**
     * Метод возвращает строку с именем задачи
     * @return возвращает строку с именем задачи
     */
    public String getName(){
            return name;
        }
        
     /**
     * Метод возвращает строку с описанием задачи
     * @return возвращает строку с описанием задачи
     */
    public String getDescription(){
            return description;
        }
        
     /**
     * Метод возвращает дату задачи
     * @return возвращает дату Date задачи
     */
    public Date getDate(){
            return date;
        }
        
     /**
     * Метод возвращает строку с контактами задачи
     * @return возвращает строку с контактами задачи
     */
    public String getContacts(){
            return contacts;
        }
        
    /**
     * Метод устанавливает значение имени задачи
     * @param name новое имя задачи
     */
    public void setName(String name){
            this.name=name;
        }
        
    /**
     * Метод устанавливает значение описания задачи
     * @param description новое описание задачи
     */
    public void setDescription(String description){
            this.description=description;
        }
        
    /**
     * Метод устанавливает значение даты задачи
     * @param date новая дата задачи
     */
    public void setDate(Date date){
             this.date=date;
        }
        
    /**
     * Метод устанавливает значение контактов задачи
     * @param contacts новые контакты задачи
     */
    public void setContacts(String contacts){
            this.contacts=contacts;
        }
        
    /**
     * Метод помечает задачу как проверенную или непроверенную
     * @param flag флаг
     */
    public void setChecked (boolean flag){
            this.checked = flag;
        }
        
    /**
     * Метод возвращает значение флага, показывая проверена задача или нет
     * @return возвращает значение флага
     */
    public boolean isChecked(){
            return checked;
        }
        
    }       

