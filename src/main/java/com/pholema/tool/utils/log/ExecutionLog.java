package com.pholema.tool.utils.log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.log4j.Logger;

import com.pholema.tool.utils.common.DateUtils;
import com.pholema.tool.utils.common.StringUtils;
import com.pholema.tool.utils.dynamic.PropertiesUtils;

public class ExecutionLog {

	private Logger logger = Logger.getLogger(ExecutionLog.class);
	private String logHome;
	private PrintWriter writer;
	private NumberFormat fmt = new DecimalFormat("#0.00000");

	// static{
	// try {
	// ExecutionLog.logHome = System.getProperty("app.home", PropertiesUtils.properties.getProperty("app.home")) + "/log";
	// } catch (Exception e) {
	// logger.warn("app.home is empty.");
	// }
	// }

	public void init(String logHome) {
		this.logHome = logHome;
	}

	private String getFilePath(String type, Date date) {
		return logHome + "/" + type + DateUtils.toDateStrYYYYMMDD(date) + ".json";
	}

	private synchronized void saveToFile(LogEntity containerLog, String type) {
		File file = new File(getFilePath(type, new Date()));
		try {
			if (!file.exists()) {
				logger.info("createNewFile:" + file);
				file.createNewFile();
				removeHistoryFile(type);
			}
			writer = new PrintWriter(new FileOutputStream(file, true));
			String result = StringUtils.toGson(containerLog) + "\r\n";
			writer.write(result);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			logger.error("save file error:" + e.getMessage());
			e.printStackTrace();
		}
	}

	private void removeHistoryFile(String type) {
		File file = new File(getFilePath(type, DateUtils.getDateStart(-7)));
		logger.info("filePath_old:" + file);
		if (file.exists()) {
			if (file.delete()) {
				logger.info("old file is deleted:" + file);
			} else {
				logger.error("old file is failed:" + file);
			}
		}
	}

	public List<LogEntity> readLogFile(String type) throws IOException {
		List<LogEntity> logList = new ArrayList<>();
		File file = new File(getFilePath(type, new Date()));
		logger.info("filePath:" + file);

		BufferedReader reader = new BufferedReader(new FileReader(file.getAbsolutePath()));
		String line;
		LogEntity containerLog;
		while ((line = reader.readLine()) != null) {
			containerLog = StringUtils.fromGson(line, LogEntity.class);
			logList.add(containerLog);
		}
		reader.close();
		return logList;
	}

	public void saveLog(long startTime, LogEntity log) {
		log.setLogDate(DateUtils.getDateTime(new Date(), "yyyy-MM-dd HH:mm:ss"));
		log.setExecutionTime(executionTime(startTime));
		saveToFile(log, log.getLogType());
	}

	public long saveLog(long startTime, String logApp, String logType, String logContent, boolean isSuccess) {
		LogEntity log = new LogEntity();
		log.setIsSuccess(isSuccess);
		log.setLogApp(logApp);
		log.setLogType(logType);
		log.setLogDate(DateUtils.getDateTime(new Date(), "yyyy-MM-dd HH:mm:ss"));
		log.setExecutionTime(executionTime(startTime));
		log.setLogContent(logContent);
		saveToFile(log, log.getLogType());
		return System.currentTimeMillis();
	}

	public String executionTime(long start) {
		return fmt.format((System.currentTimeMillis() - start) / 1000d);
	}

	// public LogEntity logEntityBuilder() {
	// LogEntity log = new LogEntity();
	// log.setIsSuccess(isSuccess);
	// log.setJobCounter(jobCounter);
	// log.setLogApp(logApp);
	// log.setLogContent(logContent);
	// log.setLogType(logType);
	// return log;
	// }
	public void printWriterLog(Exception e, Logger logger) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		logger.error(sw.toString());
	}
}
