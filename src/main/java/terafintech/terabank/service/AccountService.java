package terafintech.terabank.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import terafintech.terabank.config.ConfigProperties;
import terafintech.terabank.domain.Account;
import terafintech.terabank.domain.TransactionType;
import terafintech.terabank.repository.AccountRepository;
import terafintech.terabank.token.JwtTokenProvider;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountService {

    private final ConfigProperties configProperties;

    private final AccountRepository accountRepository;

    /**
     * 계좌 등록
     */
    @Transactional
    public Long apply(Account account) {
        // 중복 userId 검증 && 4자리 이상 값 검증
        validateDuplicateAccount(account);

        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();
        // 토큰 생성
        account.setPublicKey(jwtTokenProvider.createToken(account.getUserId(), configProperties.getToken()));
        account.setPrivateKey(jwtTokenProvider.createToken(account.getUserId(), configProperties.getToken()));
        account.setMoney(0);

        accountRepository.save(account);

        return account.getId();
    }

    public Account findOne(Long accountId) {
        return accountRepository.findOne(accountId);
    }

    @Transactional
    public void applyTransaction(Long id, TransactionType transactionType, int amount) {
        Account account = accountRepository.findOne(id);

        if(transactionType.equals(TransactionType.DEPOSIT)) {
            account.setMoney(account.getMoney() + amount);
        } else if(transactionType.equals(TransactionType.WITHDRAWAL)) {
            account.setMoney(account.getMoney() - amount);
        }
    }

    public void validateDuplicateAccount(Account account) {
        List<Account> findAccounts = accountRepository.findByUserId(account.getUserId());

        if(!findAccounts.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 사용자 ID 입니다.");
        }

        if(account.getUserId().length() < 4) {
            throw new IllegalStateException("사용자 ID는 4자리 이상 값으로 입력하세요.");
        }
    }
}
