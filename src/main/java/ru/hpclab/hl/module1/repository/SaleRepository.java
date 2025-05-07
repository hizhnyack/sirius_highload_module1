package ru.hpclab.hl.module1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hpclab.hl.module1.model.Sale;

public interface SaleRepository extends JpaRepository<Sale, Long> {
}