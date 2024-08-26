package com.ticket.ticketservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;



import java.time.LocalDateTime;

@Data
@Entity(name = "ticket")
@Getter
@Setter
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank
    @Column(name = "title", nullable = false)
    private String title;
    @NotBlank
    @Column(name = "description", nullable = false)
    private String description;
    @NotBlank
    @Column(name = "imageUrl", nullable = false)
    private String imageUrl;
    @NotNull
    @Column(name = "quantity", nullable = false)
    private Integer quantity;
    @NotNull
    @Column(name = "eventDateTime", nullable = false)
    private LocalDateTime eventDateTime;
}
