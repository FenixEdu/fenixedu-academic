/*
 * Created on 17/Mar/2003
 *
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.gesdis.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoBibliographicReference;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.BibliographicReference;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentBibliographicReference;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.exceptions.ExistingPersistentException;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author PTRLV
 * 
 * 
 */
public class ReadBibliographicReference extends Service {

	public List run(InfoExecutionCourse infoExecutionCourse, Boolean optional)
			throws FenixServiceException, ExcepcaoPersistencia {
		List references = null;
		List infoBibRefs = null;
		try {
			ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
			IPersistentExecutionYear persistentExecutionYear = sp.getIPersistentExecutionYear();
			IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();
			IPersistentBibliographicReference persistentBibliographicReference = persistentBibliographicReference = sp
					.getIPersistentBibliographicReference();
			IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();

			ExecutionYear executionYear = persistentExecutionYear
					.readExecutionYearByName(infoExecutionCourse.getInfoExecutionPeriod()
							.getInfoExecutionYear().getYear());
			ExecutionPeriod executionPeriod = persistentExecutionPeriod.readByNameAndExecutionYear(
					infoExecutionCourse.getInfoExecutionPeriod().getName(), executionYear.getYear());
			ExecutionCourse executionCourse = persistentExecutionCourse
					.readByExecutionCourseInitialsAndExecutionPeriodId(infoExecutionCourse.getSigla(),
							executionPeriod.getIdInternal());
			references = persistentBibliographicReference.readBibliographicReference(executionCourse
					.getIdInternal());

			Iterator iterator = references.iterator();
			infoBibRefs = new ArrayList();
			while (iterator.hasNext()) {
				BibliographicReference bibRef = (BibliographicReference) iterator.next();

				if (optional != null) {
					if (bibRef.getOptional().equals(optional)) {
						InfoBibliographicReference infoBibRef = InfoBibliographicReference
								.newInfoFromDomain(bibRef);
						infoBibRefs.add(infoBibRef);
					}
				} else {
					InfoBibliographicReference infoBibRef = InfoBibliographicReference
							.newInfoFromDomain(bibRef);
					infoBibRefs.add(infoBibRef);
				}
			}

		} catch (ExistingPersistentException e) {
			throw new ExistingServiceException(e);
		}
		return infoBibRefs;
	}
}