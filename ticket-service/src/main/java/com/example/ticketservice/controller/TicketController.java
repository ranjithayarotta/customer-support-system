package com.example.ticketservice.controller;

import com.example.ticketservice.domain.Ticket;
import com.example.ticketservice.service.TicketCommandService;
import com.example.ticketservice.service.TicketQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketCommandService commandService;
    private final TicketQueryService queryService;

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'SUPPORT')")
    public Ticket create(@RequestBody Ticket ticket) {
        return commandService.createTicket(ticket);
    }

    @PutMapping("/{id}")
    public Ticket update(@PathVariable Long id, @RequestBody Ticket ticket) {
        return commandService.updateTicket(id, ticket);
    }

    @PutMapping("/{id}/close")
    @PreAuthorize("hasRole('SUPPORT')")
    public void close(@PathVariable Long id) {
        commandService.closeTicket(id);
    }

    @GetMapping
    public List<Ticket> getAll() {
        return queryService.getAll();
    }

    @GetMapping("/status/{status}")
    public List<Ticket> getByStatus(@PathVariable String status) {
        return queryService.filterByStatus(status);
    }

    @GetMapping("/priority/{priority}")
    public List<Ticket> getByPriority(@PathVariable String priority) {
        return queryService.filterByPriority(priority);
    }

    @GetMapping("/{id}")
    public Ticket getById(@PathVariable Long id) {
        return queryService.getById(id);
    }
}
