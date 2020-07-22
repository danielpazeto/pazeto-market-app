package com.pazeto.comercio.vo;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.ServerTimestamp;
import com.pazeto.comercio.widgets.Utils;

import java.io.Serializable;
import java.util.Date;

/**
 * @author pazeto
 */
public abstract class BaseSaleStocked implements Serializable {

    public enum TYPE {
        ALL, SALE, STOCK
    }

    public static final String ID = "_id";

    @Exclude
    private String id;

    private @ServerTimestamp Date timestamp;

    private Product product;
    private String idClient;
    private String date;

    private boolean isPaid;

    private double quantity = 0;
    private double unitPrice = 0;
    private double totalPrice;



    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public BaseSaleStocked(Product product, double quantity, String date) {
        this.setProduct(product);
        this.setQuantity(quantity);
        this.setDate(date);
    }

    public BaseSaleStocked() {
    }

    public BaseSaleStocked(Product product, String clientId, Date date, double quantity, double unitPrice) {
        this.setProduct(product);
        this.setQuantity(quantity);
        this.setIdClient(clientId);
        this.setUnitPrice(unitPrice);
        this.setDate(Utils.ISO_8601_DATE_FORMAT.format(date));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getIdClient() {
        return idClient;
    }

    public void setIdClient(String idClient) {
        this.idClient = idClient;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean isPaid) {
        this.isPaid = isPaid;
    }

    public abstract BaseSaleStocked.TYPE getType();

    public double getTotalPrice() {
        return getUnitPrice()*getQuantity();
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

}