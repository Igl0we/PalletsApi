package lt.javau9.controllers.api;

import lt.javau9.models.Pallet;
import lt.javau9.services.PalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api")
public class PalletController {

    private final PalletService palletService;

    @Autowired
    public PalletController(PalletService palletService) {
        this.palletService = palletService;
    }

    @GetMapping("/all")
    public Collection<Pallet> getAllPallets() {
        return palletService.getAllPallets();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pallet> getPalletById(@PathVariable Long id) {
        return ResponseEntity.of(palletService.getPalletById(id));
    }


    @PostMapping("/add")
    public ResponseEntity<Pallet> createPallet(@RequestBody Pallet pallet) {
        Pallet savedPallet = palletService.addPallet(pallet);
        double totalPrice = palletService.calculatePalletPrice(savedPallet.getId());
        savedPallet.setPrice(totalPrice);
        return ResponseEntity.ok(savedPallet);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pallet> updatePallet(@PathVariable Long id, @RequestBody Pallet pallet) {
        return ResponseEntity.of(palletService.updatePallet(id, pallet));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Pallet> deletePallet(@PathVariable Long id) {
        return palletService.deletePalletById(id) ?
                ResponseEntity.ok().build() :
                ResponseEntity.notFound().build();
    }
    //@PostMapping("/update/{id}")
    //public ResponseEntity<Pallet> updatePrice (@PathVariable Long id, @RequestBody Pallet pallet) {
    //    return
    //}


}
