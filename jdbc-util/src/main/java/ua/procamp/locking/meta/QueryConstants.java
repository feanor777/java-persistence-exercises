package ua.procamp.locking.meta;

public final class QueryConstants {
  private QueryConstants() {

  }

  public static final String CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS programs ( id SERIAL NOT NULL PRIMARY KEY, name VARCHAR(255) NOT NULL, version BIGINT NOT NULL, CONSTRAINT programs_version_bigger_zero check (version > 0))";
  public static final String DROP_TABLE_SQL = "DROP TABLE IF EXISTS programs";
  public static final String INSERT_PROGRAM_SQL = "INSERT INTO programs (name, version) VALUES ('test program', 1)";
  public static final String UPDATE_PROGRAM_SQL = "UPDATE programs SET name = ? WHERE id = ? AND version = ?";
  public static final String SELECT_PROGRAM_SQL = "SELECT * FROM programs where id = ?";
}
