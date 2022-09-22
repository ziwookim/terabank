package terafintech.terabank.api;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import terafintech.terabank.domain.DepositHistory;
import terafintech.terabank.dto.CreateDepositRequest;
import terafintech.terabank.dto.CreateDepositResponse;
import terafintech.terabank.service.DepositHistoryService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class DepositApiController {

    private static final Logger logger = LoggerFactory.getLogger(DepositApiController.class);

    private final DepositHistoryService depositHistoryService;

    @PostMapping("/api/deposit")
    public CreateDepositResponse createDeposit(@RequestBody @Valid CreateDepositRequest request) throws InterruptedException {

        logger.info("requestParam receiverPublickey: {}", request.getReceiverPublicKey());
        logger.info("requestParam amount: {}", request.getAmount());

//        Thread.sleep(10000);

        int checkedAmount = checkAmount(request.getAmount());

        Long id = depositHistoryService.deposit(request.getReceiverPublicKey(), checkedAmount);

        DepositHistory depositHistory = depositHistoryService.findOne(id);

        String receiverUserId = "";
        if(depositHistory.getReceiver() != null) {
            receiverUserId = depositHistory.getReceiver().getUserId();
        }

        return new CreateDepositResponse(depositHistory.getResultCode().toString(), receiverUserId);

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
