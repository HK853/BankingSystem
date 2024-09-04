package BankingManagementSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class BankingApp {

    private static final String url ="jdbc:mysql://localhost:3306/banking_system";
    private static final String username ="root";
    private static final String password ="root@123";

    public static void main(String[] args) throws ClassNotFoundException,SQLException {

        // driver loaded
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        // connection establish
        try{
            Connection con = DriverManager.getConnection(url,username,password);
            Scanner sc = new Scanner(System.in);
            User user = new User(con,sc);
            Accounts accounts = new Accounts(con,sc);
            AccountManager accountManager = new AccountManager(con,sc);

            String email;
            long account_number;

            while(true){
                System.out.println();
                System.out.println("****** WELCOME TO BANKING SYSTEM ******");
                System.out.println();
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.println("Enter your choice: ");
                int choice = sc.nextInt();

                switch (choice){
                    case 1:
                        user.register();
                        break;
                    case 2:
                        email = user.login();
                        if(email!=null){
                            System.out.println();
                            System.out.println("User login Successfully!!!");
                            if(!accounts.account_exist(email)){
                                System.out.println();
                                System.out.println("1. Open a new Bank Account");
                                System.out.println("2. Exit");
                                if(sc.nextInt() == 1){
                                    account_number = accounts.open_account(email);
                                    System.out.println("Account created successfully");
                                    System.out.println("Your account number is: "+account_number);
                                }else{
                                    break;
                                }
                            }
                            account_number = accounts.getAccount_number(email);
                            int choice2=0;

                            while(choice2!=5){
                                System.out.println();
                                System.out.println("1. Debit Money");
                                System.out.println("2. Credit Money");
                                System.out.println("3. Transfer Money");
                                System.out.println("4. Check Balance");
                                System.out.println("5. Logout");

                                choice2 = sc.nextInt();

                                switch (choice2){
                                    case 1:
                                        accountManager.debit_money(account_number);
                                        break;
                                    case 2:
                                        accountManager.credit_money(account_number);
                                        break;
                                    case 3:
                                        accountManager.transfer_money(account_number);
                                        break;
                                    case 4:
                                        accountManager.get_balance(account_number);
                                    case 5:
                                        break;
                                    default:
                                        System.out.println("Enter Valid choice");
                                        break;
                                }
                            }
                        }else {
                            System.out.println("Incorrect email or password...");
                        }
                    case 3:
                        System.out.println("THANK YOU FOR USING BANKING SYSTEM!!!!");
                        System.out.println("Existing System");
                        return;
                    default:
                        System.out.println("Enter Valid choice");
                        break;
                }
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }
}
