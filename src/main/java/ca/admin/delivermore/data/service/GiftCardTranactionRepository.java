package ca.admin.delivermore.data.service;

import ca.admin.delivermore.data.entity.GiftCardTranactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GiftCardTranactionRepository extends JpaRepository<GiftCardTranactionEntity, Long> {
    @Query("select g from GiftCardTranactionEntity g where g.code = ?1 order by g.transactionDateTime DESC")
    List<GiftCardTranactionEntity> findByCodeOrderByTransactionDateTimeDesc(String code);


}
