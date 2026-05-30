package com.example.democom.movie.ticket.booking.system.commons.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
@Builder
public class BookingDto {
    private UUID bookingId;
    @NotBlank(message = "user id is mandatory")
    private String userId;
    @NotNull(message = "movie id is mandatory")
    @Positive(message = "please provide valid movie id")
    private Integer movieId;
    @NotEmpty(message = "please provide at least one seat")
    private List<String> seatsSelected;
    @NotNull(message = "please select show date")
    private LocalDate showDate;
    @NotNull(message = "please select show time")
    private LocalTime showTime;
    @Positive(message = "booking amount should be a positive value")
    @NotNull(message = "please select booking amount")
    private Double bookingAmount;
    private BookingStatus bookingStatus;
    private String paymentLink;
    private String razorpayOrderId; // ← add in both services' BookingDto
}



