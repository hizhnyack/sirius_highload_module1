package ru.hpclab.hl.module1.model;

import java.util.Objects;
import javax.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private boolean hasDiscountCard;
    // Конструкторы
    public Customer() {}

    public Customer(Long id, String fullName, String phone, boolean hasDiscountCard) {
        this.id = id;
        this.fullName = fullName;
        this.phone = phone;
        this.hasDiscountCard = hasDiscountCard;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isHasDiscountCard() {
        return hasDiscountCard;
    }

    public void setHasDiscountCard(boolean hasDiscountCard) {
        this.hasDiscountCard = hasDiscountCard;
    }

    // equals, hashCode, toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return hasDiscountCard == customer.hasDiscountCard &&
                Objects.equals(id, customer.id) &&
                Objects.equals(fullName, customer.fullName) &&
                Objects.equals(phone, customer.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fullName, phone, hasDiscountCard);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", phone='" + phone + '\'' +
                ", hasDiscountCard=" + hasDiscountCard +
                '}';
    }
}