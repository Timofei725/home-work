package complex_numbers;

public class ComplexNumberTest {
    public static void main(String[] args) {
        ComplexNumber z1 = new ComplexNumber(9);
        ComplexNumber z2 = new ComplexNumber(1, 2);

        System.out.println("z1 = " + z1);
        System.out.println("z2 = " + z2);

        ComplexNumber z3 = z1.plus(z2);
        ComplexNumber z4 = z1.minus(z2);
        ComplexNumber z5 = z1.multiplication(z2);

        System.out.println("z1 + z2 = " + z3);
        System.out.println("z1 - z2 = " + z4);
        System.out.println("z1 * z2 = " + z5);

        System.out.println("|z1| = " + z1.module());
        System.out.println("|z2| = " + z2.module());
    }
}
