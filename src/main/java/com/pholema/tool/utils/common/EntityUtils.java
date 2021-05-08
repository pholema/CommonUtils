package com.pholema.tool.utils.common;

import java.lang.reflect.Method;

public class EntityUtils {
	// obj's data default 0 (double and int)
	public static <M> void objInitZero(Class<M> mClass, Object obj) {
		Method[] method = mClass.getMethods(); // include all methods
		M entity = (M) obj;
		if (entity != null) {
			for (int i = 0; i < method.length; i++) {
				String methodName = method[i].getName();
				if (methodName.startsWith("set")) {
					try {
						String pmtType = method[i].getParameterTypes()[0].toString().toLowerCase();
						if (pmtType.contains("double")) {
							method[i].invoke(entity, 0.0);
						} else if (pmtType.contains("integer") || pmtType.contains("int")) {
							method[i].invoke(entity, 0);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	
	public static <M> void invokeSetting(Class<M> mClass, Object obj, String key, Object value) {
		Method[] method = mClass.getMethods(); // include all methods
		M entity = (M) obj;
		if (entity != null) {
			for (int i = 0; i < method.length; i++) {
				String methodName = method[i].getName();
				if (methodName.startsWith("set")) {
					try {
						if(methodName.toLowerCase().replace("set", "").equals(key.toLowerCase())){
							method[i].invoke(entity, value);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public static Double toDouble(Object obj) {
		if (obj == null || obj.toString().isEmpty()) {
			return 0.0;
		} else {
			return Double.parseDouble(obj.toString());
		}
	}

	public static int toInt(Double obj) {
		return obj.intValue();
	}

	public static int toInt(Object obj) {
		if (obj == null || obj.toString().isEmpty()) {
			return 0;
		} else {
			return Integer.parseInt(obj.toString());
		}
	}

	public static String toString(Object obj) {
		if (obj == null) {
			return "";
		} else {
			return obj.toString();
		}
	}
}
