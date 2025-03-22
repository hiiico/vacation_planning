package app.web;

import app.contract.model.Contract;
import app.contract.service.ContractService;
import org.hibernate.mapping.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/contracts")
public class ContractController {

    @Autowired
    private ContractService contractService;

    @GetMapping("/user/{userId}")
    public String listContractsByUser(@PathVariable UUID userId, Model model) {
        model.addAttribute("contracts", contractService.getContractsByUserId(userId));
        model.addAttribute("userId", userId);
        return "contracts/list";
    }

    @GetMapping("/create/{userId}")
    public String showCreateForm(@PathVariable UUID userId, Model model) {
        model.addAttribute("contract", new Contract());
        model.addAttribute("userId", userId);
        return "contracts/create";
    }

//    @PostMapping("/create/{userId}")
//    public String createContract(@PathVariable UUID userId, @ModelAttribute Contract contract) {
//        contractService.createContract(userId, contract);
//        return "redirect:/contracts/user/" + userId;
//    }

    @GetMapping("/delete/{id}")
    public String deleteContract(@PathVariable UUID id) {
        List userId = contractService.getContractsByUserId(id);
        contractService.deleteContract(id);
        return "redirect:/contracts/user/" + userId;
    }
}