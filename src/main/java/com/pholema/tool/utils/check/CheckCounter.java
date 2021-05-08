package com.pholema.tool.utils.check;

import java.util.concurrent.atomic.AtomicInteger;

public class CheckCounter extends CheckFactor {
	private AtomicInteger counter = new AtomicInteger(0);
	private static Integer limit ;
	static{
		limit = System.getProperty("check.counter.limit")==null?20:Integer.parseInt(System.getProperty("check.counter.limit"));
	}
	
	@Override
	public boolean check() {
		//System.out.println("counter:"+counter);
		if(counter.incrementAndGet()+1>limit){
			return true;
		}else{
			return false;
		}
	}
	
	public void reset() {
		counter = new AtomicInteger(0);
	}

	public AtomicInteger getCounter() {
		return counter;
	}

	public void setCounter(AtomicInteger counter) {
		this.counter = counter;
	}

	public static Integer getLimit() {
		return limit;
	}

	public static void setLimit(Integer limit) {
		CheckCounter.limit = limit;
	}
	
}
