import java.util.*;
import java.sql.*;
import java.util.HashMap;
import java.util.logging.Logger;
import java.io.IOException;
import java.util.logging.Level;

public class SalesApp {
	
	private final static Logger LOGGER = Logger.getLogger(MyLogger.class.getName());
	public static HashMap<String, Object> map = new HashMap<String, Object>();
	public static ArrayList<Transactions> tranIDList = new ArrayList<Transactions>();
	
	public static void main(String[] args) throws SecurityException, IOException, SQLException{
			
		// System.out.println( "Results: " + getProductName("A1")); 
		
		// LOGGER.info("Logger Name: "+LOGGER.getName());
		
	        try{
	        	System.out.println("Welcome to the sales application!");
	    		//calls the navigation method
	    		mainMenu();
	    		// LOGGER.info("info msg");
	            
	        }catch(Exception ex){
	            LOGGER.log(Level.SEVERE, "Exception occurred", ex);
	        }
	 
		}
	
	//-----------------------------------------------------------MAIN MENU----------------------------------------------------------------------//
	public static void mainMenu() throws SQLException {
		System.out.println("\n|[MAIN MENU]|\n");
		System.out.println("Please select what you would like to do:\n");
		System.out.println("[1: Purchase an item.]");
		System.out.println("[2: Add a new item.]");
		System.out.println("[3: Update an item.]");
		System.out.println("[4: Remove an item.]");
		System.out.println("[5: View transaction report.]");
		System.out.println("[6: Exit application.]");
		Scanner input = new Scanner(System.in);
		int selection = input.nextInt();
		if (selection == 1) {
			purchaseItem();
		}
		if(selection == 2) {
			addItem();
		}
		if (selection == 3) {
			UpdateItem();
		}
		if (selection == 4) {
			removeItem();
		}
		if (selection == 5) {
			showTransactionReport();
		}
		if (selection == 6) {
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
		int quantity;
		double totalAmountDue;
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
		
			//needed to make input a char in order for the conditional statement below to work as intended.
			char[] confirmed = confirm.toCharArray();
			if( confirmed[0] == 'Y') {
				if(getProductName(selectedItem) == "N/A"){
					System.out.println("The product you chose is currently out of stock...Please select a different item.");
					purchaseItem();
				}
				else{
					System.out.println("The price of " + getProductName(selectedItem) + " is: $" + getProductPrice(selectedItem) + ". How many of this item would you like to purchase?");
				quantity = input.nextInt();
				double Verifiedquantity = checkQuantity(quantity, selectedItem);
				totalAmountDue = Verifiedquantity * getProductPrice(selectedItem);
					while (totalAmountDue == 0){

						System.out.println("The total amount of " + getProductName(selectedItem) + " in stock is " + getProductQuantity(selectedItem) + ". Please choose a quantity less than or equal to this value: ");
						quantity = input.nextInt();
						 totalAmountDue = checkQuantity(quantity, selectedItem) * getProductPrice(selectedItem);
					}

				System.out.print("The total amount due is : $" + totalAmountDue + "...");
				System.out.println("Please enter payment at this time.");
				payment = input.nextDouble();
				System.out.println("Amount Received: $" + payment);
				double change = (payment - totalAmountDue);
				System.out.println("Thank You! Here is your change: $" + change);


				CreateTransaction(getProductName(selectedItem), totalAmountDue, quantity);		
				mainMenu();
				}
				
			}
			else {
				System.out.println("Please re-enter your selection");
				purchaseItem();	
			}
		}
	}

	//checks to see if the Quantity of the select item in inventory is greater than then user's selected amount to purchase
	// Then if selected Amount for purchase is less than the quantity in stock, it uodates the databse with the new quantity after pruchase.
	public static double checkQuantity(int quantity, String id) throws SQLException{
		if (quantity <= getProductQuantity(id) && quantity != 0){
		int newQuantity = getProductQuantity(id) - quantity;

		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/courseproject", "root", "root");
		Statement stmt = connection.createStatement();
		String query = "UPDATE Product SET ProductQuantity = \"" + newQuantity + "\" WHERE ProductId = \"" + id + "\" ;";
		stmt.executeUpdate(query);	
		double quant = quantity;
		return quant;
	}
	else{
		return 0;
	}
}

	

	//================================-ADDS NEW Product========================================//
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
	//======================================UPDATES ITEM=========================================================//
	public static void UpdateItem() throws SQLException {

		System.out.println("Please enter the Item Id of the Product you would like to update: ");
		Scanner input = new Scanner(System.in);
		String Id = input.nextLine().toUpperCase();
		//ADD INPUT VALIDATION. Check to see if ID Exists
		System.out.println("What would you like to update?");
		System.out.println("[1: All.]");
		System.out.println("[2: Item ID.]");
		System.out.println("[3: Item Name.]");
		System.out.println("[4: Item Price.]");
		System.out.println("[5: Item Quantity.]");
		int UpdateSelection = input.nextInt();

		//All
		if (UpdateSelection == 1){

		System.out.println("Please enter the desired item ID: ");
		String newId = input.nextLine();

		System.out.println("Please enter the name of the item: ");
			String ItemName = input.nextLine();

		System.out.println("Please enter the price of the item: ");
			Double price = input.nextDouble();

		System.out.println("Please enter the quantity of the item in stock: ");
			int quantity = input.nextInt();

			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/courseproject", "root", "root");
			Statement stmt = connection.createStatement();
			String query = "UPDATE Product SET ProductId = "+ newId +", ProductName = \""+ ItemName + "\", Productprice = "+ price +" ProductQuantity = " + quantity + " WHERE ProductId = \"" + Id + "\" ;";
			stmt.executeUpdate(query);
		}
		//Item ID
		else if (UpdateSelection == 2){
			System.out.println("Please enter the desired item ID: ");
				Scanner scanner = new Scanner(System.in);
				String newId = scanner.nextLine();

				Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/courseproject", "root", "root");
				Statement stmt = connection.createStatement();
				String query = "UPDATE Product SET ProductId = " + newId + " WHERE ProductId = \"" + Id + "\" ;";
				stmt.executeUpdate(query);
		}
		//Item name
		else if (UpdateSelection == 3){
			System.out.println("Please enter the name of the item: ");
			Scanner scanner = new Scanner(System.in);
				String ItemName = scanner.nextLine();

				Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/courseproject", "root", "root");
				Statement stmt = connection.createStatement();
				String query = "UPDATE Product SET ProductName = \"" + ItemName + "\" WHERE ProductId = \"" + Id + "\" ;";
				stmt.executeUpdate(query);
		}
		//Item Price
		else if (UpdateSelection == 4){
			System.out.println("Please enter the price of the item: ");
			Scanner scanner = new Scanner(System.in);
				Double price = scanner.nextDouble();

				Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/courseproject", "root", "root");
				Statement stmt = connection.createStatement();
				String query = "UPDATE Product SET Productprice = " + price + " WHERE ProductId = \"" + Id + "\" ;";
				stmt.executeUpdate(query);
		}
		//Item Quantity
		else if (UpdateSelection == 5){
			System.out.println("Please enter the quantity of the item in stock: ");
			Scanner scanner = new Scanner(System.in);
				int quantity = scanner.nextInt();

				Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/courseproject", "root", "root");
				Statement stmt = connection.createStatement();
				String query = "UPDATE Product SET ProductQuantity = " + quantity + " WHERE ProductId = \"" + Id + "\" ;";
				stmt.executeUpdate(query);
		}
		else{
			System.out.println("Selection was invalid. Please choose a selection between 1-5...");
			UpdateItem();
		}
		System.out.println("Item was successfully updated!");
		mainMenu();

	}

	//===================================================REMOVES ITEM-============================================//
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
	//=======================================Transaction Report===================================//
	public static void populateTransactionList() throws SQLException{
		// tranIDList = new ArrayList<ResultSet>();
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/courseproject", "root", "root");
		Statement stmt = connection.createStatement();
		String query = "Select * from transactions";
		ResultSet rs = stmt.executeQuery(query);
		while (rs.next()){
			Transactions trans = new Transactions();
			trans.setID(rs.getInt(1));
			trans.setName(rs.getString(2));
			trans.setPrice(rs.getDouble(3));
			trans.setQuantity(rs.getInt(4));

			tranIDList.add(trans);
		}
	}
	public static void CreateTransaction(String productSold, double price, int AmountPurchased) throws SQLException{

		populateTransactionList();
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/courseproject", "root", "root");
		Statement stmt = connection.createStatement();
		String query = "INSERT INTO Transactions VALUES (" + (tranIDList.size() + 1) + ", \"" + productSold + "\", " + price + ", " + AmountPurchased + ");";
		stmt.executeUpdate(query);
	}

	public static void showTransactionReport() throws SQLException{
		populateTransactionList();
		
		System.out.println("\n|[TRANSACTION REPORT]|");
		System.out.println("______________________");
		for (Transactions tran : tranIDList) {
			
			System.out.println("|[ Transaction ID: "+ tran.getId() + " ]|[ Product: " + tran.getName() + " ]|[ Price: $" + tran.getPrice()+ " ]|[ Quantity Sold: " + tran.getQuantity() + " ]|");
		}
		System.out.print("\nTo go back to the main menu enter any character...");
		Scanner input = new Scanner(System.in);
		String usrInput = input.nextLine();
		
		if (usrInput != ""){
			mainMenu();
		}
		
	}

	//TAKES IN id and returns productName
	public static String getProductName(String Selectedid) throws SQLException{
		
		//Check to see if ID Exists first
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/courseproject", "root", "root");
		Statement stmt = connection.createStatement();
		String query = "Select * from product where productId =" + "\"" + Selectedid +"\";";
		ResultSet rs = stmt.executeQuery(query);
			while (rs.next()){
				String itemName = rs.getString(2);
		connection.close();
		if(getProductQuantity(Selectedid) > 0){
			return itemName;
		}
		else{
			return "N/A";
		}
				
			}
			
		return "Empty";
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

	public static int getProductQuantity(String selectedID) throws SQLException{
			
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/courseproject", "root", "root");
		Statement stmt = connection.createStatement();
		String query = "Select * from product where productId =" + "\"" + selectedID +"\";";
		ResultSet rs = stmt.executeQuery(query);
				rs.next();
				int Quantity = rs.getInt(4);
			
		connection.close();

		return Quantity;
	}
	

}
class Transactions{
	private int Id;
	private String productName;
	private int quantity;
	private double price;

	public Transactions(){

	}
	//setters
	public void setID(int _id){
		this.Id = _id;
	}
	public void setName(String newName){
		this.productName = newName;
	}
	public void setQuantity(int _quantity){
		this.quantity = _quantity;
	}
	public void setPrice(Double _Price){
		this.price = _Price;
	}
	//getters
	public int getId(){
		return Id;
	}
	public String getName(){
		return productName;
	}
	public int getQuantity(){
		return quantity;
	}
	public double getPrice(){
		return price;
	}

}

