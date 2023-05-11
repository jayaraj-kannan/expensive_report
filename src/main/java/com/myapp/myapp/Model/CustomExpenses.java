package com.myapp.myapp.Model;

import jakarta.persistence.*;

@Entity
@Table
public class CustomExpenses {
    @SequenceGenerator(
            name = "expenses_sequence",
            sequenceName = "expenses_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "expenses_sequence"
    )
    private Long id;
}
