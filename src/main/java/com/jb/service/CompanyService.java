package com.jb.service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import com.jb.entity.Company;
import com.jb.entity.Coupon;
import com.jb.rest.ex.InvalidPriceException;
import com.jb.rest.ex.NoSuchCouponException;
import com.jb.rest.ex.NonExistingCategoryException;

public interface CompanyService extends Service {

	Coupon createCoupon(Coupon coupon);

	Optional<Coupon> getCoupon(long id) throws NoSuchCouponException;

	List<Coupon> getAllCompanyCoupons();

	Coupon updateCoupon(Coupon coupon);

	void removeCoupon(long id) throws NoSuchCouponException;

	Company updateCompany(Company company);

	List<Coupon> getAllCompanyCouponsByCategory(int category) throws NonExistingCategoryException;

	List<Coupon> getAllCompanyCouponsBeforeEndDate(Date endDate);

	List<Coupon> getAllCompanyCouponsBelowPrice(double price) throws InvalidPriceException;
}
