package terafintech.terabank.dto;

import lombok.Data;

@Data
public class CreateRemitResponse {

    private String returnCode;
    private String transactionHistoryId;

    public CreateRemitResponse(String returnCode, String transactionHistoryId) {
        this.returnCode = returnCode;
        this.transactionHistoryId = transactionHistoryId;
    }
}
