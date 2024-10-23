package vpm.gui_prototype.services;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Service class for hashing passwords using the MD5 algorithm.
 */
public class PasswordHashingService {

    /**
     * Hashes the given password using MD5.
     *
     * @param passwordToHash The password to be hashed.
     * @return The hashed password in hexadecimal format.
     */
    public String getHash(String passwordToHash) {
        String generatedPassword = null;
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // Add password bytes to digest
            md.update(passwordToHash.getBytes());

            // Get the hash's bytes
            byte[] bytes = md.digest();

            // Convert bytes to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                // Convert each byte to hex and append to the StringBuilder
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }

            // Get complete hashed password in hex format
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // Print stack trace if the algorithm is not found
            e.printStackTrace();
        }
        return generatedPassword; // Return the hashed password
    }
}
