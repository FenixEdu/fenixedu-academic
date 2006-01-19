/*
 * Created on 3/Set/2003, 15:35:33
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Servico.Seminaries;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoEquivalencyWithCurricularCourse;
import net.sourceforge.fenixedu.domain.Seminaries.CourseEquivalency;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminaryCurricularCourseEquivalency;
import net.sourceforge.fenixedu.presentationTier.Action.Seminaries.Exceptions.BDException;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 3/Set/2003, 15:35:33
 * 
 */
public class GetAllEquivalencies extends Service {

	public List run() throws BDException, ExcepcaoPersistencia {
		List infoEquivalencies = new LinkedList();

		ISuportePersistente persistenceSupport = PersistenceSupportFactory
				.getDefaultPersistenceSupport();
		IPersistentSeminaryCurricularCourseEquivalency persistentEquivalency = persistenceSupport
				.getIPersistentSeminaryCurricularCourseEquivalency();
		List equivalencies = persistentEquivalency.readAll();
		for (Iterator iterator = equivalencies.iterator(); iterator.hasNext();) {
			CourseEquivalency equivalency = (CourseEquivalency) iterator.next();

			infoEquivalencies.add(InfoEquivalencyWithCurricularCourse.newInfoFromDomain(equivalency));
		}

		return infoEquivalencies;
	}
}