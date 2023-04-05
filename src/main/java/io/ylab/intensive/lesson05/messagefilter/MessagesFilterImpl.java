package io.ylab.intensive.lesson05.messagefilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MessagesFilterImpl implements MessagesFilter {
    private DataSource dataSource;

    @Autowired
    public MessagesFilterImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public String filter(String string) {
        String[] spitedWords = string.split("\\s*[.,;?!\\n]\\s*|\\s+");
        for (int i = 0; i < spitedWords.length; i++) {
            if (!spitedWords[i].equals(censorWord(spitedWords[i]))) {
                string = string.replaceAll("\\b" + spitedWords[i] + "\\b", censorWord(spitedWords[i]));
            }
        }
        return string;
    }

    private String censorWord(String thisWord) {
        char[] thisWordAllChars = thisWord.toCharArray();
        String onlyLetters = thisWord.replaceAll("[^\\p{L}]", "");
        if (checkInDB(thisWord)) {
            thisWord = getNewWord(thisWordAllChars);
        } else if (onlyLetters.length() < thisWord.length() && checkInDB(onlyLetters)) {
            //the case when a bad word contains not only letters
            //like this - "b__a_d_w_or_d"
            int firstLetterIndex = thisWord.indexOf(onlyLetters.charAt(0));
            int lastLetterIndex = thisWord.lastIndexOf(onlyLetters.charAt(onlyLetters.length() - 1));
            thisWord = getNewWord(thisWordAllChars, firstLetterIndex, lastLetterIndex);
        }
        return thisWord;
    }

    private String getNewWord(char[] thisWordAllChars) {
        for (int i = 1; i < thisWordAllChars.length - 1; i++) {
            thisWordAllChars[i] = '*';

        }
        return new String(thisWordAllChars);
    }

    private String getNewWord(char[] thisWordAllChars, int firstLetter, int lastLetter) {
        for (int i = firstLetter + 1; i < lastLetter; i++) {
            if (Character.isLetter(thisWordAllChars[i])) {
                thisWordAllChars[i] = '*';
            }
        }
        return new String(thisWordAllChars);
    }

    private boolean checkInDB(String word) {
        String query = "SELECT * FROM bad_words WHERE LOWER(word) LIKE LOWER(?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "%" + word + "%");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String wordFromDB = resultSet.getString("word");
                if (word.equalsIgnoreCase(wordFromDB.trim())) {
                    resultSet.close();
                    return true;
                }
            }
            resultSet.close();
            return false;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

}

