package ServidorAplicacao.Servico.degreeAdministrativeOffice.enrolment.withRules;

import Dominio.IStudent;
import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
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

public class GetUserViewFromStudentNumberAndDegreeType implements IServico {

	private static GetUserViewFromStudentNumberAndDegreeType _servico = new GetUserViewFromStudentNumberAndDegreeType();

	public static GetUserViewFromStudentNumberAndDegreeType getService() {
		return _servico;
	}

	private GetUserViewFromStudentNumberAndDegreeType() {
	}

	public final String getNome() {
		return "GetUserViewFromStudentNumberAndDegreeType";
	}

	public IUserView run(Integer degreeTypeInt, Integer studentNumber) throws FenixServiceException {

		IUserView userView = null;
		try {
			ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
			IPersistentStudent studentDAO = persistentSupport.getIPersistentStudent();
			TipoCurso degreeType = new TipoCurso(degreeTypeInt);
			IStudent student = studentDAO.readStudentByNumberAndDegreeType(studentNumber, degreeType);
			if(student != null) {
				userView = new UserView(student.getPerson().getUsername(), student.getPerson().getPersonRoles());
			}
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
		return userView;
	}
}