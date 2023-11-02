package com.example.strash_cart;

import java.util.ArrayList;

public class ItemModel {
    private String ItemName, ItemDesc, ItemId, ItemPrice, UserPhone;
    private ArrayList<String> ImageUrls;

    // we need empty constructor
    public ItemModel() {
    }

    public ItemModel(String itemName, String itemDesc, String itemId, String itemPrice, ArrayList<String> imageUrls, String userPhone) {
        UserPhone = userPhone;
        ItemName = itemName;
        ItemDesc = itemDesc;
        ItemId = itemId;
        ItemPrice = itemPrice;
        ImageUrls = imageUrls;
    }

    public String getItemName() { return ItemName; }

    public void setUserPhone(String userPhone) {
        UserPhone = userPhone;
    }

    public String getUserPhone(){return UserPhone;}

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public void setItemPrice(String itemPrice) {
        ItemPrice = itemPrice;
    }

    public String getItemDesc() {
        return ItemDesc;
    }

    public void setItemDesc(String itemDesc) {
        ItemDesc = itemDesc;
    }

    public String getItemImageFirst(){
        return ImageUrls.get(0);
    }

    public String getItemPrice(){
        return ItemPrice;
    }

    public String getItemId() {
        return ItemId;
    }

    public void setItemId(String itemId) {
        ItemId = itemId;
    }

    public ArrayList<String> getImageUrls() {
        return ImageUrls;
    }

    public void setImageUrls(ArrayList<String> imageUrls) {
        ImageUrls = imageUrls;
    }
}

