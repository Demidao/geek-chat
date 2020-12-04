package com.demid.geekchat.server;

import com.sun.tools.javac.Main;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class ServerApp {
    private final static int PORT = 8189;
    public static void main(String[] args) {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1); // создание программного сервера, который обрабатывает подключение клиентов
        EventLoopGroup workerGroup = new NioEventLoopGroup(); // создание программного сервера, который работает с запросами клиентов

        try{
            ServerBootstrap b = new ServerBootstrap(); // создается для преднастроек сервера - какой порт слушать, как общаться с клиентами, опции и т.п.
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // подключаем канал для подключения клиентов к серверу
                    .childHandler(new ChannelInitializer<SocketChannel>() { //при каждом общении клиента с сервером, нужно
                                                                // настроить это общение с помощью childHandler
                                                                // Информация о подключении лежить в ChannelInitializer'е  - адрес, потоки отправки данных и т.п.


                        protected void initChannel(SocketChannel socketChannel) throws Exception { // при подключении клиента, инициализация клиента на сервере происходит тут
                            socketChannel.pipeline().addLast(new StringDecoder(), new StringEncoder(), new MainHandler()); // после создания обработчика MainHandker, нужно его добавить в
                                                                                // конвеер обработчиков (pipeline)
                        }
                    });
            ChannelFuture future = b.bind(PORT).sync(); // запускаем сервер. Bind() - указание серверу,
                                                                // что нужно запускаться на порту 8189, а sync() - сам запуск
                                                                // Объект типа future - объект, которые содержит в себе инфо о какой-то выполняемой задаче - поток, асинхронная задача и т.п.
                                                                // в данном случае используется для того, чтобы получать инфу о работающем сервере, что с ним происходит
            future.channel().closeFuture().sync(); // после запуска сервера мы ждем, пока его кто-то остановит, чтобы "идти" дальше.

        } catch (Exception e){
            e.printStackTrace();
        } finally { // после того, как кто-то остановил сервак, нам в ЛЮБОМ СЛУЧАЕ нжно закрыть пулы потоков, чтобы завершить окончательно программу.
            bossGroup.shutdownGracefully();
            workerGroup.shutdown();
        }
    }
}
