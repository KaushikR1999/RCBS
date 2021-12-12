/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import entity.DepositAccount;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.DepositAccountNotFoundException;

/**
 *
 * @author kaushikr
 */
@Stateless
public class DepositAccountSessionBean implements DepositAccountSessionBeanRemote, DepositAccountSessionBeanLocal {

    @PersistenceContext(unitName = "RetailCoreBankingSystem-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @Override
    public DepositAccount createNewDepositAccount(DepositAccount newDepositAccount, Long customerId) {
        
        Customer customer = em.find(Customer.class, customerId);
        
        newDepositAccount.setCustomer(customer);
        
        customer.getDepositAccounts().add(newDepositAccount);
        
        em.persist(newDepositAccount);
        em.flush();

        return newDepositAccount;
    }
    
    @Override
    public String generateAccountNumber() {
        Random random = new Random();
        
        String accountNumber = "";
        
        int upperbound = 9;
        //generate random values from 0-9
        for (int i = 0; i < 12; i++) {
            int int_random = random.nextInt(upperbound);
            accountNumber += String.valueOf(int_random);
        }
        return accountNumber;
    }

    public DepositAccount retrieveDepositAccountByAccountNumber(String accountNumber) throws DepositAccountNotFoundException {
        Query query = em.createQuery("SELECT d FROM DepositAccount d WHERE d.accountNumber = :inAccountNumber");
        query.setParameter("inAccountNumber", accountNumber);

        try {
            return (DepositAccount) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new DepositAccountNotFoundException("Deposit Account Number " + accountNumber + " does not exist!");
        }
    }
}
