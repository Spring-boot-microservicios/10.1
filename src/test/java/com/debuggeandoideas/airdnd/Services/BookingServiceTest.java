package com.debuggeandoideas.airdnd.Services;

import com.debuggeandoideas.airdnd.dto.BookingDto;
import com.debuggeandoideas.airdnd.helpers.MailHelper;
import com.debuggeandoideas.airdnd.repositories.BookingRepository;
import com.debuggeandoideas.airdnd.utils.DataDummy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    private PaymentService paymentServiceMock;

    @Mock
    private RoomService roomServiceMock;

    @Spy // Sirve para utilizar literal el metodo a usar
    private BookingRepository bookingRepositoryMock;

    @Mock // Simula completamente el metodo
    private MailHelper mailHelperMock;

    @Captor
    private ArgumentCaptor<String> stringCapture;

    @InjectMocks
    private BookingService bookingService;

    @Test
    @DisplayName("getAvailablePlaceCount should works")
    public void getAvailablePlaceCount() {

        // Obtener varios retornos
        when(this.roomServiceMock.findAllAvailableRooms())
                .thenReturn(DataDummy.default_rooms_list)
                .thenReturn(Collections.emptyList())
                .thenReturn(DataDummy.single_rooms_list);

        var expected_1 = 14;
        var expected_2 = 0;
        var expected_3 = 5;

        var result_1 = this.bookingService.getAvailablePlaceCount();
        var result_2 = this.bookingService.getAvailablePlaceCount();
        var result_3 = this.bookingService.getAvailablePlaceCount();

//        assertEquals(expected_1, result_1);
//        assertEquals(expected_2, result_2);

        assertAll(
            () ->  assertEquals(expected_1, result_1),
            () ->  assertEquals(expected_2, result_2),
            () ->  assertEquals(expected_3, result_3)
        );
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

        // Si se llama el metodo real
        when(this.bookingRepositoryMock.save(any(BookingDto.class)))
                .thenReturn(roomId);

        var result = this.bookingService.booking(DataDummy.default_booking_req_2);

        assertEquals(roomId, result);
    }

    @Test
    @DisplayName("booking should works - doReturn()")
    public void bookingDoReturn() {
        final var roomId = UUID.randomUUID().toString();

        // No se llama el metodo real - 100% mock
        doReturn(DataDummy.default_rooms_list.stream().findFirst().get())
                .when(this.roomServiceMock).findAvailableRoom(DataDummy.default_booking_req_1);

        doReturn(roomId).when(this.bookingRepositoryMock).save(DataDummy.default_booking_req_1);

        // Llamar a metodos void
        doNothing()
                .when(this.roomServiceMock).bookRoom(anyString());

        var result = this.bookingService.booking(DataDummy.default_booking_req_1);

        assertEquals(roomId, result);

        // Verificamos que los metodos se mandaron a llamar
        // times() // para saber el numero de veces que se debe llamar el metodo
        verify(this.roomServiceMock, times(1)).findAvailableRoom(any(BookingDto.class));
        verify(this.bookingRepositoryMock, times(1)).save(any(BookingDto.class));

        // Verificar y testear el metodo void
        verify(this.roomServiceMock, times(1)).bookRoom(anyString());
    }

    @Test
    @DisplayName("booking happyPath should works")
    public void bookingHappyPath() {
        final var roomId = UUID.randomUUID().toString();

        // No se llama el metodo real - 100% mock
        doReturn(DataDummy.default_rooms_list.stream().findFirst().get())
                .when(this.roomServiceMock).findAvailableRoom(DataDummy.default_booking_req_1);

        doReturn(roomId).when(this.bookingRepositoryMock).save(DataDummy.default_booking_req_1);

        // Llamar a metodos void
        doNothing()
                .when(this.roomServiceMock).bookRoom(anyString());

        var result = this.bookingService.booking(DataDummy.default_booking_req_1);

        assertEquals(roomId, result);

        // Verificamos que los metodos se mandaron a llamar
        // times() // para saber el numero de veces que se debe llamar el metodo
        verify(this.roomServiceMock, times(1)).findAvailableRoom(any(BookingDto.class));
        verify(this.bookingRepositoryMock, times(1)).save(any(BookingDto.class));

        // Verificar y testear el metodo void
        verify(this.roomServiceMock, times(1)).bookRoom(anyString());
    }

    @Test
    @DisplayName("booking unhappyPath should works")
    public void bookingUnHappyPath() {

        doReturn(DataDummy.default_rooms_list.stream().findFirst().get())
                .when(this.roomServiceMock).findAvailableRoom(DataDummy.default_booking_req_3);

//        doThrow(new IllegalArgumentException("Max 3 guest"))
//                .when(this.paymentServiceMock).pay(any(BookingDto.class), anyDouble());

        // Otra forma de obtener excepciones
//        when(this.paymentServiceMock.pay(any(BookingDto.class), anyDouble()))
//                .thenThrow(new IllegalArgumentException("Max 3 guest"));

        // eq() => es un equals() el argumento es igual a..
        doThrow(new IllegalArgumentException("Max 3 guest"))
                .when(this.paymentServiceMock).pay(eq(DataDummy.default_booking_req_3), eq(320.0));

        Executable executable = () -> this.bookingService.booking(DataDummy.default_booking_req_3);

        assertThrows(IllegalArgumentException.class, executable);
    }

    @Test
    @DisplayName("unbook should works")
    public void unBook() {
        String id_1 = "id1";
        String id_2 = "id2";

        BookingDto bookingResp_1 = DataDummy.default_booking_req_1;
        bookingResp_1.setRoom(DataDummy.default_rooms_list.get(3));

        BookingDto bookingResp_2 = DataDummy.default_booking_req_2;
        bookingResp_2.setRoom(DataDummy.default_rooms_list.get(4));

        when(this.bookingRepositoryMock.findById(anyString()))
                .thenReturn(bookingResp_1)
                .thenReturn(bookingResp_2);

        doNothing().when(this.roomServiceMock).unbookRoom(anyString());
        doNothing().when(this.bookingRepositoryMock).deleteById(anyString());

        this.bookingService.unbook(id_1);
        this.bookingService.unbook(id_2);

        verify(this.roomServiceMock, times(2)).unbookRoom(anyString());
        verify(this.bookingRepositoryMock, times(2)).deleteById(anyString());


        verify(this.bookingRepositoryMock, times(2))
                .findById(this.stringCapture.capture());

        // Evaluamos los argumentos que llamaron a las funciones en metodos void
        System.out.println("captured arguments: " + this.stringCapture.getAllValues());
        assertEquals(List.of(id_1, id_2), this.stringCapture.getAllValues());
    }


}
