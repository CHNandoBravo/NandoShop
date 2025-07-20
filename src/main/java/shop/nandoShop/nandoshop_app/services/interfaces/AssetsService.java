package shop.nandoShop.nandoshop_app.services.interfaces;

import org.springframework.web.multipart.MultipartFile;
import shop.nandoShop.nandoshop_app.dtos.AssetDTO;

import java.io.IOException;

public interface AssetsService {
    AssetDTO uploadFile(MultipartFile file) throws IOException;
}
