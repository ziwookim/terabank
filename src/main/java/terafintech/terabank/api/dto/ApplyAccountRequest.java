package terafintech.terabank.api.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ApplyAccountRequest {

    @NotEmpty
    private String userId;
}
