package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.*;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

import static java.lang.Integer.parseInt;

public class modify_productController implements Initializable {

    public TextField ModifyProdMaxText;

    public TextField ModifyProdPriceText;

    public TextField ModifyProdInvText;

    public TextField ModifyProdNameText;

    public TextField ModifyProdMinText;

    public TableView<Part> ModifyProdAllTable;

    public TableColumn<Part, Integer> AllPartIDCol;

    public TableColumn<Part, String> AllPartNameCol;

    public TableColumn<Part, Integer> AllInvLevelCol;

    public TableColumn<Part, Double> AllCostCol;

    public TextField ModifyProdSearchText;

    public Button ModifyProdPartButton;

    public TableView<Part> ModifyProdAssocTable;

    public TableColumn<Part, Integer> AssocPartIDCol;

    public TableColumn<Part, String> AssocPartNameCol;

    public TableColumn<Part, Integer> AssocInvLevelCol;

    public TableColumn<Part, Double> AssocCostCol;

    public Button ModifyProdRemoveB;

    public Button ModifyProdCancelB;

    public Button ModifyProdSaveB;

    public Part selectedPart = null;

    public static Product productInfo = null;

    public TextField ModifyProdIdText;

    public static ObservableList<Part> currentAssocParts = FXCollections.observableArrayList();

    public ObservableList<Part> newAssocParts = FXCollections.observableArrayList();

    FilteredList<Part> filteredParts = new FilteredList<>(FXCollections.observableList(Inventory.getAllParts()));


    /**
     * Method called by main page to pass data on selected product
     * @param selectedProduct product selected
     */
    public static void screenFill(Product selectedProduct){
        productInfo = selectedProduct;
        currentAssocParts.addAll(productInfo.getAllAssociatedParts());
    }

    /**
     * Moves a part to modified products associated parts table but does not save
     * @param actionEvent on add button click
     */
    public void ModifyPartOnClick(ActionEvent actionEvent) {
        selectedPart = ModifyProdAllTable.getSelectionModel().getSelectedItem();

        if(selectedPart == null) {
            Inventory.validationAlert(6);
        }
        else{
            currentAssocParts.add(selectedPart);
        }
        ModifyProdAllTable.getSelectionModel().clearSelection();
    }

    /**
     * Removes an associated part from the product being modified
     * @param actionEvent on remove associated part button click
     */
    public void ModifyProdRemoveClick(ActionEvent actionEvent) {

        selectedPart = ModifyProdAssocTable.getSelectionModel().getSelectedItem();

        if(selectedPart == null) {
            Inventory.validationAlert(6);
            return;
        }
        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to remove this part?");

            Optional<ButtonType> answer = alert.showAndWait();

            if(answer.isPresent() && answer.get() == ButtonType.OK){
                currentAssocParts.remove(selectedPart);
            }
        }
        ModifyProdAssocTable.getSelectionModel().clearSelection();
    }

    /**
     * Cancels modify and returns to main page
     * @param actionEvent on cancel button click
     * @throws IOException
     */
    public void ModifyProdCancelClick(ActionEvent actionEvent) throws IOException {
        currentAssocParts.clear();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to cancel?");
        Optional<ButtonType> answer = alert.showAndWait();

        if(answer.isPresent() && answer.get() == ButtonType.OK){
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/main_form.fxml")));
            Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * Saves modified product to inventory and returns to main page. keeping id
     * @param actionEvent on save button click
     */
    public void ModifyProdSaveClick(ActionEvent actionEvent) {
        String name = ModifyProdNameText.getText();
        if(name.isBlank()){
            Inventory.validationAlert(1);
            return;
        }

        int error = 0;
        try {
            error = 2;
            int stock = Integer.parseInt(ModifyProdInvText.getText());
            error = 3;
            double cost = Double.parseDouble(ModifyProdPriceText.getText());
            error = 2;
            int maxstock = Integer.parseInt(ModifyProdMaxText.getText());
            int minstock = Integer.parseInt(ModifyProdMinText.getText());

            if(minstock > stock || stock > maxstock){
                Inventory.validationAlert(5);
                return;
            }
                Product newProduct = new Product(productInfo.getId(), name,cost,stock,minstock,maxstock);
                newAssocParts.setAll(currentAssocParts);

                for(Part newPart : newAssocParts){
                    newProduct.addAssociatedPart(newPart);
                }

                Inventory.updateProduct(Inventory.getProductIndex(productInfo),newProduct);

                currentAssocParts.clear();

                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/main_form.fxml")));
                Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
            Inventory.validationAlert(error);
        }
    }

    /**
     * initializer for modify product page. loads the tables and fields with product to be modified data
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ModifyProdIdText.setText(String.valueOf(productInfo.getId()));
        ModifyProdNameText.setText(productInfo.getName());
        ModifyProdInvText.setText(String.valueOf(productInfo.getStock()));
        ModifyProdPriceText.setText(String.valueOf(productInfo.getPrice()));
        ModifyProdMaxText.setText(String.valueOf(productInfo.getMax()));
        ModifyProdMinText.setText(String.valueOf(productInfo.getMin()));

        ModifyProdSearchText.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredParts.setPredicate(product -> {
                if(newValue == null || newValue.isBlank()){
                    return true;
                }
                try {
                    if (product.getName().toLowerCase().contains(newValue.toLowerCase())) {
                        return true;
                    }
                }catch(Exception ignored){
                }
                try {
                    int numberPartFilter = parseInt(newValue);
                    if (product.getId() == numberPartFilter) {
                        return true;
                    }
                }catch(Exception ignored){
                }
                ModifyProdAllTable.setPlaceholder(new Label("None found"));
                return false;
            });
        });

        SortedList<Part> sortedList = new SortedList<>(filteredParts);

        sortedList.comparatorProperty().bind(ModifyProdAllTable.comparatorProperty());

        ModifyProdAllTable.setItems(sortedList);

        AllPartIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        AllPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        AllInvLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        AllCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        ModifyProdAssocTable.setItems(currentAssocParts);

        AssocPartIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        AssocPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        AssocInvLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        AssocCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));

    }
}
