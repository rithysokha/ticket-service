package com.ticket.ticketservice.dto;

import org.springframework.http.HttpStatus;

public record ApiResponse<T>(String message, T data, HttpStatus status) {
}
