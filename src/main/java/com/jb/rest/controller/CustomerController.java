package com.jb.rest.controller;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jb.entity.Coupon;
import com.jb.entity.Customer;
import com.jb.rest.ClientSession;
import com.jb.rest.ex.CouponAlreadyPurchasedException;
import com.jb.rest.ex.InvalidPriceException;
import com.jb.rest.ex.InvalidTokenException;
import com.jb.rest.ex.NonExistingCategoryException;
import com.jb.rest.ex.ZeroCouponAmountException;
import com.jb.service.CustomerService;

@RestController
@RequestMapping("/api")
public class CustomerController {

	private Map<String, ClientSession> tokensMap;

	@Autowired
	public CustomerController(@Qualifier("tokens") Map<String, ClientSession> tokensMap) {
		this.tokensMap = tokensMap;
	}

	private ClientSession getSession(String token) {
		return tokensMap.get(token);
	}

	/**
	 * This function allows us to retrieve customer data
	 * 
	 * @param token
	 * @return ResponseEntity<Customer>
	 * @throws InvalidTokenException
	 */
	@GetMapping("/customers/{token}")
	public ResponseEntity<Customer> getCustomer(@PathVariable String token) throws InvalidTokenException {
		ClientSession session = getSession(token);
		if (session == null) {
			throw new InvalidTokenException("Invalid token");
		}
		CustomerService service = (CustomerService) session.getService();
		return ResponseEntity.ok(service.getCustomer());
	}

	/**
	 * This feature allows us to recover all customer coupons
	 * 
	 * @param token
	 * @return ResponseEntity<List<Coupon>>
	 * @throws InvalidTokenException
	 */
	@GetMapping("/customers/coupons/{token}")
	public ResponseEntity<List<Coupon>> getAllCustomerCoupons(@PathVariable String token) throws InvalidTokenException {
		ClientSession session = getSession(token);
		if (session == null) {
			throw new InvalidTokenException("Invalid token");
		}
		CustomerService service = (CustomerService) session.getService();
		final List<Coupon> allCustomerCoupons = service.getAllCustomerCoupons();

		if (allCustomerCoupons.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(allCustomerCoupons);
	}

	/**
	 * This feature allows us to recover all customer coupons by category
	 * 
	 * @param token
	 * @param category
	 * @return ResponseEntity<List<Coupon>>
	 * @throws InvalidTokenException
	 * @throws NonExistingCategoryException
	 */
	@GetMapping("/customers/couponsByCategory/{token}")
	public ResponseEntity<List<Coupon>> getAllCustomerCouponsByCategory(@PathVariable String token,
			@RequestParam int category) throws InvalidTokenException, NonExistingCategoryException {
		ClientSession session = getSession(token);
		if (session == null) {
			throw new InvalidTokenException("Invalid token");
		}
		CustomerService service = (CustomerService) session.getService();
		final List<Coupon> allCustomerCouponsByCategory = service.getAllCustomerCouponsByCatergory(category);

		if (allCustomerCouponsByCategory.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(allCustomerCouponsByCategory);
	}

	/**
	 * This function allows us to retrieve all customer coupons before a date
	 * 
	 * @param token
	 * @param endDate
	 * @return ResponseEntity<List<Coupon>>
	 * @throws InvalidTokenException
	 */
	@GetMapping("/customers/couponsBeforeEndDate/{token}")
	public ResponseEntity<List<Coupon>> getAllCustomerCouponsBeforeEndDate(@PathVariable String token,
			@RequestParam Date endDate) throws InvalidTokenException {
		ClientSession session = getSession(token);
		if (session == null) {
			throw new InvalidTokenException("Invalid token");
		}
		CustomerService service = (CustomerService) session.getService();
		final List<Coupon> AllCustomerCouponsBeforeEndDate = service.getAllCustomerCouponsBeforeEndDate(endDate);

		if (AllCustomerCouponsBeforeEndDate.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(AllCustomerCouponsBeforeEndDate);
	}

	/**
	 * This function allows us to recover all coupons of the customer whose price is
	 * lower than the price in parameter
	 * 
	 * @param token
	 * @param price
	 * @return ResponseEntity<List<Coupon>>
	 * @throws InvalidTokenException
	 * @throws InvalidPriceException
	 */
	@GetMapping("/customers/couponsBelowPrice/{token}")
	public ResponseEntity<List<Coupon>> getAllCustomerCouponsBelowPrice(@PathVariable String token,
			@RequestParam double price) throws InvalidTokenException, InvalidPriceException {
		ClientSession session = getSession(token);
		if (session == null) {
			throw new InvalidTokenException("Invalid token");
		}
		CustomerService service = (CustomerService) session.getService();
		final List<Coupon> allCustomerCouponsBelowPrice = service.getAllCustomerCouponsBelowPrice(price);

		if (allCustomerCouponsBelowPrice.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(allCustomerCouponsBelowPrice);
	}

	/**
	 * This function allows us to modify the client's data
	 * 
	 * @param token
	 * @param customer
	 * @return ResponseEntity<Customer>
	 * @throws InvalidTokenException
	 */
	@PutMapping("/customers/{token}")
	public ResponseEntity<Customer> updateCustomer(@PathVariable String token, @RequestBody Customer customer)
			throws InvalidTokenException {
		ClientSession session = getSession(token);
		if (session == null) {
			throw new InvalidTokenException("Invalid token");
		}
		CustomerService service = (CustomerService) session.getService();
		return ResponseEntity.ok(service.updateCustomer(customer));
	}

	/**
	 * This function allows the customer to buy a coupon
	 * 
	 * @param token
	 * @param id
	 * @return ResponseEntity<Coupon>
	 * @throws InvalidTokenException
	 * @throws CouponAlreadyPurchasedException
	 * @throws ZeroCouponAmountException
	 */
	@PostMapping("/customers/coupons/{token}")
	public ResponseEntity<Coupon> purchaseCoupon(@PathVariable String token, @RequestParam long id)
			throws InvalidTokenException, CouponAlreadyPurchasedException, ZeroCouponAmountException {
		ClientSession session = getSession(token);
		if (session == null) {
			throw new InvalidTokenException("Invalid token");
		}
		CustomerService service = (CustomerService) session.getService();
		return ResponseEntity.ok(service.purchaseCoupon(id));
	}
}
