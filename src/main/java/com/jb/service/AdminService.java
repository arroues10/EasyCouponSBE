package com.jb.service;

import java.sql.Date;
import java.util.List;

import com.jb.entity.Company;
import com.jb.entity.Coupon;
import com.jb.entity.Customer;
import com.jb.rest.ex.CompanyAlreadyExistsException;
import com.jb.rest.ex.CustomerAlreadyExistsException;
import com.jb.rest.ex.InvalidPriceException;
import com.jb.rest.ex.NoSuchMemberException;
import com.jb.rest.ex.NonExistingCategoryException;

public interface AdminService extends Service {

	List<Company> getAllCompanies();

	List<Customer> getAllCustomers();

	List<Coupon> getAllCoupons();

	Company createCompany(Company company) throws CompanyAlreadyExistsException;

	Customer createCustomer(Customer customer) throws CustomerAlreadyExistsException;

	void removeCompany(long id) throws NoSuchMemberException;

	void removeCustomer(long id) throws NoSuchMemberException;

	List<Coupon> getAllCouponsByCategory(int category) throws NonExistingCategoryException;

	List<Coupon> getAllCouponsBeforeEndDate(Date endDate);

	List<Coupon> getAllCouponsBelowPrice(double price) throws InvalidPriceException;
}
