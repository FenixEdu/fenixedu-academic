package ServidorAplicacao.Servico.degreeAdministrativeOffice.enrolment.withRules;

import DataBeans.InfoCurricularCourse;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.enrolment.degree.SelectOptionalCurricularCourse;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.strategy.enrolment.context.InfoEnrolmentContext;

/**
 * @author David Santos
 */

public class SelectOptionalCurricularCourseWithRules implements IServico {

	private static SelectOptionalCurricularCourseWithRules _servico = new SelectOptionalCurricularCourseWithRules();

	public static SelectOptionalCurricularCourseWithRules getService() {
		return _servico;
	}

	private SelectOptionalCurricularCourseWithRules() {
	}

	public final String getNome() {
		return "SelectOptionalCurricularCourseWithRules";
	}

	public InfoEnrolmentContext run(InfoEnrolmentContext infoEnrolmentContext, InfoCurricularCourse infoCurricularCourseForOption) throws FenixServiceException {
		SelectOptionalCurricularCourse service = SelectOptionalCurricularCourse.getService();
		return service.run(infoEnrolmentContext, infoCurricularCourseForOption);
	}
}