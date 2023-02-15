package dao.impl;

import dao.EmployeeDAO;
import model.City;
import model.Employee;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class EmployeeDAOImpl implements EmployeeDAO {
    private Connection connection;

    public EmployeeDAOImpl(Connection connection) {
        this.connection = connection;
    }


    /*1. Создание(добавление) сущности model.Employee в таблицу
   2. Получение конкретного объекта model.Employee по id
3. Получение списка всех объектов model.Employee из базы
4. Изменение конкретного объекта model.Employee в базе по id
5. Удаление конкретного объекта model.Employee из базы по id*/

    final static String CREATE = "INSERT INTO employee (first_name, last_name, gender, age, city_id) VALUES ((?), (?), (?), (?), (?))";
    final static String READBYID = "SELECT * FROM employee INNER JOIN city ON employee.city_id=city.city_id AND id=(?)";
    final static String READALL = "SELECT * FROM employee INNER JOIN city ON employee.city_id=city.city_id";
    final static String UPDATE = "UPDATE employee SET first_name=(?), last_name=(?), gender=(?), age=(?), city_id=(?) WHERE id=(?)";
    final static String DELETE = "DELETE FROM employee WHERE id=(?)";

    private Employee readEmployee(ResultSet resultSet) throws SQLException {
        // С помощью методов getInt и getString получаем данные из resultSet
        // и присваиваем их полям объекта
        Employee employee = new Employee();
        employee.setId(resultSet.getInt("id"));
        employee.setFirstName(resultSet.getString("first_name"));
        employee.setLastName(resultSet.getString("last_name"));
        employee.setGender(resultSet.getString("gender"));
        employee.setAge(resultSet.getInt("age"));
        employee.setCity(new City(resultSet.getInt("city_id"),
                resultSet.getString("city_name")));

        return employee;
    }

    @Override
    public void create(Employee employee) {

        // Формируем запрос к базе с помощью PreparedStatement
        try (PreparedStatement statement = connection.prepareStatement(
                CREATE)) {

            // Подставляем значение вместо wildcard
            // первым параметром указываем порядковый номер wildcard
            // вторым параметром передаем значение
            statement.setString(1, employee.getFirstName());
            statement.setString(2, employee.getLastName());
            statement.setString(3, employee.getGender());
            statement.setInt(4, employee.getAge());
            statement.setInt(5, employee.getCity().getCityId());

            // С помощью метода executeQuery отправляем запрос к базе
            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Employee readById(int id) {
        Employee employee = new Employee();
        // Формируем запрос к базе с помощью PreparedStatement
        try (PreparedStatement statement = connection.prepareStatement(
                READBYID)) {

            // Подставляем значение вместо wildcard
            statement.setInt(1, id);

            // Делаем запрос к базе и результат кладем в ResultSet
            ResultSet resultSet = statement.executeQuery();

            // Методом next проверяем есть ли следующий элемент в resultSet
            // и одновременно переходим к нему, если таковой есть

            while (resultSet.next()) {
                employee = readEmployee(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employee;
    }

    @Override
    public List<Employee> readAll() {

        // Создаем список, в который будем укладывать объекты
        List<Employee> employeeList = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(
                READALL)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Employee employee = readEmployee(resultSet);
                employeeList.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeeList;
    }

    @Override
    public void update(int id, Employee employee) {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE)) {

            statement.setInt(6, id);
            statement.setString(1, employee.getFirstName());
            statement.setString(2, employee.getLastName());
            statement.setString(3, employee.getGender());
            statement.setInt(4, employee.getAge());
            statement.setInt(5, employee.getCity().getCityId());
            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void delete(int id) {
        try (PreparedStatement statement = connection.prepareStatement(
                DELETE)) {

            statement.setInt(1, id);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
