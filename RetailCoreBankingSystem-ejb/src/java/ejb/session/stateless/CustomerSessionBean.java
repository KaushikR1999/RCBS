/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CustomerNotFoundException;

/**
 *
 * @author kaushikr
 */
@Stateless
public class CustomerSessionBean implements CustomerSessionBeanRemote, CustomerSessionBeanLocal {

    @PersistenceContext(unitName = "RetailCoreBankingSystem-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @Override
    public Customer createNewCustomer(Customer newCustomer) {

        em.persist(newCustomer);
        em.flush();

        return newCustomer;
    }
    
    @Override
    public Customer retrieveCustomerByID(String identificationNumber) throws CustomerNotFoundException {
        Query query = em.createQuery("SELECT c FROM Customer c WHERE c.identificationNumber = :inIdentificationNumber");
        query.setParameter("inIdentificationNumber", identificationNumber);

        try {
            return (Customer) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new CustomerNotFoundException("Customer ID " + identificationNumber + " does not exist!");
        }
    }
}
