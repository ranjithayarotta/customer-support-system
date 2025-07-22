package com.example.ticketservice.controller.service;

import com.example.ticketservice.domain.TicketCommandDTO;
import com.example.ticketservice.entity.Ticket;
import com.example.ticketservice.mapper.TicketMapper;
import com.example.ticketservice.repository.TicketRepository;
import com.example.ticketservice.service.TicketCommandService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TicketCommandServiceTest {

    @Mock
    private TicketRepository repository;

    @Mock
    private TicketMapper mapper;

    @InjectMocks
    private TicketCommandService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTicket_shouldSaveAndReturnDto() {
        TicketCommandDTO inputDto = new TicketCommandDTO();
        Ticket entity = new Ticket();
        Ticket savedEntity = new Ticket(1L, "Title", "Desc", "OPEN", "HIGH", "user");

        when(mapper.toEntity(inputDto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(savedEntity);
        when(mapper.toCommandDto(savedEntity)).thenReturn(new TicketCommandDTO());

        TicketCommandDTO result = service.createTicket(inputDto);

        assertNotNull(result);
        assertEquals("OPEN", savedEntity.getStatus());
        verify(repository).save(entity);
    }

    @Test
    void updateTicket_shouldUpdateAndReturnDto() {
        TicketCommandDTO inputDto = new TicketCommandDTO();
        inputDto.setDescription("New Desc");
        inputDto.setPriority("LOW");

        Ticket existing = new Ticket(1L, "Title", "Old Desc", "OPEN", "HIGH", "user");

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);
        when(mapper.toCommandDto(existing)).thenReturn(new TicketCommandDTO());

        TicketCommandDTO result = service.updateTicket(1L, inputDto);

        assertEquals("New Desc", existing.getDescription());
        assertEquals("LOW", existing.getPriority());
        verify(repository).save(existing);
    }

    @Test
    void updateTicket_shouldThrowIfNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.updateTicket(1L, new TicketCommandDTO()));
    }

    @Test
    void closeTicket_shouldUpdateStatusToClosed() {
        Ticket ticket = new Ticket(1L, "Title", "Desc", "OPEN", "HIGH", "user");

        when(repository.findById(1L)).thenReturn(Optional.of(ticket));
        when(repository.save(ticket)).thenReturn(ticket);
        when(mapper.toCommandDto(ticket)).thenReturn(new TicketCommandDTO());

        TicketCommandDTO result = service.closeTicket(1L);

        assertEquals("CLOSED", ticket.getStatus());
        verify(repository).save(ticket);
    }

    @Test
    void closeTicket_shouldThrowIfNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.closeTicket(99L));
    }
}
