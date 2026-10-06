package com.example.KinoXP;

import com.example.KinoXP.dto.KioskProductRequest;
import com.example.KinoXP.model.KioskProduct;
import com.example.KinoXP.repository.KioskProductRepo;
import com.example.KinoXP.service.KioskProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KioskProductServiceTest {

    @Mock
    private KioskProductRepo kioskProductRepo;

    @InjectMocks
    private KioskProductService kioskProductService;

    @Test
    void createProduct_savesTrimmedNameAndPrice() {
        when(kioskProductRepo.existsByProductIgnoreCase("Popcorn")).thenReturn(false);
        when(kioskProductRepo.save(any(KioskProduct.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        KioskProduct product = kioskProductService.createProduct(
                new KioskProductRequest("  Popcorn  ", new BigDecimal("45.00")));

        assertEquals("Popcorn", product.getProduct());
        assertEquals(new BigDecimal("45.00"), product.getPrice());
    }

    @Test
    void createProduct_duplicateName_throws() {
        when(kioskProductRepo.existsByProductIgnoreCase("Popcorn")).thenReturn(true);

        assertBadRequest(new KioskProductRequest("Popcorn", new BigDecimal("50.00")),
                "Produktet findes allerede");
    }

    @Test
    void createProduct_blankName_throws() {
        assertBadRequest(new KioskProductRequest("   ", new BigDecimal("45.00")),
                "Tilføj et produktnavn");
    }

    @Test
    void createProduct_withoutName_throws() {
        assertBadRequest(new KioskProductRequest(null, new BigDecimal("45.00")),
                "Tilføj et produktnavn");
    }

    @Test
    void createProduct_priceZero_throws() {
        assertBadRequest(new KioskProductRequest("Cola", BigDecimal.ZERO),
                "Prisen skal være større end 0");
    }

    @Test
    void createProduct_negativePrice_throws() {
        assertBadRequest(new KioskProductRequest("Cola", new BigDecimal("-10")),
                "Prisen skal være større end 0");
    }

    @Test
    void createProduct_withoutPrice_throws() {
        assertBadRequest(new KioskProductRequest("Cola", null),
                "Prisen skal være større end 0");
    }

    @Test
    void createProduct_priceWithThreeDecimals_throws() {
        assertBadRequest(new KioskProductRequest("Cola", new BigDecimal("29.995")),
                "Prisen må højst have 2 decimaler");
    }

    private void assertBadRequest(KioskProductRequest request, String expectedMessage) {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> kioskProductService.createProduct(request)
        );
        assertEquals(expectedMessage, exception.getMessage());
        verify(kioskProductRepo, never()).save(any());
    }
}