package ru.hpclab.hl.module1.repository;

import ru.hpclab.hl.module1.model.Customer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerRepository {
    private static final List<Customer> customers = new ArrayList<>();

    public List<Customer> findAll() {
        return customers;
    }

    public void save(Customer customer) {
        customers.add(customer);
    }

    public Optional<Customer> findById(Long id) {
        return customers.stream()
                .filter(customer -> customer.getId().equals(id))
                .findFirst();
    }
}