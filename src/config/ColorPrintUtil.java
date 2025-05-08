package config;

public class ColorPrintUtil {

    // Standard messages
    public static void printInfo(String message) {
        System.out.println(PrintColor.BLUE + message + PrintColor.RESET);
    }

    public static void printSuccess(String message) {
        System.out.println(PrintColor.GREEN_BOLD + message + PrintColor.RESET);
    }

    public static void printWarning(String message) {
        System.out.println(PrintColor.YELLOW_BOLD + message + PrintColor.RESET);
    }

    public static void printError(String message) {
        System.out.println(PrintColor.RED_BOLD + message + PrintColor.RESET);
    }

    public static void printLogout(String message) {
        System.out.println(PrintColor.RED_UNDERLINED + message + PrintColor.RESET);
    }

    // Headers and sections
    public static void printHeader(String header) {
        System.out.println(PrintColor.CYAN_BOLD + "\n=== " + header + " ===" + PrintColor.RESET);
    }

    public static void printSubHeader(String subHeader) {
        System.out.println(PrintColor.PURPLE_BOLD + "\n--- " + subHeader + " ---" + PrintColor.RESET);
    }

    // Table headers
    public static void printTableHeader(String header) {
        System.out.println(PrintColor.WHITE_BOLD_BRIGHT + header + PrintColor.RESET);
    }

    // Status indicators
    public static void printStatus(String status) {
        switch (status.toLowerCase()) {
            case "pending":
                System.out.print(PrintColor.YELLOW_BOLD + status + PrintColor.RESET);
                break;
            case "handling":
                System.out.print(PrintColor.BLUE_BOLD + status + PrintColor.RESET);
                break;
            case "interviewed":
                System.out.print(PrintColor.PURPLE_BOLD + status + PrintColor.RESET);
                break;
            case "accepted":
                System.out.print(PrintColor.GREEN_BOLD + status + PrintColor.RESET);
                break;
            case "rejected":
                System.out.print(PrintColor.RED_BOLD + status + PrintColor.RESET);
                break;
            case "cancelled":
                System.out.print(PrintColor.RED_BOLD_BRIGHT + status + PrintColor.RESET);
                break;
            case "active":
                System.out.print(PrintColor.GREEN_BOLD + status + PrintColor.RESET);
                break;
            case "inactive":
                System.out.print(PrintColor.RED_BOLD + status + PrintColor.RESET);
                break;
            default:
                System.out.print(status);
        }
    }

    // Input prompts
    public static void printPrompt(String prompt) {
        System.out.print(PrintColor.CYAN_BRIGHT + prompt + PrintColor.RESET);
    }

    // Highlight important information
    public static void printHighlight(String text) {
        System.out.println(PrintColor.WHITE_BOLD_BRIGHT + text + PrintColor.RESET);
    }

    // Menu items
    public static void printMenuItem(int number, String item) {
        System.out.println(PrintColor.YELLOW + number + ". " + PrintColor.WHITE + item + PrintColor.RESET);
    }

    // Dividers
    public static void printDivider() {
        System.out.println(PrintColor.CYAN + "--------------------------------------------------" + PrintColor.RESET);
    }

    // Application title
    public static void printAppTitle(String title) {
        System.out.println(PrintColor.CYAN_BACKGROUND + PrintColor.BLACK_BOLD + " " + title + " " + PrintColor.RESET);
    }

    // Success operations
    public static void printOperationSuccess(String operation) {
        System.out.println(PrintColor.GREEN_BACKGROUND + PrintColor.BLACK_BOLD + " " + operation + " " + PrintColor.RESET);
    }

    // Failed operations
    public static void printOperationFailed(String operation) {
        System.out.println(PrintColor.RED_BACKGROUND + PrintColor.BLACK_BOLD + " " + operation + " " + PrintColor.RESET);
    }

    // User input field
    public static void printInputField(String fieldName) {
        System.out.print(PrintColor.CYAN_UNDERLINED + fieldName + ": " + PrintColor.RESET);
    }

    // Result label
    public static void printResultLabel(String label) {
        System.out.print(PrintColor.PURPLE_BRIGHT + label + ": " + PrintColor.RESET);
    }
}