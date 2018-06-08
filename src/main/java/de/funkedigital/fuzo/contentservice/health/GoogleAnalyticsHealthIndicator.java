package de.funkedigital.fuzo.contentservice.health;

import de.funkedigital.fuzo.contentservice.repo.TopListRepo;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty("toplist.enabled")
public class GoogleAnalyticsHealthIndicator implements HealthIndicator {

    private final TopListRepo topListRepo;

    GoogleAnalyticsHealthIndicator(TopListRepo topListRepo) {
        this.topListRepo = topListRepo;
    }

    @Override
    public Health health() {
        try {
            return topListRepo.health().map(healthStatus -> Health.status(healthStatus.getStatus()).build());
        } catch (Exception e) {
            return Health.down(e).build();
        }
    }
}
