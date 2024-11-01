package ru.otus.hw.actuators;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import ru.otus.hw.repositories.BookRepository;

@Component
@RequiredArgsConstructor
public class BookHealthIndicator implements HealthIndicator {

    private final BookRepository bookRepository;

    @Override
    public Health health() {
        if (bookRepository.count() > 0) {
            return Health
                    .up()
                    .withDetail("message", "Everything is great!")
                    .build();
        } else {
            return Health
                    .down()
                    .withDetail("message", "Something is wrong!")
                    .build();
        }
    }
}
