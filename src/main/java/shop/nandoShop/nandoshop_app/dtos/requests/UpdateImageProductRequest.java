package shop.nandoShop.nandoshop_app.dtos.requests;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
@Data
public class UpdateImageProductRequest {
    private MultipartFile image;
}
