package terafintech.terabank;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import terafintech.terabank.config.ConfigProperties;
import terafintech.terabank.domain.Account;
import terafintech.terabank.token.JwtTokenProvider;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInitSender();
        initService.dbInitReceiver();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final ConfigProperties configProperties;
        private final EntityManager em;

        public void dbInitSender() {
            Account sender = createAccount("sender", 100000, "123456", "654321");
            em.persist(sender);
        }

        public void dbInitReceiver() {
            Account receiver = createAccount("receiver", 0, "234567", "765432");
            em.persist(receiver);
        }

        private Account createAccount(String userId, int balance, String publicKey, String privateKey) {
            Account account = new Account();
            account.setUserId(userId);
            account.setBalance(balance);
//            account.setPublicKey(new JwtTokenProvider().createToken(account.getUserId(), configProperties.getPublicKey()));
//            account.setPrivateKey(new JwtTokenProvider().createToken(account.getUserId(), configProperties.getPrivateKey()));
            account.setPublicKey(publicKey);
            account.setPrivateKey(privateKey);
            return account;
        }
    }
}
