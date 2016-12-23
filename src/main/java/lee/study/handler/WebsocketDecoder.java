package lee.study.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;

import java.util.List;

import lee.study.bean.WebSocketFrameBean;
import lee.study.util.CodecUtil;

public class WebsocketDecoder extends ByteToMessageDecoder{

	private int status = 1;
	private byte FIN = 1;
	private boolean isMask = false;
	private long dataLenth;
	private byte[] maskKey; 
	private int opcode;
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf,
			List<Object> outList) throws Exception {
		//第一个字节处理
		if(status==1){
			if(!byteBuf.isReadable()){
				return;
			}
			//10000010
			byte head1 = byteBuf.readByte();
			//是否为最后一帧
			if((head1&0x80)!=0){
				//00000010
				int RSVs = (head1&0x70)>>4;
				if(RSVs==0){
					//帧类型
					opcode = head1&0xF;
					if(opcode==0||opcode<0||opcode>0xF){
						//暂时不支持的帧类型
						throw new RuntimeException("un support!");
					}
				}else{
					//暂时不支持扩展帧
					throw new RuntimeException("un support!");
				}
			}else{
				//暂时不支持分帧
				throw new RuntimeException("un support!");
			}
			this.status++;
		}
		//第二个字节处理
		if(status==2){
			if(!byteBuf.isReadable()){
				return;
			}
			//10111110
			byte head2 = byteBuf.readByte();
			//是否有掩码
			if((head2&0x80)!=0){
				this.isMask=true;
			}
			dataLenth = head2&0x7F;
			this.status++;
		}
		//数据长度
		if(status==3){
			if(!byteBuf.isReadable()){
				return;
			}
			/*
			 * length<126:7 bits
			 * length=126:7+16 bits
			 * length=127:7+64 bits
			 */
			int lengthHeadByteCount = 0;
			if(dataLenth==126){
				//Extended payload length 占2字节
				lengthHeadByteCount = 2;
			}else if(dataLenth==127){
				//Extended payload length continued 占8字节
				lengthHeadByteCount = 8;
			}
			if(byteBuf.readableBytes()<lengthHeadByteCount){
				return;
			}
			//计算真实接收的数据长度
			if(lengthHeadByteCount==2){
				dataLenth = byteBuf.readUnsignedShort();
			}else if(lengthHeadByteCount==8){
				dataLenth = byteBuf.readLong();
			}
			this.status++;
		}
		//掩码处理
		if(status==4){
			if(isMask){
				//Masking-key 占4字节
				if(byteBuf.readableBytes()<4){
					return;
				}
				if(maskKey==null){
					maskKey = new byte[4];
				}
				byteBuf.readBytes(maskKey);
			}
			this.status++;
		}
		//数据读取
		if(status==5){
			if(byteBuf.readableBytes()>=dataLenth){
				if(dataLenth>Integer.MAX_VALUE){
					throw new TooLongFrameException("dataLenth is too long:"+dataLenth);
				}
				byte[] bts = new byte[(int) dataLenth];
				byteBuf.readBytes(bts);
				this.status=1;
				if(isMask){
					CodecUtil.mask(maskKey,bts);
				}
				outList.add(new WebSocketFrameBean(FIN,(byte)0,(byte)0,(byte)0,(byte)opcode,isMask?(byte)1:(byte)0,bts));
			}
		}
	}
	
	public static void main(String[] args) {
		//WebSocket08FrameDecoder
		//System.out.println(Integer.toBinaryString(128));
		/*
		 * 10001101
		 * 10000000
		 */
		byte b = (byte) 200;
		System.out.println((b&0xFF)>100);
		System.out.println(Integer.toBinaryString(b));
		System.out.println(b&0x80);
		b = (byte) ((b&0xFF)>>1);
		System.out.println(Integer.toBinaryString(b&0xFF));
		System.out.println(128>>1);
	}
	
}
