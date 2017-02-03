package com.example.edward.tradingapp.Models;

/**
 * Created by Edward on 03-Feb-17.
 */

public class Good
{
    private int id;
    private String name;
    private int quantity;
    private int price;
    private int updated;

    public Good(int id, String name, int quantity, int price,int updated)
    {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.updated = updated;
    }

    private Good() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getUpdated() {
        return updated;
    }

    public void setUpdated(int updated) {
        this.updated = updated;
    }

    @Override
    public String toString() {
        return "Good{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", updated=" + updated +
                '}';
    }

    public String toJSON() {
        return "Good{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", updated=" + updated +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Good)) return false;

        Good good = (Good) o;

        if (id != good.id) return false;
        if (quantity != good.quantity) return false;
        if (price != good.price) return false;
        if (updated != good.updated) return false;
        return name != null ? name.equals(good.name) : good.name == null;

    }

}
