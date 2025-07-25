package com.example.ticketservice.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketQueryDTO {
    private Long id;
    private String title;
    private String status;
    private String priority;
    private String createdBy;
    private String description;

}