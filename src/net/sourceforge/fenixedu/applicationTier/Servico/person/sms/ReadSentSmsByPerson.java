/*
 * Created on 11/Jun/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.person.sms;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.sms.InfoSentSms;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.sms.SentSms;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */
public class ReadSentSmsByPerson extends Service {

	public List<InfoSentSms> run(IUserView userView) {
		final Person person = Person.readPersonByUsername(userView.getUtilizador());
        final List<InfoSentSms> infoSentSmsList = new ArrayList<InfoSentSms>();
        for (final SentSms sentSms : person.getSentSmsSortedBySendDate()) {
            infoSentSmsList.add(InfoSentSms.copyFromDomain(sentSms));
        }
		return infoSentSmsList;
	}

}