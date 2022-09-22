package terafintech.terabank.dto;

import lombok.Data;

@Data
public class CreateRemitResponse {

    private String returnCode;
    private String remitHistoryId;

    public CreateRemitResponse(String returnCode, String remitHistoryId) {
        this.returnCode = returnCode;
        this.remitHistoryId = remitHistoryId;
    }
}
