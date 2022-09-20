package terafintech.terabank.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import terafintech.terabank.domain.*;
import terafintech.terabank.exception.InvalidAmountException;
import terafintech.terabank.repository.AccountRepository;
import terafintech.terabank.repository.TransactionHistoryRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TransactionHistoryService {

    private final TransactionHistoryRepository transactionHistoryRepository;
    private final DepositHistoryService depositHistoryService;
    private final WithdrawHistoryService withdrawHistoryService;
    private final AccountRepository accountRepository;

    @Transactional
    public Long remit(String receiverPublicKey, String senderPrivateKey, String amount) {

        TransactionResult resultCode = TransactionResult.SUCCESS;

        /**
         * valid amount value
         */
        int checkedAmount = checkAmount(amount);
        if(checkedAmount == -999) {
            resultCode = TransactionResult.INVALIDAMOUNT;
        }

        // 입금 api 호출
        Account receiver = depositHistoryService.checkReceiver(receiverPublicKey);
        // 입금 데이터 생성
        DepositHistory depositHistory = DepositHistory.createDeposit(receiver, checkedAmount);

        // 출금 api 호출
        Account sender = withdrawHistoryService.checkSender(senderPrivateKey);
        int doubleCheckedAmount = checkedAmount;
        if(doubleCheckedAmount > 0) {
            doubleCheckedAmount = withdrawHistoryService.checkMoney(sender, checkedAmount);
        }
        // 출금 데이터 생성
        WithdrawHistory withdrawHistory = WithdrawHistory.createWithdraw(sender, doubleCheckedAmount);

        TransactionHistory transactionHistory = TransactionHistory.createTransaction(depositHistory, withdrawHistory, receiverPublicKey, senderPrivateKey, amount);
//        if(!resultCode.equals(TransactionResult.SUCCESS)) {
//            depositHistory.setResultCode(resultCode);
//            withdrawHistory.setResultCode(resultCode);
//            transactionHistory.setResultCode(resultCode);
//
//        } else if (!depositHistory.getResultCode().equals(TransactionResult.SUCCESS)) {
//            /**
//             * 입금 과정에서 실패한 경우,
//             */
//            withdrawHistory.setResultCode(depositHistory.getResultCode());
//            transactionHistory.setResultCode(depositHistory.getResultCode());
//        } else if(!withdrawHistory.getResultCode().equals(TransactionResult.SUCCESS)) {
//            /**
//             * 출금 과정에서 실패한 경우,
//             */
//            depositHistory.setResultCode(withdrawHistory.getResultCode());
//            transactionHistory.setResultCode(withdrawHistory.getResultCode());
//        }
//
//        transactionHistory.setDepositHistory(depositHistory);
//        transactionHistory.setWithdrawHistory(withdrawHistory);
//        transactionHistory.setTransactionDate(LocalDateTime.now());
//        // 송금내역 저장
//        transactionHistory = transactionHistory.recordTransactionHistory(receiverPublicKey, senderPrivateKey, amount, resultCode);
//        transactionHistory = transactionHistory.tryCreateRemit(receiverPublicKey, receiver, senderPrivateKey, sender, checkedAmount);
        // 저장
        transactionHistoryRepository.save(transactionHistory);

        return transactionHistory.getId();
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

    public TransactionHistory findOne(Long id) {
        return transactionHistoryRepository.findOne(id);
    }

}
