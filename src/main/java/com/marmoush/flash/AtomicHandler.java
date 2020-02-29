package com.marmoush.flash;

import io.netty.buffer.*;
import io.netty.channel.*;
import io.netty.util.CharsetUtil;

import java.time.*;
import java.util.concurrent.atomic.AtomicLong;

public class AtomicHandler extends ChannelInboundHandlerAdapter {
  private final AtomicLong id;
  private final LocalDateTime appStartTime;
  private final long delay;

  public AtomicHandler(AtomicLong id, LocalDateTime appStartTime, long delay) {
    this.id = id;
    this.appStartTime = appStartTime;
    this.delay = delay;
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws InterruptedException {
    ByteBuf inBuffer = (ByteBuf) msg;
    var received = Long.parseLong(inBuffer.toString(CharsetUtil.UTF_8).trim());
    while (Duration.between(appStartTime, LocalDateTime.now()).toMillis() < delay) {
      Thread.sleep(1000);
    }
    if (received > id.get()) {
      id.set(received);
    }
    ctx.writeAndFlush(Unpooled.copiedBuffer("" + id.incrementAndGet(), CharsetUtil.UTF_8));
  }

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) {
    ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    cause.printStackTrace();
    ctx.close();
  }
}