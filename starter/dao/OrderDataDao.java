package com.galeeva.jdbc.starter.dao;

import com.galeeva.jdbc.starter.dto.OrderDataFilter;
import com.galeeva.jdbc.starter.entity.MachineEntity;
import com.galeeva.jdbc.starter.entity.OrderDataEntity;
import com.galeeva.jdbc.starter.entity.UserEntity;
import com.galeeva.jdbc.starter.exception.DaoException;
import com.galeeva.jdbc.starter.util.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderDataDao implements Dao<Long, OrderDataEntity> {

    private static final OrderDataDao INSTANCE = new OrderDataDao();

    private static final String DELETE_SQL = """
            DELETE FROM order_data
            WHERE id = ?
             """;

    private static final String SAVE_SQL = """
            INSERT INTO order_data(users_id, service_id, file, paper_type, quantity, machine_id, status, total_price, 
            created_at, delivered_at, delivery) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
            """;

    private static final String UPDATE_SQL = """
            UPDATE order_data
            SET  users_id = ?,
                 service_id = ?,
                 file=?,
                 paper_type= ?,
                 quantity = ?,
                 machine_id = ?,
                 status= ?,
                 total_price = ?, 
                 created_at = ?,
                 delivered_at = ?,
                 delivery = ?  
            WHERE id = ?  
            """;

    private static final String FIND_ALL_SQL = """
            SELECT id,
                   users_id,
                   service_id,
                   file,
                   paper_type,
                   quantity, 
                   machine_id,
                   status,
                   total_price,
                   created_at, 
                   delivered_at,
                   delivery
            FROM order_data
            WHERE id = ?
            """;

    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE id = ?
            """;

    private final UserDao userDao = UserDao.getInstance();

    private final MachineDao machineDao = MachineDao.getInstance();

    private final ServiceDao serviceDao = ServiceDao.getInstance();

    private OrderDataDao() {
    }

    public List<OrderDataEntity> findAll(OrderDataFilter filter) {
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
            List<OrderDataEntity> orderDataEntities = new ArrayList<>();
            while (resultSet.next()) {
                orderDataEntities.add(buildOrderData(resultSet));
            }
            return orderDataEntities;
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    public List<OrderDataEntity> findAll() {
        try {
            var connection = ConnectionManager.get();
            var prepareStatement = connection.prepareStatement(FIND_ALL_SQL);
            var resultSet = prepareStatement.executeQuery();
            List<OrderDataEntity> orderDataEntities = new ArrayList<>();
            while (resultSet.next()) {
                orderDataEntities.add(buildOrderData(resultSet));
            }
            return orderDataEntities;
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    public Optional<OrderDataEntity> findById(Long id) {
        try (var connection = ConnectionManager.get(); var prepareStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            var resultSet = prepareStatement.executeQuery();
            OrderDataEntity orderData = null;
            if (resultSet.next()) {
                orderData = buildOrderData(resultSet);
            }
            return Optional.ofNullable(orderData);
        } catch (SQLException throwable) {
            throw new DaoException(throwable);
        }
    }

    private OrderDataEntity buildOrderData(ResultSet resultSet) throws SQLException {
        return new OrderDataEntity(
                resultSet.getLong("id"),
                userDao.findById(resultSet.getLong("users_id")).orElse(null),
                serviceDao.findById(resultSet.getInt("service_id")).orElse(null),
                resultSet.getString("file"),
                resultSet.getString("paper_type"),
                resultSet.getLong("quantity "),
                machineDao.findById(resultSet.getInt("machine_id")).orElse(null),
                resultSet.getString("status"),
                resultSet.getBigDecimal("total_price"),
                resultSet.getTimestamp("created_at").toLocalDateTime(),
                resultSet.getTimestamp("delivery_at").toLocalDateTime(),//Null ?
                resultSet.getString("delivery")

        );
    }

    public void update(OrderDataEntity orderData) {
        try (var connection = ConnectionManager.get();
             var prepareStatement = connection.prepareStatement(UPDATE_SQL)) {
            prepareStatement.setLong(1, orderData.getUsersId().getId());
            prepareStatement.setInt(2, orderData.getServiceId().getId());
            prepareStatement.setString(3, orderData.getFile());
            prepareStatement.setString(4, orderData.getPaperType());
            prepareStatement.setLong(5, orderData.getQuantity());
            prepareStatement.setObject(6, orderData.getMachineId());
            prepareStatement.setString(7, orderData.getStatus());
            prepareStatement.setBigDecimal(8, orderData.getTotalPrice());
            prepareStatement.setTimestamp(9, Timestamp.valueOf(orderData.getCreatedAt()));
            prepareStatement.setObject(10, orderData.getDeliveredAt());
            prepareStatement.setString(11, orderData.getDelivery());
            prepareStatement.setLong(12, orderData.getId());

            prepareStatement.executeUpdate();
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    public OrderDataEntity save(OrderDataEntity orderData) {
        try (var connection = ConnectionManager.get(); var prepareStatement = connection.prepareStatement(SAVE_SQL)) {
            prepareStatement.setLong(1, orderData.getUsersId().getId());
            prepareStatement.setInt(2, orderData.getServiceId().getId());
            prepareStatement.setString(3, orderData.getFile());
            prepareStatement.setString(4, orderData.getPaperType());
            prepareStatement.setLong(5, orderData.getQuantity());
            prepareStatement.setInt(6, orderData.getMachineId().getId());  // ?Null
            prepareStatement.setString(7, orderData.getStatus());
            prepareStatement.setBigDecimal(8, orderData.getTotalPrice());
            prepareStatement.setTimestamp(9, Timestamp.valueOf(orderData.getCreatedAt()));
            prepareStatement.setTimestamp(10, Timestamp.valueOf(orderData.getDeliveredAt()));  // ?Null
            prepareStatement.setString(11, orderData.getDelivery());

            prepareStatement.executeUpdate();

            ResultSet generatedKeys = prepareStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                orderData.setId(generatedKeys.getLong("id"));
            }
            return orderData;
        } catch (SQLException throwable) {
            throw new DaoException(throwable);
        }
    }

    public boolean delete(Long id) {
        try (var connection = ConnectionManager.get(); var prepareStatement = connection.prepareStatement(DELETE_SQL)) {
            prepareStatement.setLong(1, id);
            return prepareStatement.executeUpdate() > 0;
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    public static OrderDataDao getInstance() {
        return INSTANCE;
    }
}
