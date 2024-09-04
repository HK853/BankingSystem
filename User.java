package BankingManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {

    private Connection con;
    private Scanner sc;

    public  User(Connection con , Scanner sc){
        this.con = con;
        this.sc=sc;
    }

    public void register(){
        sc.nextLine();
        System.out.println("Enter Full name: ");
        String full_name = sc.nextLine();
        System.out.println("Enter email: ");
        String email = sc.nextLine();
        System.out.println("Enter password: ");
        String password = sc.nextLine();

        if(user_exist(email)){
            System.out.println("User is already exists for this Email address");
            return;
        }

        String query="INSERT INTO user(full_name,email,password) VALUES (?,?,?)";

        try{
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1,full_name);
            preparedStatement.setString(2,email);
            preparedStatement.setString(3,password);
            int change = preparedStatement.executeUpdate();

            if(change > 0 ){
                System.out.println("Registration Sucessfull!!!");
            }else {
                System.out.println("Registration Unsucessfull!!!");
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public String login(){
        sc.nextLine();
        System.out.println("Enter email: ");
        String email = sc.nextLine();
        System.out.println("Enter password: ");
        String password = sc.nextLine();

        String query = "SELECT email FROM user WHERE email = ? AND password = ?";

        try{
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1,email);
            preparedStatement.setString(2,password);
            ResultSet rs = preparedStatement.executeQuery();

            if(rs.next()){
                return email;
            }else {
                return null;
            }

        }catch (SQLException e){
            e.printStackTrace();
        }

        return null;
    }

    public boolean user_exist(String email){
        String query = "SELECT * FROM user WHERE email = (?)";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, email);
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
