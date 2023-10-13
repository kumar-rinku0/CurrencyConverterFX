package currencyconverterfx;

import java.io.IOException;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.text.DecimalFormat;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

/**
 *
 * @author rinku
 */
public class CurrencyConverterFX extends Application {

    /**
     * @param args the command line arguments
     */
    Stage window;
    static BorderPane layout;
    static ComboBox<String> comboBox1, comboBox2;
    static TextField amountInput;
    static Label label4;

    static String red = "\u001B[31m";
    static String green = "\u001B[32m";
    static String yellow = "\u001B[33m";

    public static void main(String[] args) {
        // TODO code application logic here
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("currency converter");

        Label label1 = new Label("FROM ?? ");
        GridPane.setConstraints(label1, 1, 1);

//        TextField nameInput = new TextField();
        comboBox1 = new ComboBox<>();
        comboBox1.setPromptText("Currency ??");
        comboBox1.getStyleClass().add("combo");
        ArrayList<String> list = CurrencyConverter.currencyCodesHashMethod();
        comboBox1.getItems().addAll(list);
        GridPane.setConstraints(comboBox1, 2, 1);

        Label label2 = new Label("TO ?? ");
        GridPane.setConstraints(label2, 1, 2);

//        TextField passInput = new TextField();
        comboBox2 = new ComboBox<>();
        comboBox2.setPromptText("Currency ??");
        comboBox2.getStyleClass().add("combo");
        comboBox2.getItems().addAll(list);
        GridPane.setConstraints(comboBox2, 2, 2);

        Label label3 = new Label("AMOUNT ??");
        GridPane.setConstraints(label3, 1, 3);

        amountInput = new TextField();
        amountInput.setPromptText("Enter AMOUNT !!");
        GridPane.setConstraints(amountInput, 2, 3);

        Button button = new Button("convert!");
        GridPane.setConstraints(button, 2, 4);
        button.setOnAction(e -> {
            try {
                buttonClick();
            } catch (IOException ex) {
                Logger.getLogger(CurrencyConverterFX.class.getName()).log(Level.SEVERE, null, ex);
            }

        });

        HBox box = new HBox();
        box.setMinWidth(200);
        box.paddingProperty().setValue(new Insets(50, 50, 50, 50));

        label4 = new Label();
        GridPane.setConstraints(box, 1, 6);
        box.getChildren().add(label4);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(20);
        grid.setHgap(40);

        grid.getChildren().addAll(label1, label2, label3, comboBox1, comboBox2, amountInput, button);

        layout = new BorderPane();
        layout.setTop(grid);
        layout.setCenter(box);
        Scene scene = new Scene(layout, 400, 400);
        scene.getStylesheets().add("currencyconverterfx/CurrencyStyle.css");

        window.setScene(scene);
        window.show();
    }

    private static void buttonClick() throws ProtocolException, IOException {
        if (isDouble(amountInput.getText())) {
            String fromKey = comboBox1.getSelectionModel().getSelectedItem();
            String fromValue = CurrencyConverter.currencyCodes.get(fromKey);
            String toKey = comboBox2.getSelectionModel().getSelectedItem();
            String toValue = CurrencyConverter.currencyCodes.get(toKey);
            double amount = Double.parseDouble(amountInput.getText());
            double value = CurrencyConverter.sendHttpGETRequest(fromValue, toValue, amount);
            if (value != -1) {
                amountInput.setText("");
                label4.setText(null);
                DecimalFormat d = new DecimalFormat("00.00");
                label4.setText(d.format(amount) + fromValue + " = " + d.format(value) + toValue);
                label4.setId("Value");
            } else {
                System.out.println(red + "REQUEST FAILED!!");
            }

        }
    }

    private static boolean isDouble(String str) {
        try {
            double value = Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            System.out.println(yellow + "Entered Number is Not an Acceptable Value!!");
            amountInput.setText("");
            label4.setText(null);
            return false;
        }

    }

}
