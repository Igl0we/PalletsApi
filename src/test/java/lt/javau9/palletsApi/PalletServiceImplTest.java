package lt.javau9.palletsApi;

import lt.javau9.models.Pallet;
import lt.javau9.repositories.PalletRepository;
import lt.javau9.services.impl.PalletServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PalletServiceImplTest {

    @Mock
    private PalletRepository palletRepository;

    @InjectMocks
    private PalletServiceImpl palletService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getPalletById_found() {
        Pallet pallet = new Pallet();
        pallet.setId(1L);
        when(palletRepository.findById(1L)).thenReturn(Optional.of(pallet));

        Optional<Pallet> found = palletService.getPalletById(1L);

        assertTrue(found.isPresent());
        assertEquals(1L, found.get().getId());
        verify(palletRepository).findById(1L);
    }

    @Test
    void getPalletById_notFound() {
        when(palletRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Pallet> found = palletService.getPalletById(1L);

        assertFalse(found.isPresent());
        verify(palletRepository).findById(1L);
    }
}