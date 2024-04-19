package org.example.repository;

import org.example.dataBaseConfig.ConnectionPool;
import org.example.entity.Store;
import org.example.repository.impl.StoreRepository;
import org.example.util.TestObjectInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class StoreRepositoryTest {

    @Mock
    private ConnectionPool connectionPool;
    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private ResultSet resultSet;
    @InjectMocks
    private StoreRepository storeRepository;
    @Spy
    private StoreRepository spyRepository;

    private Store storeExpected;

    @BeforeEach
    void setup() {
        storeRepository = new StoreRepository(connectionPool);
        spyRepository = spy(storeRepository);
        storeExpected = TestObjectInitializer.initializeStore();
    }

    @Test
    void findAllTest() {
        try {
            Field sqlField = StoreRepository.class.getDeclaredField("GET_ALL_STORE_SQL");
            sqlField.setAccessible(true);
            String getAllStoreSql = (String) sqlField.get(storeRepository);

            when(connectionPool.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);

            List<Store> storeList = storeRepository.findAll();

            assertNotNull(storeList);
            verify(connection).prepareStatement(eq(getAllStoreSql));
            verify(preparedStatement).executeQuery();
        } catch (SQLException | NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void findOneTest() {
        try {
            Field sqlField = StoreRepository.class.getDeclaredField("GET_STORE_BY_ID_SQL");
            sqlField.setAccessible(true);
            String getStoreByIdSql = (String) sqlField.get(storeRepository);

            when(connectionPool.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(spyRepository.findOne(1L)).thenReturn(storeExpected);

            Store store = spyRepository.findOne(1L);

            assertNotNull(store);
            assertEquals(1, store.getId());
            assertEquals("TestStore", store.getName());
            verify(connection).prepareStatement(eq(getStoreByIdSql));
        } catch (SQLException | NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void saveTest() {
        try {
            Field sqlFieldStore = StoreRepository.class.getDeclaredField("SAVE_STORE_SQL");
            sqlFieldStore.setAccessible(true);
            String saveStoreql = (String) sqlFieldStore.get(storeRepository);

            when(connectionPool.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS)))
                    .thenReturn(preparedStatement);
            when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
            when(preparedStatement.getGeneratedKeys().next()).thenReturn(true);

            Store savedStore = storeRepository.save(storeExpected);

            assertNotNull(savedStore);
            verify(connection).prepareStatement(eq(saveStoreql), eq(Statement.RETURN_GENERATED_KEYS));
            verify(preparedStatement).setString(1, storeExpected.getName());
            verify(preparedStatement).executeUpdate();

        } catch (SQLException | NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void updateTest() {
        try {
            Field sqlField = StoreRepository.class.getDeclaredField("UPDATE_STORE_SQL");
            sqlField.setAccessible(true);
            String updateStoreSql = (String) sqlField.get(storeRepository);

            when(connectionPool.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

            Store updatedStore = new Store();
            updatedStore.setId(1L);
            updatedStore.setName("UpdatedTestStore");

            Store result = storeRepository.update(1L, updatedStore);

            assertNotNull(result);
            assertEquals(1, result.getId());
            assertEquals("UpdatedTestStore", result.getName());
            verify(connection).prepareStatement(eq(updateStoreSql));
            verify(preparedStatement).executeUpdate();
            verify(preparedStatement).setString(eq(1), eq("UpdatedTestStore"));
            verify(preparedStatement).setLong(eq(2), eq(1L));
        } catch (SQLException | NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void removeTest() {
        try {
            Field sqlField = StoreRepository.class.getDeclaredField("REMOVE_STORE_SQL");
            sqlField.setAccessible(true);
            String removeStoreSql = (String) sqlField.get(storeRepository);

            when(connectionPool.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeUpdate()).thenReturn(1);

            boolean result = storeRepository.remove(1L);

            assertTrue(result);
            verify(connection).prepareStatement(eq(removeStoreSql));
            verify(preparedStatement).setLong(eq(1), eq(1L));
            verify(preparedStatement).executeUpdate();
        } catch (SQLException | NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}