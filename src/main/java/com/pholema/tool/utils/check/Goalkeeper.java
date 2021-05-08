package com.pholema.tool.utils.check;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pholema.tool.utils.mail.MailUtils;

/*-------------------------------------------
 * Class:		Goalkeeper (clone from SORealtimeClient.scala)
 * Created by:   Wilson
 * Created date: 2017-04-10
 * Description:  
 * This class is to prevent DDOS attacks
 * hit function will be triggered for each user request 
 * it returns validate result
 * output data type is Boolean
---------------------------------------------*/
	
public class Goalkeeper {
	private static final Logger logger = LoggerFactory.getLogger(Goalkeeper.class);

	private int total_counter=0;
	private int partial_counter=0;
	private Long partial_timer=System.nanoTime();
	private Long mail_timer=System.nanoTime();
	private boolean mailSentFlag = false;
	private int cycle=30;
	private int maxFrequency=60;
	private int mailCycle=180;
	SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
	private String mailSubject="(Ignite RLT Alert)Found suspicious or malicious request";

	
	public Goalkeeper(int cycle,int maxFrequency,int mailCycle){
		setCycle(cycle);
		setMaxFrequency(maxFrequency);
		setMailCycle(mailCycle);
	}
	
	synchronized public boolean hit() {
    	this.total_counter+=1;
		if(((System.nanoTime()-this.partial_timer)/1000000000.0)>=this.cycle){
			this.partial_counter=0;
			this.partial_timer=System.nanoTime();
		}else{
			this.partial_counter+=1;
		}
		if(((System.nanoTime()-this.mail_timer)/1000000000.0)>=this.mailCycle){
			this.mailSentFlag=false;
			this.mail_timer=System.nanoTime();
		}
		return this.validate();
    }

    public void setMailSentFlag(boolean mailSentFlag) {
    	this.mailSentFlag=mailSentFlag;
    }
    
    public boolean getMailSentFlag() {
    	return this.mailSentFlag;
    }
    
    public String print() {
    	return "cycle(in second):="+this.cycle+"\n"+ 
    			"max frequency="+this.maxFrequency+"\n"+ 
    			"total_counter="+this.total_counter+"\n"+ 
    			"partial_counter="+this.partial_counter+"\n"+ 
    			"partial_timer="+this.partial_timer+"\n"+ 
    			"validate="+this.validate();
    }
    
    
    public String content(String ip) {
    	String now = fmt.format(new Date());
    	String rtn = "Time: "+now+"\r\n"+
    			"Event: Found suspicious or malicious request which rated high risk to realtime service.\n"+
    			"User IP Address: "+ip+" \r\n"+ 
    			"Description: This event has potential risk to realtime service, please help contact this user \r\n"+ 
    			"		rules: "+this.maxFrequency+" times in "+this.cycle+" seconds \r\n"+ 
    			"		total counter="+this.total_counter+"\r\n"+ 
    			"		partial counter="+this.partial_counter+"\r\n";
    	logger.info(rtn);
    	return rtn;
    }
    
    public void reset() {
    	this.total_counter=0;
		this.partial_counter=0;
		this.partial_timer=System.nanoTime();
    }
    
    public boolean validate() {
    	return this.partial_counter<this.maxFrequency;
    }
    
    public void sendAlertMail(String ip) {
    	logger.info("sendAlertMail start");
    	if(!getMailSentFlag()){
    		setMailSentFlag(true);
    		String mailTo = null;
    		String mailCc = null;
    		String mailBcc = null;

			MailUtils.sendMessage(MailUtils.ADMIN_MAIL, mailCc, mailBcc, MailUtils.SYSTEM_MAIL, getMailSubject(), content(ip));
        	logger.info("alert mail has been sent successfully");
    	}
    }

    
    /* 檢查request次數會不會太頻繁(server不檢查)
     * return true:合法的request
     * return false:request太頻繁,後面break
     * 
    */
    public static boolean requestCheck(String myIp, ConcurrentHashMap<String, Goalkeeper> goalkeepers, Goalkeeper initGoalkeeper,List<String> exclude_ip){
    	boolean rtn = true;
    	try{
    		if(exclude_ip==null || !exclude_ip.contains(myIp)){ //server不檢查
    			if(goalkeepers.get(myIp)!=null) initGoalkeeper = goalkeepers.get(myIp);
            	rtn = initGoalkeeper.hit();
            	
            	//send alert mail
            	if(!rtn) initGoalkeeper.sendAlertMail(myIp);
            	
            	goalkeepers.put(myIp, initGoalkeeper);
    		}
    	}catch(Exception e){
    		logger.error("requestCheck error:"+e.getMessage());
    	}
    	
    	return rtn;
    }
    
	public int getCycle() {
		return cycle;
	}

	public void setCycle(int cycle) {
		this.cycle = cycle;
	}

	public int getMaxFrequency() {
		return maxFrequency;
	}

	public void setMaxFrequency(int maxFrequency) {
		this.maxFrequency = maxFrequency;
	}

	public int getMailCycle() {
		return mailCycle;
	}

	public void setMailCycle(int mailCycle) {
		this.mailCycle = mailCycle;
	}

	public String getMailSubject() {
		return mailSubject;
	}

	public void setMailSubject(String mailSubject) {
		this.mailSubject = mailSubject;
	}

    
}
