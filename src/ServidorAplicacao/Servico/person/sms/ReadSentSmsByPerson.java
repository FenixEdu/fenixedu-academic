/*
 * Created on 11/Jun/2004
 *  
 */
package ServidorAplicacao.Servico.person.sms;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.sms.InfoSentSms;
import Dominio.IPessoa;
import Dominio.sms.ISentSms;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.sms.IPersistentSentSms;

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

            IPessoa person = ps.getIPessoaPersistente().lerPessoaPorUsername(userView.getUtilizador());

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