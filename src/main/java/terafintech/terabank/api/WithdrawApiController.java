package terafintech.terabank.api;

import ch.qos.logback.core.joran.spi.NoAutoStart;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import terafintech.terabank.domain.ResultCode;
import terafintech.terabank.domain.WithdrawHistory;
import terafintech.terabank.dto.CreateDepositRequest;
import terafintech.terabank.dto.CreateDepositResponse;
import terafintech.terabank.dto.CreateWithdrawRequest;
import terafintech.terabank.dto.CreateWithdrawResponse;
import terafintech.terabank.exception.InvalidAmountException;
import terafintech.terabank.service.WithdrawHistoryService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class WithdrawApiController {

    private static final Logger logger = LoggerFactory.getLogger(WithdrawApiController.class);

    private final WithdrawHistoryService withdrawHistoryService;

    @PostMapping("/api/withdraw")
    public CreateWithdrawResponse createWithdraw(@RequestBody @Valid CreateWithdrawRequest request) throws InterruptedException {

        logger.info("requestParam receiverPublickey: {}", request.getSenderPrivateKey());
        logger.info("requestParam amount: {}", request.getAmount());

//        Thread.sleep(10000);

        int checkedAmount = checkAmount(request.getAmount());

        Long id = withdrawHistoryService.withdraw(request.getSenderPrivateKey(), checkedAmount);

        WithdrawHistory withdrawHistory = withdrawHistoryService.findOne(id);

        String senderUserId = "";
        int balance = 0;
        if(withdrawHistory.getSender() != null) {
            senderUserId = withdrawHistory.getSender().getUserId();
            balance = withdrawHistory.getSender().getBalance();
        }

        return new CreateWithdrawResponse(withdrawHistory.getResultCode().toString(), senderUserId, balance);

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
//            throw new InvalidAmountException("출금액이 너무 적습니다.");
        } else{
            return integerAmount;
        }
    }
}
