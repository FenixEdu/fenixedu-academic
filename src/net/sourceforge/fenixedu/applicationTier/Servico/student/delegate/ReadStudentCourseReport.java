/*
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student.delegate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.gesdis.InfoSiteEvaluationStatistics;
import net.sourceforge.fenixedu.dataTransferObject.student.InfoSiteStudentCourseReport;
import net.sourceforge.fenixedu.dataTransferObject.student.InfoStudentCourseReport;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.domain.gesdis.StudentCourseReport;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

/**
 * @author <a href="mailto:lesa@mega.ist.utl.pt">Leonor Almeida </a>
 * @author <a href="mailto:shmc@mega.ist.utl.pt">Sergio Montelobo </a>
 * 
 */
public class ReadStudentCourseReport extends Service {

	public InfoSiteStudentCourseReport run(Integer objectId) throws FenixServiceException, ExcepcaoPersistencia {

		final CurricularCourse curricularCourse = (CurricularCourse) rootDomainObject.readDegreeModuleByOID(objectId);
		StudentCourseReport studentCourseReport = curricularCourse.getStudentCourseReport();

		List infoScopes = (List) CollectionUtils.collect(curricularCourse.getScopes(),
				new Transformer() {

					public Object transform(Object arg0) {
						CurricularCourseScope curricularCourseScope = (CurricularCourseScope) arg0;
						return InfoCurricularCourseScope
								.newInfoFromDomain(curricularCourseScope);
					}

				});

		InfoStudentCourseReport infoStudentCourseReport = null;
		if (studentCourseReport == null) {
			infoStudentCourseReport = new InfoStudentCourseReport();
			InfoCurricularCourse infoCurricularCourse = InfoCurricularCourse
					.newInfoFromDomain(curricularCourse);
			infoCurricularCourse.setInfoScopes(infoScopes);
			infoStudentCourseReport.setInfoCurricularCourse(infoCurricularCourse);
		} else {
			infoStudentCourseReport = InfoStudentCourseReport.newInfoFromDomain(studentCourseReport);
			InfoCurricularCourse infoCurricularCourse = infoStudentCourseReport
					.getInfoCurricularCourse();
			infoCurricularCourse.setInfoScopes(infoScopes);
		}

		InfoSiteStudentCourseReport infoSiteStudentCourseReport = new InfoSiteStudentCourseReport();
		ExecutionPeriod actualPeriod = ExecutionPeriod.readActualExecutionPeriod();
		ExecutionPeriod executionPeriod = actualPeriod.getPreviousExecutionPeriod();

		List infoSiteEvaluationHistory = getInfoSiteEvaluationsHistory(executionPeriod, curricularCourse);

		InfoSiteEvaluationStatistics infoSiteEvaluationStatistics = getInfoSiteEvaluationStatistics(
				executionPeriod, curricularCourse);

		infoSiteStudentCourseReport.setInfoStudentCourseReport(infoStudentCourseReport);
		infoSiteStudentCourseReport.setInfoSiteEvaluationHistory(infoSiteEvaluationHistory);
		infoSiteStudentCourseReport.setInfoSiteEvaluationStatistics(infoSiteEvaluationStatistics);

		return infoSiteStudentCourseReport;
	}

	private InfoSiteEvaluationStatistics getInfoSiteEvaluationStatistics(
			ExecutionPeriod executionPeriod, CurricularCourse curricularCourse) {

		InfoSiteEvaluationStatistics infoSiteEvaluationStatistics = new InfoSiteEvaluationStatistics();
		List<Enrolment> enrolled = curricularCourse.getEnrolmentsByExecutionPeriod(executionPeriod);
		infoSiteEvaluationStatistics.setEnrolled(new Integer(enrolled.size()));
		infoSiteEvaluationStatistics.setEvaluated(getEvaluated(enrolled));
		infoSiteEvaluationStatistics.setApproved(getApproved(enrolled));
		InfoExecutionPeriod infoExecutionPeriod = InfoExecutionPeriod.newInfoFromDomain(executionPeriod);
		infoSiteEvaluationStatistics.setInfoExecutionPeriod(infoExecutionPeriod);

		return infoSiteEvaluationStatistics;
	}

	private List getInfoSiteEvaluationsHistory(ExecutionPeriod executionPeriodToTest,
			CurricularCourse curricularCourse) {
		List infoSiteEvaluationsHistory = new ArrayList();
		List executionPeriods = (List) CollectionUtils.collect(curricularCourse
				.getAssociatedExecutionCourses(), new Transformer() {
			public Object transform(Object arg0) {
				ExecutionCourse executionCourse = (ExecutionCourse) arg0;
				return executionCourse.getExecutionPeriod();
			}

		});
		// filter the executionPeriods by semester
		// also, information regarding execution years after the course's
		// execution year must not be shown
		final ExecutionPeriod historyExecutionPeriod = executionPeriodToTest;
		executionPeriods = (List) CollectionUtils.select(executionPeriods, new Predicate() {
			public boolean evaluate(Object arg0) {
				ExecutionPeriod executionPeriod = (ExecutionPeriod) arg0;
				return (executionPeriod.getSemester().equals(historyExecutionPeriod.getSemester()) && executionPeriod
						.getExecutionYear().getBeginDate().before(
								historyExecutionPeriod.getExecutionYear().getBeginDate()));
			}
		});
		Collections.sort(executionPeriods, new Comparator() {
			public int compare(Object o1, Object o2) {
				ExecutionPeriod executionPeriod1 = (ExecutionPeriod) o1;
				ExecutionPeriod executionPeriod2 = (ExecutionPeriod) o2;
				return executionPeriod1.getExecutionYear().getYear().compareTo(
						executionPeriod2.getExecutionYear().getYear());
			}
		});
		Iterator iter = executionPeriods.iterator();
		while (iter.hasNext()) {
			ExecutionPeriod executionPeriod = (ExecutionPeriod) iter.next();

			InfoSiteEvaluationStatistics infoSiteEvaluationStatistics = new InfoSiteEvaluationStatistics();
			infoSiteEvaluationStatistics.setInfoExecutionPeriod(InfoExecutionPeriod
					.newInfoFromDomain(executionPeriod));
			List<Enrolment> enrolled = curricularCourse.getEnrolmentsByExecutionPeriod(executionPeriod);
			infoSiteEvaluationStatistics.setEnrolled(new Integer(enrolled.size()));
			infoSiteEvaluationStatistics.setEvaluated(getEvaluated(enrolled));
			infoSiteEvaluationStatistics.setApproved(getApproved(enrolled));
			infoSiteEvaluationsHistory.add(infoSiteEvaluationStatistics);
		}

		return infoSiteEvaluationsHistory;
	}

	private Integer getApproved(List enrolments) {
		int approved = 0;
		Iterator iter = enrolments.iterator();
		while (iter.hasNext()) {
			Enrolment enrolment = (Enrolment) iter.next();
			EnrollmentState enrollmentState = enrolment.getEnrollmentState();
			if (enrollmentState.equals(EnrollmentState.APROVED)) {
				approved++;
			}
		}
		return Integer.valueOf(approved);
	}

	private Integer getEvaluated(List enrolments) {
		int evaluated = 0;
		Iterator iter = enrolments.iterator();
		while (iter.hasNext()) {
			Enrolment enrolment = (Enrolment) iter.next();
			EnrollmentState enrollmentState = enrolment.getEnrollmentState();
			if (enrollmentState.equals(EnrollmentState.APROVED)
					|| enrollmentState.equals(EnrollmentState.NOT_APROVED)) {
				evaluated++;
			}
		}
		return Integer.valueOf(evaluated);
	}
}