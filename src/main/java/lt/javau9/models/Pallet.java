package lt.javau9.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Pallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private double price;


    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "pallets",
            joinColumns = @JoinColumn(name = "pallet_id"),
            inverseJoinColumns = @JoinColumn(name = "palletComponent_id")
    )
    List<PalletComponent> components = new ArrayList<>();

    public Pallet() {

    }


    public Pallet(String name) {
        this.name = name;
        this.price = getTotalPrice();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Pallet{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", ingredients=" + components +
                '}';
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTotalPrice() {
        return components.stream().mapToDouble(PalletComponent::getPrice).sum();
    }

    public List<PalletComponent> getComponents() {
        return components;
    }

    public void setComponents(List<PalletComponent> components) {
        this.components = components;
    }

    public Pallet addComponent(PalletComponent palletComponent) {
        palletComponent.addPallet(this);
        components.add(palletComponent);
        return this;
    }
}
