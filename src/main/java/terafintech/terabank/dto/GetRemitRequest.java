package terafintech.terabank.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class GetRemitRequest {

    @NotEmpty
    private String id;
}
