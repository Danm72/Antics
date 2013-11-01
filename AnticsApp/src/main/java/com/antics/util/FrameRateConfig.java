package com.antics.util;

public class FrameRateConfig {
	public static long startTime;
	public static long endTime = System.currentTimeMillis();
	public static long dt = endTime - startTime;
}
