package BankingManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class Accounts {

    private Connection con;
    private Scanner sc;

    public  Accounts(Connection con , Scanner sc){
        this.con=con;
        this.sc=sc;
    }

    public long open_account(String email){

        if(!account_exist(email)){
            String query = "INSERT INTO accounts (account_number,full_name,email,balance,security_pin) VALUES (?,?,?,?,?)";
            sc.nextLine();
            System.out.println("Enter Full name: ");
            String full_name = sc.nextLine();
            System.out.println("Enter Initial Amount: ");
            double balance = sc.nextDouble();
            sc.nextLine();
            System.out.println("Enter Security pin: ");
            String pin = sc.nextLine();

            try{
                long account_number = generateAccountNumber();
                PreparedStatement preparedStatement = con.prepareStatement(query);
                preparedStatement.setLong(1,account_number);
                preparedStatement.setString(2,full_name);
                preparedStatement.setString(3,email);
                preparedStatement.setDouble(4,balance);
                preparedStatement.setString(5,pin);
                int change = preparedStatement.executeUpdate();

                if(change > 0){
                    return account_number;
                }else {
                    throw new RuntimeException("Account creation failed");
                }

            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        throw  new RuntimeException("Account Already Exist");
    }

    public long getAccount_number(String email){
        String query = "SELECT account_number FROM accounts WHERE email = ?";

        try{
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1,email);
            ResultSet rs = preparedStatement.executeQuery();

            if(rs.next()){
                return rs.getLong("account_number");
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        throw new RuntimeException("Account number does not exist");
    }

    public long generateAccountNumber(){
        String query = "SELECT account_number FROM accounts ORDER BY account_number DESC LIMIT 1";
        try{
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query);
            if(rs.next()){
                long old_acc_number = rs.getLong("account_number");
                return old_acc_number+1;
            }else {
                return 10000100;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return 10000100;
    }

    public boolean account_exist(String email){
        String query = "SELECT account_number FROM accounts WHERE email = ?";
        try{
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1,email);
            ResultSet rs = preparedStatement.executeQuery();

            if(rs.next()){
                return true;
            }else {
                return false;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
