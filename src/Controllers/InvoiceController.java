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
    public TextField customerName, billNo, quanlity, tradePrice, individualDiscount, combineDiscount ;
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
    public Button addButton , printButton;
    
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
        
        
        
        ProductTable productTable  = calculateProcess(quanlity, productLine, tradePrice, individualDiscount);
        productData.add( productTable );
        
    }
    
    @FXML
    private void printOnClick( ActionEvent event ) {
        
        retieveCustomerData();
        getTableData();
    }
    
    public void retieveCustomerData () {
        // Get Customer Name
        String customerNames = customerName.getText();
        //Get Bill No
        String billNumber = billNo.getText();
        
        // Data Formations
        String pattern = "yyyy/MM/dd";
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
        
        // Get Date Picker Date.
        LocalDate datePiker = datePicker.getValue();
        
        String getDateInFormat = dateFormatter.format( datePiker );
        
        String discountPage =  (String) discountPackage.getValue();
    
    }
    
    public void getTableData () {
        int productTableSize = productTable.getItems().size();
        
        if ( productTableSize > 0 ) {
            for ( int i = 0; i < productTableSize ;  i++ ) {
                // Declare JSONObject.
                ProductTable productTables = productTable.getItems().get(i);
                System.out.println( productTables );
                
                
            }
        } else {
            System.out.println("No data is Add");
        }
    }
    
    public  ProductTable calculateProcess ( TextField productQuanlity, ChoiceBox productName, TextField tradePrice, TextField productDiscount ) {
        int productQuanlityInInteger = 0, tradePriceSelected = 0, productDiscountSelected = 0 , productAmountSelected = 0;
        if ( !productQuanlity.getText().isEmpty() && !productQuanlity.getText().matches(".*[a-z].*") ) {
            productQuanlityInInteger = Integer.parseInt( productQuanlity.getText() );
        }
        
        String productNameSelected = (String)(productName.getValue());  
        if ( productNameSelected.isEmpty() ) {
            productNameSelected = "No Product Found";
        }
        
        if ( !tradePrice.getText().isEmpty() && !tradePrice.getText().matches(".*[a-z].*") ) {
            
            tradePriceSelected = Integer.parseInt( tradePrice.getText() ); 
        }
        
        if ( !productDiscount.getText().isEmpty() && !productDiscount.getText().matches(".*[a-z].*") ){
            productDiscountSelected = ( Integer.parseInt( productDiscount.getText() ) == 0 ) ? 0 : Integer.parseInt( productDiscount.getText() ); 
        } else {
            productDiscountSelected = 0;
        }
        
        if ( productDiscountSelected == 0 ) {
            productAmountSelected = tradePriceSelected * productQuanlityInInteger;
        } else {
            float discount = ( (float) productDiscountSelected ) / 100;
            productAmountSelected = (int) (( tradePriceSelected * productQuanlityInInteger ) * ( float )discount) ;
        }
        
        System.out.print( productAmountSelected );
        ProductTable productTable = new ProductTable( productQuanlityInInteger, productNameSelected, tradePriceSelected, productDiscountSelected, productAmountSelected);
        
        return productTable;
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        final String[] discountPackages = new String[] { "Combine", "Individuals" };
        ObservableList<String> productNames = FXCollections.observableArrayList("Product A", "Product B");
        productLine.setItems(productNames);
        
        ObservableList<String> cursors = FXCollections.observableArrayList("Combine", "Individuals");
        discountPackage.setItems(cursors);
        
        discountPackage.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
          public void changed(ObservableValue observable, Number value, Number new_value) {
              
            if ( discountPackages[new_value.intValue()].equals("Combine") ) {
                individualDiscount.setVisible(false);
                combineDiscount.setVisible(true);
            } else {
                combineDiscount.setVisible(false);
                individualDiscount.setVisible(true);
            }
          }
        });
        
        // TODO
        productName.setCellValueFactory(new PropertyValueFactory<ProductTable, String>("productName"));
        productQuanlity.setCellValueFactory(new PropertyValueFactory<ProductTable,Integer>("productQuanlity"));
        productTradePrice.setCellValueFactory(new PropertyValueFactory<ProductTable,Integer>("productTradePrice"));
        productDiscount.setCellValueFactory(new PropertyValueFactory<ProductTable, Integer>("productDiscount"));
        productAmount.setCellValueFactory(new PropertyValueFactory<ProductTable, Integer>("productAmount"));
        productTable.setItems(productData);
    }    
    
}
