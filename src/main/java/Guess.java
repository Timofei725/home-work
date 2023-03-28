

import java.util.Random;
import java.util.Scanner;

public class Guess {
    public static void main(String[] args) {
        int number = new Random().nextInt(100); // здесь загадывается число от 1 до 99
        int maxAttempts = 10; // здесь задается количество попыток
        System.out.println("Я загадал число от 1 до 99. У тебя " + maxAttempts + " попыток угадать.");
        try (Scanner scanner = new Scanner(System.in)) {
            int numberFromConsole;
            while (maxAttempts > 0) {
                numberFromConsole = scanner.nextInt();
                maxAttempts--;
                if (numberFromConsole > number) {
                    System.out.println("Мое число меньше! У тебя осталось " + maxAttempts + " попыток");
                } else if (numberFromConsole < number) {
                    System.out.println("Мое число больше! У тебя осталось " + maxAttempts + " попыток");
                } else {
                    int userAttempts = 10 - maxAttempts;
                    System.out.println("Ты угадал с " + userAttempts + " попытки");
                    break;
                }
            }
            if (maxAttempts == 0) {
                System.out.println("Ты не угадал");
            }
        }
    }
}
