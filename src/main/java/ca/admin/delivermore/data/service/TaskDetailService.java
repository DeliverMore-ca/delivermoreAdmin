package ca.admin.delivermore.data.service;

import ca.admin.delivermore.collector.data.service.TaskDetailRepository;
import ca.admin.delivermore.collector.data.entity.DriverPayoutEntity;
import ca.admin.delivermore.collector.data.entity.TaskEntity;
import ca.admin.delivermore.views.restaurants.RestView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskDetailService {
    private Logger log = LoggerFactory.getLogger(TaskDetailService.class);
    private TaskDetailRepository taskDetailRepository;

    public TaskDetailService(TaskDetailRepository taskDetailRepository){

        this.taskDetailRepository = taskDetailRepository;
    }

    public List<TaskEntity> findAllTaskDetails(LocalDateTime fromDate, LocalDateTime toDate) {
        log.info("findAllTaskDetails: from:" + fromDate + " to:" + toDate);
        if (fromDate == null || toDate == null) {
            log.info("findAllTaskDetails: running search with default dates");
            return taskDetailRepository.search(LocalDateTime.parse("2022-08-14T00:00:00"), LocalDateTime.parse("2022-08-14T23:59:59"));
        } else {
            log.info("findAllTaskDetails: running search using passed in dates");
            return taskDetailRepository.search(fromDate, toDate);
        }
    }

    public List<DriverPayoutEntity> findAllDriverPayouts(LocalDateTime fromDate, LocalDateTime toDate) {
        log.info("findAllDriverPayouts: from:" + fromDate + " to:" + toDate);
        if (fromDate == null || toDate == null) {
            log.info("findAllDriverPayouts: running search with default dates");
            return taskDetailRepository.getDriverPayout(LocalDateTime.parse("2022-08-14T00:00:00"), LocalDateTime.parse("2022-08-14T23:59:59"));
        } else {
            log.info("findAllDriverPayouts: running search using passed in dates");
            return taskDetailRepository.getDriverPayout(fromDate, toDate);
        }
    }

    public long countTaskDetail() {
        return taskDetailRepository.count();
    }

    public void deleteTaskDetail(TaskEntity taskEntity) {
        taskDetailRepository.delete(taskEntity);
    }

    public void saveTaskDetail(TaskEntity taskEntity) {
        if (taskEntity == null) {
            System.err.println("TaskDetail is null. Are you sure you have connected your form to the application?");
            return;
        }
        taskDetailRepository.save(taskEntity);
    }

    public TaskEntity getTaskEntityByJobId(Long jobId){
        List<TaskEntity> taskEntityList = taskDetailRepository.findByJobId(jobId);
        if(taskEntityList.size()>0){
            //return the first - there should only be one for a jobId
            return taskEntityList.get(0);
        }else{
            log.info("getTaskEntityByJobId: none found for:" + jobId + " so returning null");
            return null;
        }

    }

}
