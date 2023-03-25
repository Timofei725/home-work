package password_validator;

import java.util.regex.Pattern;

public class PasswordValidator {

    public static Boolean validateCredentials(String login, String password, String confirmPassword) {
        String regex = "^[a-zA-Z0-9_]*$";
        try {
            if (!Pattern.matches(regex, login)) {
                throw new WrongLoginException("Логин содержит недопустимые символы");
            } else if (login.length() >= 20) {
                throw new WrongLoginException("Логин слишком длинный");
            } else if (!Pattern.matches(regex, password)) {
                throw new WrongPasswordException("Пароль содержит недопустимые символы");
            } else if (password.length() >= 20) {
                throw new WrongPasswordException("Пароль слишком длинный");
            } else if (!password.equals(confirmPassword)) {
                throw new WrongPasswordException("Пароль и подтверждение не совпадают");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }

        return true;
    }
}
