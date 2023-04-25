package com.cretus.retailbackend.entity;

import java.util.HashSet;

import com.cretus.retailbackend.DiscountType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="discounts")
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String discountCode;

    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    @Column(nullable = false, precision = 7, scale = 2)
    private BigDecimal discountValue;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean active;

    @ManyToMany(cascade = CascadeType.ALL, targetEntity = Product.class, mappedBy = "discounts")
    private Set<Product> products = new HashSet<>();

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
