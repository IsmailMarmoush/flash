package com.marmoush.pulse;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.concurrent.atomic.AtomicLong;

public class AtomicHandler extends ChannelInboundHandlerAdapter {
  private final AtomicLong id;

  public AtomicHandler(AtomicLong id) {
    this.id = id;
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) {
    ByteBuf inBuffer = (ByteBuf) msg;
    var received = Long.parseLong(inBuffer.toString(CharsetUtil.UTF_8).trim());
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