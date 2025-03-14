package ru.hpclab.hl.module1.repository;

import org.springframework.stereotype.Repository;
import ru.hpclab.hl.module1.model.Sale;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class SaleRepository {
    private static final List<Sale> sales = new ArrayList<>();

    public List<Sale> findAll() {
        return sales;
    }

    public void save(Sale sale) {
        sales.add(sale);
    }

    public Optional<Sale> findById(Long id) {
        return sales.stream()
                .filter(sale -> sale.getId().equals(id))
                .findFirst();
    }
}