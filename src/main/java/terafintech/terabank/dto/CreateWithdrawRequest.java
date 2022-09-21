package terafintech.terabank.dto;

import lombok.Data;

@Data
public class CreateWithdrawRequest {

    private String senderPrivateKey;
    private String amount;
}
