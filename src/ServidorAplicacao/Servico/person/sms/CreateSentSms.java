/*
 * Created on 11/Jun/2004
 *  
 */
package ServidorAplicacao.Servico.person.sms;

import java.util.Calendar;
import java.util.Date;

import Dominio.IPessoa;
import Dominio.sms.ISentSms;
import Dominio.sms.SentSms;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.sms.SmsLimitReachedServiceException;
import ServidorAplicacao.Servico.exceptions.sms.SmsNotSentServiceException;
import ServidorAplicacao.utils.SmsUtil;
import ServidorAplicacao.utils.exceptions.FenixUtilException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.sms.IPersistentSentSms;
import Util.SmsDeliveryType;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali</a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed</a>
 *  
 */
public class CreateSentSms implements IService
{

	/**
	 *  
	 */
	public CreateSentSms()
	{
	}

	public void run(
		UserView userView,
		Date startDate,
		Date endDate,
		Integer destinationNumber,
		String message)
		throws FenixServiceException
	{

		try
		{
			ISuportePersistente ps = SuportePersistenteOJB.getInstance();
			IPersistentSentSms persistentSentSms = ps.getIPersistentSentSms();

			IPessoa person = ps.getIPessoaPersistente().lerPessoaPorUsername(userView.getUtilizador());

			//count number of sent SMS's by this person
			Integer numberOfSms =
				persistentSentSms.countByPersonAndDatePeriod(person.getIdInternal(), startDate, endDate);

			if (numberOfSms.intValue() >= SmsUtil.getInstance().getMonthlySmsLimit())
			{
				throw new SmsLimitReachedServiceException("error.person.sendSmsLimitReached");
			}

			//create sentSms Object
			ISentSms sentSms = new SentSms();
			persistentSentSms.simpleLockWrite(sentSms);

			sentSms.setDestinationNumber(destinationNumber);
			sentSms.setPerson(person);
			sentSms.setSendDate(Calendar.getInstance().getTime());
			sentSms.setDeliveryDate(Calendar.getInstance().getTime());
			sentSms.setDeliveryType(SmsDeliveryType.NOT_SENT);

			//send SMS
			try
			{
				SmsUtil.getInstance().sendSms(destinationNumber, message, sentSms.getIdInternal());

			}
			catch (FenixUtilException e1)
			{
				throw new SmsNotSentServiceException("error.person.sendSms");
			}

			sentSms.setDeliveryType(SmsDeliveryType.SMSC_SUBMIT);

		}
		catch (ExcepcaoPersistencia e)
		{
			throw new FenixServiceException(e);
		}

	}

}
