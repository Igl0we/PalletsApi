package lt.javau9.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;

import java.time.LocalDate;

@Entity
@Table(name = "`order`")  // Note the use of backticks to quote the table name
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String customerName;
    private String customerAddress;

    private LocalDate orderDate;
    private LocalDate expiryDate;

    @OneToOne  // Assuming each order has one pallet, but one pallet can be in many orders
    @JoinColumn(name = "pallet_id") // This creates a column in the Order table for the pallet ID
    private Pallet pallet;
    @Min(value = 1, message = "Quantity must be at least 1")
    private double quantity;
    private double totalPrice;

    public Order() {
        this.orderDate = LocalDate.now();
        this.expiryDate = this.orderDate.plusWeeks(2);
    }

    public Order(String customerName, Pallet pallet, int quantity, String customerAddress) {
        this.customerAddress = customerAddress;
        this.customerName = customerName;
        this.orderDate = LocalDate.now();
        this.expiryDate = this.orderDate.plusWeeks(2);
        this.pallet = pallet;
        this.quantity = quantity;
        this.totalPrice = calculateTotalPrice();
    }

    private double calculateTotalPrice() {
        return this.pallet.getPrice() * this.quantity;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public Pallet getPallet() {
        return pallet;
    }

    public void setPallet(Pallet pallet) {
        this.pallet = pallet;
        this.totalPrice = calculateTotalPrice();
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
        this.totalPrice = calculateTotalPrice();
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }
}