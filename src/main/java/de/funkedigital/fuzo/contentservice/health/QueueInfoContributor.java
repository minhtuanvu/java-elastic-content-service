package de.funkedigital.fuzo.contentservice.health;

import de.funkedigital.fuzo.contentservice.models.LastQueueResult;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty("consumer.enabled")
public class QueueInfoContributor implements InfoContributor {

    private final LastQueueResult lastQueueResult;

    QueueInfoContributor(LastQueueResult lastQueueResult) {
        this.lastQueueResult = lastQueueResult;
    }

    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("queue", lastQueueResult.toString());
    }
}
