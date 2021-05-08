package com.pholema.tool.utils.log;

public class LogEntity {

	private String logDate;
	private String logType;
	private String logContent;
	private String logApp;
	private Integer jobCounter;
	private String executionTime;
	private Boolean isSuccess;

	public String getLogDate() {
		return logDate;
	}

	public void setLogDate(String logDate) {
		this.logDate = logDate;
	}

	public String getLogType() {
		return logType;
	}

	public void setLogType(String logType) {
		this.logType = logType;
	}

	public String getLogContent() {
		return logContent;
	}

	public void setLogContent(String logContent) {
		this.logContent = logContent;
	}

	public String getLogApp() {
		return logApp;
	}

	public void setLogApp(String logApp) {
		this.logApp = logApp;
	}

	public Integer getJobCounter() {
		return jobCounter;
	}

	public void setJobCounter(Integer jobCounter) {
		this.jobCounter = jobCounter;
	}

	public String getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(String executionTime) {
		this.executionTime = executionTime;
	}

	public Boolean getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
}
