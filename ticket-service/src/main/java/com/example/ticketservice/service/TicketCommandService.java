package com.example.ticketservice.service;

import com.example.ticketservice.domain.Ticket;
import com.example.ticketservice.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketCommandService {

    private final TicketRepository repository;

    public Ticket createTicket(Ticket ticket) {
        ticket.setStatus("OPEN");
        return repository.save(ticket);
    }

    public Ticket updateTicket(Long id, Ticket ticket) {
        var existing = repository.findById(id).orElseThrow();
        existing.setTitle(ticket.getTitle());
        existing.setDescription(ticket.getDescription());
        existing.setPriority(ticket.getPriority());
        return repository.save(existing);
    }

    public void closeTicket(Long id) {
        var ticket = repository.findById(id).orElseThrow();
        ticket.setStatus("CLOSED");
        repository.save(ticket);
    }
}
