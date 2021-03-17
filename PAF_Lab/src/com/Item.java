package com;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;


public class Item {
	//DB connection
	public Connection connect()
	{
		Connection con = null;

			 try
			 {
				 Class.forName("com.mysql.jdbc.Driver");
				 con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/paf_lab", "root", "@ThaR13741");
				 
				 if(con == null) {
					 System.out.print("Successfully not connected");
				 }
				 else {
					//For testing
					 System.out.print("Successfully connected");
				 }
				 
			 }
			 catch(Exception e)
			 {
				 e.printStackTrace();
			 }

	 	return con;
	}
	
	
	//insert method
	public String insertItem(String code, String name, String price, String desc){
		
		String output = "";
		try {
			
			Connection con = connect();
			
			if (con == null) 
			{ 
			return "Error while connecting to the database"; 
			}
			
			// create a prepared statement
			String query = " insert into items(`itemCode`,`itemName`,`itemPrice`,`itemDesc`) values (?, ?, ?, ?)"; 
			PreparedStatement preparedStmt = con.prepareStatement(query);
			
			
			// binding values 
			preparedStmt.setString(1, code); 
			preparedStmt.setString(2, name); 
			preparedStmt.setDouble(3, Double.parseDouble(price)); 
			preparedStmt.setString(4, desc);

			//execute the statement
			preparedStmt.execute(); 
			
			
			output = "Inserted Successfully";
		}
		catch(Exception e) {

			 output = "Error while inserting"; 
			 System.err.println(e.getMessage());
		}
		
		return output;
		}
	
	
	//read option method
	public String readItems()
	{ 
	 String output = "";
	 
	 try
	 { 
	  Connection con = connect(); 
		 if (con == null) 
		 { 
		  return "Error while connecting to the database for reading."; 
		 }
		 

		// Prepare the html table to be displayed
		output = "<table border='1'>" 
		+ "<tr><th>Item Code</th><th>Item Name</th><th>Item Price</th>"
		+ "<th>Item Description</th><th>Update</th><th>Remove</th></tr>";
		 
		
		//selecting query
		String query = "select * from items";
		Statement stmt = con.createStatement(); 
		ResultSet rs = stmt.executeQuery(query);
		
		// iterate through the rows in the result set
		while (rs.next()) 
		{ 
			 String itemID = Integer.toString(rs.getInt("itemID")); 
			 String itemCode = rs.getString("itemCode"); 
			 String itemName = rs.getString("itemName"); 
			 String itemPrice = Double.toString(rs.getDouble("itemPrice"));
			 String itemDesc = rs.getString("itemDesc");
			
			 
			 // Add a row into the html table
			 output += "<tr><td>" + itemCode + "</td>"; 
			 output += "<td>" + itemName + "</td>"; 
			 output += "<td>" + itemPrice + "</td>"; 
			 output += "<td>" + itemDesc + "</td>"; 
			
			 // buttons
			 output += "<td><form method='post' action='items.jsp'>"
						+"<input name='itemID' type='hidden' value='" + itemID + "'>"
						+ "<input name='action' value='select' type='hidden'>"
						+ "<input name='btnUpdate' type='submit' value='Update'>"
						+ "</form></td>"
						+ "<td><form method='post' action='items.jsp'>"
						+ "<input name='btnRemove' type='submit' value='Remove'>"
						+ "<input name='action' value='delete' type='hidden'>"
						+ "<input name='itemID' type='hidden' value='" + itemID + "'>" 
						+ "</form></td></tr>";
		}
		
		con.close();
		
		//complete html table
		output += "</table>";
		
	 } 
	 catch (Exception e) 
	 { 
		 output = "Error while reading the items."; 
		 System.err.println(e.getMessage()); 
	 }
	 return output;
	 
	}
	
	
	//get one row details to update
	public String readOneItem(int id)
	{
		//test the id
		//System.out.print(id);
		String output = "";
		try
		{
			Connection con = connect();
			if (con == null)
			{
				return "Error while connecting to the database for selected item reading.";
			}
	 
			String query = "select * from items where itemID = ? ";
			PreparedStatement preparedStmt = con.prepareStatement(query);
			preparedStmt.setInt(1, id);
			ResultSet rs = preparedStmt.executeQuery();
			// iterate through the rows in the result set
			while (rs.next())
			{
				String itemID = Integer.toString(rs.getInt("itemID"));
				String itemCode = rs.getString("itemCode");
				String itemName = rs.getString("itemName");
				String itemPrice = Double.toString(rs.getDouble("itemPrice"));
				String itemDesc = rs.getString("itemDesc");
				output += "<form method=post action=items.jsp>"
						+ "		<input name='action' value='update' type='hidden'>"
						+ " 	Item ID: '"+itemID+"' <br>"
						+ " 	Item code: <input name=itemCode type=text value='"+itemCode+ "'><br>"
						+ " 	Item name: <input name=itemName type=text value='"+itemName+ "'><br>"
						+ " 	Item price: <input name=itemPrice type=text value='"+itemPrice+ "'><br>"
						+ " 	Item description: <input name=itemDesc type=text value='"+itemDesc+ "'><br>"
						+ "     <input name='itemID' type='hidden' value='" + itemID + "'>"
						+ " 	<input name=btnSubmit type=submit value=Update >"
						+ "	</form>";
			}
			con.close();
			// Complete the html table
			output += "</table>";
	 
	}
	catch (Exception e)
	{
		output = "Error while reading the one item.";
		System.err.println(e.getMessage());
	}
	return output;
	}
	
	//update method
	public String updateItem(int iID,String iCode,String iName,String iPrice, String iDesc) {
		String output = "";
		
		try
		{
			Connection con = connect();
			if (con == null)
			{
				return "Error while connecting to the database";
			}
			// create a prepared statement      
			String query = "update items set itemCode=?,itemName =?,itemPrice=?,itemDesc=? where itemID = ?";
			PreparedStatement preparedStmt = con.prepareStatement(query);
			
			// binding values
			preparedStmt.setString(1, iCode);
			preparedStmt.setString(2, iName);
			preparedStmt.setDouble(3,Double.parseDouble(iPrice));
			preparedStmt.setString(4, iDesc);
			preparedStmt.setInt(5, iID);
			
			//execute the statement
			preparedStmt.executeUpdate();
			con.close();
			output = "Updated successfully";
		}
		catch (Exception e)
		{
			output = "Error while updating a one item";
			System.err.println(e.getMessage());
		}
		return output;
	}
	
	//delete method
	public String deleteItem(int itemID)
	{ 
	 String output = "";
	 
	 try
	 { 
		 Connection con = connect(); 
		 if (con == null) { 
			 return "Error while connecting to the database for deleting."; 
		 }
		 
		 // create a prepared statement
		 String query = "delete from items where itemID=?"; 
		 PreparedStatement preparedStmt = con.prepareStatement(query); 
		 
		 // binding values
		 preparedStmt.setInt(1, itemID); 
		  
		 // execute the statement
		 preparedStmt.execute(); 
		 con.close();
		 
		 output = "Deleted successfully";
	 } 
	 catch (Exception e) 
	 { 
		 output = "Error while deleting the item."; 
		 System.err.println(e.getMessage()); 
	 }
	 
	 return output; 
	}

}