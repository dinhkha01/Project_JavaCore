package business.service.candidate.apply;

import business.DAO.candidate.apply.CandidateApplyDaoImpl;
import entity.Application;
import entity.RecruitmentPosition;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ApplicationService {
    private final CandidateApplyDaoImpl candidateApplyDao;

    public ApplicationService() {
        this.candidateApplyDao = new CandidateApplyDaoImpl();
    }

    /**
     * Get all active recruitment positions
     * @return List of active positions
     */
    public List<RecruitmentPosition> getAllActivePositions() {
        return candidateApplyDao.findAll();
    }

    /**
     * Get position details by ID
     * @param positionId The position ID
     * @return RecruitmentPosition object
     */
    public RecruitmentPosition getPositionById(int positionId) {
        return candidateApplyDao.findById(positionId);
    }

    /**
     * Apply for a position
     * @param candidateId The candidate ID
     * @param positionId The position ID
     * @param cvUrl URL to the candidate's CV
     * @return Created application object
     */
    public Application applyForPosition(int candidateId, int positionId, String cvUrl) {
        // Check if position exists
        RecruitmentPosition position = getPositionById(positionId);
        if (position == null) {
            return null;
        }

        // Create new application
        Application application = new Application();
        application.setCandidateId(candidateId);
        application.setRecruitmentPositionId(positionId);
        application.setCvUrl(cvUrl);
        application.setProgress("pending");
        application.setCreatedAt(new Timestamp(new Date().getTime()));

        // Save application
        candidateApplyDao.save(application);

        return application;
    }

    /**
     * Get all applications by candidate ID
     * @param candidateId The candidate ID
     * @return List of applications
     */
    public List<Application> getApplicationsByCandidateId(int candidateId) {
        return candidateApplyDao.getApplicationsByCandidateId(candidateId);
    }

    /**
     * Get application details by ID
     * @param applicationId The application ID
     * @return Application object
     */
    public Application getApplicationById(int applicationId) {
        return candidateApplyDao.getApplicationById(applicationId);
    }

    /**
     * Update interview response for an application
     * @param applicationId Application ID
     * @param response Response text ("Đã xác nhận" or "Từ chối")
     * @param reason Reason for rejection (can be null if confirmed)
     * @return true if successful, false otherwise
     */
    public boolean updateInterviewResponse(int applicationId, String response, String reason) {
        return candidateApplyDao.updateInterviewResponse(applicationId, response, reason);
    }

    /**
     * Cancel an application
     * @param applicationId The application ID to cancel
     */
    public void cancelApplication(int applicationId) {
        candidateApplyDao.deleteById(applicationId);
    }

    /**
     * Format position for display in list format
     * @param position Position to format
     * @return Formatted string representation
     */
    public String formatPositionForList(RecruitmentPosition position) {
        return String.format("ID: %-5d | %-25s | %s - %s | Min Exp: %d years | Expires: %s",
                position.getId(),
                truncateString(position.getName(), 25),
                position.getMinSalary(),
                position.getMaxSalary(),
                position.getMinExperience(),
                position.getExpiredDate());
    }

    /**
     * Format position details for detailed view
     * @param position Position to format
     * @return Formatted string with detailed position information
     */
    public String formatPositionDetails(RecruitmentPosition position) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== POSITION DETAILS ===\n");
        sb.append("ID: ").append(position.getId()).append("\n");
        sb.append("Name: ").append(position.getName()).append("\n");
        sb.append("Description: ").append(position.getDescription()).append("\n");
        sb.append("Salary Range: ").append(position.getMinSalary()).append(" - ").append(position.getMaxSalary()).append("\n");
        sb.append("Minimum Experience: ").append(position.getMinExperience()).append(" years\n");
        sb.append("Created Date: ").append(position.getCreatedDate()).append("\n");
        sb.append("Expired Date: ").append(position.getExpiredDate()).append("\n");

        return sb.toString();
    }


    private String truncateString(String str, int maxLength) {
        if (str == null) {
            return "";
        }

        if (str.length() <= maxLength) {
            return str;
        }

        return str.substring(0, maxLength - 3) + "...";
    }
}