package com.jb.rest.controller;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jb.entity.Company;
import com.jb.entity.Coupon;
import com.jb.entity.Customer;
import com.jb.rest.ClientSession;
import com.jb.rest.ex.CompanyAlreadyExistsException;
import com.jb.rest.ex.CustomerAlreadyExistsException;
import com.jb.rest.ex.InvalidPriceException;
import com.jb.rest.ex.InvalidTokenException;
import com.jb.rest.ex.NoSuchMemberException;
import com.jb.rest.ex.NonExistingCategoryException;
import com.jb.service.AdminService;

@RestController
@RequestMapping("/api")
public class AdminController {

	// Field
	private Map<String, ClientSession> tokensMap;

	/**
	 * Constructor
	 * 
	 * @param tokensMap
	 */
	@Autowired
	public AdminController(@Qualifier("tokens") Map<String, ClientSession> tokensMap) {
		this.tokensMap = tokensMap;
	}

	/**
	 * This function returns us the session which corresponds to the token received
	 * 
	 * @param token
	 * @return ClientSession
	 */
	private ClientSession getSession(String token) {
		return tokensMap.get(token);
	}

	/**
	 * This function allows us to create a company
	 * 
	 * @param token
	 * @param company
	 * @return ResponseEntity<Company>
	 * @throws InvalidTokenException
	 * @throws CompanyAlreadyExistsException
	 */
	@PostMapping("/admin/companies/{token}")
	public ResponseEntity<Company> createCompany(@PathVariable String token, @RequestBody Company company)
			throws InvalidTokenException, CompanyAlreadyExistsException {
		ClientSession session = getSession(token);
		if (session == null) {
			throw new InvalidTokenException("Invalid token");
		}
		AdminService service = (AdminService) session.getService();
		service.createCompany(company);
		return ResponseEntity.ok(company);
	}

	/**
	 * This function allows us to create a customer
	 * 
	 * @param token
	 * @param customer
	 * @return ResponseEntity<Customer>
	 * @throws InvalidTokenException
	 * @throws CustomerAlreadyExistsException
	 */
	@PostMapping("/admin/customers/{token}")
	public ResponseEntity<Customer> createCustomer(@PathVariable String token, @RequestBody Customer customer)
			throws InvalidTokenException, CustomerAlreadyExistsException {
		ClientSession session = getSession(token);
		if (session == null) {
			throw new InvalidTokenException("Invalid token");
		}
		AdminService service = (AdminService) session.getService();
		service.createCustomer(customer);
		return ResponseEntity.ok(customer);
	}

	/**
	 * This function returns us all the companies in the database
	 * 
	 * @param token
	 * @return ResponseEntity<List<Company>>
	 * @throws InvalidTokenException
	 */
	@GetMapping("/admin/companies/{token}")
	public ResponseEntity<List<Company>> getAllCompany(@PathVariable String token) throws InvalidTokenException {
		ClientSession session = getSession(token);
		if (session == null) {
			throw new InvalidTokenException("Invalid token");
		}
		AdminService service = (AdminService) session.getService();
		final List<Company> allCompanies = service.getAllCompanies();

		if (allCompanies.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(allCompanies);
	}

	/**
	 * This function returns us all the customers of the database
	 * 
	 * @param token
	 * @return ResponseEntity<List<Customer>>
	 * @throws InvalidTokenException
	 */
	@GetMapping("/admin/customers/{token}")
	public ResponseEntity<List<Customer>> getAllCustomers(@PathVariable String token) throws InvalidTokenException {
		ClientSession session = getSession(token);
		if (session == null) {
			throw new InvalidTokenException("Invalid token");
		}
		AdminService service = (AdminService) session.getService();
		final List<Customer> allCustomers = service.getAllCustomers();

		if (allCustomers.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(allCustomers);
	}

	/**
	 * This function returns us all coupons from the database
	 * 
	 * @param token
	 * @return ResponseEntity<List<Coupon>>
	 * @throws InvalidTokenException
	 */
	@GetMapping("/admin/coupons/{token}")
	public ResponseEntity<List<Coupon>> getAllCoupons(@PathVariable String token) throws InvalidTokenException {
		ClientSession session = getSession(token);
		if (session == null) {
			throw new InvalidTokenException("Invalid token");
		}
		AdminService service = (AdminService) session.getService();
		final List<Coupon> allCoupons = service.getAllCoupons();

		if (allCoupons.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(allCoupons);
	}

	/**
	 * This function returns us all coupons from the database by category
	 * 
	 * @param token
	 * @param category
	 * @return ResponseEntity<List<Coupon>>
	 * @throws InvalidTokenException
	 * @throws NonExistingCategoryException
	 */
	@GetMapping("/admin/couponsByCategory/{token}")
	public ResponseEntity<List<Coupon>> getAllCouponsByCategory(@PathVariable String token, @RequestParam int category)
			throws InvalidTokenException, NonExistingCategoryException {
		ClientSession session = getSession(token);
		if (session == null) {
			throw new InvalidTokenException("Invalid token");
		}
		AdminService service = (AdminService) session.getService();
		final List<Coupon> allCouponsByCategory = service.getAllCouponsByCategory(category);

		if (allCouponsByCategory.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(allCouponsByCategory);
	}

	/**
	 * This function allows us to retrieve all coupons before a date
	 * 
	 * @param token
	 * @param endDate
	 * @return ResponseEntity<List<Coupon>>
	 * @throws InvalidTokenException
	 */
	@GetMapping("/admin/couponsBeforeEndDate/{token}")
	public ResponseEntity<List<Coupon>> getAllCouponsBeforEndDate(@PathVariable String token,
			@RequestParam Date endDate) throws InvalidTokenException {
		ClientSession session = getSession(token);
		if (session == null) {
			throw new InvalidTokenException("Invalid token");
		}
		AdminService service = (AdminService) session.getService();
		final List<Coupon> allCouponsBeforeEndDate = service.getAllCouponsBeforeEndDate(endDate);

		if (allCouponsBeforeEndDate.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(allCouponsBeforeEndDate);
	}

	/**
	 * This function allows us to recover all coupons whose price is lower than the
	 * price given in parameter
	 * 
	 * @param token
	 * @param price
	 * @return ResponseEntity<List<Coupon>>
	 * @throws InvalidTokenException
	 * @throws InvalidPriceException
	 */
	@GetMapping("/admin/couponsBelowPrice/{token}")
	public ResponseEntity<List<Coupon>> getAllCouponsBelowPrice(@PathVariable String token, @RequestParam double price)
			throws InvalidTokenException, InvalidPriceException {
		ClientSession session = getSession(token);
		if (session == null) {
			throw new InvalidTokenException("Invalid token");
		}
		AdminService service = (AdminService) session.getService();
		final List<Coupon> allCouponsBelowPrice = service.getAllCouponsBelowPrice(price);

		if (allCouponsBelowPrice.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(allCouponsBelowPrice);
	}

	/**
	 * This function allows us to delete a company
	 * 
	 * @param token
	 * @param id
	 * @return id of the company
	 * @throws InvalidTokenException
	 * @throws NoSuchMemberException
	 */
	@DeleteMapping("admin/companies/{token}")
	public ResponseEntity<Long> removeCompany(@PathVariable String token, @RequestParam long id)
			throws InvalidTokenException, NoSuchMemberException {
		ClientSession session = getSession(token);
		if (session == null) {
			throw new InvalidTokenException("Invalid token");
		}
		AdminService service = (AdminService) session.getService();
		service.removeCompany(id);
		return ResponseEntity.ok(id);
	}

	/**
	 * This function allows us to delete a customer
	 * 
	 * @param token
	 * @param id
	 * @return id of the customer
	 * @throws InvalidTokenException
	 * @throws NoSuchMemberException
	 */
	@DeleteMapping("admin/customers/{token}")
	public ResponseEntity<Long> removeCustomer(@PathVariable String token, @RequestParam long id)
			throws InvalidTokenException, NoSuchMemberException {
		ClientSession session = getSession(token);
		if (session == null) {
			throw new InvalidTokenException("Invalid token");
		}
		AdminService service = (AdminService) session.getService();
		service.removeCustomer(id);
		return ResponseEntity.ok(id);
	}
}
