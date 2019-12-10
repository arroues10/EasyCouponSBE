package com.jb.service;

import java.sql.Date;
import java.util.List;

import com.jb.entity.Coupon;
import com.jb.entity.Customer;
import com.jb.rest.ex.CouponAlreadyPurchasedException;
import com.jb.rest.ex.InvalidPriceException;
import com.jb.rest.ex.NonExistingCategoryException;
import com.jb.rest.ex.ZeroCouponAmountException;

public interface CustomerService extends Service {

	Customer getCustomer();

	Customer updateCustomer(Customer customer);

	List<Coupon> getAllCustomerCoupons();

	Coupon purchaseCoupon(long id) throws CouponAlreadyPurchasedException, ZeroCouponAmountException;

	List<Coupon> getAllCustomerCouponsByCatergory(int category) throws NonExistingCategoryException;

	List<Coupon> getAllCustomerCouponsBeforeEndDate(Date endDate);

	List<Coupon> getAllCustomerCouponsBelowPrice(double price) throws InvalidPriceException;
}
