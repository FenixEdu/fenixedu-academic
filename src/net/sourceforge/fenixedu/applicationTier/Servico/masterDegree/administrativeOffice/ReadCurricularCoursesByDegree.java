package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourseScope;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Fernanda Quitério 01/07/2003
 *  
 */
public class ReadCurricularCoursesByDegree implements IService {

	public List run(String executionYearString, String degreeName)
			throws FenixServiceException {
		List infoCurricularCourses = null;
		try {
			ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

			IExecutionYear executionYear = sp.getIPersistentExecutionYear()
					.readExecutionYearByName(executionYearString);

			// Read degree
			IExecutionDegree cursoExecucao = sp.getIPersistentExecutionDegree()
					.readByDegreeCurricularPlanNameAndExecutionYear(degreeName,
							executionYear);

			if (cursoExecucao == null
					|| cursoExecucao.getDegreeCurricularPlan() == null
					|| cursoExecucao.getDegreeCurricularPlan().getCurricularCourses() == null
					|| cursoExecucao.getDegreeCurricularPlan().getCurricularCourses()
							.size() == 0) {
				throw new NonExistingServiceException();
			}

			infoCurricularCourses = new ArrayList();
			ListIterator iterator = cursoExecucao.getDegreeCurricularPlan()
					.getCurricularCourses().listIterator();
			while (iterator.hasNext()) {
				ICurricularCourse curricularCourse = (ICurricularCourse) iterator
						.next();

				InfoCurricularCourse infoCurricularCourse = Cloner
						.copyCurricularCourse2InfoCurricularCourse(curricularCourse);
				infoCurricularCourse.setInfoScopes((List) CollectionUtils
						.collect(curricularCourse.getScopes(),
								new Transformer() {

									public Object transform(Object arg0) {
										ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) arg0;
										return Cloner
												.copyICurricularCourseScope2InfoCurricularCourseScope(curricularCourseScope);
									}
								}));

				infoCurricularCourses.add(infoCurricularCourse);
			}

		} catch (ExcepcaoPersistencia ex) {
			FenixServiceException newEx = new FenixServiceException(
					"Persistence layer error");
			newEx.fillInStackTrace();
			throw newEx;
		}

		return infoCurricularCourses;
	}

	public List run(Integer degreeCurricularPlanID)
			throws FenixServiceException {
		List infoCurricularCourses = null;
		try {
			ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

			IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) sp
					.getIPersistentDegreeCurricularPlan().readByOID(
							DegreeCurricularPlan.class, degreeCurricularPlanID);

			infoCurricularCourses = new ArrayList();
			ListIterator iterator = degreeCurricularPlan.getCurricularCourses()
					.listIterator();
			while (iterator.hasNext()) {
				ICurricularCourse curricularCourse = (ICurricularCourse) iterator
						.next();

				InfoCurricularCourse infoCurricularCourse = Cloner
						.copyCurricularCourse2InfoCurricularCourse(curricularCourse);
				infoCurricularCourse.setInfoScopes((List) CollectionUtils
						.collect(curricularCourse.getScopes(),
								new Transformer() {

									public Object transform(Object arg0) {
										ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) arg0;
										return Cloner
												.copyICurricularCourseScope2InfoCurricularCourseScope(curricularCourseScope);
									}
								}));

				infoCurricularCourses.add(infoCurricularCourse);
			}

		} catch (ExcepcaoPersistencia ex) {
			FenixServiceException newEx = new FenixServiceException(
					"Persistence layer error");
			newEx.fillInStackTrace();
			throw newEx;
		}

		return infoCurricularCourses;
	}

}