
public class MultTable {
    public static void main(String[] args) {
        String format = "%d x %d = %d";
        for (int i = 1; i < 10; i++) {
            for (int j = 1; j < 10; j++) {
                System.out.printf((format) + "%n", i, j, i * j);
            }
        }
    }
}
