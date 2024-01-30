package mg.fita.kinkinasaifu.repositories;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.*;
import java.util.*;
import mg.fita.kinkinasaifu.annotations.Column;
import mg.fita.kinkinasaifu.annotations.Id;
import mg.fita.kinkinasaifu.annotations.Model;

public class ReflectCrudOperations<T> implements ReCrudOperations<T, Long> {
    private final Connection connection;
  public ReflectCrudOperations(Connection connection, String table_name) {
    this.connection = connection;
    this.table_name = table_name;
  }

  public String table_name;
  private Class<?> toClass() {
    return ((Class<?>)
        ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
  }

  private String getTableName() {
    return this.toClass().getAnnotation(Model.class).name();
  }

  private Long getId(T t) throws IllegalAccessException {
    Class<?> clazz = toClass();
    for (Field field : clazz.getDeclaredFields()) {
      if (field.isAnnotationPresent(Id.class)) {
        field.setAccessible(true);
        return (long) field.get(t);
      }
    }
    throw new IllegalArgumentException("No field annotated with @Id found in the entity class");
  }

  private List<String> getColumns() {
    List<Field> fields = List.of(this.toClass().getDeclaredFields());

    return fields.stream()
        .filter(field -> field.isAnnotationPresent(Column.class))
        .map(Field::getName)
        .toList();
  }
  @Override
  public List<T> findAll() {
    List<T> t = new ArrayList<>();
        String FIND_ALL_QUERY = String.format("SELECT * FROM \"%s\"", this.getTableName());
    try (PreparedStatement statement = connection.prepareStatement(FIND_ALL_QUERY);
         ResultSet resultSet = statement.executeQuery()) {
      while (resultSet.next()) {
        t.add(mapToT(resultSet));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return t;
  }

  @Override
  public T findById(Long id) {
    T t = null;
      String FIND_BY_ID_QUERY = String.format("SELECT * FROM \"%s\" WHERE \"%s\".id = ?",
              this.getTableName(),
              this.getTableName());
    try (PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_QUERY)) {
      statement.setLong(1, id);
      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          t = mapToT(resultSet);
        }
      }
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
      return t;
  }
  public String createInsertQuery(List<String> columns){
    StringBuilder SAVE_QUERY = new StringBuilder(String.format(
            "INSERT INTO \"%s\" (",
            getTableName()));
    StringBuilder valuesPart = new StringBuilder(") VALUES (");
    for (String column : columns){
      SAVE_QUERY.append(column).append(",");
      valuesPart.append("?,");
    }
    SAVE_QUERY = new StringBuilder(SAVE_QUERY.substring(0, SAVE_QUERY.length() - 1)
            + valuesPart.substring(0, valuesPart.length() - 1) + ")");
    return SAVE_QUERY.toString();
  }

  public String createUpdateQuery(List<String> columns, String conditionColumn) {
    StringBuilder UPDATE_QUERY = new StringBuilder(String.format(
            "UPDATE \"%s\" SET ",
            getTableName()
    ));
    for (String column : columns) {
      UPDATE_QUERY.append(String.format("\"%s\" = ?,", column));
    }
    UPDATE_QUERY = new StringBuilder(UPDATE_QUERY.substring(0, UPDATE_QUERY.length() - 1));
    UPDATE_QUERY.append(String.format(" WHERE \"%s\" = ?", conditionColumn));
    return UPDATE_QUERY.toString();
  }

  @Override
  public T save(T t) {
    String SAVE_QUERY = createInsertQuery(getColumns());
    String UPDATE_QUERY = createUpdateQuery(getColumns(), "id");

    // Check if the entity with the given ID already exists in the database
    if (t.getId(T) != null && findById(t.getId()) != null) {
      String SQL_QUERY = UPDATE_QUERY;

      try (PreparedStatement statement = connection.prepareStatement(SQL_QUERY)) {
        // Assuming you have a method to set the PreparedStatement parameters based on the entity
        setPreparedStatementParameters(statement, t);

        // Execute the update query
        statement.executeUpdate();
      } catch (SQLException e) {
        throw new RuntimeException("Error occurred while updating entity", e);
      }
    } else {
      // The entity does not exist in the database or has a null ID, perform an insert
      String SQL_QUERY = SAVE_QUERY;

      try (PreparedStatement statement = connection.prepareStatement(SQL_QUERY, Statement.RETURN_GENERATED_KEYS)) {
        // Assuming you have a method to set the PreparedStatement parameters based on the entity
        setPreparedStatementParameters(statement, t);

        // Execute the insert query
        statement.executeUpdate();

        // If it was an INSERT query, retrieve the generated keys and set them to the entity
        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
          if (generatedKeys.next()) {
            t.setId(generatedKeys.getLong(1));
          } else {
            throw new SQLException("Creating entity failed, no generated key obtained.");
          }
        }
      } catch (SQLException e) {
        throw new RuntimeException("Error occurred while saving entity", e);
      }
    }

    return t;
  }



  @Override
  public List<T> saveAll(List<T> toSave) {
    toSave.forEach(this::save);
    return toSave;
  }

  @Override
  public void delete(Long id) {
    String DELETE_QUERY = String.format("DELETE FROM \"%s\" WHERE id = ?", this.getTableName());
    try (PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
      statement.setLong(1, id);
      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
