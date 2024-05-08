package lt.javau9.services.impl;

import lt.javau9.models.PalletComponent;
import lt.javau9.models.enums.ComponentType;
import lt.javau9.repositories.PalletComponentRepository;
import lt.javau9.services.PalletComponentService;
import lt.javau9.services.PalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Service
public class PalletComponentServiceImpl implements PalletComponentService {

    PalletComponentRepository palletComponentDatabase;
    PalletService palletService;

    @Autowired
    public PalletComponentServiceImpl(PalletComponentRepository palletComponentDatabase, PalletService palletService) {
        this.palletComponentDatabase = palletComponentDatabase;
        this.palletService = palletService;
    }

    @Override
    public boolean deleteComponentById(Long id) {
        if (!palletComponentDatabase.existsById(id)) return false;
        palletComponentDatabase.deleteById(id);
        return true;
    }

    @Override
    public Collection<PalletComponent> getAllPalletComponents() {
        return palletComponentDatabase.findAll();
    }


    @Override
    public Optional<PalletComponent> getPalletComponentById(Long id) {
        return palletComponentDatabase.findById(id);
    }

    @Override
    public Optional<PalletComponent> updatePalletComponent(Long id, PalletComponent updatedComponent) {
        return palletComponentDatabase.findById(id).map(existingComponent -> {
            existingComponent.setAmount(updatedComponent.getAmount());
            existingComponent.setPriceM3(updatedComponent.getPriceM3());
            existingComponent.setUnitPrice(updatedComponent.getUnitPrice());
            existingComponent.setSize(updatedComponent.getSize());

            recalculateComponentPrice(existingComponent);

            PalletComponent savedComponent = palletComponentDatabase.save(existingComponent);

            palletService.updatePalletPrice(savedComponent.getPallets().stream().findFirst().orElse(null));

            return savedComponent;
        });
    }

    private void recalculateComponentPrice(PalletComponent component) {
        if (component.getComponentType() == ComponentType.NAIL) {
            component.setPrice(component.getUnitPrice() * component.getAmount());
        } else {
            double volume = component.getWidth() * component.getLength() * component.getHeight() * component.getAmount();
            component.setPrice(volume * component.getPriceM3());
        }
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
