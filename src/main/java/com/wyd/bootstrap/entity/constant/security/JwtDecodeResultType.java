package com.wyd.bootstrap.entity.constant.security;

public enum JwtDecodeResultType {
	SUCCESS(0,"成功"),
	TIMEOUT(1,"超时"),
	FAIL(2,"失败");
	private int type;
	private String desc;
	private JwtDecodeResultType(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
