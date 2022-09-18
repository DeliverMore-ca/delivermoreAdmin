package ca.admin.delivermore.data.service;

import ca.admin.delivermore.data.entity.DriverPayoutEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface DriverPayoutRepository extends JpaRepository<DriverPayoutEntity, UUID> {
    @Query("select t from DriverPayoutEntity t WHERE t.creationDateTime BETWEEN :fromDate AND :toDate")
    List<DriverPayoutEntity> search(@Param("fromDate") LocalDateTime fromDate, @Param("toDate") LocalDateTime toDate);

}
