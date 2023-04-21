package vn.techmaster.onticket.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import vn.techmaster.onticket.model.Role;
import vn.techmaster.onticket.model.User;
import vn.techmaster.onticket.repository.RoleRepository;
import vn.techmaster.onticket.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private EmailService emailService;

  @Override
  public void registerUser(User user) {
    Role role = roleRepository.findByName("ROLE_USER");
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setRoles(Collections.singleton(role));
    userRepository.save(user);
    emailService.sendWelcomeEmail(user.getEmail());
  }

  @Override
  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  @Override
  public User getUserById(Long id) {
    return userRepository.findById(id).orElse(null);
  }

  @Override
  public void updateUser(Long id, User user) {
    User existingUser = userRepository.findById(id).orElse(null);
    existingUser.setFirstName(user.getFirstName());
    existingUser.setLastName(user.getLastName());
    existingUser.setEmail(user.getEmail());
    existingUser.setRoles(user.getRoles());
    userRepository.save(existingUser);
  }

  @Override
  public void deleteUser(Long id) {
    userRepository.deleteById(id);
  }

  @Override
  public void resetPassword(String email) {
    Optional<User> oUser = userRepository.findByEmail(email);
    
    if (oUser.isPresent()) {
      User user = oUser.get();
      String token = UUID.randomUUID().toString();
      user.setResetPasswordToken(token);
      user.setResetPasswordTokenExpiration(LocalDateTime.now().plusHours(24));
      userRepository.save(user);
      emailService.sendPasswordResetEmail(user.getEmail(), token);
    }
  }

  @Override
  public boolean isPasswordResetTokenValid(String token) {
    User user = userRepository.findByResetPasswordToken(token);
    if (user != null) {
      LocalDateTime tokenExpiration = user.getResetPasswordTokenExpiration();
      if (tokenExpiration.isAfter(LocalDateTime.now())) {
        return true;
      }
    }
    return false;
  }

  @Override
  public void resetPassword(String token, String password) {
    User user = userRepository.findByResetPasswordToken(token);
    if (user != null) {
      user.setPassword(passwordEncoder.encode(password));
      user.setResetPasswordToken(null);
      user.setResetPasswordTokenExpiration(null);
      userRepository.save(user);
    }
  }
}
