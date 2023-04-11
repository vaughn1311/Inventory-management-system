package models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

/**
 * @Author Christopher Vaughn
 */

public class Inventory {

    /**
     * Observable lists holding all parts and products for tables
     */
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * holds the first and future part ids
     */
    private static int origPartId = 0;

    /**
     * holds the first and future product ids
     */
    private static int origProductId = 1000;

    /**
     * adds parts to main list
     * @param newPart part object to add to list
     */
    public static void addPart(Part newPart){
        allParts.add(newPart);
    }

    /**
     * adds products to main list
     * @param newProduct product object to add to list
     */
    public static void addProduct(Product newProduct){
        allProducts.add(newProduct);
    }

    /**
     * Method to lookup specific part in list with part id
     * @param partId part id
     * @return part id
     */
    public static Part lookupPart(int partId){
        Part match = null;
        for(Part checked : allParts){
            if(checked.getId() == partId){
                match = checked;
                return match;
            }
        } return null;
    }

    /**
     * method to lookup specific product in list with product id
     * @param productId id of product
     * @return product id
     */
    public static Product lookupProduct(int productId){
        return allProducts.get(productId);
    }

    /**
     * method to lookup parts in list with part name. returns list of matching parts
     * @param partName part name
     * @return part name
     */
    public static ObservableList<Part> lookupPart(String partName) {

        ObservableList<Part> partMatches = FXCollections.observableArrayList();

        for(Part part : allParts) {
            if(partName.isBlank()){
                System.out.println("search bar is empty");
                break;
            }
            else if(part.getName().contains(partName)) {
                partMatches.add(part);
            }
        }
        return partMatches;
    }

    /**
     * method to lookup product in list with product name. returns list of matching products
     * @param productName text from user to search product
     * @return list of matching products
     */
    public static ObservableList<Product> lookupProduct(String productName){

        ObservableList<Product> productMatches = FXCollections.observableArrayList();

        for(Product product : allProducts) {
            if(product.getName().contains(productName)){
                productMatches.add(product);
            }
        }
        return productMatches;
    }


    /**
     * Method updates specific part in part list
     * @param index index to look for
     * @param selectedPart selected part
     */
    public static void updatePart(int index, Part selectedPart){
        allParts.set(index, selectedPart);
    }

    /**
     * @param index index of product to modify
     * @param newProduct product object to modify
     */
    public static void updateProduct(int index, Product newProduct){
        allProducts.set(index, newProduct);
    }

    /**
     * @param selectedPart part selected
     * @return Removes a selected part and returns true to caller if successful
     */
    public static boolean deletePart(Part selectedPart){
        if(allParts.contains(selectedPart)){
        allParts.remove(selectedPart);
        return true;
        }
        return false;
    }

    /**
     * @param selectedProduct product selected
     * @return Removes a selected product and returns true to caller if successful
     */
    public static boolean deleteProduct(Product selectedProduct){
        if(allParts.contains(selectedProduct)){
        allProducts.remove(selectedProduct);
        return true;
        }
        return false;
    }

    /**
     * @return Method returns entire part list to caller
     */
    public static ObservableList<Part> getAllParts(){
        return allParts;
    }

    /**
     * @return Method returns entire product list to caller
     */
    public static ObservableList<Product> getAllProducts(){
        return allProducts;
    }

    /**
     * gets new original part id for a new part
     * @return increments a new part id
     */
    public static int getOriginalPartId() {
        return ++origPartId;
    }

    /**
     * gets new original product id for a new product
     * @return increments a new product id
     */
    public static int getOriginalProductId() {
        return ++origProductId;
    }

    /**
     * gets index of a part in the parts list
     * @param part part to find
     * @return index of part
     */
    public static int getPartIndex(Part part){
        return allParts.indexOf(part);
    }

    /**
     * gets index of a product in the product list
     * @param product product to find
     * @return index of product
     */
    public static int getProductIndex(Product product){
        return allProducts.indexOf(product);
    }

    /**
     * Switch for the many alerts needed for this program
     * @param alertCase matching error to popup response
     */
    public static void validationAlert(int alertCase){
        Alert alert = new Alert(Alert.AlertType.ERROR);

        switch (alertCase) {
            case 1 -> {
                alert.setContentText("No field can be blank");
                alert.showAndWait();
            }
            case 2 -> {
                alert.setContentText("Inventory and stock fields must be integer i.e. 5");
                alert.showAndWait();
            }
            case 3 -> {
                alert.setContentText("Cost must be a double i.e. 20.00");
                alert.showAndWait();
            }
            case 4 -> {
                alert.setContentText("Machine ID must be an integer");
                alert.showAndWait();
            }
            case 5 -> {
                alert.setContentText("Check fields. Inventory can't be lower than minimum or higher than maximum");
                alert.showAndWait();
            }
            case 6 -> {
                alert.setContentText("Must make a selection first");
                alert.showAndWait();
            }
        }
    }

}

