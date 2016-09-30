import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Encrypter {

	/**
	 * Encrypts data using the AES encryption algorithm. Uses the key generated
	 * from user specified masterKey. Returns the encrypted data.
	 * 
	 * @param rawData
	 *            data to be encrypted
	 * @param masterKey
	 *            16 character string used to generate encryption key
	 * @return encrypted data - ciphertext
	 * @throws Exception
	 */
	public static String encrypt(String rawData, String masterKey) throws Exception {
		Key key = generateKey(masterKey);
		Cipher c = Cipher.getInstance("AES");
		c.init(Cipher.ENCRYPT_MODE, key);
		byte[] encVal = c.doFinal(rawData.getBytes());
		String encryptedValue = new BASE64Encoder().encode(encVal);
		return encryptedValue;
	}

	/**
	 * Decrypts data previously encrypted with the AES encryption algorithm.
	 * User must enter the same key used for initial encryption in order for
	 * decryption to happen properly. Returns the original data.
	 * 
	 * @param encryptedData
	 *            ciphertext generated from encryption
	 * @param masterKey
	 *            16 character string used to encrypt original data
	 * @return decrypted data - plaintext
	 * @throws Exception
	 */
	public static String decrypt(String encryptedData, String masterKey) throws Exception {
		Key key = generateKey(masterKey);
		Cipher c = Cipher.getInstance("AES");
		c.init(Cipher.DECRYPT_MODE, key);
		byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);
		byte[] decValue = c.doFinal(decordedValue);
		String decryptedValue = new String(decValue);
		return decryptedValue;
	}

	/**
	 * Generates a key for AES encryption/decryption from the 16 character
	 * string.
	 * 
	 * @param masterKey
	 *            16 character string
	 * @return AES key used for encryption/decryption
	 * @throws Exception
	 */
	private static Key generateKey(String masterKey) throws Exception {
		byte[] keyVal = masterKey.getBytes();
		Key key = new SecretKeySpec(keyVal, "AES");
		return key;
	}
}
