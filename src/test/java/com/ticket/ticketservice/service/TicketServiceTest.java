package com.ticket.ticketservice.service;

import com.ticket.ticketservice.model.Ticket;
import com.ticket.ticketservice.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class TicketServiceTest {
    @Mock
    private TicketRepository ticketRepository;
    @InjectMocks
    private TicketService ticketService;

    @BeforeEach
    void setUp(){
        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setTitle("ticket1");
        ticket.setDescription("ticket1 desc");
        ticket.setQuantity(200);
        ticket.setEventDateTime(LocalDateTime.now().plusDays(2));
    }

    @Test
    void getAllTickets() {
    }

    @Test
    void getOneTicketById() {
    }

    @Test
    void createTicket() {
    }

    @Test
    void deleteOneTicketById() {
    }

    @Test
    void editTicket() {
    }
}