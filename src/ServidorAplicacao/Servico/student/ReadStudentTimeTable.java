package ServidorAplicacao.Servico.student;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoLesson;
import DataBeans.util.Cloner;
import Dominio.IAula;
import Dominio.IExecutionPeriod;
import Dominio.IStudent;
import Dominio.ITurno;
import Dominio.ITurnoAluno;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoAlunoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author João Mota
 *
 */

public class ReadStudentTimeTable implements IServico {

	private static ReadStudentTimeTable _servico = new ReadStudentTimeTable();
	/**
	 * The singleton access method of this class.
	 **/
	public static ReadStudentTimeTable getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private ReadStudentTimeTable() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "ReadStudentTimeTable";
	}

	public List run(String username)
		throws FenixServiceException {

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentStudent persistentStudent = sp.getIPersistentStudent();
			IStudent student = persistentStudent.readByUsername(username);
			ITurnoAlunoPersistente persistentShiftStudent = sp.getITurnoAlunoPersistente();
			IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();
			IExecutionPeriod executionPeriod = persistentExecutionPeriod.readActualExecutionPeriod();
			List studentShifts = persistentShiftStudent.readByStudentAndExecutionPeriod(student,executionPeriod);
		
			List lessons = new ArrayList();
			Iterator shiftIter = studentShifts.iterator();
			while (shiftIter.hasNext()){
				ITurnoAluno shiftStudent = (ITurnoAluno) shiftIter.next();
				ITurno shift = shiftStudent.getShift();
				lessons.addAll(shift.getAssociatedLessons());
			}
			Iterator iter = lessons.iterator();
			List infoLessons = new ArrayList();
			while(iter.hasNext()){
				IAula lesson = (IAula) iter.next();
				InfoLesson infolesson = Cloner.copyILesson2InfoLesson(lesson);
				infoLessons.add(infolesson);
			}
			return infoLessons;
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}

		

	}

}
