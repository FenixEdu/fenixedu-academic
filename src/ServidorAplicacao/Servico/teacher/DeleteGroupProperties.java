/*
 * Created on 2/Abr/2004
 *
 */
package ServidorAplicacao.Servico.teacher;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.GroupProperties;
import Dominio.IGroupProperties;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentGroupProperties;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Tânia Pousão
 *  
 */
public class DeleteGroupProperties implements IService
{

	public DeleteGroupProperties()
	{

	}

	public Boolean run(Integer executionCourseId, Integer groupPropertiesId)
			throws FenixServiceException
	{

		Boolean result = Boolean.FALSE;

		if (groupPropertiesId == null)
		{
			return result;
		}

		try
		{

			ISuportePersistente sp = (ISuportePersistente) SuportePersistenteOJB.getInstance();
			IPersistentGroupProperties persistentGroupProperties = sp.getIPersistentGroupProperties();

			IGroupProperties groupProperties = (IGroupProperties) persistentGroupProperties.readByOID(GroupProperties.class,
			        groupPropertiesId);

			if (groupProperties != null)
			{
				persistentGroupProperties.deleteByOID(GroupProperties.class, groupPropertiesId);
			}

			result = Boolean.TRUE;
		} catch (ExcepcaoPersistencia e)
		{
			e.printStackTrace();
			throw new FenixServiceException("error.groupProperties.delete");
		}

		return result;
	}
}
