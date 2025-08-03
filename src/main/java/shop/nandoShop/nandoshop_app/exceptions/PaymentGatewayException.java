package shop.nandoShop.nandoshop_app.exceptions;

public class PaymentGatewayException extends RuntimeException{
    public PaymentGatewayException(String message) {
        super(message);
    }
}
