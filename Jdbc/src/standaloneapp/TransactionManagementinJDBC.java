package standaloneapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Savepoint;
import java.util.Scanner;

public class TransactionManagementinJDBC {

	public static void main(String[] args) {
      String url="jdbc:mysql://localhost:3306/bank1";
      String u="root";
      String p="root";
      Connection road=null;
      PreparedStatement lorry=null;
      ResultSet res=null;
		try {
		Class.forName("com.mysql.cj.jdbc.Driver");
		road=DriverManager.getConnection(url,u,p);
		System.out.println("---Welcome to BANK---");
		String query="select * from account where acc_num=? and pin=?";
	    lorry=road.prepareStatement(query);
		Scanner sc=new Scanner(System.in);
		System.out.println("enter acc_number :");
		int acc_no=sc.nextInt();
		System.out.println("Enter pin :");
		int pin=sc.nextInt();
		lorry.setInt(1, acc_no);	
		lorry.setInt(2, pin);
		res=lorry.executeQuery();
		res.next();
		String name=res.getString(2);
		int balance=res.getInt(4);
		System.out.println("welcome" + name +"!" + "\n" + "your availables balance is"+ " " + balance);
		System.out.println("---TRANFER MODULE---");
		System.out.println("Enter the Beneficiary account numner :");
		int b_no=sc.nextInt();
		System.out.println("Enter Amount to be transfer :");
		int t_no=sc.nextInt();
		//autocommit false
		road.setAutoCommit(false);
		Savepoint s=road.setSavepoint();
		String query1="update account set balance=balance-?" + " where acc_num=?";
		PreparedStatement lorry1=road.prepareStatement(query1);
		lorry1.setInt(1, t_no);
		lorry1.setInt(2,acc_no);
		lorry1.executeUpdate();
		System.out.println("--INCOMING CREDIT REQUEST FROM" +" "+ name
				+" "+ " ACOUNT NUMBER"+" "+ acc_no + " "+ "want to transfer" +" "+ t_no);
	
		System.out.println("enter yes to receive");
		System.out.println("enter no to reject");
		String choice=sc.next();
		if(choice.equals("yes")) {
			String query2="update account set balance=balance+?" +
		         " " + "where acc_num=?";
			PreparedStatement lorry2=road.prepareStatement(query2);
		    lorry2.setInt(1,t_no);
		    lorry2.setInt(2, b_no);
			lorry2.executeUpdate();
		//to display added amount on benificiary acc
			String query3="select * from account where acc_num=?";
			PreparedStatement lorry3=road.prepareStatement(query3);
		    lorry3.setInt(1, b_no);
		    ResultSet res1=lorry3.executeQuery();
		    res1.next();
		    String b=res1.getString(2);
		    int ba=res1.getInt(4);
		    System.out.println("hello"+" "+ b + " " + "your bank balance is" + " "+ ba);
		}
		else {
			road.rollback(s);
			String query4="select * from account where acc_num=?";
			PreparedStatement lorry4=road.prepareStatement(query4);
		    lorry4.setInt(1, b_no);
		    ResultSet res1=lorry4.executeQuery();
		    res1.next();
		    String b=res1.getString(2);
		    int ba=res1.getInt(4);
		    System.out.println("hello"+" "+ b + " " + "your bank balance is" + " "+ ba);
		}
		road.commit();
		sc.close();
		lorry.close();
		road.close();
		
	} 
		catch (Exception e) {
		System.out.println("invalid! enter correct pin / account number");
	}
  }

}
