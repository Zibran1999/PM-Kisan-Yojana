package com.pmkisanyojana.models;

public class StatusViewModel {

    private String id;
    private String userId;
    private String statusId;
    private String statusTime;
    private String profileID;
    private String profileImage;
    private String profileName;

    public StatusViewModel(String id, String userId, String statusId, String statusTime, String profileID, String profileImage, String profileName) {
        this.id = id;
        this.userId = userId;
        this.statusId = statusId;
        this.statusTime = statusTime;
        this.profileID = profileID;
        this.profileImage = profileImage;
        this.profileName = profileName;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getStatusId() {
        return statusId;
    }

    public String getStatusTime() {
        return statusTime;
    }

    public String getProfileID() {
        return profileID;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public String getProfileName() {
        return profileName;
    }
}
