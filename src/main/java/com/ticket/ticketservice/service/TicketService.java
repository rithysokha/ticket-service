package com.ticket.ticketservice.service;

import com.ticket.ticketservice.dto.ApiResponse;
import com.ticket.ticketservice.model.Ticket;
import com.ticket.ticketservice.repository.TicketRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.UnhandledException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;

    private Optional<Ticket> findTicketById(Long id) {
        return ticketRepository.findById(id);
    }

    public ApiResponse<List<Ticket>> getAllTickets() {
        return new ApiResponse<>("All tickets retrieved",
                ticketRepository.findAll(), HttpStatus.OK);
    }

    public ApiResponse<Ticket> getOneTicketById(Long id) {
        Optional<Ticket> ticket = findTicketById(id);
        return ticket.map(value
                -> new ApiResponse<>("Ticket found", value, HttpStatus.OK)).orElseGet(()
                -> new ApiResponse<>("Ticket not found", null, HttpStatus.NOT_FOUND));
    }

    public ApiResponse<Ticket> createTicket(Ticket ticketBody) {
        try {
            Ticket ticket = ticketRepository.save(ticketBody);
            return new ApiResponse<>("Ticket created", ticket, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ApiResponse<>("Ticket creation failed", null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ApiResponse<String> deleteOneTicketById(Long id) {
        Optional<Ticket> ticket = findTicketById(id);
        try {
            if (ticket.isPresent()){
             ticketRepository.deleteById(id);
             return new ApiResponse<>("Ticket deleted", null, HttpStatus.OK);}
        } catch (Exception e) {
            return new ApiResponse<>("Ticket deletion failed", null, HttpStatus.BAD_REQUEST);
        }
        return new ApiResponse<>("Ticket not found", null, HttpStatus.NOT_FOUND);
    }
//    Update the ticket
//    Check each attribute if it's not null or empty before updating it
@Transactional
public ApiResponse<Ticket> editTicket(Long id, Ticket ticketBody) {
    Optional<Ticket> ticketOptional = findTicketById(id);
    if (ticketOptional.isEmpty()) {
        return new ApiResponse<>("Ticket not found", null, HttpStatus.NOT_FOUND);
    }
    try {
        Ticket ticket = ticketOptional.get();
        if (ticketBody.getTitle() != null && !ticketBody.getTitle().isEmpty())
            ticket.setTitle(ticketBody.getTitle());
        if (ticketBody.getImageUrl() != null && !ticketBody.getImageUrl().isEmpty())
            ticket.setImageUrl(ticketBody.getImageUrl());
        if (ticketBody.getDescription() != null && !ticketBody.getDescription().isEmpty())
            ticket.setDescription(ticketBody.getDescription());
        if (ticketBody.getEventDateTime() != null && !ticketBody.getEventDateTime().toString().isEmpty())
            ticket.setEventDateTime(ticketBody.getEventDateTime());
        if (ticketBody.getQuantity() != null && !ticketBody.getQuantity().toString().isEmpty())
            ticket.setQuantity(ticketBody.getQuantity());

        ticketRepository.save(ticket);
        return new ApiResponse<>("Ticket updated", ticket, HttpStatus.OK);
    } catch (UnhandledException e) {
        return new ApiResponse<>("Ticket updating failed", null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
}
