package ca.admin.delivermore.data.service;

import ca.admin.delivermore.data.entity.DriverPayoutEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DriverPayoutService {
    private DriverPayoutRepository driverPayoutRepository;

    public DriverPayoutService(DriverPayoutRepository driverPayoutRepository) {
        this.driverPayoutRepository = driverPayoutRepository;
    }

    public List<DriverPayoutEntity> findAllDriverPayouts(LocalDateTime fromDate, LocalDateTime toDate) {
        System.out.println("findAllDriverPayouts: from:" + fromDate + " to:" + toDate);
        if (fromDate == null || toDate == null) {
            System.out.println("findAllDriverPayouts: running search with default dates");
            return driverPayoutRepository.search(LocalDateTime.parse("2022-08-14T00:00:00"), LocalDateTime.parse("2022-08-14T23:59:59"));
        } else {
            System.out.println("findAllDriverPayouts: running search using passed in dates");
            return driverPayoutRepository.search(fromDate, toDate);
        }
    }

    public long countDriverPayout() {
        return driverPayoutRepository.count();
    }

    public void deleteDriverPayout(DriverPayoutEntity driverPayoutEntity) {
        driverPayoutRepository.delete(driverPayoutEntity);
    }

    public void saveDriverPayout(DriverPayoutEntity driverPayoutEntity) {
        if (driverPayoutEntity == null) {
            System.err.println("DriverPayoutEntity is null. Are you sure you have connected your form to the application?");
            return;
        }
        driverPayoutRepository.save(driverPayoutEntity);
    }


}
