package src.complex_numbers;

public class ComplexNumber {
    private double realPart;
    private double imaginaryPart;

    public ComplexNumber(double realPart) {
        this.realPart = realPart;
        this.imaginaryPart = 0.0;
    }

    public ComplexNumber(double realPart, double imaginePart) {
        this.realPart = realPart;
        this.imaginaryPart = imaginePart;
    }

    public ComplexNumber plus(ComplexNumber other) {
        double newRealPart  = this.realPart + other.realPart;
        double newImaginaryPart  = this.imaginaryPart + other.imaginaryPart;
        return new ComplexNumber(newRealPart , newImaginaryPart );
    }

    public ComplexNumber minus(ComplexNumber other) {
        double newRealPart  = this.realPart - other.realPart;
        double newImaginaryPart  = this.imaginaryPart - other.imaginaryPart;
        return new ComplexNumber(newRealPart , newImaginaryPart );
    }

    public ComplexNumber multiplication(ComplexNumber other) {
        double newRealPart = this.realPart * other.realPart - this.imaginaryPart * other.imaginaryPart;
        double newImaginaryPart = this.realPart * other.imaginaryPart + this.imaginaryPart * other.realPart;
        return new ComplexNumber(newRealPart, newImaginaryPart);
    }
    public double module() {
        return Math.sqrt(Math.pow(this.realPart, 2) + Math.pow(this.imaginaryPart, 2));

    }


    @Override
    public String toString() {
        return String.format("%.1f + %.1fi", realPart, imaginaryPart);
    }
}
