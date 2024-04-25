package lt.javau9.services.impl;

import lt.javau9.models.Pallet;
import lt.javau9.models.PalletComponent;
import lt.javau9.repositories.PalletRepository;
import lt.javau9.services.PalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class PalletServiceImpl implements PalletService {

    PalletRepository palletDatabase;


    @Autowired
    public PalletServiceImpl(PalletRepository palletDatabase) {
        this.palletDatabase = palletDatabase;
    }

    @Override
    public Collection<Pallet> getAllPallets() {
        return palletDatabase.findAll();
    }

    @Override
    public Pallet addPallet(Pallet pallet) {
        return palletDatabase.save(pallet);
    }

    @Override
    public double calculatePalletPrice(Long palletId) {
        Pallet pallet = palletDatabase.findById(palletId).orElse(null);
        if (pallet == null) {
            throw new RuntimeException("Pallet doesn't exist");
        }
        double totalPrice = 0;
        for (PalletComponent palletComponent : pallet.getComponents()) {
            totalPrice += palletComponent.getPrice();
        }
        return totalPrice;
    }

    @Override
    public Optional<Pallet> getPalletById(Long id) {
        return palletDatabase.findById(id);
    }

    @Override
    public Optional<Pallet> updatePallet(Long id, Pallet pallet) {
        Optional<Pallet> existingPallet = palletDatabase.findById(id);
        if (existingPallet.isEmpty())
            return Optional.empty();
        Pallet updatedPallet = existingPallet.get();
        updatedPallet.setName(pallet.getName());

        return Optional.of(palletDatabase.save(updatedPallet));
    }

    @Override
    public boolean deletePalletById(Long id) {
        if (palletDatabase.existsById(id)) {
            palletDatabase.deleteById(id);
            return true;
        }

        return false;
    }
}


