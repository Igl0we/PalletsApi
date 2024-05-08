package lt.javau9.palletsApi;

import lt.javau9.models.PalletComponent;
import lt.javau9.models.enums.ComponentType;
import lt.javau9.repositories.PalletComponentRepository;
import lt.javau9.services.PalletService;
import lt.javau9.services.impl.PalletComponentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PalletComponentServiceImplTest {

    @Mock
    private PalletComponentRepository palletComponentRepository;

    @Mock
    private PalletService palletService;

    @InjectMocks
    private PalletComponentServiceImpl palletComponentService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDeleteComponentById_Success() {
        when(palletComponentRepository.existsById(1L)).thenReturn(true);
        doNothing().when(palletComponentRepository).deleteById(1L);

        boolean result = palletComponentService.deleteComponentById(1L);

        assertTrue(result);
        verify(palletComponentRepository).deleteById(1L);
    }

    @Test
    public void testDeleteComponentById_Failure() {
        when(palletComponentRepository.existsById(1L)).thenReturn(false);

        boolean result = palletComponentService.deleteComponentById(1L);

        assertFalse(result);
        verify(palletComponentRepository, never()).deleteById(1L);
    }

    @Test
    public void testGetAllPalletComponents() {
        List<PalletComponent> expectedComponents = Arrays.asList(new PalletComponent(), new PalletComponent());
        when(palletComponentRepository.findAll()).thenReturn(expectedComponents);

        Collection<PalletComponent> components = palletComponentService.getAllPalletComponents();

        assertNotNull(components);
        assertEquals(2, components.size());
        verify(palletComponentRepository).findAll();
    }

    @Test
    public void testGetPalletComponentById() {
        PalletComponent expectedComponent = new PalletComponent();
        when(palletComponentRepository.findById(1L)).thenReturn(Optional.of(expectedComponent));

        Optional<PalletComponent> component = palletComponentService.getPalletComponentById(1L);

        assertTrue(component.isPresent());
        assertEquals(expectedComponent, component.get());
    }

    @Test
    public void testUpdatePalletComponent() {
        PalletComponent existingComponent = new PalletComponent();
        existingComponent.setId(1L);
        existingComponent.setComponentType(ComponentType.NAIL);
        existingComponent.setUnitPrice(5.0);
        existingComponent.setAmount(100);

        PalletComponent updatedComponent = new PalletComponent();
        updatedComponent.setAmount(150);
        updatedComponent.setUnitPrice(5.0);

        when(palletComponentRepository.findById(1L)).thenReturn(Optional.of(existingComponent));
        when(palletComponentRepository.save(any(PalletComponent.class))).thenReturn(existingComponent);

        Optional<PalletComponent> result = palletComponentService.updatePalletComponent(1L, updatedComponent);

        assertTrue(result.isPresent());
        assertEquals(150, result.get().getAmount());
        verify(palletComponentRepository).save(existingComponent);
    }

    @Test
    public void testCreateComponentFromParams_NailType() {
        PalletComponent result = palletComponentService.createComponentFromParams(ComponentType.NAIL, 100, Map.of("size", "55", "unitPrice", "2.5"));

        assertNotNull(result);
        assertEquals(ComponentType.NAIL, result.getComponentType());
        assertEquals(55, result.getSize());
        assertEquals(2.5, result.getUnitPrice());
    }

    @Test
    public void testCreateComponentFromParams_NonNailType() {
        PalletComponent result = palletComponentService.createComponentFromParams(ComponentType.BEAM, 50, Map.of("width", "200", "length", "100", "height", "10", "priceM3", "3.0"));

        assertNotNull(result);
        assertEquals(ComponentType.BEAM, result.getComponentType());
        assertEquals(0.2, result.getWidth());  // Divided by 1000 as per your constructor logic
        assertEquals(0.1, result.getLength());
        assertEquals(0.01, result.getHeight());
        assertEquals(3.0, result.getPriceM3());
    }
}
