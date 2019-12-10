package com.jb.rest.controller;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jb.entity.Company;
import com.jb.entity.Coupon;
import com.jb.rest.ClientSession;
import com.jb.rest.ex.InvalidPriceException;
import com.jb.rest.ex.InvalidTokenException;
import com.jb.rest.ex.NoSuchCouponException;
import com.jb.rest.ex.NonExistingCategoryException;
import com.jb.service.CompanyService;

@RestController
@RequestMapping("/api")
public class CompanyController {

	private Map<String, ClientSession> tokensMap;

	@Autowired
	public CompanyController(@Qualifier("tokens") Map<String, ClientSession> tokensMap) {
		this.tokensMap = tokensMap;
	}

	private ClientSession getSession(String token) {
		return tokensMap.get(token);
	}

	/**
	 * This function allows us to create a coupon
	 * 
	 * @param token
	 * @param coupon
	 * @return ResponseEntity<Coupon>
	 * @throws InvalidTokenException
	 */
	@PostMapping("/companies/coupons/{token}")
	public ResponseEntity<Coupon> createCoupon(@PathVariable String token, @RequestBody Coupon coupon)
			throws InvalidTokenException {
		ClientSession session = getSession(token);
		if (session == null) {
			throw new InvalidTokenException("Invalid token");
		}
		CompanyService service = (CompanyService) session.getService();
		service.createCoupon(coupon);
		return ResponseEntity.ok(coupon);
	}

	/**
	 * This function allows us to retrieve a coupon
	 * 
	 * @param token
	 * @param id
	 * @return ResponseEntity<Optional<Coupon>>
	 * @throws InvalidTokenException
	 * @throws NoSuchCouponException
	 */
	@GetMapping("/companies/coupons/{token}")
	public ResponseEntity<Optional<Coupon>> getCoupon(@PathVariable String token, @RequestParam long id)
			throws InvalidTokenException, NoSuchCouponException {
		ClientSession session = getSession(token);
		if (session == null) {
			throw new InvalidTokenException("Invalid token");
		}
		CompanyService service = (CompanyService) session.getService();
		return ResponseEntity.ok(service.getCoupon(id));
	}

	/**
	 * This function allows us to recover all the coupons of a certain company
	 * 
	 * @param token
	 * @return ResponseEntity<List<Coupon>>
	 * @throws InvalidTokenException
	 */
	@GetMapping("/companies/allCoupons/{token}")
	public ResponseEntity<List<Coupon>> getAllCompanyCoupons(@PathVariable String token) throws InvalidTokenException {
		ClientSession session = getSession(token);
		if (session == null) {
			throw new InvalidTokenException("Invalid token");
		}
		CompanyService service = (CompanyService) session.getService();
		final List<Coupon> allCompanyCoupons = service.getAllCompanyCoupons();

		if (allCompanyCoupons.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(allCompanyCoupons);
	}

	/**
	 * This function allows us to recover all the coupons of a certain company by
	 * category
	 * 
	 * @param token
	 * @param category
	 * @return ResponseEntity<List<Coupon>>
	 * @throws InvalidTokenException
	 * @throws NonExistingCategoryException
	 */
	@GetMapping("/companies/allCouponsByCategory/{token}")
	public ResponseEntity<List<Coupon>> getAllCompanyCouponsByCategory(@PathVariable String token,
			@RequestParam int category) throws InvalidTokenException, NonExistingCategoryException {
		ClientSession session = getSession(token);
		if (session == null) {
			throw new InvalidTokenException("Invalid token");
		}
		CompanyService service = (CompanyService) session.getService();
		final List<Coupon> allCompanyCouponsByCategory = service.getAllCompanyCouponsByCategory(category);

		if (allCompanyCouponsByCategory.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(allCompanyCouponsByCategory);
	}

	/**
	 * This function allows us to retrieve all company coupons before a date
	 * 
	 * @param token
	 * @param endDate
	 * @return ResponseEntity<List<Coupon>>
	 * @throws InvalidTokenException
	 */
	@GetMapping("/companies/allCouponsBeforeEndDate/{token}")
	public ResponseEntity<List<Coupon>> getAllCompanyCouponsBeforeEndDate(@PathVariable String token,
			@RequestParam Date endDate) throws InvalidTokenException {
		ClientSession session = getSession(token);
		if (session == null) {
			throw new InvalidTokenException("Invalid token");
		}
		CompanyService service = (CompanyService) session.getService();
		final List<Coupon> allCompanyCouponsBeforeEndDate = service.getAllCompanyCouponsBeforeEndDate(endDate);

		if (allCompanyCouponsBeforeEndDate.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(allCompanyCouponsBeforeEndDate);
	}

	/**
	 * This function allows us to recover all coupons from the company whose price
	 * is lower than the price given in parameter
	 * 
	 * @param token
	 * @param price
	 * @return ResponseEntity<List<Coupon>>
	 * @throws InvalidTokenException
	 * @throws InvalidPriceException
	 */
	@GetMapping("/companies/allCouponsBelowPrice/{token}")
	public ResponseEntity<List<Coupon>> getAllCompanyCouponsBelowPrice(@PathVariable String token,
			@RequestParam double price) throws InvalidTokenException, InvalidPriceException {
		ClientSession session = getSession(token);
		if (session == null) {
			throw new InvalidTokenException("Invalid token");
		}
		CompanyService service = (CompanyService) session.getService();
		final List<Coupon> AllCompanyCouponsBelowPrice = service.getAllCompanyCouponsBelowPrice(price);

		if (AllCompanyCouponsBelowPrice.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(AllCompanyCouponsBelowPrice);
	}

	/**
	 * This function allows us to modify a coupon
	 * 
	 * @param token
	 * @param coupon
	 * @return ResponseEntity<Coupon>
	 * @throws InvalidTokenException
	 */
	@PutMapping("/companies/coupons/{token}")
	public ResponseEntity<Coupon> updateCoupon(@PathVariable String token, @RequestBody Coupon coupon)
			throws InvalidTokenException {
		ClientSession session = getSession(token);
		if (session == null) {
			throw new InvalidTokenException("Invalid token");
		}
		CompanyService service = (CompanyService) session.getService();
		return ResponseEntity.ok(service.updateCoupon(coupon));
	}

	/**
	 * This feature allows us to delete a coupon
	 * 
	 * @param token
	 * @param id
	 * @return ResponseEntity<Long> (id of the coupon)
	 * @throws InvalidTokenException
	 * @throws NoSuchCouponException
	 */
	@DeleteMapping("/companies/coupons/{token}")
	public ResponseEntity<Long> removeCoupon(@PathVariable String token, @RequestParam long id)
			throws InvalidTokenException, NoSuchCouponException {
		ClientSession session = getSession(token);
		if (session == null) {
			throw new InvalidTokenException("Invalid token");
		}
		CompanyService service = (CompanyService) session.getService();
		service.removeCoupon(id);
		return ResponseEntity.ok(id);
	}

	/**
	 * This function allows us to modify the data of the company
	 * 
	 * @param token
	 * @param company
	 * @return ResponseEntity<Company>
	 * @throws InvalidTokenException
	 */
	@PutMapping("/companies/{token}")
	public ResponseEntity<Company> updateCompany(@PathVariable String token, @RequestBody Company company)
			throws InvalidTokenException {
		ClientSession session = getSession(token);
		if (session == null) {
			throw new InvalidTokenException("Invalid token");
		}
		CompanyService service = (CompanyService) session.getService();
		service.updateCompany(company);
		return ResponseEntity.ok(company);
	}
}
