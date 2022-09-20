package terafintech.terabank.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import terafintech.terabank.domain.Account;
import terafintech.terabank.domain.DepositHistory;
import terafintech.terabank.repository.AccountRepository;
import terafintech.terabank.repository.DepositHistoryRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DepositHistoryService {

    private final DepositHistoryRepository depositHistoryRepository;
    private final AccountRepository accountRepository;

    public Account checkReceiver(String receiverPublicKey) {

        Account receiver = null;
        try {
            receiver = accountRepository.findOneByPublicKey(receiverPublicKey);
            return receiver;
        } catch(Exception e) {
//            throw new NotExistAccountException("Receiver's account do not exist.");
            return null;
        }
    }

    public DepositHistory findOne(Long id) {
        return depositHistoryRepository.findOne(id);
    }

}
