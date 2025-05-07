package ru.hpclab.hl.module1.model;

import java.time.LocalDateTime;
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

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(nullable = false)
    private Double weight;

    @Column(nullable = false)
    private LocalDateTime date;

    // Конструкторы
    public Sale() {}

    public Sale(Long id, Long productId, Long customerId, Double weight, LocalDateTime date) {
        this.id = id;
        this.productId = productId;
        this.customerId = customerId;
        this.weight = weight;
        this.date = date;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    // equals, hashCode, toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sale sale = (Sale) o;
        return Objects.equals(id, sale.id) &&
                Objects.equals(productId, sale.productId) &&
                Objects.equals(customerId, sale.customerId) &&
                Objects.equals(weight, sale.weight) &&
                Objects.equals(date, sale.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productId, customerId, weight, date);
    }

    @Override
    public String toString() {
        return "Sale{" +
                "id=" + id +
                ", productId=" + productId +
                ", customerId=" + customerId +
                ", weight=" + weight +
                ", date=" + date +
                '}';
    }
}