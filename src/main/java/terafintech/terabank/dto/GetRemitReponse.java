package terafintech.terabank.dto;

import lombok.Data;

@Data
public class GetRemitReponse {

    private String returnCode;

    public GetRemitReponse (String returnCode) {
        this.returnCode = returnCode;
    }
}
