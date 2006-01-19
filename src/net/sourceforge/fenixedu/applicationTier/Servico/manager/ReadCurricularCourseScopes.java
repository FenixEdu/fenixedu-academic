/*
 * Created on 19/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScopeWithBranchAndSemesterAndYear;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * @author lmac1
 */
public class ReadCurricularCourseScopes implements IService {

	/**
	 * Executes the service. Returns the current collection of
	 * infoCurricularCourseScopes.
	 * 
	 * @throws ExcepcaoPersistencia
	 */
	public List run(Integer curricularCourseId) throws FenixServiceException, ExcepcaoPersistencia {
		ISuportePersistente sp;
		List allCurricularCourseScopes = null;

		sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		CurricularCourse curricularCourse = (CurricularCourse) sp.getIPersistentCurricularCourse()
				.readByOID(CurricularCourse.class, curricularCourseId);
		allCurricularCourseScopes = curricularCourse.getScopes();

		if (allCurricularCourseScopes == null || allCurricularCourseScopes.isEmpty())
			return new ArrayList();

		Iterator iterator = allCurricularCourseScopes.iterator();
		List result = new ArrayList(allCurricularCourseScopes.size());

		while (iterator.hasNext())
			result.add(InfoCurricularCourseScopeWithBranchAndSemesterAndYear
					.newInfoFromDomain((CurricularCourseScope) iterator.next()));

		return result;
	}
}