package org.example.repository;

import org.example.dataBaseConfig.ConnectionPool;
import org.example.entity.Vine;
import org.example.repository.impl.VineRepository;
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
public class VineRepositoryTest {
    @Mock
    private ConnectionPool connectionPool;
    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private ResultSet resultSet;
    @InjectMocks
    private VineRepository vineRepository;
    @Spy
    private VineRepository spyRepository;

    private Vine vineExpected;

    @BeforeEach
    void setup() {
        vineRepository = new VineRepository(connectionPool);
        spyRepository = spy(vineRepository);
        vineExpected = TestObjectInitializer.initializeVine();
    }

    @Test
    void findAllTest() {
        try {
            Field sqlField = VineRepository.class.getDeclaredField("GET_ALL_VINE_SQL");
            sqlField.setAccessible(true);
            String getAllVineSql = (String) sqlField.get(vineRepository);

            when(connectionPool.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);

            List<Vine> vineList = vineRepository.findAll();

            assertNotNull(vineList);
            verify(connection).prepareStatement(eq(getAllVineSql));
            verify(preparedStatement).executeQuery();
        } catch (SQLException | NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void findOneTest() {
        try {
            Field sqlField = VineRepository.class.getDeclaredField("GET_VINE_BY_ID_SQL");
            sqlField.setAccessible(true);
            String getVineByIdSql = (String) sqlField.get(vineRepository);

            when(connectionPool.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(spyRepository.findOne(1L)).thenReturn(vineExpected);

            Vine vine = spyRepository.findOne(1L);

            assertNotNull(vine);
            assertEquals(1, vine.getId());
            assertEquals("TestVine", vine.getName());
            verify(connection).prepareStatement(eq(getVineByIdSql));
        } catch (SQLException | NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void saveTest() {
        try {
            Field sqlFieldStore = VineRepository.class.getDeclaredField("SAVE_VINE_SQL");
            sqlFieldStore.setAccessible(true);
            String saveStoreql = (String) sqlFieldStore.get(vineRepository);

            when(connectionPool.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS)))
                    .thenReturn(preparedStatement);
            when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
            when(preparedStatement.getGeneratedKeys().next()).thenReturn(true);

            Vine savedStore = vineRepository.save(vineExpected);

            assertNotNull(savedStore);
            verify(connection).prepareStatement(eq(saveStoreql), eq(Statement.RETURN_GENERATED_KEYS));
            verify(preparedStatement).setString(1, vineExpected.getName());
            verify(preparedStatement).setLong(2,vineExpected.getStoreID());
            verify(preparedStatement).executeUpdate();

        } catch (SQLException | NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void updateTest() {
        try {
            Field sqlField = VineRepository.class.getDeclaredField("UPDATE_VINE_SQL");
            sqlField.setAccessible(true);
            String updateVineSql = (String) sqlField.get(vineRepository);

            when(connectionPool.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

            Vine updatedStore = new Vine();
            updatedStore.setId(1L);
            updatedStore.setName("UpdatedTestVine");
            updatedStore.setStoreID(1L);

            Vine result = vineRepository.update(1L, updatedStore);

            assertNotNull(result);
            assertEquals(1, result.getId());
            assertEquals("UpdatedTestVine", result.getName());
            assertEquals(1,result.getStoreID());
            verify(connection).prepareStatement(eq(updateVineSql));
            verify(preparedStatement).executeUpdate();
            verify(preparedStatement).setString(eq(1), eq("UpdatedTestVine"));
            verify(preparedStatement).setLong(eq(2), eq(1L));
            verify(preparedStatement).setLong(eq(3),eq(1L));
        } catch (SQLException | NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void removeTest() {
        try {
            Field sqlField = VineRepository.class.getDeclaredField("REMOVE_VINE_SQL");
            sqlField.setAccessible(true);
            String removeVineSql = (String) sqlField.get(vineRepository);

            when(connectionPool.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeUpdate()).thenReturn(1);

            boolean result = vineRepository.remove(1L);

            assertTrue(result);
            verify(connection).prepareStatement(eq(removeVineSql));
            verify(preparedStatement).setLong(eq(1), eq(1L));
            verify(preparedStatement).executeUpdate();
        } catch (SQLException | NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
