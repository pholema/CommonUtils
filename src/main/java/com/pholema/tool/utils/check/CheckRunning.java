package com.pholema.tool.utils.check;

public class CheckRunning extends CheckFactor {
	private Boolean running = false ;
	@Override
	public boolean check() {
		if(running){
			return true;
		}else{
			return false;
		}
	}
	
	public void lock() {
		running = true;
	}
	
	public void reset() {
		running = false;
	}

	public Boolean getRunning() {
		return running;
	}

	public void setRunning(Boolean running) {
		this.running = running;
	}
	
}
