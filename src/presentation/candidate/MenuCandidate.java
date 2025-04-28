package presentation.candidate;

import business.service.candidate.CandidateAuthen;
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
            System.out.println("Bạn chưa đăng nhập! Vui lòng đăng nhập trước.");
            return;
        }

        Candidate currentCandidate = candidateAuthen.getCurrentCandidate();

        while (true) {
            System.out.println("\n========== MENU ỨNG VIÊN ===========");
            System.out.println("Xin chào, " + currentCandidate.getName() + "!");
            System.out.println("1. Quản lý Thông tin cá nhân");
            System.out.println("2. Xem và nộp đơn ứng tuyển");
            System.out.println("3. Xem đơn đã ứng tuyển");
            System.out.println("4. Đăng xuất");
            System.out.println("====================================");
            System.out.print("Nhập lựa chọn: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Lựa chọn không hợp lệ!");
                continue;
            }

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
                    candidateAuthen.logoutCandidate();
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }
}