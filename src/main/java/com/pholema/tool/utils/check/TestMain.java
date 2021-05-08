package com.pholema.tool.utils.check;

import java.util.HashSet;
import java.util.Set;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class TestMain  {
	
	//debug
	public static void main(String[] args) throws Exception {
		java.util.Timer timer = new java.util.Timer();
		timer.schedule(new DebugTask(), 1000 ,100); //每0.1秒跑一次		
	}
	
}

//debug
class DebugTask extends TimerTask {

	private static Set<Future<Object>> outset = new HashSet<Future<Object>>() ;
	private static ExecutorService execSvc = Executors.newFixedThreadPool(10);
	
	private static CheckUtils checkUtils = new CheckUtils();
	
	@Override
	public void run() {
		outset.add(execSvc.submit(new Callable<Object>() {
			@Override public String call() throws Exception {
				doCheck();
				return "finish.";
			}
		}));
		
	}
	public void doCheck() {
		if(checkUtils.check()){
			System.out.println("do query...");
			try {
				Thread.sleep(3500);
				//Thread.sleep(500);
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			checkUtils.reset();
			System.out.println("query done.");
		}else{
			System.out.println("pass.");
		}
	}
	
}

