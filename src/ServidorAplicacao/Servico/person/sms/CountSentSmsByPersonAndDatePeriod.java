/*
 * Created on 7/Jun/2004
 *  
 */
package ServidorAplicacao.Servico.person.sms;

import java.util.Date;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.IPessoa;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.sms.SmsLimitReachedServiceException;
import ServidorAplicacao.utils.SmsUtil;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.sms.IPersistentSentSms;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 * <strong>Description: </strong> <br>
 * This service counts the number of sent Sms's by a person, in a date period
 * delimited by starDate(inclusive) and endDate(exclusive)
 *  
 */
public class CountSentSmsByPersonAndDatePeriod implements IService {

    /**
     *  
     */
    public CountSentSmsByPersonAndDatePeriod() {
    }

    public Integer run(UserView userView, Date startDate, Date endDate) throws FenixServiceException {

        try {
            ISuportePersistente ps = SuportePersistenteOJB.getInstance();
            IPersistentSentSms persistentSentSms = ps.getIPersistentSentSms();

            IPessoa person = ps.getIPessoaPersistente().lerPessoaPorUsername(userView.getUtilizador());

            Integer numberOfSms = persistentSentSms.countByPersonAndDatePeriod(person.getIdInternal(),
                    startDate, endDate);

            if (numberOfSms.intValue() >= SmsUtil.getInstance().getMonthlySmsLimit()) {
                throw new SmsLimitReachedServiceException("error.person.sendSmsLimitReached");
            }

            return new Integer(SmsUtil.getInstance().getMonthlySmsLimit() - numberOfSms.intValue());

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

    }

}