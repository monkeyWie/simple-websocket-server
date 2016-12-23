package lee.study.handler;

import java.io.FileOutputStream;

import lee.study.bean.HttpRequstBean;
import lee.study.bean.HttpResponseBean;
import lee.study.bean.WebSocketFrameBean;
import lee.study.util.CodecUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class WebsocketHandler extends ChannelInboundHandlerAdapter{

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		if(msg instanceof HttpRequstBean){
			HttpRequstBean requstBean = (HttpRequstBean) msg;
			HttpResponseBean responseBean = new HttpResponseBean();
			//是websocket握手请求
			if(requstBean.getHeads().get("Upgrade")!=null&&
				requstBean.getHeads().get("Upgrade").indexOf("websocket")>-1&&
				requstBean.getHeads().get("Connection")!=null&&
				requstBean.getHeads().get("Connection").indexOf("Upgrade")>-1){
				String secWebSocketKey = requstBean.getHeads().get("Sec-WebSocket-Key");
				if(secWebSocketKey!=null){
					responseBean.setProto(requstBean.getProto());
					responseBean.setStatus(101);
					responseBean.setStatusMsg("Switching Protocols");
					responseBean.getHeads().put("Connection","Upgrade");
					responseBean.getHeads().put("Upgrade","websocket");
					/*
					 * RFC6455
					 * 握手响应：BASE64(SHA1(Sec-WebSocket-Key+258EAFA5-E914-47DA-95CA-C5AB0DC85B11))
					 */
					responseBean.getHeads().put("Sec-WebSocket-Accept",CodecUtil.base64(CodecUtil.SHA1(secWebSocketKey+"258EAFA5-E914-47DA-95CA-C5AB0DC85B11")));
					ctx.writeAndFlush(responseBean);
					//握手成功进入websocket双向通信,移除http编、解码器
					ctx.pipeline().remove("http-decoder");
					ctx.pipeline().remove("http-encoder");
					//添加websocket frame编、解码器
					ctx.pipeline().addFirst(new WebsocketDecoder());
					ctx.pipeline().addFirst(new WebsocketEncoder());
				}
			}
		}else if(msg instanceof WebSocketFrameBean){
			WebSocketFrameBean frameBean = (WebSocketFrameBean) msg;
			//chrome浏览器不支持服务器返回mask data
			//WebSocketFrameBean response = new WebSocketFrameBean((byte)1,(byte)0,(byte)0,(byte)0,(byte)1,(byte)1,null);
			WebSocketFrameBean response = new WebSocketFrameBean((byte)1,(byte)0,(byte)0,(byte)0,(byte)1,(byte)0,null);
			String received = "";
			if(frameBean.getOpcode()==1){//文本
				received = "RECEIVED:"+new String(frameBean.getPayloadData());
			}else if(frameBean.getOpcode()==2){//二进制
				FileOutputStream outputStream = new FileOutputStream("d:/ws.dat");
				outputStream.write(frameBean.getPayloadData());
				outputStream.flush();
				outputStream.close();
				received = "FILE SIZE:"+frameBean.getPayloadData().length;
				response.setPayloadData(received.getBytes());
			}
			response.setPayloadData(received.getBytes());
			ctx.writeAndFlush(response);
		}
		
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
	
}
