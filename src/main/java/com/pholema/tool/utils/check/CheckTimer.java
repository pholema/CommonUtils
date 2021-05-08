package com.pholema.tool.utils.check;

public class CheckTimer extends CheckFactor {
	private Long start;
	private static Long limit;

	static{
		limit = System.getProperty("check.timer.limit") == null ? (long) 30000 : Long.parseLong(System.getProperty("check.timer.limit"));
	}
	
	@Override
	public boolean check() {
		if (start == null) {
			start = System.currentTimeMillis();
			return true;
		} else {
			Long now = System.currentTimeMillis();
			return ((now - start) / 1d) > limit;
		}
	}

	public void skip() {
		start = null;
	}
	
	public void reset() {
		start = System.currentTimeMillis();
	}

	public static Long getLimit() {
		return limit;
	}

	public static void setLimit(Long limit) {
		CheckTimer.limit = limit;
	}
	
}
