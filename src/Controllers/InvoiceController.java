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
import Handler.ProductTable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
/**
 * FXML Controller class
 *
 * @author Ahmed Saboor
 */
public class InvoiceController implements Initializable {
    // Create TextField
    @FXML
    public TextField customerName, billNo, quanlity, tradePrice, discountProduct ;
    // Create DatePicker
    @FXML
    public DatePicker datePicker;
    // Create ChoiceBox
    @FXML
    public ChoiceBox<String> productLine = new ChoiceBox<String>();
    @FXML
    public ChoiceBox<String> discountPackage = new ChoiceBox<String>();
    // Create Button
    @FXML
    public Button addButton;
    
    // Declare TableView Variable.
    @FXML
    private TableView <ProductTable> productTable = new TableView<ProductTable>();;
    // Declare TableView Columns Variable.
    @FXML
    private TableColumn<ProductTable, String> productName;
    @FXML
    private TableColumn<ProductTable, Integer> productQuanlity;
    @FXML
    private TableColumn<ProductTable, Integer> productTradePrice;
    @FXML
    private TableColumn<ProductTable, Integer> productDiscount;
    @FXML
    private TableColumn<ProductTable, Integer> productAmount;
    // Declare Observabilelist.
    @FXML
    private final ObservableList<ProductTable> productData = FXCollections.observableArrayList();
    
    
    
    @FXML
    private void onClickButton( ActionEvent event ) {
        
        
        // Get Customer Name
        String customerNames = customerName.getText();
        //Get Bill No
        String billNumber = billNo.getText();
        
        // Data Formations
        String pattern = "yyyy/MM/dd";
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
        
        // Get Date Picker Date.
        LocalDate datePiker = datePicker.getValue();
        //String getDateInFormat = dateFormatter.format(datePiker);
        
        ProductTable productTable = new ProductTable(23, "AHMED", 36, 23, 23);
        System.out.println( productTable );
        productData.add( productTable );
        
    }
    
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        final String[] discountPackages = new String[] { "Combine", "Individuals" };
        ObservableList<String> cursors = FXCollections.observableArrayList("Combine", "Individuals");
        discountPackage.setItems(cursors);
        
        discountPackage.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
          public void changed(ObservableValue observable, Number value, Number new_value) {
              
            if ( discountPackages[new_value.intValue()].equals("Combine") ) {
                discountProduct.setVisible(false);
            } else {
                discountProduct.setVisible(true);
            }
          }
        });
        
        // TODO
        productName.setCellValueFactory(new PropertyValueFactory<ProductTable, String>("productName"));
        productQuanlity.setCellValueFactory(new PropertyValueFactory<ProductTable,Integer>("productQuanlity"));
        productTradePrice.setCellValueFactory(new PropertyValueFactory<ProductTable,Integer>("productTradePrice"));
        productDiscount.setCellValueFactory(new PropertyValueFactory<ProductTable, Integer>("productTradePrice"));
        productAmount.setCellValueFactory(new PropertyValueFactory<ProductTable, Integer>("productTradePrice"));
        productTable.setItems(productData);
    }    
    
}
