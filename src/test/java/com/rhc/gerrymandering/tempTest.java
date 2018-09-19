package com.rhc.gerrymandering;

import org.junit.Test;

public class tempTest {
	
	@Test
	public void test(){
		Double one = new Double(1);
		Double two = new Double(0);
		Double three = new Double(3);
		
		Double answer = one/two;
		
		System.out.println(answer);
		
		System.out.println(answer - three/two);
		System.out.println(answer.longValue());
		System.out.println(Double.valueOf(Double.NaN).longValue());
		
		System.out.println(answer.equals(Double.NaN));
		
		System.out.println(answer == Double.valueOf(Double.NaN));
		
		System.out.println(0 - Double.NaN);
		
		double d = answer.doubleValue();
		
		System.out.println(Double.NaN == d);
		
		double a = 0;
		double b = 0;
		double c = a/b;
		
		System.out.println(c);
		System.out.println(Double.isNaN(c));
	}

}
