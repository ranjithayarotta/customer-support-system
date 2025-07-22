package com.example.ticketservice.controller.service;

import com.example.ticketservice.domain.TicketQueryDTO;
import com.example.ticketservice.entity.Ticket;
import com.example.ticketservice.mapper.TicketMapper;
import com.example.ticketservice.repository.TicketRepository;
import com.example.ticketservice.service.TicketQueryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TicketQueryServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private TicketMapper ticketMapper;

    @InjectMocks
    private TicketQueryService ticketQueryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("testUser", null)
        );
    }

    @Test
    void testGetTicketsByFilter() {
        Ticket ticket1 = new Ticket(1L, "Bug #1", "Fix it", "OPEN", "HIGH", "testUser");
        TicketQueryDTO dto1 = new TicketQueryDTO(1L, "Bug #1", "Fix it", "OPEN", "HIGH", "testUser");

        when(ticketRepository.findAll()).thenReturn(List.of(ticket1));
        when(ticketMapper.toQueryDto(ticket1)).thenReturn(dto1);

        List<TicketQueryDTO> result = ticketQueryService.getTicketsByFilter("OPEN", "HIGH");

        assertEquals(1, result.size());
        assertEquals("Bug #1", result.get(0).getTitle());
    }


    @Test
    void testGetById_found() {
        Ticket ticket = new Ticket(1L, "Bug #2", "Crash", "CLOSED", "MEDIUM", "testUser");
        TicketQueryDTO dto = new TicketQueryDTO(1L, "Bug #2", "Crash", "CLOSED", "MEDIUM", "testUser");

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(ticketMapper.toQueryDto(ticket)).thenReturn(dto);

        TicketQueryDTO result = ticketQueryService.getById(1L);

        assertEquals("Bug #2", result.getTitle());
    }

    @Test
    void testGetById_notFound() {
        when(ticketRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            ticketQueryService.getById(999L);
        });

        assertEquals("Ticket not found", exception.getMessage());
    }
}
