package vn.techmaster.onticket.service;

public interface EmailService {
  public void sendWelcomeEmail(String to);
  public void sendPasswordResetEmail(String to, String token);
}
