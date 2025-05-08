package presentation.candidate;

import business.service.candidate.CandidateAuthen;
import config.ColorPrintUtil;
import entity.Candidate;
import java.util.Scanner;

public class MenuCandidate {
    private Scanner scanner;
    private CandidateAuthen candidateAuthen;

    public MenuCandidate() {
        // Default constructor when called without authentication
        this.candidateAuthen = new CandidateAuthen();
        this.scanner = new Scanner(System.in);
    }

    public MenuCandidate(CandidateAuthen candidateAuthen) {
        // Constructor with existing authentication service
        this.candidateAuthen = candidateAuthen;
        this.scanner = new Scanner(System.in);
    }

    public void showMainMenu() {
        if (!candidateAuthen.hasActiveSession()) {
            ColorPrintUtil.printError("Bạn chưa đăng nhập! Vui lòng đăng nhập trước.");
            return;
        }

        Candidate currentCandidate = candidateAuthen.getCurrentCandidate();

        while (true) {
            ColorPrintUtil.printHeader("MENU ỨNG VIÊN");
            ColorPrintUtil.printHighlight("Xin chào, " + currentCandidate.getName() + "!");
            ColorPrintUtil.printInfo("1. Quản lý Thông tin cá nhân");
            ColorPrintUtil.printInfo("2. Xem và nộp đơn ứng tuyển");
            ColorPrintUtil.printInfo("3. Xem đơn đã ứng tuyển");
            ColorPrintUtil.printInfo("4. Đăng xuất");
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
                    MenuInfor menuInfo = new MenuInfor(currentCandidate);
                    menuInfo.showMenu();
                    break;
                case 2:
                    MenuApply menuApply = new MenuApply(currentCandidate);
                    menuApply.showMenu();
                    break;
                case 3:
                    MenuApplied menuApplied = new MenuApplied(currentCandidate.getId());
                    menuApplied.showMenu();
                    break;
                case 4:
                    candidateAuthen.logoutCandidate();
                    return;
                default:
                    ColorPrintUtil.printError("Lựa chọn không hợp lệ!");
            }
        }
    }
}