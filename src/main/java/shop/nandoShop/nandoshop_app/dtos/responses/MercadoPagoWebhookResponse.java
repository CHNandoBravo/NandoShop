package shop.nandoShop.nandoshop_app.dtos.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MercadoPagoWebhookResponse {
    private String action;
    @JsonProperty("api_version")
    private String apiVersion;
    private Data data;
    @JsonProperty("date_created")
    private String dateCreated;
    private long id;
    @JsonProperty("live_mode")
    private boolean liveMode;
    private String type;
    @JsonProperty("user_id")
    private String userId;

    @lombok.Data
    public static class Data {
        private String id;
    }
}
