/*
 * Created on 9/Set/2004
 *
 */
package ServidorAplicacao.Servico.teacher;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.IGroupPropertiesExecutionCourse;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentGroupPropertiesExecutionCourse;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author joaosa & rmalo
 *  
 */
public class VerifyIfGroupPropertiesHasProjectProposal implements IService
{

	public VerifyIfGroupPropertiesHasProjectProposal()
	{

	}

	public boolean run(Integer executionCourseId,Integer groupPropertiesId)throws FenixServiceException{
		boolean result = true;
		try{
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentGroupPropertiesExecutionCourse persistentGroupPropertiesExecutionCourse = sp.getIPersistentGroupPropertiesExecutionCourse();
			IGroupPropertiesExecutionCourse groupPropertiesExecutionCourse = (IGroupPropertiesExecutionCourse) persistentGroupPropertiesExecutionCourse.readByIDs(groupPropertiesId,executionCourseId);
			if(groupPropertiesExecutionCourse==null){
				result = false;
			}
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia.getMessage());
		}
		return result;
	}
	}