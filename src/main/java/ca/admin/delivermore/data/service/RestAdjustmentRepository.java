package ca.admin.delivermore.data.service;

import ca.admin.delivermore.data.entity.RestAdjustment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface RestAdjustmentRepository extends JpaRepository<RestAdjustment, Long> {

    @Query("select d from RestAdjustment d where d.restaurantId = ?1 and d.adjustmentDate between ?2 and ?3")
    List<RestAdjustment> findByRestaurantIdAndAdjustmentDateBetween(Long restaurantId, LocalDate adjustmentDateStart, LocalDate adjustmentDateEnd);

}
