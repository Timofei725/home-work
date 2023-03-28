package io.ylab.intensive.lesson04.movie;


import javax.sql.DataSource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;


public class MovieLoaderImpl implements MovieLoader {
    private DataSource dataSource;

    public MovieLoaderImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public void loadData(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file)); Connection connection = dataSource.getConnection()) {
            String line;
            br.readLine();
            br.readLine();
            while ((line = br.readLine()) != null) {
                Movie thisMovie = readMovie(line);
                saveMovie(thisMovie, connection);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }


    }


    private Movie readMovie(String line) {
        String[] separatedLine = line.split(";");
        Movie thisMovie = new Movie();
        thisMovie.setYear(separatedLine[0].isEmpty() ? null : Integer.valueOf(separatedLine[0]));
        thisMovie.setLength(separatedLine[1].isEmpty() ? null : Integer.valueOf(separatedLine[1]));
        thisMovie.setTitle(separatedLine[2].isEmpty() ? null : separatedLine[2]);
        thisMovie.setSubject(separatedLine[3].isEmpty() ? null : separatedLine[3]);
        thisMovie.setActors(separatedLine[4].isEmpty() ? null : separatedLine[4]);
        thisMovie.setActress(separatedLine[5].isEmpty() ? null : separatedLine[5]);
        thisMovie.setDirector(separatedLine[6].isEmpty() ? null : separatedLine[6]);
        thisMovie.setPopularity(separatedLine[7].isEmpty() ? null : Integer.valueOf(separatedLine[7]));
        thisMovie.setAwards(separatedLine[8].isEmpty() ? null : Boolean.valueOf(separatedLine[8]));
        return thisMovie;
    }


    private void saveMovie(Movie thisMovie, Connection connection) {
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement("INSERT INTO movie (year, length, title, subject, actors, actress,"
                             + " director, popularity, awards) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

            setParameterOrNull(preparedStatement, 1, thisMovie.getYear(), Types.INTEGER);
            setParameterOrNull(preparedStatement, 2, thisMovie.getLength(), Types.INTEGER);
            setParameterOrNull(preparedStatement, 3, thisMovie.getTitle(), Types.VARCHAR);
            setParameterOrNull(preparedStatement, 4, thisMovie.getSubject(), Types.VARCHAR);
            setParameterOrNull(preparedStatement, 5, thisMovie.getActors(), Types.VARCHAR);
            setParameterOrNull(preparedStatement, 6, thisMovie.getActress(), Types.VARCHAR);
            setParameterOrNull(preparedStatement, 7, thisMovie.getDirector(), Types.VARCHAR);
            setParameterOrNull(preparedStatement, 8, thisMovie.getPopularity(), Types.INTEGER);
            setParameterOrNull(preparedStatement, 9, thisMovie.getAwards(), Types.BOOLEAN);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    private void setParameterOrNull(PreparedStatement preparedStatement, int parameterIndex, Object value, int sqlType)
            throws SQLException {
        if (value == null) {
            preparedStatement.setNull(parameterIndex, sqlType);
        } else {
            preparedStatement.setObject(parameterIndex, value, sqlType);
        }
    }


}