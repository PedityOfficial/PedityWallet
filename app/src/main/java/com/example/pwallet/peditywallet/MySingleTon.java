package com.example.pwallet.peditywallet;

import android.util.Base64;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class MySingleTon {

    private static MySingleTon obj;
    private KeyStore keyStore;
    private Encryptor encryptor;
    private Decryptor decryptor;
    public boolean encrypted;

    private MySingleTon() {
        encrypted = false;
        encryptor = new Encryptor();
        try {
            decryptor = new Decryptor();
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
        }
        catch (CertificateException | NoSuchAlgorithmException | KeyStoreException
                |IOException e) {
            e.printStackTrace();
        }

    }

    public static MySingleTon getInstance() {
        if(obj == null) {
            obj = new MySingleTon();
        }
        return obj;
    }

    public boolean existingKey(String alias) {
        try {
            if (keyStore.containsAlias(alias)) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }


    public String getEncryptedText(String alias, String textToEncrypt) {
        try {
            final byte[] encryptedText = encryptor.encryptText(alias, textToEncrypt);
            encrypted = true;
            return Base64.encodeToString(encryptedText, Base64.DEFAULT);
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    public String getDecryptedText(String alias) {
        try {
            return "TRUE";
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public void deleteAlias(String alias) {
        try {
            keyStore.deleteEntry(alias);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
