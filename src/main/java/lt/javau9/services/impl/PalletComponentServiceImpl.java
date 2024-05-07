package lt.javau9.services.impl;

import lt.javau9.models.PalletComponent;
import lt.javau9.models.enums.ComponentType;
import lt.javau9.repositories.PalletComponentRepository;
import lt.javau9.services.PalletComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Service
public class PalletComponentServiceImpl implements PalletComponentService {

    PalletComponentRepository palletComponentDatabase;

    @Autowired
    public PalletComponentServiceImpl(PalletComponentRepository palletComponentDatabase) {
        this.palletComponentDatabase = palletComponentDatabase;
    }

    @Override
    public boolean deleteComponentById(Long id) {
        if (palletComponentDatabase.existsById(id)) {
            palletComponentDatabase.deleteById(id);
            return true;
        }

        return false;

    }

    @Override
    public Collection<PalletComponent> getAllPalletComponents() {
        if (palletComponentDatabase != null) {
            return palletComponentDatabase.findAll();
        } else {
            System.out.println("palletComponentDatabase is null");
            return Collections.emptyList();
        }
    }

    @Override
    public Optional<PalletComponent> getPalletComponentById(Long id) {
        return palletComponentDatabase.findById(id);
    }

    @Override
    public Optional<PalletComponent> updatePalletComponent(Long id, PalletComponent updatedComponent) {
        Optional<PalletComponent> existingComponent = palletComponentDatabase.findById(id);
        if (existingComponent.isPresent()) {
            PalletComponent component = existingComponent.get();
            // Set fields that you allow to be updated
            component.setAmount(updatedComponent.getAmount());
            component.setPriceM3(updatedComponent.getPriceM3());
            component.setUnitPrice(updatedComponent.getUnitPrice());
            // Calculate the price if it's dependent on other fields
            component.setPrice(updatedComponent.getAmount() * updatedComponent.getUnitPrice()); // Simplified example

            // Save the updated component
            return Optional.of(palletComponentDatabase.save(component));
        }
        return Optional.empty();
    }

    @Override
    public PalletComponent createComponentFromParams(ComponentType type, int amount, Map<String, String> params) {
        if (type == ComponentType.NAIL) {
            double size = Double.parseDouble(params.getOrDefault("size", "1"));
            double unitPrice = Double.parseDouble(params.getOrDefault("unitPrice", "1"));
            return new PalletComponent(type, amount, size, unitPrice);
        } else {
            double width = Double.parseDouble(params.getOrDefault("width", "1"));
            double length = Double.parseDouble(params.getOrDefault("length", "1"));
            double height = Double.parseDouble(params.getOrDefault("height", "1"));
            double priceM3 = Double.parseDouble(params.getOrDefault("priceM3", "1"));
            return new PalletComponent(type, amount, width, length, height, priceM3);
        }
    }
}
