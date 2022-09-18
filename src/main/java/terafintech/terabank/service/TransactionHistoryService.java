package terafintech.terabank.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import terafintech.terabank.domain.Account;
import terafintech.terabank.domain.TransactionHistory;
import terafintech.terabank.domain.TransactionResult;
import terafintech.terabank.exception.InvalidAmountException;
import terafintech.terabank.exception.NotEnoughMoneyException;
import terafintech.terabank.repository.AccountRepository;
import terafintech.terabank.repository.TransactionHistoryRepository;
import terafintech.terabank.exception.NotExistAccountException;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TransactionHistoryService {

    private final TransactionHistoryRepository transactionHistoryRepository;
    private final AccountRepository accountRepository;

//    @Transactional
//    public Long remit(String receiverPublicKey, String senderPrivateKey, String amount) {
//
//        TransactionResult resultCode = null;
//        Long receiverId = null;
//        Long senderId = null;
//        Account receiver = null;
//        Account sender = null;
//        /**
//         * valid amount value
//         */
//        Map<String, String> checkAmount = checkAmount(amount);
//        if(checkAmount.containsKey("invalidAmount")) {
//            resultCode = TransactionResult.INVALIDAMOUNT;
//        }
//
//        /**
//         * valid receiverPublicKey value
//         */
//        Map<String, Object> checkReceiver = checkReceiver(receiverPublicKey);
//        if(checkReceiver.containsKey("receiverId")) {
////            receiverId = (Long) checkReceiver.get("receiverId");
//            receiver = accountRepository.findOne((Long) checkReceiver.get("receiverId"));
//        } else {
//            resultCode = TransactionResult.RECEIVERERROR;
//        }
//
//        /**
//         * valid senderPrivateKey value
//         */
//        Map<String, Object> checkSender = checkSender(senderPrivateKey);
//        if(checkSender.containsKey("senderId")) {
////            senderId = (Long) checkSender.get("senderId");
//            sender = accountRepository.findOne((Long) checkSender.get("senderId"));
//        } else {
//            resultCode = TransactionResult.SENDERERROR;
//        }
//
//        /**
//         * valid sender's Money value
//         */
//        Map<String, String> checkMoney = null;
//        if(resultCode.equals(null) && !senderId.equals(null)) {
//            checkMoney = checkMoney(senderId, Integer.parseInt(amount));
//        }
//
//        if(checkMoney.containsKey("notEnoughMoney")) {
//            resultCode = TransactionResult.LACKOFMONEY;
//        }
//
//        TransactionHistory transactionHistory = null;
//        if (resultCode.equals(null)) {
//            /** 검증 오류 없는 경우,
//             * 송금 시도
//             */
//            int integerAmount = Integer.parseInt(amount);
//            transactionHistory = transactionHistory.tryCreateRemit(receiverPublicKey, receiver, senderPrivateKey, sender, integerAmount);
//        } else {
//            /**
//             * 검증 오류 있는 경우, 송금 내역 시도 저장.
//             */
//            transactionHistory = transactionHistory.recordTransactionHistory(receiverPublicKey, senderPrivateKey, amount, resultCode);
//        }
//
//        // 송금내역 저장
//        transactionHistoryRepository.save(transactionHistory);
//
//        return transactionHistory.getId();
//    }

    public Map<String, String> checkAmount (String amount) {

        Map<String, String> checkResult = new HashMap<>();
        checkResult.put("invalidAmount", "invalidAmount");

        int integerAmount = 0;
        try {
            integerAmount = Integer.parseInt(amount);
        } catch(InvalidAmountException e) {
            return checkResult;
//            throw new InvalidAmountException("잘못된 금액 입니다.");
        }

        if(integerAmount <= 0) {
            return checkResult;
//            throw new InvalidAmountException("입금액이 너무 적습니다.");
        } else{
            checkResult.remove("invalidAmount");
        }

        return checkResult;
    }

    public Map<String, Object> checkReceiver(String receiverPublicKey) {

        Map<String, Object> checkResult = new HashMap<>();

        try {
            Account receiver = accountRepository.findOneByPublicKey(receiverPublicKey);
            checkResult.put("receiverId", receiver.getId());
        } catch(Exception e) {
            return checkResult;
//            throw new NotExistAccountException("Receiver's account do not exist.");
        }

        return checkResult;
    }

    public Map<String, Object> checkSender(String senderPrivateKey) {

        Map<String, Object> checkResult = new HashMap<>();

        try {
            Account sender = accountRepository.findOneByPublicKey(senderPrivateKey);
            checkResult.put("senderId", sender.getId());
        } catch(Exception e) {
            return checkResult;
//            throw new NotExistAccountException("Sender's account do not exist.");
        }

        return checkResult;
    }

    public Map<String, String> checkMoney (Long senderId, int amount) {

        Map<String, String> checkResult = new HashMap<>();
        checkResult.put("notEnoughMoney", "notEnoughMoney");


        Account sender = accountRepository.findOne(senderId);
        if(sender.getMoney() < amount) {
            return checkResult;
//            throw new NotEnoughMoneyException("잔액이 부족합니다.");
        } else {
            checkResult.remove("notEnoughMoney");
        }

        return checkResult;

    }

}
