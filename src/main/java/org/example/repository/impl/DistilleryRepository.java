package org.example.repository.impl;

import org.example.dataBaseConfig.ConnectionPool;
import org.example.entity.Distillery;
import org.example.repository.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DistilleryRepository implements Repository<Distillery> {
    private static final String GET_ALL_DISTILLERY_SQL = "SELECT * FROM  Distillery";
    private static final String GET_DISTILLERY_BY_ID_SQL = "SELECT * FROM  Distillery WHERE id = ?";
    private static final String SAVE_DISTILLERY_SQL = "INSERT INTO  Distillery(name) VALUES (?)";
    private static final String SAVE_VINE_DISTILLERY_SQL = "INSERT INTO DistilleryVine(vineID, DistilleryID) VALUES (?, ?)";
    private static final String UPDATE_DISTILLERY_SQL = "UPDATE  Distillery SET name=?, vineID=? WHERE id=?";
    private static final String REMOVE_DISTILLERY_SQL = "DELETE FROM Distillery WHERE id=?";

    private final ConnectionPool connectionPool;

    public DistilleryRepository() {
        connectionPool = ConnectionPool.getInstance();
    }

    public DistilleryRepository(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public List<Distillery> findAll() {
        List<Distillery> distilleryList = new ArrayList<>();
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_DISTILLERY_SQL)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Distillery distillery = new Distillery();
                distillery.setId(resultSet.getLong(1));
                distillery.setName(resultSet.getString(2));
                distilleryList.add(distillery);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return distilleryList;
    }

    @Override
    public Distillery findOne(Long id) {
        Distillery distillery = new Distillery();
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_DISTILLERY_BY_ID_SQL)) {

            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                distillery.setId(resultSet.getLong(1));
                distillery.setName(resultSet.getString(2));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return distillery;
    }

    public Distillery save(Distillery distillery, List<Long> vineIDs) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatementMovie = connection.prepareStatement(SAVE_DISTILLERY_SQL, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement preparedStatementActorMovie = connection.prepareStatement(SAVE_VINE_DISTILLERY_SQL)) {

            preparedStatementMovie.setString(1, distillery.getName());
            preparedStatementMovie.executeUpdate();

            ResultSet generatedKeys = preparedStatementMovie.getGeneratedKeys();
            if (generatedKeys.next()) {
                Long distilleryID = generatedKeys.getLong(1);
                distillery.setId(distilleryID);

                for (Long vineID : vineIDs) {
                    preparedStatementActorMovie.setLong(1, vineID);
                    preparedStatementActorMovie.setLong(2, distilleryID);
                    preparedStatementActorMovie.addBatch();
                }
                preparedStatementActorMovie.executeBatch();
            } else {
                throw new SQLException("Error of obtaining the generated key for Movie");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return distillery;
    }

    @Override
    public Distillery update(Long id, Distillery updatedElement) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_DISTILLERY_SQL)) {

            preparedStatement.setString(1, updatedElement.getName());
            preparedStatement.setLong(2, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        updatedElement.setId(id);
        return updatedElement;
    }

    @Override
    public boolean remove(Long id) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(REMOVE_DISTILLERY_SQL)) {

            preparedStatement.setLong(1, id);
            int result = preparedStatement.executeUpdate();

            return result > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
