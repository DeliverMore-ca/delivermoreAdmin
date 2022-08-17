package ca.admin.delivermore.data.generator;

import ca.admin.delivermore.data.Role;
import ca.admin.delivermore.data.entity.Orders;
import ca.admin.delivermore.data.entity.User;
import ca.admin.delivermore.data.service.OrdersRepository;
import ca.admin.delivermore.data.service.UserRepository;
import com.vaadin.exampledata.DataType;
import com.vaadin.exampledata.ExampleDataGenerator;
import com.vaadin.flow.spring.annotation.SpringComponent;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringComponent
public class DataGenerator {

    @Bean
    public CommandLineRunner loadData(PasswordEncoder passwordEncoder, UserRepository userRepository,
            OrdersRepository ordersRepository) {
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
        };
    }

}