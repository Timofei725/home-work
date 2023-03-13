package snils_validator;

public class SnilsValidatorImpl implements SnilsValidator {

    @Override
    public boolean validate(String snils) {
        String snilsWithoutSeparators = snils.replaceAll("\\D", "");
        if (snilsWithoutSeparators.length() == 11)  {
            char[] numbers = snilsWithoutSeparators.toCharArray();
            int nineNumbersSum = 0;
            for (int i = 0; i < 9; i++) {
                nineNumbersSum += Character.digit(numbers[i], 10) * (9 - i);
            }
            int controlNumber;
            if (nineNumbersSum < 100) {
                controlNumber = nineNumbersSum;
            } else if (nineNumbersSum == 100) {
                controlNumber = 0;
            } else {
                controlNumber = nineNumbersSum % 101 == 100 ? 0 : nineNumbersSum % 101;
            }
            return controlNumber == Integer.parseInt(snilsWithoutSeparators.substring(9, 11));
        }
        return false;
    }

}