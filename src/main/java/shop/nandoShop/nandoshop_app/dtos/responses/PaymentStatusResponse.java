package shop.nandoShop.nandoshop_app.dtos.responses;

public class PaymentStatusResponse {
    private String status; // Ej: "approved", "pending"
    private String externalReference;
    private String paymentMethod;
}
