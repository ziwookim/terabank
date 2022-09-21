package terafintech.terabank.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import terafintech.terabank.domain.Account;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AccountRepository {

    private final EntityManager em;

    public void save(Account account) {
        em.persist(account);
    }

    public Account findOne(Long id) {
        return em.find(Account.class, id);
    }

    public List<Account> findByUserId(String userId) {
        return em.createQuery("select a from Account a where a.userId = :userId", Account.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    public Account findOneByPublicKey(String publicKey) {
        return em.createQuery("select a from Account a where a.publicKey = :publicKey", Account.class)
                .setParameter("publicKey", publicKey)
                .getSingleResult();
    }

    public Account findOneByPrivateKey(String privateKey) {
        return em.createQuery("select a from Account a where a.privateKey = :privateKey", Account.class)
                .setParameter("privateKey", privateKey)
                .getSingleResult();
    }

    public Account findOneBySecretKey(String privateKey) {
        return em.createQuery("select a from Account a where a.privateKey = :privateKey", Account.class)
                .setParameter("privateKey", privateKey)
                .getSingleResult();
    }
}
