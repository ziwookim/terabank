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
            Account sender = createAccount("sender", 100000,
                    "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzZW5kZXIiLCJpYXQiOjE2NjM4NTIxMjN9.Ea9QUVeBNEG2eoxCzH7hnxjYWJWDsGoohJt5G09EMz4",
                    "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzZW5kZXIiLCJpYXQiOjE2NjM4NTIxMjN9.FVQmVG9OP7Xf7-n9MNA2_n8598BdHgCFvm0CXguiyuc");
            em.persist(sender);
        }

        public void dbInitReceiver() {
            Account receiver = createAccount("receiver", 0,
                    "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyZWNlaXZlciIsImlhdCI6MTY2Mzg1MjE0NH0.LuwsgcMNB4w0ApquZ2i6-8PLiqP8X18Br6esywFLjmY",
                    "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyZWNlaXZlciIsImlhdCI6MTY2Mzg1MjE0NH0.wCdEajB9TQ0e6gZl1I5crahLazIkjiv0VOsoKRqC2RM");
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
