package lt.javau9.controllers.api;

import lt.javau9.models.Order;
import lt.javau9.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class ApiOrderController {

    private final OrderService orderService;

    @Autowired
    public ApiOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Add a new order
    @PostMapping
    public ResponseEntity<Order> addOrder(@RequestBody Order order) {
        try {
            Order savedOrder = orderService.addOrder(order);
            return ResponseEntity.ok(savedOrder);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Get all orders
    @GetMapping
    public ResponseEntity<Collection<Order>> findAllOrders() {
        return ResponseEntity.ok(orderService.findAll());
    }

    // Get a specific order by ID
    @GetMapping("/{id}")
    public ResponseEntity<Order> findOrderById(@PathVariable Long id) {
        Optional<Order> order = orderService.findById(id);
        return order.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete an order by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderById(@PathVariable Long id) {
        boolean isDeleted = orderService.deleteOrderById(id);
        if (isDeleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
