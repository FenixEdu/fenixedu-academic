package ServidorAplicacao.Servico.enrolment.degree;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import DataBeans.InfoCurricularCourse;
import DataBeans.InfoCurricularCourseScope;
import DataBeans.InfoEnrolmentInOptionalCurricularCourse;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.strategy.enrolment.context.InfoEnrolmentContext;
import Util.CurricularCourseType;
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
	public InfoEnrolmentContext run(InfoEnrolmentContext infoEnrolmentContext, InfoCurricularCourse infoCurricularCourseForOption) throws FenixServiceException {

		if(infoCurricularCourseForOption == null){
			throw new FenixServiceException("Curricular Course For Option cannot be null!");
		}
		if(infoEnrolmentContext.getInfoChosenOptionalCurricularCourseScope().getInfoCurricularCourse().getType().equals(new CurricularCourseType(CurricularCourseType.OPTIONAL_COURSE))) {
			InfoEnrolmentInOptionalCurricularCourse infoEnrolment = new InfoEnrolmentInOptionalCurricularCourse();
//			infoEnrolment.setInfoCurricularCourse(infoEnrolmentContext.getInfoChosenOptionalCurricularCourseScope().getInfoCurricularCourse());
			infoEnrolment.setInfoCurricularCourseScope(infoEnrolmentContext.getInfoChosenOptionalCurricularCourseScope());
			infoEnrolment.setInfoExecutionPeriod(infoEnrolmentContext.getInfoExecutionPeriod());
			infoEnrolment.setInfoStudentCurricularPlan(infoEnrolmentContext.getInfoStudentActiveCurricularPlan());
			infoEnrolment.setEnrolmentState(EnrolmentState.TEMPORARILY_ENROLED);
			infoEnrolment.setInfoCurricularCourseForOption(infoCurricularCourseForOption);

			// For one optional curricular course chosen there can be only one coresponding curricular course.
//			final InfoCurricularCourse infoCurricularCourseChosen = infoEnrolmentContext.getInfoChosenOptionalCurricularCourseScope().getInfoCurricularCourse();
			final InfoCurricularCourseScope infoCurricularCourseScopeChosen = infoEnrolmentContext.getInfoChosenOptionalCurricularCourseScope();
			List enrolmentsList = (List) CollectionUtils.select(infoEnrolmentContext.getInfoOptionalCurricularCoursesEnrolments(), new Predicate() {
				public boolean evaluate(Object obj) {
					InfoEnrolmentInOptionalCurricularCourse infoEnrolmentInOptionalCurricularCourse = (InfoEnrolmentInOptionalCurricularCourse) obj;
//					return infoEnrolmentInOptionalCurricularCourse.getInfoCurricularCourse().equals(infoCurricularCourseChosen);
					return infoEnrolmentInOptionalCurricularCourse.getInfoCurricularCourseScope().equals(infoCurricularCourseScopeChosen);
				}
			});

			// There for the substituition is done.
			if (!enrolmentsList.isEmpty()) {
				infoEnrolmentContext.getInfoOptionalCurricularCoursesEnrolments().remove(enrolmentsList.get(0));
			}
			infoEnrolmentContext.getInfoOptionalCurricularCoursesEnrolments().add(infoEnrolment);
		} else {
			throw new FenixServiceException("Cannot associate a curricular course to a non optional curricular course!");
		}
		return infoEnrolmentContext;
	}
}