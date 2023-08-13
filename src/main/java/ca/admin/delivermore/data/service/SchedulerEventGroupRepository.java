package ca.admin.delivermore.data.service;

import ca.admin.delivermore.data.scheduler.SchedulerEventGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchedulerEventGroupRepository extends JpaRepository<SchedulerEventGroup, Long> {


}
