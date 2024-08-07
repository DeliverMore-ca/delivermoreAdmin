package ca.admin.delivermore.data.service;

import ca.admin.delivermore.data.scheduler.SchedulerEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SchedulerEventRepository extends JpaRepository<SchedulerEvent, Long> {
    @Query("select s from SchedulerEvent s where s.start between ?1 and ?2 and s.teamId = ?3")
    List<SchedulerEvent> findByStartBetween(LocalDateTime startStart, LocalDateTime startEnd, Long teamId);

    @Query("select s from SchedulerEvent s where s.start between ?2 and ?3 and s.resourceId = ?1 and s.teamId = ?4")
    List<SchedulerEvent> findByResourceIdAndStartBetween(String resourceId, LocalDateTime startStart, LocalDateTime startEnd, Long teamId);

    @Query("select s from SchedulerEvent s where s.published = ?1 and s.start between ?2 and ?3 and s.teamId = ?4 order by s.start, s.end")
    List<SchedulerEvent> findByPublishedAndStartBetween(Boolean published, LocalDateTime startStart, LocalDateTime startEnd, Long teamId);

    @Query("select s from SchedulerEvent s where s.teamId = ?1 order by s.start, s.end")
    List<SchedulerEvent> findByTeamIdOrderByStartAscEndAsc(Long teamId);


    List<SchedulerEvent> findByEventGroup_IdAndStartGreaterThanEqual(Long id, LocalDateTime start);

    List<SchedulerEvent> findByEventGroup_Id(Long id);

}
