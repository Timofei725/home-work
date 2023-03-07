import java.util.Scanner;

public class Pell {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            int n = scanner.nextInt();
            if (n < 0 || n > 30) {
                System.out.println("n должно соответствовать диапазону 0 <= n <= 30");
            } else {
                long[] array = new long[n + 1];
                array[0] = 0;
                if (n > 0) {
                    array[1] = 1;
                }

                for (int i = 2; i <= n; i++) {
                    array[i] = 2 * array[i - 1] + array[i - 2];
                }
                System.out.println(array[n]);
            }
        }
    }
}
