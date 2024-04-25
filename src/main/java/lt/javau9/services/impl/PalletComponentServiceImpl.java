package lt.javau9.services.impl;

import lt.javau9.models.PalletComponent;
import lt.javau9.repositories.PalletComponentRepository;
import lt.javau9.services.PalletComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

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
}
