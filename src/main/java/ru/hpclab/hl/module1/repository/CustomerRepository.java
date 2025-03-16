package ru.hpclab.hl.module1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hpclab.hl.module1.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}