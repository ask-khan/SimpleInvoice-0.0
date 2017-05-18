/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import java.net.URL;
import java.util.ResourceBundle;
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
import Handler.GeneratePDF;
import com.itextpdf.text.DocumentException;
import java.awt.print.PrinterException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import javafx.beans.binding.Bindings;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressIndicator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;

/**
 * FXML Controller class
 *
 * @author Ahmed Saboor
 */
public class InvoiceController implements Initializable {

    //Create CheckBox
    @FXML
    public CheckBox generatePrinter;
    
    // Create TextField
    @FXML
    public TextField customerName, billNo, quanlity, tradePrice, individualDiscount, combineDiscount;

    // Create DatePicker
    @FXML
    public DatePicker datePicker;

    // Create ChoiceBox
    @FXML
    public ChoiceBox<String> productLine = new ChoiceBox<String>();
    @FXML
    public ChoiceBox<String> discountPackage = new ChoiceBox<String>();
    
    // Declare TableView Variable.
    @FXML
    private TableView<ProductTable> productTable = new TableView<ProductTable>();

    private Boolean checkValidation = false;
        
    //Declare Menu Bar
    @FXML
    MenuBar MenuNewBar;
    
    //Declare Menu
    @FXML
    Menu fileMenu;
    
    // Declare File Menu
    @FXML
    MenuItem NewMenuItem ;
    // Declare Progress Indicator.
    @FXML
    ProgressIndicator printingIndicator = new ProgressIndicator();

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
    
    // Create Button
    @FXML
    public Button addButton, printButton ;
    
    @FXML
    public Button deleteButton;

    /**
     * This method is used to add the row columns into table
     *
     * @param event Action Event for button.
     */
    @FXML
    private void onClickAddButton(ActionEvent event) {
        
        ProductTable producTableObject = calculateProcess(quanlity, productLine, tradePrice, individualDiscount, discountPackage);
        
        if (producTableObject != null) {
            productData.add(producTableObject);
        }

    }
    
    
    
    /**
     * This method is used for print PDF Button
     *
     * @param event Action Event for Button.
     * @exception IOException On input error.
     * @exception PrinterException On print error.
     * @exception JSONException On json error.
     * @exception DocumentException On document error.
     * @see IOException
     * @see PrinterException
     * @see DocumentException
     * @see JSONException
     */
    @FXML
    private void printOnClick(ActionEvent event) throws JSONException, IOException, DocumentException, PrinterException {
        
        // Declare Customer Name and retrieve Customer Data.
        String[] customersName = retieveCustomerData();
        // Declare GeneratePDF
        GeneratePDF generatePDF = new GeneratePDF();
        
        // If Customer is not null.
        if (customersName != null) {
            // Get table Data.
            JSONArray tableData = getTableData();
            // Check Table is null or not
            if (tableData != null) {
                // If Customer is equal to Combine
                if ("Combine".equals(customersName[3])) {
                    customersName[4] = combineDiscount.getText();
                    // If Customer is null
                    if ("".equals(customersName[4])) {
                        combineDiscount.getStyleClass().add("error");
                        generatePDF.ShowDialog("", 7);
                    } else {
                        // if checkbox is selected
                        if ( generatePrinter.isSelected() ) {
                            String filePath = generatePDF.GeneratePDFDoc(customersName, tableData);
                            generatePDF.PrintPDF( filePath );
                        } else {
                            generatePDF.GeneratePDFDoc(customersName, tableData);
                            generatePDF.ShowDialog("", 8);
                        }
                    }
                }

            }

        }

    }

    /**
     * Add Delete Button
     *
     * @param event
     */
    @FXML
    private void ButtonDeleteAction(ActionEvent event) {
        // Alert Dialog Code.
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Product");
        alert.setContentText("Are you sure you want to delete this Product?");
        Optional<ButtonType> result = alert.showAndWait();

        // If Button is Yes.
        if (result.get() == ButtonType.OK) {
            //System.out.println("OK");
            ProductTable addProduct = productTable.getSelectionModel().getSelectedItem();
            // Remove Peoduct
            productData.remove( addProduct );
            // Function Call For Delete Product By Product Code.
        } else {
            System.out.println("Delete");
        }
    }
    
    /**
     * This method is used to retrieve customer data
     * @return 
     */
    public String[] retieveCustomerData() {
        
        // Declare CustomerData.
        String[] customerData = new String[5];
        
        // Declare GeneratePDF
        GeneratePDF generatePDF = new GeneratePDF();
        // Try and Catch Execption
        try {
            // If CustomerName is Empty or not
            if (customerName.getText().isEmpty()) {
                customerName.getStyleClass().add("error");
                checkValidation = true;
            } else {
                customerData[0] = customerName.getText();
            }
            
            // If BillNumber is Empty or not.
            if (billNo.getText().isEmpty()) {
                billNo.getStyleClass().add("error");
                checkValidation = true;
            } else {
                int billNumber = 0;
                billNumber = Integer.parseInt(billNo.getText());
                customerData[1] = Integer.toString(billNumber);
                checkValidation = false;
            }
            // Check CheckValidation Is True
            if (checkValidation == true) {
                String attributeName = "";
                generatePDF.ShowDialog(attributeName, 2);
            }

            // Data Formations
            String pattern = "yyyy/MM/dd";
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

            // Get Date Picker Date.
            LocalDate datePiker = datePicker.getValue();
            customerData[2] = dateFormatter.format(datePiker);
            
            // Get Discount Package Value.
            customerData[3] = (String) discountPackage.getValue();
            
            // Check Customer is null or checkValidation is false.
            if (customerData[3] == null && checkValidation == false) {

                discountPackage.getStyleClass().add("error");
                generatePDF.ShowDialog("", 4);
                return null;
            } else {
                return customerData;
            }
        // Catch Execption.
        } catch (NumberFormatException e) {
            generatePDF.ShowDialog("", 3);
        }
        return null;
    }
    
    /**
     * This method is used to get table data
     * @return 
     * @throws org.json.JSONException 
     */
    public JSONArray getTableData() throws JSONException {
        
        // Dealare GeneratePDF.
        GeneratePDF generatePDF = new GeneratePDF();
        
        // Get Table Size
        int productTableSize = productTable.getItems().size();
        
        // Declare JSONArray.
        JSONArray tableInvoiceProductsData = new JSONArray();
        
        //System.out.println(productTableSize);
        // if Table Size is greater than 0.
        if (productTableSize > 0) {
            // for loop for individual product.
            for (int i = 0; i < productTableSize; i++) {
                // Declare JSONObject 
                JSONObject tableData = new JSONObject();
                // Declare JSONObject.
                ProductTable productTables = productTable.getItems().get(i);
                
                // Put Table Data.
                tableData.put("ProductName", productTables.getProductName());
                tableData.put("Quanlity", productTables.getProductQuanlity());
                tableData.put("Discount", productTables.getProductDiscount());
                tableData.put("TradePrice", productTables.getProductTradePrice());
                tableData.put("Amount", productTables.getProductAmount());

                tableInvoiceProductsData.put(tableData);
            }
            // Return
            return tableInvoiceProductsData;
        } else {
            // If checkValidation is false.
            if (checkValidation == false) {
                
                generatePDF.ShowDialog("", 5);
            }
            return null;
        }

    }
    /**
     * This method is used to calculate the product row.
     * @param productQuanlity used for product quantity
     * @param productName   used for product name.
     * @param tradePrice used for trade price
     * @param productDiscount used for product discount
     * @param discountPackage used for product package
     * @return 
     */
    public ProductTable calculateProcess(TextField productQuanlity, ChoiceBox productName, TextField tradePrice, TextField productDiscount, ChoiceBox discountPackage) {
        // Declare generatePDF object
        GeneratePDF generatePDF = new GeneratePDF();
        
         // Declare validation error.
        boolean validationError = false;
        // Declare integer variable for calculation.
        int productQuanlityInInteger = 0, tradePriceSelected = 0, productDiscountSelected = 0;
        // Declare float variable.
        float productAmountSelected = 0;
        
        // If product Quanlity is empty and not contain a to z.
        if (!productQuanlity.getText().isEmpty() && !productQuanlity.getText().matches(".*[a-z].*")) {
            productQuanlityInInteger = Integer.parseInt(productQuanlity.getText());
        } else {
            validationError = true;
        }
        // Get selected values
        String productNameSelected = (productName.getSelectionModel().getSelectedItem() == null) ? "null" : (String) productName.getSelectionModel().getSelectedItem().toString();
        // Check Product Selected is null or not.
        if (productNameSelected == null) {
            validationError = true;
        }
        // Get product package values
        String productPackage = (String) (discountPackage.getValue());
        // If trade price is empty and not contain a to z.
        if (!tradePrice.getText().isEmpty() && !tradePrice.getText().matches(".*[a-z].*")) {
            tradePriceSelected = Integer.parseInt(tradePrice.getText());
        } else {
            validationError = true;
        }
        // If product discount is empty and not contain a to z.
        if (!productDiscount.getText().isEmpty() && !productDiscount.getText().matches(".*[a-z].*")) {
            productDiscountSelected = (Integer.parseInt(productDiscount.getText()) == 0) ? 0 : Integer.parseInt(productDiscount.getText());
        } else {
            productDiscountSelected = 0;
            productDiscount.setText("0");
        }
        // Check if discount value is zero.
        if (productDiscountSelected == 0 && "Combine".equals(productPackage)) {
            productAmountSelected = tradePriceSelected * productQuanlityInInteger;
        } else {
            float discount = ((float) productDiscountSelected) / 100;
            productAmountSelected = (float) ((int) ((tradePriceSelected * productQuanlityInInteger) * (float) discount));
            productAmountSelected = (tradePriceSelected * productQuanlityInInteger) - productAmountSelected;
        }
        // Check validation 
        if (validationError == true) {
            generatePDF.ShowDialog("", 6);
            return null;
        } else {
            //System.out.print( productAmountSelected );
            ProductTable productTable = new ProductTable(productQuanlityInInteger, productNameSelected, tradePriceSelected, productDiscountSelected, (int) productAmountSelected);
            return productTable;
        }

    }

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Declare discountPackage variable for choicebox.
        final String[] discountPackages = new String[]{"Combine", "Individuals"};
        
        deleteButton.disableProperty().bind(Bindings.isEmpty(productTable.getSelectionModel().getSelectedItems()));
        
        // When user click on the New Invoice item.
        fileMenu.setOnAction((ActionEvent event) -> {
            customerName.setText("");
            billNo.setText("");
            quanlity.setText("");
            tradePrice.setText("");
            individualDiscount.setText("");
            combineDiscount.setText("");
        });
        
        // Declare Product item for choice Box. 
        ObservableList<String> productNames = FXCollections.observableArrayList("Astexim 100mg Susp", "Astexim 200mg Susp", "Astexim 200mg Cap", "Astexim 400mg Cap", "Astexone 250mg IV Inj", "Astexone 250mg IM Inj", "Astexone 500mg IM Inj", "Astexone 500mg IV Inj", "Astexone 1gm IM Inj", "Astoxil 125mg Susp", "Astoxil 250mg Susp", "Moreapt Susp", "Kanlvy Susp", "Kan Benrry", "Achfree");
        productLine.setItems(productNames);
        
        // Declare Date Object and set to right now date.
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate now = LocalDate.now();
        
        // Set Current Date on Date Picker.
        //System.out.println();
        datePicker.setValue(now);
        
        // Declare Packages for choicebox
        ObservableList<String> cursors = FXCollections.observableArrayList("Combine", "Individuals");
        discountPackage.setItems(cursors);
        
        // Get Select Choice Box value whenever its change.
        discountPackage.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue observable, Number value, Number new_value) {
                // Check if discount package is equal to combine
                if (discountPackages[new_value.intValue()].equals("Combine")) {
                    individualDiscount.setVisible(false);
                    combineDiscount.setVisible(true);
                } else {
                    combineDiscount.setVisible(false);
                    individualDiscount.setVisible(true);
                }
            }
        });
        
       
        // Set Values for table
        productName.setCellValueFactory(new PropertyValueFactory<ProductTable, String>("productName"));
        productQuanlity.setCellValueFactory(new PropertyValueFactory<ProductTable, Integer>("productQuanlity"));
        productTradePrice.setCellValueFactory(new PropertyValueFactory<ProductTable, Integer>("productTradePrice"));
        productDiscount.setCellValueFactory(new PropertyValueFactory<ProductTable, Integer>("productDiscount"));
        productAmount.setCellValueFactory(new PropertyValueFactory<ProductTable, Integer>("productAmount"));
        productTable.setItems(productData);
    }

}
