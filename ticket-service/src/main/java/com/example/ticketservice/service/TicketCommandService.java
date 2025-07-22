package com.example.ticketservice.service;

import com.example.ticketservice.entity.Ticket;
import com.example.ticketservice.domain.TicketCommandDTO;
import com.example.ticketservice.mapper.TicketMapper;
import com.example.ticketservice.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketCommandService {

    private final TicketRepository repository;
    private final TicketMapper ticketMapper;

    public TicketCommandDTO createTicket(TicketCommandDTO dto) {
        Ticket ticket = ticketMapper.toEntity(dto);
        ticket.setStatus("OPEN");
        return ticketMapper.toCommandDto(repository.save(ticket));
    }

    public TicketCommandDTO updateTicket(Long id, TicketCommandDTO dto) {
        Ticket existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        existing.setDescription(dto.getDescription());
        existing.setPriority(dto.getPriority());
        return ticketMapper.toCommandDto(repository.save(existing));
    }

    public TicketCommandDTO closeTicket(Long id) {
        Ticket ticket = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        ticket.setStatus("CLOSED");
        return ticketMapper.toCommandDto(repository.save(ticket));
    }
}
