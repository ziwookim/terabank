package terafintech.terabank.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class CreateRemitRequest {

    @NotEmpty
    private String receiverPublicKey;

    @NotEmpty
    private String senderPrivateKey;

    @NotEmpty
    private String amount;
}
