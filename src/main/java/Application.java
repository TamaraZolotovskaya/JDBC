
import java.sql.*;
import java.util.List;

public class Application {

    public static void main(String[] args) throws SQLException {

        // Создаем переменные с данными для подключения к базе
        final String user = "postgres";
        final String password = "mypass";
        final String url = "jdbc:postgresql://localhost:5432/skypro";

        // Создаем соединение с базой с помощью Connection
        // Формируем запрос к базе с помощью PreparedStatement
        try (final Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement
                     ("SELECT * FROM employee WHERE id = (?)")) {

            // Подставляем значение вместо wildcard
            statement.setInt(1, 4);

            // Делаем запрос к базе и результат кладем в ResultSet
            final ResultSet resultSet = statement.executeQuery();

            // Методом next проверяем есть ли следующий элемент в resultSet
            // и одновременно переходим к нему, если таковой есть
            while (resultSet.next()) {

                // С помощью методов getInt и getString получаем данные из resultSet
                String employeeName = "Name: " + resultSet.getString("first_name");
                String employeeLastName = "Last name: " + resultSet.getString("last_name");
                String employeeGender = "Gender: " + resultSet.getString(4);
                int employeeAge = resultSet.getInt(5);
                int employeeCityId = resultSet.getInt("city_id");

                // Выводим данные в консоль
                System.out.println(employeeName);
                System.out.println(employeeLastName);
                System.out.println(employeeGender);
                System.out.println("Age: " + employeeAge);
                System.out.println("City Id: " + employeeCityId);
            }
        }

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            EmployeeDAO employeeDAO = new EmployeeDAOImpl(connection);

            City city1 = new City(2, "Moscow");
            Employee employeeNew = new Employee("John", "Doe", "male", 57, city1);


            employeeDAO.create(employeeNew);

            Employee employee2 = employeeDAO.readById(4);
            System.out.println(employee2);

            employeeDAO.update(3, employeeNew);
            employeeDAO.delete(16);

            List<Employee> eList = employeeDAO.readAll();
            eList.forEach(System.out::println);

        }

    }
}


