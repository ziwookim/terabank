package terafintech.terabank.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import terafintech.terabank.domain.DepositHistory;
import terafintech.terabank.domain.ResultCode;
import terafintech.terabank.dto.CreateDepositRequest;
import terafintech.terabank.dto.CreateDepositResponse;
import terafintech.terabank.exception.InvalidAmountException;
import terafintech.terabank.service.DepositHistoryService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class DepositApiController {

    private final DepositHistoryService depositHistoryService;

    @PostMapping("/api/deposit")
    public CreateDepositResponse createDeposit(@RequestBody @Valid CreateDepositRequest request) {

        int checkedAmount = checkAmount(request.getAmount());

        Long id = depositHistoryService.deposit(request.getReceiverPublicKey(), checkedAmount);

        DepositHistory depositHistory = depositHistoryService.findOne(id);

        String receiverId = "";
        if(depositHistory.getReceiver() != null) {
            receiverId = depositHistory.getReceiver().getUserId();
        }

        return new CreateDepositResponse(depositHistory.getResultCode().toString(), receiverId);
    }

    public int checkAmount (String amount) {

        int integerAmount = 0;
        try {
            integerAmount = Integer.parseInt(amount);
        } catch(NumberFormatException e) {
            return -999;
//            throw new InvalidAmountException("잘못된 금액 입니다.");
        }

        if(integerAmount <= 0) {
            return -999;
//            throw new InvalidAmountException("입금액이 너무 적습니다.");
        } else{
            return integerAmount;
        }
    }
}