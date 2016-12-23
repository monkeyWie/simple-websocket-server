package lee.study.bean;

public class HttpResponseBean extends HttpBaseBean{
	private int status;
	private String statusMsg;
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getStatusMsg() {
		return statusMsg;
	}
	public void setStatusMsg(String statusMsg) {
		this.statusMsg = statusMsg;
	}
	
}
