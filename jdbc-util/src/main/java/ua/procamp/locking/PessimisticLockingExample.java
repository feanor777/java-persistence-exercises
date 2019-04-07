package ua.procamp.locking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import lombok.extern.slf4j.Slf4j;
import ua.procamp.locking.meta.SqlUtils;
import ua.procamp.locking.model.Program;

import static ua.procamp.locking.meta.QueryConstants.SELECT_PROGRAM_SQL;
import static ua.procamp.locking.meta.QueryConstants.UPDATE_PROGRAM_SQL;

@Slf4j
public class PessimisticLockingExample {

  public static void main(String[] args) {
    SqlUtils.init();

    int id = 1;
    long version;

    try (Connection connection = SqlUtils.postgresDataSource.getConnection()) {
      connection.setAutoCommit(false);
      Program p = findProgramById(id, connection);
      if (p.getVersion() > 0) {
        version = p.getVersion();
      } else {
        throw new IllegalArgumentException("Program with such id wasn't found. id = " + id);
      }
      int updated = updateNameByIdAndVersion(id, version, connection);
      if (updated != 1) {
        throw new IllegalArgumentException("The version of the program should be " + version);
      }
      connection.setAutoCommit(true);
    } catch (SQLException e) {
      log.error(e.getMessage());
    }
    log.info("Successfully updated");
    SqlUtils.clear();
  }

  private static int updateNameByIdAndVersion(int id, long version, Connection connection) throws SQLException {
    PreparedStatement updateStatement = connection.prepareStatement(UPDATE_PROGRAM_SQL);
    updateStatement.setString(1, "test program 2");
    updateStatement.setInt(2, id);
    updateStatement.setLong(3, version);
    return updateStatement.executeUpdate();
  }

  private static Program findProgramById(int id, Connection connection) throws SQLException {
    PreparedStatement preparedStatement = connection.prepareStatement(SELECT_PROGRAM_SQL);
    preparedStatement.setInt(1, id);
    ResultSet resultSet = preparedStatement.executeQuery();
    return toProgram(resultSet);
  }

  private static Program toProgram(ResultSet resultSet) throws SQLException {
    Program p = new Program();
    while (resultSet.next()) {
      p.setId(resultSet.getLong("id"));
      p.setName(resultSet.getString("name"));
      p.setVersion(resultSet.getInt("version"));
    }
    return p;
  }
}
