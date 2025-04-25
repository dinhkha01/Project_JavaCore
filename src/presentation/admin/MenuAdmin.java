package presentation.admin;

import business.service.admin.AdminManagerAuthen;

import java.util.Scanner;

public class MenuAdmin {
    private Scanner scanner = new Scanner(System.in);
    private AdminManagerAuthen adminManager = new AdminManagerAuthen();

    public void showMainMenu() {
        while (true) {
            System.out.println("\n======== MENU QUẢN TRỊ =================");
            System.out.println("1. Quản lý Công nghệ tuyển dụng");
            System.out.println("2. Quản lý ứng viên");
            System.out.println("3. Quản lý Vị trí tuyển dụng");
            System.out.println("4. Quản lý Đơn ứng tuyển");
            System.out.println("5. Đăng xuất");
            System.out.println("=======================================");
            System.out.print("Nhập lựa chọn: ");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    MenuTechnology menuTech = new MenuTechnology();
                    menuTech.showMenu();
                    break;
                case 2:
                    MenuCandidate menuCandidate = new MenuCandidate();
                    menuCandidate.showMenu();
                    break;
                case 3:
                    MenuRecruitmentPosition menuPosition = new MenuRecruitmentPosition();
                    menuPosition.showMenu();
                    break;
                case 4:
                    MenuApplicationForm menuAppForm = new MenuApplicationForm();
                    menuAppForm.showMenu();
                    break;
                case 5:
                    adminManager.logoutAdmin();
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }
}