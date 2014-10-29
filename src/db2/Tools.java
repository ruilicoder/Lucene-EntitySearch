package db2;


//
//   Source File Name: Tools.java  1.3 
//  
//   Licensed Materials -- Property of IBM 
//  
//   (c) Copyright International Business Machines Corporation, 1999. 
//       All Rights Reserved. 
//  
//   US Government Users Restricted Rights - 
//   Use, duplication or disclosure restricted by 
//   GSA ADP Schedule Contract with IBM Corp. 
//  
//   This is a toolkit for DB2 Java program samples. 
//  

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

class Tools {

   static {
     
   }

   //  
   //  DBConnect 
   //    - if provided with database name, user ID, and password,  
   //      connects to database 
   //    - otherwise prompts user for database name, user ID, and 
   //      password and connects to database  
   //    - returns the connection 
   //  



   public static void main(String[] args)  {
	   
	   String JDBCDriver = "COM.ibm.db2.jdbc.app.DB2Driver";
	   String DbSource ="jdbc:DB2:entity";
	 
	  try {

		  Driver driver = new COM.ibm.db2.jdbc.app.DB2Driver();
		  DriverManager.registerDriver(driver);
		  System.out.println("Driver Loaded Successfully ..."); 
	   Connection conn = DriverManager.getConnection(DbSource);
	   Statement stmt = conn.createStatement();
	   ResultSet rs = stmt.executeQuery("SELECT * from entityentitytype");
	   if (rs.next())
	System.out.println(rs.getString(1));
	   else
	       System.out.println("totle:0");
	   
	   rs.close();
	   stmt.close();
	   conn.close();
	  }
	  catch (SQLException se){
	    System.err.println("sql error.");
	    se.printStackTrace();
	  }
	  catch (Exception E) {
	    System.err.println("other exception");
	    E.printStackTrace();
	  }    
	 
	   
	 
	}
   


}


