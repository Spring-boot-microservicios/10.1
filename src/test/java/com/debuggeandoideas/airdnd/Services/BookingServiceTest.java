package com.debuggeandoideas.airdnd.Services;

import com.debuggeandoideas.airdnd.helpers.MailHelper;
import com.debuggeandoideas.airdnd.repositories.BookingRepository;
import com.debuggeandoideas.airdnd.utils.DataDummy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

}
