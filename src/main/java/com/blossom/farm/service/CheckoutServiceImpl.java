package com.blossom.farm.service;

import java.util.Random;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.blossom.farm.dao.CustomerRepository;
import com.blossom.farm.dto.Purchase;
import com.blossom.farm.dto.PurchaseResponse;
import com.blossom.farm.model.Customer;
import com.blossom.farm.model.Order;
import com.blossom.farm.model.OrderItem;

@Service
public class CheckoutServiceImpl implements CheckoutService{
	
	private CustomerRepository customerRepository;
	
	public CheckoutServiceImpl(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	@Override
	@Transactional
	public PurchaseResponse placeOrder(Purchase purchase) {
		
		// retrieve order from DTO
		Order order = purchase.getOrder();
		
		//generate tracking number
		String orderTrackingNumber = generateTrackingNumber();
		order.setOrderTrackingNumber(orderTrackingNumber);
		
		//populate order with orderItem
		Set<OrderItem> orderItems = purchase.getOrderItems();
		orderItems.forEach(item -> order.add(item));
		
		//populate order with shipping and billing addresses
		order.setBillingAddress(purchase.getBillingAddress());
		order.setShippingAddress(purchase.getShippingAddress());
		
		//populate customer with order
		Customer customer = purchase.getCustomer();
		customer.add(order);
		
		//save to the database
		System.out.println("----Saving Customer----");
		customerRepository.save(customer);
		
		//return a response
		return new PurchaseResponse(orderTrackingNumber);
		
	}

	private String generateTrackingNumber() {
		//one way	
		//String r = Math.random().nextInt(999999).toString().padLeft(6, '0');
		// generate random UUID - google wikipedia Universally Unique Identifier version 4
		Integer otp = new Random().nextInt(999999);
		int noOfOtpDigit=6;
		  while(Integer.toString(otp).length()!=noOfOtpDigit) {
		  otp = new Random().nextInt(999999);
		 }
		//return UUID.randomUUID().toString();
		  return String.valueOf(otp);
		
	}

}
