/*
 * Created on 18/Set/2003, 19:16:29
 * 
 * By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import pt.utl.ist.fenix.tools.smtp.EmailSender;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 18/Set/2003, 19:16:29
 *  
 */
public class SendMail extends Service {

    public Collection<String> run(List toList, List ccList, List bccList, String fromName, String from, String subject, String text) {
    	return EmailSender.send(fromName, from, toList, ccList, bccList, subject, text);
    }

}