package lt.javau9.services;

import lt.javau9.models.Order;

import java.util.Collection;
import java.util.Optional;

public interface OrderService {

    Order addOrder(Order order);

    Collection<Order> findAll();

    Optional<Order> findById(Long id);

    boolean deleteOrderById(Long id);
}