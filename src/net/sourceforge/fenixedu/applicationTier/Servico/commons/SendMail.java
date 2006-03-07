/*
 * Created on 18/Set/2003, 19:16:29
 * 
 * By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.util.EMail;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 18/Set/2003, 19:16:29
 *  
 */
public class SendMail extends Service {

    private static final String mailServer = ResourceBundle.getBundle("SMTPConfiguration").getString("mailSender.server.url");

    public List run(List toList, List ccList, List bccList, String fromName, String from, String subject, String text) {
        return EMail.send(SendMail.mailServer, fromName, from, subject, toList, ccList, bccList, text);
    }

}