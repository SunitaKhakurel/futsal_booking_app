package com.namus.futsalbookingsystem.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class PasswordChangeRequest {
    @NotBlank
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String currentPassword;
    @NotBlank
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String newPassword;

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
