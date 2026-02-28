package org.socialmediaapp.social_media_app.util;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

/**
 * Utility class for secure password hashing using PBKDF2WithHmacSHA256.
 * Generates a random salt per password and stores both salt and hash together.
 * Format: "salt:hash" (both Base64-encoded).
 */
public class PasswordUtil {

    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 256;
    private static final int SALT_LENGTH = 16;

    private PasswordUtil() {
        // Utility class — prevent instantiation
    }

    /**
     * Hashes a plain-text password with a randomly generated salt.
     * @param password the plain-text password
     * @return the hashed password in "salt:hash" format (Base64-encoded)
     */
    public static String hashPassword(String password) {
        byte[] salt = generateSalt();
        byte[] hash = pbkdf2(password.toCharArray(), salt);
        return Base64.getEncoder().encodeToString(salt)
                + ":"
                + Base64.getEncoder().encodeToString(hash);
    }

    /**
     * Verifies a plain-text password against a stored "salt:hash" value.
     * @param password    the plain-text password to check
     * @param storedHash  the stored hash in "salt:hash" format
     * @return true if the password matches, false otherwise
     */
    public static boolean verifyPassword(String password, String storedHash) {
        String[] parts = storedHash.split(":");
        if (parts.length != 2) {
            return false;
        }
        byte[] salt = Base64.getDecoder().decode(parts[0]);
        byte[] expectedHash = Base64.getDecoder().decode(parts[1]);
        byte[] actualHash = pbkdf2(password.toCharArray(), salt);

        // Constant-time comparison to prevent timing attacks
        return constantTimeEquals(expectedHash, actualHash);
    }

    /** Generates a cryptographically secure random salt. */
    private static byte[] generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    /** Derives a key from the password and salt using PBKDF2. */
    private static byte[] pbkdf2(char[] password, byte[] salt) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
            SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
            return factory.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    /** Constant-time byte array comparison to prevent timing attacks. */
    private static boolean constantTimeEquals(byte[] a, byte[] b) {
        if (a.length != b.length) {
            return false;
        }
        int result = 0;
        for (int i = 0; i < a.length; i++) {
            result |= a[i] ^ b[i];
        }
        return result == 0;
    }
}
