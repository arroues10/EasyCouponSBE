package com.jb.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.jb.entity.Company;
import com.jb.entity.Customer;
import com.jb.repository.CompanyRepository;
import com.jb.repository.CustomerRepository;
import com.jb.rest.ex.InvalidLoginException;
import com.jb.service.AdminServiceImpl;
import com.jb.service.CompanyServiceImpl;
import com.jb.service.CustomerServiceImpl;

/**
 * It is in this class that I create the login function whose login controller
 * will use
 * 
 * @author Solal Arroues
 *
 */
@Service
public class LoginSystem {

	// Fields
	private ApplicationContext context;
	private CompanyRepository companyRepository;
	private CustomerRepository customerRepository;

	// Constructor
	@Autowired
	public LoginSystem(ApplicationContext context, CompanyRepository companyRepository,
			CustomerRepository customerRepository) {
		this.context = context;
		this.companyRepository = companyRepository;
		this.customerRepository = customerRepository;
	}

	/**
	 * This function uses the switch box system to dispatch connections for Admin
	 * Company and Customer
	 * 
	 * @param email
	 * @param password
	 * @param loginType
	 * @return ClientSession
	 * @throws InvalidLoginException
	 */
	public ClientSession login(String email, String password, String loginType) throws InvalidLoginException {
		switch (loginType) {
			case "ADMIN":
				return adminLogin(email, password);
			case "COMPANY":
				return companyLogin(email, password);
			case "CUSTOMER":
				return customerLogin(email, password);
			default:
				throw new InvalidLoginException("Invalid loginType");
		}
	}

	private ClientSession companyLogin(String email, String password) throws InvalidLoginException {
		Company company = companyRepository.findByEmailAndPassword(email, password);
		if (company == null) {
			throw new InvalidLoginException("Email or password are invalid.");
		}
		CompanyServiceImpl service = context.getBean(CompanyServiceImpl.class);
		service.setCompanyId(company.getId());

		ClientSession session = context.getBean(ClientSession.class);
		session.setService(service);
		session.accessed();

		return session;
	}

	private ClientSession customerLogin(String email, String password) throws InvalidLoginException {
		Customer customer = customerRepository.findByEmailAndPassword(email, password);
		if (customer == null) {
			throw new InvalidLoginException("Email or password are invalid.");
		}
		CustomerServiceImpl service = context.getBean(CustomerServiceImpl.class);
		service.setCustomerId(customer.getId());

		ClientSession session = context.getBean(ClientSession.class);
		session.setService(service);
		session.accessed();

		return session;
	}

	private ClientSession adminLogin(String email, String password) throws InvalidLoginException {
		if (!email.equals("admin") && password.equals("1234")) {
			throw new InvalidLoginException("Email or password are invalid.");
		}
		AdminServiceImpl service = context.getBean(AdminServiceImpl.class);

		ClientSession session = context.getBean(ClientSession.class);
		session.setService(service);
		session.accessed();

		return session;
	}
}
