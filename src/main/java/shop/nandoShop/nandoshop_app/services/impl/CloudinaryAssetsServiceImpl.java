package shop.nandoShop.nandoshop_app.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import shop.nandoShop.nandoshop_app.dtos.AssetDTO;
import shop.nandoShop.nandoshop_app.services.interfaces.AssetsService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class CloudinaryAssetsServiceImpl implements AssetsService {

    private final Cloudinary cloudinary;

    public CloudinaryAssetsServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public AssetDTO uploadFile(MultipartFile file) throws IOException {
        Map<String, Object> options = new HashMap<>();
        return new AssetDTO(cloudinary.uploader().upload(file.getBytes(), options)
        );
    }
    @Override
    public void deleteFile(String publicId) throws Exception {
        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }
}
