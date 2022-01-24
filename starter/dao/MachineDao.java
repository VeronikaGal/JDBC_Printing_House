package com.galeeva.jdbc.starter.dao;

import com.galeeva.jdbc.starter.entity.MachineEntity;
import com.galeeva.jdbc.starter.exception.DaoException;
import com.galeeva.jdbc.starter.util.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MachineDao implements Dao<Integer, MachineEntity> {

    private static final MachineDao INSTANCE = new MachineDao();

    private static final String FIND_ALL_SQL = """
            SELECT id,
                   model,
                   type
            FROM machine
            WHERE id = ?
            """;

    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE id = ?
            """;

    private static final String DELETE_SQL = """
            DELETE FROM machine
            WHERE id = ?
             """;

    private static final String SAVE_SQL = """
            INSERT INTO machine(model, type) 
            VALUES (?, ?);
            """;

    private static final String UPDATE_SQL = """
            UPDATE machine
            SET  model = ?,
                 type = ?
            WHERE id = ?  
            """;

    public static MachineDao getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean delete(Integer id) {
        try (var connection = ConnectionManager.get(); var prepareStatement = connection.prepareStatement(DELETE_SQL)) {
            prepareStatement.setInt(1, id);
            return prepareStatement.executeUpdate() > 0;
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    @Override
    public MachineEntity save(MachineEntity machine) {
        try (var connection = ConnectionManager.get(); var prepareStatement = connection.prepareStatement(SAVE_SQL)) {
            prepareStatement.setString(1, machine.getModel());
            prepareStatement.setString(2, machine.getType());

            prepareStatement.executeUpdate();

            ResultSet generatedKeys = prepareStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                machine.setId(generatedKeys.getInt("id"));
            }
            return machine;
        } catch (SQLException throwable) {
            throw new DaoException(throwable);
        }
    }

    @Override
    public void update(MachineEntity machine) {
        try (var connection = ConnectionManager.get();
             var prepareStatement = connection.prepareStatement(UPDATE_SQL)) {
            prepareStatement.setString(1, machine.getModel());
            prepareStatement.setString(2, machine.getType());
            prepareStatement.setInt(3, machine.getId());
            prepareStatement.executeUpdate();
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    @Override
    public Optional<MachineEntity> findById(Integer id) {
        try (var connection = ConnectionManager.get(); var prepareStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            prepareStatement.setInt(1, id);
            var resultSet = prepareStatement.executeQuery();
            MachineEntity machine = null;
            if (resultSet.next()) {
                machine = buildMachine(resultSet);
            }
            return Optional.ofNullable(machine);
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    @Override
    public List<MachineEntity> findAll() {
        try {
            var connection = ConnectionManager.get();
            var prepareStatement = connection.prepareStatement(FIND_ALL_SQL);
            var resultSet = prepareStatement.executeQuery();
            List<MachineEntity> machineEntities = new ArrayList<>();
            while (resultSet.next()) {
                machineEntities.add(buildMachine(resultSet));
            }
            return machineEntities;
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    private MachineEntity buildMachine(ResultSet resultSet) throws SQLException {
        return new MachineEntity(
                resultSet.getInt("id"),
                resultSet.getString("model"),
                resultSet.getString("type")
        );
    }
}
