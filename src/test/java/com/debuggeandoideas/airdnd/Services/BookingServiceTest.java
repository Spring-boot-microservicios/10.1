package com.debuggeandoideas.airdnd.Services;

import com.debuggeandoideas.airdnd.dto.BookingDto;
import com.debuggeandoideas.airdnd.helpers.MailHelper;
import com.debuggeandoideas.airdnd.repositories.BookingRepository;
import com.debuggeandoideas.airdnd.utils.DataDummy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    private PaymentService paymentServiceMock;

    @Mock
    private RoomService roomServiceMock;

    @Mock
    private BookingRepository bookingRepositoryMock;

    @Mock
    private MailHelper mailHelperMock;

    @InjectMocks
    private BookingService bookingService;

    @Test
    @DisplayName("getAvailablePlaceCount should works")
    public void getAvailablePlaceCount() {
        when(this.roomServiceMock.findAllAvailableRooms()).thenReturn(DataDummy.default_rooms_list);

        var expected = 14;
        var result = this.bookingService.getAvailablePlaceCount();

        assertEquals(expected, result);
    }

    @Test
    @DisplayName("booking should works")
    public void booking() {
        final var roomId = UUID.randomUUID().toString();

        when(this.roomServiceMock.findAvailableRoom(DataDummy.default_booking_req_1))
                .thenReturn(DataDummy.default_rooms_list.stream().findFirst().get());

        when(this.bookingRepositoryMock.save(DataDummy.default_booking_req_1))
                .thenReturn(roomId);

        var result = this.bookingService.booking(DataDummy.default_booking_req_1);

        assertEquals(roomId, result);
    }

    @Test
    @DisplayName("booking should works - any()")
    public void bookingAny() {
        final var roomId = UUID.randomUUID().toString();

        // any cuando no requiro obtener los argumentos necesariamente
        when(this.roomServiceMock.findAvailableRoom(any(BookingDto.class)))
                .thenReturn(DataDummy.default_rooms_list.stream().findFirst().get());

        when(this.bookingRepositoryMock.save(any(BookingDto.class)))
                .thenReturn(roomId);

        var result = this.bookingService.booking(DataDummy.default_booking_req_2);

        assertEquals(roomId, result);
    }

}
