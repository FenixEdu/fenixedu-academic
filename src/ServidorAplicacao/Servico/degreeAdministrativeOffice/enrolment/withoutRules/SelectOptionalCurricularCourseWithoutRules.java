package ServidorAplicacao.Servico.degreeAdministrativeOffice.enrolment.withoutRules;

import DataBeans.InfoCurricularCourse;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.enrolment.degree.SelectOptionalCurricularCourse;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.strategy.enrolment.context.InfoEnrolmentContext;

/**
 * @author David Santos
 */

public class SelectOptionalCurricularCourseWithoutRules implements IServico {

	private static SelectOptionalCurricularCourseWithoutRules _servico = new SelectOptionalCurricularCourseWithoutRules();

	public static SelectOptionalCurricularCourseWithoutRules getService() {
		return _servico;
	}

	private SelectOptionalCurricularCourseWithoutRules() {
	}

	public final String getNome() {
		return "SelectOptionalCurricularCourseWithoutRules";
	}

	public InfoEnrolmentContext run(InfoEnrolmentContext infoEnrolmentContext, InfoCurricularCourse infoCurricularCourseForOption) throws FenixServiceException {
		SelectOptionalCurricularCourse service = SelectOptionalCurricularCourse.getService();
		return service.run(infoEnrolmentContext, infoCurricularCourseForOption);
	}
}