package lee.study.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.http.HttpConstants;

import java.util.ArrayList;
import java.util.List;

import lee.study.bean.HttpRequstBean;

public class HttpDecoder extends ByteToMessageDecoder{

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf,
			List<Object> outList) throws Exception {
		System.out.println("--------begin---------");
		/*if(findBodyLimiter(byteBuf)==-1){
			return;
		}*/
		/*int aaa = 0;
		while(byteBuf.writerIndex()>byteBuf.readerIndex()){
			byte b = byteBuf.readByte();
			System.out.print(aaa+" "+" "+b);
			System.out.print((char)b+" ");
			aaa++;
			if(b==HttpConstants.CR){
				
			}
		}*/
		List<Integer> CRLFIndexList = new ArrayList<>();
		int ret = -1;
		int index = -1;
		int beginIndex = 0;
		do {
			index = findCRLF(byteBuf,beginIndex);
			if(index!=-1){
				CRLFIndexList.add(index);
				int nextIndex = findCRLF(byteBuf,index+1);
				if(nextIndex!=-1&&nextIndex==index+2){
					ret = index;
					break;
				}else{
					beginIndex = index+1;
				}
			}
		} while (index!=-1);
		if(ret!=-1){
			HttpRequstBean requstBean = new HttpRequstBean();
			//遍历header
			beginIndex = 0;
			for (int j = 0; j < CRLFIndexList.size(); j++) {
				byte[] bts = new byte[CRLFIndexList.get(j)-beginIndex];
				for (int i = 0; i < bts.length; i++) {
					bts[i] = byteBuf.readByte();
				}
				String headline = new String(bts);
//				System.out.println(headline);
				if(j==0){
					String[] strs = headline.split(String.valueOf(HttpConstants.SP_CHAR));
					requstBean.setMethod(strs[0]);
					requstBean.setUrl(strs[1]);
					requstBean.setProto(strs[2]);
				}else{
					int splitIndex = headline.indexOf(HttpConstants.COLON);
					if(splitIndex>-1){
						requstBean.getHeads().put(headline.substring(0,splitIndex),headline.substring(splitIndex+1).trim());
					}
				}
				byteBuf.skipBytes(2);
				beginIndex = CRLFIndexList.get(j)+2;
			}
			byteBuf.skipBytes(2);
			outList.add(requstBean);
		}
		/*int beginIndex = 0;
		int endIndex = -1;
		HttpRequstBean requstBean = new HttpRequstBean();
		requstBean.setHeads(new LinkedHashMap<String,String>());
		while((endIndex = findCRLF(byteBuf,beginIndex))!=-1){
			byte[] bts = new byte[endIndex-beginIndex];
			for (int i = 0; i < bts.length; i++) {
				bts[i] = byteBuf.readByte();
			}
			beginIndex = endIndex;
			System.out.println(new String(bts));
		}*/
	}

	/*private int findBodyLimiter(ByteBuf byteBuf){
		int ret = -1;
		int index = -1;
		int beginIndex = 0;
		do {
			index = findCRLF(byteBuf,beginIndex);
			if(index!=-1){
				if(findCRLF(byteBuf,index+1)!=-1){
					ret = index;
					break;
				}else{
					beginIndex = index+1;
				}
			}
		} while (index!=-1);
		return ret;
	}*/
	
	private int findCRLF(ByteBuf byteBuf,int beginIndex){
		int ret = -1;
		int index =-1;
		do {
			//寻找回车符
			index = byteBuf.indexOf(beginIndex,byteBuf.readableBytes(),HttpConstants.CR);
			if(index!=-1){
				//寻找相邻在后的换行符
				if(byteBuf.indexOf(index+1,index+2,HttpConstants.LF)!=-1){
					ret = index;
					break;
				}else{
					beginIndex = index+1;
				}
			}
		} while (index!=-1);
		return ret;
	}
	
	public static void main(String[] args) {
		ByteBuf byteBuf = new UnpooledByteBufAllocator(true).buffer();
		byteBuf.writeByte(-50);
		byteBuf.writeByte((byte)'a');
		byteBuf.writeByte((byte)'b');
		byteBuf.writeByte((byte)'c');
		byteBuf.writeByte((byte)'d');
//		byteBuf.writeChar('c');
//		byteBuf.writeChar('d');
//		byteBuf.writeChar('b');
		/*while(byteBuf.writerIndex()>byteBuf.readerIndex()){
			System.out.println(byteBuf.readChar());
		}*/
		System.out.println(byteBuf.readByte());
//		System.out.println(byteBuf.indexOf(0, 1, (byte)'a'));
//		System.out.println(byteBuf.indexOf(1, 2, (byte)'b'));
//		System.out.println(byteBuf.indexOf(0, byteBuf.readableBytes(), (byte)'c'));
//		System.out.println(byteBuf.indexOf(0, byteBuf.readableBytes(), (byte)'d'));
//		System.out.println(byteBuf.writerIndex());
//		System.out.println(byteBuf.readerIndex());
	}
	
}
