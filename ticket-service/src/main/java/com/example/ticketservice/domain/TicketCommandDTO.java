package com.example.ticketservice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketCommandDTO {
    private Long id;
    private String title;
    private String description;
    private String priority;
    private TicketStatus status;
}