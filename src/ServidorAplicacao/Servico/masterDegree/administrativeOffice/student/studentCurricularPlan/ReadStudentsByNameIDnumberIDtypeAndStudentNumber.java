package ServidorAplicacao.Servico.masterDegree.administrativeOffice.student.studentCurricularPlan;

import java.util.ArrayList;
import java.util.List;

import Dominio.IPessoa;
import Dominio.IStudent;
import Dominio.Pessoa;
import Dominio.Student;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoDocumentoIdentificacao;

/**
 * @author David Santos
 * 2/Out/2003
 */

public class ReadStudentsByNameIDnumberIDtypeAndStudentNumber implements IServico {

	private static ReadStudentsByNameIDnumberIDtypeAndStudentNumber servico = new ReadStudentsByNameIDnumberIDtypeAndStudentNumber();

	public static ReadStudentsByNameIDnumberIDtypeAndStudentNumber getService() {
		return servico;
	}

	private ReadStudentsByNameIDnumberIDtypeAndStudentNumber() {
	}

	public final String getNome() {
		return "ReadStudentsByNameIDnumberIDtypeAndStudentNumber";
	}

	public List run(String studentName, Integer studentNumber, String idNumber, TipoDocumentoIdentificacao idType) throws FenixServiceException {
		List result = null;
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentStudent persistentStudent = sp.getIPersistentStudent();
			
			IPessoa personCriteria = new Pessoa();
			personCriteria.setNumeroDocumentoIdentificacao(idNumber);
			personCriteria.setTipoDocumentoIdentificacao(idType);
			personCriteria.setNome(studentName);
			
			IStudent studentCriteria = new Student();
			studentCriteria.setNumber(studentNumber);
			studentCriteria.setPerson(personCriteria);

			result = persistentStudent.readByCriteria(studentCriteria);
			
			if(result == null) {
				result = new ArrayList();
			}
		} catch (ExcepcaoPersistencia ex) {
			FenixServiceException newEx = new FenixServiceException("Persistence layer error");
			newEx.fillInStackTrace();
			throw newEx;
		}
		return result;
	}
}