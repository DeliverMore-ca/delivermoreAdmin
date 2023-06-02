package ca.admin.delivermore.data.generator;

import ca.admin.delivermore.collector.data.service.RestaurantRepository;
import ca.admin.delivermore.collector.data.service.OrderDetailRepository;
import ca.admin.delivermore.collector.data.service.DriversRepository;
import ca.admin.delivermore.data.service.*;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringComponent
public class DataGenerator {

    @Bean
    public CommandLineRunner loadData(PasswordEncoder passwordEncoder, RestaurantRepository restaurantRepository,
                                      OrdersRepository ordersRepository,
                                      OrderDetailRepository orderDetailRepository, DriversRepository driversRepository) {
        return args -> {
            Logger logger = LoggerFactory.getLogger(getClass());
            //TODO: remove data generator if not needed

            /*
            if (userRepository.count() != 0L) {
                logger.info("Using existing database");
                return;
            }
            int seed = 123;


            logger.info("Generating demo data");

            logger.info("... generating 2 User entities...");
            User user = new User();
            user.setName("John Normal");
            user.setUsername("user");
            user.setHashedPassword(passwordEncoder.encode("user"));
            user.setProfilePictureUrl(
                    "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=128&h=128&q=80");
            user.setRoles(Collections.singleton(Role.USER));
            userRepository.save(user);
            User admin = new User();
            admin.setName("Emma Powerful");
            admin.setUsername("admin");
            admin.setHashedPassword(passwordEncoder.encode("admin"));
            admin.setProfilePictureUrl(
                    "https://images.unsplash.com/photo-1607746882042-944635dfe10e?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=128&h=128&q=80");
            admin.setRoles(Set.of(Role.USER, Role.ADMIN));
            userRepository.save(admin);

             */

            /*
            logger.info("... generating 100 Orders entities...");
            ExampleDataGenerator<Orders> ordersRepositoryGenerator = new ExampleDataGenerator<>(Orders.class,
                    LocalDateTime.of(2022, 8, 17, 0, 0, 0));
            ordersRepositoryGenerator.setData(Orders::setTaskid, DataType.NUMBER_UP_TO_100);
            ordersRepositoryGenerator.setData(Orders::setStoreid, DataType.NUMBER_UP_TO_100);
            ordersRepositoryGenerator.setData(Orders::setStorename, DataType.WORD);
            ordersRepositoryGenerator.setData(Orders::setStreet, DataType.ADDRESS);
            ordersRepositoryGenerator.setData(Orders::setPostalCode, DataType.ZIP_CODE);
            ordersRepositoryGenerator.setData(Orders::setCity, DataType.CITY);
            ordersRepositoryGenerator.setData(Orders::setState, DataType.STATE);
            ordersRepositoryGenerator.setData(Orders::setCountry, DataType.COUNTRY);
            ordersRepositoryGenerator.setData(Orders::setSubtotal, DataType.NUMBER_UP_TO_100);
            ordersRepository.saveAll(ordersRepositoryGenerator.create(100, seed));
            logger.info("Generated demo data");
             */

            /*
            //get all drivers
            List<Driver> drivers = restClientService.getAllDrivers();
            for (Driver driver: drivers) {
                log.info("Driver:" + driver.getName() + " :" + driver.toString());
            }
            driversRepository.saveAll(drivers);

             */

            /*
            log.info("**************** RUNNING");
            //List<TaskDetail> taskDetailList = restClientService.getAllTasks(LocalDate.parse("2022-08-14"),LocalDate.parse("2022-08-15") );
            List<TaskDetail> taskDetailList = restClientService.getAllTasks(LocalDate.parse("2022-08-14"),LocalDate.now() );
            List<TaskEntity> taskEntityList = new ArrayList<>();
            //List<DriverPayoutEntity> driverPayoutEntities = new ArrayList<>();
            for (TaskDetail taskDetail: taskDetailList) {
                taskEntityList.add(taskDetail.getTaskEntity(restaurantRepository, orderDetailRepository, driversRepository));
            }
            taskDetailRepository.saveAll(taskEntityList);

            logger.info("loaded Tookan Tasks from REST api");

             */
        };
    }

}