package ServidorAplicacao.Servico.enrolment.degree;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import DataBeans.InfoCurricularCourse;
import DataBeans.InfoEnrolment;
import DataBeans.InfoEquivalence;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorAplicacao.strategy.enrolment.degree.InfoEnrolmentContext;
import Util.EnrolmentState;
import Util.EquivalenceType;

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
	public InfoEnrolmentContext run(InfoEnrolmentContext infoEnrolmentContext, InfoCurricularCourse infoCurricularCourse)
		throws FenixServiceException {

		InfoEnrolment infoEnrolmentEquivalent = new InfoEnrolment();
		infoEnrolmentEquivalent.setInfoCurricularCourse(infoCurricularCourse);

		infoEnrolmentEquivalent.setInfoExecutionPeriod(infoEnrolmentContext.getInfoExecutionPeriod());

		infoEnrolmentEquivalent.setInfoStudentCurricularPlan(infoEnrolmentContext.getInfoStudentActiveCurricularPlan());
		infoEnrolmentEquivalent.setState(new EnrolmentState(EnrolmentState.TEMPORARILY_ENROLED));

		InfoEnrolment infoEnrolment = new InfoEnrolment();
		infoEnrolment.setInfoCurricularCourse(infoEnrolmentContext.getInfoChosenOptionalCurricularCourseScope().getInfoCurricularCourse());

		//FIXME David-Ricardo: o execution period tera de vir no contexto
		infoEnrolment.setInfoExecutionPeriod(infoEnrolmentContext.getInfoExecutionPeriod());
		infoEnrolment.setInfoStudentCurricularPlan(infoEnrolmentContext.getInfoStudentActiveCurricularPlan());
		infoEnrolmentEquivalent.setState(new EnrolmentState(EnrolmentState.TEMPORARILY_ENROLED));

		InfoEquivalence infoEquivalence = new InfoEquivalence();
		infoEquivalence.setInfoEnrolment(infoEnrolment);
		infoEquivalence.setInfoEquivalentEnrolment(infoEnrolmentEquivalent);
		infoEquivalence.setEquivalenceType(new EquivalenceType(EquivalenceType.OPTIONAL_COURSE));

		final InfoCurricularCourse infoCurricularCourseEnrolment =
			infoEnrolmentContext.getInfoChosenOptionalCurricularCourseScope().getInfoCurricularCourse();
		List equivalenceList = (List) CollectionUtils.select(infoEnrolmentContext.getInfoOptionalCurricularCoursesEquivalences(), new Predicate() {
			public boolean evaluate(Object obj) {
				InfoEquivalence infoEquivalence = (InfoEquivalence) obj;
				return infoEquivalence.getInfoEnrolment().getInfoCurricularCourse().equals(infoCurricularCourseEnrolment);
			}
		});

		if (!equivalenceList.isEmpty()) {
			infoEnrolmentContext.getInfoOptionalCurricularCoursesEquivalences().remove(equivalenceList.get(0));
		}
		infoEnrolmentContext.getInfoOptionalCurricularCoursesEquivalences().add(infoEquivalence);

		if (!infoEnrolmentContext.getActualEnrolment().contains(infoEnrolmentContext.getInfoChosenOptionalCurricularCourseScope())) {
			infoEnrolmentContext.getActualEnrolment().add(infoEnrolmentContext.getInfoChosenOptionalCurricularCourseScope());
		}
		return infoEnrolmentContext;
	}
}