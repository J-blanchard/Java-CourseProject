import java.util.*;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogManager;

public class SalesApp {
	
	private final static Logger LOGGER = Logger.getLogger(MyLogger.class.getName());
	public static HashMap<String, Object> map = new HashMap<String, Object>();
	//test
	public static void main(String[] args) throws SecurityException, IOException{
		 	
		LOGGER.info("Logger Name: "+LOGGER.getName());
		
	        try{
	        	System.out.println("Welcome to the sales application!\n");
	    		//calls the navigation method
	    		mainMenu();
	    		LOGGER.info("info msg");
	            
	        }catch(Exception ex){
	            LOGGER.log(Level.SEVERE, "Exception occurred", ex);
	        }
	 
		}
	
	
	public static void mainMenu() {
		
		System.out.println("Please select what you would like to do:\n");
		System.out.println("Enter 1 to purchase an item.");
		System.out.println("Enter 2 to add a new item.");
		System.out.println("Enter 3 to remove an item.");
		System.out.println("Enter 4 to exit.");
		Scanner input = new Scanner(System.in);
		int selection = input.nextInt();
		if (selection == 1) {
			purchaseItem();
		}
		if(selection == 2) {
			addItem(map);
		}
		if (selection == 3) {
			removeItem(map);
		}
		if (selection == 4) {
			System.out.println("Thank you, enjoy your day!");
		}
		else {
			System.out.println("Selection was invalid please choose from the following options:" + 2/0);
			mainMenu();
		}//test
	}
	public static void purchaseItem() {
		String selectedItem;
		String confirm;
		double payment;
		System.out.println("______|    A  	|    B    |    C    |    D    |\n");
		System.out.println("   1  |  item1  |  item2  |  item3  |  item4  |\n");
		System.out.println("   2  |  item5  |  item6  |  item7  |  item8  |\n");
		System.out.println("   3  |  item9  |  item10 |  item11 |  item12 |\n");
		System.out.println("   4  |  item13 |  item14 |  item15 |  item16 |\n");
		System.out.println("Please select an item to purchase: "
				+ " (Ex. Enter B1 to select item2)");
		Scanner input = new Scanner(System.in);
		selectedItem = input.nextLine().toUpperCase();
		if(selectedItem.length() != 2) {
			System.out.println("Your selection was invalid. Selection must be the column letter followed by the row number (A1).");
			purchaseItem();
		}
		else {
		
			//check to see if the input value is correct
			System.out.println("You have selected item " + selectedItem + ", " + map.get(selectedItem)+ ". Is this selection correct? (Enter Y/N)");
			confirm = input.nextLine().toUpperCase();
			System.out.println(confirm);
			
			
			//needed to make input a char in order for the conditional statement below to work as intended.
			char[] confirmed = confirm.toCharArray();
			if( confirmed[0] == 'Y') {
				System.out.println("The price of " + map.get(selectedItem) + " is: $2.00. Please enter payment at this time");
				System.out.println("Total Amount Due: $2.00");
				payment = input.nextDouble();
				System.out.println("amount Received: " + payment);
				System.out.println("Thank You! Here is your change: $0.00");
				mainMenu();
			}
			//if input is incorrect, then 
			else {
				System.out.println("Please re-enter your selection");
				purchaseItem();	
			}
		}
	}
	public static void addItem(HashMap<String, Object> map) {
		Product product = new Product();
		System.out.println("Please enter the desired Item Id: ");
		Scanner input = new Scanner(System.in);
		product.Id = input.nextLine().toUpperCase();
		System.out.println("Please enter the name of the item: ");
		product.itemName = input.nextLine();
		System.out.println("Please enter the price of the item: ");
		product.price = input.nextDouble();
		System.out.println("Please enter the quantity of the item in stock: ");
		product.quantity = input.nextInt();
		
		map.put(product.Id, product);
		
		System.out.println("You have successfully added the following item:" + product.itemName);
		
		mainMenu();
		
		
	}
	public static void removeItem(HashMap<String, Object> map) {
		String itemId;
		System.out.println("Enter the Id of the product you would like to delete: ");
		Scanner input = new Scanner(System.in);
		itemId = input.nextLine();
		map.remove(itemId);
		System.out.println("You have successfully removed the following item:" + map.get(itemId));
		mainMenu();
	}

}
class Product{

	public String Id;
	public String itemName;
	public double price;
	public int quantity;
	
	public Product() {
		
		
	}
}
