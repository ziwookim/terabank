package terafintech.terabank.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import terafintech.terabank.domain.Account;
import terafintech.terabank.domain.WithdrawHistory;
import terafintech.terabank.repository.AccountRepository;
import terafintech.terabank.repository.WithdrawHistoryRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WithdrawHistoryService {

    private final WithdrawHistoryRepository withdrawHistoryRepository;
    private final AccountRepository accountRepository;

    public WithdrawHistory findOne(Long id) {
        return withdrawHistoryRepository.findOne(id);
    }

    @Transactional
    public Long withdraw(String senderPrivateKey, int amount)
    {
        Account sender = null;
        if(amount > 0) {
            sender = checkSender(senderPrivateKey);

            if(sender != null && amount > 0) {
                amount = checkMoney(sender, amount);
            }
        }

        WithdrawHistory withdrawHistory = WithdrawHistory.createWithdraw(sender, amount);

        withdrawHistoryRepository.save(withdrawHistory);

        return withdrawHistory.getId();
    }

    public Account checkSender(String senderPrivateKey) {

        Account sender = null;

        try {
            sender = accountRepository.findOneByPrivateKey(senderPrivateKey);
            return sender;
        } catch(Exception e) {
//            throw new NotExistAccountException("Sender's account do not exist.");
            return null;
        }
    }

    public int checkMoney (Account sender, int amount) {

//        Account sender = accountRepository.findOne(senderId);
        if (sender.getBalance() < amount) {
            return -1;
//            throw new NotEnoughMoneyException("잔액이 부족합니다.");
        } else {
            return amount;
        }
    }
}
