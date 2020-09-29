package com.snaggly.ksw_soundrestorer;

import com.cgutman.adblib.AdbBase64;
import com.cgutman.adblib.AdbConnection;
import com.cgutman.adblib.AdbCrypto;
import com.cgutman.adblib.AdbStream;

import org.apache.commons.codec.binary.Base64;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.atomic.AtomicReference;

public class PmAdbManager {
    private static PmAdbManager instance;
    private AdbCrypto adbCrypto;
    private Socket socket;
    private AdbConnection adbConnection;
    private AdbStream shellStream;
    private boolean isConnected = false;

    public static PmAdbManager initInstance(File fileDir) throws InterruptedException, NoSuchAlgorithmException, IOException {
        if (instance!=null){
            instance.disconnect();
        }
        instance = new PmAdbManager(fileDir);
        return instance;
    }

    public static PmAdbManager getInstance() throws InterruptedException, NoSuchAlgorithmException, IOException {
        return instance;
    }

    private PmAdbManager(File fileDir) throws IOException, NoSuchAlgorithmException, InterruptedException {
        adbCrypto = setupCrypto(fileDir, "public.key", "private.key");
        socket = new Socket();
        connect();
    }

    private void connect() throws IOException, InterruptedException {
        socket.connect(new InetSocketAddress("localhost", 5555), 5000);
        adbConnection = AdbConnection.create(socket, adbCrypto);
        adbConnection.connect();
        shellStream = adbConnection.open("shell:");
    }

    public void disconnect() throws IOException {
        shellStream.close();
        adbConnection.close();
        socket.close();
        instance = null;
    }

    public static AdbBase64 getBase64Impl() {
        return data -> Base64.encodeBase64String(data);
    }

    private AdbCrypto setupCrypto(File fileDir, String pubKeyFile, String privKeyFile) throws NoSuchAlgorithmException, IOException {
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

    public void executeCmd(String cmd) throws IOException, InterruptedException {
        shellStream.write(cmd + "\n");
    }

    public void executeShellCmds(String[] cmds) throws Exception{
        for (String cmd : cmds){
            shellStream.write(cmd + "\n");
        }
    }

    public void tryGrantingPermissionOverAdb(String pm) throws Exception {
        AtomicReference<Exception> outerexception = new AtomicReference<>();
        Thread thread = new Thread(() -> {
            try{
                AdbStream shellStream = adbConnection.open("shell:");
                shellStream.write("pm grant " + BuildConfig.APPLICATION_ID + " " + pm + "\n");
                shellStream.close();
            }
            catch (Exception innerException){
                outerexception.set(innerException);
            }
        });

        thread.start();
        thread.join();

        if (outerexception.get() != null)
            throw outerexception.get();
    }
}
