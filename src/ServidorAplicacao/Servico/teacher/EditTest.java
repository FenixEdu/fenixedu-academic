/*
 * Created on 1/Ago/2003
 *
 */

package ServidorAplicacao.Servico.teacher;

import java.util.Calendar;
import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.ITest;
import Dominio.Test;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentTest;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Susana Fernandes
 */
public class EditTest implements IService
{
	public EditTest()
	{
	}

	public boolean run(Integer executionCourseId, Integer testId, String title, String information)
			throws FenixServiceException
	{
		try
		{
			ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
			IPersistentTest persistentTest = persistentSuport.getIPersistentTest();
			ITest test = new Test(testId);
			test = (ITest) persistentTest.readByOId(test, true);
			test.setTitle(title);
			test.setInformation(information);
			test.setLastModifiedDate(Calendar.getInstance().getTime());
			return true;
		}
		catch (ExcepcaoPersistencia e)
		{
			throw new FenixServiceException(e);
		}
	}
}
