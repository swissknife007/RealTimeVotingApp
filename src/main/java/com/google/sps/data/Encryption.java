package com.google.sps.data;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

import com.google.crypto.tink.Aead;
import com.google.crypto.tink.CleartextKeysetHandle;
import com.google.crypto.tink.JsonKeysetReader;
import com.google.crypto.tink.JsonKeysetWriter;
import com.google.crypto.tink.KeysetHandle;
import com.google.crypto.tink.aead.AeadConfig;
import com.google.crypto.tink.aead.AeadKeyTemplates;

/**
 * This is our encryption class that uses the GOOGLE TINK library. This class
 * has two functionalities: encryption and decryption.
 */
public final class Encryption {
    private static File keyset;

    /**
     * necessary constructor for utility class.
     */
    private Encryption() {
    }

    /**
     * This is our initialzer that initializes the type of encryption from Google
     * Tink (AEAD), and creates a new file for our encryption keys.
     */
    public static void registerEncryption() {
        try {
            AeadConfig.register();
        } catch (GeneralSecurityException e) {
            System.err.println("ERROR: Aead registering");
        }
        keyset = new File("test.cfg");
    }

    /**
     * This method Loads a KeysetHandle from keyset or generate a new one if it
     * doesn't exist.
     *
     * @param keyset , a file containing keys for decryption/encryption.
     * @return KeysetHandle , returns keyset handler.
     */

    private static KeysetHandle getKeysetHandle(File key) throws GeneralSecurityException, IOException {
        if (key.exists()) {
            return CleartextKeysetHandle.read(JsonKeysetReader.withFile(key));
        }
        KeysetHandle handle = KeysetHandle.generateNew(AeadKeyTemplates.AES128_GCM);
        CleartextKeysetHandle.write(handle, JsonKeysetWriter.withFile(key));
        return handle;
    }

    /**
     * This method takes in a string, then it encrypts it and returns the encrypted
     * string.
     *
     * @param string , the string to be encrypted.
     * @return byte[] , the encrypted string.
     */
    public static byte[] encrypt(String string) {
        // 1. Obtain a keyset handle.
        KeysetHandle handle;

        try {
            handle = getKeysetHandle(keyset);
            // 2. Get a primitive.
            Aead aead = handle.getPrimitive(Aead.class);
            // 3. Do crypto.
            byte[] plaintext = string.getBytes();
            byte[] ciphertext = aead.encrypt(plaintext, new byte[0]);
            return ciphertext;
        } catch (GeneralSecurityException | IOException e) {
            System.err.println("ERROR: encrypting in Encryption.");
            return null;
        }

    }

    /**
     * This method decrypts the byte list taken it and returns the decyphered text.
     *
     * @param string , the encrypted word/s.
     * @return String , the decrypted string.
     */
    public static String decrypt(byte[] string) {
        KeysetHandle handle;
        try {
            handle = getKeysetHandle(keyset);
            Aead aead = handle.getPrimitive(Aead.class);
            byte[] ciphertext = string;
            String plaintext = new String(aead.decrypt(ciphertext, new byte[0]));
            return plaintext;
        } catch (GeneralSecurityException | IOException e) {
            System.err.println("ERROR: decrypting in Encryption");
            return null;
        }

    }

}