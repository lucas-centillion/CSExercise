package com.semjournals.encryption;

import org.apache.commons.lang3.ArrayUtils;
import org.bouncycastle.jcajce.provider.digest.SHA3;
import org.bouncycastle.util.encoders.Hex;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

/**
 * Based on OWASP recommendations: https://www.owasp.org/index.php/Hashing_Java
 * Provides an easy and secure way to encrypt passwords and validate them
 */
public class HashUtil {

    private final static int ITERATION_NUMBER = 10000;

    private static byte[] getHash(int iterationNb, String password, byte[] salt) throws UnsupportedEncodingException {
        final byte[] passwordBytes = password.getBytes("UTF-8");
        final byte[] all = ArrayUtils.addAll(passwordBytes, salt);

        SHA3.DigestSHA3 md = new SHA3.Digest512();
        md.update(all);
        byte[] input = md.digest();
        for (int i = 0; i < iterationNb; i++) {
            md.reset();
            input = md.digest(input);
        }
        return input;
    }

    public static String encrypt(String text, byte[] salt) throws UnsupportedEncodingException {
        byte[] bDigest = getHash(ITERATION_NUMBER, text, salt);

        return Hex.toHexString(bDigest);
    }

    public static String[] encrypt(String text) throws UnsupportedEncodingException {
        SecureRandom secureRandom;
        try {
            secureRandom = SecureRandom.getInstance("SHA1PRNG", "SUN");
        } catch (NoSuchAlgorithmException nsae) {
            secureRandom = new SecureRandom();
        } catch (NoSuchProviderException e) {
            secureRandom = new SecureRandom();
        }

        byte[] bSalt = new byte[32];
        secureRandom.nextBytes(bSalt);

        String sDigest = encrypt(text, bSalt);
        String sSalt = Hex.toHexString(bSalt);

        return new String[]{sDigest, sSalt};
    }
}
