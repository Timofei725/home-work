import java.util.Scanner;

public class Stars {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            int n = scanner.nextInt();
            int m = scanner.nextInt();
            String template = scanner.next();
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    System.out.print(template);
                    if (j != m - 1) {
                        System.out.print(" ");
                    } else {
                        System.out.print("");
                    }
                }
                System.out.println();
            }
        }
    }
}
