package com.wyd.bootstrap.controller.vo;

public class ResponseVO <T>{
	private String status;
	private String message;
	private T value;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public T getValue() {
		return value;
	}
	public void setValue(T value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return "Response [status=" + status + ", message=" + message + ", value=" + value + "]";
	}
	
}
