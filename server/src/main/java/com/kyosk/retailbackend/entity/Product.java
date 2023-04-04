package com.kyosk.retailbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String name;

    @Column(nullable=false)
    private String image;

    @Column(nullable=false)
    private String productCode;

    @Column(nullable=false, length = 512)
    private String description;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name="product_id")
    private List<ProductAttribute> productAttributeList;

    @ManyToMany(targetEntity = Discount.class, cascade = CascadeType.ALL, fetch=FetchType.EAGER)
    private Set<Discount> discounts = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL )
    private ProductPrice productPrice;

    @OneToOne(cascade = CascadeType.ALL )
    private ProductInventory productInventory;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
