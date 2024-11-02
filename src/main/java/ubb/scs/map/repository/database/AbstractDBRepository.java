package ubb.scs.map.repository.database;

import ubb.scs.map.domain.Entity;
import ubb.scs.map.domain.validators.Validator;
import ubb.scs.map.repository.memory.InMemoryRepository;

import java.sql.*;
import java.util.*;

public abstract class AbstractDBRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID, E> {
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "ionut2803";

    protected final Validator<E> validator;
    protected final TableName tableName;

    public AbstractDBRepository(Validator<E> validator, TableName tableName) {
        super(validator);
        this.validator = validator;
        this.tableName = tableName;
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    protected abstract E toEntity(ResultSet resultSet) throws SQLException;

    protected abstract Map<String, Object> getEntityValues(E entity);

    public Optional<E> findOne(ID Id) {
        StringBuilder sql = selectOneIdCommand();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql.toString())) {
            statement.setObject(1, Id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(toEntity(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    private StringBuilder selectOneIdCommand() {
        return new StringBuilder("SELECT * FROM " + tableName + " WHERE id = ?");
    }

    @Override
    public Iterable<E> findAll() {
        StringBuilder sql = selectAllCommand();
        List<E> entities = new ArrayList<>();
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql.toString());
             ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                entities.add(toEntity(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return entities;
    }

    private StringBuilder selectAllCommand() {
        return new StringBuilder("SELECT * FROM " + tableName);
    }


    public Optional<E> save(E entity) {
        validator.validate(entity);
        Map<String, Object> entityVals = getEntityValues(entity);
        StringBuilder sql = getInsertCommand(entityVals);

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql.toString())) {

            int index = 1;
            for (Object value : entityVals.values()) {
                statement.setObject(index, value);
                index = index + 1;
            }
            int withSuccess = statement.executeUpdate();
            if (withSuccess > 0) {
                return Optional.of(entity);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return Optional.empty();
    }

    private StringBuilder getInsertCommand(Map<String, Object> entityVals) {
        StringBuilder sql = new StringBuilder("INSERT INTO " + tableName.getName() + " (");
        entityVals.forEach((key, value) -> sql.append(key).append(", "));
        sql.setLength(sql.length() - 2);
        sql.append(") VALUES (");
        entityVals.forEach((key, value) -> sql.append("?, "));
        sql.setLength(sql.length() - 2);
        sql.append(")");
        return sql;
    }

    public Optional<E> delete(ID id) {
        Optional<E> deleted = findOne(id);
        if (deleted.isPresent()) {
            StringBuilder sql = deleteCommand();
            try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql.toString())) {
                statement.setObject(1, id);
                int withSucces = statement.executeUpdate();
                if (withSucces > 0) {
                    return deleted;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return Optional.empty();
    }

    private StringBuilder deleteCommand() {
        return new StringBuilder("DELETE FROM " + tableName + " WHERE id = ?");
    }

    private StringBuilder updateCommand(Map<String, Object> entityVals) {
        StringBuilder sql = new StringBuilder("UPDATE " + tableName + " SET ");
        entityVals.forEach((key, value) -> {
            if (!key.equals("ID")) {
                sql.append(key).append(" = ?, ");
            }
        });
        sql.setLength(sql.length() - 2);
        sql.append(" WHERE ID = ?");
        return sql;
    }

    public Optional<E> update(E entity) {
        validator.validate(entity);
        Map<String, Object> entityVals = getEntityValues(entity);

        StringBuilder sql = updateCommand(entityVals);

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql.toString())) {

            int index = 1;

            for (Map.Entry<String, Object> entry : entityVals.entrySet()) {
                if (!entry.getKey().equals("ID")) {
                    statement.setObject(index++, entry.getValue());
                }
            }
            statement.setObject(index, entity.getId());
            int withSuccess = statement.executeUpdate();
            if (withSuccess > 0) {
                return Optional.of(entity);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }
}
