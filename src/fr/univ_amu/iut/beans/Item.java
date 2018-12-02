package fr.univ_amu.iut.beans;

import fr.univ_amu.iut.Exceptions.NoItemFoundException;

import java.util.ArrayList;

public class Item {
    private int idItem;
    private String name;
    private String desc;
    private boolean conso;
    private int price;
    private static ArrayList<Item> allItems = new ArrayList<>();

    public static Item findItemById(int idItem) throws NoItemFoundException {
        for (Item item : Item.getAllItems()) {
            if (item.getIdItem() == idItem) {
                return item;
            }
        }
        throw new NoItemFoundException();
    }

    public static ArrayList<Item> getAllItems() {
        return allItems;
    }

    public static void setAllItems(ArrayList<Item> allItems) {
        Item.allItems = allItems;
    }

    public int getIdItem() {
        return idItem;
    }

    public void setIdItem(int idItem) {
        this.idItem = idItem;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isConso() {
        return conso;
    }

    public void setConso(boolean conso) {
        this.conso = conso;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Item{" +
                "idItem=" + idItem +
                ", name='" + name + '\'' +
                '}';
    }
}
