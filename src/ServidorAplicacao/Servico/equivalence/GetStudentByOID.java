package ServidorAplicacao.Servico.equivalence;

import DataBeans.InfoStudent;
import DataBeans.util.Cloner;
import Dominio.IStudent;
import Dominio.Student;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author David Santos
 * 9/Jul/2003
 */

public class GetStudentByOID implements IServico {

	private static GetStudentByOID service = new GetStudentByOID();

	public static GetStudentByOID getService() {
		return GetStudentByOID.service;
	}

	private GetStudentByOID() {
	}

	public final String getNome() {
		return "GetStudentByOID";
	}

	public InfoStudent run(Integer studentOID) throws FenixServiceException {
		try {
			ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
			IPersistentStudent persistentStudent = persistentSupport.getIPersistentStudent();
			IStudent studentCriteria = new Student();
			studentCriteria.setIdInternal(studentOID);
			IStudent student = (IStudent) persistentStudent.readByOId(studentCriteria, false);
			return Cloner.copyIStudent2InfoStudent(student);
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
	}
}