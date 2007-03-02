/*
 * Created on 18/Set/2003, 19:16:29
 * 
 * By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.util.Email;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 18/Set/2003, 19:16:29
 *  
 */
public class SendMail extends Service {

    public Collection<String> run(List toList, List ccList, List bccList, String fromName, String from, String subject, String text) {
    	new Email(fromName, from, null, toList, ccList, bccList, subject, text);
    	return new ArrayList<String>();
    }

}