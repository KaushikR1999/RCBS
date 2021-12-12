/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AtmCard;
import entity.DepositAccount;
import java.util.List;
import javax.ejb.Local;
import util.exception.AtmCardNotFoundException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author kaushikr
 */
@Local
public interface AtmCardSessionBeanLocal {

    public AtmCard createNewAtmCard(AtmCard newAtmCard, String depositAccountNumber);

    public String generateCardNumber();

    public AtmCard issueReplacementAtmCard(String cardNumber) throws AtmCardNotFoundException;

    public AtmCard insertAtmCard(String cardNumber, String pin) throws InvalidLoginCredentialException;

    public void changePIN(Long atmCardId, String pin);

    public List<DepositAccount> enquireAvailableBalance(Long atmCardId);
    
}
