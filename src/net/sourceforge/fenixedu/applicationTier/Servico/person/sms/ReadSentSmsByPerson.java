/*
 * Created on 11/Jun/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.person.sms;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.sms.InfoSentSms;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.sms.ISentSms;
import net.sourceforge.fenixedu.applicationTier.Servico.UserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.persistenceTier.sms.IPersistentSentSms;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class ReadSentSmsByPerson implements IService {

    /**
     *  
     */
    public ReadSentSmsByPerson() {
    }

    public List run(UserView userView) throws FenixServiceException {

        try {
            ISuportePersistente ps = SuportePersistenteOJB.getInstance();
            IPersistentSentSms persistentSentSms = ps.getIPersistentSentSms();

            IPerson person = ps.getIPessoaPersistente().lerPessoaPorUsername(userView.getUtilizador());

            List infoSentSmsList = new ArrayList();

            List sentSmsList = persistentSentSms.readByPerson(person, new Integer(50));
            Iterator it = sentSmsList.iterator();
            ISentSms sentSms = null;

            while (it.hasNext()) {
                sentSms = (ISentSms) it.next();
                infoSentSmsList.add(InfoSentSms.copyFromDomain(sentSms));
            }

            return infoSentSmsList;

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

    }

}