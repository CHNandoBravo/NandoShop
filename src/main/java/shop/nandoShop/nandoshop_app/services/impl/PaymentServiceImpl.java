package shop.nandoShop.nandoshop_app.services.impl;

import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.nandoShop.nandoshop_app.dtos.PreferenceDTO;
import shop.nandoShop.nandoshop_app.dtos.requests.PaymentRequest;
import shop.nandoShop.nandoshop_app.dtos.responses.PaymentPreferenceResponse;
import shop.nandoShop.nandoshop_app.dtos.responses.PaymentStatusResponse;
import shop.nandoShop.nandoshop_app.entities.Payment;
import shop.nandoShop.nandoshop_app.entities.Product;
import shop.nandoShop.nandoshop_app.entities.User;
import shop.nandoShop.nandoshop_app.enums.PaymentStatus;
import shop.nandoShop.nandoshop_app.exceptions.PaymentGatewayException;
import shop.nandoShop.nandoshop_app.mappers.PaymentMapper;
import shop.nandoShop.nandoshop_app.repositories.PaymentRepository;
import shop.nandoShop.nandoshop_app.repositories.ProductRepository;
import shop.nandoShop.nandoshop_app.services.interfaces.PaymentGateway;
import shop.nandoShop.nandoshop_app.services.interfaces.PaymentService;
import shop.nandoShop.nandoshop_app.services.interfaces.ProductService;
import shop.nandoShop.nandoshop_app.utils.AuthUtil;

import java.math.BigDecimal;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentGateway paymentGateway;

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    ProductService productService;

    @Autowired
    ProductRepository productRepository;
    public PaymentServiceImpl(PaymentGateway paymentGateway) {
        this.paymentGateway = paymentGateway;
    }

    @Transactional
    public PaymentPreferenceResponse createPayment(PaymentRequest request) {
        User user = AuthUtil.getCurrentUser();
        Product product = productService.getProductOrThrow(request.getIdProduct());
        Payment payment = createPendingPayment(user, product, request.getQuantity());

        paymentRepository.save(payment);

        PreferenceDTO dto = PaymentMapper.mapToPreferenceDTO(payment, product);
        try {
            PaymentPreferenceResponse preference = paymentGateway.createPreference(dto);
            payment.setPaymentId(preference.getPreferenceId());
            payment.setStatus(PaymentStatus.PENDING);
            paymentRepository.save(payment);
            return preference;
        }
        catch (MPException | MPApiException e) {
            // Logueamos el error con detalle pero lanzamos una excepción propia para no filtrar detalles al cliente
            log.error("Error al crear preferencia en Mercado Pago", e);

            throw new PaymentGatewayException("No se pudo procesar el pago en el proveedor externo.");
        }
    }

    public PaymentStatusResponse checkPaymentStatus(String paymentId) {
        return paymentGateway.checkStatus(paymentId);
    }

    public Payment createPendingPayment(User user, Product product, int quantity) {
        Payment payment = new Payment();

        payment.setPayer(user);
        payment.setQuantity(quantity);

        // Validamos que el precio no sea nulo para evitar NPE
        BigDecimal unitPrice = product.getPrice();
        if (unitPrice == null) {
            throw new IllegalStateException("El producto no tiene precio definido");
        }

        payment.setUnitPrice(unitPrice);
        payment.setTotalPrice(unitPrice.multiply(BigDecimal.valueOf(quantity)));

        // Estado inicial
        payment.setStatus(PaymentStatus.IN_PROCESS);

        return payment;
    }

}
