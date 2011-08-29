// Developed by Jose.Ruperez@intersytems.com

import java.io.*;
import com.intersys.globals.*;

public class MyPassword {
	
    static			Connection		connection;
	
	public static void main(String[] args) throws IOException {
		checkParameters(args,"");
		
		try {
			//System.out.println("Connecting to DB...");
			connection = ConnectionContext.getConnection();
			if (!connection.isConnected()) {
				connection.connect("USER","_SYSTEM","SYS");
		    }
			
			// MENU
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			String optionStr;
			Integer option;
			do{
				System.out.println("\nMyPassword");
				System.out.println("1. Insert Password");
				System.out.println("2. Delete Password");
				System.out.println("3. Search Password");
				System.out.println("4. Browse All");
				System.out.println("9. Exit");
				System.out.print("Enter your option: ");
				optionStr = in.readLine();
				option = new Integer(optionStr);
				if (option == 1)
				{
					System.out.print("SYSTEM: ");
					String mysystem = in.readLine();
					System.out.print("USERNAME: ");
					String myusername = in.readLine();
					System.out.print("PASSWORD: ");
					String mypassword = in.readLine();
					
					NodeReference mynode = connection.createNodeReference("MyPass");
					mynode.appendSubscript(mysystem);
					mynode.appendSubscript(myusername);
					mynode.set(mypassword);
					
					System.out.println("Inserting...");
				}
				if (option == 2){
					System.out.print("SYSTEM: ");
					String mysystem = in.readLine();
					System.out.print("USERNAME: ");
					String myusername = in.readLine();
					
					NodeReference mynode = connection.createNodeReference("MyPass");
					mynode.appendSubscript(mysystem);
					mynode.appendSubscript(myusername);
					mynode.kill();
					
					System.out.println("Deleting...");
				}
				if (option == 3){
					System.out.print("SYSTEM: ");
					String mysystem = in.readLine();
					System.out.print("USERNAME: ");
					String myusername = in.readLine();
					
					NodeReference mynode = connection.createNodeReference("MyPass");
					String str = mynode.getString(mysystem, myusername);
					
					System.out.println("PASSWORD --> "+str);
				}
				if (option == 4){	
					NodeReference mynode = connection.createNodeReference("MyPass");
					// ITEREATE THROUGH FIRST LEVEL SUBSCRIPT
					String subscript1 = "";
					do {
						subscript1 = mynode.nextSubscript(subscript1);
						if (subscript1.length() > 0){
							System.out.println(" " + subscript1 + " ");
							// ITERATE THROUGH SECOND LEVEL SUBSCRIPT
							String subscript2 = "";
							do {
								mynode.setSubscript(1, subscript1);
								subscript2 = mynode.nextSubscript(subscript2);
								if (subscript2.length() > 0)
									System.out.println(" - " + subscript2);
							}while (subscript2.length() > 0);
							mynode.setSubscriptCount(0)	;
						}
					} while (subscript1.length() > 0);
					
				}
				
				
			}while( option != 9 );
			
			}catch (GlobalsException e) {
		         System.out.println("Caught GlobalsException: " + e.getMessage());
		    }
		    finally {
		         //System.out.println("Closing connection.");
		         try {
					connection.close();
		         } catch (GlobalsException e) {
		            System.out.println("Caught GlobalsException: " + e.getMessage());
		         }
		      }
	}

	static void checkParameters(String args[], String helpText) {
	        if ((args.length == 1) && args[0].equalsIgnoreCase("-help")) {
	            System.out.println("Parameters: \r\n  none \r\n  " + helpText);
	            return;
	        }
	}
}

