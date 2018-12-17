/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TaskServer;

import java.util.Date;
import java.util.List;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 *
 * @author Nikita
 */
public class ServerHandler extends ChannelInboundHandlerAdapter  {
    private final Charset charset = Charset.forName("UTF-8");    
    GUI gui = new GUI(); 
    int lastSendedTaskIndex = 0;
    
    
    ServerHandler(GUI g){
        this.gui=g;        
        gui.setVisible(true);         
    }
    
    
    @Override
    public void channelRead(final ChannelHandlerContext ctx, Object msg) {
        ByteBuf in = (ByteBuf) msg;
        String mess = in.toString(CharsetUtil.UTF_8);        
        if("Connected".equals(mess)||"Ready".equals(mess)){
            System.out.println("Connected");
            final ByteBuf task = ctx.alloc().buffer(4);            
            int index = 0;
            boolean flag = true;
            while(flag){
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH-mm");
                if(index>=gui.getJournal().getCount()){
                    index=0;
                }
                Date currentTime = new Date();
                if(format.format(gui.getJournal().getNode(index).getDate()).equals(format.format(currentTime))){
                    System.out.println("Found index last index: "+lastSendedTaskIndex);
                    System.out.println("Found index : "+index);
                    lastSendedTaskIndex = index;
                    String s = gui.getJournal().getNode(index).getName();        
                    int length = s.length();        
                    task.writeInt(length);
                    task.writeCharSequence(s, charset);        
                    s = gui.getJournal().getNode(index).getDescription();
                    length = s.length();        
                    task.writeInt(length);
                    task.writeCharSequence(s, charset);                   
                    final ChannelFuture f = ctx.writeAndFlush(task);                
                    flag = false;
                    }
                index++;            
            }  
        }
        if("Done".equals(mess)){
            ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                   .addListener(ChannelFutureListener.CLOSE);
        }
        if("Later".equals(mess)){
            System.out.println("Time delayed");
            System.out.println("Delayed index : "+lastSendedTaskIndex);
            Date laterTime = new Date(gui.getJournal().getNode(lastSendedTaskIndex).getDate().getTime()+60000);
            gui.getJournal().getNode(lastSendedTaskIndex).setDate(laterTime);
            try{
                    gui.getJournal().writeFile("Journal.txt");
                } catch(IOException ex){
                    System.err.println(ex);
                } catch(ClassNotFoundException ex){
                    System.err.println(ex);
                }
        }
    }
    
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER);           
            
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
    
}
