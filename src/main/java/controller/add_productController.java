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

public class add_productController implements Initializable{

    public TextField AddProdMaxText;

    public TextField AddProdPriceText;

    public TextField AddProdInvText;

    public TextField AddProdNameText;

    public TextField AddProdMinText;

    public TableView<Part> AddProdAllTable;

    public TableColumn<Part, Integer> AllPartIDCol;

    public TableColumn<Part, String> AllPartNameCol;

    public TableColumn<Part, Integer> AllInvLevelCol;

    public TableColumn<Part, Double> AllCostCol;

    public TextField AddProdSearchText;

    public Button AddPartToProdButton;

    public TableView<Part> AddProdAssocTable;

    public TableColumn<Part, Integer> AssocPartIDCol;

    public TableColumn<Part, String> AssocPartNameCol;

    public TableColumn<Part, Integer> AssocInvLevelCol;

    public TableColumn<Part, Double> AssocCostCol;

    public Button AddProdRemoveB;

    public Button AddProdCancelB;

    public Button AddProdSaveB;

    Part selectedPart = null;

    ObservableList<Part> currentAssocParts = FXCollections.observableArrayList();

    FilteredList<Part> filteredParts = new FilteredList<>(FXCollections.observableList(Inventory.getAllParts()));

    /**
     * adds a part to be associated with a product if saved
     * @param actionEvent on add button click
     */
    public void onAddPartClick(ActionEvent actionEvent) {
        selectedPart = AddProdAllTable.getSelectionModel().getSelectedItem();

        if(selectedPart == null) {
            Inventory.validationAlert(6);
        }
        else{
            currentAssocParts.add(selectedPart);
        }
        AddProdAllTable.getSelectionModel().clearSelection();
    }

    /**
     * removes a part that was in the associated parts table for adding new products
     * @param actionEvent remove button click
     */
    public void AddProdRemoveClick(ActionEvent actionEvent) {

        selectedPart = AddProdAssocTable.getSelectionModel().getSelectedItem();

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
        AddProdAssocTable.getSelectionModel().clearSelection();
    }

    /**
     * Cancels adding a part and returns to main form
     * @param actionEvent in cancel button click
     */
    public void AddProdCancelClick(ActionEvent actionEvent) throws IOException {

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
     * Saves product to inventory with associated parts if all are fields valid
     * @param actionEvent on save button click
     */
    public void AddProdSaveClick(ActionEvent actionEvent) {

        String name = AddProdNameText.getText();
        if(name.isBlank()){
            Inventory.validationAlert(1);
            return;
        }

        int error = 0;
        try {
            error = 2;
            int stock = Integer.parseInt(AddProdInvText.getText());
            error = 3;
            double cost = Double.parseDouble(AddProdPriceText.getText());
            error = 2;
            int maxstock = Integer.parseInt(AddProdMaxText.getText());
            int minstock = Integer.parseInt(AddProdMinText.getText());

            if(minstock > stock || stock > maxstock) {
                Inventory.validationAlert(5);
                return;
            }

            int newProductId = Inventory.getOriginalProductId();
            Product newProduct = new Product(newProductId,name,cost,stock,minstock,maxstock);
            if (currentAssocParts.isEmpty()) {
                Inventory.addProduct(newProduct);

                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/main_form.fxml")));
                Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
            else {
                for(Part assocParts : currentAssocParts){
                    newProduct.addAssociatedPart(assocParts);
                }
            }

            Inventory.addProduct(newProduct);

            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/main_form.fxml")));
            Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        }
        catch(NumberFormatException e) {
            Inventory.validationAlert(error);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Initializes part table with all parts on add product form and sets up associated parts table for adding to new products
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        AddProdSearchText.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredParts.setPredicate(parts -> {
                if(newValue == null || newValue.isBlank()){
                    return true;
                }
                try {
                    if (parts.getName().toLowerCase().contains(newValue.toLowerCase())) {
                        return true;
                    }
                }catch(Exception ignored){
                }
                try {
                    int numberPartFilter = parseInt(newValue);
                    if (parts.getId() == numberPartFilter) {
                        return true;
                    }
                }catch(Exception ignored){
                }
                AddProdAllTable.setPlaceholder(new Label("None found"));
                return false;
            });
        });

        SortedList<Part> sortedList = new SortedList<>(filteredParts);

        sortedList.comparatorProperty().bind(AddProdAllTable.comparatorProperty());

        AddProdAllTable.setItems(sortedList);

        AllPartIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        AllPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        AllInvLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        AllCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        AddProdAssocTable.setItems(currentAssocParts);

        AssocPartIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        AssocPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        AssocInvLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        AssocCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));

    }
}
