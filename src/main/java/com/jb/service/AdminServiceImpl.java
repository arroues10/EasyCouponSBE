package com.jb.service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jb.entity.Company;
import com.jb.entity.Coupon;
import com.jb.entity.Customer;
import com.jb.repository.CompanyRepository;
import com.jb.repository.CouponRepository;
import com.jb.repository.CustomerRepository;
import com.jb.rest.ex.CompanyAlreadyExistsException;
import com.jb.rest.ex.CustomerAlreadyExistsException;
import com.jb.rest.ex.InvalidPriceException;
import com.jb.rest.ex.NoSuchMemberException;
import com.jb.rest.ex.NonExistingCategoryException;

/**
 * In this class I create all the functions that will be used later by the admin
 * controller
 * 
 * @author Solal Arroues
 *
 */
@Service
public class AdminServiceImpl implements AdminService {

	// Fields
	private CompanyRepository companyRepository;
	private CouponRepository couponRepository;
	private CustomerRepository customerRepository;

	// Constructor
	@Autowired
	public AdminServiceImpl(CompanyRepository companyRepository, CustomerRepository customerRepository,
			CouponRepository couponRepository) {
		this.companyRepository = companyRepository;
		this.customerRepository = customerRepository;
		this.couponRepository = couponRepository;
	}

	/**
	 * This function uses the already existing save function in JpaRepository to
	 * insert a new company to the database The company's id is initialized to 0 in
	 * order to create a new company and not to modify an existing company
	 * 
	 * @throws CompanyAlreadyExistsException : if the company email give already
	 *                                       exists in the database
	 * @return Company
	 */
	@Override
	public Company createCompany(Company company) throws CompanyAlreadyExistsException {
		if (companyAlreadyExistsByEmail(company)) {
			throw new CompanyAlreadyExistsException(
					String.format("This email : %s , is already exists", company.getEmail()));
		}
		if (company != null) {
			company.setId(0);
			return companyRepository.save(company);
		}
		return null;
	}

	/**
	 * this function uses the already existing save function in JpaRepository to
	 * insert a new customer to the database the customer id is initialized to 0 in
	 * order to create a new company and not modify an existing customer
	 * 
	 * @param customer
	 * @throws CustomerAlreadyExistsException : if the customer email give already
	 *                                        exists in the database
	 * @return Customer
	 */
	@Override
	public Customer createCustomer(Customer customer) throws CustomerAlreadyExistsException {
		if (customerAlreadyExistsByEmail(customer)) {
			throw new CustomerAlreadyExistsException(
					String.format("This email : %s , is already exists", customer.getEmail()));
		}
		if (customer != null) {
			customer.setId(0);
			return customerRepository.save(customer);
		}
		return null;
	}

	/**
	 * This function calls the already existing getAll function in the JpaRepository
	 * in order to retrieve all the data from the company table of the database
	 * 
	 * @return List<Company>
	 */
	@Override
	public List<Company> getAllCompanies() {
		return companyRepository.findAll();
	}

	/**
	 * This function calls the existing getAll function in the JpaRepository to
	 * retrieve all the data from the customer table of the database
	 * 
	 * @return List<Customer>
	 */
	@Override
	public List<Customer> getAllCustomers() {
		return customerRepository.findAll();
	}

	/**
	 * This function calls the already existing getAll function in the JpaRepository
	 * in order to retrieve all the data from the coupon table of the database
	 */
	@Override
	public List<Coupon> getAllCoupons() {
		return couponRepository.findAll();
	}

	/**
	 * This function calls the getAllCouponsByCategory function that I created in
	 * company repository in order to retrieve all of them from the coupon table
	 * corresponding to the parameter category
	 * 
	 * @throws NonExistingCategoryException : if the given category does not exist
	 * @return List<Coupon>
	 */
	@Override
	public List<Coupon> getAllCouponsByCategory(int category) throws NonExistingCategoryException {
		if (category > 8 || category < 1) {
			throw new NonExistingCategoryException(String.format("This category : %d, is not exists", category));
		}
		return couponRepository.findAllByCategory(category);
	}

	/**
	 * This function calls the getAllCouponsBeforeEndDate function that I created in
	 * company repository in order to retrieve all the given coupon table whose
	 * expiry date is not yet passed
	 * 
	 * @return List<Coupon>
	 */
	@Override
	public List<Coupon> getAllCouponsBeforeEndDate(Date endDate) {
		return couponRepository.findAllBeforeEndDate(endDate);
	}

	/**
	 * This function uses the findAllBelowPrice function that I created in
	 * CouponRepository to retrieve all coupons that are priced below the parameter
	 * price.
	 * 
	 * @return List<Coupon>
	 */
	@Override
	public List<Coupon> getAllCouponsBelowPrice(double price) throws InvalidPriceException {
		if (price <= 0) {
			throw new InvalidPriceException("Invalid price : " + price);
		}
		return couponRepository.findAllBelowPrice(price);
	}

	/**
	 * This function uses the existing deleteById function in JpaRepository to
	 * delete a company from the database
	 * 
	 * @param id
	 * @throws NoSuchMemberException : if the given id does not exist
	 */
	@Override
	public void removeCompany(long id) throws NoSuchMemberException {
		Optional<Company> company = companyRepository.findById(id);
		if (!company.isPresent()) {
			throw new NoSuchMemberException("Invalid company id");
		}
		companyRepository.deleteById(id);
	}

	/**
	 * this function uses the existing deleteById function in JpaRepository to
	 * delete a customer from the database
	 * 
	 * @param id
	 * @throws NoSuchMemberException : if the given id does not exist
	 */
	@Override
	public void removeCustomer(long id) throws NoSuchMemberException {
		Optional<Customer> customer = customerRepository.findById(id);
		if (!customer.isPresent()) {
			throw new NoSuchMemberException("Invalid customer id");
		}
		customerRepository.deleteById(id);
	}

	/**
	 * This function is a private function that allows me to check if the company
	 * already exists in the database by email
	 * 
	 * @param company
	 * @return true if exist and false otherwise
	 */
	private boolean companyAlreadyExistsByEmail(Company company) {
		for (Company c : getAllCompanies()) {
			if (c.getEmail().equals(company.getEmail())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * This function is a private function that allows me to check if the customer
	 * already exists in the database by email
	 * 
	 * @param customer
	 * @return true if exist and false otherwise
	 */
	private boolean customerAlreadyExistsByEmail(Customer customer) {
		for (Customer c : getAllCustomers()) {
			if (c.getEmail().equals(customer.getEmail())) {
				return true;
			}
		}
		return false;
	}
}
