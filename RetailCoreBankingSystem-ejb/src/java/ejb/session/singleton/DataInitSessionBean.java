/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.CustomerSessionBeanLocal;
import ejb.session.stateless.EmployeeSessionBeanLocal;
import entity.Customer;
import entity.Employee;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.EmployeeAccessRightEnum;

/**
 *
 * @author kaushikr
 */
@Singleton
@LocalBean
@Startup
public class DataInitSessionBean {
    
    @EJB
    private CustomerSessionBeanLocal customerSessionBeanLocal;

    @EJB
    private EmployeeSessionBeanLocal employeeSessionBeanLocal;

    @PersistenceContext(unitName = "RetailCoreBankingSystem-ejbPU")
    private EntityManager em;

    @PostConstruct
    public void postConstruct() {
        if(em.find(Employee.class, 1l) == null){
            Employee newEmployee = new Employee ();
            newEmployee.setFirstName("Kaushik");
            newEmployee.setLastName("Rangaraj");
            newEmployee.setAccessRight(EmployeeAccessRightEnum.TELLER);
            newEmployee.setUsername("KR");
            newEmployee.setPassword("password");
            employeeSessionBeanLocal.createNewEmployee(newEmployee);
        }
        
        if(em.find(Customer.class, 1l) == null){
            Customer newCustomer = new Customer ();
            newCustomer.setFirstName("Kaushik");
            newCustomer.setLastName("Rangaraj");
            newCustomer.setIdentificationNumber("12345678");
            newCustomer.setContactNumber("12345678");
            newCustomer.setAddressLine1("Computing 1");
            newCustomer.setAddressLine2("Computing Drive");
            newCustomer.setPostalCode("344578");
            customerSessionBeanLocal.createNewCustomer(newCustomer);
        }
    }
    
   
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
