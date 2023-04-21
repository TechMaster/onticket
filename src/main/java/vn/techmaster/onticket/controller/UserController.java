package vn.techmaster.onticket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import vn.techmaster.onticket.model.User;
import vn.techmaster.onticket.service.UserService;

@Controller
public class UserController {
  @Autowired
  private UserService userService;

  @GetMapping("/register")
  public String showRegistrationForm(Model model) {
    model.addAttribute("user", new User());
    return "register";
  }

  @PostMapping("/register-submit")
  public String submitRegistrationForm(@ModelAttribute("user") @Valid User user, BindingResult result) {
    if (result.hasErrors()) {
      return "register";
    }
    userService.registerUser(user);
    return "redirect:/login?registered";
  }

  @GetMapping("/login")
  public String showLoginForm() {
    return "login";
  }

  @GetMapping("/forgot-password")
  public String showForgotPasswordForm() {
    return "forgot-password";
  }

  @PostMapping("/forgot-password")
  public String submitForgotPasswordForm(@RequestParam("email") String email) {
    userService.resetPassword(email);
    return "redirect:/login?reset";
  }

  @GetMapping("/reset-password")
  public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
    if (userService.isPasswordResetTokenValid(token)) {
      model.addAttribute("token", token);
      return "reset-password";
    } else {
      return "redirect:/login";
    }
  }

  @PostMapping("/reset-password")
  public String submitResetPasswordForm(@RequestParam("token") String token,
      @RequestParam("password") String password,
      @RequestParam("confirmPassword") String confirmPassword) {
    if (password.equals(confirmPassword)) {
      userService.resetPassword(token, password);
      return "redirect:/login?reset";
    } else {
      return "redirect:/reset-password?token=" + token;
    }
  }
}
