package src.password_validator;

public class PasswordValidatorTest {
    public static void main(String[] args) {
        PasswordValidator.validateCredentials("Timofe_i", "йцв", "fwefewwfe");
        PasswordValidator.validateCredentials("Timofe_i", "wqeffqwfqw", "fwefewwfe");
        PasswordValidator.validateCredentials("айцй", "йцв", "fwefewwfe");
        PasswordValidator.validateCredentials("вйцвйцвйцццццццццвцйвцйцвй", "йцв", "fwefewwfe");
        System.out.println(PasswordValidator.validateCredentials("Timofe_i", "qwddqwdwq", "qwddqwdwq"));


    }
}
