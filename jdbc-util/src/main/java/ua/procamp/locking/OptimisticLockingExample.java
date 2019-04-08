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
public class OptimisticLockingExample {

  public static void main(String[] args) {
    SqlUtils.init();

    int id = 1;
    long version;

    Program p = findProgramById(id);
    if (p.getVersion() > 0) {
      version = p.getVersion();
    } else {
      throw new IllegalArgumentException("Program with such id wasn't found. id = " + id);
    }
    updateProgramByIdAndVersion(id, version);
    log.info("Successfully updated");

    SqlUtils.clear();
  }

  private static void updateProgramByIdAndVersion(int id, long version) {
    try (Connection connection = SqlUtils.postgresDataSource.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PROGRAM_SQL)) {
      preparedStatement.setString(1, "test program 2");
      preparedStatement.setLong(2, version + 1);
      preparedStatement.setInt(3, id);
      preparedStatement.setLong(4, version);
      int updated = preparedStatement.executeUpdate();
      if (updated != 1) {
        throw new IllegalArgumentException("The version of the program should be " + version);
      }
    } catch (SQLException e) {
      log.error(e.getMessage());
    }
  }

  private static Program findProgramById(int id) {
    Program p = new Program();
    try (Connection connection = SqlUtils.postgresDataSource.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(SELECT_PROGRAM_SQL)) {
      preparedStatement.setInt(1, id);
      ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        p.setId(resultSet.getLong("id"));
        p.setName(resultSet.getString("name"));
        p.setVersion(resultSet.getInt("version"));
      }
    } catch (SQLException e) {
      log.error(e.getMessage());
    }
    return p;
  }

}
