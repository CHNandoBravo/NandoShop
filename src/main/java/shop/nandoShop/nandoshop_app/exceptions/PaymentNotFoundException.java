package shop.nandoShop.nandoshop_app.exceptions;

public class PaymentNotFoundException extends RuntimeException{
    public PaymentNotFoundException(Long id) {
        super("Pago con ID " + id + " no encontrado.");
    }

    public PaymentNotFoundException(String message) {
        super(message);
    }
}
