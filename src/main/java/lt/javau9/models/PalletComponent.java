package lt.javau9.models;

import jakarta.persistence.*;
import lt.javau9.models.enums.ComponentType;

import java.util.ArrayList;
import java.util.List;

@Entity
public class PalletComponent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    int amount;
    ComponentType componentType;
    double width;
    double length;
    double height;
    double priceM3;
    double unitPrice;
    double price;
    double size;


    @ManyToMany(mappedBy = "components")
    List<Pallet> pallets = new ArrayList<>();

    public PalletComponent() {
    }

    public PalletComponent(ComponentType componentType, int amount, double width, double length, double height, double price) {
        this.componentType = componentType;
        this.amount = amount;
        this.width = width / 1000;
        this.length = length / 1000;
        this.height = height / 1000;
        this.priceM3 = price;
        this.price = amount * calculatePrice();
    }

    public PalletComponent(ComponentType componentType, int amount, double size, double unitPrice) {

        this.componentType = componentType;
        this.size = size;
        this.amount = amount;
        this.unitPrice = price;
        this.price = amount * unitPrice / 1000;

    }

    public void addPallet(Pallet pallet) {
        pallets.add(pallet);
    }


    public double getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public double getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public ComponentType getComponentType() {
        return componentType;
    }

    public void setComponentType(ComponentType componentType) {
        this.componentType = componentType;
    }


    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public double getPriceM3() {
        return priceM3;
    }

    public void setPriceM3(double priceM3) {
        this.priceM3 = priceM3;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    private double calculatePrice() {
        return (width * length * height) * priceM3;
    }
}
