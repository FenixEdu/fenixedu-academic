package ServidorAplicacao.Servico.enrolment.degree;

import DataBeans.InfoCurricularCourse;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorAplicacao.strategy.enrolment.degree.InfoEnrolmentContext;

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
		return infoEnrolmentContext;
	}
}