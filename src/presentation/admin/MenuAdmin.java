package presentation.admin;

import business.service.admin.AdminManagerAuthen;
import config.ColorPrintUtil;

import java.util.Scanner;

public class MenuAdmin {
    private Scanner scanner = new Scanner(System.in);
    private AdminManagerAuthen adminManager = new AdminManagerAuthen();

    public void showMainMenu() {
        while (true) {
            ColorPrintUtil.printHeader("MENU QUẢN TRỊ");
            ColorPrintUtil.printInfo("1. Quản lý Công nghệ tuyển dụng");
            ColorPrintUtil.printInfo("2. Quản lý ứng viên");
            ColorPrintUtil.printInfo("3. Quản lý Vị trí tuyển dụng");
            ColorPrintUtil.printInfo("4. Quản lý Đơn ứng tuyển");
            ColorPrintUtil.printInfo("5. Đăng xuất");
            ColorPrintUtil.printPrompt("Nhập lựa chọn: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                ColorPrintUtil.printError("Lựa chọn không hợp lệ!");
                continue;
            }

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
                    ColorPrintUtil.printSuccess("Đăng xuất thành công!");
                    return;
                default:
                    ColorPrintUtil.printError("Lựa chọn không hợp lệ!");
            }
        }
    }
}