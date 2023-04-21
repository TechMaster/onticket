package vn.techmaster.onticket.controller;

import vn.techmaster.onticket.model.Customer;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CustomerController {
  final private List<Customer> customers = List.of(
      Customer.builder().id("001").name("Customer 1").email("c1@gmail.com").build(),
      Customer.builder().id("002").name("Customer 2").email("c2@gmail.com").build());

  @GetMapping("/hello")
  public ResponseEntity<String> hello() {
    return ResponseEntity.ok("hello is exception");
  }

  @GetMapping("customer/all")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public ResponseEntity<List<Customer>> getCustomerList() {
    List<Customer> customers = this.customers;
    return ResponseEntity.ok(customers);
  }

  @GetMapping("/customer/{id}")
  @PreAuthorize("hasAuthority('ROLE_USER')")
  public ResponseEntity<Customer> getCustomerList(@PathVariable("id") String id) {
    List<Customer> customers = this.customers.stream().filter(customer -> customer.getId().equals(id)).toList();
    return ResponseEntity.ok(customers.get(0));
  }
}
