import java.util.Scanner;

public class Driver {

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		PasswordDatabase database = new PasswordDatabase();

		int choice = 0;
		String website, file;
		String masterKey = "";

		while (choice != 7) {
			System.out.println("\n-----------------");
			System.out
					.println("Welcome to Password Manager. Select an option:");
			System.out.println("1: Generate a new password.");
			System.out.println("2: Retrieve a single password.");
			System.out
					.println("3: Generate a new password for an existing account.");
			System.out.println("4: Print all entries.");
			System.out.println("5: Save All Password Data to File.");
			System.out.println("6: Load Password Data from File.");
			System.out.println("7: Exit.");
			choice = scan.nextInt();

			switch (choice) {
			case 1:
				System.out.println("-----------------");
				System.out.println("Generating New Password.");
				System.out.println("Enter website: ");
				website = scan.next();

				database.add(website);

				break;
			case 2:
				System.out.println("-----------------");
				System.out.println("Retrieving Single Password.");
				System.out.println("Enter website: ");
				website = scan.next();

				database.retrievePassword(website);

				break;
			case 3:
				System.out.println("-----------------");
				System.out
						.println("Generating New Password for an existing account.");
				System.out.println("Enter website: ");
				website = scan.next();

				database.generateNewPassword(website);

				break;
			case 4:
				database.printAll();

				break;
			case 5:
				System.out.println("-----------------");
				System.out.println("Save Password Data to File.");
				System.out.println("Enter file to be output: ");
				file = scan.next();

				while (masterKey.length() < 16) {
					System.out.println("Enter Master Key: ");
					masterKey = scan.next();

					if (masterKey.length() < 16) {
						System.out
								.println("Invalid length. Master Key must be 16+ Characters.");
					}
				}

				database.outputToFile(file, masterKey);

				break;
			case 6:
				System.out.println("-----------------");
				System.out.println("Load Password Data from File.");
				System.out.println("Enter file to be loaded: ");
				file = scan.next();

				while (masterKey.length() < 16) {
					System.out.println("Enter Master Key: ");
					masterKey = scan.next();

					if (masterKey.length() < 16) {
						System.out
								.println("Invalid length. Master Key must be 16+ Characters.");
					}
				}

				database.readFromFile(file, masterKey);
				break;
			case 7:
				System.out.println("-----------------");
				System.out.println("Exiting Password Manager.");

				break;
			default:
				System.out.println("Invalid choice");
			}
		}

	}
}
