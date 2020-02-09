package com.scriptexecutor.beans;

public class ScreenRegion {

	ScreenPoint a, b, c, d;
	
	public ScreenPoint getA() {
		return a;
	}

	public void setA(ScreenPoint a) {
		this.a = a;
	}

	public ScreenPoint getB() {
		return b;
	}

	public void setB(ScreenPoint b) {
		this.b = b;
	}

	public ScreenPoint getC() {
		return c;
	}

	public void setC(ScreenPoint c) {
		this.c = c;
	}

	public ScreenPoint getD() {
		return d;
	}

	public void setD(ScreenPoint d) {
		this.d = d;
	}

	public ScreenRegion() {
		a = b = c = d = null;
	}
	
	public ScreenRegion(ScreenPoint a, ScreenPoint b, ScreenPoint c, ScreenPoint d) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
	}
}
