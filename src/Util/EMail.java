package Util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EMail {
    
    private String Servidor;
    private String Origem;
    
    /**
     * Metodo Construtor, recebe um Servidor e uma Origem por omissao
     */
    public EMail(String Servidor, String Origem){
        this.Servidor = Servidor;
        this.Origem = Origem;
    }
    
    /**
     * Envia um mail utilizando o Servidor e Origem por Omissao
     */
    public boolean send(String Destino, String Assunto, String Texto){
        return _send(this.Servidor, this.Origem, Destino, Assunto, Texto);
    }
    
    /**
     * Envia um mail utilizando o Servidor por Omissao
     */
    public boolean send(String Origem, String Destino, String Assunto, String Texto){
        return _send(this.Servidor, Origem, Destino, Assunto, Texto);
    }
    
    /**
     * Envia um mail
     */
    public static boolean send(String Servidor, String Origem, String Destino, String Assunto, String Texto){
        return _send(Servidor, Origem, Destino, Assunto, Texto);
    }
    
    
    
    private static boolean _send(String Servidor, String Origem, String Destino, String Assunto, String Texto) {
        /* Configura as propriedades do sistema */
        Properties props = new Properties();
        props.put("mail.smtp.host", Servidor);
        
        /* Obtem a sessao */
        Session sessao = Session.getDefaultInstance(props, null);
        
        /* Cria a mensagem */
        MimeMessage mensagem = new MimeMessage(sessao);
        
        try {
            /* Define os parametros da mensagem */
            mensagem.setFrom(new InternetAddress(Origem));
            mensagem.addRecipient(Message.RecipientType.TO,
            new InternetAddress(Destino));
            mensagem.setSubject(Assunto);
            mensagem.setText(Texto);

            Transport.send(mensagem);
        }
        catch (AddressException e) {
            System.out.println("EMail: Nao foi possivel enviar o email");
            e.printStackTrace(System.out);
            return false;
        }
        catch (MessagingException e) {
            System.out.println("EMail: Nao foi possivel enviar o email");
            e.printStackTrace(System.out);
            return false;
        }
        return true;
    }
}