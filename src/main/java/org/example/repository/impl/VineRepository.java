package org.example.repository.impl;

import org.example.dataBaseConfig.ConnectionPool;
import org.example.entity.Vine;
import org.example.repository.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class VineRepository implements Repository<Vine> {

    private static final String GET_ALL_VINE_SQL = "SELECT * FROM Vine";
    private static final String GET_VINE_BY_ID_SQL = "SELECT * FROM Vine WHERE id = ?";
    private static final String SAVE_VINE_SQL = "INSERT INTO Vine(name) VALUES (?)";
    private static final String UPDATE_VINE_SQL = "UPDATE Vine SET name=?, storeID=? WHERE id=?";
    private static final String REMOVE_VINE_SQL = "DELETE FROM Vine WHERE id=?";
    private final ConnectionPool connectionPool;

    public VineRepository() {
        connectionPool = ConnectionPool.getInstance();
    }

    public VineRepository(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }
    @Override
    public List<Vine> findAll() {
        List<Vine> vineList = new ArrayList<>();
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_VINE_SQL)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Vine vine = new Vine();
                vine.setId(resultSet.getLong(1));
                vine.setName(resultSet.getString(2));
                vine.setStoreID(resultSet.getLong(3));
                vineList.add(vine);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return vineList;
    }

    @Override
    public Vine findOne(Long id) {
        Vine vine = new Vine();
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_VINE_BY_ID_SQL)) {

            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                vine.setId(resultSet.getLong(1));
                vine.setName(resultSet.getString(2));
                vine.setId(resultSet.getLong(3));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return vine;
    }

    public Vine save(Vine vine) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_VINE_SQL, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, vine.getName());
            preparedStatement.setLong(2,vine.getStoreID());
            preparedStatement.setLong(3, vine.getStoreID());
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                Long vineID = generatedKeys.getLong(1);
                vine.setId(vineID);
            } else {
                throw new SQLException("Error of obtaining the generated key for Actor");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return vine;
    }

    @Override
    public Vine update(Long id, Vine updatedElement) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_VINE_SQL)) {

            preparedStatement.setString(1, updatedElement.getName());
            preparedStatement.setLong(2, updatedElement.getStoreID());
            preparedStatement.setLong(3, id);
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
             PreparedStatement preparedStatement = connection.prepareStatement(REMOVE_VINE_SQL)) {

            preparedStatement.setLong(1, id);
            int result = preparedStatement.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
