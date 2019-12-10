package com.jb.service;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jb.entity.Coupon;
import com.jb.entity.Customer;
import com.jb.repository.CouponRepository;
import com.jb.repository.CustomerRepository;
import com.jb.rest.ex.CouponAlreadyPurchasedException;
import com.jb.rest.ex.InvalidPriceException;
import com.jb.rest.ex.NonExistingCategoryException;
import com.jb.rest.ex.ZeroCouponAmountException;

/**
 * In this class I create all the functions that will be used later by the
 * customer controller
 * 
 * @author Solal Arroues
 *
 */
@Service
public class CustomerServiceImpl implements CustomerService {

	// Fields
	private long customerId;

	private CustomerRepository customerRepository;
	private CouponRepository couponRepository;

	// Constructor
	@Autowired
	public CustomerServiceImpl(CustomerRepository customerRepository, CouponRepository couponRepository) {
		this.customerRepository = customerRepository;
		this.couponRepository = couponRepository;
	}

	// Is use in loginSystem.customerLogin()
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	/**
	 * This function uses the existing findById function in JpaRepository to
	 * retrieve customer data
	 * 
	 * @return Customer
	 */
	@Override
	public Customer getCustomer() {
		return customerRepository.findById(customerId).orElse(null);
	}

	/**
	 * This function uses the existing save function in JpaRepository to modify
	 * customer data
	 * 
	 * @param Customer
	 * @return Customer
	 */
	@Override
	public Customer updateCustomer(Customer customer) {
		customer.setId(customerId);
		return customerRepository.save(customer);
	}

	/**
	 * This function uses the findAllByCustomerId function that I created in coupon
	 * repository to retrieve all customer coupons
	 * 
	 * @return List<Coupon>
	 */
	@Override
	public List<Coupon> getAllCustomerCoupons() {
		return couponRepository.findAllByCustomerId(customerId);
	}

	/**
	 * This function uses the function findAllByCustomerIdAndCategory that I created
	 * in coupon repository to retrieve all customer's coupons by category
	 * 
	 * @throws NonExistingCategoryException : if the category is not valid
	 * @param category
	 * @return List<Coupon>
	 */
	@Override
	public List<Coupon> getAllCustomerCouponsByCatergory(int category) throws NonExistingCategoryException {
		if (category > 8 || category < 1) {
			throw new NonExistingCategoryException(String.format("This category : %d, is not exists", category));
		}
		return couponRepository.findAllByCustomerIdAndCategory(customerId, category);
	}

	/**
	 * This function uses the function findAllByCustomerIdAndBeforeEndDate that I
	 * created in coupon repository to retrieve all the coupons of the customer
	 * whose date has not yet expired
	 * 
	 * @param endDate
	 * @return List<Coupon>
	 */
	@Override
	public List<Coupon> getAllCustomerCouponsBeforeEndDate(Date endDate) {
		return couponRepository.findAllByCustomerIdAndBeforeEndDate(customerId, endDate);
	}

	/**
	 * This function uses the function findAllByCustomerIdAndBelowPrice that I
	 * created in coupon repository to retrieve all the coupons of the customer
	 * whose price is lower than the price in parameter
	 * 
	 * @param price
	 * @return List<Coupon>
	 */
	@Override
	public List<Coupon> getAllCustomerCouponsBelowPrice(double price) throws InvalidPriceException {
		if (price <= 0) {
			throw new InvalidPriceException("Invalid price : " + price);
		}
		return couponRepository.findAllByCustomerIdAndBelowPrice(customerId, price);
	}

	/**
	 * This function uses the 2 private functions addCoupon and
	 * decrementAmountCoupon in order to make a purchase by adding the coupon to the
	 * customer and remove a unit from the basic quantity
	 * 
	 * @throws CouponAlreadyPurchasedException : if the coupon has already been
	 *                                         purchased by the customer
	 * @throws ZeroCouponAmountException       : if there is no more coupon
	 *                                         available
	 * @param couponId
	 * @return Coupon
	 */
	@Override
	public Coupon purchaseCoupon(long couponId) throws CouponAlreadyPurchasedException, ZeroCouponAmountException {
		if (couponAlreadyPurchased(couponId)) {
			throw new CouponAlreadyPurchasedException(
					String.format("This coupon : %s , is already purchased", couponId));
		}
		return addCoupon(decrementAmountCoupon(couponId));
	}

	/**
	 * This function is a private function that allows me to check if the coupon has
	 * already been purchased by the customer
	 * 
	 * @param couponId
	 * @return True if already purchased and false otherwise
	 */
	private boolean couponAlreadyPurchased(long couponId) {
		for (Coupon c : getAllCustomerCoupons()) {
			return c.getId() == couponId;
		}
		return false;
	}

	/**
	 * This function is a private function that uses the existing save function in
	 * JpaRepository to add a coupon to the customer and save the transaction
	 * 
	 * @param coupon
	 * @return Coupon
	 */
	private Coupon addCoupon(Coupon coupon) {
		Customer customer = getCustomer();

		customer.add(coupon);
		customerRepository.save(customer);

		return coupon;
	}

	/**
	 * This function is a function that allows me to decrement a coupon and uses the
	 * save function to save the update
	 * 
	 * @param id
	 * @return Coupon
	 * @throws ZeroCouponAmountException : if there is no more coupon available
	 */
	private Coupon decrementAmountCoupon(long id) throws ZeroCouponAmountException {
		Coupon coupon = couponRepository.findById(id).orElse(null);

		if (coupon.getAmount() == 0) {
			throw new ZeroCouponAmountException("There is no coupon left to buy");
		}
		coupon.setAmount(coupon.getAmount() - 1);
		couponRepository.save(coupon);

		return coupon;
	}
}
