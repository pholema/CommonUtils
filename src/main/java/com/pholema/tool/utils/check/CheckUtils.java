package com.pholema.tool.utils.check;


/*
factor 1: current event count
factor 2: duration since pervious process
factor 3: is current query running
if event count>200 then factor1=true
if event duration>=5 then factor2=true
if currnt running=true then factor3=true
if ((factor1=true or factor2=true) and factor3=false) do your query
4s * (5/3)
*/
public class CheckUtils {
	private CheckCounter checkCounter= new CheckCounter();
	private CheckTimer checkTimer= new CheckTimer();
	private CheckRunning checkRunning= new CheckRunning();
	synchronized public boolean check(){
		boolean factor1 = checkCounter.check();
		boolean factor2 = checkTimer.check();
		boolean factor3 = checkRunning.check();
		//System.out.println(factor1+","+factor2+","+factor3);
		if(( factor1 || factor2) && !factor3){
			checkRunning.lock();
			checkTimer.reset();
			checkCounter.reset();
			return true;
		}else{
			return false;
		}
	}
	
	public void reset() {
		checkRunning.reset();
	}

	public void skip() {
		checkRunning.reset();
		checkTimer.skip();
	}

	public CheckCounter getCheckCounter() {
		return checkCounter;
	}

	public void setCheckCounter(CheckCounter checkCounter) {
		this.checkCounter = checkCounter;
	}

	public CheckTimer getCheckTimer() {
		return checkTimer;
	}

	public void setCheckTimer(CheckTimer checkTimer) {
		this.checkTimer = checkTimer;
	}

	public CheckRunning getCheckRunning() {
		return checkRunning;
	}

	public void setCheckRunning(CheckRunning checkRunning) {
		this.checkRunning = checkRunning;
	}
	
	
}


