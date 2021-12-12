/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.DepositAccount;
import javax.ejb.Local;
import util.exception.DepositAccountNotFoundException;

/**
 *
 * @author kaushikr
 */
@Local
public interface DepositAccountSessionBeanLocal {

    public String generateAccountNumber();

    public DepositAccount createNewDepositAccount(DepositAccount newDepositAccount, Long customerId);

    public DepositAccount retrieveDepositAccountByAccountNumber(String accountNumber) throws DepositAccountNotFoundException;
    
}
