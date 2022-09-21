package terafintech.terabank.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import terafintech.terabank.domain.*;
import terafintech.terabank.exception.InvalidAmountException;
import terafintech.terabank.repository.AccountRepository;
import terafintech.terabank.repository.RemitHistoryRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RemitHistoryService {

    private final RemitHistoryRepository remitHistoryRepository;
    private final DepositHistoryService depositHistoryService;
    private final WithdrawHistoryService withdrawHistoryService;
    private final AccountRepository accountRepository;

    @Transactional
    public Long remit(String receiverPublicKey, String senderPrivateKey, String amount) {

        /**
         * valid amount value
         */
        int checkedAmount = checkAmount(amount);

        /**
         *
         */

        Account receiver = depositHistoryService.checkReceiver(receiverPublicKey);
        // 입금 api 호출
        DepositHistory depositHistory = DepositHistory.createDeposit(receiver, checkedAmount);

//        RestTemplate depositTemplate = new RestTemplate();

        // 출금 api 호출
        Account sender = withdrawHistoryService.checkSender(senderPrivateKey);
        int doubleCheckedAmount = checkedAmount;
        if(doubleCheckedAmount > 0) {
            doubleCheckedAmount = withdrawHistoryService.checkMoney(sender, checkedAmount);
        }
        // 출금 api 호출
        WithdrawHistory withdrawHistory = WithdrawHistory.createWithdraw(sender, doubleCheckedAmount);

        /**
         *
         */

        RemitHistory remitHistory = RemitHistory.createTransaction(depositHistory, withdrawHistory, receiverPublicKey, senderPrivateKey, amount);
        // 저장
        remitHistoryRepository.save(remitHistory);

        return remitHistory.getId();
    }

    public int checkAmount (String amount) {

        int integerAmount = 0;
        try {
            integerAmount = Integer.parseInt(amount);
        } catch(InvalidAmountException e) {
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

    public RemitHistory findOne(Long id) {
        return remitHistoryRepository.findOne(id);
    }

}
