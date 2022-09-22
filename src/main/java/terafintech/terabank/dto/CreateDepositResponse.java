package terafintech.terabank.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class CreateDepositResponse {

    private String resultCode;
    private String receiverUserId;

    public CreateDepositResponse(String resultCode, String receiverUserId) {
        this.resultCode = resultCode;
        this.receiverUserId = receiverUserId;
    }
}
