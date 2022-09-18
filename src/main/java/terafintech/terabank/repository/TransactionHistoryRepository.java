package terafintech.terabank.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import terafintech.terabank.domain.TransactionHistory;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class TransactionHistoryRepository {

    private EntityManager em;

    public void save(TransactionHistory transactionHistory) {
        em.persist(transactionHistory);
    }

    public TransactionHistory findOne(Long id) {
        return em.find(TransactionHistory.class, id);
    }
}
