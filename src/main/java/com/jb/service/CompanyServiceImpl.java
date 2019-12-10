package com.jb.service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jb.entity.Company;
import com.jb.entity.Coupon;
import com.jb.repository.CompanyRepository;
import com.jb.repository.CouponRepository;
import com.jb.rest.ex.InvalidPriceException;
import com.jb.rest.ex.NoSuchCouponException;
import com.jb.rest.ex.NonExistingCategoryException;

/**
 * In this class I create all the functions that will be used later by the
 * company controller
 * 
 * @author Solal Arroues
 *
 */
@Service
public class CompanyServiceImpl implements CompanyService {

	// Fields
	private long companyId;

	private CompanyRepository companyRepository;
	private CouponRepository couponRepository;

	// Constructor
	@Autowired
	public CompanyServiceImpl(CompanyRepository companyRepository, CouponRepository couponRepository) {
		this.companyRepository = companyRepository;
		this.couponRepository = couponRepository;
	}

	// is use in loginSystem.companyLogin()
	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	/**
	 * This function uses the existing save function in JpaRepository to insert a
	 * new coupon into the database the coupon id is initialized to 0 in order to
	 * create a new coupon and not modify an existing coupon
	 * 
	 * @param Coupon
	 * @return Coupon
	 */
	@Override
	public Coupon createCoupon(Coupon coupon) {
		coupon.setCompany(companyRepository.findById(companyId).orElse(null));
		coupon.setId(0);
		return couponRepository.save(coupon);
	}

	/**
	 * This function uses the findByIdAndCompanyId function I created in company
	 * repository to retrieve a coupon by its id and its company id
	 * 
	 * @throws NoSuchCouponException : if id is not valid
	 * @param id
	 * @return Optional<Coupon>
	 */
	@Override
	public Optional<Coupon> getCoupon(long id) throws NoSuchCouponException {
		Optional<Coupon> coupon = couponRepository.findByIdAndCompanyId(id, companyId);
		if (!coupon.isPresent()) {
			throw new NoSuchCouponException("Invalid coupon id");
		}
		return coupon;
	}

	/**
	 * this function uses the function findAllByCompanyId that I created in company
	 * repository to retrieve all the coupons of the company
	 * 
	 * @return List<Coupon>
	 */
	@Override
	public List<Coupon> getAllCompanyCoupons() {
		return couponRepository.findAllByCompanyId(companyId);
	}

	/**
	 * This function uses the findAllByCompanyIdAndCategory function I created in
	 * company repository to retrieve all company coupons by category
	 * 
	 * @throws NonExistingCategoryException : if the category is not exists
	 * @param category
	 * @return List<Coupon>
	 */
	@Override
	public List<Coupon> getAllCompanyCouponsByCategory(int category) throws NonExistingCategoryException {
		if (category > 8 || category < 1) {
			throw new NonExistingCategoryException(String.format("This category : %d, is not exists", category));
		}
		return couponRepository.findAllByCompanyIdAndCategory(companyId, category);
	}

	/**
	 * This function uses the function findAllByCompanyIdAndBeforeEndDate that I
	 * created in company repository to retrieve all the coupons of the company
	 * whose date has not yet expired
	 * 
	 * @param endDate
	 * @return List<Coupon>
	 */
	@Override
	public List<Coupon> getAllCompanyCouponsBeforeEndDate(Date endDate) {
		return couponRepository.findAllByCompanyIdAndBeforeEndDate(companyId, endDate);
	}

	/**
	 * This function uses the function findAllByCompanyIdAndBelowPrice that I
	 * created in CouponRepository to retrieve all the coupons of the company whose
	 * price is lower than the price in parameter
	 * 
	 * @return List<Coupon>
	 * @throws InvalidPriceException 
	 */
	@Override
	public List<Coupon> getAllCompanyCouponsBelowPrice(double price) throws InvalidPriceException {
		if (price <= 0) {
			throw new InvalidPriceException("Invalid price : " + price);
		}
		return couponRepository.findAllByCompanyIdAndBelowPrice(companyId, price);
	}

	/**
	 * This function uses the already existing save function in JpaRepository to
	 * modify an already existing coupon
	 * 
	 * @param coupon
	 * @return Coupon
	 */
	@Override
	public Coupon updateCoupon(Coupon coupon) {
		return couponRepository.save(coupon);
	}

	/**
	 * This function uses the already existing deleteById function in JpaRepository
	 * to delete a coupon by its id
	 * 
	 * @throws NoSuchCouponException : if the id is not valid
	 * @param id
	 * @return id
	 */
	@Override
	public void removeCoupon(long id) throws NoSuchCouponException {
		Optional<Coupon> coupon = couponRepository.findByIdAndCompanyId(id, companyId);
		if (!coupon.isPresent()) {
			throw new NoSuchCouponException("Invalid coupon id");
		}
		couponRepository.deleteById(id);
	}

	/**
	 * This function uses the already existing save function in JpaRepository to
	 * modify the company data
	 * 
	 * @param company
	 * @return Company
	 */
	@Override
	public Company updateCompany(Company company) {
		company.setId(companyId);
		return companyRepository.save(company);
	}
}
