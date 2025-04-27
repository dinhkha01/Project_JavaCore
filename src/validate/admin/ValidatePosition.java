package validate.admin;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class ValidatePosition {

    // Kiểm tra đầu vào số nguyên
    public int validateIntegerInput(Scanner scanner) {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.print("Đầu vào không hợp lệ. Vui lòng nhập số: ");
            }
        }
    }

    // Kiểm tra tên vị trí
    public String validateName(Scanner scanner) {
        while (true) {
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                System.out.print("Tên vị trí không được để trống. Vui lòng nhập tên: ");
            } else if (name.length() > 100) {
                System.out.print("Tên vị trí quá dài (tối đa 100 ký tự). Vui lòng nhập lại: ");
            } else if (name.contains("*deleted")) {
                System.out.print("Tên không được chứa '*deleted'. Vui lòng nhập tên khác: ");
            } else {
                return name;
            }
        }
    }

    // Kiểm tra lương tối thiểu
    public BigDecimal validateMinSalary(Scanner scanner) {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    return null; // Cho phép null
                }

                BigDecimal salary = new BigDecimal(input);
                if (salary.compareTo(BigDecimal.ZERO) < 0) {
                    System.out.print("Lương không được âm. Vui lòng nhập lại: ");
                } else {
                    return salary;
                }
            } catch (NumberFormatException e) {
                System.out.print("Định dạng lương không hợp lệ. Vui lòng nhập số hợp lệ: ");
            }
        }
    }

    // Kiểm tra lương tối đa
    public BigDecimal validateMaxSalary(Scanner scanner, BigDecimal minSalary) {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    return null; // Cho phép null
                }

                BigDecimal salary = new BigDecimal(input);
                if (salary.compareTo(BigDecimal.ZERO) < 0) {
                    System.out.print("Lương không được âm. Vui lòng nhập lại: ");
                } else if (minSalary != null && salary.compareTo(minSalary) < 0) {
                    System.out.print("Lương tối đa phải lớn hơn hoặc bằng lương tối thiểu (" + minSalary + "). Vui lòng nhập lại: ");
                } else {
                    return salary;
                }
            } catch (NumberFormatException e) {
                System.out.print("Định dạng lương không hợp lệ. Vui lòng nhập số hợp lệ: ");
            }
        }
    }

    // Kiểm tra kinh nghiệm tối thiểu
    public int validateMinExperience(Scanner scanner) {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                int experience = Integer.parseInt(input);

                if (experience < 0) {
                    System.out.print("Kinh nghiệm không được âm. Vui lòng nhập lại: ");
                } else {
                    return experience;
                }
            } catch (NumberFormatException e) {
                System.out.print("Định dạng kinh nghiệm không hợp lệ. Vui lòng nhập số nguyên: ");
            }
        }
    }

    // Kiểm tra ngày hết hạn
    public Date validateExpiredDate(Scanner scanner) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

        while (true) {
            try {
                String input = scanner.nextLine().trim();
                LocalDate date = LocalDate.parse(input, formatter);

                if (date.isBefore(LocalDate.now())) {
                    System.out.print("Ngày hết hạn phải từ ngày hiện tại trở đi. Vui lòng nhập lại (yyyy-MM-dd): ");
                } else {
                    return Date.valueOf(date);
                }
            } catch (DateTimeParseException e) {
                System.out.print("Định dạng ngày không hợp lệ. Vui lòng nhập theo định dạng yyyy-MM-dd: ");
            }
        }
    }

    // Kiểm tra mô tả vị trí
    public String validateDescription(Scanner scanner) {
        while (true) {
            String description = scanner.nextLine().trim();
            // Mô tả có thể rỗng hoặc tối đa 500 ký tự
            if (description.length() > 500) {
                System.out.print("Mô tả quá dài (tối đa 500 ký tự). Vui lòng nhập lại: ");
            } else {
                return description;
            }
        }
    }

    // Kiểm tra ID
    public int validateId(Scanner scanner) {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                int id = Integer.parseInt(input);

                if (id <= 0) {
                    System.out.print("ID phải là số nguyên dương. Vui lòng nhập lại: ");
                } else {
                    return id;
                }
            } catch (NumberFormatException e) {
                System.out.print("ID không hợp lệ. Vui lòng nhập số nguyên dương: ");
            }
        }
    }

    // Kiểm tra ngày tạo
    public Date validateCreatedDate(Scanner scanner) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

        while (true) {
            try {
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    // Nếu không nhập, sử dụng ngày hiện tại
                    return Date.valueOf(LocalDate.now());
                }

                LocalDate date = LocalDate.parse(input, formatter);
                if (date.isAfter(LocalDate.now())) {
                    System.out.print("Ngày tạo không thể trong tương lai. Vui lòng nhập lại (yyyy-MM-dd): ");
                } else {
                    return Date.valueOf(date);
                }
            } catch (DateTimeParseException e) {
                System.out.print("Định dạng ngày không hợp lệ. Vui lòng nhập theo định dạng yyyy-MM-dd: ");
            }
        }
    }

    // Kiểm tra xác nhận (y/n)
    public boolean validateConfirmation(Scanner scanner, String message) {
        System.out.print(message + " (y/n): ");
        while (true) {
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("y")) {
                return true;
            } else if (input.equals("n")) {
                return false;
            } else {
                System.out.print("Lựa chọn không hợp lệ. Vui lòng nhập 'y' hoặc 'n': ");
            }
        }
    }

    // Kiểm tra lựa chọn từ danh sách
    public int validateSelection(Scanner scanner, int max) {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                int selection = Integer.parseInt(input);

                if (selection < 1 || selection > max) {
                    System.out.print("Lựa chọn phải từ 1 đến " + max + ". Vui lòng nhập lại: ");
                } else {
                    return selection;
                }
            } catch (NumberFormatException e) {
                System.out.print("Lựa chọn không hợp lệ. Vui lòng nhập số từ 1 đến " + max + ": ");
            }
        }
    }

    // Kiểm tra danh sách lựa chọn (ví dụ: 1,3,5)
    public int[] validateMultipleSelections(Scanner scanner, int max) {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    return new int[0]; // Mảng rỗng nếu không chọn gì
                }

                String[] selections = input.split(",");
                int[] result = new int[selections.length];
                boolean valid = true;

                for (int i = 0; i < selections.length; i++) {
                    int selection = Integer.parseInt(selections[i].trim());
                    if (selection < 1 || selection > max) {
                        System.out.print("Lựa chọn phải từ 1 đến " + max + ". Vui lòng nhập lại: ");
                        valid = false;
                        break;
                    }
                    result[i] = selection;
                }

                if (valid) {
                    return result;
                }
            } catch (NumberFormatException e) {
                System.out.print("Định dạng không hợp lệ. Vui lòng nhập các số cách nhau bằng dấu phẩy: ");
            }
        }
    }

    // Kiểm tra đầu vào tùy chọn (cho phép để trống)
    public String validateOptionalInput(Scanner scanner) {
        return scanner.nextLine().trim();
    }
}