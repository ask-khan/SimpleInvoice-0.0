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
import Handler.GeneratePDF;
import com.itextpdf.text.DocumentException;
import java.awt.print.PrinterException;
import java.io.IOException;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * FXML Controller class
 *
 * @author Ahmed Saboor
 */
public class InvoiceController implements Initializable {

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

    // Create Button
    @FXML
    public Button addButton, printButton;

    // Declare TableView Variable.
    @FXML
    private TableView<ProductTable> productTable = new TableView<ProductTable>();

    private Boolean checkValidation = false;
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
    private void onClickAddButton(ActionEvent event) {

        ProductTable productTable = calculateProcess(quanlity, productLine, tradePrice, individualDiscount, discountPackage);
        if (productTable != null) {
            productData.add(productTable);
        }

    }

    @FXML
    private void printOnClick(ActionEvent event) throws JSONException, IOException, DocumentException, PrinterException {

        String[] customersName = retieveCustomerData();
        GeneratePDF generatePDF = new GeneratePDF();

        if (customersName != null) {
            JSONArray tableData = getTableData();
            if (tableData != null) {

                if ("Combine".equals(customersName[3])) {
                    customersName[4] = combineDiscount.getText();
                    if ("".equals(customersName[4])) {
                        combineDiscount.getStyleClass().add("error");
                        generatePDF.ShowDialog("", 7);
                    } else {
                        String filePath = generatePDF.GeneratePDFDoc(customersName, tableData);
                        //generatePDF.PrintPDF( filePath );
                    }
                }

            }

        }

    }

    public String[] retieveCustomerData() {
        String[] customerData = new String[5];

        GeneratePDF generatePDF = new GeneratePDF();
        try {

            // Get Customer Name         
            if (customerName.getText().isEmpty()) {
                customerName.getStyleClass().add("error");
                checkValidation = true;
            } else {
                customerData[0] = customerName.getText();
            }
            // Get Bill Number
            if (billNo.getText().isEmpty()) {
                billNo.getStyleClass().add("error");
                checkValidation = true;
            } else {
                int billNumber = 0;
                billNumber = Integer.parseInt(billNo.getText());
                customerData[1] = Integer.toString(billNumber);
                checkValidation = false;
            }

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

            customerData[3] = (String) discountPackage.getValue();

            if (customerData[3] == null && checkValidation == false) {

                discountPackage.getStyleClass().add("error");
                generatePDF.ShowDialog("", 4);
                return null;
            } else {
                return customerData;
            }
        } catch (NumberFormatException e) {
            generatePDF.ShowDialog("", 3);
        }
        return null;
    }

    public JSONArray getTableData() throws JSONException {
        GeneratePDF generatePDF = new GeneratePDF();

        int productTableSize = productTable.getItems().size();
        // Declare JSONArray.
        JSONArray tableInvoiceProductsData = new JSONArray();
        //System.out.println(productTableSize);
        if (productTableSize > 0) {
            for (int i = 0; i < productTableSize; i++) {
                JSONObject tableData = new JSONObject();
                // Declare JSONObject.
                ProductTable productTables = productTable.getItems().get(i);

                tableData.put("ProductName", productTables.getProductName());
                tableData.put("Quanlity", productTables.getProductQuanlity());
                tableData.put("Discount", productTables.getProductDiscount());
                tableData.put("TradePrice", productTables.getProductTradePrice());
                tableData.put("Amount", productTables.getProductAmount());

                tableInvoiceProductsData.put(tableData);
            }
            return tableInvoiceProductsData;
        } else {
            if (checkValidation == false) {
                generatePDF.ShowDialog("", 5);
            }
            return null;
        }

    }

    public ProductTable calculateProcess(TextField productQuanlity, ChoiceBox productName, TextField tradePrice, TextField productDiscount, ChoiceBox discountPackage) {
        GeneratePDF generatePDF = new GeneratePDF();
        boolean validationError = false;
        int productQuanlityInInteger = 0, tradePriceSelected = 0, productDiscountSelected = 0;
        float productAmountSelected = 0;

        if (!productQuanlity.getText().isEmpty() && !productQuanlity.getText().matches(".*[a-z].*")) {
            productQuanlityInInteger = Integer.parseInt(productQuanlity.getText());
        } else {
            validationError = true;
        }

        String productNameSelected = (productName.getSelectionModel().getSelectedItem() == null) ? "null" : (String) productName.getSelectionModel().getSelectedItem().toString();
        if (productNameSelected.equals(null)) {
            validationError = true;
        }

        String productPackage = (String) (discountPackage.getValue());

        if (!tradePrice.getText().isEmpty() && !tradePrice.getText().matches(".*[a-z].*")) {
            tradePriceSelected = Integer.parseInt(tradePrice.getText());
        } else {
            validationError = true;
        }

        if (!productDiscount.getText().isEmpty() && !productDiscount.getText().matches(".*[a-z].*")) {
            productDiscountSelected = (Integer.parseInt(productDiscount.getText()) == 0) ? 0 : Integer.parseInt(productDiscount.getText());
        } else {
            productDiscountSelected = 0;
            productDiscount.setText("0");
        }

        if (productDiscountSelected == 0 && "Combine".equals(productPackage)) {
            productAmountSelected = tradePriceSelected * productQuanlityInInteger;
        } else {
            float discount = ((float) productDiscountSelected) / 100;
            productAmountSelected = (float) ((int) ((tradePriceSelected * productQuanlityInInteger) * (float) discount));
            productAmountSelected = (tradePriceSelected * productQuanlityInInteger) - productAmountSelected;
        }

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
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        final String[] discountPackages = new String[]{"Combine", "Individuals"};
        ObservableList<String> productNames = FXCollections.observableArrayList("Astexim 100mg Susp", "Astexim 200mg Susp", "Astexim 200mg Cap", "Astexim 400mg Cap", "Astexone 250mg IV Inj", "Astexone 250mg IM Inj", "Astexone 500mg IM Inj", "Astexone 500mg IV Inj", "Astexone 1gm IM Inj", "Astoxil 125mg Susp", "Astoxil 250mg Susp", "Moreapt Susp", "Kanlvy Susp", "Kan Benrry", "Achfree");
        productLine.setItems(productNames);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate now = LocalDate.now();
        //System.out.println();
        datePicker.setValue(now);

        ObservableList<String> cursors = FXCollections.observableArrayList("Combine", "Individuals");
        discountPackage.setItems(cursors);

        discountPackage.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue observable, Number value, Number new_value) {

                if (discountPackages[new_value.intValue()].equals("Combine")) {
                    individualDiscount.setVisible(false);
                    combineDiscount.setVisible(true);
                } else {
                    combineDiscount.setVisible(false);
                    individualDiscount.setVisible(true);
                }
            }
        });
        productTable.setEditable(true);
        // TODO
        productName.setCellValueFactory(new PropertyValueFactory<ProductTable, String>("productName"));
        productQuanlity.setCellValueFactory(new PropertyValueFactory<ProductTable, Integer>("productQuanlity"));
        productTradePrice.setCellValueFactory(new PropertyValueFactory<ProductTable, Integer>("productTradePrice"));
        productDiscount.setCellValueFactory(new PropertyValueFactory<ProductTable, Integer>("productDiscount"));
        productAmount.setCellValueFactory(new PropertyValueFactory<ProductTable, Integer>("productAmount"));
        productTable.setItems(productData);
    }

}
