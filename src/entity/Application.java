package entity;

import java.sql.Timestamp;

public class Application {
    private int id;
    private int candidateId;
    private int recruitmentPositionId;
    private String cvUrl;
    private String progress;
    private Timestamp interviewRequestDate;
    private String interviewRequestResult;
    private String interviewLink;
    private Timestamp interviewTime;
    private String interviewResult;
    private String interviewResultNote;
    private Timestamp destroyAt;
    private String destroyReason;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Application(int id, int candidateId, int recruitmentPositionId, String cvUrl, String progress, Timestamp interviewRequestDate, String interviewRequestResult, String interviewLink, Timestamp interviewTime, String interviewResult, String interviewResultNote, Timestamp destroyAt, String destroyReason, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.candidateId = candidateId;
        this.recruitmentPositionId = recruitmentPositionId;
        this.cvUrl = cvUrl;
        this.progress = progress;
        this.interviewRequestDate = interviewRequestDate;
        this.interviewRequestResult = interviewRequestResult;
        this.interviewLink = interviewLink;
        this.interviewTime = interviewTime;
        this.interviewResult = interviewResult;
        this.interviewResultNote = interviewResultNote;
        this.destroyAt = destroyAt;
        this.destroyReason = destroyReason;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    // Constructors
    public Application() {}

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(int candidateId) {
        this.candidateId = candidateId;
    }

    public int getRecruitmentPositionId() {
        return recruitmentPositionId;
    }

    public void setRecruitmentPositionId(int recruitmentPositionId) {
        this.recruitmentPositionId = recruitmentPositionId;
    }

    public String getCvUrl() {
        return cvUrl;
    }

    public void setCvUrl(String cvUrl) {
        this.cvUrl = cvUrl;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public Timestamp getInterviewRequestDate() {
        return interviewRequestDate;
    }

    public void setInterviewRequestDate(Timestamp interviewRequestDate) {
        this.interviewRequestDate = interviewRequestDate;
    }

    public String getInterviewRequestResult() {
        return interviewRequestResult;
    }

    public void setInterviewRequestResult(String interviewRequestResult) {
        this.interviewRequestResult = interviewRequestResult;
    }

    public String getInterviewLink() {
        return interviewLink;
    }

    public void setInterviewLink(String interviewLink) {
        this.interviewLink = interviewLink;
    }

    public Timestamp getInterviewTime() {
        return interviewTime;
    }

    public void setInterviewTime(Timestamp interviewTime) {
        this.interviewTime = interviewTime;
    }

    public String getInterviewResult() {
        return interviewResult;
    }

    public void setInterviewResult(String interviewResult) {
        this.interviewResult = interviewResult;
    }

    public String getInterviewResultNote() {
        return interviewResultNote;
    }

    public void setInterviewResultNote(String interviewResultNote) {
        this.interviewResultNote = interviewResultNote;
    }

    public Timestamp getDestroyAt() {
        return destroyAt;
    }

    public void setDestroyAt(Timestamp destroyAt) {
        this.destroyAt = destroyAt;
    }

    public String getDestroyReason() {
        return destroyReason;
    }

    public void setDestroyReason(String destroyReason) {
        this.destroyReason = destroyReason;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Application{" +
                "id=" + id +
                ", candidateId=" + candidateId +
                ", recruitmentPositionId=" + recruitmentPositionId +
                ", progress='" + progress + '\'' +
                '}';
    }
}