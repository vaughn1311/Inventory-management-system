package models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @Author Christopher Vaughn
 */
public class Product {

    /**
     * holds a list of associated parts for a product
     */
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    /**
     * product id
     */
    private int id;

    /**
     * product name
     */
    private String name;

    /**
     * product price
     */
    private double price;

    /**
     * product stock
     */
    private int stock;

    /**
     * product minimum stock allowed
     */
    private int min;

    /**
     * product maximum stock allowed
     */
    private int max;

    /**
     * Product constructor
     */
    public Product(int id, String name, double price, int stock, int min, int max) {
    this.id = id;
    this.name = name;
    this.price = price;
    this.stock = stock;
    this.min = min;
    this.max = max;
    }

    /**
     * sets product id
     * @param id product id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * sets product name
     * @param name product name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * sets product price
     * @param price product price
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * sets product stock
     * @param stock set product stock amount
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * sets product minimum stock
     * @param min minimum stock amount
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * sets product maximum stock
     * @param max maximum stock amount
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * @return gets product id
     */
    public int getId() {
        return id;
    }

    /**
     * gets product name
     * @return product name
     */
    public String getName() {
        return name;
    }

    /**
     * gets product price
     * @return price
     */
    public double getPrice() {
        return price;
    }

    /**
     * gets product stock/inventory
     * @return stock
     */
    public int getStock() {
        return stock;
    }

    /**
     * gets product minimum
     * @return minimum stock
     */
    public int getMin() {
        return min;
    }

    /**
     * gets product maximum
     * @return max
     */
    public int getMax() {
        return max;
    }

    /**
     * adds associated part to product
     * @param part takes part object
     */
    public void addAssociatedPart(Part part){
        associatedParts.add(part);
    }

    /**
     * deletes an associated part from list after checking for existence
     * @param selectedAssociatedPart selected part object from form
     * @return boolean to inform success
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart){
        if(associatedParts.contains(selectedAssociatedPart)) {
            associatedParts.remove(selectedAssociatedPart);
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * returns whole list of associated parts
     * @return observable list of associated parts
     */
    public ObservableList<Part> getAllAssociatedParts(){
        return associatedParts;
    }
}
