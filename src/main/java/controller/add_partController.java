package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.InHouse;
import models.Inventory;
import models.OutSourced;


import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class add_partController implements Initializable{

    public RadioButton PartInHouseB;

    public ToggleGroup AddPartGroup;

    public RadioButton PartOutSB;

    public Label AddPartFlexID;

    public TextField AddPartIDText;

    public TextField AddPartNameText;

    public TextField AddPartInventoryText;

    public TextField AddPartCostText;

    public TextField AddPartMax;

    public TextField AddPartFlexText;

    public TextField AddPartMin;

    public Button AddPartSaveB;

    public Button AddPartCancel;

    /**
     * Changes text field for InHouse part machine ids when selected
     * @param actionEvent InHouse radiobutton selected
     */
    public void AddPartInHouseB(ActionEvent actionEvent) {
        AddPartFlexID.setText("Machine ID");
    }

    /**
     * Changes text field for OutSourced part company names when selected
     * @param actionEvent OutSourced radiobutton selected
     */
    public void AddPartOutSB(ActionEvent actionEvent) {
        AddPartFlexID.setText("Company Name");
    }

    /**
     * Validates a filled add part form then saves to inventory if passed
     * @param actionEvent on add button clicked
     */
    public void onAddPartSave(ActionEvent actionEvent) {
        String name = AddPartNameText.getText();
        if(name.isBlank()){
            Inventory.validationAlert(1);
            return;
        }

        int error = 0;
        try {
             error = 2;
            int stock = Integer.parseInt(AddPartInventoryText.getText());
            error = 3;
            double cost = Double.parseDouble(AddPartCostText.getText());
            error = 2;
            int maxstock = Integer.parseInt(AddPartMax.getText());
            int minstock = Integer.parseInt(AddPartMin.getText());
            int machineId;
            String companyName;

            if(minstock > stock || stock > maxstock){
                Inventory.validationAlert(5);
                return;
            }


            if(PartInHouseB.isSelected()){
                try{
                    machineId = Integer.parseInt(AddPartFlexText.getText());
                    int newPartId = Inventory.getOriginalPartId();
                    InHouse newPart = new InHouse(newPartId,name,cost,stock,minstock,maxstock,machineId);
                    Inventory.addPart(newPart);
                }
                catch(NumberFormatException e){
                    Inventory.validationAlert(4);
                    return;
                }
            }
            if(PartOutSB.isSelected()){
                    companyName = AddPartFlexText.getText();
                    if(companyName.isBlank()){
                        Inventory.validationAlert(1);
                        return;
                    }
                    int newPartId = Inventory.getOriginalPartId();
                    OutSourced newPart = new OutSourced(newPartId,name,cost,stock,minstock,maxstock,companyName);
                    Inventory.addPart(newPart);
            }

            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/main_form.fxml")));
            Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch(NumberFormatException e){
            Inventory.validationAlert(error);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Cancels entering a part and returns to main form. Confirms
     * @param actionEvent on clicking cancel button
     * @throws IOException
     */
    public void onAddPartCancel(ActionEvent actionEvent) throws IOException {
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
     * Initialize for add part form.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
