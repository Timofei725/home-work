package sequences;

public class SequencesImpl implements Sequences {
    //These four methods below have been written
    //to print the results in a formatted and readable wayд
    public void printHeader(String sequenceName) {
        System.out.print(sequenceName + ". ");
    }

    public void printFooter() {
        System.out.println();
    }

    private String formatNumber(int number, boolean isLast) {
        String separator = isLast ? "..." : ", ";
        return String.format("%d%s", number, separator);
    }

    private String formatNumber(long number, boolean isLast) {
        String separator = isLast ? "..." : ", ";
        return String.format("%d%s", number, separator);
    }

    @Override
    public void a(int n) {
        System.out.print("A. ");
        for (int i = 1; i <= n; i++) {
            System.out.print(formatNumber(2 * i, i == n));
        }
        printFooter();
    }

    @Override
    public void b(int n) {
        printHeader("B");
        if (n > 0) {
            int currentNumber = 1;
            for (int i = 0; i < n; i++) {
                System.out.print(formatNumber(currentNumber, i == n - 1));
                currentNumber += 2;
            }
        }
        printFooter();
    }

    @Override
    public void c(int n) {
        printHeader("С");
        for (int i = 1; i <= n; i++) {
            System.out.print(formatNumber(i * i, i == n));
        }
        printFooter();

    }

    @Override
    public void d(int n) {
        printHeader("D");
        for (int i = 1; i <= n; i++) {
            System.out.print(formatNumber(i * i * i, i == n));
        }
        printFooter();
    }

    @Override
    public void e(int n) {
        printHeader("E");
        int currentNumber = 1;
        for (int i = 1; i <= n; i++) {
            System.out.print(formatNumber(currentNumber, i == n));
            currentNumber *= -1;
        }
        printFooter();

    }

    @Override
    public void f(int n) {
        printHeader("F");
        int currentNumber = -1;
        for (int i = 1; i <= n; i++) {
            currentNumber *= -i;
            System.out.print(formatNumber(currentNumber, i == n));
            currentNumber /= i;
        }
        printFooter();
    }

    @Override
    public void g(int n) {
        printHeader("G");
        long currentNumber;
        for (long i = 1; i <= n; i++) {
            currentNumber = i * i;
            if (i % 2 == 0) currentNumber *= -1;
            System.out.print(formatNumber(currentNumber, i == n));
        }
        printFooter();
    }

    @Override
    public void h(int n) {
        printHeader("H");
        for (int i = 1; i <= n; i++) {
            if (i % 2 == 0) {
                System.out.print(formatNumber(0, i == n));
            } else {
                System.out.print(formatNumber((i + 1) / 2, i == n));

            }

        }
        printFooter();
    }

    @Override
    public void i(int n) {
        printHeader("I");
        int currentNumber = 1;
        for (int i = 1; i <= n; i++) {
            currentNumber *= i;
            System.out.print(formatNumber(currentNumber, i == n));
        }
        printFooter();
    }

    @Override
    public void j(int n) {
        printHeader("J");
        if (n > 0) {
            long[] allNumbers = new long[n];
            allNumbers[0] = 1;
            if (n > 1) {
                allNumbers[1] = 1;
                System.out.printf("%d, %d%s", allNumbers[0], allNumbers[1], (n > 2) ? ", " : "...");
            } else {
                System.out.printf("%d...", allNumbers[0]);
            }
            for (int i = 2; i < n; i++) {
                allNumbers[i] = allNumbers[i - 2] + allNumbers[i - 1];
                System.out.print(formatNumber(allNumbers[i], i == n - 1));
            }
        }
        printFooter();
    }

}
