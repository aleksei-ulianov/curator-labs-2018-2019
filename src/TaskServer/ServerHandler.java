/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TaskServer;

import java.util.Date;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import java.util.regex.*;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author Nikita
 */
public class ServerHandler extends ChannelInboundHandlerAdapter  {
    private final Charset charset = Charset.forName("UTF-8");    
    GUI gui = new GUI(); 
    int lastSendedTaskIndex = 0;
    final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    
    
    ServerHandler(GUI g){
        this.gui=g;        
        gui.setVisible(true);
    }
    
    /**
     * Переопределенный метод вызывается при получении сообщения.
     * Обрабатывает полученное сообщение как команду и выполняет действия соответствующие команде.
     * При получении команды Connected создает новый поток, который выполняет мониторинг задач и отправляет оповещения.
     * При получении команды Ready создает новый поток, выполняющий мониторинг задач.
     * При получении команды Later откладывает последнюю отправленную задачу на 5 минут
     * При получении команды New считывает парамерты идущие следом, если задача со считанным именем существует
     * то существующая задача перезаписывается, в случае если в журнале нет задачи с таким именем создается новая.
     * При получении команды Delete удаляет задачу с именем совпадающим с присланным параметром.
     * При получении команды Done закрывает соединение.
     * @param ctx экземпляр интерфейса ChannelHandlerContext
     * @param msg полученное сообщение
     */
    @Override
    public void channelRead(final ChannelHandlerContext ctx, Object msg) {
        
        class SilentCheck implements Runnable{
            @Override
            public void run(){
                final ByteBuf task = ctx.alloc().buffer(4);            
                int index = 0;
                boolean flag = true;
                while(flag){                
                    if(index>=gui.getJournal().getCount()){
                        index=0;
                    }
                    Date currentTime = new Date();
                    if(format.format(gui.getJournal().getNode(index).getDate()).equals(format.format(currentTime))){
                        if(gui.getJournal().getNode(index).isChecked()==false){
                            lastSendedTaskIndex = index;
                            String s = gui.getJournal().getNode(index).getName();        
                            int length = s.length();        
                            task.writeInt(length);
                            task.writeCharSequence(s, charset);        
                            s = gui.getJournal().getNode(index).getDescription();
                            length = s.length();        
                            task.writeInt(length);
                            task.writeCharSequence(s, charset);
                            gui.getJournal().getNode(index).setChecked(true);
                            final ChannelFuture f = ctx.writeAndFlush(task);                
                            flag = false;
                        }
                    }
                    index++;            
                }  
            }
        }
        
        ByteBuf in = (ByteBuf) msg;
        String mess = in.toString(CharsetUtil.UTF_8);        
        Pattern newTaskPattern = Pattern.compile("New\\,.*\\,.*\\,\\d\\d\\d\\d\\-\\d\\d\\-\\d\\d\\ \\d\\d\\:\\d\\d\\,.*");
        Pattern deletePattern = Pattern.compile("Delete\\,.*");
        Matcher newTaskMatcher = newTaskPattern.matcher(mess);
        Matcher deleteMatcher = deletePattern.matcher(mess);
        System.out.println("Message recieved: "+mess);
        
        if("Connected".equals(mess)){
            System.out.println("Client connected and ready to recieve alerts");
            Thread silentCheckThread = new Thread(new SilentCheck());
            silentCheckThread.start();
        }
        if("Ready".equals(mess)){
            System.out.println("Client ready to recieve new alerts");
            Thread silentCheckThread = new Thread(new SilentCheck());
            silentCheckThread.start();
        }
        if("Done".equals(mess)){
            System.out.println("Client Disconnected");
            ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                   .addListener(ChannelFutureListener.CLOSE);
        }
        if("Later".equals(mess)){
            System.out.println("Time delayed");
            System.out.println("Delayed index : "+lastSendedTaskIndex);
            Date laterTime = new Date(gui.getJournal().getNode(lastSendedTaskIndex).getDate().getTime()+300000);
            gui.getJournal().getNode(lastSendedTaskIndex).setDate(laterTime);
            gui.getJournal().getNode(lastSendedTaskIndex).setChecked(false);
            if("TXT".equals(gui.getConfigSelection())){
                try{
                        gui.getJournal().writeFile("Journal.txt");
                } catch(IOException ex){
                        System.err.println(ex);
                } catch(ClassNotFoundException ex){
                        System.err.println(ex);
                }
            }
            if("XML".equals(gui.getConfigSelection())){
                try{
                    gui.getJournal().writeXML();
                } catch(ParserConfigurationException | TransformerException ex){
                    System.err.println(ex);                    
                }
            }
        }
        if(newTaskMatcher.find()){
            System.out.println("New task recieved.");
            String find = newTaskMatcher.group();
            String[] splitStrings = find.split("\\,");
            for (int i = 0; i<splitStrings.length;i++){
                System.out.println("Index: "+i+" String: "+splitStrings[i]);
            }
            if(gui.getJournal().getNode(splitStrings[1])==null){                
                try{
                    gui.getJournal().add(splitStrings[1], splitStrings[2], format.parse(splitStrings[3]), splitStrings[4]);
                    gui.InitJList();
                } catch(ParseException ex){
                    System.err.println("Wrong date recieved.");
                }
                System.out.println("New task created.");
            } else {
                System.out.println("There is task with this name, changing old task.");
                try{
                    gui.getJournal().getNode(splitStrings[1]).setDescription(splitStrings[2]);
                    gui.getJournal().getNode(splitStrings[1]).setDate(format.parse(splitStrings[3]));
                    gui.getJournal().getNode(splitStrings[1]).setContacts(splitStrings[4]);
                    gui.InitJList();
                }catch(ParseException ex){
                    System.err.println("Wrong date recieved.");
                }
                System.out.println("Old task changed.");                
            }            
            if("TXT".equals(gui.getConfigSelection())){
                try{
                        gui.getJournal().writeFile("Journal.txt");
                } catch(IOException ex){
                        System.err.println(ex);
                } catch(ClassNotFoundException ex){
                        System.err.println(ex);
                }
            }
            if("XML".equals(gui.getConfigSelection())){
                try{
                    gui.getJournal().writeXML();
                } catch(ParserConfigurationException | TransformerException ex){
                    System.err.println(ex);                    
                }
            }
            gui.deSelect();
        }
        if(deleteMatcher.find()){
            String find = deleteMatcher.group();
            String[] splitStrings = find.split("\\,");
            System.out.println("Recieved delete command, trying to delete task: "+splitStrings[1]);
            gui.getJournal().deleteElement(splitStrings[1]);
            gui.InitJList();
            if("TXT".equals(gui.getConfigSelection())){
                try{
                        gui.getJournal().writeFile("Journal.txt");
                } catch(IOException ex){
                        System.err.println(ex);
                } catch(ClassNotFoundException ex){
                        System.err.println(ex);
                }
            }
            if("XML".equals(gui.getConfigSelection())){
                try{
                    gui.getJournal().writeXML();
                } catch(ParserConfigurationException | TransformerException ex){
                    System.err.println(ex);                    
                }
            }
            gui.deSelect();
        }
    }
    
    /**
     * Метод вызывается при окончании считывания, очищает буффер
     * @param ctx
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER);           
            
    }
    
    /**
     * Метод отлавливающий исключения
     * @param ctx экземпляр интерфейса ChannelHandlerContext
     * @param cause пойманное исключение
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
    
}
