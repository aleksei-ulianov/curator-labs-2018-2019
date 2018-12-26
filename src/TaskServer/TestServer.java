/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TaskServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;


/**
 * Класс сервера для журнала задач
 * @author Nikita
 */


public class TestServer {
    /** Поле порт*/
    private int port;
    
    /**
     * Конструктор - создание нового объекта {@link TestServer} 
     * с параметром устанавливающим значение поля {@link TestServer#port}
     * @param port значение поля {@link TestServer#port}
     */
    public TestServer(int port) {
        this.port = port;
    }
    
    /**
     * Метод запускающий сервер и окно графического интерфейса {@link GUI}
     * @param gui
     * @throws Exception
     */
    public void run(GUI gui) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap(); // (2)
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class) // (3)
             .childHandler(new ChannelInitializer<SocketChannel>() {
                 @Override
                 public void initChannel(SocketChannel ch) throws Exception {
                     ch.pipeline().addLast(new ServerHandler(gui));
                 }
             })
             .option(ChannelOption.SO_BACKLOG, 128) 
             .childOption(ChannelOption.SO_KEEPALIVE, true); 
    
            ChannelFuture f = b.bind(port).sync(); // (7)
    
          
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }    
  
    /**
     * Метод main запускает окно графического интерфейса и сервер журнала задач
     * @param args параметры запуска
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        GUI gui = new GUI();
        gui.InitJList();
        gui.InitListeners();
        gui.setVisible(true);
        int port = 8080;        
        new TestServer(port).run(gui);       
    }    
}
