package lt.javau9.controllers;

import lt.javau9.models.PalletComponent;
import lt.javau9.services.PalletComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping
public class PalletComponentController {

    private final PalletComponentService palletComponentService;

    @Autowired
    public PalletComponentController(PalletComponentService palletComponentService) {
        this.palletComponentService = palletComponentService;
    }


    @PostMapping("/pallets/{palletId}/components/{componentId}/update")
    public String updatePalletComponent(@PathVariable Long palletId, @PathVariable Long componentId, @ModelAttribute PalletComponent component) {
        palletComponentService.updatePalletComponent(componentId, component);
        return "redirect:/pallets/" + palletId;
    }

    @GetMapping("/pallets/{palletId}/components/{componentId}/edit")
    public String editPalletComponent(@PathVariable Long palletId, @PathVariable Long componentId, Model model) {
        Optional<PalletComponent> component = palletComponentService.getPalletComponentById(componentId);
        if (component.isEmpty()) {
            return "redirect:/pallets/" + palletId;
        }
        model.addAttribute("component", component.get());
        model.addAttribute("palletId", palletId);
        return "editComponent";
    }
}
