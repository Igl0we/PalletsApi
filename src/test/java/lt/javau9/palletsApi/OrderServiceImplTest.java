package lt.javau9.palletsApi;

import lt.javau9.models.Order;
import lt.javau9.models.Pallet;
import lt.javau9.repositories.OrderRepository;
import lt.javau9.repositories.PalletRepository;
import lt.javau9.services.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PalletRepository palletRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddOrder_ThrowsExceptionWhenPalletIsNull() {
        Order order = new Order();
        order.setPallet(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.addOrder(order);
        });

        assertEquals("Pallet is required to create an order.", exception.getMessage());
    }

    @Test
    public void testAddOrder_SuccessfulCreation() {
        Order order = new Order();
        Pallet pallet = new Pallet();
        pallet.setId(1L);
        pallet.setPrice(100.0);
        order.setPallet(pallet);
        order.setQuantity(2);
        order.setOrderDate(LocalDate.now());

        when(palletRepository.findById(anyLong())).thenReturn(Optional.of(pallet));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order savedOrder = orderService.addOrder(order);

        assertNotNull(savedOrder);
        verify(orderRepository).save(order);
        assertEquals(200.0, savedOrder.getTotalPrice());
    }

    @Test
    public void testFindAll() {
        List<Order> orders = Arrays.asList(new Order(), new Order());
        when(orderRepository.findAll()).thenReturn(orders);

        List<Order> result = orderService.findAll();

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testFindById() {
        Order order = new Order();
        order.setId(1L);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Optional<Order> result = orderService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    public void testDeleteOrderById_Success() {
        when(orderRepository.existsById(1L)).thenReturn(true);
        doNothing().when(orderRepository).deleteById(1L);

        boolean result = orderService.deleteOrderById(1L);

        assertTrue(result);
        verify(orderRepository).deleteById(1L);
    }

    @Test
    public void testDeleteOrderById_Failure() {
        when(orderRepository.existsById(1L)).thenReturn(false);

        boolean result = orderService.deleteOrderById(1L);

        assertFalse(result);
        verify(orderRepository, never()).deleteById(anyLong());
    }
}