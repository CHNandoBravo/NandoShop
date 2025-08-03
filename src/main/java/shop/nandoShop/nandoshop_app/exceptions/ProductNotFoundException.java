package shop.nandoShop.nandoshop_app.exceptions;

public class ProductNotFoundException extends RuntimeException{
    public ProductNotFoundException(Long id) {
        super("Producto con ID " + id + " no encontrado.");
    }

    public ProductNotFoundException(String message) {
        super(message);
    }
}
