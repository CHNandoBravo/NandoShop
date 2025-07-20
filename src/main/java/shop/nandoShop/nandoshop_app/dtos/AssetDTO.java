package shop.nandoShop.nandoshop_app.dtos;

import java.util.Map;

public record AssetDTO(String url, String publicId) {
    public AssetDTO(Map<String, Object> asset) {
        this(asset.get("url").toString(), asset.get("public_id").toString());
    }
}
