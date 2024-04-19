package org.example.repository.impl;

import org.example.dataBaseConfig.ConnectionPool;
import org.example.entity.Store;
import org.example.repository.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class StoreRepository implements Repository<Store> {
    private static final String GET_ALL_STORE_SQL = "SELECT * FROM Store";
    private static final String GET_STORE_BY_ID_SQL = "SELECT * FROM Store WHERE id = ?";
    private static final String SAVE_STORE_SQL = "INSERT INTO Store(name) VALUES (?)";
    private static final String UPDATE_STORE_SQL = "UPDATE Store SET name=? WHERE id=?";
    private static final String REMOVE_STORE_SQL = "DELETE FROM Store WHERE id=?";
    private final ConnectionPool connectionPool;

    public StoreRepository() {
        connectionPool = ConnectionPool.getInstance();
    }

    public StoreRepository(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public List<Store> findAll() {
        List<Store> storeList = new ArrayList<>();
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_STORE_SQL)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Store store = new Store();
                store.setId(resultSet.getLong(1));
                store.setName(resultSet.getString(2));
                storeList.add(store);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return storeList;
    }

    @Override
    public Store findOne(Long id) {
        Store vine = new Store();
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_STORE_BY_ID_SQL)) {

            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                vine.setId(resultSet.getLong(1));
                vine.setName(resultSet.getString(2));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return vine;
    }

    public Store save(Store store) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_STORE_SQL, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, store.getName());
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                Long storeID = generatedKeys.getLong(1);
                store.setId(storeID);
            } else {
                throw new SQLException("Error of obtaining the generated key for Actor");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return store;
    }

    @Override
    public Store update(Long id, Store updatedElement) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_STORE_SQL)) {

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
             PreparedStatement preparedStatement = connection.prepareStatement(REMOVE_STORE_SQL)) {

            preparedStatement.setLong(1, id);
            int result = preparedStatement.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
