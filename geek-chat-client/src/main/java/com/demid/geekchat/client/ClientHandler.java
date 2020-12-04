package com.demid.geekchat.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientHandler extends SimpleChannelInboundHandler<String>  {

        private Callback onMessageReceivedCallback;

        public ClientHandler(Callback onMessageReceiveCallback){
            this.onMessageReceivedCallback = onMessageReceiveCallback;
        }

        @Override
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
            if (onMessageReceivedCallback != null){
               onMessageReceivedCallback.callback(s);
            }
        }
}
