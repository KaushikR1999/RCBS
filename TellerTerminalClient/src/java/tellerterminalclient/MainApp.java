/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tellerterminalclient;

import ejb.session.stateless.AtmCardSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.DepositAccountSessionBeanRemote;
import ejb.session.stateless.DepositAccountTransactionSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import entity.AtmCard;
import entity.Customer;
import entity.DepositAccount;
import entity.Employee;
import java.math.BigDecimal;
import java.util.Scanner;
import util.enumeration.DepositAccountType;
import util.exception.AtmCardNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.DepositAccountNotFoundException;
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

    private Employee currentEmployee;

    public MainApp() {
    }

    public MainApp(AtmCardSessionBeanRemote atmCardSessionBeanRemote, CustomerSessionBeanRemote customerSessionBeanRemote, DepositAccountSessionBeanRemote depositAccountSessionBeanRemote, DepositAccountTransactionSessionBeanRemote depositAccountTransactionSessionBeanRemote, EmployeeSessionBeanRemote employeeSessionBeanRemote) {
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
            System.out.println("*** Welcome to Teller Terminal Client ***\n");
            System.out.println("1: Login");
            System.out.println("2: Exit\n");
            response = 0;

            while (response < 1 || response > 2) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    try {
                        doLogin();
                        System.out.println("Login successful!\n");

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

    private void doLogin() throws InvalidLoginCredentialException {

        Scanner scanner = new Scanner(System.in);
        String username = "";
        String password = "";

        System.out.println("*** Teller Terminal :: Login ***\n");
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();

        if (username.length() > 0 && password.length() > 0) {
            currentEmployee = employeeSessionBeanRemote.employeeLogin(username, password);
        } else {
            throw new InvalidLoginCredentialException("Missing login credential!");
        }
    }

    private void menuMain() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Teller Terminal Client ***\n");
            System.out.println("You are login as " + currentEmployee.getUsername() + " with " + currentEmployee.getAccessRight().toString() + " rights\n");
            System.out.println("1: Create Customer");
            System.out.println("2: Open Deposit Account");
            System.out.println("3: Issue ATM Card");
            System.out.println("4: Issue Replacement Card");
            System.out.println("5: Logout\n");
            response = 0;

            while (response < 1 || response > 5) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    doCreateCustomer();

                } else if (response == 2) {
                    doOpenDepositAccount();

                } else if (response == 3) {
                    doIssueATMCard();

                } else if (response == 4) {
                    doIssueReplacementATMCard();

                } else if (response == 5) {
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

    private void doCreateCustomer() {
        Scanner scanner = new Scanner(System.in);
        Customer newCustomer = new Customer();

        System.out.println("*** Teller Terminal Client :: Create New Customer ***\n");
        System.out.print("Enter First Name> ");
        newCustomer.setFirstName(scanner.nextLine().trim());

        System.out.print("Enter Last Name> ");
        newCustomer.setLastName(scanner.nextLine().trim());

        System.out.print("Enter Identification Number> ");
        newCustomer.setIdentificationNumber(scanner.nextLine().trim());

        System.out.print("Enter Contact Number> ");
        newCustomer.setContactNumber(scanner.nextLine().trim());

        System.out.print("Enter Address Line 1> ");
        newCustomer.setAddressLine1(scanner.nextLine().trim());

        System.out.print("Enter Address Line 2> ");
        newCustomer.setAddressLine2(scanner.nextLine().trim());

        System.out.print("Enter Postal Code> ");
        newCustomer.setPostalCode(scanner.nextLine().trim());

        newCustomer = customerSessionBeanRemote.createNewCustomer(newCustomer);
        System.out.println("New Customer created successfully!: " + newCustomer.getCustomerId() + "\n");
    }

    private void doOpenDepositAccount() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** Teller Terminal Client :: Open Deposit Account ***\n");
        DepositAccount newDepositAccount = new DepositAccount();

        System.out.println("Enter Account Type> ");
        System.out.println("1: Savings");
        System.out.println("2: Current");
        int response = scanner.nextInt();

        while (response < 1 || response > 2) {
            response = scanner.nextInt();
        }
        
        if (response == 1) {
            newDepositAccount.setAccountType(DepositAccountType.SAVINGS);
        } else if (response == 2) {
            newDepositAccount.setAccountType(DepositAccountType.CURRENT);
        } else {
            System.out.println("Invalid option, please try again!\n");
        }

        System.out.print("Enter Available Balance> ");
        newDepositAccount.setAvailableBalance(scanner.nextBigDecimal());
        scanner.nextLine();
        
        System.out.print("Enter Customer Identification Number> ");
        String customerId = scanner.nextLine().trim();
//        scanner.nextLine();

        try {
            Customer customer = customerSessionBeanRemote.retrieveCustomerByID(customerId);
            
            newDepositAccount.setAccountNumber(depositAccountSessionBeanRemote.generateAccountNumber());
            newDepositAccount.setHoldBalance(new BigDecimal(0));
            newDepositAccount.setLedgerBalance(new BigDecimal(0));
            newDepositAccount.setEnabled(true);
            
            newDepositAccount = depositAccountSessionBeanRemote.createNewDepositAccount(newDepositAccount, customer.getCustomerId());

            System.out.println("New Deposit Account opened successfully!: " + newDepositAccount.getAccountNumber() + "\n");
        } catch (CustomerNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

    }
    
    private void doIssueATMCard() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** Teller Terminal Client :: Issue ATM Card ***\n");
        AtmCard newAtmCard = new AtmCard();
        
        System.out.print("Enter Name on Card> ");
        String nameOnCard = scanner.nextLine().trim();
        newAtmCard.setNameOnCard(nameOnCard);
        
        System.out.print("Enter Deposit Account Number> ");
        String accountNumber = scanner.nextLine().trim();
        
        newAtmCard.setCardNumber(atmCardSessionBeanRemote.generateCardNumber());
        newAtmCard.setEnabled(true);
        newAtmCard.setPin("123456");
        
        try {
            DepositAccount depositAccount = depositAccountSessionBeanRemote.retrieveDepositAccountByAccountNumber(accountNumber);
            newAtmCard = atmCardSessionBeanRemote.createNewAtmCard(newAtmCard, accountNumber);
            System.out.println("New Atm Card issued successfully!: " + newAtmCard.getCardNumber() + "\n");
        } catch (DepositAccountNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        
    }
    
    private void doIssueReplacementATMCard() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** Teller Terminal Client :: Issue Replacement ATM Card ***\n");
        AtmCard newAtmCard = new AtmCard();
        
        System.out.print("Enter AtmCard Number > ");
        String cardNumber = scanner.nextLine().trim();
        
        try {
            newAtmCard = atmCardSessionBeanRemote.issueReplacementAtmCard(cardNumber);
            System.out.println("Replacement Atm Card issued successfully!: " + newAtmCard.getCardNumber() + "\n");        
        } catch (AtmCardNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
