package zhongchiedu.log.pojo;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import zhongchiedu.framework.pojo.GeneralBean;
import zhongchiedu.general.pojo.User;
@Document
public class Log extends GeneralBean<Log>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -574653567328003244L;
	
	private String method;
	private String type;
	private String requestIp;
	private String exceptionCode;
	private String exceptionDetail;
	private String params;
	private String createby;
	private String userId;
	@DBRef  
	private User user;
	@DBRef
	private List<Log> logs;
	
	
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getRequestIp() {
		return requestIp;
	}
	public void setRequestIp(String requestIp) {
		this.requestIp = requestIp;
	}
	public String getExceptionCode() {
		return exceptionCode;
	}
	public void setExceptionCode(String exceptionCode) {
		this.exceptionCode = exceptionCode;
	}
	public String getExceptionDetail() {
		return exceptionDetail;
	}
	public void setExceptionDetail(String exceptionDetail) {
		this.exceptionDetail = exceptionDetail;
	}
	public String getParams() {
		return params;
	}
	public void setParams(String params) {
		this.params = params;
	}
	public String getCreateby() {
		return createby;
	}
	public void setCreateby(String createby) {
		this.createby = createby;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		return "Log [method=" + method + ", type=" + type + ", requestIp=" + requestIp
				+ ", exceptionCode=" + exceptionCode + ", exceptionDetail=" + exceptionDetail + ", params=" + params
				+ ", createby=" + createby + "]";
	}
	
	

}
