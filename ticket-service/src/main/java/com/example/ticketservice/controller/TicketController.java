package com.example.ticketservice.controller;

import com.example.ticketservice.domain.TicketCommandDTO;
import com.example.ticketservice.domain.TicketQueryDTO;
import com.example.ticketservice.service.TicketCommandService;
import com.example.ticketservice.service.TicketQueryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class TicketController {

    private final TicketCommandService commandService;
    private final TicketQueryService queryService;


    @PostMapping("/create")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<TicketCommandDTO> createTicket(@Valid @RequestBody TicketCommandDTO ticketDto) {
        return ResponseEntity.ok(commandService.createTicket(ticketDto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SUPPORT') or hasRole('ADMIN')")
    public ResponseEntity<TicketCommandDTO> updateTicket(@PathVariable Long id, @RequestBody TicketCommandDTO dto) {
        return ResponseEntity.ok(commandService.updateTicket(id, dto));
    }

    @PatchMapping("/{id}/close")
    @PreAuthorize("hasRole('SUPPORT') or hasRole('ADMIN')")
    public ResponseEntity<TicketCommandDTO> closeTicket(@PathVariable Long id) {
        return ResponseEntity.ok(commandService.closeTicket(id));
    }


    @GetMapping
    @PreAuthorize("hasRole('SUPPORT') or hasRole('ADMIN')")
    public ResponseEntity<List<TicketQueryDTO>> getTicketsByFilter(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority) {
        return ResponseEntity.ok(queryService.getTicketsByFilter(status, priority));
    }

    @GetMapping("/mine")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<List<TicketQueryDTO>> getMyTickets() {
        return ResponseEntity.ok(queryService.getTicketsForCurrentUser());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('SUPPORT') or hasRole('ADMIN')")
    public ResponseEntity<TicketQueryDTO> getTicketById(@PathVariable Long id) {
        return ResponseEntity.ok(queryService.getById(id));
    }
}
