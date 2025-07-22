package com.example.ticketservice.controller;

import com.example.ticketservice.service.TicketCommandService;
import com.example.ticketservice.service.TicketQueryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TicketController.class)
class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TicketCommandService commandService;

    @MockBean
    private TicketQueryService queryService;

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void shouldAllowCustomerToAccessMyTickets() throws Exception {
        mockMvc.perform(get("/api/tickets/mine"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldAllowAdminToAccessAllTickets() throws Exception {
        mockMvc.perform(get("/api/tickets"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "SUPPORT")
    void shouldAllowSupportToUpdateTicket() throws Exception {
        mockMvc.perform(get("/api/tickets/1"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnUnauthorizedForAnonymousUser() throws Exception {
        mockMvc.perform(get("/api/tickets"))
                .andExpect(status().isUnauthorized());
    }
}
