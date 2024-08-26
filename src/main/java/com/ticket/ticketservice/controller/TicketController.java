package com.ticket.ticketservice.controller;

import com.ticket.ticketservice.dto.ApiResponse;
import com.ticket.ticketservice.model.Ticket;
import com.ticket.ticketservice.service.TicketService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ticket")
@AllArgsConstructor
public class TicketController {
    private final TicketService ticketService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Ticket>>> getAllTickets(){
        ApiResponse<List<Ticket>> response = ticketService.getAllTickets();
        return new ResponseEntity<>(response,response.status());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Ticket>> getOneTicketById(@PathVariable Long id){
        ApiResponse<Ticket> response = ticketService.getOneTicketById(id);
        return new ResponseEntity<>(response, response.status());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Ticket>> createTicket(@Valid @RequestBody Ticket ticket){
        ApiResponse<Ticket> response = ticketService.createTicket(ticket);
        return new ResponseEntity<>(response, response.status());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteTicketById(@PathVariable Long id){
        ApiResponse<String> response = ticketService.deleteOneTicketById(id);
        return new ResponseEntity<>(response, response.status());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Ticket>> editTicketById(@PathVariable Long id, @RequestBody Ticket ticketBody){
        ApiResponse<Ticket> response = ticketService.editTicket(id, ticketBody);
        return new ResponseEntity<>(response, response.status());
    }
}
