package Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import ServidorPersistenteJDBC.config.IST2002Properties;

/**
 * @author Fernanda Quitério 06/Out/2003
 */
public class Ftp extends FenixUtil
{
    private static Properties _properties = null;

    private static String _userName;

    private static String _password;

    private static String _urlFTP;

    private static void carregaPropriedades(String filename)
    {
        _properties = new IST2002Properties(filename);
        _userName = _properties.getProperty("IST.FTP.server.usernameFTP");
        _password = _properties.getProperty("IST.FTP.server.passwordFTP");
        _urlFTP = _properties.getProperty("IST.FTP.server.urlFTP");
        if (_userName == null || _password == null)
        {
            System.out.println("FtpFicheiro: propriedades indefinidas.");
        }
        return;
    }

    public static void enviarFicheiro(String nomeFicheiroConfig,
            String nomeFicheiro, String caminho) throws IOException
    {
        carregaPropriedades(nomeFicheiroConfig);

        FTPClient ftp = new FTPClient();
        try
        {
            int reply;

            ftp.connect(_urlFTP);

            // After connection attempt, you should check the reply code to
            // verify
            // success.
            reply = ftp.getReplyCode();

            if (!FTPReply.isPositiveCompletion(reply))
            {
                ftp.disconnect();
                System.err.println("FTP server refused connection.");
                throw new IOException("error.impossible.submit");
            }
            System.out.println("Connected to " + _urlFTP);
            System.out.print(ftp.getReplyString());

            ftp.login(_userName, _password);
            InputStream inputStream = new FileInputStream(System
                    .getProperty("java.io.tmpdir")
                    + File.separator + nomeFicheiro);

            ftp.storeFile(caminho + nomeFicheiro, inputStream);
            inputStream.close();
            System.out.println("Going to disconnect from " + _urlFTP);
            ftp.disconnect();

        }
        catch (IOException e)
        {
            if (ftp.isConnected())
            {
                try
                {
                    ftp.disconnect();
                }
                catch (IOException f)
                {
                    // do nothing
                }
            }
            System.err.println("Could not connect to server.");
            e.printStackTrace();
            throw new IOException("error.impossible.submit");
        }
    }

    public static void enviarFicheiros(String nomeFicheiroConfig, List files,
            String caminho) throws IOException
    {
        carregaPropriedades(nomeFicheiroConfig);

        FTPClient ftp = new FTPClient();
        try
        {
            int reply;

            ftp.connect(_urlFTP);

            // After connection attempt, you should check the reply code to
            // verify
            // success.
            reply = ftp.getReplyCode();

            if (!FTPReply.isPositiveCompletion(reply))
            {
                ftp.disconnect();
                System.err.println("FTP server refused connection.");
                throw new IOException("error.impossible.submit");
            }
            System.out.println("Connected to " + _urlFTP);
            System.out.print(ftp.getReplyString());

            ftp.login(_userName, _password);
            Iterator iter = files.iterator();
            while (iter.hasNext())
            {
                File file = (File) iter.next();
                InputStream inputStream = new FileInputStream(System
                        .getProperty("java.io.tmpdir")
                        + File.separator + file.getName());

                ftp.storeFile(caminho + file.getName(), inputStream);
                inputStream.close();
            }

            System.out.println("Going to disconnect from " + _urlFTP);
            ftp.disconnect();

        }
        catch (IOException e)
        {
            if (ftp.isConnected())
            {
                try
                {
                    ftp.disconnect();
                }
                catch (IOException f)
                {
                    // do nothing
                }
            }
            System.err.println("Could not connect to server.");
            e.printStackTrace();
            throw new IOException("error.impossible.submit");
        }
    }
}