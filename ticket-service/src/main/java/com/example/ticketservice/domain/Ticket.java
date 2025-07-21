package com.example.ticketservice.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String status; // OPEN, IN_PROGRESS, CLOSED

    private String priority; // LOW, MEDIUM, HIGH

    private String createdBy;
}
