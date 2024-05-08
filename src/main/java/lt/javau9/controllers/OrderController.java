package lt.javau9.controllers;

import lt.javau9.models.Order;
import lt.javau9.models.Pallet;
import lt.javau9.services.OrderService;
import lt.javau9.services.PalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collection;
import java.util.Optional;

@Controller
@RequestMapping("/orders")
public class OrderController {


    private final OrderService orderService;
    private final PalletService palletService;// Assume an OrderService is available for handling business logic

    @Autowired
    public OrderController(OrderService orderService, PalletService palletService) {
        this.orderService = orderService;
        this.palletService = palletService;
    }

    @GetMapping("/list")
    public String listOrders(Model model) {
        Collection<Order> orders = orderService.findAll();
        model.addAttribute("orders", orders);
        return "ordersList";
    }

    @GetMapping("/create/{palletId}")
    public String createOrderForm(@PathVariable Long palletId, Model model) {
        Order order = new Order();
        if (palletId != null) {
            Optional<Pallet> palletOptional = palletService.getPalletById(palletId); // Assuming you have a service to find a pallet
            if (palletOptional.isPresent()) {
                Pallet pallet = palletOptional.get();
                order.setPallet(pallet);
                order.setQuantity(1); // Default quantity can be set as 1 or adjusted as needed
                order.setTotalPrice(pallet.getPrice() * order.getQuantity()); // Set initial total price based on pallet price and quantity
            } else {
                return "redirect:/error-page"; // Redirect to an error page if pallet is not found
            }
        } else {
            return "redirect:/error-page"; // Redirect if no palletId is provided
        }

        model.addAttribute("order", order);
        return "orderForm"; // Name of the HTML file for creating orders
    }

    //@PostMapping
    //public String createOrder(@ModelAttribute Order order, RedirectAttributes redirectAttributes) {
    //    Order savedOrder = orderService.addOrder(order);
    //    redirectAttributes.addFlashAttribute("successMessage", "Order successfully created!");
    //    return "redirect:/orders";
    //}

    @GetMapping("/{id}")
    public String viewOrder(@PathVariable Long id, Model model) {
        Optional<Order> orderOptional = orderService.findById(id);
        if (orderOptional.isPresent()) {
            model.addAttribute("order", orderOptional.get());
        } else {
            return "redirect:/error-page"; // Or some error handling
        }
        return "viewOrder";
    }

    @PostMapping("/submit")
    public String submitOrder(@ModelAttribute("order") Order order, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "error-page";
        }
        orderService.addOrder(order);
        redirectAttributes.addFlashAttribute("successMessage", "Order successfully created!");
        return "redirect:/orders/{id}";
    }

    @PostMapping("/delete/{id}")
    public String deleteOrder(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            orderService.deleteOrderById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Order deleted successfully.");
            return "redirect:/orders/all"; // Redirect to the list of pallets after deletion
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting order.");
            return "redirect:/orders/{id}"; // Redirect back to the edit page if there's an error
        }
    }

}
