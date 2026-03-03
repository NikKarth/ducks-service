package edu.iu.habahram.ducksservice.repository;

import edu.iu.habahram.ducksservice.model.Customer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerFileRepository {

    private final List<Customer> customers = new ArrayList<>();

    public Customer save(Customer customer) {
        customers.add(customer);
        return customer;
    }

    public Optional<Customer> findByUsername(String username) {
        return customers.stream()
                .filter(c -> c.getUsername().equals(username))
                .findFirst();
    }
}