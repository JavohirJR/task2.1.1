package com.javohir.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false,unique = true)
    @NotNull(message = "Fill the blank")
    private String corpName;

    @NotNull(message = "Fill the blank")
    @Column(nullable = false)
    private String directorName;

    @OneToOne
    private Address address;
}