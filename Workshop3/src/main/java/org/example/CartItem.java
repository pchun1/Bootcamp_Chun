
package org.example;

public class CartItem {

    // Cart item properties
    private Product product;
    private int quantity;

    // Constructor
    public CartItem(Product product) {
        this.product = product;
        this.quantity = 1;
    }

    // Getters
    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    // Increase product quantity
    public void increaseQuantity() {
        quantity++;
    }

    // Decrease product quantity
    public void decreaseQuantity() {
        if (quantity > 0) {
            quantity--;
        }
    }

    // Calculate subtotal for this product
    public double getSubtotal() {
        return product.getPrice() * quantity;
    }
}
