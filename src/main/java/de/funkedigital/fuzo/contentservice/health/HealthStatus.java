package de.funkedigital.fuzo.contentservice.health;

import de.funkedigital.fuzo.contentservice.traits.Mappable;

import org.springframework.boot.actuate.health.Status;

public class HealthStatus implements Mappable<HealthStatus> {

    private Status status;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
