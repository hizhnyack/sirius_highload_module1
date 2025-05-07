package ru.hpclab.hl.module1.model;

import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "sales")
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private double weight;

    @Column(nullable = false)
    private double totalCost;

    // Конструкторы
    public Sale() {}

    public Sale(Long id, Product product, Customer customer, LocalDate date, double weight, double totalCost) {
        this.id = id;
        this.product = product;
        this.customer = customer;
        this.date = date;
        this.weight = weight;
        this.totalCost = totalCost;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    // equals, hashCode, toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sale sale = (Sale) o;
        return Double.compare(sale.weight, weight) == 0 &&
                Double.compare(sale.totalCost, totalCost) == 0 &&
                Objects.equals(id, sale.id) &&
                Objects.equals(product, sale.product) &&
                Objects.equals(customer, sale.customer) &&
                Objects.equals(date, sale.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, product, customer, date, weight, totalCost);
    }

    @Override
    public String toString() {
        return "Sale{" +
                "id=" + id +
                ", product=" + product +
                ", customer=" + customer +
                ", date=" + date +
                ", weight=" + weight +
                ", totalCost=" + totalCost +
                '}';
    }
}