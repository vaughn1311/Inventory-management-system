package starter;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.InHouse;
import models.Inventory;
import models.OutSourced;
import models.Product;

import java.io.IOException;

/**
 * FUTURE_ENHANCEMENT
 * Would add a way to keep data even after program closes. Maybe a third table on the main page that shows the associated
 *
 * parts for a selected product so one doesn't have to select and modify to see them. I used some actions repeatedly
 *
 * enough that it would be good for future readability to make a static method for those events like switching to main page.
 *
 * Search function can actively filter tables with partial names but needs full id for ids, would be nice QoL feature to find
 *
 * a way to implement that for partial integers.
 */
public class Main_Screen extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main_Screen.class.getResource("/main_form.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 950, 500);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {

        int firstPartId = Inventory.getOriginalPartId();
        InHouse nail = new InHouse(firstPartId,"nail",2.00,15,5,100,100);

        firstPartId = Inventory.getOriginalPartId();
        OutSourced wood = new OutSourced(firstPartId,"wood",10.00,40,5,200,
                "Wood Inc.");

        firstPartId = Inventory.getOriginalPartId();
        InHouse clarity = new InHouse(firstPartId,"Ahhhh",5000.00,82,10,9999,101);

        firstPartId = Inventory.getOriginalPartId();
        OutSourced glass = new OutSourced(firstPartId,"glass",60.50,23,2,70,
                "GlassMakerz");

        Inventory.addPart(nail);
        Inventory.addPart(wood);
        Inventory.addPart(clarity);
        Inventory.addPart(glass);

        /**
         * Test products for table
         */
        int firstProdId = Inventory.getOriginalProductId();
        Product desk = new Product(firstProdId,"desky",15.00,5,1,10);

        firstProdId = Inventory.getOriginalProductId();
        Product desk2 = new Product(firstProdId,"desky pro",20.00,5,1,10);

        firstProdId = Inventory.getOriginalProductId();
        Product confusion = new Product(firstProdId,"huh",150.00,5,5,100);

        Inventory.addProduct(desk);
        Inventory.addProduct(desk2);
        Inventory.addProduct(confusion);


        launch();
    }
}