package com.snaggly.ksw_soundrestorer;

import android.util.Log;

import com.cgutman.adblib.AdbBase64;
import com.cgutman.adblib.AdbConnection;
import com.cgutman.adblib.AdbCrypto;
import com.cgutman.adblib.AdbStream;

import org.apache.commons.codec.binary.Base64;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;

public class PmAdbManager {
    public static AdbBase64 getBase64Impl() {
        return data -> Base64.encodeBase64String(data);
    }

    private static AdbCrypto setupCrypto(File fileDir, String pubKeyFile, String privKeyFile) throws NoSuchAlgorithmException, IOException {
        File publicKey = new File(fileDir, pubKeyFile);
        File privateKey = new File(fileDir, privKeyFile);
        AdbCrypto c = null;

        if (publicKey.exists() && privateKey.exists())
        {
            try {
                c = AdbCrypto.loadAdbKeyPair(getBase64Impl(), privateKey, publicKey);
            } catch (Exception e) {
                c = null;
            }
        }

        if (c == null)
        {
            c = AdbCrypto.generateAdbKeyPair(getBase64Impl());
            c.saveAdbKeyPair(privateKey, publicKey);
        }

        return c;
    }

    public static void tryGrantingPermissionOverAdb(File fileDir, String pm) throws IOException, NoSuchAlgorithmException, InterruptedException {
        AdbCrypto adbCrypto = setupCrypto(fileDir, "public.key", "private.key");
        Socket socket = new Socket ("127.0.0.1", 5555);
        AdbConnection adbConnection = AdbConnection.create(socket, adbCrypto);
        adbConnection.connect();

        AdbStream shellStream = adbConnection.open("shell:");
        shellStream.write("pm grant " + BuildConfig.APPLICATION_ID + " android.permission." + pm);
        shellStream.close();
    }
}
