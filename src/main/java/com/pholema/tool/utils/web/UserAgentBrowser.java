package com.pholema.tool.utils.web;

public class UserAgentBrowser {
	public UserAgentBrowser(){}
	public UserAgentBrowser(String browser,String version,String os){
		this.browser=browser;
		this.version=version;
		this.os=os;
	}
	
	public String browser;
	public String version;
	public String os;
	@Override
	public String toString() {
		return "UserAgentBrowser [browser=" + browser + ", version=" + version
				+ ", os=" + os + "]";
	}
}
