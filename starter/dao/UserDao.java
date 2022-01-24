package com.galeeva.jdbc.starter.dao;

import com.galeeva.jdbc.starter.dto.UserFilter;
import com.galeeva.jdbc.starter.entity.UserEntity;
import com.galeeva.jdbc.starter.exception.DaoException;
import com.galeeva.jdbc.starter.util.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDao implements Dao<Long, UserEntity> {

    private static final UserDao INSTANCE = new UserDao();

    private static final String FIND_ALL_SQL = """
            SELECT id,
                   name,
                   phone_number,
                   address,
                   role,
                   email,
                   password
            FROM users
            WHERE id = ?
            """;

    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE id = ?
            """;

    private static final String DELETE_SQL = """
            DELETE FROM users
            WHERE id = ?
             """;

    private static final String SAVE_SQL = """
            INSERT INTO users(name, phone_number, address, role, email, password) 
            VALUES (?, ?, ?, ?, ?, ?);
            """;

    private static final String UPDATE_SQL = """
            UPDATE users
            SET  name = ?,
                 phone_number = ?,
                 address=?,
                 role = ?,
                 email = ?,
                 password = ?  
            WHERE id = ?  
            """;

    private UserDao() {
    }

    public static UserDao getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean delete(Long id) {
        try (var connection = ConnectionManager.get(); var prepareStatement = connection.prepareStatement(DELETE_SQL)) {
            prepareStatement.setLong(1, id);
            return prepareStatement.executeUpdate() > 0;
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    @Override
    public UserEntity save(UserEntity user) {
        try (var connection = ConnectionManager.get(); var prepareStatement = connection.prepareStatement(SAVE_SQL)) {
            prepareStatement.setString(1, user.getName());
            prepareStatement.setString(2, user.getPhone_number());
            prepareStatement.setString(3, user.getAddress());
            prepareStatement.setInt(4, user.getRole());
            prepareStatement.setString(5, user.getEmail());
            prepareStatement.setString(6, user.getPassword());
            prepareStatement.executeUpdate();
            ResultSet generatedKeys = prepareStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                user.setId(generatedKeys.getLong("id"));
            }
            return user;
        } catch (SQLException throwable) {
            throw new DaoException(throwable);
        }
    }

    @Override
    public void update(UserEntity user) {
        try (var connection = ConnectionManager.get();
             var prepareStatement = connection.prepareStatement(UPDATE_SQL)) {
            prepareStatement.setString(1, user.getName());
            prepareStatement.setString(2, user.getPhone_number());
            prepareStatement.setString(3, user.getAddress());
            prepareStatement.setInt(4, user.getRole());
            prepareStatement.setString(5, user.getEmail());
            prepareStatement.setString(6, user.getPassword());
            prepareStatement.setLong(7, user.getId());

            prepareStatement.executeUpdate();
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    @Override
    public Optional<UserEntity> findById(Long id) {
        try (var connection = ConnectionManager.get(); var prepareStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            prepareStatement.setLong(1, id);
            var resultSet = prepareStatement.executeQuery();
            UserEntity user = null;
            if (resultSet.next()) {
                user = buildUser(resultSet);
            }
            return Optional.ofNullable(user);
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    @Override
    public List<UserEntity> findAll() {
        try {
            var connection = ConnectionManager.get();
            var prepareStatement = connection.prepareStatement(FIND_ALL_SQL);
            var resultSet = prepareStatement.executeQuery();
            List<UserEntity> userEntities = new ArrayList<>();
            while (resultSet.next()) {
                userEntities.add(buildUser(resultSet));
            }
            return userEntities;
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    public List<UserEntity> findAll(UserFilter filter) {
        List<Object> parameters = new ArrayList<>();
        parameters.add(filter.limit());
        parameters.add(filter.offset());
        var sql = FIND_ALL_SQL + """
                LIMIT ?
                OFFSET ?
                """;
        try (var connection = ConnectionManager.get(); var prepareStatement = connection.prepareStatement(sql);) {
            for (int i = 0; i < parameters.size(); i++) {
                prepareStatement.setObject(i + 1, parameters.get(i));
            }
            System.out.println(prepareStatement);
            var resultSet = prepareStatement.executeQuery();
            List<UserEntity> userEntities = new ArrayList<>();
            while (resultSet.next()) {
                userEntities.add(buildUser(resultSet));
            }
            return userEntities;
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    private UserEntity buildUser(ResultSet resultSet) throws SQLException {
        return new UserEntity(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("phone_number"),
                resultSet.getString("address"),
                resultSet.getInt("role"),
                resultSet.getString("email"),
                resultSet.getString("password")
        );
    }
}
