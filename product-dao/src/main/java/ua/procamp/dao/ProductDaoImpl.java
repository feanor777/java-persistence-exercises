package ua.procamp.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ua.procamp.exception.DaoOperationException;
import ua.procamp.model.Product;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoImpl implements ProductDao {
  private static final Logger LOGGER = LoggerFactory.getLogger(ProductDaoImpl.class);

  private static final String INSERT_PRODUCT = "INSERT INTO products (name, producer, price, expiration_date) VALUES (?, ?, ?, ?)";
  private static final String SELECT_ALL_PRODUCTS = "SELECT id, name, producer, price, expiration_date, creation_time FROM products";
  private static final String SELECT_PRODUCT_BY_ID = "SELECT id, name, producer, price, expiration_date, creation_time FROM products WHERE id = ?";
  private static final String UPDATE_PRODUCT_BY_ID = "UPDATE products SET name = ?, producer = ?, price = ?, expiration_date = ? WHERE id = ?";
  private static final String DELETE_PRODUCTS_BY_ID = "DELETE FROM products WHERE id = ?";

  private DataSource dataSource;

  public ProductDaoImpl(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public void save(Product product) {
    validateProduct(product);
    try (Connection connection = dataSource.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PRODUCT, Statement.RETURN_GENERATED_KEYS)) {

      preparedStatement.setString(1, product.getName());
      preparedStatement.setString(2, product.getProducer());
      preparedStatement.setBigDecimal(3, product.getPrice());
      preparedStatement.setDate(4, Date.valueOf(product.getExpirationDate()));
      int createdProductCount = preparedStatement.executeUpdate();
      if (createdProductCount == 0) {
        throw new DaoOperationException("The product wasn't created. Product name = " + product.getName());
      }

      try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
        if (generatedKeys.next()) {
          product.setId(generatedKeys.getLong(1));
        }
      }
    } catch (SQLException e) {
      LOGGER.error(e.getMessage(), e);
    }
  }

  private void validateProduct(Product product) {
    if (product == null || product.getProducer() == null) {
      throw new DaoOperationException(String.format("Error saving product: %s", product));
    }
  }

  @Override
  public List<Product> findAll() {
    List<Product> products = new ArrayList<>();
    try (Connection connection = dataSource.getConnection();
         Statement statement = connection.createStatement();
         ResultSet resultSet = statement.executeQuery(SELECT_ALL_PRODUCTS)) {
      while (resultSet.next()) {
        Product p = toProduct(resultSet);
        products.add(p);
      }
    } catch (SQLException e) {
      LOGGER.error(e.getMessage(), e);
    }
    return products;
  }

  @Override
  public Product findOne(Long id) {
    try (Connection connection = dataSource.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(SELECT_PRODUCT_BY_ID)) {
      preparedStatement.setLong(1, id);
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        if (resultSet.next()) {
          return toProduct(resultSet);
        }
      }
    } catch (SQLException e) {
      LOGGER.error(e.getMessage(), e);
    }
    throw new DaoOperationException(String.format("Product with id = %s does not exist", id));
  }

  @Override
  public void update(Product product) {
    if (product == null || product.getId() == null) {
      throw new DaoOperationException("Product id cannot be null");
    }
    try (Connection connection = dataSource.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PRODUCT_BY_ID)) {
      fillPreparedStatement(preparedStatement, product);
      int updatedProductCount = preparedStatement.executeUpdate();
      if (updatedProductCount == 0) {
        throw new DaoOperationException(String.format("Product with id = %d does not exist", product.getId()));
      }
    } catch (SQLException e) {
      LOGGER.error(e.getMessage(), e);
    }
  }

  @Override
  public void remove(Product product) {
    if (product == null || product.getId() == null) {
      throw new DaoOperationException("Product id cannot be null");
    }
    try (Connection connection = dataSource.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(DELETE_PRODUCTS_BY_ID)) {
      preparedStatement.setLong(1, product.getId());
      int removedProductCount = preparedStatement.executeUpdate();
      if (removedProductCount == 0) {
        throw new DaoOperationException(String.format("Product with id = %d does not exist", product.getId()));
      }
    } catch (SQLException e) {
      LOGGER.error(e.getMessage(), e);
    }
  }

  private void fillPreparedStatement(PreparedStatement preparedStatement, Product product) throws SQLException {
    preparedStatement.setString(1, product.getName());
    preparedStatement.setString(2, product.getProducer());
    preparedStatement.setBigDecimal(3, product.getPrice());
    preparedStatement.setDate(4, Date.valueOf(product.getExpirationDate()));
    preparedStatement.setLong(5, product.getId());
  }

  private Product toProduct(ResultSet resultSet) throws SQLException {
    Product p = new Product();
    p.setId(resultSet.getLong("id"));
    p.setName(resultSet.getString("name"));
    p.setProducer(resultSet.getString("producer"));
    p.setPrice(resultSet.getBigDecimal("price"));
    p.setExpirationDate(resultSet.getDate("expiration_date").toLocalDate());
    p.setCreationTime(resultSet.getTimestamp("creation_time").toLocalDateTime());
    return p;
  }

}
