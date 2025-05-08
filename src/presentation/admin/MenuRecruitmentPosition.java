package presentation.admin;

import business.service.admin.PositionService;
import config.ColorPrintUtil;
import entity.RecruitmentPosition;
import entity.Technology;
import validate.admin.ValidatePosition;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class MenuRecruitmentPosition {
    private Scanner scanner = new Scanner(System.in);
    private PositionService positionService = new PositionService();
    private ValidatePosition validatePosition = new ValidatePosition();

    // Hiển thị menu chính
    public void showMenu() {
        while (true) {
            ColorPrintUtil.printHeader("QUẢN LÝ VỊ TRÍ TUYỂN DỤNG");
            ColorPrintUtil.printMenuItem(1, "Thêm vị trí tuyển dụng mới");
            ColorPrintUtil.printMenuItem(2, "Cập nhật vị trí tuyển dụng");
            ColorPrintUtil.printMenuItem(3, "Xóa vị trí");
            ColorPrintUtil.printMenuItem(4, "Xem danh sách vị trí đang hoạt động");
            ColorPrintUtil.printMenuItem(5, "Xem chi tiết vị trí");
            ColorPrintUtil.printMenuItem(6, "Quay về menu chính");
            ColorPrintUtil.printPrompt("Nhập lựa chọn: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        addPosition();
                        break;
                    case 2:
                        updatePosition();
                        break;
                    case 3:
                        deletePosition();
                        break;
                    case 4:
                        viewActivePositions();
                        break;
                    case 5:
                        viewPositionDetail();
                        break;
                    case 6:
                        return;
                    default:
                        ColorPrintUtil.printError("Lựa chọn không hợp lệ!");
                }
            } catch (NumberFormatException e) {
                ColorPrintUtil.printError("Vui lòng nhập số!");
            }
        }
    }

    // Thêm vị trí mới - Cải thiện với xác thực từng trường
    private void addPosition() {
        ColorPrintUtil.printHeader("THÊM VỊ TRÍ TUYỂN DỤNG MỚI");

        RecruitmentPosition position = new RecruitmentPosition();

        try {
            // Nhập và xác thực tên vị trí
            ColorPrintUtil.printInputField("Nhập tên vị trí");
            position.setName(validatePosition.validateName(scanner));

            // Nhập và xác thực mô tả
            ColorPrintUtil.printInputField("Nhập mô tả");
            position.setDescription(validatePosition.validateDescription(scanner));

            // Nhập và xác thực lương tối thiểu
            ColorPrintUtil.printInputField("Nhập lương tối thiểu");
            position.setMinSalary(validatePosition.validateMinSalary(scanner));

            // Nhập và xác thực lương tối đa
            ColorPrintUtil.printInputField("Nhập lương tối đa");
            position.setMaxSalary(validatePosition.validateMaxSalary(scanner, position.getMinSalary()));

            // Nhập và xác thực kinh nghiệm tối thiểu
            ColorPrintUtil.printInputField("Nhập số năm kinh nghiệm tối thiểu");
            position.setMinExperience(validatePosition.validateMinExperience(scanner));

            // Nhập và xác thực ngày hết hạn
            ColorPrintUtil.printInputField("Nhập ngày hết hạn (yyyy-MM-dd)");
            position.setExpiredDate(validatePosition.validateExpiredDate(scanner));

            // Thiết lập ngày tạo là ngày hiện tại
            position.setCreatedDate(Date.valueOf(LocalDate.now()));

            // Lưu vị trí
            boolean success = positionService.addPosition(position);
            if (success) {
                ColorPrintUtil.printOperationSuccess("Thêm vị trí thành công với ID: " + position.getId());

                // Liên kết với công nghệ
                associateTechnologies(position.getId());
            } else {
                ColorPrintUtil.printOperationFailed("Thêm vị trí thất bại!");
            }

        } catch (Exception e) {
            ColorPrintUtil.printError("Có lỗi xảy ra: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Cập nhật vị trí - Cải thiện với menu chọn trường cập nhật
    private void updatePosition() {
        ColorPrintUtil.printHeader("CẬP NHẬT VỊ TRÍ TUYỂN DỤNG");

        // Hiển thị danh sách vị trí
        List<RecruitmentPosition> positions = positionService.getAllActivePositions();
        if (positions.isEmpty()) {
            ColorPrintUtil.printInfo("Không có vị trí tuyển dụng nào!");
            return;
        }

        displayPositionsList(positions);

        // Chọn vị trí cần cập nhật
        ColorPrintUtil.printPrompt("\nNhập ID vị trí cần cập nhật: ");
        try {
            int id = validatePosition.validateId(scanner);

            RecruitmentPosition position = positionService.getPositionById(id);
            if (position == null) {
                ColorPrintUtil.printError("Không tìm thấy vị trí với ID: " + id);
                return;
            }

            boolean continueUpdating = true;
            boolean positionModified = false;

            while (continueUpdating) {
                displayPositionDetails(position);
                displayUpdateMenu();

                int choice = validatePosition.validateIntegerInput(scanner);

                switch (choice) {
                    case 1: // Cập nhật tên
                        updateName(position);
                        positionModified = true;
                        break;
                    case 2: // Cập nhật mô tả
                        updateDescription(position);
                        positionModified = true;
                        break;
                    case 3: // Cập nhật lương tối thiểu
                        updateMinSalary(position);
                        positionModified = true;
                        break;
                    case 4: // Cập nhật lương tối đa
                        updateMaxSalary(position);
                        positionModified = true;
                        break;
                    case 5: // Cập nhật kinh nghiệm tối thiểu
                        updateMinExperience(position);
                        positionModified = true;
                        break;
                    case 6: // Cập nhật ngày hết hạn
                        updateExpiredDate(position);
                        positionModified = true;
                        break;
                    case 7: // Cập nhật công nghệ
                        updateTechnologies(position.getId());
                        positionModified = true;
                        break;
                    case 8: // Lưu thay đổi
                        if (positionModified) {
                            savePositionChanges(position);
                        } else {
                            ColorPrintUtil.printWarning("Không có thay đổi nào để lưu!");
                        }
                        continueUpdating = false;
                        break;
                    case 9: // Hủy thay đổi
                        ColorPrintUtil.printInfo("Đã hủy cập nhật vị trí.");
                        continueUpdating = false;
                        break;
                    default:
                        ColorPrintUtil.printError("Lựa chọn không hợp lệ!");
                }
            }

        } catch (NumberFormatException e) {
            ColorPrintUtil.printError("ID không hợp lệ!");
        } catch (Exception e) {
            ColorPrintUtil.printError("Có lỗi xảy ra: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Hiển thị chi tiết vị trí khi cập nhật
    private void displayPositionDetails(RecruitmentPosition position) {
        ColorPrintUtil.printSubHeader("THÔNG TIN HIỆN TẠI CỦA VỊ TRÍ");

        ColorPrintUtil.printResultLabel("1. Tên");
        System.out.println(position.getName());

        ColorPrintUtil.printResultLabel("2. Mô tả");
        System.out.println(position.getDescription());

        ColorPrintUtil.printResultLabel("3. Lương tối thiểu");
        System.out.println(position.getMinSalary());

        ColorPrintUtil.printResultLabel("4. Lương tối đa");
        System.out.println(position.getMaxSalary());

        ColorPrintUtil.printResultLabel("5. Kinh nghiệm tối thiểu");
        System.out.println(position.getMinExperience() + " năm");

        ColorPrintUtil.printResultLabel("6. Ngày hết hạn");
        System.out.println(position.getExpiredDate());

        // Hiển thị các công nghệ yêu cầu
        List<Technology> technologies = positionService.getPositionTechnologies(position.getId());
        ColorPrintUtil.printResultLabel("7. Công nghệ yêu cầu");
        System.out.println();

        if (technologies.isEmpty()) {
            ColorPrintUtil.printInfo("   - Không có công nghệ nào được yêu cầu cho vị trí này.");
        } else {
            for (Technology tech : technologies) {
                ColorPrintUtil.printInfo("   - " + tech.getName());
            }
        }
    }

    // Hiển thị menu cập nhật
    private void displayUpdateMenu() {
        ColorPrintUtil.printSubHeader("CHỌN THÔNG TIN CẦN CẬP NHẬT");
        ColorPrintUtil.printMenuItem(1, "Cập nhật tên");
        ColorPrintUtil.printMenuItem(2, "Cập nhật mô tả");
        ColorPrintUtil.printMenuItem(3, "Cập nhật lương tối thiểu");
        ColorPrintUtil.printMenuItem(4, "Cập nhật lương tối đa");
        ColorPrintUtil.printMenuItem(5, "Cập nhật kinh nghiệm tối thiểu");
        ColorPrintUtil.printMenuItem(6, "Cập nhật ngày hết hạn");
        ColorPrintUtil.printMenuItem(7, "Cập nhật công nghệ yêu cầu");
        ColorPrintUtil.printMenuItem(8, "Lưu thay đổi");
        ColorPrintUtil.printMenuItem(9, "Hủy thay đổi");
        ColorPrintUtil.printPrompt("Nhập lựa chọn: ");
    }

    // Phương thức cập nhật từng trường
    private void updateName(RecruitmentPosition position) {
        ColorPrintUtil.printInputField("Nhập tên mới (hiện tại: " + position.getName() + ")");
        String name = validatePosition.validateName(scanner);
        position.setName(name);
        ColorPrintUtil.printSuccess("Cập nhật tên thành công!");
    }

    private void updateDescription(RecruitmentPosition position) {
        ColorPrintUtil.printInputField("Nhập mô tả mới (hiện tại: " + position.getDescription() + ")");
        String description = validatePosition.validateDescription(scanner);
        position.setDescription(description);
        ColorPrintUtil.printSuccess("Cập nhật mô tả thành công!");
    }

    private void updateMinSalary(RecruitmentPosition position) {
        ColorPrintUtil.printInputField("Nhập lương tối thiểu mới (hiện tại: " + position.getMinSalary() + ")");
        BigDecimal minSalary = validatePosition.validateMinSalary(scanner);
        position.setMinSalary(minSalary);
        ColorPrintUtil.printSuccess("Cập nhật lương tối thiểu thành công!");
    }

    private void updateMaxSalary(RecruitmentPosition position) {
        ColorPrintUtil.printInputField("Nhập lương tối đa mới (hiện tại: " + position.getMaxSalary() + ")");
        BigDecimal maxSalary = validatePosition.validateMaxSalary(scanner, position.getMinSalary());
        position.setMaxSalary(maxSalary);
        ColorPrintUtil.printSuccess("Cập nhật lương tối đa thành công!");
    }

    private void updateMinExperience(RecruitmentPosition position) {
        ColorPrintUtil.printInputField("Nhập kinh nghiệm tối thiểu mới (hiện tại: " + position.getMinExperience() + ")");
        int minExp = validatePosition.validateMinExperience(scanner);
        position.setMinExperience(minExp);
        ColorPrintUtil.printSuccess("Cập nhật kinh nghiệm thành công!");
    }

    private void updateExpiredDate(RecruitmentPosition position) {
        ColorPrintUtil.printInputField("Nhập ngày hết hạn mới (hiện tại: " + position.getExpiredDate() + ", định dạng yyyy-MM-dd)");
        Date expiredDate = validatePosition.validateExpiredDate(scanner);
        position.setExpiredDate(expiredDate);
        ColorPrintUtil.printSuccess("Cập nhật ngày hết hạn thành công!");
    }

    private void updateTechnologies(int positionId) {
        ColorPrintUtil.printSubHeader("CẬP NHẬT CÔNG NGHỆ YÊU CẦU");

        // Hiển thị công nghệ hiện tại
        List<Technology> currentTechnologies = positionService.getPositionTechnologies(positionId);
        ColorPrintUtil.printInfo("Công nghệ hiện tại:");
        if (currentTechnologies.isEmpty()) {
            ColorPrintUtil.printInfo("- Không có công nghệ nào");
        } else {
            for (Technology tech : currentTechnologies) {
                ColorPrintUtil.printInfo("- " + tech.getName());
            }
        }

        // Xác nhận xóa công nghệ hiện tại
        if (validatePosition.validateConfirmation(scanner, "Bạn muốn xóa tất cả công nghệ hiện tại và chọn lại?")) {
            // Xóa các liên kết công nghệ hiện có
            positionService.clearPositionTechnologies(positionId);
            // Thêm liên kết mới
            associateTechnologies(positionId);
        }
    }

    // Lưu thay đổi vị trí
    private void savePositionChanges(RecruitmentPosition position) {
        boolean success = positionService.updatePosition(position);
        if (success) {
            ColorPrintUtil.printOperationSuccess("Cập nhật vị trí thành công!");
        } else {
            ColorPrintUtil.printOperationFailed("Cập nhật vị trí thất bại!");
        }
    }

    // Xóa vị trí
    private void deletePosition() {
        ColorPrintUtil.printHeader("XÓA VỊ TRÍ TUYỂN DỤNG");

        // Hiển thị danh sách vị trí
        List<RecruitmentPosition> positions = positionService.getAllActivePositions();
        if (positions.isEmpty()) {
            ColorPrintUtil.printInfo("Không có vị trí tuyển dụng nào!");
            return;
        }

        displayPositionsList(positions);

        // Chọn vị trí cần xóa
        ColorPrintUtil.printPrompt("\nNhập ID vị trí cần xóa: ");
        try {
            int id = validatePosition.validateId(scanner);

            RecruitmentPosition position = positionService.getPositionById(id);
            if (position == null) {
                ColorPrintUtil.printError("Không tìm thấy vị trí với ID: " + id);
                return;
            }

            ColorPrintUtil.printWarning("Vị trí cần xóa: " + position.getName());

            if (validatePosition.validateConfirmation(scanner, "Bạn có chắc chắn muốn xóa vị trí này?")) {
                boolean success = positionService.deletePosition(id);

                if (success) {
                    ColorPrintUtil.printOperationSuccess("Xóa vị trí thành công!");
                } else {
                    ColorPrintUtil.printOperationFailed("Xóa vị trí thất bại!");
                }
            } else {
                ColorPrintUtil.printInfo("Đã hủy xóa vị trí.");
            }

        } catch (NumberFormatException e) {
            ColorPrintUtil.printError("ID không hợp lệ!");
        } catch (Exception e) {
            ColorPrintUtil.printError("Có lỗi xảy ra: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Xem danh sách vị trí đang hoạt động
    private void viewActivePositions() {
        ColorPrintUtil.printHeader("DANH SÁCH VỊ TRÍ TUYỂN DỤNG ĐANG HOẠT ĐỘNG");

        List<RecruitmentPosition> positions = positionService.getAllActivePositions();

        if (positions.isEmpty()) {
            ColorPrintUtil.printInfo("Không có vị trí tuyển dụng nào!");
            return;
        }

        displayPositionsList(positions);
    }

    // Xem chi tiết vị trí
    private void viewPositionDetail() {
        ColorPrintUtil.printHeader("CHI TIẾT VỊ TRÍ TUYỂN DỤNG");

        // Hiển thị danh sách vị trí
        List<RecruitmentPosition> positions = positionService.getAllActivePositions();
        if (positions.isEmpty()) {
            ColorPrintUtil.printInfo("Không có vị trí tuyển dụng nào!");
            return;
        }

        displayPositionsList(positions);

        // Chọn vị trí cần xem
        ColorPrintUtil.printPrompt("\nNhập ID vị trí cần xem chi tiết: ");
        try {
            int id = validatePosition.validateId(scanner);

            RecruitmentPosition position = positionService.getPositionById(id);
            if (position == null) {
                ColorPrintUtil.printError("Không tìm thấy vị trí với ID: " + id);
                return;
            }

            // Hiển thị thông tin chi tiết
            ColorPrintUtil.printSubHeader("CHI TIẾT VỊ TRÍ");

            ColorPrintUtil.printResultLabel("ID");
            System.out.println(position.getId());

            ColorPrintUtil.printResultLabel("Tên");
            System.out.println(position.getName());

            ColorPrintUtil.printResultLabel("Mô tả");
            System.out.println(position.getDescription());

            ColorPrintUtil.printResultLabel("Lương tối thiểu");
            System.out.println(position.getMinSalary());

            ColorPrintUtil.printResultLabel("Lương tối đa");
            System.out.println(position.getMaxSalary());

            ColorPrintUtil.printResultLabel("Kinh nghiệm tối thiểu");
            System.out.println(position.getMinExperience() + " năm");

            ColorPrintUtil.printResultLabel("Ngày tạo");
            System.out.println(position.getCreatedDate());

            ColorPrintUtil.printResultLabel("Ngày hết hạn");
            System.out.println(position.getExpiredDate());

            // Hiển thị các công nghệ yêu cầu
            ColorPrintUtil.printSubHeader("CÁC CÔNG NGHỆ YÊU CẦU");
            List<Technology> technologies = positionService.getPositionTechnologies(id);

            if (technologies.isEmpty()) {
                ColorPrintUtil.printInfo("Không có công nghệ nào được yêu cầu cho vị trí này.");
            } else {
                for (Technology tech : technologies) {
                    ColorPrintUtil.printInfo("- " + tech.getName());
                }
            }

        } catch (NumberFormatException e) {
            ColorPrintUtil.printError("ID không hợp lệ!");
        } catch (Exception e) {
            ColorPrintUtil.printError("Có lỗi xảy ra: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Hiển thị danh sách vị trí
    private void displayPositionsList(List<RecruitmentPosition> positions) {
        ColorPrintUtil.printTableHeader(String.format("%-5s | %-30s | %-15s | %-15s | %-10s | %-12s",
                "ID", "Tên", "Lương Min", "Lương Max", "Kinh Nghiệm", "Ngày Hết Hạn"));
        ColorPrintUtil.printDivider();

        for (RecruitmentPosition position : positions) {
            System.out.printf("%-5d | %-30s | %-15s | %-15s | %-10d | %-12s%n",
                    position.getId(),
                    position.getName(),
                    position.getMinSalary(),
                    position.getMaxSalary(),
                    position.getMinExperience(),
                    position.getExpiredDate());
        }
    }

    // Liên kết vị trí với các công nghệ
    private void associateTechnologies(int positionId) {
        List<Technology> allTechnologies = positionService.getAllTechnologies();

        if (allTechnologies.isEmpty()) {
            ColorPrintUtil.printInfo("Không có công nghệ nào trong hệ thống.");
            return;
        }

        ColorPrintUtil.printSubHeader("Danh sách công nghệ");
        for (int i = 0; i < allTechnologies.size(); i++) {
            ColorPrintUtil.printMenuItem(i + 1, allTechnologies.get(i).getName());
        }

        while (true) {
            ColorPrintUtil.printPrompt("\nChọn các công nghệ cho vị trí này (nhập số thứ tự, cách nhau bằng dấu phẩy, ví dụ: 1,3,5): ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                ColorPrintUtil.printWarning("Không có công nghệ nào được chọn.");
                break;
            }

            boolean validInput = true;
            String[] selections = input.split(",");

            for (String selection : selections) {
                try {
                    int index = Integer.parseInt(selection.trim()) - 1;
                    if (index < 0 || index >= allTechnologies.size()) {
                        ColorPrintUtil.printError("Lựa chọn không hợp lệ: " + selection + ". Phải từ 1 đến " + allTechnologies.size());
                        validInput = false;
                        break;
                    }
                } catch (NumberFormatException e) {
                    ColorPrintUtil.printError("Lựa chọn không hợp lệ: " + selection + ". Phải là số.");
                    validInput = false;
                    break;
                }
            }

            if (validInput) {
                for (String selection : selections) {
                    int index = Integer.parseInt(selection.trim()) - 1;
                    Technology selected = allTechnologies.get(index);
                    positionService.addPositionTechnology(positionId, selected.getId());
                    ColorPrintUtil.printSuccess("Đã thêm công nghệ: " + selected.getName());
                }
                break;
            }
        }
    }
}