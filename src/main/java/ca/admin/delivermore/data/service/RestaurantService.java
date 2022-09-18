package ca.admin.delivermore.data.service;

import ca.admin.delivermore.data.entity.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class RestaurantService {
    private final RestaurantRepository repository;

    @Autowired
    public RestaurantService(RestaurantRepository repository) {
        this.repository = repository;
    }

    public Optional<Restaurant> get(UUID id) {
        return repository.findById(id);
    }

    public Restaurant getByFormId(Long id) {
        return repository.findByFormId(id);
    }

    public Restaurant getByRestaurantId(Long id) {
        return repository.findByRestaurantId(id);
    }

    public Restaurant update(Restaurant entity) {
        return repository.save(entity);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public Page<Restaurant> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }


}
