package com.demid.geekchat.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.ArrayList;
import java.util.List;

public class MainHandler extends SimpleChannelInboundHandler<String> { // ...Inbound означает, что обработчик работает с входящими сообщениями, а если бы ...Outhandler, то с исходящими

    private static final List<Channel> channels = new ArrayList<>(); // каналы, через которые подключаются клиенты
    private String clientName; // имя подключившегося клиента
    private static int newClientIndex = 1; // порядковый номер подключившегося клиента

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception { // когда клиент подключается, можно что-то предварительно сделать.
        System.out.println("Client connected!" + ctx);
        channels.add(ctx.channel());
        clientName = "Client " + newClientIndex;
        newClientIndex++;
        broadcastMessage("Server: ", clientName+ "...connected now!");
    }


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        System.out.println("Received message:" + s);
        if (s.startsWith("/")){
            if (s.startsWith("/changename")){
                String newClientName = s.split("\\s", 2)[1];
                broadcastMessage("Server: ", clientName+ " change name to " + newClientName );
                clientName = newClientName;
            }
            return;
        }
        broadcastMessage(clientName, s);
    }

    private void broadcastMessage(String clientName, String message){
        String out = String.format("[%s]: %s", clientName, message);
        for (Channel c : channels){
            c.writeAndFlush(out);
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // в случае возникновения ошибки при посылке сообщения, эксепшн формируется тут
        System.out.println("Client " + clientName + " disconnected");
        channels.remove(ctx.channel());
        broadcastMessage("Server: ", clientName + " disconnected");
        ctx.close();
    }
}
