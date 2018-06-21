package de.funkedigital.fuzo.contentservice.models;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class LastQueueResult {

    private LocalDateTime processedAt;

    private String message;

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(LocalDateTime processedAt) {
        this.processedAt = processedAt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "LastQueueResult{" +
                "processedAt=" + processedAt +
                ", message='" + message + '\'' +
                '}';
    }
}
