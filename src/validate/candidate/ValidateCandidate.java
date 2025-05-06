package validate.candidate;

import business.service.candidate.infor.CandidateServiceImpl;
import business.service.candidate.infor.ICandidateService;

import java.util.regex.Pattern;

public class ValidateCandidate {
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final String PHONE_REGEX = "^\\d{10,11}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    private static final Pattern PHONE_PATTERN = Pattern.compile(PHONE_REGEX);

    private ICandidateService candidateService;

    public ValidateCandidate() {
        this.candidateService = new CandidateServiceImpl();
    }

    public boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public boolean isValidPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone).matches();
    }

    public boolean isValidPassword(String password) {
        return password != null && password.length() >= 8 && password.length() <= 20;
    }

    public boolean isValidIdentifier(String identifier) {
        return isValidEmail(identifier) || isValidPhone(identifier);
    }

    public boolean isExistingEmailOrPhone(String identifier) {
        if (isValidEmail(identifier)) {
            return candidateService.getCandidateByEmail(identifier) != null;
        } else if (isValidPhone(identifier)) {
            return candidateService.getCandidateByPhone(identifier) != null;
        }
        return false;
    }

    public boolean isValidGender(String gender) {
        return gender != null && (gender.equalsIgnoreCase("Nam") || gender.equalsIgnoreCase("Nữ"));
    }

    public boolean isValidExperience(int experience) {
        return experience >= 0;
    }
}