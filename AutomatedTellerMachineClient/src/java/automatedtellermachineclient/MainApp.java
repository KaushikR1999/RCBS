/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automatedtellermachineclient;

import ejb.session.stateless.AtmCardSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.DepositAccountSessionBeanRemote;
import ejb.session.stateless.DepositAccountTransactionSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import entity.AtmCard;
import entity.Customer;
import entity.DepositAccount;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;
import javax.ejb.EJB;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author kaushikr
 */
public class MainApp {

    private AtmCardSessionBeanRemote atmCardSessionBeanRemote;
    private CustomerSessionBeanRemote customerSessionBeanRemote;
    private DepositAccountSessionBeanRemote depositAccountSessionBeanRemote;
    private DepositAccountTransactionSessionBeanRemote depositAccountTransactionSessionBeanRemote;
    private EmployeeSessionBeanRemote employeeSessionBeanRemote;

    private AtmCard atmCard;

    public MainApp() {
        atmCard = null;
    }

    public MainApp(AtmCardSessionBeanRemote atmCardSessionBeanRemote, CustomerSessionBeanRemote customerSessionBeanRemote, DepositAccountSessionBeanRemote depositAccountSessionBeanRemote, DepositAccountTransactionSessionBeanRemote depositAccountTransactionSessionBeanRemote, EmployeeSessionBeanRemote employeeSessionBeanRemote) {
        this();
        this.atmCardSessionBeanRemote = atmCardSessionBeanRemote;
        this.customerSessionBeanRemote = customerSessionBeanRemote;
        this.depositAccountSessionBeanRemote = depositAccountSessionBeanRemote;
        this.depositAccountTransactionSessionBeanRemote = depositAccountTransactionSessionBeanRemote;
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
    }

    public void runApp() {

        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Welcome to Automated Teller Machine Client ***\n");
            System.out.println("1: Insert ATM Card");
            System.out.println("2: Exit\n");
            response = 0;

            while (response < 1 || response > 2) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    try {
                        doInsertAtmCard();
                        System.out.println("Atm Card inserted successfully!\n");

                        menuMain();
                    } catch (InvalidLoginCredentialException ex) {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
                } else if (response == 2) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 2) {
                break;
            }
        }
    }

    private void doInsertAtmCard() throws InvalidLoginCredentialException {

        Scanner scanner = new Scanner(System.in);

        System.out.println("*** Teller Terminal :: Insert Atm Card ***\n");
        System.out.print("Enter Atm Card Number> ");
        String cardNumber = scanner.nextLine().trim();
        System.out.print("Enter PIN> ");
        String pin = scanner.nextLine().trim();

        if (cardNumber.length() > 0 && pin.length() > 0) {
            atmCard = atmCardSessionBeanRemote.insertAtmCard(cardNumber, pin);
        } else {
            throw new InvalidLoginCredentialException("Missing login credential!");
        }
    }

    private void menuMain() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Automated Teller Terminal Client ***\n");
            System.out.println("You are login as " + atmCard.getNameOnCard() + "\n");
            System.out.println("1: Change PIN");
            System.out.println("2: Enquire Available Balance");
            System.out.println("3: Logout\n");
            response = 0;

            while (response < 1 || response > 3) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    doChangePIN();

                } else if (response == 2) {
                    doEnquireAvailableBalance();

                } else if (response == 3) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 5) {
                break;
            }
        }
    }

    public void doChangePIN() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** Teller Terminal :: Change PIN ***\n");

        System.out.print("Enter PIN> ");
        String pin = scanner.nextLine().trim();

        atmCardSessionBeanRemote.changePIN(atmCard.getAtmCardId(), pin);
    }

    public void doEnquireAvailableBalance() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** Teller Terminal :: Enquire Available Balance ***\n");

        List<DepositAccount> depositAccounts = atmCardSessionBeanRemote.enquireAvailableBalance(atmCard.getAtmCardId());
        for (DepositAccount depositAccount : depositAccounts) {
            BigDecimal availableBalance = depositAccount.getAvailableBalance();
            System.out.println("Deposit Account Number : " + depositAccount.getAccountNumber());
            System.out.println("Available Balance : " + availableBalance);
            System.out.println("******************************************");
        }
    }
}
