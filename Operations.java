package org.example;

import java.sql.*;
import java.util.Scanner;

public class Operations {
    static Scanner sc=new Scanner(System.in);
    public static void main(String[] args) {
        Connection conn;
        try {
            conn= DriverManager.getConnection("jdbc:postgresql://localhost:5432/demo","postgres","Password");
            System.out.println("connection successfully");

        } catch (SQLException e) {
            System.out.println("connection failed");
            //e.printStackTrace();
        }
    }
    public static void deposit(Connection conn){
        System.out.println("enter account number:");
        int ac=sc.nextInt();
        System.out.println("enter amount to deposit");
        int amount=sc.nextInt();
        if(amount>0) {
            try {
                String que = "update bank set balance=balance+? where acno=?";
                PreparedStatement ps = conn.prepareStatement(que);
                ps.setInt(1, amount);
                ps.setInt(2, ac);
                int row=ps.executeUpdate();
                if(row>0) System.out.println("Amount is Deposited successfully");
                else System.out.println("Deposite not successful");
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }else System.out.println("Deposite not successful");

    }
    public static void withdraw(Connection conn){
        int bal=0;
        System.out.println("enter account number:");
        int ac=sc.nextInt();
        System.out.println("enter amount to withdraw");
        int amount=sc.nextInt();
        if(amount<=0) {
            System.out.println("Invalid amount");
            throw new RuntimeException();
        }
        try {
            String q="select *from bank where acno=?";
            String que="update bank set balance=balance-? where acno=?";
            PreparedStatement p=conn.prepareStatement(q);
            PreparedStatement ps=conn.prepareStatement(que);
            p.setInt(1,ac);
            ResultSet rs=p.executeQuery();
            if(rs.next())
                bal=rs.getInt("balance");
            if(bal>amount) {
                ps.setInt(1, amount);
                ps.setInt(2, ac);
                ps.executeUpdate();
                System.out.println("Amount is withdraw successfully");
                conn.close();
            }
            else System.out.println("With draw not possible");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static void balanceEnquiry(Connection conn){
        System.out.println("enter account number:");
        int ac=sc.nextInt();
        try {
            String que="select * from bank where acno=?";
            PreparedStatement ps=conn.prepareStatement(que);
            ps.setInt(1,ac);
            ResultSet rs=ps.executeQuery();
            while(rs.next()) {
                int a = rs.getInt(1);
                String s = rs.getString(2);
                int t = rs.getInt(3);
                System.out.println("Account no:" + a + "\n Account Holdername:" + s + "\n Balance:" + t);
                conn.close();
            }
            if(!rs.next()) System.out.println("no details found");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
