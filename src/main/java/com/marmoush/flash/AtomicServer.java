package com.marmoush.flash;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;
import java.time.*;
import java.util.concurrent.atomic.AtomicLong;

public final class AtomicServer {

  public static void main(String[] args) throws Exception {
    var startTime = LocalDateTime.now();
    long delay = 10000;
    AtomicLong id = new AtomicLong();
    EventLoopGroup group = new NioEventLoopGroup();

    try {
      ServerBootstrap serverBootstrap = new ServerBootstrap();
      serverBootstrap.group(group);
      serverBootstrap.channel(NioServerSocketChannel.class);
      serverBootstrap.localAddress(new InetSocketAddress("localhost", 9999));

      serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
        protected void initChannel(SocketChannel socketChannel) {
          socketChannel.pipeline().addLast(new AtomicHandler(id, startTime, delay));
        }
      });

      ChannelFuture channelFuture = serverBootstrap.bind().sync();
      channelFuture.channel().closeFuture().sync();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      group.shutdownGracefully().sync();
    }
  }
}