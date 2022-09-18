package terafintech.terabank.api.dto;

import lombok.Data;

@Data
public class ApplyAccountResponse {

    private String userId;
    private String publicKey;
    private String privateKey;

    public ApplyAccountResponse (String userId, String publicKey, String privateKey) {
        this.userId = userId;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

}
