package lt.javau9.services;

import lt.javau9.models.Pallet;
import lt.javau9.models.PalletComponent;
import lt.javau9.models.enums.ComponentType;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface PalletService {

    Collection<Pallet> getAllPallets();

    Pallet addPallet(Pallet pallet);

    double calculatePalletPrice(Long palletId);

    Optional<Pallet> getPalletById(Long id);

    Optional<Pallet> updatePallet(Long id, Pallet pallet);

    boolean deletePalletById(Long id);

    boolean removeComponentFromPallet(Long palletId, Long componentId);

    public void updatePalletPrice(Pallet pallet);

    List<PalletComponent> filterComponents(Pallet pallet, ComponentType type);


}
