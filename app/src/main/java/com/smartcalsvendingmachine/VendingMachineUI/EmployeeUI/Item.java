package com.smartcalsvendingmachine.VendingMachineUI.EmployeeUI;

public class Item {
    private int code;
    private String name;
    private double price;
    private String type;
    private int calories;
    private int sugar;
    private String info;
    private String picture;
    private int capacity;
    private int quantity;
    private boolean chosen;

    public Item(int c, String n, double pr) {
        code = c;
        name = n;
        price = pr;
        type = null;
        info = null;
        picture = null;
        capacity = 0;
        quantity = 0;
        chosen = false;
    }

    public Item(int c, String n, double pr, int cap) {
        code = c;
        name = n;
        price = pr;
        type = null;
        calories = 0;
        sugar = 0;
        info = null;
        picture = null;
        capacity = cap;
        quantity = 0;
        chosen = false;
    }

    public Item(int c, String n, double pri, String t, int ca, int s, String i, String pic){
        code = c;
        name = n;
        price = pri;
        type = t;
        calories = ca;
        sugar = s;
        info = i;
        picture = pic;
        capacity = 0;
        quantity = 0;
        chosen = false;
    }

    public int getID() {
        return code;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getType() { return type; }

    public int getCalories() {
        return calories;
    }

    public int getSugar() {
        return sugar;
    }

    public String getInfo() {
        return info;
    }

    public String getPic() {
        return picture;
    }

    public int getCapacity() { return capacity; }

    public int getQuantity() {
        return quantity;
    }

    public boolean isChosen() { return chosen; }

    public void setInfo(String i) { info = i; }

    public void setPic(String p) { picture = p; }

    public void setCapacity(int c) { capacity = c; }

    public void setQuantity(int q) { quantity = q; }

    public void setChosen(boolean tag){ chosen = tag; }
}
