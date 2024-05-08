package lt.javau9.services.impl;

import jakarta.transaction.Transactional;
import lt.javau9.models.Order;
import lt.javau9.models.Pallet;
import lt.javau9.repositories.OrderRepository;
import lt.javau9.repositories.PalletRepository;
import lt.javau9.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final PalletRepository palletRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, PalletRepository palletRepository) {
        this.orderRepository = orderRepository;
        this.palletRepository = palletRepository;
    }

    @Override
    public Order addOrder(Order order) {
        if (order.getPallet() == null || order.getPallet().getId() == null) {
            throw new IllegalArgumentException("Pallet is required to create an order.");
        }

        Pallet pallet = palletRepository.findById(order.getPallet().getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid pallet ID: " + order.getPallet().getId()));

        order.setPallet(pallet);
        order.setTotalPrice(pallet.getPrice() * order.getQuantity());

        if (order.getOrderDate() == null) {
            order.setOrderDate(LocalDate.now());
            order.setExpiryDate(order.getOrderDate().plusWeeks(2));
        }

        return orderRepository.save(order);
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public boolean deleteOrderById(Long id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
            return true;
        }

        return false;
    }
}
