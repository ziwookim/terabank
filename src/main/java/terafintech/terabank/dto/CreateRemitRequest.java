package terafintech.terabank.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class CreateRemitRequest {

    private String receiverPublicKey;

    private String senderPrivateKey;

    private String amount;
}
