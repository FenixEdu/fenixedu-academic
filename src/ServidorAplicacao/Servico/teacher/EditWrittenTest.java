/*
 * Created on 18/Fev/2004
 *
 */
package ServidorAplicacao.Servico.teacher;

import org.apache.commons.beanutils.PropertyUtils;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoWrittenTest;
import DataBeans.util.Cloner;
import Dominio.IWrittenTest;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.IPersistentWrittenTest;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Luis Egidio, lmre@mega.ist.utl.pt
 * @author Nuno Ochoa,  nmgo@mega.ist.utl.pt
 *
 */
public class EditWrittenTest implements IService
{
	public EditWrittenTest()
	{

	}

	public void run(InfoWrittenTest infoWrittenTest) throws FenixServiceException
	{
		try
		{
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentWrittenTest persistentWrittenTest = sp.getIPersistentWrittenTest();

			IWrittenTest writtenTestToEdit = Cloner.copyInfoWrittenTest2IWrittenTest(infoWrittenTest);
			IWrittenTest writtenTestFromDatabase =
				(IWrittenTest) persistentWrittenTest.readByOId(writtenTestToEdit, false);
			if (writtenTestFromDatabase == null)
			{
				throw new NonExistingServiceException("Object doesn't exist!");
			}

			persistentWrittenTest.simpleLockWrite(writtenTestFromDatabase);

			Integer ackOptLock = writtenTestFromDatabase.getAckOptLock();
			PropertyUtils.copyProperties(writtenTestFromDatabase, writtenTestToEdit);
			writtenTestFromDatabase.setAckOptLock(ackOptLock);
		}
		catch (Exception e)
		{
			if (e instanceof FenixServiceException)
			{
				throw (FenixServiceException) e;
			}
			throw new FenixServiceException(e);
		}
	}
}