package com.ticket.ticketservice.service;

import com.ticket.ticketservice.dto.ApiResponse;
import com.ticket.ticketservice.model.Ticket;
import com.ticket.ticketservice.repository.TicketRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;


class TicketServiceTest {
    @Mock
    private TicketRepository ticketRepository;
    @InjectMocks
    private TicketService ticketService;

    private Validator validator;

    Ticket ticket = new Ticket();
    @BeforeEach
    void setUp(){
        ticket.setId(1L);
        ticket.setTitle("ticketPost");
        ticket.setDescription("ticketPost desc");
        ticket.setQuantity(200);
        ticket.setEventDateTime(LocalDateTime.now().plusDays(2));
        MockitoAnnotations.openMocks(this);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void getAllTickets() {
        when(ticketRepository.findAll()).thenReturn(Collections.singletonList(ticket));
        ApiResponse<List<Ticket>> response = ticketService.getAllTickets();
        assertEquals(HttpStatus.OK, response.status());
        assertEquals(1, response.data().size());
    }

    @Test
    void getOneTicketWithValidId(){
        when(ticketRepository.findById(anyLong())).thenReturn(Optional.of(ticket));
        ApiResponse<Ticket> response = ticketService.getOneTicketById(1L);
        assertEquals(HttpStatus.OK, response.status());
        assertEquals("Ticket found",response.message());
        assertNotNull(response.data());
    }

    @Test
    void getOneTicketWithInvalidId(){
        when(ticketRepository.findById(2L)).thenReturn(Optional.empty());
        ApiResponse<Ticket> response = ticketService.getOneTicketById(2L);
        assertEquals(HttpStatus.NOT_FOUND, response.status());
        assertEquals("Ticket not found",response.message());
        assertNull(response.data());
    }

    @Test
    void deleteTicketWIthValidId(){
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        ApiResponse<String> response = ticketService.deleteOneTicketById(1L);
        assertEquals(HttpStatus.OK, response.status());
        assertEquals("Ticket deleted", response.message());
    }

    @Test
    void deleteTicketWithInvalidId(){
        when(ticketRepository.findById(2L)).thenReturn(Optional.empty());
        ApiResponse<String> response = ticketService.deleteOneTicketById(2L);
        assertEquals(HttpStatus.NOT_FOUND, response.status());
        assertEquals("Ticket not found", response.message());
    }
    @Test
    void deleteTicketWhenExceptionThrown(){
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        doThrow(new RuntimeException()).when(ticketRepository).deleteById(1L);
        ApiResponse<String> response = ticketService.deleteOneTicketById(1L);
        assertEquals(HttpStatus.BAD_REQUEST, response.status());
        assertEquals("Ticket deletion failed", response.message());
    }

    @Test
    void postTicketWithFullBodyShouldWork(){
        Ticket ticketPost = new Ticket();
        ticketPost.setTitle("Ticket1");
        ticketPost.setDescription("Ticket1 desc");
        ticketPost.setQuantity(10);
        ticketPost.setImageUrl("ImgURLofTicket1");
        ticketPost.setEventDateTime(LocalDateTime.now().plusDays(2));

        when(ticketRepository.save(ticketPost)).thenReturn(ticketPost);
        ApiResponse<Ticket> response = ticketService.createTicket(ticketPost);
        assertEquals(HttpStatus.CREATED, response.status());
        assertEquals(ticketPost, response.data());
        assertEquals("Ticket created", response.message());
    }
    @Test
    void postTicketWithMissingFieldsShouldNotWork(){
        Ticket ticketPostMissingFields = new Ticket();
        ticketPostMissingFields.setTitle("Ticket2");

        Set<ConstraintViolation<Ticket>> violations = validator.validate(ticketPostMissingFields);
        assertFalse(violations.isEmpty());

        when(ticketRepository.save(ticketPostMissingFields)).thenReturn(ticketPostMissingFields);
        ApiResponse<Ticket> response = ticketService.createTicket(ticketPostMissingFields);
        assertEquals(HttpStatus.BAD_REQUEST, response.status());
        assertNull(response.data());
        assertEquals("Ticket creation failed", response.message());
    }

    @Test
    void editTicketWithValidIdShouldWork() {
        Ticket updatedTicket = new Ticket();
        updatedTicket.setTitle("Updated Title");
        updatedTicket.setDescription("Updated Description");
        updatedTicket.setQuantity(100);
        updatedTicket.setEventDateTime(LocalDateTime.now().plusDays(5));

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(ticket)).thenReturn(ticket);

        ApiResponse<Ticket> response = ticketService.editTicket(1L, updatedTicket);
        assertEquals(HttpStatus.OK, response.status());
        assertEquals("Ticket updated", response.message());
        assertNotNull(response.data());
        assertEquals("Updated Title", response.data().getTitle());
        assertEquals("Updated Description", response.data().getDescription());
        assertEquals(100, response.data().getQuantity());
    }

    @Test
    void editTicketWithInvalidIdShouldReturnNotFound() {
        Ticket updatedTicket = new Ticket();
        updatedTicket.setTitle("Updated Title");

        when(ticketRepository.findById(2L)).thenReturn(Optional.empty());

        ApiResponse<Ticket> response = ticketService.editTicket(2L, updatedTicket);
        assertEquals(HttpStatus.NOT_FOUND, response.status());
        assertEquals("Ticket not found", response.message());
        assertNull(response.data());
    }

}