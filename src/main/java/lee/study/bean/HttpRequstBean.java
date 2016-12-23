package lee.study.bean;

public class HttpRequstBean extends HttpBaseBean{
	private String method;
	private String url;

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
}
