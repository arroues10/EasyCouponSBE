package com.jb.repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.jb.entity.Coupon;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {

	List<Coupon> findAllByCompanyId(long companyId);

	Optional<Coupon> findByIdAndCompanyId(long id, long companyId);

	List<Coupon> findAllByCategory(int category);

	List<Coupon> findAllByCompanyIdAndCategory(long companyId, int category);

	@Query("SELECT c FROM Customer cust JOIN cust.coupons c WHERE cust.id = :customerId")
	List<Coupon> findAllByCustomerId(long customerId);

	@Query("SELECT c FROM Customer cust JOIN cust.coupons c WHERE cust.id = :customerId AND c.category = :category")
	List<Coupon> findAllByCustomerIdAndCategory(long customerId, int category);

	@Query("SELECT c FROM Coupon c WHERE c.endDate <= :endDate")
	List<Coupon> findAllBeforeEndDate(Date endDate);

	@Query("SELECT c FROM Coupon c WHERE c.company.id = :companyId AND c.endDate <= :endDate")
	List<Coupon> findAllByCompanyIdAndBeforeEndDate(long companyId, Date endDate);

	@Query("SELECT c FROM Customer cust JOIN cust.coupons c WHERE cust.id = :customerId AND c.endDate <= :endDate")
	List<Coupon> findAllByCustomerIdAndBeforeEndDate(long customerId, Date endDate);

	@Query("SELECT c FROM Coupon c WHERE c.price <= :price")
	List<Coupon> findAllBelowPrice(double price);

	@Query("SELECT c FROM Coupon c WHERE c.price <= :price AND companyId = :companyId")
	List<Coupon> findAllByCompanyIdAndBelowPrice(long companyId, double price);

	@Query("SELECT c FROM Customer cust JOIN cust.coupons c WHERE cust.id = :customerId AND c.price <= :price")
	List<Coupon> findAllByCustomerIdAndBelowPrice(long customerId, double price);
}
