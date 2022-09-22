package terafintech.terabank.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import terafintech.terabank.dto.CreateRemitRequest;
import terafintech.terabank.dto.CreateRemitResponse;
import terafintech.terabank.domain.RemitHistory;
import terafintech.terabank.dto.GetRemitReponse;
import terafintech.terabank.dto.GetRemitRequest;
import terafintech.terabank.exception.InvalidRemitIdException;
import terafintech.terabank.exception.NotExistRemitIdException;
import terafintech.terabank.service.RemitHistoryService;

import javax.validation.Valid;
import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
public class RemitApiController {

    private final RemitHistoryService remitHistoryService;

    @PostMapping("/api/remit")
    public CreateRemitResponse createRemit(@RequestBody @Valid CreateRemitRequest request) throws InterruptedException, ExecutionException {

        Long id = remitHistoryService.remit(request.getReceiverPublicKey(), request.getSenderPrivateKey(), request.getAmount());

        RemitHistory remitHistory = remitHistoryService.findOne(id);

        return new CreateRemitResponse(remitHistory.getResultCode().toString(), remitHistory.getId().toString());
    }

    @GetMapping("/api/remit/info")
    public GetRemitReponse getRemit(@RequestBody @Valid GetRemitRequest request) {

        String strId = request.getId();
        Long id = Long.valueOf(0);
        try {
            id = Long.valueOf(strId);
        } catch (NumberFormatException e) {
            throw new InvalidRemitIdException("잘못된 거래 식별 키를 입력하셨습니다.");
        }

        try{
            RemitHistory remitHistory = remitHistoryService.findOne(id);
            if(!remitHistory.equals(null)) {
                return new GetRemitReponse(remitHistory.getResultCode().toString());
            }
            throw new NotExistRemitIdException("존재하지 않는 거래 식별 키 입니다.");
        } catch (Exception e) {
            throw new NotExistRemitIdException("존재하지 않는 거래 식별 키 입니다.");
        }
    }
}
