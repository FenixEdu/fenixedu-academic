/*
 * Created on 9/Set/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.IExportGrouping;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExportGrouping;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
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
			ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
			IPersistentExportGrouping persistentExportGrouping = sp.getIPersistentExportGrouping();
			IExportGrouping groupPropertiesExecutionCourse = persistentExportGrouping.readBy(groupPropertiesId,executionCourseId);
			if(groupPropertiesExecutionCourse==null){
				result = false;
			}
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia.getMessage());
		}
		return result;
	}
	}