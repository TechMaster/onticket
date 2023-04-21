package vn.techmaster.onticket.service;

import java.util.List;

import vn.techmaster.onticket.model.User;

public interface UserService {
  public void registerUser(User user);
  public List<User> getAllUsers();
  public User getUserById(Long id);
  public void updateUser(Long id, User user);
  public void deleteUser(Long id);
  public void resetPassword(String email);
  public boolean isPasswordResetTokenValid(String token);
  public void resetPassword(String token, String password);
}
