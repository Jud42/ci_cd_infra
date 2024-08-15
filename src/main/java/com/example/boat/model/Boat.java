package com.example.boat.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Boat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotBlank
    @Column(unique = true)
    private String name;

    @NotBlank
    private String description;

    //constructor
    public Boat(String name, String description) {
        this.name = name;
        this.description = description;
    }

    //getters
    public String getDescription() {
        return description;
    }
    public String getName() {
        return name;
    }
}