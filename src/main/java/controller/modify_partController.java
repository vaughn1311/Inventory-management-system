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
import models.Part;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class modify_partController implements Initializable{

    public RadioButton ModifyInHouseB;

    public ToggleGroup ModifyPartGroup;

    public RadioButton ModifyOutSB;

    public Label ModifyPartFlexID;

    public TextField ModifyPartIDText;

    public TextField ModifyPartNameText;

    public TextField ModifyPartInventoryText;

    public TextField ModifyPartCostText;

    public TextField ModifyPartMax;

    public TextField ModifyPartFlexText;

    public TextField ModifyPartMin;

    public Button ModifyPartSaveB;

    public Button ModifyPartCancel;

    public static Part partInfo = null;

    /**
     * method called by main page to pass data of part to be modified
     * @param selectedPart part selected on main page
     */
    public static void screenFill(Part selectedPart){
        partInfo = selectedPart;
    }

    /**
     * initializer for modify part page that fills field with part to be modified info
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ModifyPartIDText.setText(String.valueOf(partInfo.getId()));
        ModifyPartNameText.setText(partInfo.getName());
        ModifyPartInventoryText.setText(String.valueOf(partInfo.getStock()));
        ModifyPartCostText.setText(String.valueOf(partInfo.getPrice()));
        ModifyPartMax.setText(String.valueOf(partInfo.getMax()));
        ModifyPartMin.setText(String.valueOf(partInfo.getMin()));

        if(partInfo instanceof InHouse){
            ModifyInHouseB.setSelected(true);
            ModifyPartFlexText.setText(String.valueOf(((InHouse) partInfo).getMachineId()));
            ModifyPartFlexID.setText("Machine ID");
        } else if (partInfo instanceof OutSourced) {
            ModifyOutSB.setSelected(true);
            ModifyPartFlexText.setText(((OutSourced) partInfo).getCompanyName());
            ModifyPartFlexID.setText("Company Name");
        }
    }

    /**
     * radiobutton to specify part type machine id
     * @param actionEvent radio button selected
     */
    public void ModifyPartInHouseB(ActionEvent actionEvent) {
        ModifyPartFlexID.setText("Machine ID");
    }

    /**
     * radiobutton to specify part type company name
     * @param actionEvent radio button selected
     */
    public void ModifyPartOutSB(ActionEvent actionEvent) {
        ModifyPartFlexID.setText("Company Name");
    }

    /**
     * Saves modified info for part if it passed validations
     * @param actionEvent on save button click
     */
    public void onModifyPartSave(ActionEvent actionEvent) {
        String name = ModifyPartNameText.getText();
        if(name.isBlank()){
            Inventory.validationAlert(1);
            return;
        }

        int error = 0;
        try {
            error = 2;
            int stock = Integer.parseInt(ModifyPartInventoryText.getText());
            error = 3;
            double cost = Double.parseDouble(ModifyPartCostText.getText());
            error = 2;
            int maxstock = Integer.parseInt(ModifyPartMax.getText());
            int minstock = Integer.parseInt(ModifyPartMin.getText());
            int machineId;
            String companyName;

            if(minstock > stock || stock > maxstock){
                Inventory.validationAlert(5);
                return;
            }

            if(ModifyInHouseB.isSelected()){
                try{
                    machineId = Integer.parseInt(ModifyPartFlexText.getText());
                    InHouse newPart = new InHouse(partInfo.getId(),name,cost,stock,minstock,maxstock,machineId);
                    Inventory.updatePart(Inventory.getPartIndex(partInfo), newPart);
                }
                catch(NumberFormatException e){
                    Inventory.validationAlert(4);
                    return;
                }
            }
            if(ModifyOutSB.isSelected()){
                companyName = ModifyPartFlexText.getText();
                if(companyName.isBlank()){
                    Inventory.validationAlert(1);
                    return;
                }

                OutSourced newPart = new OutSourced(partInfo.getId(),name,cost,stock,minstock,maxstock,companyName);
                Inventory.updatePart(Inventory.getPartIndex(partInfo),newPart);
            }
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/main_form.fxml")));
            Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();;
        }
        catch(NumberFormatException e){
            Inventory.validationAlert(error);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * cancels modifying and returns to main page. confirms
     * @param actionEvent on cancel button click
     * @throws IOException
     */
    public void onModifyPartCancel(ActionEvent actionEvent) throws IOException {

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
}
