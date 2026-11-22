package com.banking.model;

public class TransactionHistory {
    private String transactionId;
    private String date;
    private  String type;
    private  double amount;

    public TransactionHistory(String transactionId, String date, String type, double amount) {
        this.transactionId = transactionId;
        this.date = date;
        this.type = type;
        this.amount = amount;
    }

    //getter

    public String getTransactionId() {
        return transactionId;
    }

    public String getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    //setters

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void recordTransaction(){
        System.out.println("Transaction Recorded.");
    }
    public void displayTransaction() {
        System.out.println("ID: " + transactionId);
        System.out.println("Date: " + date);
        System.out.println("Type: " + type);
        System.out.println("Amount: " + amount);
    }


}
