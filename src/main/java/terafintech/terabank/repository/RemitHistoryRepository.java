package terafintech.terabank.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import terafintech.terabank.domain.RemitHistory;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class RemitHistoryRepository {

    private final EntityManager em;

    public void save(RemitHistory remitHistory) {
        em.persist(remitHistory);
    }

    public RemitHistory findOne(Long id) {
        return em.find(RemitHistory.class, id);
    }
}
