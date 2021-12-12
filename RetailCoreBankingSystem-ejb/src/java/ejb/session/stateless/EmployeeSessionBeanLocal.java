/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import java.util.List;
import javax.ejb.Local;
import util.exception.EmployeeNotFoundException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author kaushikr
 */
@Local
public interface EmployeeSessionBeanLocal {

    public Employee retrieveEmployeeByUsername(String username) throws EmployeeNotFoundException;

    public Employee employeeLogin(String username, String password) throws InvalidLoginCredentialException;

    public Long createNewEmployee(Employee newEmployee);

    public List<Employee> retrieveAllEmployees();
    
}
