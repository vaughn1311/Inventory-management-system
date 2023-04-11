package controller;

import javafx.application.Platform;
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
import models.Inventory;
import models.Part;
import models.Product;

import java.io.IOException;
import java.net.URL;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

import static java.lang.Integer.parseInt;


public class main_formController implements Initializable {

    //Table fx ids
    public TableView<Product> ProductTable;

    public TableView<Part> PartTable;

    //Part table column fx ids
    public TableColumn<Part, Integer> PartIDCol;

    public TableColumn<Part, String> PartNameCol;

    public TableColumn<Part, Integer> PartInvLvlCol;

    public TableColumn<Part, Double> PartCostCol;

    //Part table search box
    public TextField PartSearchText;

    //Part side button fx ids
    public Button AddPartB;

    public Button ModifyPartB;

    public Button DeletePartB;

    //Exit button fx id
    public Button ExitButton;

    //Product table column fx ids
    public TableColumn<Product, Integer> ProdIDColumn;

    public TableColumn<Product, String> ProdNameColumn;

    public TableColumn<Product, Integer> ProdInvColumn;

    public TableColumn<Product, Double> ProdCostColumn;

    //Product search text box
    public TextField ProdSearchText;

    //Product side button fx ids
    public Button AddProductB;

    public Button ModifyProdB;

    public Button DeleteProdB;

    public Part selectedPart = null;

    public Product selectedProduct = null;

    FilteredList<Part> filteredParts = new FilteredList<>(FXCollections.observableList(Inventory.getAllParts()));

    FilteredList<Product> filteredProducts = new FilteredList<>(FXCollections.observableList(Inventory.getAllProducts()));





    /**
     * Will initialize the parts and product tables on the main screen with data.
     * Also handles active searching for those tables with the text fields.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        /**
         * RUNTIME_ERROR This search filter was the hardest and most time-consuming functionality to figure out fully for me.
         *
         * Encountered many bugs with it like it crashing when a character didn't match, table not filtering at all on typing,
         *
         * and table rows duplicating once search bar was cleared. Had to research each problem as they arose and learned
         *
         * new things about how the language itself worked. Things that ultimately helped me get the search working was
         *
         * learning about setPredicate, using try/catch for exceptions, setPlaceholder for when search finds nothing, and
         *
         * getting the filtered list to notice when the observable it's wrapped around changed with getSource.
         */
        PartSearchText.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredParts.setPredicate(parts -> {
                if(newValue == null || newValue.isBlank()){
                    return true;
                }

                try {
                    if (parts.getName().toLowerCase().contains(newValue.toLowerCase())) {
                        return true;
                    }
                }catch(NumberFormatException ignored){
                }
                try {
                    int numberPartFilter = parseInt(newValue);
                    if (parts.getId() == numberPartFilter) {
                        return true;
                    }
                }catch(Exception ignored){
                }
                PartTable.setPlaceholder(new Label("None found"));
                return false;
            });
        });
        SortedList<Part> sortedList = new SortedList<>(filteredParts);

        sortedList.comparatorProperty().bind(PartTable.comparatorProperty());

        PartTable.setItems(sortedList);

        PartIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        PartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        PartInvLvlCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        PartCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        //Product Table search and initial data setup
        ProdSearchText.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredProducts.setPredicate(product -> {
                if(newValue == null || newValue.isBlank()){
                    return true;
                }

                try {
                    if (product.getName().toLowerCase().contains(newValue.toLowerCase())) {
                        return true;
                    }
                }catch(NumberFormatException ignored){
                }
                try {
                    if (product.getId() == parseInt(newValue)) {
                        return true;
                    }
                }catch(NumberFormatException ignored){
                }
                ProductTable.setPlaceholder(new Label("None Found"));
                return false;
            });
        });
        SortedList<Product> sortedProducts = new SortedList<>(filteredProducts);

        sortedProducts.comparatorProperty().bind(ProductTable.comparatorProperty());

        ProductTable.setItems(sortedProducts);

        ProdIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        ProdNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        ProdInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        ProdCostColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

    }

    /**
     * Moves to add parts form on clicking add parts button
     */
    public void AddPartsOnClick(ActionEvent actionEvent) throws IOException {
        Parent addParts = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/add_part_form.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(addParts);
        stage.setScene(scene);
        stage.show();
    }

    /**
     *
     * @param actionEvent moves to modify part form on clicking modify button
     * @throws IOException
     */
    public void ModifyPartsOnClick(ActionEvent actionEvent) throws IOException {

        selectedPart = PartTable.getSelectionModel().getSelectedItem();

        if(selectedPart == null){
            Inventory.validationAlert(6);
            return;
        }

        modify_partController.screenFill(selectedPart);

        Parent modifyParts = FXMLLoader.load((Objects.requireNonNull(getClass().getResource("/modify_part_form.fxml"))));

        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(modifyParts);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * will delete selected part on button click for part table
     * @param actionEvent onclick button
     */
    public void DeletePartsOnClick(ActionEvent actionEvent) {

        selectedPart = PartTable.getSelectionModel().getSelectedItem();

        if(selectedPart == null){
            Inventory.validationAlert(6);
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this part?");

        Optional<ButtonType> answer = alert.showAndWait();

        if(answer.isPresent() && answer.get() == ButtonType.OK) {
        selectedPart = PartTable.getSelectionModel().getSelectedItem();
        filteredParts.getSource().remove(selectedPart);
        PartTable.getSelectionModel().clearSelection();
        }
    }

    /**Closes the app
     * @param actionEvent exit program
     */
    public void onExitButton(ActionEvent actionEvent) {
        Platform.exit();
    }


    /**
     * Moves to add products page when add product button clicked
     * @param actionEvent on clicking add button
     * @throws IOException
     */
    public void AddProdOnClick(ActionEvent actionEvent) throws IOException {
        Parent addProd = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/add_product_form.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(addProd);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Moves to modify products page when modify product button clicked
     * @param actionEvent on clicking modify button
     * @throws IOException
     */
    public void ModifyProdOnClick(ActionEvent actionEvent) throws IOException {

        selectedProduct = ProductTable.getSelectionModel().getSelectedItem();

        if(selectedProduct == null){
            Inventory.validationAlert(6);
            return;
        }

        modify_productController.screenFill(selectedProduct);

        Parent ModifyProd = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/modify_product_form.fxml")));
        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(ModifyProd);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * delete selected product on table when clicked. Will confirm
     * @param actionEvent on clicking delete button
     */
    public void DeleteProdOnClick(ActionEvent actionEvent) {

        selectedProduct = ProductTable.getSelectionModel().getSelectedItem();

        if(selectedProduct == null){
            Inventory.validationAlert(6);
            return;
        }

        if(selectedProduct.getAllAssociatedParts().isEmpty()){

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this product?");

        Optional<ButtonType> answer = alert.showAndWait();

        if(answer.isPresent() && answer.get() == ButtonType.OK){
        selectedProduct = ProductTable.getSelectionModel().getSelectedItem();
        filteredProducts.getSource().remove(selectedProduct);
        ProductTable.getSelectionModel().clearSelection();
        }
        }

        else{
            Alert alert = new Alert(Alert.AlertType.ERROR, "Cannot delete a product with an associated part");
            alert.showAndWait();
        }
    }
}