package lee.study.handler;

import lee.study.bean.HttpResponseBean;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.http.HttpConstants;

public class HttpEncoder extends MessageToByteEncoder<HttpResponseBean>{

	@Override
	protected void encode(ChannelHandlerContext ctx, HttpResponseBean responseBean,
			ByteBuf out) throws Exception {
		//HTTP协议版本
		out.writeBytes(responseBean.getProto().getBytes());
		writeSP(out);
		//状态码
		out.writeBytes((responseBean.getStatus()+"").getBytes());
		writeSP(out);
		//状态描述
		out.writeBytes(responseBean.getStatusMsg().getBytes());
		//回车换行
		writeCRLF(out);
		//请求头
		for (String name : responseBean.getHeads().keySet()) {
			out.writeBytes(name.getBytes());
			writeCOLON(out);
			writeSP(out);
			out.writeBytes(responseBean.getHeads().get(name).getBytes());
			writeCRLF(out);
		}
		//回车换行结尾
		writeCRLF(out);
		//下面就是响应体
		/*while (out.writerIndex()>out.readerIndex()) {
			byte b = out.readByte();
			System.out.print((char)b);
		}*/
	}
	
	private void writeCOLON(ByteBuf out){
		out.writeByte(HttpConstants.COLON);
	}
	
	private void writeSP(ByteBuf out){
		out.writeByte(HttpConstants.SP);
	}
	
	private void writeCRLF(ByteBuf out){
		out.writeByte(HttpConstants.CR);
		out.writeByte(HttpConstants.LF);
	}

}
