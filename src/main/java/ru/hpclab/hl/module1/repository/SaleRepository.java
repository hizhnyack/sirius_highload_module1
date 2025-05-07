package ru.hpclab.hl.module1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hpclab.hl.module1.model.Sale;

import java.time.LocalDate;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {
    List<Sale> findByDateBetween(LocalDate startDate, LocalDate endDate);
}