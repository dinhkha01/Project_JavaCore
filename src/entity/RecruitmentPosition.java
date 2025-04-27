package entity;

import java.math.BigDecimal;
import java.sql.Date;

public class RecruitmentPosition {
    private int id;
    private String name;
    private String description;
    private BigDecimal minSalary;
    private BigDecimal maxSalary;
    private int minExperience;
    private Date createdDate;
    private Date expiredDate;


    public RecruitmentPosition(int id, String name, String description, BigDecimal minSalary, BigDecimal maxSalary, int minExperience, Date createdDate, Date expiredDate, Date createdAt, Date updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.minSalary = minSalary;
        this.maxSalary = maxSalary;
        this.minExperience = minExperience;
        this.createdDate = createdDate;
        this.expiredDate = expiredDate;

    }

    // Constructors
    public RecruitmentPosition() {}

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getMinSalary() {
        return minSalary;
    }

    public void setMinSalary(BigDecimal minSalary) {
        this.minSalary = minSalary;
    }

    public BigDecimal getMaxSalary() {
        return maxSalary;
    }

    public void setMaxSalary(BigDecimal maxSalary) {
        this.maxSalary = maxSalary;
    }

    public int getMinExperience() {
        return minExperience;
    }

    public void setMinExperience(int minExperience) {
        this.minExperience = minExperience;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(Date expiredDate) {
        this.expiredDate = expiredDate;
    }


    @Override
    public String toString() {
        return "RecruitmentPosition{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", minSalary=" + minSalary +
                ", maxSalary=" + maxSalary +
                ", minExperience=" + minExperience +
                ", createdDate=" + createdDate +
                ", expiredDate=" + expiredDate +
                '}';
    }
}