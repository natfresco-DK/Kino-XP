package com.example.KinoXP;

import com.example.KinoXP.dto.CreateKioskSaleRequest;
import com.example.KinoXP.model.KioskProduct;
import com.example.KinoXP.model.KioskSale;
import com.example.KinoXP.repository.KioskProductRepo;
import com.example.KinoXP.repository.KioskSaleRepo;
import com.example.KinoXP.service.KioskSaleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KioskSaleServiceTest {

    @Mock
    private KioskSaleRepo kioskSaleRepo;

    @Mock
    private KioskProductRepo kioskProductRepo;

    @InjectMocks
    private KioskSaleService kioskSaleService;

    @Test
    void createSale_copiesPriceFromProductAndCalculatesTotal() {
        KioskProduct popcorn = new KioskProduct(new BigDecimal("45.00"), "Popcorn");
        when(kioskProductRepo.findById(1L)).thenReturn(Optional.of(popcorn));
        when(kioskSaleRepo.save(any(KioskSale.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        KioskSale sale = kioskSaleService.createSale(new CreateKioskSaleRequest(1L, 2));

        assertEquals("Popcorn", sale.getProduct().getProduct());
        assertEquals(2, sale.getQuantity());
        assertEquals(new BigDecimal("45.00"), sale.getUnitPrice());
        assertEquals(new BigDecimal("90.00"), sale.getTotal());
        assertNotNull(sale.getSaleTime());
    }

    @Test
    void createSale_keepsOldPriceWhenProductPriceChanges() {
        KioskProduct popcorn = new KioskProduct(new BigDecimal("45.00"), "Popcorn");
        when(kioskProductRepo.findById(1L)).thenReturn(Optional.of(popcorn));
        when(kioskSaleRepo.save(any(KioskSale.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        KioskSale sale = kioskSaleService.createSale(new CreateKioskSaleRequest(1L, 2));
        popcorn.setPrice(new BigDecimal("50.00"));

        assertEquals(new BigDecimal("45.00"), sale.getUnitPrice());
        assertEquals(new BigDecimal("90.00"), sale.getTotal());
    }

    @Test
    void createSale_withQuantityZero_throws() {
        assertBadRequest(new CreateKioskSaleRequest(1L, 0), "Antal skal være mindst 1");
    }

    @Test
    void createSale_withNegativeQuantity_throws() {
        assertBadRequest(new CreateKioskSaleRequest(1L, -3), "Antal skal være mindst 1");
    }

    @Test
    void createSale_withoutQuantity_throws() {
        assertBadRequest(new CreateKioskSaleRequest(1L, null), "Antal skal være mindst 1");
    }

    @Test
    void createSale_withoutProductId_throws() {
        assertBadRequest(new CreateKioskSaleRequest(null, 2), "Vælg et produkt");
    }

    @Test
    void createSale_withUnknownProduct_throws() {
        when(kioskProductRepo.findById(999L)).thenReturn(Optional.empty());

        assertBadRequest(new CreateKioskSaleRequest(999L, 2), "Produkt ikke fundet");
    }

    private void assertBadRequest(CreateKioskSaleRequest request, String expectedMessage) {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> kioskSaleService.createSale(request)
        );
        assertEquals(expectedMessage, exception.getMessage());
        verify(kioskSaleRepo, never()).save(any());
    }
}