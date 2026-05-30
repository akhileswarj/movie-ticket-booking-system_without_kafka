package com.movie.ticket.booking.system.service.impl;

import com.example.democom.movie.ticket.booking.system.commons.dto.BookingDto;
import com.example.democom.movie.ticket.booking.system.commons.dto.BookingStatus;
import com.example.democom.movie.ticket.booking.system.commons.dto.ResponseDto;
import com.movie.ticket.booking.system.broker.PaymentServiceBroker;
import com.movie.ticket.booking.system.entity.BookingEntity;
import com.movie.ticket.booking.system.repository.BookingRepo;
import com.movie.ticket.booking.system.service.BookingService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepo bookingRepo;

    @Autowired
    private PaymentServiceBroker paymentService;

    @Override
    @Transactional
    public ResponseDto createBooking(BookingDto bookingDto) {
        log.info("entered into BookingService createBooking method with request data {}", bookingDto.toString());

        BookingEntity bookingEntityBuilded = BookingEntity.builder()
                .movieId(bookingDto.getMovieId())
                .bookingStatus(BookingStatus.PENDING)
                .seatsSelected(bookingDto.getSeatsSelected())
                .showDate(bookingDto.getShowDate())
                .showTime(bookingDto.getShowTime())
                .userId(bookingDto.getUserId())
                .bookingAmount(bookingDto.getBookingAmount())
                .build();

        bookingRepo.save(bookingEntityBuilded);

        bookingDto.setBookingId(bookingEntityBuilded.getBookingId());
        //call payment service
        log.info("calling payment service to make payment of amount {} and for booking id {}",
                bookingEntityBuilded.getBookingAmount(), bookingEntityBuilded.getBookingId());
        BookingDto paymentResponse = paymentService.makePayment(bookingDto);
        log.info("payment was successful for the booking id {}", bookingEntityBuilded.getBookingId());

        bookingEntityBuilded.setBookingStatus(paymentResponse.getBookingStatus());

        log.info(String.valueOf(paymentResponse));

        return ResponseDto.builder()
                .bookingDto(BookingDto.builder()
                        .bookingId(bookingEntityBuilded.getBookingId())
                        .movieId(bookingEntityBuilded.getMovieId())
                        .bookingStatus(paymentResponse.getBookingStatus())
                        .seatsSelected(bookingEntityBuilded.getSeatsSelected())
                        .showDate(bookingEntityBuilded.getShowDate())
                        .showTime(bookingEntityBuilded.getShowTime())
                        .userId(bookingEntityBuilded.getUserId())
                        .bookingAmount(bookingEntityBuilded.getBookingAmount())
                        .paymentLink(paymentResponse.getPaymentLink())
                        .razorpayOrderId(paymentResponse.getRazorpayOrderId())
                        .build())
                .build();
    }


    @Override
    @Transactional
    public void updateBookingStatus(UUID bookingId, String status) {
        BookingEntity booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found: " + bookingId));
        booking.setBookingStatus(BookingStatus.valueOf(status));
        bookingRepo.save(booking);
        log.info("Booking {} status updated to {}", bookingId, status);
    }

    @Override
    public BookingDto getBookingById(UUID bookingId) {
        BookingEntity booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found: " + bookingId));
        return BookingDto.builder()
                .bookingId(booking.getBookingId())
                .userId(booking.getUserId())
                .movieId(booking.getMovieId())
                .seatsSelected(booking.getSeatsSelected())
                .showDate(booking.getShowDate())
                .showTime(booking.getShowTime())
                .bookingAmount(booking.getBookingAmount())
                .bookingStatus(booking.getBookingStatus())
                .build();
    }
}
