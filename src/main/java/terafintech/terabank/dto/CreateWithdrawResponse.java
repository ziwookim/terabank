package terafintech.terabank.dto;

import lombok.Data;

@Data
public class CreateWithdrawResponse {

    private String resultCode;
    private String senderUserId;
    private int balance;

    public CreateWithdrawResponse(String resultCode, String senderUserId, int balance) {
        this.resultCode = resultCode;
        this.senderUserId = senderUserId;
        this.balance = balance;
    }
}
