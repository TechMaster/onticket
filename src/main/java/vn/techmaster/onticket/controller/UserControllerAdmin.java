package vn.techmaster.onticket.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import vn.techmaster.onticket.model.User;
import vn.techmaster.onticket.service.UserService;

@Controller
@RequestMapping("/admin/users")
public class UserControllerAdmin {
  @Autowired
  private UserService userService;

  @GetMapping
  public String listUsers(Model model) {
    List<User> users = userService.getAllUsers();
    model.addAttribute("users", users);
    return "admin/users/list";
  }

  @GetMapping("/new")
  public String showNewUserForm(Model model) {
    model.addAttribute("user", new User());
    return "admin/users/new";
  }

  @PostMapping("/new")
  public String submitNewUserForm(@ModelAttribute("user") @Valid User user, BindingResult result) {
    if (result.hasErrors()) {
      return "admin/users/new";
    }
    userService.registerUser(user);
    return "redirect:/admin/users?created";
  }

  @GetMapping("/{id}/edit")
  public String showEditUserForm(@PathVariable("id") Long id, Model model) {
    User user = userService.getUserById(id);
    model.addAttribute("user", user);
    return "admin/users/edit";
  }

  @PostMapping("/{id}/edit")
  public String submitEditUserForm(@PathVariable("id") Long id, @ModelAttribute("user") @Valid User user,
      BindingResult result) {
    if (result.hasErrors()) {
      return "admin/users/edit";
    }
    userService.updateUser(id, user);
    return "redirect:/admin/users?updated";
  }

  @PostMapping("/{id}/delete")
  public String deleteUser(@PathVariable("id") Long id) {
    userService.deleteUser(id);
    return "redirect:/admin/users?deleted";
  }
}
