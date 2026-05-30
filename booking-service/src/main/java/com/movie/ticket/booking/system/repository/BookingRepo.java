package com.movie.ticket.booking.system.repository;

import com.movie.ticket.booking.system.entity.BookingEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BookingRepo extends CrudRepository<BookingEntity, UUID> {

}
