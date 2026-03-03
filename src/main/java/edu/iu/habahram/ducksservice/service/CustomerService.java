package edu.iu.habahram.ducksservice.service;

import edu.iu.habahram.ducksservice.model.Customer;
import edu.iu.habahram.ducksservice.repository.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer signup(String username, String password, String email) {
        // basic duplicate check
        if (customerRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        Customer c = new Customer(username, password, email); // (stores plain text for now)
        return customerRepository.save(c);
    }

    public void signin(String username, String password) {
        Customer c = customerRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid username/password"));

        if (!c.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid username/password");
        }
    }
}