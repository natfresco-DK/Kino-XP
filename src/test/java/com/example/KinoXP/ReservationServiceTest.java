package com.example.KinoXP;

import com.example.KinoXP.dto.CreateReservationRequest;
import com.example.KinoXP.dto.ReservationResponse;
import com.example.KinoXP.model.Screen;
import com.example.KinoXP.model.Screening;
import com.example.KinoXP.model.Seat;
import com.example.KinoXP.repository.ReservationRepo;
import com.example.KinoXP.repository.ScreeningRepo;
import com.example.KinoXP.repository.SeatRepo;
import com.example.KinoXP.service.ReservationService;
import com.example.KinoXP.utils.ScreenType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepo reservationRepo;

    @Mock
    private ScreeningRepo screeningRepo;

    @Mock
    private SeatRepo seatRepo;

    @InjectMocks
    private ReservationService reservationService;

    private Screen lilleSal;
    private Screen storSal;
    private Screening upcomingScreening;
    private Seat seatA1;
    private Seat seatA2;

    @BeforeEach
    void setUp() {
        lilleSal = newScreen(1L, "Lille", ScreenType.SMALL);
        storSal = newScreen(2L, "Stor", ScreenType.LARGE);

        upcomingScreening = newScreening(1L, lilleSal, LocalDateTime.now().plusDays(1));

        seatA1 = newSeat(1L, "A", 1, lilleSal);
        seatA2 = newSeat(2L, "A", 2, lilleSal);
    }

    @Test
    void createReservation_savesOneReservationPerSeat() {
        List<Long> seatIds = List.of(1L, 2L);
        when(screeningRepo.findById(1L)).thenReturn(Optional.of(upcomingScreening));
        when(seatRepo.findAllById(seatIds)).thenReturn(List.of(seatA1, seatA2));
        when(reservationRepo.existsByScreeningAndSeatIdIn(upcomingScreening, seatIds)).thenReturn(false);
        saveAllReturnsSameList();

        List<ReservationResponse> responses = reservationService.createReservation(
                new CreateReservationRequest(1L, seatIds, "12345678"));

        assertEquals(2, responses.size());
        assertEquals(1L, responses.get(0).seatId());
        assertEquals(2L, responses.get(1).seatId());
        assertEquals("12345678", responses.get(0).phoneNumber());
        assertEquals(1L, responses.get(0).screeningId());
    }

    @Test
    void createReservation_trimsPhoneNumber() {
        List<Long> seatIds = List.of(1L);
        when(screeningRepo.findById(1L)).thenReturn(Optional.of(upcomingScreening));
        when(seatRepo.findAllById(seatIds)).thenReturn(List.of(seatA1));
        when(reservationRepo.existsByScreeningAndSeatIdIn(upcomingScreening, seatIds)).thenReturn(false);
        saveAllReturnsSameList();

        List<ReservationResponse> responses = reservationService.createReservation(
                new CreateReservationRequest(1L, seatIds, "  12345678  "));

        assertEquals("12345678", responses.getFirst().phoneNumber());
    }

    @Test
    void createReservation_duplicateSeatIds_reservesSeatOnce() {
        List<Long> distinctSeatIds = List.of(1L);
        when(screeningRepo.findById(1L)).thenReturn(Optional.of(upcomingScreening));
        when(seatRepo.findAllById(distinctSeatIds)).thenReturn(List.of(seatA1));
        when(reservationRepo.existsByScreeningAndSeatIdIn(upcomingScreening, distinctSeatIds)).thenReturn(false);
        saveAllReturnsSameList();

        List<ReservationResponse> responses = reservationService.createReservation(
                new CreateReservationRequest(1L, List.of(1L, 1L), "12345678"));

        assertEquals(1, responses.size());
    }

    @Test
    void createReservation_withoutPhoneNumber_throws() {
        assertBadRequest(new CreateReservationRequest(1L, List.of(1L), null),
                "Telefonnummer skal udfyldes");
    }

    @Test
    void createReservation_blankPhoneNumber_throws() {
        assertBadRequest(new CreateReservationRequest(1L, List.of(1L), "   "),
                "Telefonnummer skal udfyldes");
    }

    @Test
    void createReservation_phoneNumberTooShort_throws() {
        assertBadRequest(new CreateReservationRequest(1L, List.of(1L), "123"),
                "Telefonnummer skal indeholde præcis 8 cifre og kun tal");
    }

    @Test
    void createReservation_phoneNumberWithLetters_throws() {
        assertBadRequest(new CreateReservationRequest(1L, List.of(1L), "1234abcd"),
                "Telefonnummer skal indeholde præcis 8 cifre og kun tal");
    }

    @Test
    void createReservation_withoutSeats_throws() {
        assertBadRequest(new CreateReservationRequest(1L, List.of(), "12345678"),
                "Vælg mindst ét sæde");
    }

    @Test
    void createReservation_withoutScreeningId_throws() {
        assertBadRequest(new CreateReservationRequest(null, List.of(1L), "12345678"),
                "Vælg en forestilling");
    }

    @Test
    void createReservation_unknownScreening_throws() {
        when(screeningRepo.findById(999L)).thenReturn(Optional.empty());

        assertBadRequest(new CreateReservationRequest(999L, List.of(1L), "12345678"),
                "Forestillingen findes ikke");
    }

    @Test
    void createReservation_screeningAlreadyStarted_throws() {
        Screening startedScreening = newScreening(2L, lilleSal, LocalDateTime.now().minusMinutes(10));
        when(screeningRepo.findById(2L)).thenReturn(Optional.of(startedScreening));

        assertBadRequest(new CreateReservationRequest(2L, List.of(1L), "12345678"),
                "Forestillingen er allerede startet");
    }

    @Test
    void createReservation_unknownSeat_throws() {
        List<Long> seatIds = List.of(1L, 999L);
        when(screeningRepo.findById(1L)).thenReturn(Optional.of(upcomingScreening));
        when(seatRepo.findAllById(seatIds)).thenReturn(List.of(seatA1));

        assertBadRequest(new CreateReservationRequest(1L, seatIds, "12345678"),
                "Et eller flere sæder findes ikke");
    }

    @Test
    void createReservation_seatFromOtherScreen_throws() {
        Seat seatInStorSal = newSeat(500L, "A", 1, storSal);
        List<Long> seatIds = List.of(500L);
        when(screeningRepo.findById(1L)).thenReturn(Optional.of(upcomingScreening));
        when(seatRepo.findAllById(seatIds)).thenReturn(List.of(seatInStorSal));

        assertBadRequest(new CreateReservationRequest(1L, seatIds, "12345678"),
                "Sædet hører ikke til forestillingens sal");
    }

    @Test
    void createReservation_seatAlreadyReserved_throwsConflict() {
        List<Long> seatIds = List.of(1L);
        when(screeningRepo.findById(1L)).thenReturn(Optional.of(upcomingScreening));
        when(seatRepo.findAllById(seatIds)).thenReturn(List.of(seatA1));
        when(reservationRepo.existsByScreeningAndSeatIdIn(upcomingScreening, seatIds)).thenReturn(true);

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> reservationService.createReservation(
                        new CreateReservationRequest(1L, seatIds, "12345678"))
        );

        assertEquals("Et eller flere af sæderne er allerede reserveret", exception.getMessage());
        verify(reservationRepo, never()).saveAll(anyList());
    }

    private void saveAllReturnsSameList() {
        when(reservationRepo.saveAll(anyList()))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    private void assertBadRequest(CreateReservationRequest request, String expectedMessage) {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> reservationService.createReservation(request)
        );
        assertEquals(expectedMessage, exception.getMessage());
        verify(reservationRepo, never()).saveAll(any());
    }

    private Screen newScreen(Long id, String name, ScreenType type) {
        Screen screen = new Screen(name, type);
        ReflectionTestUtils.setField(screen, "id", id);
        return screen;
    }

    private Screening newScreening(Long id, Screen screen, LocalDateTime startTime) {
        Screening screening = new Screening(null, screen, startTime, startTime.plusHours(2));
        ReflectionTestUtils.setField(screening, "id", id);
        return screening;
    }

    private Seat newSeat(Long id, String row, int seatNumber, Screen screen) {
        Seat seat = new Seat(row, seatNumber, screen);
        ReflectionTestUtils.setField(seat, "id", id);
        return seat;
    }
}