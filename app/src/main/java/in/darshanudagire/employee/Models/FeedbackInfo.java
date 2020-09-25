package in.darshanudagire.employee.Models;

import in.darshanudagire.employee.dashboard.Schedule;

public class FeedbackInfo {
    String description,descriptionType,experience;

    public FeedbackInfo(String description, String descriptionType, String experience) {
        this.description = description;
        this.descriptionType = descriptionType;
        this.experience = experience;
    }

    public FeedbackInfo() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescriptionType() {
        return descriptionType;
    }

    public void setDescriptionType(String descriptionType) {
        this.descriptionType = descriptionType;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }
}
