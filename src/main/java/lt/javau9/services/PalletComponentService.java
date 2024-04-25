package lt.javau9.services;

import lt.javau9.models.PalletComponent;

import java.util.Collection;

public interface PalletComponentService {

    boolean deleteComponentById(Long id);

    Collection<PalletComponent> getAllPalletComponents();
}
