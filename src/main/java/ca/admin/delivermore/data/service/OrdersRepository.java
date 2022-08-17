package ca.admin.delivermore.data.service;

import ca.admin.delivermore.data.entity.Orders;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders, UUID> {

}