package com.jb.rest.ex;

@SuppressWarnings("serial")
public class CouponAlreadyPurchasedException extends Exception {
	public CouponAlreadyPurchasedException(String message) {
		super(message);
	}

}
