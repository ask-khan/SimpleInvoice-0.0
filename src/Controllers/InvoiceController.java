/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Ahmed Saboor
 */
public class InvoiceController implements Initializable {
    
    @FXML
    public TextField customerName, billNo, quanlity, tradePrice, discountProduct ;
    @FXML
    public DatePicker datePicker;
    @FXML
    public ChoiceBox discountPackage, productLine;
    @FXML
    public Button addButton;
    
    @FXML
    private void onClickButton( ActionEvent event ) {
        System.out.println("Contollers.InvoiceController.onClickButton()");
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
