package lt.javau9.services;

import lt.javau9.models.Pallet;

import java.util.Collection;
import java.util.Optional;

public interface PalletService {

    Collection<Pallet> getAllPallets();

    Pallet addPallet(Pallet pallet);

    double calculatePalletPrice(Long palletId);

    Optional<Pallet> getPalletById(Long id);

    Optional<Pallet> updatePallet(Long id, Pallet pallet);

    boolean deletePalletById(Long id);
}
