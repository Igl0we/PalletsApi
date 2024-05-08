package lt.javau9.controllers;

import lt.javau9.models.Order;
import lt.javau9.services.OrderService;
import lt.javau9.services.PalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/orders")
public class OrderController {


    private final OrderService orderService;
    private final PalletService palletService;

    @Autowired
    public OrderController(OrderService orderService, PalletService palletService) {
        this.orderService = orderService;
        this.palletService = palletService;
    }

    @GetMapping("/list")
    public String listOrders(Model model) {
        model.addAttribute("orders", orderService.findAll());
        return "ordersList";
    }

    @GetMapping("/create/{palletId}")
    public String createOrderForm(@PathVariable Long palletId, Model model) {
        return palletService.getPalletById(palletId)
                .map(pallet -> {
                    Order order = new Order();
                    order.setPallet(pallet);
                    order.setQuantity(1);
                    order.setTotalPrice(pallet.getPrice() * order.getQuantity());
                    model.addAttribute("order", order);
                    return "orderForm";
                })
                .orElse("redirect:/error-page");
    }


    @GetMapping("/{id}")
    public String viewOrder(@PathVariable Long id, Model model) {
        return orderService.findById(id)
                .map(order -> {
                    model.addAttribute("order", order);
                    return "viewOrder";
                })
                .orElse("redirect:/error-page");
    }

    @PostMapping("/submit")
    public String submitOrder(@ModelAttribute("order") Order order, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "error-page";
        }
        orderService.addOrder(order);
        redirectAttributes.addFlashAttribute("successMessage", "Order successfully created!");
        return "redirect:/orders/" + order.getId();
    }

    @PostMapping("/delete/{id}")
    public String deleteOrder(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (orderService.deleteOrderById(id)) {
            redirectAttributes.addFlashAttribute("successMessage", "Order deleted successfully.");
            return "redirect:/orders/list";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting order.");
            return "redirect:/orders/" + id;
        }
    }

}
