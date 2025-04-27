package presentation.admin;

import business.service.admin.PositionService;
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
            System.out.println("\n=== QUẢN LÝ VỊ TRÍ TUYỂN DỤNG ===");
            System.out.println("1. Thêm vị trí tuyển dụng mới");
            System.out.println("2. Cập nhật vị trí tuyển dụng");
            System.out.println("3. Xóa vị trí");
            System.out.println("4. Xem danh sách vị trí đang hoạt động");
            System.out.println("5. Xem chi tiết vị trí");
            System.out.println("6. Quay về menu chính");
            System.out.print("Nhập lựa chọn: ");

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
                        System.out.println("Lựa chọn không hợp lệ!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập số!");
            }
        }
    }

    // Thêm vị trí mới - Cải thiện với xác thực từng trường
    private void addPosition() {
        System.out.println("\n=== THÊM VỊ TRÍ TUYỂN DỤNG MỚI ===");

        RecruitmentPosition position = new RecruitmentPosition();

        try {
            // Nhập và xác thực tên vị trí
            System.out.print("Nhập tên vị trí: ");
            position.setName(validatePosition.validateName(scanner));

            // Nhập và xác thực mô tả
            System.out.print("Nhập mô tả: ");
            position.setDescription(validatePosition.validateDescription(scanner));

            // Nhập và xác thực lương tối thiểu
            System.out.print("Nhập lương tối thiểu: ");
            position.setMinSalary(validatePosition.validateMinSalary(scanner));

            // Nhập và xác thực lương tối đa
            System.out.print("Nhập lương tối đa: ");
            position.setMaxSalary(validatePosition.validateMaxSalary(scanner, position.getMinSalary()));

            // Nhập và xác thực kinh nghiệm tối thiểu
            System.out.print("Nhập số năm kinh nghiệm tối thiểu: ");
            position.setMinExperience(validatePosition.validateMinExperience(scanner));

            // Nhập và xác thực ngày hết hạn
            System.out.print("Nhập ngày hết hạn (yyyy-MM-dd): ");
            position.setExpiredDate(validatePosition.validateExpiredDate(scanner));

            // Thiết lập ngày tạo là ngày hiện tại
            position.setCreatedDate(Date.valueOf(LocalDate.now()));

            // Lưu vị trí
            boolean success = positionService.addPosition(position);
            if (success) {
                System.out.println("Thêm vị trí thành công với ID: " + position.getId());

                // Liên kết với công nghệ
                associateTechnologies(position.getId());
            } else {
                System.out.println("Thêm vị trí thất bại!");
            }

        } catch (Exception e) {
            System.out.println("Có lỗi xảy ra: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Cập nhật vị trí - Cải thiện với menu chọn trường cập nhật
    private void updatePosition() {
        System.out.println("\n=== CẬP NHẬT VỊ TRÍ TUYỂN DỤNG ===");

        // Hiển thị danh sách vị trí
        List<RecruitmentPosition> positions = positionService.getAllActivePositions();
        if (positions.isEmpty()) {
            System.out.println("Không có vị trí tuyển dụng nào!");
            return;
        }

        displayPositionsList(positions);

        // Chọn vị trí cần cập nhật
        System.out.print("\nNhập ID vị trí cần cập nhật: ");
        try {
            int id = validatePosition.validateId(scanner);

            RecruitmentPosition position = positionService.getPositionById(id);
            if (position == null) {
                System.out.println("Không tìm thấy vị trí với ID: " + id);
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
                            System.out.println("Không có thay đổi nào để lưu!");
                        }
                        continueUpdating = false;
                        break;
                    case 9: // Hủy thay đổi
                        System.out.println("Đã hủy cập nhật vị trí.");
                        continueUpdating = false;
                        break;
                    default:
                        System.out.println("Lựa chọn không hợp lệ!");
                }
            }

        } catch (NumberFormatException e) {
            System.out.println("ID không hợp lệ!");
        } catch (Exception e) {
            System.out.println("Có lỗi xảy ra: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Hiển thị chi tiết vị trí khi cập nhật
    private void displayPositionDetails(RecruitmentPosition position) {
        System.out.println("\nTHÔNG TIN HIỆN TẠI CỦA VỊ TRÍ:");
        System.out.println("1. Tên: " + position.getName());
        System.out.println("2. Mô tả: " + position.getDescription());
        System.out.println("3. Lương tối thiểu: " + position.getMinSalary());
        System.out.println("4. Lương tối đa: " + position.getMaxSalary());
        System.out.println("5. Kinh nghiệm tối thiểu: " + position.getMinExperience() + " năm");
        System.out.println("6. Ngày hết hạn: " + position.getExpiredDate());

        // Hiển thị các công nghệ yêu cầu
        List<Technology> technologies = positionService.getPositionTechnologies(position.getId());
        System.out.println("7. Công nghệ yêu cầu:");
        if (technologies.isEmpty()) {
            System.out.println("   - Không có công nghệ nào được yêu cầu cho vị trí này.");
        } else {
            for (Technology tech : technologies) {
                System.out.println("   - " + tech.getName());
            }
        }
    }

    // Hiển thị menu cập nhật
    private void displayUpdateMenu() {
        System.out.println("\nCHỌN THÔNG TIN CẦN CẬP NHẬT:");
        System.out.println("1. Cập nhật tên");
        System.out.println("2. Cập nhật mô tả");
        System.out.println("3. Cập nhật lương tối thiểu");
        System.out.println("4. Cập nhật lương tối đa");
        System.out.println("5. Cập nhật kinh nghiệm tối thiểu");
        System.out.println("6. Cập nhật ngày hết hạn");
        System.out.println("7. Cập nhật công nghệ yêu cầu");
        System.out.println("8. Lưu thay đổi");
        System.out.println("9. Hủy thay đổi");
        System.out.print("Nhập lựa chọn: ");
    }

    // Phương thức cập nhật từng trường
    private void updateName(RecruitmentPosition position) {
        System.out.print("Nhập tên mới (hiện tại: " + position.getName() + "): ");
        String name = validatePosition.validateName(scanner);
        position.setName(name);
        System.out.println("Cập nhật tên thành công!");
    }

    private void updateDescription(RecruitmentPosition position) {
        System.out.print("Nhập mô tả mới (hiện tại: " + position.getDescription() + "): ");
        String description = validatePosition.validateDescription(scanner);
        position.setDescription(description);
        System.out.println("Cập nhật mô tả thành công!");
    }

    private void updateMinSalary(RecruitmentPosition position) {
        System.out.print("Nhập lương tối thiểu mới (hiện tại: " + position.getMinSalary() + "): ");
        BigDecimal minSalary = validatePosition.validateMinSalary(scanner);
        position.setMinSalary(minSalary);
        System.out.println("Cập nhật lương tối thiểu thành công!");
    }

    private void updateMaxSalary(RecruitmentPosition position) {
        System.out.print("Nhập lương tối đa mới (hiện tại: " + position.getMaxSalary() + "): ");
        BigDecimal maxSalary = validatePosition.validateMaxSalary(scanner, position.getMinSalary());
        position.setMaxSalary(maxSalary);
        System.out.println("Cập nhật lương tối đa thành công!");
    }

    private void updateMinExperience(RecruitmentPosition position) {
        System.out.print("Nhập kinh nghiệm tối thiểu mới (hiện tại: " + position.getMinExperience() + "): ");
        int minExp = validatePosition.validateMinExperience(scanner);
        position.setMinExperience(minExp);
        System.out.println("Cập nhật kinh nghiệm thành công!");
    }

    private void updateExpiredDate(RecruitmentPosition position) {
        System.out.print("Nhập ngày hết hạn mới (hiện tại: " + position.getExpiredDate() + ", định dạng yyyy-MM-dd): ");
        Date expiredDate = validatePosition.validateExpiredDate(scanner);
        position.setExpiredDate(expiredDate);
        System.out.println("Cập nhật ngày hết hạn thành công!");
    }

    private void updateTechnologies(int positionId) {
        System.out.println("\n=== CẬP NHẬT CÔNG NGHỆ YÊU CẦU ===");

        // Hiển thị công nghệ hiện tại
        List<Technology> currentTechnologies = positionService.getPositionTechnologies(positionId);
        System.out.println("Công nghệ hiện tại:");
        if (currentTechnologies.isEmpty()) {
            System.out.println("- Không có công nghệ nào");
        } else {
            for (Technology tech : currentTechnologies) {
                System.out.println("- " + tech.getName());
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
            System.out.println("Cập nhật vị trí thành công!");
        } else {
            System.out.println("Cập nhật vị trí thất bại!");
        }
    }

    // Xóa vị trí
    private void deletePosition() {
        System.out.println("\n=== XÓA VỊ TRÍ TUYỂN DỤNG ===");

        // Hiển thị danh sách vị trí
        List<RecruitmentPosition> positions = positionService.getAllActivePositions();
        if (positions.isEmpty()) {
            System.out.println("Không có vị trí tuyển dụng nào!");
            return;
        }

        displayPositionsList(positions);

        // Chọn vị trí cần xóa
        System.out.print("\nNhập ID vị trí cần xóa: ");
        try {
            int id = validatePosition.validateId(scanner);

            RecruitmentPosition position = positionService.getPositionById(id);
            if (position == null) {
                System.out.println("Không tìm thấy vị trí với ID: " + id);
                return;
            }

            System.out.println("Vị trí cần xóa: " + position.getName());

            if (validatePosition.validateConfirmation(scanner, "Bạn có chắc chắn muốn xóa vị trí này?")) {
                boolean success = positionService.deletePosition(id);

                if (success) {
                    System.out.println("Xóa vị trí thành công!");
                } else {
                    System.out.println("Xóa vị trí thất bại!");
                }
            } else {
                System.out.println("Đã hủy xóa vị trí.");
            }

        } catch (NumberFormatException e) {
            System.out.println("ID không hợp lệ!");
        } catch (Exception e) {
            System.out.println("Có lỗi xảy ra: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Xem danh sách vị trí đang hoạt động
    private void viewActivePositions() {
        System.out.println("\n=== DANH SÁCH VỊ TRÍ TUYỂN DỤNG ĐANG HOẠT ĐỘNG ===");

        List<RecruitmentPosition> positions = positionService.getAllActivePositions();

        if (positions.isEmpty()) {
            System.out.println("Không có vị trí tuyển dụng nào!");
            return;
        }

        displayPositionsList(positions);
    }

    // Xem chi tiết vị trí
    private void viewPositionDetail() {
        System.out.println("\n=== CHI TIẾT VỊ TRÍ TUYỂN DỤNG ===");

        // Hiển thị danh sách vị trí
        List<RecruitmentPosition> positions = positionService.getAllActivePositions();
        if (positions.isEmpty()) {
            System.out.println("Không có vị trí tuyển dụng nào!");
            return;
        }

        displayPositionsList(positions);

        // Chọn vị trí cần xem
        System.out.print("\nNhập ID vị trí cần xem chi tiết: ");
        try {
            int id = validatePosition.validateId(scanner);

            RecruitmentPosition position = positionService.getPositionById(id);
            if (position == null) {
                System.out.println("Không tìm thấy vị trí với ID: " + id);
                return;
            }

            // Hiển thị thông tin chi tiết
            System.out.println("\nCHI TIẾT VỊ TRÍ:");
            System.out.println("ID: " + position.getId());
            System.out.println("Tên: " + position.getName());
            System.out.println("Mô tả: " + position.getDescription());
            System.out.println("Lương tối thiểu: " + position.getMinSalary());
            System.out.println("Lương tối đa: " + position.getMaxSalary());
            System.out.println("Kinh nghiệm tối thiểu: " + position.getMinExperience() + " năm");
            System.out.println("Ngày tạo: " + position.getCreatedDate());
            System.out.println("Ngày hết hạn: " + position.getExpiredDate());

            // Hiển thị các công nghệ yêu cầu
            List<Technology> technologies = positionService.getPositionTechnologies(id);
            System.out.println("\nCÁC CÔNG NGHỆ YÊU CẦU:");

            if (technologies.isEmpty()) {
                System.out.println("Không có công nghệ nào được yêu cầu cho vị trí này.");
            } else {
                for (Technology tech : technologies) {
                    System.out.println("- " + tech.getName());
                }
            }

        } catch (NumberFormatException e) {
            System.out.println("ID không hợp lệ!");
        } catch (Exception e) {
            System.out.println("Có lỗi xảy ra: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Hiển thị danh sách vị trí
    private void displayPositionsList(List<RecruitmentPosition> positions) {
        System.out.printf("%-5s | %-30s | %-15s | %-15s | %-10s | %-12s%n",
                "ID", "Tên", "Lương Min", "Lương Max", "Kinh Nghiệm", "Ngày Hết Hạn");
        System.out.println("----------------------------------------------------------------------------------------");

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
            System.out.println("Không có công nghệ nào trong hệ thống.");
            return;
        }

        System.out.println("\nDanh sách công nghệ:");
        for (int i = 0; i < allTechnologies.size(); i++) {
            System.out.println((i + 1) + ". " + allTechnologies.get(i).getName());
        }

        while (true) {
            System.out.println("\nChọn các công nghệ cho vị trí này (nhập số thứ tự, cách nhau bằng dấu phẩy, ví dụ: 1,3,5): ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Không có công nghệ nào được chọn.");
                break;
            }

            boolean validInput = true;
            String[] selections = input.split(",");

            for (String selection : selections) {
                try {
                    int index = Integer.parseInt(selection.trim()) - 1;
                    if (index < 0 || index >= allTechnologies.size()) {
                        System.out.println("Lựa chọn không hợp lệ: " + selection + ". Phải từ 1 đến " + allTechnologies.size());
                        validInput = false;
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Lựa chọn không hợp lệ: " + selection + ". Phải là số.");
                    validInput = false;
                    break;
                }
            }

            if (validInput) {
                for (String selection : selections) {
                    int index = Integer.parseInt(selection.trim()) - 1;
                    Technology selected = allTechnologies.get(index);
                    positionService.addPositionTechnology(positionId, selected.getId());
                    System.out.println("Đã thêm công nghệ: " + selected.getName());
                }
                break;
            }
        }
    }
}