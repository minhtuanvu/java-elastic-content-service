package de.funkedigital.fuzo.contentservice.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

public class Section {
    private Long sectionId;
    private String name;
    private String uniqueName;
    private String directoryName;
    private String directoryPath;
    private String state;

    @JsonProperty("subsections")
    private List<Section> subSections;

    public Section() {
    }

    public Section(Long sectionId, String name, String uniqueName, String directoryName, String directoryPath, String state, List<Section> subSections) {
        this.sectionId = sectionId;
        this.name = name;
        this.uniqueName = uniqueName;
        this.directoryName = directoryName;
        this.directoryPath = directoryPath;
        this.state = state;
        this.subSections = subSections;
    }

    public Long getSectionId() {
        return sectionId;
    }

    public void setSectionId(Long sectionId) {
        this.sectionId = sectionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUniqueName() {
        return uniqueName;
    }

    public void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }

    public String getDirectoryName() {
        return directoryName;
    }

    public void setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
    }

    public String getDirectoryPath() {
        return directoryPath;
    }

    public void setDirectoryPath(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    public List<Section> getSubSections() {
        return subSections;
    }

    public void setSubSections(List<Section> subSections) {
        this.subSections = subSections;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Section section = (Section) o;
        return Objects.equals(sectionId, section.sectionId) &&
                Objects.equals(name, section.name) &&
                Objects.equals(uniqueName, section.uniqueName) &&
                Objects.equals(directoryName, section.directoryName) &&
                Objects.equals(directoryPath, section.directoryPath) &&
                Objects.equals(state, section.state);
    }

    @Override
    public int hashCode() {

        return Objects.hash(sectionId, name, uniqueName, directoryName, directoryPath, state);
    }
}
