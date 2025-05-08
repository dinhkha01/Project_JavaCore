package presentation.candidate;

import business.service.candidate.infor.CandidateServiceImpl;
import business.service.candidate.infor.ICandidateService;
import business.service.candidate.technology.CandidateTechnologyServiceImpl;
import business.service.candidate.technology.ICandidateTechnologyService;
import business.service.technology.ITechnologyService;
import business.service.technology.TechnologyServiceImpl;
import entity.Candidate;
import entity.Technology;
import validate.InputMethod;
import validate.candidate.ValidateCandidate;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;

public class MenuInfor {
    private Scanner scanner = new Scanner(System.in);
    private ICandidateService candidateService;
    private ITechnologyService technologyService;
    private ICandidateTechnologyService candidateTechnologyService;
    private ValidateCandidate validateCandidate;
    private Candidate currentCandidate;

    public MenuInfor(Candidate currentCandidate) {
        this.candidateService = new CandidateServiceImpl();
        this.technologyService = new TechnologyServiceImpl();
        this.candidateTechnologyService = new CandidateTechnologyServiceImpl();
        this.validateCandidate = new ValidateCandidate();
        this.currentCandidate = currentCandidate;
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n=== QUẢN LÝ THÔNG TIN CÁ NHÂN ===");
            System.out.println("1. Cập nhật thông tin cá nhân");
            System.out.println("2. Đổi mật khẩu");
            System.out.println("3. Quản lý công nghệ");
            System.out.println("4. Quay về menu chính");
            System.out.print("Nhập lựa chọn: ");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    updatePersonalInfo();
                    break;
                case 2:
                    changePassword();
                    break;
                case 3:
                    manageTechnologies();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    private void updatePersonalInfo() {
        System.out.println("\n=== CẬP NHẬT THÔNG TIN CÁ NHÂN ===");

        // Lấy thông tin hiện tại của ứng viên
        Candidate candidate = candidateService.getCandidateById(currentCandidate.getId());
        if (candidate == null) {
            System.out.println("Không tìm thấy thông tin ứng viên!");
            return;
        }

        // Hiển thị thông tin hiện tại của người dùng
        displayCurrentInfo(candidate);

        // Hiển thị menu lựa chọn thông tin cần cập nhật
        while (true) {
            System.out.println("\nChọn thông tin bạn muốn cập nhật:");
            System.out.println("1. Họ tên");
            System.out.println("2. Số điện thoại");
            System.out.println("3. Số năm kinh nghiệm");
            System.out.println("4. Giới tính");
            System.out.println("5. Mô tả bản thân");
            System.out.println("6. Ngày sinh");
            System.out.println("7. Cập nhật tất cả thông tin");
            System.out.println("0. Quay lại");
            System.out.print("Nhập lựa chọn của bạn: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.err.println("Vui lòng nhập một số!");
                continue;
            }

            switch (choice) {
                case 0:
                    return;
                case 1:
                    updateName(candidate);
                    break;
                case 2:
                    updatePhone(candidate);
                    break;
                case 3:
                    updateExperience(candidate);
                    break;
                case 4:
                    updateGender(candidate);
                    break;
                case 5:
                    updateDescription(candidate);
                    break;
                case 6:
                    updateDateOfBirth(candidate);
                    break;
                case 7:
                    updateAllInfo(candidate);
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
                    break;
            }

            // Lưu thông tin cập nhật sau mỗi lần thay đổi
            if (choice >= 1 && choice <= 7) {
                boolean success = candidateService.updateCandidateInfo(candidate);
                if (success) {
                    System.out.println("Cập nhật thông tin thành công!");
                    // Cập nhật lại thông tin trong currentCandidate
                    currentCandidate = candidate;
                    // Hiển thị thông tin đã cập nhật
                    System.out.println("\n=== THÔNG TIN SAU KHI CẬP NHẬT ===");
                    displayCurrentInfo(candidate);
                } else {
                    System.out.println("Cập nhật thông tin thất bại!");
                }
            }
        }
    }

    // Phương thức hiển thị thông tin hiện tại của ứng viên
    private void displayCurrentInfo(Candidate candidate) {
        System.out.println("\n=== THÔNG TIN HIỆN TẠI ===");
        System.out.println("Họ tên: " + candidate.getName());
        System.out.println("Email: " + candidate.getEmail());
        System.out.println("Số điện thoại: " + candidate.getPhone());
        System.out.println("Số năm kinh nghiệm: " + candidate.getExperience());
        System.out.println("Giới tính: " + candidate.getGender());
        System.out.println("Mô tả bản thân: " + candidate.getDescription());
        System.out.println("Ngày sinh: " + candidate.getDob());
        System.out.println("Trạng thái: " + candidate.getStatus());
    }

    private void updateName(Candidate candidate) {
        String name = InputMethod.inputString(scanner, "Nhập họ tên (" + candidate.getName() + "): ", "Họ tên không được để trống");
        candidate.setName(name);
    }

    private void updatePhone(Candidate candidate) {
        while (true) {
            String phone = InputMethod.inputString(scanner, "Nhập số điện thoại (" + candidate.getPhone() + "): ", "Số điện thoại không được để trống");
            if (validateCandidate.isValidPhone(phone)) {
                candidate.setPhone(phone);
                break;
            }
            System.err.println("Số điện thoại không hợp lệ! Số điện thoại phải có 10-11 chữ số.");
        }
    }

    private void updateExperience(Candidate candidate) {
        System.out.println("Nhập số năm kinh nghiệm (" + candidate.getExperience() + "): ");
        while (true) {
            try {
                int experience = InputMethod.inputInt(scanner, "Số năm kinh nghiệm phải là số nguyên không âm!");
                if (validateCandidate.isValidExperience(experience)) {
                    candidate.setExperience(experience);
                    break;
                }
                System.err.println("Số năm kinh nghiệm không được âm!");
            } catch (NumberFormatException e) {
                System.err.println("Vui lòng nhập một số hợp lệ!");
            }
        }
    }

    private void updateGender(Candidate candidate) {
        while (true) {
            String gender = InputMethod.inputString(scanner, "Nhập giới tính (Nam/Nữ) (" + candidate.getGender() + "): ", "Giới tính không được để trống");
            if (validateCandidate.isValidGender(gender)) {
                candidate.setGender(gender);
                break;
            }
            System.err.println("Giới tính không hợp lệ! Chỉ chấp nhận 'Nam' hoặc 'Nữ'.");
        }
    }

    private void updateDescription(Candidate candidate) {
        String description = InputMethod.inputString(scanner, "Nhập mô tả bản thân (" + candidate.getDescription() + "): ", "Mô tả không được để trống");
        candidate.setDescription(description);
    }

    private void updateDateOfBirth(Candidate candidate) {
        while (true) {
            String dobStr = InputMethod.inputString(scanner, "Nhập ngày sinh (YYYY-MM-DD) (" + candidate.getDob() + "): ", "Ngày sinh không được để trống");
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date utilDate = sdf.parse(dobStr);
                candidate.setDob(new Date(utilDate.getTime()));
                break;
            } catch (ParseException e) {
                System.err.println("Định dạng ngày không hợp lệ! Sử dụng định dạng YYYY-MM-DD.");
            }
        }
    }

    private void updateAllInfo(Candidate candidate) {
        updateName(candidate);
        updatePhone(candidate);
        updateExperience(candidate);
        updateGender(candidate);
        updateDescription(candidate);
        updateDateOfBirth(candidate);
    }

    private void changePassword() {
        System.out.println("\n=== ĐỔI MẬT KHẨU ===");

        // Nhập định danh (email hoặc số điện thoại) để xác thực
        String identifier;
        while (true) {
            identifier = InputMethod.inputString(scanner, "Nhập email hoặc số điện thoại để xác thực: ", "Email/Số điện thoại không được để trống");

            // Kiểm tra xem định danh có phải của tài khoản đang đăng nhập không
            boolean isCurrentUser = (identifier.equals(currentCandidate.getEmail()) || identifier.equals(currentCandidate.getPhone()));

            if (!isCurrentUser) {
                System.err.println("Bạn chỉ có thể đổi mật khẩu cho tài khoản của chính mình!");
                continue;
            }

            if (validateCandidate.isValidIdentifier(identifier)) {
                // Kiểm tra xem định danh có tồn tại trong hệ thống không
                if (validateCandidate.isExistingEmailOrPhone(identifier)) {
                    break;
                }
                System.err.println("Email/Số điện thoại không tồn tại trong hệ thống!");
            } else {
                System.err.println("Email/Số điện thoại không hợp lệ!");
            }
        }

        // Nhập mật khẩu cũ
        String oldPassword;
        boolean isOldPasswordCorrect = false;
        int attempts = 0;
        final int MAX_ATTEMPTS = 3;

        while (attempts < MAX_ATTEMPTS && !isOldPasswordCorrect) {
            oldPassword = InputMethod.inputString(scanner, "Nhập mật khẩu cũ: ", "Mật khẩu không được để trống");

            // Kiểm tra mật khẩu cũ có đúng không
            if (candidateService.verifyPassword(identifier, oldPassword)) {
                isOldPasswordCorrect = true;

                // Nhập mật khẩu mới
                String newPassword;
                boolean isValidNewPassword = false;

                while (!isValidNewPassword) {
                    newPassword = InputMethod.inputString(scanner, "Nhập mật khẩu mới: ", "Mật khẩu không được để trống");

                    // Kiểm tra tính hợp lệ của mật khẩu mới
                    if (validateCandidate.isValidPassword(newPassword)) {
                        // Xác nhận mật khẩu mới
                        String confirmPassword = InputMethod.inputString(scanner, "Xác nhận mật khẩu mới: ", "Xác nhận mật khẩu không được để trống");

                        if (newPassword.equals(confirmPassword)) {
                            // Thực hiện đổi mật khẩu
                            boolean success = candidateService.changePassword(identifier, oldPassword, newPassword);

                            if (success) {
                                System.out.println("Đổi mật khẩu thành công!");
                                return;
                            } else {
                                System.err.println("Đổi mật khẩu thất bại! Vui lòng thử lại sau.");
                                return;
                            }
                        } else {
                            System.err.println("Mật khẩu xác nhận không khớp với mật khẩu mới!");
                        }
                    } else {
                        System.err.println("Mật khẩu không hợp lệ! Mật khẩu phải có ít nhất 6 ký tự, bao gồm chữ hoa, chữ thường và số.");
                    }
                }
            } else {
                attempts++;
                System.err.println("Mật khẩu cũ không đúng! Còn " + (MAX_ATTEMPTS - attempts) + " lần thử.");

                if (attempts >= MAX_ATTEMPTS) {
                    System.err.println("Bạn đã nhập sai mật khẩu quá nhiều lần. Vui lòng thử lại sau.");
                    return;
                }
            }
        }
    }

    private void manageTechnologies() {
        while (true) {
            System.out.println("\n=== QUẢN LÝ CÔNG NGHỆ ===");
            System.out.println("1. Xem danh sách công nghệ đã đăng ký");
            System.out.println("2. Thêm công nghệ mới");
            System.out.println("3. Xóa công nghệ");
            System.out.println("0. Quay lại");
            System.out.print("Nhập lựa chọn: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.err.println("Vui lòng nhập một số!");
                continue;
            }

            switch (choice) {
                case 0:
                    return;
                case 1:
                    viewRegisteredTechnologies();
                    break;
                case 2:
                    addTechnology();
                    break;
                case 3:
                    removeTechnology();
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
                    break;
            }
        }
    }

    private void viewRegisteredTechnologies() {
        System.out.println("\n=== DANH SÁCH CÔNG NGHỆ ĐÃ ĐĂNG KÝ ===");
        List<Technology> technologies = candidateTechnologyService.getCandidateTechnologies(currentCandidate.getId());

        if (technologies.isEmpty()) {
            System.out.println("Bạn chưa đăng ký công nghệ nào!");
        } else {
            System.out.println("Số lượng: " + technologies.size());
            System.out.println("ID\tTên công nghệ");
            System.out.println("--------------------");

            for (Technology tech : technologies) {
                System.out.println(tech.getId() + "\t" + tech.getName());
            }
        }
    }

    private void addTechnology() {
        System.out.println("\n=== THÊM CÔNG NGHỆ ===");

        // Lấy danh sách tất cả công nghệ trong hệ thống
        List<Technology> allTechnologies = technologyService.getAllTechnologies();

        // Lấy danh sách công nghệ đã đăng ký của ứng viên hiện tại
        List<Technology> registeredTechnologies = candidateTechnologyService.getCandidateTechnologies(currentCandidate.getId());

        if (allTechnologies.isEmpty()) {
            System.out.println("Không có công nghệ nào trong hệ thống!");
            return;
        }

        // Hiển thị danh sách tất cả công nghệ và đánh dấu những công nghệ đã đăng ký
        System.out.println("Danh sách tất cả công nghệ:");
        System.out.println("ID\tTên công nghệ\tĐã đăng ký");
        System.out.println("--------------------------------");
        System.out.println("(Những công nghệ có dấu X là công nghệ bạn đã đăng ký, không thể thêm lại)");

        for (Technology tech : allTechnologies) {
            boolean isRegistered = false;
            for (Technology regTech : registeredTechnologies) {
                if (regTech.getId() == tech.getId()) {
                    isRegistered = true;
                    break;
                }
            }
            System.out.println(tech.getId() + "\t" + tech.getName() + "\t\t" + (isRegistered ? "X" : ""));
        }

        // Chọn công nghệ để thêm
        System.out.println("\nLƯU Ý: Vui lòng nhập ID của công nghệ (cột đầu tiên) mà bạn muốn thêm");
        System.out.println("Bạn chỉ có thể thêm những công nghệ chưa có dấu X.");
        try {
            int techId = InputMethod.inputInt(scanner, "Nhập ID công nghệ muốn thêm (0 để hủy): ");

            if (techId == 0) {
                return;
            }

            // Kiểm tra xem công nghệ có tồn tại không
            Technology selectedTech = technologyService.getTechnologyById(techId);
            if (selectedTech == null) {
                System.err.println("Công nghệ không tồn tại!");
                return;
            }

            // Kiểm tra xem đã đăng ký công nghệ này chưa
            if (candidateTechnologyService.hasTechnology(currentCandidate.getId(), techId)) {
                System.err.println("Bạn đã đăng ký công nghệ này rồi!");
                return;
            }

            // Thêm công nghệ cho ứng viên
            boolean success = candidateTechnologyService.addTechnologyToCandidate(currentCandidate.getId(), techId);
            if (success) {
                System.out.println("Thêm công nghệ " + selectedTech.getName() + " thành công!");
            } else {
                System.err.println("Thêm công nghệ thất bại!");
            }
        } catch (NumberFormatException e) {
            System.err.println("ID công nghệ phải là số nguyên!");
        }
    }

    private void removeTechnology() {
        System.out.println("\n=== XÓA CÔNG NGHỆ ===");

        // Hiển thị danh sách công nghệ đã đăng ký
        List<Technology> registeredTechnologies = candidateTechnologyService.getCandidateTechnologies(currentCandidate.getId());

        if (registeredTechnologies.isEmpty()) {
            System.out.println("Bạn chưa đăng ký công nghệ nào!");
            return;
        }

        System.out.println("Danh sách công nghệ đã đăng ký:");
        System.out.println("ID\tTên công nghệ");
        System.out.println("--------------------");

        for (Technology tech : registeredTechnologies) {
            System.out.println(tech.getId() + "\t" + tech.getName());
        }

        System.out.println("Nhập ID công nghệ muốn xóa");
        try {
            int techId = InputMethod.inputInt(scanner, "Nhập ID công nghệ muốn xóa (0 để hủy): ");

            if (techId == 0) {
                return;
            }

            // Kiểm tra xem công nghệ có tồn tại và đã đăng ký chưa
            boolean hasRegistered = false;
            String techName = "";

            for (Technology tech : registeredTechnologies) {
                if (tech.getId() == techId) {
                    hasRegistered = true;
                    techName = tech.getName();
                    break;
                }
            }

            if (!hasRegistered) {
                System.err.println("Bạn chưa đăng ký công nghệ này hoặc công nghệ không tồn tại!");
                return;
            }

            // Xác nhận xóa
            System.out.println("Bạn có chắc chắn muốn xóa công nghệ " + techName + "? (Y/N)");
            String confirm = scanner.nextLine();

            if (confirm.equalsIgnoreCase("Y")) {
                boolean success = candidateTechnologyService.removeTechnologyFromCandidate(currentCandidate.getId(), techId);
                if (success) {
                    System.out.println("Xóa công nghệ " + techName + " thành công!");
                } else {
                    System.err.println("Xóa công nghệ thất bại!");
                }
            }
        } catch (NumberFormatException e) {
            System.err.println("ID công nghệ phải là số nguyên!");
        }
    }
}