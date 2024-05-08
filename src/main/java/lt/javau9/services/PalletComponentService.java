package lt.javau9.services;

import lt.javau9.models.PalletComponent;
import lt.javau9.models.enums.ComponentType;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public interface PalletComponentService {

    boolean deleteComponentById(Long id);

    Collection<PalletComponent> getAllPalletComponents();

    Optional<PalletComponent> getPalletComponentById(Long id);

    Optional<PalletComponent> updatePalletComponent(Long id, PalletComponent palletComponent);

    PalletComponent createComponentFromParams(ComponentType type, int amount, Map<String, String> params);


}