package lee.study.bean;

/**
 * 	 RFC 6455
 * 	 Base Framing Protocol
 *    0                   1                   2                   3
      0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
     +-+-+-+-+-------+-+-------------+-------------------------------+
     |F|R|R|R| opcode|M| Payload len |    Extended payload length    |
     |I|S|S|S|  (4)  |A|     (7)     |             (16/64)           |
     |N|V|V|V|       |S|             |   (if payload len==126/127)   |
     | |1|2|3|       |K|             |                               |
     +-+-+-+-+-------+-+-------------+ - - - - - - - - - - - - - - - +
     |     Extended payload length continued, if payload len == 127  |
     + - - - - - - - - - - - - - - - +-------------------------------+
     |                               |Masking-key, if MASK set to 1  |
     +-------------------------------+-------------------------------+
     | Masking-key (continued)       |          Payload Data         |
     +-------------------------------- - - - - - - - - - - - - - - - +
     :                     Payload Data continued ...                :
     + - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - +
     |                     Payload Data continued ...                |
     +---------------------------------------------------------------+
 * @author Administrator
 *
 */
public class WebSocketFrameBean {
	private byte FIN;
	private byte RSV1;
	private byte RSV2;
	private byte RSV3;
	/*
	  *  %x0 denotes a continuation frame
      *  %x1 denotes a text frame
      *  %x2 denotes a binary frame
      *  %x3-7 are reserved for further non-control frames
      *  %x8 denotes a connection close
      *  %x9 denotes a ping
      *  %xA denotes a pong
      *  %xB-F are reserved for further control frames
      *  (4个字节)
      *  0:接上一帧 1:文本帧 2:二进制帧 3-7:保留待使用 8:关闭连接 9:ping帧 10:pong帧 B-F:保留待使用
	 */
	private byte opcode;
	/*
	 * 是否要做掩码换算
	 * 1:是 0:否
	 */
	private byte MASK;
	
	/*
	 *传输的数据
	 * 数据的长度(7 bits, 7+16 bits, or 7+64 bits)
	 * 7+16 bits:payloadLen==126
	 * 7+64 bits:payloadLen==127
	 */
	private byte[] payloadData;
	
	public byte[] getPayloadData() {
		return payloadData;
	}

	public void setPayloadData(byte[] payloadData) {
		this.payloadData = payloadData;
	}

	public byte getFIN() {
		return FIN;
	}

	public void setFIN(byte fIN) {
		FIN = fIN;
	}

	public byte getRSV1() {
		return RSV1;
	}

	public void setRSV1(byte rSV1) {
		RSV1 = rSV1;
	}

	public byte getRSV2() {
		return RSV2;
	}

	public void setRSV2(byte rSV2) {
		RSV2 = rSV2;
	}

	public byte getRSV3() {
		return RSV3;
	}

	public void setRSV3(byte rSV3) {
		RSV3 = rSV3;
	}

	public byte getOpcode() {
		return opcode;
	}

	public void setOpcode(byte opcode) {
		this.opcode = opcode;
	}

	public byte getMASK() {
		return MASK;
	}

	public void setMASK(byte mASK) {
		MASK = mASK;
	}

	public static void main(String[] args) {
		System.out.println(Integer.toBinaryString(0xFF&(1<<7)));
	}
	
	public WebSocketFrameBean() {
		
	}

	public WebSocketFrameBean(byte fIN, byte rSV1, byte rSV2, byte rSV3,
			byte opcode, byte mASK, byte[] payloadData) {
		FIN = fIN;
		RSV1 = rSV1;
		RSV2 = rSV2;
		RSV3 = rSV3;
		this.opcode = opcode;
		MASK = mASK;
		this.payloadData = payloadData;
	}
	
}
