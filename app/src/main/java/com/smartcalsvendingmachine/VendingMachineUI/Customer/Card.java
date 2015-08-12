package com.smartcalsvendingmachine.VendingMachineUI.Customer;

public class Card {

    private int number;
    private double balance;

    public Card(int n, double b){
        number = n;
        balance = b;
    }

    public int getNumber(){
        return number;
    }

    public double getBalance(){
        return balance;
    }
}
