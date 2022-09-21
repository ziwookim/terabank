package terafintech.terabank.dto;

import lombok.Data;

@Data
public class CreateWithdrawResponse {

    private String resultCode;
    private String senderId;
    private int balance;

    public CreateWithdrawResponse(String resultCode, String senderId, int balance) {
        this.resultCode = resultCode;
        this.senderId = senderId;
        this.balance = balance;
    }
}
