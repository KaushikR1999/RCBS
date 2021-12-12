/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AtmCard;
import entity.Customer;
import entity.DepositAccount;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.AtmCardNotFoundException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author kaushikr
 */
@Stateless
public class AtmCardSessionBean implements AtmCardSessionBeanRemote, AtmCardSessionBeanLocal {

    @PersistenceContext(unitName = "RetailCoreBankingSystem-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @Override
    public AtmCard createNewAtmCard(AtmCard newAtmCard, String depositAccountNumber) {
        
//        DepositAccount depositAccount = em.find(DepositAccount.class, depositAccountNumber);
        
        Query query = em.createQuery("SELECT d FROM DepositAccount d WHERE d.accountNumber = :inAccountNumber");
        query.setParameter("inAccountNumber", depositAccountNumber);
        DepositAccount depositAccount = (DepositAccount) query.getSingleResult();
        
        Customer customer = depositAccount.getCustomer();
        
        customer.setAtmCard(newAtmCard);
        newAtmCard.setCustomer(customer);
        
        depositAccount.setAtmCard(newAtmCard);
        newAtmCard.getDepositAccounts().add(depositAccount);

        em.persist(newAtmCard);
        em.flush();

        return newAtmCard;
    }
    
    @Override
    public AtmCard issueReplacementAtmCard (String cardNumber) throws AtmCardNotFoundException {
        
        Query query = em.createQuery("SELECT a FROM AtmCard a WHERE a.cardNumber = :inCardNumber");
        query.setParameter("inCardNumber", cardNumber);
        
        try {
            AtmCard atmCard = (AtmCard) query.getSingleResult();
        
            String newCardNumber = generateCardNumber();
            
            atmCard.setCardNumber(newCardNumber);

            return atmCard;
            
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new AtmCardNotFoundException ("ATM card " + cardNumber + " does not exist!");
        }
    }
    
    @Override
    public AtmCard insertAtmCard (String cardNumber, String pin) throws InvalidLoginCredentialException {
        try {
            AtmCard atmCard = retrieveAtmCardByCardNumber (cardNumber);
            if(atmCard.getPin().equals(pin))
            {                
                return atmCard;
            }
            else
            {
                throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
            }
        } catch (AtmCardNotFoundException ex) {
            throw new InvalidLoginCredentialException("Card Number does not exist or invalid pin!");
        }
    }
    
    public AtmCard retrieveAtmCardByCardNumber (String cardNumber) throws AtmCardNotFoundException {
        Query query = em.createQuery("SELECT a FROM AtmCard a WHERE a.cardNumber = :inCardNumber");
        query.setParameter("inCardNumber", cardNumber);
        
        try {
            AtmCard atmCard = (AtmCard) query.getSingleResult();
            
            return atmCard;
            
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new AtmCardNotFoundException ("ATM card " + cardNumber + " does not exist!");
        }
    }
    
    @Override
    public void changePIN (Long atmCardId, String pin) {
        AtmCard atmCard = em.find(AtmCard.class, atmCardId);
        atmCard.setPin(pin);
    }
    
    @Override
    public List<DepositAccount> enquireAvailableBalance (Long atmCardId) {
        AtmCard atmCard = em.find(AtmCard.class, atmCardId);
        System.out.println(atmCard.getAtmCardId());
        atmCard.getDepositAccounts().size();
        return atmCard.getDepositAccounts();
    }
    
    @Override
    public String generateCardNumber() {
        Random random = new Random();
        
        String accountNumber = "";
        
        int upperbound = 9;
        //generate random values from 0-9
        for (int i = 0; i < 16; i++) {
            int int_random = random.nextInt(upperbound);
            accountNumber += String.valueOf(int_random);
        }
        return accountNumber;
    }
}
