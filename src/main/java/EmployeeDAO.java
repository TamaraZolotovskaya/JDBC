import java.awt.*;
import java.util.List;


public interface EmployeeDAO {

    void create(Employee employee);

    Employee readById(int id);

    List<Employee> readAll();

    void update(int id, Employee employee);

    void delete(int id);
}
