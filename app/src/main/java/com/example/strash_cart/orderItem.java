package com.example.strash_cart;

public class orderItem {
    String sellerPhone, buyerPhone, deliverAddress, productPrice, productId, payment;

    public orderItem(){}

    public orderItem(String sellerPhone, String buyerPhone, String deliverAddress, String productPrice, String productId, String payment) {
        this.sellerPhone = sellerPhone;
        this.buyerPhone = buyerPhone;
        this.deliverAddress = deliverAddress;
        this.productPrice = productPrice;
        this.productId = productId;
        this.payment = payment;
    }

    public String getSellerPhone() {
        return sellerPhone;
    }

    public void setSellerPhone(String sellerPhone) {
        this.sellerPhone = sellerPhone;
    }

    public String getBuyerPhone() {
        return buyerPhone;
    }

    public void setBuyerPhone(String buyerPhone) {
        this.buyerPhone = buyerPhone;
    }

    public String getDeliverAddress() {
        return deliverAddress;
    }

    public void setDeliverAddress(String deliverAddress) {
        this.deliverAddress = deliverAddress;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }
}
