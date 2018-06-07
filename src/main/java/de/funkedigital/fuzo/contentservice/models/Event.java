package de.funkedigital.fuzo.contentservice.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Objects;
import java.util.Optional;

import javax.validation.constraints.NotNull;

public class Event {

    public Event() {
    }

    public Event(@NotNull ActionType actionType,
                 @NotNull Long objectId,
                 @NotNull JsonNode payload) {
        this.actionType = actionType;
        this.objectId = objectId;
        this.payload = payload;
    }

    public enum ActionType {
        CREATE, UPDATE;

        @JsonCreator
        public static ActionType fromString(String key) {
            return Optional.ofNullable(key)
                    .map(s -> ActionType.valueOf(s.toUpperCase()))
                    .orElse(null);
        }

        @JsonValue
        public String getKey() {
            return toString();
        }


    }

    @NotNull
    private ActionType actionType;

    @NotNull
    private Long objectId;

    @NotNull
    private JsonNode payload;

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    public JsonNode getPayload() {
        return payload;
    }

    public void setPayload(JsonNode payload) {
        this.payload = payload;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Event event = (Event) o;
        return actionType == event.actionType &&
                Objects.equals(objectId, event.objectId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(actionType, objectId);
    }

    @Override
    public String toString() {
        return "Event{" +
                "actionType=" + actionType +
                ", objectId=" + objectId +
                '}';
    }
}
