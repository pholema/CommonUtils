package com.pholema.tool.utils.dynamic;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.pholema.tool.utils.SystemUtil;
import com.pholema.tool.utils.common.StringUtils;

import java.io.*;

public class PropertiesUtils {

	private static Logger logger;

	private static String propertiesFile;
	public static CustomizeProperties properties = null; // for list order

	public static void init() throws ParameterException {
		propertiesFile = System.getProperty("properties.file");
		System.out.println("PropertiesUtils init!!");
		System.out.println("propertiesFile=" + propertiesFile);
		properties = new CustomizeProperties();
		try {
			properties.load(new FileInputStream(propertiesFile));
			// adapt system.properties
			if (PropertiesUtils.properties.keySet().contains("app.home") && // register system if set up in .properties
					!PropertiesUtils.properties.getProperty("app.home").isEmpty() && !PropertiesUtils.properties.getProperty("app.home").equals("${app.home}")) {
				System.setProperty("app.home", PropertiesUtils.properties.getProperty("app.home"));
			}
			for (Object key : PropertiesUtils.properties.keySet()) {
				String value = PropertiesUtils.properties.getProperty(key.toString());
				if (systemPropertiesInvolved(value)) {
					value = swiftSystemProps(value);
					PropertiesUtils.properties.setProperty(key.toString(), value);
				}
				System.setProperty(key.toString(), value);
			}
			System.out.println("**custom.log4j.output="+System.getProperty("custom.log4j.output"));
			System.out.println("**log4j.configuration="+System.getProperty("log4j.configuration"));
			
			PropertyConfigurator.configure(PropertiesUtils.properties.getProperty("log4j.configuration"));
			logger = Logger.getLogger(PropertiesUtils.class);
			//
			System.setProperty("app.name", SystemUtil.getAppName());

			logger.info("PropertiesUtils init!!");
			logger.info("propertiesFile=" + propertiesFile);
			logger.info("java.runtime.name:" + System.getProperty("java.runtime.name"));
			logger.info("java.runtime.version:" + System.getProperty("java.runtime.version"));
			logger.info("java.vm.vendor:" + System.getProperty("java.vm.vendor"));
			logger.info("java.vm.name:" + System.getProperty("java.vm.name"));
			logger.info("java.vm.version:" + System.getProperty("java.vm.version"));
			logger.info("================ System property info ================");
			logger.info("|                                                    |");
			for (Object key : PropertiesUtils.properties.keySet()) {
				String value = PropertiesUtils.properties.getProperty(key.toString());
				if (key.toString().toLowerCase().contains("password")) {
					value = "***************";
				}
				logger.info("|  " + key.toString() + ":" + value);
				if (StringUtils.isEmpty(value)) {
					throw new ParameterException(key.toString() + " not set");
				}
			}
			logger.info("|                                                    |");
			logger.info("================ System property info ================");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static boolean systemPropertiesInvolved(String value) {
		return value.contains("${") && value.contains("}");
	}

	public static String swiftSystemProps(String value) {
		do {
			String system = getSystemPropertiesInvolved(value);
			if (system != null) {
				if (System.getProperty(system) != null) {
					value = value.replace("${" + system + "}", System.getProperty(system));
				} else {
					value = value.replace("${" + system + "}", "null");
				}
			}
		} while (systemPropertiesInvolved(value));
		return value;
	}

	public static String getSystemPropertiesInvolved(String value) {
		if (value.contains("${")) {
			int start = value.indexOf("${");
			if (value.substring(start).contains("}")) {
				int end = value.substring(start).indexOf("}");
				return value.substring(start + 2, start + end);
			}
		}
		return null;
	}

	public static void writeProperties(String keyname, String keyvalue) throws IOException {
		String propertiesFile = System.getProperty("properties.file");
		System.out.println("propertiesFile=" + propertiesFile);
		//
		File file = new File(propertiesFile);// 文件路径
		FileReader fileReader = new FileReader(file);
		LineNumberReader reader = new LineNumberReader(fileReader);
		StringBuilder builder = new StringBuilder();
		String txt = "";
		while (txt != null) {
			txt = reader.readLine();
			if (txt != null && txt.startsWith(keyname)) {
				txt = keyname + "=" + keyvalue;
			}
			// logger.info(txt == null ? "null" : txt);
			if (txt != null) {
				builder.append(txt);
				builder.append(System.lineSeparator());
			}
		}
		reader.close();
		fileReader.close();
		//
		boolean executed = false;
		try {
			OutputStream fos = new FileOutputStream(propertiesFile);
			Writer writer = new OutputStreamWriter(fos, "ISO-8859-1");
			writer.write(builder.toString());
			writer.flush();
			fos.close();
			executed = true;
		} catch (Exception e) {
			logger.info("error on writer ", e);
		}

		if (executed) {
			PropertiesUtils.properties.setProperty(keyname, keyvalue);
			logger.info(keyname + " updated.");
		}
	}
}
