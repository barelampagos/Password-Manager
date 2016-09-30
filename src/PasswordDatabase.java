import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeMap;

public class PasswordDatabase {

	private TreeMap<String, String> passwordDatabase;
	private ArrayList<String> wordDatabase;

	public PasswordDatabase() {
		passwordDatabase = new TreeMap<String, String>();
		wordDatabase = new ArrayList<String>();

		try {
			// Load nounlist.txt --> wordDatabase for xkcd password generation
			BufferedReader br = new BufferedReader(new FileReader("nounlist.txt"));
			String line;

			while ((line = br.readLine()) != null) {
				wordDatabase.add(line.replaceAll("\\W", ""));
			}
		} catch (IOException e) {
			System.out.println("File does not exist.");
		}
	}

	/***
	 * Adds an entry to the password database. Given a website, checks if there
	 * is already an entry in the database. If there is not, website and newly
	 * generated password are inserted into the database.
	 * 
	 * @param website
	 *            website/service of account
	 */
	public void add(String website) {

		String cleanSite = cleanURL(website);
		String password;

		if (!passwordDatabase.containsKey(cleanSite)) {
			password = generatePassword();
			System.out.println("Account: " + cleanSite + "\nPassword: " + password);

			passwordDatabase.put(cleanSite, password);
		} else {
			System.out.println("Password already exists for '" + cleanSite + "'.");
		}

	}

	/**
	 * Helper function to clean website URLs for simplified viewing of accounts.
	 * Strips out 'www.' & '.com', and converts the URL to lowercase.
	 * 
	 * @param URL
	 *            web URL to be cleaned
	 * @return cleaned URL
	 */
	private String cleanURL(String URL) {

		String cleanURL = URL.replaceAll("www.", "");
		cleanURL = cleanURL.replaceAll(".com", "");
		cleanURL = cleanURL.toLowerCase();

		return cleanURL;
	}

	/**
	 * Retrieves password from database given a proper website.
	 * 
	 * @param website
	 *            website/service of account
	 */
	public void retrievePassword(String website) {
		if (passwordDatabase.containsKey(cleanURL(website))) {
			System.out.println(passwordDatabase.get(cleanURL(website)));
		} else {
			System.out.println("No password exists for website " + website + ".");
		}
	}

	/**
	 * Prints all account / password entries in the database
	 */
	public void printAll() {
		System.out.println("---------------------------------------------------------"); // 50
		System.out.println(String.format("|  %-20s | %-30s|", "ACCOUNT", "PASSWORD"));
		System.out.println("---------------------------------------------------------");

		for (String key : passwordDatabase.keySet()) {
			System.out.println(String.format("|  %-20s | %-30s|", key, passwordDatabase.get(key)));
		}
		System.out.println("---------------------------------------------------------");
	}

	/**
	 * Prompts the user to enter their master key/password used for both
	 * encryption and decryption of the password text file.
	 * 
	 * @return master key entered in by user.
	 */
	private String enterMasterKey() {
		String masterKey = "";
		Scanner scan = new Scanner(System.in);

		while (masterKey.length() != 16) {
			System.out.println("Enter Master Key (16 characters): ");
			masterKey = scan.next();

			if (masterKey.length() != 16) {
				System.out.println("Invalid length. Master Key must be 16+ Characters.");
			}
		}
		return masterKey;
	}

	/**
	 * Saves the current database to an encrypted text file for later use. User
	 * must specify the path they wish to save it to (i.e. passwords.txt).
	 * 
	 * @param path
	 *            path of output file
	 */
	public void outputToFile(String path) {
		String masterKey = enterMasterKey();

		try {
			FileWriter file = new FileWriter(new File(path));
			BufferedWriter writer = new BufferedWriter(file);
			String line;

			for (String key : passwordDatabase.keySet()) {
				line = Encrypter.encrypt(key + " " + passwordDatabase.get(key), masterKey);
				writer.write(line + "\n");
			}

			writer.close();
		} catch (IOException e) {
			System.out.println("Error in writing results to " + path);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Decryption of encrypted text file, and parses text file to create
	 * account/password entries in the database. User must specify the path of
	 * the encrypted password file.
	 * 
	 * @param path
	 *            path of encrypted password file
	 */
	public void readFromFile(String path) {

		System.out.println();
		String masterKey = enterMasterKey();

		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			String line;
			passwordDatabase = new TreeMap<String, String>();

			while ((line = br.readLine()) != null) {
				line = Encrypter.decrypt(line, masterKey);
				String[] accountInfo = line.split(" ");
				passwordDatabase.put(accountInfo[0], accountInfo[1]);
			}

			System.out.println(path + " successfully parsed. Password database created.");
		} catch (IOException e) {
			System.out.println("Invalid path " + path);
		} catch (Exception e) {
			System.out.println("Decryption failed. Invalid password.");
		}
	}

	/**
	 * Resets password for an already existing account in the password database.
	 * 
	 * @param website
	 *            website/service of password to be reset
	 */
	public void resetPassword(String website) {
		String cleanSite = cleanURL(website);
		String password;

		if (passwordDatabase.containsKey(cleanSite)) {
			password = generatePassword();
			System.out.println("Account: " + cleanSite + "\nPassword: " + password);

			passwordDatabase.put(cleanSite, password);
		} else {
			System.out.println("Account does not exist.");
		}
	}

	/**
	 * Generates a password from various choices of algorithms.
	 * 
	 * @return newly generated password
	 */
	private String generatePassword() {
		Scanner scanner = new Scanner(System.in);
		int choice = 0;
		String password = "";

		while (password.equals("")) {
			System.out.println("\nSelect password algorithm:");
			System.out.println("1. XKCD Algorithm");
			System.out.println("2. Random Password Generator");
			choice = scanner.nextInt();

			switch (choice) {
			case 1:
				password = generatePasswordXKCD();
				break;
			case 2:
				System.out.println("How many characters for your password? (Between 5 - 30)");
				int length = scanner.nextInt();

				while (length < 5 || length > 30) {
					System.out.println("Password length must be between 5 - 30 characters.");
					length = scanner.nextInt();
				}
				password = generatePasswordRandom(length);
				break;
			default:
				System.out.println("Invalid Choice.");
			}
		}

		return password;
	}

	/**
	 * Generates a password by concatenating 4 random dictionary words together.
	 * Inspired by the xkcd comic on password strength (936).
	 * 
	 * @return newly generated password
	 */
	private String generatePasswordXKCD() {
		Random r = new Random();
		int numWords = 4;
		StringBuilder password = new StringBuilder();
		String line;

		for (int i = 0; i < numWords; i++) {
			line = wordDatabase.get(r.nextInt(wordDatabase.size()));
			password.append(line);
		}

		return password.toString();
	}

	/**
	 * Generates a new password by selecting random characters from the valid
	 * password characters. Returns a password of user specified length.
	 * 
	 * @param length
	 *            preferred length of password
	 * @return newly generated password
	 */
	private String generatePasswordRandom(int length) {
		Random r = new Random();
		String passwordCharacters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789~`!@#$%^&*()_-+=[{}]|':?<>.,";
		StringBuilder password = new StringBuilder();

		for (int i = 0; i < length; i++) {
			password.append(passwordCharacters.charAt(r.nextInt(passwordCharacters.length())));
		}

		return password.toString();
	}
}
