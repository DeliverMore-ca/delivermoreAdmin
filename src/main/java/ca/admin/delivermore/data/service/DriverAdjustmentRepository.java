package ca.admin.delivermore.data.service;

import ca.admin.delivermore.data.entity.DriverAdjustment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface DriverAdjustmentRepository extends JpaRepository<DriverAdjustment, Long> {
    @Query("select d from DriverAdjustment d where d.fleetId = ?1 and d.adjustmentDate between ?2 and ?3")
    List<DriverAdjustment> findByFleetIdAndAdjustmentDateBetween(Long fleetId, LocalDate adjustmentDateStart, LocalDate adjustmentDateEnd);

    @Query("select distinct d.fleetId from DriverAdjustment d where d.adjustmentDate between ?1 and ?2")
    List<Long> findDistinctFleetIdByAdjustmentDateBetween(LocalDate adjustmentDateStart, LocalDate adjustmentDateEnd);



}
