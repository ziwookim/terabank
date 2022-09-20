package terafintech.terabank.service;

import lombok.RequiredArgsConstructor;
import lombok.With;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import terafintech.terabank.domain.Account;
import terafintech.terabank.domain.TransactionResult;
import terafintech.terabank.domain.WithdrawHistory;
import terafintech.terabank.repository.AccountRepository;
import terafintech.terabank.repository.WithdrawHistoryRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WithdrawHistoryService {

    private final WithdrawHistoryRepository withdrawHistoryRepository;
    private final AccountRepository accountRepository;

    public Account checkSender(String senderPrivateKey) {

        Account sender = null;

        try {
            sender = accountRepository.findOneByPublicKey(senderPrivateKey);
            return sender;
        } catch(Exception e) {
//            throw new NotExistAccountException("Sender's account do not exist.");
            return null;
        }
    }

    public int checkMoney (Account sender, int amount) {

//        Account sender = accountRepository.findOne(senderId);
        if (sender.getMoney() < amount) {
            return -1;
//            throw new NotEnoughMoneyException("잔액이 부족합니다.");
        } else {
            return amount;
        }
    }

    public WithdrawHistory findOne(Long id) {
        return withdrawHistoryRepository.findOne(id);
    }
}
