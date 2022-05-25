package com.example.trainingsystem.actuator;

import com.example.trainingsystem.repository.DictionaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;

@Component
public class DictionaryIndicator implements HealthIndicator {

    private final DictionaryRepository repository;

    @Autowired
    public DictionaryIndicator(DictionaryRepository repository) {
        this.repository = repository;
    }

    @Override
    public Health health() {
        try {
            repository.findAll();
            return Health.up().withDetail("message", "Cool!").build();
        } catch (Exception ex) {
            return Health.down()
                    .status(Status.DOWN)
                    .withDetail("message", ex.getMessage())
                    .build();
        }

    }
}
