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
import config.ColorPrintUtil;
import config.PrintColor;

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
            ColorPrintUtil.printHeader("QUẢN LÝ THÔNG TIN CÁ NHÂN");
            ColorPrintUtil.printMenuItem(1, "Cập nhật thông tin cá nhân");
            ColorPrintUtil.printMenuItem(2, "Đổi mật khẩu");
            ColorPrintUtil.printMenuItem(3, "Quản lý công nghệ");
            ColorPrintUtil.printMenuItem(4, "Quay về menu chính");
            ColorPrintUtil.printInputField("Nhập lựa chọn");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                ColorPrintUtil.printError("Vui lòng nhập một số!");
                continue;
            }

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
                    ColorPrintUtil.printError("Lựa chọn không hợp lệ!");
            }
        }
    }

    private void updatePersonalInfo() {
        ColorPrintUtil.printHeader("CẬP NHẬT THÔNG TIN CÁ NHÂN");

        // Lấy thông tin hiện tại của ứng viên
        Candidate candidate = candidateService.getCandidateById(currentCandidate.getId());
        if (candidate == null) {
            ColorPrintUtil.printError("Không tìm thấy thông tin ứng viên!");
            return;
        }

        // Hiển thị thông tin hiện tại của người dùng
        displayCurrentInfo(candidate);

        // Hiển thị menu lựa chọn thông tin cần cập nhật
        while (true) {
            ColorPrintUtil.printSubHeader("Chọn thông tin bạn muốn cập nhật");
            ColorPrintUtil.printMenuItem(1, "Họ tên");
            ColorPrintUtil.printMenuItem(2, "Số điện thoại");
            ColorPrintUtil.printMenuItem(3, "Số năm kinh nghiệm");
            ColorPrintUtil.printMenuItem(4, "Giới tính");
            ColorPrintUtil.printMenuItem(5, "Mô tả bản thân");
            ColorPrintUtil.printMenuItem(6, "Ngày sinh");
            ColorPrintUtil.printMenuItem(7, "Cập nhật tất cả thông tin");
            ColorPrintUtil.printMenuItem(0, "Quay lại");
            ColorPrintUtil.printInputField("Nhập lựa chọn của bạn");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                ColorPrintUtil.printError("Vui lòng nhập một số!");
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
                    ColorPrintUtil.printError("Lựa chọn không hợp lệ!");
                    break;
            }

            // Lưu thông tin cập nhật sau mỗi lần thay đổi
            if (choice >= 1 && choice <= 7) {
                boolean success = candidateService.updateCandidateInfo(candidate);
                if (success) {
                    ColorPrintUtil.printOperationSuccess("Cập nhật thông tin thành công!");
                    // Cập nhật lại thông tin trong currentCandidate
                    currentCandidate = candidate;
                    // Hiển thị thông tin đã cập nhật
                    ColorPrintUtil.printSubHeader("THÔNG TIN SAU KHI CẬP NHẬT");
                    displayCurrentInfo(candidate);
                } else {
                    ColorPrintUtil.printOperationFailed("Cập nhật thông tin thất bại!");
                }
            }
        }
    }

    // Phương thức hiển thị thông tin hiện tại của ứng viên
    private void displayCurrentInfo(Candidate candidate) {
        ColorPrintUtil.printSubHeader("THÔNG TIN HIỆN TẠI");
        ColorPrintUtil.printResultLabel("Họ tên");
        System.out.println(PrintColor.WHITE_BOLD + candidate.getName() + PrintColor.RESET);

        ColorPrintUtil.printResultLabel("Email");
        System.out.println(PrintColor.WHITE_BOLD + candidate.getEmail() + PrintColor.RESET);

        ColorPrintUtil.printResultLabel("Số điện thoại");
        System.out.println(PrintColor.WHITE_BOLD + candidate.getPhone() + PrintColor.RESET);

        ColorPrintUtil.printResultLabel("Số năm kinh nghiệm");
        System.out.println(PrintColor.WHITE_BOLD + candidate.getExperience() + PrintColor.RESET);

        ColorPrintUtil.printResultLabel("Giới tính");
        System.out.println(PrintColor.WHITE_BOLD + candidate.getGender() + PrintColor.RESET);

        ColorPrintUtil.printResultLabel("Mô tả bản thân");
        System.out.println(PrintColor.WHITE_BOLD + candidate.getDescription() + PrintColor.RESET);

        ColorPrintUtil.printResultLabel("Ngày sinh");
        System.out.println(PrintColor.WHITE_BOLD + candidate.getDob() + PrintColor.RESET);

        ColorPrintUtil.printResultLabel("Trạng thái");
        ColorPrintUtil.printStatus(candidate.getStatus());
        System.out.println();
    }

    private void updateName(Candidate candidate) {
        String currentName = candidate.getName();
        ColorPrintUtil.printInputField("Nhập họ tên (" + currentName + ")");
        String name = scanner.nextLine();
        if (name.trim().isEmpty()) {
            ColorPrintUtil.printError("Họ tên không được để trống");
            return;
        }
        candidate.setName(name);
        ColorPrintUtil.printSuccess("Đã cập nhật họ tên thành công");
    }

    private void updatePhone(Candidate candidate) {
        while (true) {
            ColorPrintUtil.printInputField("Nhập số điện thoại (" + candidate.getPhone() + ")");
            String phone = scanner.nextLine();
            if (phone.trim().isEmpty()) {
                ColorPrintUtil.printError("Số điện thoại không được để trống");
                continue;
            }

            if (validateCandidate.isValidPhone(phone)) {
                candidate.setPhone(phone);
                ColorPrintUtil.printSuccess("Đã cập nhật số điện thoại thành công");
                break;
            }
            ColorPrintUtil.printError("Số điện thoại không hợp lệ! Số điện thoại phải có 10-11 chữ số.");
        }
    }

    private void updateExperience(Candidate candidate) {
        ColorPrintUtil.printInputField("Nhập số năm kinh nghiệm (" + candidate.getExperience() + ")");
        while (true) {
            try {
                String input = scanner.nextLine();
                if (input.trim().isEmpty()) {
                    ColorPrintUtil.printError("Số năm kinh nghiệm không được để trống");
                    continue;
                }

                int experience = Integer.parseInt(input);
                if (validateCandidate.isValidExperience(experience)) {
                    candidate.setExperience(experience);
                    ColorPrintUtil.printSuccess("Đã cập nhật số năm kinh nghiệm thành công");
                    break;
                }
                ColorPrintUtil.printError("Số năm kinh nghiệm không được âm!");
            } catch (NumberFormatException e) {
                ColorPrintUtil.printError("Vui lòng nhập một số hợp lệ!");
            }
        }
    }

    private void updateGender(Candidate candidate) {
        while (true) {
            ColorPrintUtil.printInputField("Nhập giới tính (Nam/Nữ) (" + candidate.getGender() + ")");
            String gender = scanner.nextLine();
            if (gender.trim().isEmpty()) {
                ColorPrintUtil.printError("Giới tính không được để trống");
                continue;
            }

            if (validateCandidate.isValidGender(gender)) {
                candidate.setGender(gender);
                ColorPrintUtil.printSuccess("Đã cập nhật giới tính thành công");
                break;
            }
            ColorPrintUtil.printError("Giới tính không hợp lệ! Chỉ chấp nhận 'Nam' hoặc 'Nữ'.");
        }
    }

    private void updateDescription(Candidate candidate) {
        ColorPrintUtil.printInputField("Nhập mô tả bản thân (" + candidate.getDescription() + ")");
        String description = scanner.nextLine();
        if (description.trim().isEmpty()) {
            ColorPrintUtil.printError("Mô tả không được để trống");
            return;
        }
        candidate.setDescription(description);
        ColorPrintUtil.printSuccess("Đã cập nhật mô tả bản thân thành công");
    }

    private void updateDateOfBirth(Candidate candidate) {
        while (true) {
            ColorPrintUtil.printInputField("Nhập ngày sinh (YYYY-MM-DD) (" + candidate.getDob() + ")");
            String dobStr = scanner.nextLine();
            if (dobStr.trim().isEmpty()) {
                ColorPrintUtil.printError("Ngày sinh không được để trống");
                continue;
            }

            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date utilDate = sdf.parse(dobStr);
                candidate.setDob(new Date(utilDate.getTime()));
                ColorPrintUtil.printSuccess("Đã cập nhật ngày sinh thành công");
                break;
            } catch (ParseException e) {
                ColorPrintUtil.printError("Định dạng ngày không hợp lệ! Sử dụng định dạng YYYY-MM-DD.");
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
        ColorPrintUtil.printSuccess("Đã cập nhật tất cả thông tin thành công");
    }

    private void changePassword() {
        ColorPrintUtil.printHeader("ĐỔI MẬT KHẨU");

        // Nhập định danh (email hoặc số điện thoại) để xác thực
        String identifier;
        while (true) {
            ColorPrintUtil.printInputField("Nhập email hoặc số điện thoại để xác thực");
            identifier = scanner.nextLine();
            if (identifier.trim().isEmpty()) {
                ColorPrintUtil.printError("Email/Số điện thoại không được để trống");
                continue;
            }

            // Kiểm tra xem định danh có phải của tài khoản đang đăng nhập không
            boolean isCurrentUser = (identifier.equals(currentCandidate.getEmail()) || identifier.equals(currentCandidate.getPhone()));

            if (!isCurrentUser) {
                ColorPrintUtil.printError("Bạn chỉ có thể đổi mật khẩu cho tài khoản của chính mình!");
                continue;
            }

            if (validateCandidate.isValidIdentifier(identifier)) {
                // Kiểm tra xem định danh có tồn tại trong hệ thống không
                if (validateCandidate.isExistingEmailOrPhone(identifier)) {
                    break;
                }
                ColorPrintUtil.printError("Email/Số điện thoại không tồn tại trong hệ thống!");
            } else {
                ColorPrintUtil.printError("Email/Số điện thoại không hợp lệ!");
            }
        }

        // Nhập mật khẩu cũ
        String oldPassword;
        boolean isOldPasswordCorrect = false;
        int attempts = 0;
        final int MAX_ATTEMPTS = 3;

        while (attempts < MAX_ATTEMPTS && !isOldPasswordCorrect) {
            ColorPrintUtil.printInputField("Nhập mật khẩu cũ");
            oldPassword = scanner.nextLine();
            if (oldPassword.trim().isEmpty()) {
                ColorPrintUtil.printError("Mật khẩu không được để trống");
                continue;
            }

            // Kiểm tra mật khẩu cũ có đúng không
            if (candidateService.verifyPassword(identifier, oldPassword)) {
                isOldPasswordCorrect = true;

                // Nhập mật khẩu mới
                String newPassword;
                boolean isValidNewPassword = false;

                while (!isValidNewPassword) {
                    ColorPrintUtil.printInputField("Nhập mật khẩu mới");
                    newPassword = scanner.nextLine();
                    if (newPassword.trim().isEmpty()) {
                        ColorPrintUtil.printError("Mật khẩu không được để trống");
                        continue;
                    }

                    // Kiểm tra tính hợp lệ của mật khẩu mới
                    if (validateCandidate.isValidPassword(newPassword)) {
                        // Xác nhận mật khẩu mới
                        ColorPrintUtil.printInputField("Xác nhận mật khẩu mới");
                        String confirmPassword = scanner.nextLine();
                        if (confirmPassword.trim().isEmpty()) {
                            ColorPrintUtil.printError("Xác nhận mật khẩu không được để trống");
                            continue;
                        }

                        if (newPassword.equals(confirmPassword)) {
                            // Thực hiện đổi mật khẩu
                            boolean success = candidateService.changePassword(identifier, oldPassword, newPassword);

                            if (success) {
                                ColorPrintUtil.printOperationSuccess("Đổi mật khẩu thành công!");
                                return;
                            } else {
                                ColorPrintUtil.printOperationFailed("Đổi mật khẩu thất bại! Vui lòng thử lại sau.");
                                return;
                            }
                        } else {
                            ColorPrintUtil.printError("Mật khẩu xác nhận không khớp với mật khẩu mới!");
                        }
                    } else {
                        ColorPrintUtil.printError("Mật khẩu không hợp lệ! Mật khẩu phải có ít nhất 6 ký tự, bao gồm chữ hoa, chữ thường và số.");
                    }
                }
            } else {
                attempts++;
                ColorPrintUtil.printWarning("Mật khẩu cũ không đúng! Còn " + (MAX_ATTEMPTS - attempts) + " lần thử.");

                if (attempts >= MAX_ATTEMPTS) {
                    ColorPrintUtil.printError("Bạn đã nhập sai mật khẩu quá nhiều lần. Vui lòng thử lại sau.");
                    return;
                }
            }
        }
    }

    private void manageTechnologies() {
        while (true) {
            ColorPrintUtil.printHeader("QUẢN LÝ CÔNG NGHỆ");
            ColorPrintUtil.printMenuItem(1, "Xem danh sách công nghệ đã đăng ký");
            ColorPrintUtil.printMenuItem(2, "Thêm công nghệ mới");
            ColorPrintUtil.printMenuItem(3, "Xóa công nghệ");
            ColorPrintUtil.printMenuItem(0, "Quay lại");
            ColorPrintUtil.printInputField("Nhập lựa chọn");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                ColorPrintUtil.printError("Vui lòng nhập một số!");
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
                    ColorPrintUtil.printError("Lựa chọn không hợp lệ!");
                    break;
            }
        }
    }

    private void viewRegisteredTechnologies() {
        ColorPrintUtil.printHeader("DANH SÁCH CÔNG NGHỆ ĐÃ ĐĂNG KÝ");
        List<Technology> technologies = candidateTechnologyService.getCandidateTechnologies(currentCandidate.getId());

        if (technologies.isEmpty()) {
            ColorPrintUtil.printInfo("Bạn chưa đăng ký công nghệ nào!");
        } else {
            ColorPrintUtil.printInfo("Số lượng: " + technologies.size());
            ColorPrintUtil.printTableHeader("ID\tTên công nghệ");
            ColorPrintUtil.printDivider();

            for (Technology tech : technologies) {
                System.out.println(PrintColor.CYAN_BRIGHT + tech.getId() + "\t" +
                        PrintColor.WHITE_BOLD + tech.getName() + PrintColor.RESET);
            }
        }
    }

    private void addTechnology() {
        ColorPrintUtil.printHeader("THÊM CÔNG NGHỆ");

        // Lấy danh sách tất cả công nghệ trong hệ thống
        List<Technology> allTechnologies = technologyService.getAllTechnologies();

        // Lấy danh sách công nghệ đã đăng ký của ứng viên hiện tại
        List<Technology> registeredTechnologies = candidateTechnologyService.getCandidateTechnologies(currentCandidate.getId());

        if (allTechnologies.isEmpty()) {
            ColorPrintUtil.printInfo("Không có công nghệ nào trong hệ thống!");
            return;
        }

        // Hiển thị danh sách tất cả công nghệ và đánh dấu những công nghệ đã đăng ký
        ColorPrintUtil.printInfo("Danh sách tất cả công nghệ:");
        ColorPrintUtil.printTableHeader("ID\tTên công nghệ\tĐã đăng ký");
        ColorPrintUtil.printDivider();
        ColorPrintUtil.printInfo("(Những công nghệ có dấu X là công nghệ bạn đã đăng ký, không thể thêm lại)");

        for (Technology tech : allTechnologies) {
            boolean isRegistered = false;
            for (Technology regTech : registeredTechnologies) {
                if (regTech.getId() == tech.getId()) {
                    isRegistered = true;
                    break;
                }
            }
            System.out.print(PrintColor.CYAN_BRIGHT + tech.getId() + "\t" +
                    PrintColor.WHITE_BOLD + tech.getName() + "\t\t");

            if (isRegistered) {
                System.out.println(PrintColor.RED_BOLD + "X" + PrintColor.RESET);
            } else {
                System.out.println(PrintColor.RESET);
            }
        }

        // Chọn công nghệ để thêm
        ColorPrintUtil.printHighlight("\nLƯU Ý: Vui lòng nhập ID của công nghệ (cột đầu tiên) mà bạn muốn thêm");
        ColorPrintUtil.printInfo("Bạn chỉ có thể thêm những công nghệ chưa có dấu X.");
        try {
            ColorPrintUtil.printInputField("Nhập ID công nghệ muốn thêm (0 để hủy)");
            int techId = Integer.parseInt(scanner.nextLine());

            if (techId == 0) {
                return;
            }

            // Kiểm tra xem công nghệ có tồn tại không
            Technology selectedTech = technologyService.getTechnologyById(techId);
            if (selectedTech == null) {
                ColorPrintUtil.printError("Công nghệ không tồn tại!");
                return;
            }

            // Kiểm tra xem đã đăng ký công nghệ này chưa
            if (candidateTechnologyService.hasTechnology(currentCandidate.getId(), techId)) {
                ColorPrintUtil.printError("Bạn đã đăng ký công nghệ này rồi!");
                return;
            }

            // Thêm công nghệ cho ứng viên
            boolean success = candidateTechnologyService.addTechnologyToCandidate(currentCandidate.getId(), techId);
            if (success) {
                ColorPrintUtil.printOperationSuccess("Thêm công nghệ " + selectedTech.getName() + " thành công!");
            } else {
                ColorPrintUtil.printOperationFailed("Thêm công nghệ thất bại!");
            }
        } catch (NumberFormatException e) {
            ColorPrintUtil.printError("ID công nghệ phải là số nguyên!");
        }
    }

    private void removeTechnology() {
        ColorPrintUtil.printHeader("XÓA CÔNG NGHỆ");

        // Hiển thị danh sách công nghệ đã đăng ký
        List<Technology> registeredTechnologies = candidateTechnologyService.getCandidateTechnologies(currentCandidate.getId());

        if (registeredTechnologies.isEmpty()) {
            ColorPrintUtil.printInfo("Bạn chưa đăng ký công nghệ nào!");
            return;
        }

        ColorPrintUtil.printInfo("Danh sách công nghệ đã đăng ký:");
        ColorPrintUtil.printTableHeader("ID\tTên công nghệ");
        ColorPrintUtil.printDivider();

        for (Technology tech : registeredTechnologies) {
            System.out.println(PrintColor.CYAN_BRIGHT + tech.getId() + "\t" +
                    PrintColor.WHITE_BOLD + tech.getName() + PrintColor.RESET);
        }

        try {
            ColorPrintUtil.printInputField("Nhập ID công nghệ muốn xóa (0 để hủy)");
            int techId = Integer.parseInt(scanner.nextLine());

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
                ColorPrintUtil.printError("Bạn chưa đăng ký công nghệ này hoặc công nghệ không tồn tại!");
                return;
            }

            // Xác nhận xóa
            ColorPrintUtil.printWarning("Bạn có chắc chắn muốn xóa công nghệ " + techName + "? (Y/N)");
            String confirm = scanner.nextLine();

            if (confirm.equalsIgnoreCase("Y")) {
                boolean success = candidateTechnologyService.removeTechnologyFromCandidate(currentCandidate.getId(), techId);
                if (success) {
                    ColorPrintUtil.printOperationSuccess("Xóa công nghệ " + techName + " thành công!");
                } else {
                    ColorPrintUtil.printOperationFailed("Xóa công nghệ thất bại!");
                }
            }
        } catch (NumberFormatException e) {
            ColorPrintUtil.printError("ID công nghệ phải là số nguyên!");
        }
    }
}