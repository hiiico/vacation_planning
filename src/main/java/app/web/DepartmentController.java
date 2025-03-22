package app.web;

import app.department.model.Department;
import app.department.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping
    public String listDepartments(Model model) {
        model.addAttribute("departments", departmentService.getAllDepartments());
        return "departments/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("department", new Department());
        return "departments/create";
    }

    @PostMapping("/create")
    public String createDepartment(@ModelAttribute Department department) {
        departmentService.createDepartment(department.getName());
        return "redirect:/departments";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable UUID id, Model model) {
        model.addAttribute("department", departmentService.getDepartmentById(id).orElseThrow());
        return "departments/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateDepartment(@PathVariable UUID id, @ModelAttribute Department department) {
        Department existingDepartment = departmentService.getDepartmentById(id).orElseThrow();
        existingDepartment.setName(department.getName());
        departmentService.createDepartment(existingDepartment.getName());
        return "redirect:/departments";
    }

    @GetMapping("/delete/{id}")
    public String deleteDepartment(@PathVariable UUID id) {
        departmentService.deleteDepartment(id);
        return "redirect:/departments";
    }
}