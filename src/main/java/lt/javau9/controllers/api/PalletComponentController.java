package lt.javau9.controllers.api;

import lt.javau9.models.Pallet;
import lt.javau9.models.PalletComponent;
import lt.javau9.services.PalletComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/component")
public class PalletComponentController {


    PalletComponentService palletComponentService;

    @Autowired
    public PalletComponentController(PalletComponentService palletComponentService) {
        this.palletComponentService = palletComponentService;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Pallet> deleteComponent(@PathVariable Long id) {
        return palletComponentService.deleteComponentById(id) ?
                ResponseEntity.ok().build() :
                ResponseEntity.notFound().build();
    }

    @GetMapping("/all")
    public Collection<PalletComponent> getAllPalletComponents() {
        return palletComponentService.getAllPalletComponents();
    }
}
