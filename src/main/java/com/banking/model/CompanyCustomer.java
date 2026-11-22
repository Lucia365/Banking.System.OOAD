
package com.banking.model;

import java.util.List;

public class CompanyCustomer extends Customer {

    private String companyName;
    private String companyAddress;
    private int registrationNumber;

    public CompanyCustomer(
            String customerId,
            String firstName,
            String surname,
            String address,
            List<Account> account,
            String customerType,
            String companyName,
            String companyAddress,
            int registrationNumber) {

        super(customerId, firstName, surname,
                address, account, customerType);

        this.companyName = companyName;
        this.companyAddress = companyAddress;
        this.registrationNumber = registrationNumber;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public int getRegistrationNumber() {
        return registrationNumber;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public void setRegistrationNumber(int registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public void verifyRegistration() {
        System.out.println(
                "Company Registration Verified"
        );
    }

    @Override
    public String toString() {
        return "CompanyCustomer{" +
                "companyName='" + companyName + '\'' +
                ", companyAddress='" + companyAddress + '\'' +
                ", registrationNumber=" + registrationNumber +
                ", customerId='" + customerId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", surname='" + surname + '\'' +
                ", address='" + address + '\'' +
                ", account=" + account +
                ", customerType='" + customerType + '\'' +
                '}';
    }
}
