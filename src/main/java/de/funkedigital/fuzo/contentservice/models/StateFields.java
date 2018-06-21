package de.funkedigital.fuzo.contentservice.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StateFields {

    private String state;

    private StateFields homeSection;

    public StateFields() {
    }

    public StateFields(String state, StateFields homeSection) {
        this.state = state;
        this.homeSection = homeSection;
    }

    public StateFields(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public StateFields getHomeSection() {
        return homeSection;
    }

    public void setHomeSection(StateFields homeSection) {
        this.homeSection = homeSection;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StateFields that = (StateFields) o;
        return Objects.equals(state, that.state) &&
                Objects.equals(homeSection, that.homeSection);
    }

    @Override
    public int hashCode() {

        return Objects.hash(state, homeSection);
    }
}
