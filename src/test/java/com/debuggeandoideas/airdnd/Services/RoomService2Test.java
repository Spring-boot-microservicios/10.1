package com.debuggeandoideas.airdnd.Services;

import com.debuggeandoideas.airdnd.repositories.RoomRepository;
import com.debuggeandoideas.airdnd.utils.DataDummy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/**
 * Uso de Spy
 */
@ExtendWith(MockitoExtension.class)
public class RoomService2Test {

    private RoomRepository roomRepositoryMock;
    private RoomService roomService; // objetivo a testear

    // BeforeAll se ejecuta antes de todos los test
    @BeforeEach
    public void init() {
        this.roomRepositoryMock = spy(RoomRepository.class);
        this.roomService = new RoomService(this.roomRepositoryMock);
    }

    @Test
    @DisplayName("Should get all rooms available in repository")
    public void findAllAvailableRooms() {

        // Simular el retorno que necesitamos
        when(roomRepositoryMock.findAll()).thenReturn(DataDummy.default_rooms);

        var expected = 3;
        var result = this.roomService.findAllAvailableRooms();

        assertEquals(expected, result.size());
    }

}
