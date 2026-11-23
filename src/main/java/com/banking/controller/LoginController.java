
package com.banking.controller;

public class LoginController {

    private String selectedRole;

    // SELECT USER ROLE
    public boolean selectRole(String role) {

        if (role == null) {
            return false;
        }

        if (role.equalsIgnoreCase("Administrator")
                || role.equalsIgnoreCase("Customer")) {

            selectedRole = role;
            return true;
        }

        return false;
    }

    // GET SELECTED ROLE
    public String getSelectedRole() {
        return selectedRole;
    }

    // AUTHENTICATE USER
    public boolean authenticate(String username, String password) {

        if (selectedRole == null || username == null || password == null) {
            return false;
        }

        if (selectedRole.equalsIgnoreCase("Administrator")) {
            return username.equals("admin")
                    && password.equals("Admin@123");
        }

        if (selectedRole.equalsIgnoreCase("Customer")) {
            return username.equals("customer")
                    && password.equals("Customer@123");
        }

        return false;
    }

    // LOGOUT
    public void logout() {
        selectedRole = null;
    }
}
