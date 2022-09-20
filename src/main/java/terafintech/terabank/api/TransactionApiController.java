package terafintech.terabank.api;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import terafintech.terabank.dto.CreateRemitRequest;
import terafintech.terabank.dto.CreateRemitResponse;
import terafintech.terabank.domain.TransactionHistory;
import terafintech.terabank.dto.GetRemitReponse;
import terafintech.terabank.dto.GetRemitRequest;
import terafintech.terabank.service.TransactionHistoryService;

import javax.validation.Valid;
import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
public class TransactionApiController {

    private final TransactionHistoryService transactionHistoryService;

    @Async
    @PostMapping("/api/remit")
    public CreateRemitResponse createRemit(@RequestBody @Valid CreateRemitRequest request) throws InterruptedException, ExecutionException {

        Long id = transactionHistoryService.remit(request.getReceiverPublicKey(), request.getSenderPrivateKey(), request.getAmount());

        TransactionHistory transactionHistory = transactionHistoryService.findOne(id);

        return new CreateRemitResponse(transactionHistory.getResultCode().toString(), transactionHistory.getId().toString());
    }

    @PostMapping("/api/remitInfo")
    public GetRemitReponse getRemit(@RequestBody @Valid GetRemitRequest request) {

        String strId = request.getId();
        Long id = Long.valueOf(0);
        try {
            id = Long.valueOf(strId);
        } catch (NumberFormatException e) {
            return new GetRemitReponse("잘못된 식별 키를 입력하셨습니다.");
        }

        try{
            TransactionHistory transactionHistory = transactionHistoryService.findOne(id);
            if(!transactionHistory.equals(null)) {
                return new GetRemitReponse(transactionHistory.getResultCode().toString());
            }
            return new GetRemitReponse("존재하지 않는 식별 키 입니다.");
        } catch (Exception e) {
            return new GetRemitReponse("존재하지 않는 식별 키 입니다.");
        }
    }
}
