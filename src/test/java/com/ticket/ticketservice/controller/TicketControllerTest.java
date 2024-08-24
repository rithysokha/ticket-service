package com.ticket.ticketservice.controller;

import com.ticket.ticketservice.dto.ApiResponse;
import com.ticket.ticketservice.model.Ticket;
import com.ticket.ticketservice.service.TicketService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TicketController.class)
class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TicketService ticketService;

    @Test
    void getAllTickets() throws Exception {
        when(ticketService.getAllTickets()).thenReturn(new ApiResponse<>("success", Collections.emptyList(), HttpStatus.OK));

        mockMvc.perform(get("/api/ticket"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void getOneTicketById() throws Exception {
        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setTitle("Sample Ticket");

        when(ticketService.getOneTicketById(anyLong())).thenReturn(new ApiResponse<>("ticket found", ticket, HttpStatus.OK));

        mockMvc.perform(get("/api/ticket/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("ticket found"))
                .andExpect(jsonPath("$.data.title").value("Sample Ticket"));
    }

    @Test
    void createTicket() throws Exception {
        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setTitle("New Ticket");

        when(ticketService.createTicket(any(Ticket.class))).thenReturn(new ApiResponse<>("ticket created", ticket, HttpStatus.CREATED));

        mockMvc.perform(post("/api/ticket")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"New Ticket\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("ticket created"))
                .andExpect(jsonPath("$.data.title").value("New Ticket"));
    }

    @Test
    void deleteTicketById() throws Exception {
        when(ticketService.deleteOneTicketById(anyLong())).thenReturn(new ApiResponse<>("ticket deleted", null, HttpStatus.OK));

        mockMvc.perform(delete("/api/ticket/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("ticket deleted"));
    }

    @Test
    void editTicketById() throws Exception {
        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setTitle("Updated Ticket");

        when(ticketService.editTicket(anyLong(), any(Ticket.class))).thenReturn(new ApiResponse<>("ticket updated", ticket, HttpStatus.OK));

        mockMvc.perform(put("/api/ticket/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"Updated Ticket\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("ticket updated"))
                .andExpect(jsonPath("$.data.title").value("Updated Ticket"));
    }
}