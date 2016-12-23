package lee.study.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.nio.ByteBuffer;
import java.util.Date;
import java.util.List;

import lee.study.bean.WebSocketFrameBean;
import lee.study.util.CodecUtil;

public class WebsocketEncoder extends MessageToMessageEncoder<WebSocketFrameBean>{

	@Override
	protected void encode(ChannelHandlerContext ctx, WebSocketFrameBean frame,List<Object> out) throws Exception {
		byte head1 = 0;
		//FIN
		head1 |= frame.getFIN()<<7;
		//RSV1
		head1 |= frame.getRSV1()<<6;
		//RSV2
		head1 |= frame.getRSV2()<<5;
		//RSV3
		head1 |= frame.getRSV3()<<4;
		//opcode
		head1 |= frame.getOpcode();
		byte head2 = 0;
		//MASK
		head2 |= frame.getMASK()<<7;
		//data lenth
		ByteBuf buf = null;
		int size = 2+frame.getPayloadData().length;
		int lengthByteCount = 0;
		if(frame.getPayloadData().length<126){
			head2 |= frame.getPayloadData().length;
		}else if(frame.getPayloadData().length<=0xFFFF){
			lengthByteCount = 2;
			head2 |= 0x7E;
		}else{
			lengthByteCount = 8;
			head2 |= 0x7F;
		}
		size+=lengthByteCount;
		if(frame.getMASK()==1){
			size+=4;
		}
		buf = ctx.alloc().buffer(size);
		buf.writeByte(head1);
		buf.writeByte(head2);
		//data length
		if(lengthByteCount==2){
			buf.writeShort(frame.getPayloadData().length);
		}else if(lengthByteCount==8){
			buf.writeLong(frame.getPayloadData().length);
		}
		//mask key
		if(frame.getMASK()==1){
            byte[] maskKey = ByteBuffer.allocate(4).putInt((int)new Date().getTime()).array();
            CodecUtil.mask(maskKey,frame.getPayloadData());
            buf.writeBytes(maskKey);
		}
		buf.writeBytes(frame.getPayloadData());
		out.add(buf);
	}
	
}
