package terafintech.terabank.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
