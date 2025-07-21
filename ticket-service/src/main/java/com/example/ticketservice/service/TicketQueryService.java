package com.example.ticketservice.service;

import com.example.ticketservice.domain.Ticket;
import com.example.ticketservice.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketQueryService {

    private final TicketRepository repository;

    public List<Ticket> getAll() {
        return repository.findAll();
    }

    public List<Ticket> filterByStatus(String status) {
        return repository.findByStatus(status);
    }

    public List<Ticket> filterByPriority(String priority) {
        return repository.findByPriority(priority);
    }

    public Ticket getById(Long id) {
        return repository.findById(id).orElseThrow();
    }
}
