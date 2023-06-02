package ca.admin.delivermore.data.service.webpush;

import ca.admin.delivermore.data.entity.SubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, Long> {
    @Query("select s from SubscriptionEntity s where s.driverId = ?1")
    List<SubscriptionEntity> findByDriverId(Long driverId);

    @Transactional
    @Modifying
    @Query("delete from SubscriptionEntity s where s.driverId = ?1 and s.clientId = ?2")
    void deleteByDriverIdAndClientId(Long driverId, String clientId);

    //TODO: call delete by driver when a driver is removed from Tookan
    @Transactional
    @Modifying
    @Query("delete from SubscriptionEntity s where s.driverId = ?1")
    void deleteByDriverId(Long driverId);


}
