package com.demid.geekchat.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class Network {
    private SocketChannel channel;
    private final static String HOST = "localhost";
    private final static int PORT = 8189;

    public Network(Callback onMessageReceivedCallback){
        Thread t = new Thread(() -> {
            EventLoopGroup workerGroup = new NioEventLoopGroup(); // для работы с сетью клиенту нужен пул потоков. Создаем тут этот пулf
                                                                    // к клиенту никто не будет подключаться, поэтому пул потоков будет только один для обработки сетевых событий
            try{
                Bootstrap b = new Bootstrap();
                b.group(workerGroup)
                        .channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer<SocketChannel>() {

                            @Override
                            protected void initChannel(SocketChannel socketChannel) throws Exception {
                                channel = socketChannel; //когда открывается соединение, мы сохраняем его в поле класса
                                socketChannel.pipeline().addLast(new StringDecoder(),
                                        new StringEncoder(),
                                        new ClientHandler(onMessageReceivedCallback));
                            }
                        });
                ChannelFuture future = b.connect(HOST, PORT).sync();
                future.channel().closeFuture().sync();
            } catch (Exception e){
                e.printStackTrace();
            } finally {
                workerGroup.shutdownGracefully();
            }

        });
        t.setDaemon(true);
        t.start();
    }

    public void sendMessage(String str){ // метод отправки сообщений
        channel.writeAndFlush(str);
    }

    public void close(){
        channel.disconnect();
        channel.shutdown();
        channel.close();
    }

}
