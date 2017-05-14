/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Handler;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Ahmed Saboor
 */
public class ProductTable {

    // Declare Table View Attribute. 
    private final IntegerProperty productQuanlity;
    private final StringProperty productName;
    private final IntegerProperty productTradePrice;
    private final IntegerProperty productDiscount;
    private final IntegerProperty productAmount;

    // Constructor Product Table.
    public ProductTable(int productQuanlity, String productName, int tradePrice, int productDiscount, int productAmount) {
        this.productQuanlity = new SimpleIntegerProperty(productQuanlity);
        this.productName = new SimpleStringProperty(productName);
        this.productTradePrice = new SimpleIntegerProperty(tradePrice);
        this.productDiscount = new SimpleIntegerProperty(productDiscount);
        this.productAmount = new SimpleIntegerProperty(productAmount);
    }

    public int getProductQuanlity() {
        return productQuanlity.get();
    }

    public String getProductName() {
        return productName.get();
    }

    public int getProductTradePrice() {
        return productTradePrice.get();
    }

    public int getProductDiscount() {
        return productDiscount.get();
    }

    public int getProductAmount() {
        return productAmount.get();
    }
    
    public void setProductAmount( int productAmount ){
        this.productAmount.set( productAmount );
    }
    
    public void setProductDiscount( int productDiscount ){
        this.productDiscount.set( productDiscount );
    }
    
    public void setProductTradePrice( int productTable ) {
        this.productTradePrice.set( productTable );
    }
    
    public void setProductQuanlity ( int productQuanlity ) {
        this.productQuanlity.set(productQuanlity);
    }
    
    public void setProductName ( String productName ) {
        this.productName.set(productName);
    }
}
