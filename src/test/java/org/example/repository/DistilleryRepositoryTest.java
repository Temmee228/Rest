package org.example.repository;

import org.example.dataBaseConfig.ConnectionPool;
import org.example.entity.Distillery;
import org.example.repository.impl.DistilleryRepository;
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
class DistilleryRepositoryTest {

    @Mock
    private ConnectionPool connectionPool;
    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private PreparedStatement preparedStatementDistilleryVine;
    @Mock
    private ResultSet resultSet;
    @InjectMocks
    private DistilleryRepository distilleryRepository;
    @Spy
    private DistilleryRepository spyRepository;

    private Distillery distilleryExpected;

    @BeforeEach
    void setup() {
        distilleryRepository = new DistilleryRepository(connectionPool);
        spyRepository = spy(distilleryRepository);
        distilleryExpected = TestObjectInitializer.initializeDistillery();
    }

    @Test
    void findAllTest() {
        try {
            Field sqlField = DistilleryRepository.class.getDeclaredField("GET_ALL_DISTILLERY_SQL");
            sqlField.setAccessible(true);
            String getAllDistillerySql = (String) sqlField.get(distilleryRepository);

            when(connectionPool.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);

            List<Distillery> distilleryList = distilleryRepository.findAll();

            assertNotNull(distilleryList);
            verify(connection).prepareStatement(eq(getAllDistillerySql));
            verify(preparedStatement).executeQuery();
        } catch (SQLException | NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void findOneTest() {
        try {
            Field sqlField = DistilleryRepository.class.getDeclaredField("GET_DISTILLERY_BY_ID_SQL");
            sqlField.setAccessible(true);
            String getDistilleryByIdSql = (String) sqlField.get(distilleryRepository);

            when(connectionPool.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(spyRepository.findOne(1L)).thenReturn(distilleryExpected);

            Distillery distillery = spyRepository.findOne(1L);

            assertNotNull(distillery);
            assertEquals(1, distillery.getId());
            assertEquals("TestDistillery", distillery.getName());
            verify(connection).prepareStatement(eq(getDistilleryByIdSql));
        } catch (SQLException | NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void saveTest() {
        try {
            Field sqlFieldDistillery = DistilleryRepository.class.getDeclaredField("SAVE_DISTILLERY_SQL");
            sqlFieldDistillery.setAccessible(true);
            String saveDistillerySql = (String) sqlFieldDistillery.get(distilleryRepository);

            Field sqlFieldDistilleryVine = DistilleryRepository.class.getDeclaredField("SAVE_VINE_DISTILLERY_SQL");
            sqlFieldDistilleryVine.setAccessible(true);
            String saveDistilleryVineSql = (String) sqlFieldDistilleryVine.get(distilleryRepository);

            when(connectionPool.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(eq(saveDistillerySql), eq(Statement.RETURN_GENERATED_KEYS)))
                    .thenReturn(preparedStatement);
            when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true);

            when(connection.prepareStatement(eq(saveDistilleryVineSql))).thenReturn(preparedStatementDistilleryVine);

            List<Long> vineIds = List.of(1L);

            Distillery savedDistillery = distilleryRepository.save(distilleryExpected, vineIds);

            assertNotNull(savedDistillery);
            verify(connection).prepareStatement(eq(saveDistillerySql), eq(Statement.RETURN_GENERATED_KEYS));
            verify(preparedStatement).setString(1, distilleryExpected.getName());
            verify(preparedStatement).executeUpdate();

            verify(connection).prepareStatement(eq(saveDistilleryVineSql));
            for (Long vineId : vineIds) {
                verify(preparedStatementDistilleryVine).setLong(1, vineId);
                verify(preparedStatementDistilleryVine).setLong(2, savedDistillery.getId());
                verify(preparedStatementDistilleryVine).addBatch();
            }
            verify(preparedStatementDistilleryVine).executeBatch();
        } catch (SQLException | NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void updateTest() {
        try {
            Field sqlField = DistilleryRepository.class.getDeclaredField("UPDATE_DISTILLERY_SQL");
            sqlField.setAccessible(true);
            String updateDistillerySql = (String) sqlField.get(distilleryRepository);

            when(connectionPool.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

            Distillery updatedDistillery = new Distillery();
            updatedDistillery.setId(1L);
            updatedDistillery.setName("UpdatedTestDistillery");

            Distillery result = distilleryRepository.update(1L, updatedDistillery);

            assertNotNull(result);
            assertEquals(1, result.getId());
            assertEquals("UpdatedTestDistillery", result.getName());
            verify(connection).prepareStatement(eq(updateDistillerySql));
            verify(preparedStatement).executeUpdate();
            verify(preparedStatement).setString(eq(1), eq("UpdatedTestDistillery"));
            verify(preparedStatement).setLong(eq(2), eq(1L));
        } catch (SQLException | NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void removeTest() {
        try {
            Field sqlField = DistilleryRepository.class.getDeclaredField("REMOVE_DISTILLERY_SQL");
            sqlField.setAccessible(true);
            String removeDistillerySql = (String) sqlField.get(distilleryRepository);

            when(connectionPool.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeUpdate()).thenReturn(1);

            boolean result = distilleryRepository.remove(1L);

            assertTrue(result);
            verify(connection).prepareStatement(eq(removeDistillerySql));
            verify(preparedStatement).setLong(eq(1), eq(1L));
            verify(preparedStatement).executeUpdate();
        } catch (SQLException | NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}