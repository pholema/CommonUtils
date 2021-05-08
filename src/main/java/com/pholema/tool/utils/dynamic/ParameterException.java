package com.pholema.tool.utils.dynamic;

public class ParameterException extends Exception {
	private static final long serialVersionUID = -2316423359675720494L;

	public ParameterException(String message) {
		super(message);
	}

	public ParameterException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
