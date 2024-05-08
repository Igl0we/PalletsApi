package lt.javau9.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lt.javau9.models.Pallet;
import lt.javau9.models.PalletComponent;
import lt.javau9.models.enums.ComponentType;
import lt.javau9.services.PalletComponentService;
import lt.javau9.services.PalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/pallets")
public class PalletController {
    private static final Logger log = LoggerFactory.getLogger(PalletController.class);

    private final PalletService palletService;
    private final PalletComponentService palletComponentService;


    @Autowired
    public PalletController(PalletService palletService, PalletComponentService palletComponentService) {
        this.palletService = palletService;
        this.palletComponentService = palletComponentService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
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

    @GetMapping("/all")
    public String listPallets(Model model) {
        try {
            Collection<Pallet> pallets = palletService.getAllPallets();
            model.addAttribute("pallets", pallets);
            return "pallets";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error accessing database: " + e.getMessage());
            return "error-page";
        }
    }

    @GetMapping("/{id}")
    public String getPalletDetails(@PathVariable Long id, Model model) {
        Pallet pallet = palletService.getPalletById(id).orElseThrow(() -> new IllegalArgumentException("Invalid pallet Id:" + id));
        List<PalletComponent> nails = pallet.getComponents().stream()
                .filter(c -> "NAIL".equalsIgnoreCase(c.getComponentType().name()))
                .collect(Collectors.toList());
        List<PalletComponent> others = pallet.getComponents().stream()
                .filter(c -> !"NAIL".equalsIgnoreCase(c.getComponentType().name()))
                .collect(Collectors.toList());

        model.addAttribute("pallet", pallet);
        model.addAttribute("nails", nails);
        model.addAttribute("others", others);
        return "palletDetail";
    }

    @GetMapping("/edit/{id}")
    public String editPalletForm(@PathVariable Long id, Model model) {
        Optional<Pallet> pallet = palletService.getPalletById(id);
        model.addAttribute("pallet", pallet.orElse(new Pallet()));
        return "palletEdit"; // Create 'palletEdit.html'
    }

    @Transactional
    @PostMapping("/update/{id}")
    public String updatePallet(@PathVariable Long id, @ModelAttribute Pallet pallet) {
        palletService.updatePallet(id, pallet);
        return "redirect:/pallets/all";
    }

    @GetMapping("/{palletId}/components/{componentId}/edit")
    public String editPalletComponent(@PathVariable Long palletId, @PathVariable Long componentId, Model model) {
        Optional<PalletComponent> component = palletComponentService.getPalletComponentById(componentId);
        if (component.isEmpty()) {
            return "redirect:/pallets/" + palletId;
        }
        model.addAttribute("component", component.get());
        model.addAttribute("palletId", palletId);
        return "editComponent";
    }

    @PostMapping("/{palletId}/components/{componentId}/update")
    public String updatePalletComponent(@PathVariable Long palletId, @PathVariable Long componentId, @ModelAttribute PalletComponent component) {
        palletComponentService.updatePalletComponent(componentId, component);
        return "redirect:/pallets/" + palletId;
    }

    @PostMapping("/delete/{id}")
    public String deletePallet(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            palletService.deletePalletById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Pallet deleted successfully.");
            return "redirect:/pallets/all"; // Redirect to the list of pallets after deletion
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting pallet.");
            return "redirect:/pallets/edit/{id}"; // Redirect back to the edit page if there's an error
        }
    }


    @GetMapping("/{palletId}/components/{componentId}/delete")
    public String deleteComponent(@PathVariable Long palletId, @PathVariable Long componentId) {
        boolean removed = palletService.removeComponentFromPallet(palletId, componentId);
        if (removed) {
            return "redirect:/pallets/" + palletId; // Redirect to the pallet details page
        } else {
            return "redirect:/error-page"; // Or handle this more specifically if required
        }
    }
}


