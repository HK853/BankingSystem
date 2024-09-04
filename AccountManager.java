package BankingManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class AccountManager {

    private Connection con;
    private Scanner sc;

    public  AccountManager(Connection con , Scanner sc){
        this.con=con;
        this.sc=sc;
    }

    public void credit_money(long account_number) throws  SQLException{
        sc.nextLine();
        System.out.println("Enter the Amount: ");
        double amount = sc.nextDouble();
        sc.nextLine();
        System.out.println("Enter security pin: ");
        String pin = sc.nextLine();

        try{
            con.setAutoCommit(false);
            if(account_number!=0){
                PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM accounts WHERE account_number = ? AND security_pin = ?");
                preparedStatement.setLong(1,account_number);
                preparedStatement.setString(2,pin);
                ResultSet rs = preparedStatement.executeQuery();

                if(rs.next()){
                    String query = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
                    PreparedStatement preparedStatement1 = con.prepareStatement(query);
                    preparedStatement1.setDouble(1,amount);
                    preparedStatement1.setLong(2,account_number);
                    int change = preparedStatement1.executeUpdate();
                    if(change > 0){
                        System.out.println(amount+" Rs Credited Successfully");
                        con.commit();
                        con.setAutoCommit(true);
                        return;
                    }else{
                        System.out.println("Transaction failed");
                        con.rollback();
                        con.setAutoCommit(true);
                    }
                }else{
                    System.out.println("Invalid Security pin");
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void debit_money(long account_number) throws SQLException{
        sc.nextLine();
        System.out.println("Enter Amount: ");
        double amount = sc.nextDouble();
        sc.nextLine();
        System.out.println("Enter security pin: ");
        String pin = sc.nextLine();

        try{
            con.setAutoCommit(false);
            if(account_number!=0){
                PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM accounts WHERE account_number = ? AND security_pin = ?");
                preparedStatement.setLong(1,account_number);
                preparedStatement.setString(2,pin);
                ResultSet rs = preparedStatement.executeQuery();

                if(rs.next()){
                    double curr_balance = rs.getDouble("balance");
                    if(amount <= curr_balance){
                        String query = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
                        PreparedStatement preparedStatement1 = con.prepareStatement(query);
                        preparedStatement1.setDouble(1,amount);
                        preparedStatement1.setLong(2,account_number);
                        int change = preparedStatement1.executeUpdate();

                        if(change > 0){
                            System.out.println(amount + " Rs Debited Successfully!!!");
                            con.commit();
                            con.setAutoCommit(true);
                            return;
                        }else{
                            System.out.println("Transaction failed");
                            con.rollback();
                            con.setAutoCommit(true);
                        }
                    }else {
                        System.out.println("Insufficient Balance");
                    }
                }else{
                    System.out.println("Invalid pin");
                }
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void transfer_money(long sender_account_number) throws SQLException{
        sc.nextLine();
        System.out.println("Enter Receiver Account number: ");
        long receiver_acc_num  = sc.nextLong();
        System.out.println("Enter Amount: ");
        double amount = sc.nextDouble();
        sc.nextLine();
        System.out.println("Enter Security pin: ");
        String pin = sc.nextLine();

        try{
            con.setAutoCommit(false);

            if(sender_account_number!=0 && receiver_acc_num!=0){
                PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM accounts WHERE account_number = ? AND security_pin = ?");
                preparedStatement.setLong(1,sender_account_number);
                preparedStatement.setString(2,pin);
                ResultSet rs = preparedStatement.executeQuery();

                if(rs.next()){
                    double cur_balance = rs.getDouble("balance");

                    if(amount <= cur_balance){
                        String query1 = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
                        String query2 = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";

                        PreparedStatement preparedStatement1 = con.prepareStatement(query1);
                        PreparedStatement preparedStatement2 = con.prepareStatement(query2);

                        preparedStatement2.setDouble(1,amount);
                        preparedStatement2.setLong(2,receiver_acc_num);

                        preparedStatement1.setDouble(1,amount);
                        preparedStatement1.setLong(2,sender_account_number);

                        int change1 = preparedStatement1.executeUpdate();
                        int change2 = preparedStatement2.executeUpdate();

                        if(change2>0 && change1>0){
                            System.out.println("Transaction Successfully!!!");
                            System.out.println(amount+"Rs Transferred Successfully!!");
                            con.commit();
                            con.setAutoCommit(true);
                            return;
                        }else{
                            System.out.println("Transaction failed");
                            con.rollback();
                            con.setAutoCommit(true);
                        }
                    }else{
                        System.out.println("Insufficient Balance");
                    }

                }else{
                    System.out.println("Invalid security pin");
                }
            }else {
                System.out.println("Invalid account number");
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void get_balance(long account_number) throws  SQLException{
        sc.nextLine();
        System.out.println("Enter security pin: ");
        String pin = sc.nextLine();

        String query = "SELECT balance FROM accounts WHERE account_number = ? AND security_pin = ?";

        try{
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setLong(1,account_number);
            preparedStatement.setString(2,pin);
            ResultSet rs = preparedStatement.executeQuery();

            if(rs.next()){
                double b = rs.getDouble("balance");
                System.out.println("Balance: "+b);
            }else {
                System.out.println("Invalid pin");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

    }
}
