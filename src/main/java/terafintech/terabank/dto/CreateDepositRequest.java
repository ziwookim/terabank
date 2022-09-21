package terafintech.terabank.dto;

import lombok.Data;

@Data
public class CreateDepositRequest {

    private String receiverPublicKey;
    private String amount;
}
