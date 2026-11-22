package com.banking.model;

import java.util.List;

public class IndividualCustomer extends Customer{
    private  int nationalId;
    private String dateOfBirth;

    public IndividualCustomer(String customerId, String firstName, String surname, String address, List<Account> account, String customerType, int nationalId, String dateOfBirth) {
        super(customerId, firstName, surname, address, account, customerType);
        this.nationalId = nationalId;
        this.dateOfBirth = dateOfBirth;
    }

    //getters
    public int getNationalId() {
        return nationalId;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    //setters

    public void setNationalId(int nationalId) {
        this.nationalId = nationalId;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void verifyIdentify()
    {
        System.out.println("Identify verified");
    }

    @Override
    public String toString() {
        return "IndividualCustomer{" +
                "nationalId=" + nationalId +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", customerId='" + customerId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", surname='" + surname + '\'' +
                ", address='" + address + '\'' +
                ", account=" + account +
                ", customerType='" + customerType + '\'' +
                '}';
    }
}


