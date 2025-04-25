package presentation.candidate;

import java.util.Scanner;

public class MenuCandidate {
    private Scanner scanner = new Scanner(System.in);

    public void showMainMenu() {
        while (true) {
            System.out.println("\n========== MENU ỨNG VIÊN ===========");
            System.out.println("1. Quản lý Thông tin cá nhân");
            System.out.println("2. Xem và nộp đơn ứng tuyển");
            System.out.println("3. Xem đơn đã ứng tuyển");
            System.out.println("4. Đăng xuất");
            System.out.println("====================================");
            System.out.print("Nhập lựa chọn: ");

            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    MenuInfor menuInfo = new MenuInfor();
                    menuInfo.showMenu();
                    break;
                case 2:
                    MenuApply menuApply = new MenuApply();
                    menuApply.showMenu();
                    break;
                case 3:
                    MenuApplied menuApplied = new MenuApplied();
                    menuApplied.showMenu();
                    break;
                case 4:
                    return; // Đăng xuất
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }
}