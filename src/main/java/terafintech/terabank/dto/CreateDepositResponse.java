package terafintech.terabank.dto;

import lombok.Data;

@Data
public class CreateDepositResponse {

    private String resultCode;
    private String receiverId;

    public CreateDepositResponse(String resultCode, String receiverId) {
        this.resultCode = resultCode;
        this.receiverId = receiverId;
    }
}
