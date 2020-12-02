package com.demid.geekchat.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class MainHandler extends ChannelInboundHandlerAdapter { // ...Inbound означает, что обработчик работает с входящими сообщениями, а если бы ...Outhandler, то с исходящими

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception { // когда клиент подключается, можно что-то предварительно сделать.
        System.out.println("Client connected!");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // чтение сообщения клиента. Но предварительно нужно значть, что именно явлеяетя сообщением.
        // При работе с Netty есть правило, что все сообщения заворачиваются в байт-буффер. Т.о. если что-то отправляем в сеть, это
        //должно быть завернуто в байт-буффер.
        ByteBuf buf = (ByteBuf) msg; // заливаем в буффер msg
        while (buf.readableBytes() > 0){
            System.out.print((char) buf.readByte());
        }
        buf.release(); // освобождение байт-буффера, нужно обязательно сделать. Если этого не делать, то будет утечка памяти
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // в случае возникновения ошибки при посылке сообщения, эксепшн формируется тут
        cause.printStackTrace();
        ctx.close();

    }
}
