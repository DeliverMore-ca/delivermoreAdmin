package ca.admin.delivermore.data.generator;

import ca.admin.delivermore.data.Role;
import ca.admin.delivermore.data.entity.*;
import ca.admin.delivermore.data.global.OrderDetails;
import ca.admin.delivermore.data.service.*;
import ca.admin.delivermore.tookan.Driver;
import ca.admin.delivermore.tookan.TaskDetail;
import com.vaadin.exampledata.DataType;
import com.vaadin.exampledata.ExampleDataGenerator;
import com.vaadin.flow.spring.annotation.SpringComponent;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringComponent
public class DataGenerator {

    @Bean
    public CommandLineRunner loadData(PasswordEncoder passwordEncoder, UserRepository userRepository, RestaurantRepository restaurantRepository,
                                      OrdersRepository ordersRepository, TaskDetailRepository taskDetailRepository,
                                      OrderDetailRepository orderDetailRepository, DriversRepository driversRepository, DriverPayoutRepository driverPayoutRepository) {
        return args -> {
            Logger logger = LoggerFactory.getLogger(getClass());
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

            //TODO: Build the Restaurant list - later change this to a database table
            restaurantRepository.save(new Restaurant(402970L, "A&W", 0.1, 551118L));
            restaurantRepository.save(new Restaurant(402971L, "Booster Juice", 0.1));
            restaurantRepository.save(new Restaurant(402972L, "Boston Pizza", 0.1));
            restaurantRepository.save(new Restaurant(0L, "Custom", 0.0));
            restaurantRepository.save(new Restaurant(405508L, "Dairy Queen", 0.0));
            restaurantRepository.save(new Restaurant(402977L, "Dobre", 0.1));
            restaurantRepository.save(new Restaurant(402973L, "Edo Japan", 0.1, 551316L));
            restaurantRepository.save(new Restaurant(402974L, "Fritou Chicken", 0.1, 551318L));
            restaurantRepository.save(new Restaurant(402976L, "Husky House Restaurant Strathmore", 0.1));
            restaurantRepository.save(new Restaurant(402979L, "Imperial Dragon", 0.1, 551315L));
            restaurantRepository.save(new Restaurant(402981L, "Little Caesars", 0.1));
            restaurantRepository.save(new Restaurant(402982L, "McDonald's",0.1, 3.5));
            restaurantRepository.save(new Restaurant(402983L, "Mike's Bar & Grill",0.0,2.5, 0.0, 551314L));
            restaurantRepository.save(new Restaurant(402985L, "OPA!",0.1,3.0));
            restaurantRepository.save(new Restaurant(402986L, "Original Joe's Strathmore",0.1));
            restaurantRepository.save(new Restaurant(402987L, "Papa John's",0.15, 551465L));
            restaurantRepository.save(new Restaurant(402990L, "Pho Minh",0.1, 551317L));
            restaurantRepository.save(new Restaurant(402992L, "Pizza 249",0.1, 552128L));
            restaurantRepository.save(new Restaurant(402993L, "Quesada",0.1,4.0));
            restaurantRepository.save(new Restaurant(402994L, "Saffron Bistro",0.0));
            restaurantRepository.save(new Restaurant(402995L, "Smiley's",0.0));
            restaurantRepository.save(new Restaurant(402996L, "Taco Time",0.1,4.0));
            restaurantRepository.save(new Restaurant(402057L, "Demo",0.1,550957L));

            OrderDetails orderDetails = new OrderDetails();
            orderDetails.loadFromCSV("global_restaurants_orders.csv");
            orderDetailRepository.saveAll(orderDetails.getOrderDetailList());

            RestClientService restClientService = new RestClientService();
            //get all drivers
            List<Driver> drivers = restClientService.getAllDrivers();
            for (Driver driver: drivers) {
                System.out.println("Driver:" + driver.getName() + " :" + driver.toString());
            }
            driversRepository.saveAll(drivers);

            //TODO:get tasks from tookan since the last date - change later
            List<TaskDetail> taskDetailList = restClientService.getAllTasks(LocalDate.parse("2022-08-14"),LocalDate.parse("2022-08-15") );
            //List<TaskDetail> taskDetailList = restClientService.getAllTasks(LocalDate.parse("2022-08-14"),LocalDate.now() );
            List<TaskEntity> taskEntityList = new ArrayList<>();
            List<DriverPayoutEntity> driverPayoutEntities = new ArrayList<>();
            for (TaskDetail taskDetail: taskDetailList) {
                taskEntityList.add(taskDetail.getTaskEntity(restaurantRepository, orderDetailRepository, driversRepository));
                DriverPayoutEntity driverPayoutEntity = taskDetail.getDriverPayoutEntity(restaurantRepository, orderDetailRepository, driversRepository);
                if(driverPayoutEntity!=null){
                    driverPayoutEntities.add(driverPayoutEntity);
                }
            }
            taskDetailRepository.saveAll(taskEntityList);

            //Sort DriverPayoutEntities
            Collections.sort(driverPayoutEntities, Comparator.comparing(DriverPayoutEntity::getFleetName)
                    .thenComparing(DriverPayoutEntity::getCreationDateTime));

            driverPayoutRepository.saveAll(driverPayoutEntities);

            logger.info("loaded Tookan Tasks from REST api");
        };
    }

}