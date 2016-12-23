package lee.study.bean;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * RFC2616
 * @author Administrator
 *
 */
public class HttpBaseBean {
	private String proto;
	private Map<String,String> heads;
	private String body;
	
	public HttpBaseBean() {
		heads = new LinkedHashMap<String, String>();
	}
	public String getProto() {
		return proto;
	}
	public void setProto(String proto) {
		this.proto = proto;
	}
	public Map<String, String> getHeads() {
		return heads;
	}
	public void setHeads(Map<String, String> heads) {
		this.heads = heads;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
}
