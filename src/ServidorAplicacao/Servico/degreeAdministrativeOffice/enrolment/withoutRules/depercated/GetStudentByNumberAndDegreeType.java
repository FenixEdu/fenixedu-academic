package ServidorAplicacao.Servico.degreeAdministrativeOffice.enrolment.withoutRules.depercated;

import DataBeans.InfoStudent;
import DataBeans.util.Cloner;
import Dominio.IStudent;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoCurso;

/**
 * @author David Santos
 * 10/Jun/2003
 */

public class GetStudentByNumberAndDegreeType implements IServico {

	private static GetStudentByNumberAndDegreeType _servico = new GetStudentByNumberAndDegreeType();

	public static GetStudentByNumberAndDegreeType getService() {
		return _servico;
	}

	private GetStudentByNumberAndDegreeType() {
	}

	public final String getNome() {
		return "GetStudentByNumberAndDegreeType";
	}

	public InfoStudent run(Integer degreeTypeInt, Integer studentNumber) throws FenixServiceException {

		InfoStudent student = null;
		try {
			ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
			IPersistentStudent studentDAO = persistentSupport.getIPersistentStudent();
			TipoCurso degreeType = new TipoCurso(degreeTypeInt);
			IStudent student2 = studentDAO.readStudentByNumberAndDegreeType(studentNumber, degreeType);
			if(student2 != null) {
				student = Cloner.copyIStudent2InfoStudent(student2);
			}
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
		return student;
	}
}