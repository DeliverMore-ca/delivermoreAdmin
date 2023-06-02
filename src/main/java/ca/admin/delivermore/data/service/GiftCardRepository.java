package ca.admin.delivermore.data.service;

import ca.admin.delivermore.data.entity.GiftCardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GiftCardRepository extends JpaRepository<GiftCardEntity, Long> {
    @Query("select g from GiftCardEntity g where upper(g.code) = upper(?1)")
    GiftCardEntity findByCodeIgnoreCase(String code);

    @Query("select (count(g) > 0) from GiftCardEntity g where upper(g.code) = upper(?1)")
    boolean existsByCodeIgnoreCase(String code);

    @Query("select g from GiftCardEntity g order by g.issued DESC")
    List<GiftCardEntity> findAllOrderByIssuedDesc();

}
