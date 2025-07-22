package com.example.ticketservice.service;

import com.example.ticketservice.domain.TicketQueryDTO;
import com.example.ticketservice.mapper.TicketMapper;
import com.example.ticketservice.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketQueryService {

    private final TicketRepository repository;
    private final TicketMapper ticketMapper;

    public List<TicketQueryDTO> getTicketsByFilter(String status, String priority) {
        return repository.findAll().stream()
                .filter(ticket -> (status == null || ticket.getStatus().equals(status)))
                .filter(ticket -> (priority == null || ticket.getPriority().equals(priority)))
                .map(ticketMapper::toQueryDto)
                .toList();
    }

    public List<TicketQueryDTO> getTicketsForCurrentUser() {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        return repository.findBycreatedBy(currentUsername)
                .stream()
                .map(ticketMapper::toQueryDto)
                .toList();
    }
    public TicketQueryDTO getById(Long id) {
        return ticketMapper.toQueryDto(repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found")));
    }
}
