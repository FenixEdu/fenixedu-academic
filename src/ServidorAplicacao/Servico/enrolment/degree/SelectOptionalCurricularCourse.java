package ServidorAplicacao.Servico.enrolment.degree;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import DataBeans.InfoCurricularCourse;
import DataBeans.InfoEnrolmentInOptionalCurricularCourse;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorAplicacao.strategy.enrolment.degree.InfoEnrolmentContext;
import Util.EnrolmentState;

/**
 * @author dcs-rjao
 *
 * 9/Abr/2003
 */
public class SelectOptionalCurricularCourse implements IServico {

	private static SelectOptionalCurricularCourse _servico = new SelectOptionalCurricularCourse();
	/**
	 * The singleton access method of this class.
	 **/
	public static SelectOptionalCurricularCourse getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private SelectOptionalCurricularCourse() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "SelectOptionalCurricularCourse";
	}

	/**
	 * @param infoStudent
	 * @param infoDegree
	 * @return List
	 * @throws FenixServiceException
	 */
	public InfoEnrolmentContext run(InfoEnrolmentContext infoEnrolmentContext, InfoCurricularCourse infoCurricularCourse) throws FenixServiceException {

		InfoEnrolmentInOptionalCurricularCourse infoEnrolment = new InfoEnrolmentInOptionalCurricularCourse();
		infoEnrolment.setInfoCurricularCourse(infoEnrolmentContext.getInfoChosenOptionalCurricularCourseScope().getInfoCurricularCourse());
		infoEnrolment.setInfoExecutionPeriod(infoEnrolmentContext.getInfoExecutionPeriod());
		infoEnrolment.setInfoStudentCurricularPlan(infoEnrolmentContext.getInfoStudentActiveCurricularPlan());
		infoEnrolment.setState(new EnrolmentState(EnrolmentState.TEMPORARILY_ENROLED));
		infoEnrolment.setInfoCurricularCourseForOption(infoCurricularCourse);

		// For one optional curricular course chosen there can be only one coresponding curricular course.
		final InfoCurricularCourse infoCurricularCourseChosen = infoEnrolmentContext.getInfoChosenOptionalCurricularCourseScope().getInfoCurricularCourse();
		List enrolmentsList = (List) CollectionUtils.select(infoEnrolmentContext.getInfoOptionalCurricularCoursesEnrolments(), new Predicate() {
			public boolean evaluate(Object obj) {
				InfoEnrolmentInOptionalCurricularCourse infoEnrolmentInOptionalCurricularCourse = (InfoEnrolmentInOptionalCurricularCourse) obj;
				return infoEnrolmentInOptionalCurricularCourse.getInfoCurricularCourse().equals(infoCurricularCourseChosen);
			}
		});

		// There for the substituition is done.
		if (!enrolmentsList.isEmpty()) {
			infoEnrolmentContext.getInfoOptionalCurricularCoursesEnrolments().remove(enrolmentsList.get(0));
		}
		infoEnrolmentContext.getInfoOptionalCurricularCoursesEnrolments().add(infoEnrolment);

		// Automaticaly add the enrolment in the chosen curricular course to the actual enrolments list for this student.
		if (!infoEnrolmentContext.getActualEnrolment().contains(infoEnrolmentContext.getInfoChosenOptionalCurricularCourseScope())) {
			infoEnrolmentContext.getActualEnrolment().add(infoEnrolmentContext.getInfoChosenOptionalCurricularCourseScope());
		}
		return infoEnrolmentContext;
	}
}