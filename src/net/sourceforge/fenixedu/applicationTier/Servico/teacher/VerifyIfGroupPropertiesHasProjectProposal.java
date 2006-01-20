/*
 * Created on 9/Set/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExportGrouping;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExportGrouping;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author joaosa & rmalo
 * 
 */
public class VerifyIfGroupPropertiesHasProjectProposal extends Service {

	public boolean run(Integer executionCourseId, Integer groupPropertiesId)
			throws FenixServiceException, ExcepcaoPersistencia {
		boolean result = true;

		ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IPersistentExportGrouping persistentExportGrouping = persistentSupport.getIPersistentExportGrouping();
		ExportGrouping groupPropertiesExecutionCourse = persistentExportGrouping.readBy(
				groupPropertiesId, executionCourseId);
		if (groupPropertiesExecutionCourse == null) {
			result = false;
		}

		return result;
	}
}