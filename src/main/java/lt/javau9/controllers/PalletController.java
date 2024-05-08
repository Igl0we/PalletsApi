package lt.javau9.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lt.javau9.models.Pallet;
import lt.javau9.models.PalletComponent;
import lt.javau9.models.enums.ComponentType;
import lt.javau9.services.PalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Controller
@RequestMapping("/pallets")
public class PalletController {

    private final PalletService palletService;


    @Autowired
    public PalletController(PalletService palletService) {
        this.palletService = palletService;

    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/all")
    public String listPallets(Model model) {
        model.addAttribute("pallets", palletService.getAllPallets());
        return "pallets";
    }

    @GetMapping("/{id}")
    public String getPalletDetails(@PathVariable Long id, Model model) {
        return palletService.getPalletById(id).map(pallet -> {
            model.addAttribute("pallet", pallet);
            model.addAttribute("nails", palletService.filterComponents(pallet, ComponentType.NAIL));
            model.addAttribute("others", palletService.filterComponents(pallet, null));
            return "palletDetail";
        }).orElse("redirect:/error-page");
    }

    @GetMapping("/pallet-creation")
    public String showCreateForm(Model model) {
        model.addAttribute("componentTypes", ComponentType.values());  // This will allow the form to dynamically generate input fields based on component types.
        return "createPallet";
    }

    @PostMapping("/create")
    public String createPallet(@RequestParam String name, HttpServletRequest request) {
        Pallet newPallet = new Pallet(name);

        Map<String, String[]> params = request.getParameterMap();
        Map<Integer, PalletComponent> components = new HashMap<>();

        params.forEach((key, values) -> {
            if (key.startsWith("componentType-")) {
                int index = Integer.parseInt(key.split("-")[1]);
                String typeString = values[0];

                if (typeString.isEmpty()) {
                    return;
                }

                ComponentType type = ComponentType.valueOf(typeString);
                int amount = Integer.parseInt(params.getOrDefault("amount-" + index, new String[]{"0"})[0]);

                if (type == ComponentType.NAIL) {
                    double size = Double.parseDouble(params.getOrDefault("size-" + index, new String[]{"0"})[0]);
                    double unitPrice = Double.parseDouble(params.getOrDefault("unitPrice-" + index, new String[]{"0"})[0]);

                    PalletComponent component = new PalletComponent(type, amount, size, unitPrice);
                    components.put(index, component);
                } else {
                    double width = Double.parseDouble(params.getOrDefault("width-" + index, new String[]{"0"})[0]);
                    double length = Double.parseDouble(params.getOrDefault("length-" + index, new String[]{"0"})[0]);
                    double height = Double.parseDouble(params.getOrDefault("height-" + index, new String[]{"0"})[0]);
                    double priceM3 = Double.parseDouble(params.getOrDefault("priceM3-" + index, new String[]{"0"})[0]);

                    PalletComponent component = new PalletComponent(type, amount, width, length, height, priceM3);
                    components.put(index, component);
                }
            }
        });

        components.values().forEach(newPallet::addComponent);
        newPallet.getTotalPrice();
        palletService.addPallet(newPallet);

        return "redirect:/pallets/all";
    }


    @GetMapping("/edit/{id}")
    public String editPalletForm(@PathVariable Long id, Model model) {
        Optional<Pallet> pallet = palletService.getPalletById(id);
        model.addAttribute("pallet", pallet.orElse(new Pallet()));
        return "palletEdit";
    }

    @Transactional
    @PostMapping("/update/{id}")
    public String updatePallet(@PathVariable Long id, @ModelAttribute Pallet pallet) {
        palletService.updatePallet(id, pallet);
        return "redirect:/pallets/all";
    }


    @PostMapping("/delete/{id}")
    public String deletePallet(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (palletService.deletePalletById(id)) {
            redirectAttributes.addFlashAttribute("successMessage", "Pallet deleted successfully.");
            return "redirect:/pallets/all";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting pallet.");
            return "redirect:/pallets/" + id;
        }
    }


    @GetMapping("/{palletId}/components/{componentId}/delete")
    public String deleteComponent(@PathVariable Long palletId, @PathVariable Long componentId) {
        boolean removed = palletService.removeComponentFromPallet(palletId, componentId);
        if (removed) {
            return "redirect:/pallets/" + palletId;
        } else {
            return "redirect:/error-page";
        }
    }
}


