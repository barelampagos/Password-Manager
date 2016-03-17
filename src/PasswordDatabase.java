import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.TreeMap;

public class PasswordDatabase {

	private TreeMap<String, String> passwordDatabase;
	private ArrayList<String> wordDatabase;

	// TODO: Add function - Load passwords file from default (pwd.txt)
	public PasswordDatabase() {

		// Generate new password database
		passwordDatabase = new TreeMap<String, String>();
		wordDatabase = new ArrayList<String>();

		try {
			BufferedReader br = new BufferedReader(new FileReader(
					"nounlist.txt"));

			String line;

			while ((line = br.readLine()) != null) {
				wordDatabase.add(line.replaceAll("\\W", ""));
			}
		} catch (IOException e) {
			System.out.println("File does not exist.");
		}
	}

	public void add(String website) {

		String cleanSite = cleanURL(website);
		String password;

		if (!passwordDatabase.containsKey(cleanSite)) {
			password = passwordGenerator();
			System.out.println("Account: " + cleanSite + "\nPassword: "
					+ password);

			passwordDatabase.put(cleanSite, password);
		} else {
			System.out.println("Password already exists for '" + cleanSite
					+ "'.");
		}

	}

	private String cleanURL(String URL) {

		String cleanURL = URL.replaceAll("www.", "");
		cleanURL = cleanURL.replaceAll(".com", "");
		cleanURL = cleanURL.toLowerCase();

		return cleanURL;
	}

	public void retrievePassword(String website) {
		if (passwordDatabase.containsKey(cleanURL(website))) {
			System.out.println(passwordDatabase.get(cleanURL(website)));
		} else {
			System.out.println("No password exists for website " + website
					+ ".");
		}
	}

	public void printAll() {
		System.out
				.println("---------------------------------------------------------"); // 50
		System.out.println(String.format("|  %-20s | %-30s|", "ACCOUNT",
				"PASSWORD"));
		System.out
				.println("---------------------------------------------------------");

		for (String key : passwordDatabase.keySet()) {
			System.out.println(String.format("|  %-20s | %-30s|", key,
					passwordDatabase.get(key)));
		}
		System.out
				.println("---------------------------------------------------------");
	}

	public void outputToFile(String path, String masterKey) {

		try {
			FileWriter file = new FileWriter(new File(path));

			BufferedWriter writer = new BufferedWriter(file);

			// writer.write(key + "\n");
			String line;

			for (String key : passwordDatabase.keySet()) {
				line = Encrypter.encrypt(key + " " + passwordDatabase.get(key),
						masterKey);
				writer.write(line + "\n");
			}

			writer.close();
		} catch (IOException e) {
			System.out.println("Error in writing results to passwords.txt.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void readFromFile(String path, String masterKey) {

		System.out.println();
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			String line;
			passwordDatabase = new TreeMap<String, String>();

			while ((line = br.readLine()) != null) {
				line = Encrypter.decrypt(line, masterKey);
				String[] accountInfo = line.split(" ");
				passwordDatabase.put(accountInfo[0], accountInfo[1]);

			}
		} catch (IOException e) {
			System.out.println("Error in writing results to passwords.txt.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void generateNewPassword(String website) {
		String cleanSite = cleanURL(website);
		String password;

		if (passwordDatabase.containsKey(cleanSite)) {
			password = passwordGenerator();
			System.out.println("Account: " + cleanSite + "\nPassword: "
					+ password);

			passwordDatabase.put(cleanSite, password);
		} else {
			System.out.println("Account does not exist.");
		}
	}

	// public void testEncryption() {
	// String account;
	//
	// try {
	// for (String key : passwordDatabase.keySet()) {
	// account = key + " " + passwordDatabase.get(key);
	//
	// System.out.println(account);
	// account = Encrypter.encrypt(account);
	// System.out.println(account);
	// account = Encrypter.decrypt(account);
	// System.out.println(account + "\n");
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// }

	private String passwordGenerator() {
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
}
