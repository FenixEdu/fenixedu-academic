/*
 * Created on 9/Set/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.domain.IGroupPropertiesExecutionCourse;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGroupPropertiesExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import pt.utl.ist.berserk.logic.serviceManager.IService;

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
			IGroupPropertiesExecutionCourse groupPropertiesExecutionCourse = persistentGroupPropertiesExecutionCourse.readByIDs(groupPropertiesId,executionCourseId);
			if(groupPropertiesExecutionCourse==null){
				result = false;
			}
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia.getMessage());
		}
		return result;
	}
	}