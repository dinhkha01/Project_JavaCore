package entity;

public class RecruitmentPositionTechnology {
    private int recruitmentPositionId;
    private int technologyId;

    // Constructors
    public RecruitmentPositionTechnology() {}

    public RecruitmentPositionTechnology(int recruitmentPositionId, int technologyId) {
        this.recruitmentPositionId = recruitmentPositionId;
        this.technologyId = technologyId;
    }

    // Getters and Setters
    public int getRecruitmentPositionId() {
        return recruitmentPositionId;
    }

    public void setRecruitmentPositionId(int recruitmentPositionId) {
        this.recruitmentPositionId = recruitmentPositionId;
    }

    public int getTechnologyId() {
        return technologyId;
    }

    public void setTechnologyId(int technologyId) {
        this.technologyId = technologyId;
    }

    @Override
    public String toString() {
        return "RecruitmentPositionTechnology{" +
                "recruitmentPositionId=" + recruitmentPositionId +
                ", technologyId=" + technologyId +
                '}';
    }
}