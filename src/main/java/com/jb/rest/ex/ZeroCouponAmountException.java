package com.jb.rest.ex;

@SuppressWarnings("serial")
public class ZeroCouponAmountException extends Exception {
	public ZeroCouponAmountException(String message) {
		super(message);
	}

}
