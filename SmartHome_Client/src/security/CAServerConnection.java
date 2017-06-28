package security;

import constant.Filepath;
import util.CloseableUtil;
import util.CryptographyGenerator;
import util.LogUtil;
import util.SocketToFileStreamUtil;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.cert.X509Certificate;

public class CAServerConnection extends Thread
{
    public CAServerConnection()
    {

    }

    @Override
    public void run()
    {

    }
}
