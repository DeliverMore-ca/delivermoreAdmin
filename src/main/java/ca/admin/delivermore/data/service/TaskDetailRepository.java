package ca.admin.delivermore.data.service;

import ca.admin.delivermore.data.entity.TaskEntity;
import ca.admin.delivermore.tookan.TaskDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface  TaskDetailRepository extends JpaRepository<TaskEntity, UUID> {
    @Query("select t from TaskEntity t WHERE t.completedDate BETWEEN :fromDate AND :toDate")
    List<TaskEntity> search(@Param("fromDate") LocalDateTime fromDate, @Param("toDate") LocalDateTime toDate);

}

/*

    @Query("select t from TaskEntity t ")
    List<TaskEntity> search(@Param("fromDate") LocalDateTime fromDate, @Param("toDate") LocalDateTime toDate);

    @Query("select t from TaskEntity t " +
            "where t.completedDateTimeLocal <= :toDate " +
            "and t.completedDateTimeLocal >= :fromDate")
    List<TaskEntity> search(@Param("fromDate") LocalDateTime fromDate, @Param("toDate") LocalDateTime toDate); }


    @Query("select t from TaskEntity t " +
            "where lower(t.restaurantName) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(t.customerUsername) like lower(concat('%', :searchTerm, '%'))")
    List<TaskEntity> search(@Param("searchTerm") String searchTerm); }

 */
