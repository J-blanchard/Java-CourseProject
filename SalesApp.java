import java.util.*;
import java.sql.*;
import java.util.HashMap;
import java.util.logging.Logger;
import java.io.IOException;
import java.util.logging.Level;

public class SalesApp {
	
	private final static Logger LOGGER = Logger.getLogger(MyLogger.class.getName());
	public static HashMap<String, Object> map = new HashMap<String, Object>();
	public static Product prod = new Product();
	//test
	public static void main(String[] args) throws SecurityException, IOException, SQLException{
			
		// System.out.println( "Results: " + getProductName("A1")); 
		
		// LOGGER.info("Logger Name: "+LOGGER.getName());
		
	        try{
	        	System.out.println("Welcome to the sales application!\n");
	    		//calls the navigation method
				getProductPrice("A2");
	    		mainMenu();
	    		// LOGGER.info("info msg");
	            
	        }catch(Exception ex){
	            LOGGER.log(Level.SEVERE, "Exception occurred", ex);
	        }
	 
		}
	
	//-----------------------------------------------------------MAIN MENU----------------------------------------------------------------------//
	public static void mainMenu() throws SQLException {
		
		System.out.println("Please select what you would like to do:\n");
		System.out.println("[1: Purchase an item.]");
		System.out.println("[2: Add a new item.]");
		System.out.println("[3: Remove an item.]");
		System.out.println("[4: Exit application.]");
		Scanner input = new Scanner(System.in);
		int selection = input.nextInt();
		if (selection == 1) {
			purchaseItem();
		}
		if(selection == 2) {
			addItem();
		}
		if (selection == 3) {
			removeItem();
		}
		if (selection == 4) {
			System.out.println("Thank you, enjoy your day!");
		}
		else {
			System.out.println("Selection was invalid please choose from the following options:");
			mainMenu();
		}//test
	}



	// ---------------------------------------------------------------PURCHASE ITEM-----------------------------------------------------------//
	public static void purchaseItem() throws SQLException {
		String selectedItem;
		String confirm;
		double payment;
		System.out.println("\nSNACKS FOR PURCHASE");
		System.out.println("___________________");
		System.out.println("|[A1: " + getProductName("A1") +  "] | [A2: " + getProductName("A2") + "] | [A3: " + getProductName("A3") + "] | [A4: " + getProductName("A4") +"]|");
		System.out.println("|[A1: " + getProductName("A1") +  "] | [A2: " + getProductName("A2") + "] | [A3: " + getProductName("A3") + "] | [A4: " + getProductName("A4") +"]|");
		System.out.println("|[A1: " + getProductName("A1") +  "] | [A2: " + getProductName("A2") + "] | [A3: " + getProductName("A3") + "] | [A4: " + getProductName("A4") +"]|");
		System.out.println("|[A1: " + getProductName("A1") +  "] | [A2: " + getProductName("A2") + "] | [A3: " + getProductName("A3") + "] | [A4: " + getProductName("A4") +"]|");
		System.out.println("Please select an item to purchase: " + " (Ex. Enter A1 to select " + getProductName("A1") +")");
		Scanner input = new Scanner(System.in);
		selectedItem = input.nextLine().toUpperCase();
		if(selectedItem.length() != 2) {
			System.out.println("Your selection was invalid. Selection must be the column letter followed by the row number (A1).");
			purchaseItem();
		}
		else {
		
			//check to see if the input value is correct
			System.out.println("You have selected item " + selectedItem + ", " + getProductName(selectedItem) + ". Is this selection correct? (Enter Y/N)");
			confirm = input.nextLine().toUpperCase();
			System.out.println(confirm);
			
			
			//needed to make input a char in order for the conditional statement below to work as intended.
			char[] confirmed = confirm.toCharArray();
			if( confirmed[0] == 'Y') {
				System.out.println("The price of " + getProductName(selectedItem) + " is: $" + getProductPrice(selectedItem) + ". Please enter payment at this time");
				System.out.println("Total Amount Due: $" + getProductPrice(selectedItem)+ "...");
				payment = input.nextDouble();
				System.out.println("Amount Received: $" + payment);
				double change = payment - getProductPrice(selectedItem);
				System.out.println("Thank You! Here is your change: $" + change);
				mainMenu();
			}
			else {
				System.out.println("Please re-enter your selection");
				purchaseItem();	
			}
		}
	}

	//-------------------------------------------------------------------ADDS NEW Product---------------------------------------------------//
	public static void addItem() throws SQLException {
		
		System.out.println("Please enter the desired Item Id: ");
		Scanner input = new Scanner(System.in);
		String Id = input.nextLine().toUpperCase();
		//ADD INPUT VALIDATION. Check to see if ID Exists Already
		System.out.println("Please enter the name of the item: ");
		String ItemName = input.nextLine();
		System.out.println("Please enter the price of the item: ");
			Double price = input.nextDouble();
		System.out.println("Please enter the quantity of the item in stock: ");
			int quantity = input.nextInt();

		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/courseproject", "root", "root");
		Statement stmt = connection.createStatement();
		String query = "INSERT INTO Product VALUES (\"" + Id + "\",\"" + ItemName + "\"," + price + "," + quantity + ");";
		stmt.executeUpdate(query);	
		
		System.out.println("You have successfully added the following item: " + getProductName(Id));
		
		mainMenu();
		
		
	}

	//--------------------------------------------------------------------REMOVES ITEM---------------------------------------------------//
	public static void removeItem() throws SQLException {
		
		System.out.println("Enter the Id of the product you would like to delete: ");
		Scanner input = new Scanner(System.in);
		String itemId = input.nextLine();
		//ADD INPUT VALIDATION. Check to see if ID Exists
		String Name = getProductName(itemId);
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/courseproject", "root", "root");
		Statement stmt = connection.createStatement();
		String query = "DELETE FROM Product WHERE ProductId =" + "\"" + itemId + "\";";
		stmt.executeUpdate(query);

		System.out.println("You have successfully removed the following item:" + Name);
		mainMenu();
	}

	//TAKES IN id and returns productName
	public static String getProductName(String Selectedid) throws SQLException{
		
		//Check to see if ID Exists first
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/courseproject", "root", "root");
		Statement stmt = connection.createStatement();
		String query = "Select * from product where productId =" + "\"" + Selectedid +"\";";
		ResultSet rs = stmt.executeQuery(query);

			rs.next();
			String itemName = rs.getString(2);
		connection.close();

		return itemName;
	}
	public static Double getProductPrice(String selectedID) throws SQLException{
			
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/courseproject", "root", "root");
		Statement stmt = connection.createStatement();
		String query = "Select * from product where productId =" + "\"" + selectedID +"\";";
		ResultSet rs = stmt.executeQuery(query);

				rs.next();
				Double price = rs.getDouble(3);
			
		connection.close();

		return price;
	}
	

}

