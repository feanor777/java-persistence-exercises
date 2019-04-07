package ua.procamp.locking.meta;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import lombok.extern.slf4j.Slf4j;
import ua.procamp.util.JdbcUtil;

import static ua.procamp.locking.meta.QueryConstants.CREATE_TABLE_SQL;
import static ua.procamp.locking.meta.QueryConstants.DROP_TABLE_SQL;
import static ua.procamp.locking.meta.QueryConstants.INSERT_PROGRAM_SQL;

@Slf4j
public final class SqlUtils {
  private SqlUtils() {

  }

  public static final DataSource postgresDataSource = JdbcUtil.createPostgresDataSource("jdbc:postgresql://localhost:5437/gl_procamp", "gl_procamp", "test123");


  public static void init() {
    try (Connection connection = postgresDataSource.getConnection();
         Statement statement = connection.createStatement()) {
      statement.executeUpdate(CREATE_TABLE_SQL);
      statement.executeUpdate(INSERT_PROGRAM_SQL);
    } catch (SQLException e) {
      log.error(e.getMessage());
    }
  }

  public static void clear() {
    try (Connection connection = postgresDataSource.getConnection();
         Statement statement = connection.createStatement()) {
      statement.executeUpdate(DROP_TABLE_SQL);
    } catch (SQLException e) {
      log.error(e.getMessage());
    }
  }
}
