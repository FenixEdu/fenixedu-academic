package ServidorAplicacao.Servico.student;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoStudent;
import Dominio.DisciplinaExecucao;
import Dominio.Frequenta;
import Dominio.IDisciplinaExecucao;
import Dominio.IFrequenta;
import Dominio.IStudent;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * Describe class <code>WriteStudentAttendingCourses</code> here.
 *
 * @author <a href="mailto:tdi-dev@tagus.ist.utl.pt">tdi-dev:Edgar Gonçalves</a>
 * @version 1.0
 */
public class WriteStudentAttendingCourses implements IServico {

	private static WriteStudentAttendingCourses _servico = new WriteStudentAttendingCourses();

	/**
	 * The actor of this class.
	 **/

	private WriteStudentAttendingCourses() {

	}

	/**
	 * Returns Service Name
	 */
	public String getNome() {
		return "WriteStudentAttendingCourses";
	}

	/**
	 * Returns the _servico.
	 * @return WriteStudentAttendingCourses
	 */
	public static WriteStudentAttendingCourses getService() {
		return _servico;
	}

	/**
	 * Describe <code>run</code> method here.
	 *
	 * @param infoStudent an <code>InfoStudent</code>,
	 * @param infoExecutionCourses a <code>List</code> with the wanted executionCourse.idInternal's of the ATTEND table.
	 * @return a <code>Boolean</code> to indicate if all went fine.
	 * @exception FenixServiceException if an error occurs.
	 */
	public Boolean run(InfoStudent infoStudent, List infoExecutionCourses) throws FenixServiceException {

		if (infoExecutionCourses == null || infoStudent == null) {
			return new Boolean(false);
		} // end of if ()

		//TODO: tdi-dev (edgar.goncalves) -> remove system.out.println's
		boolean result = false;

		try {

			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			//Reads the student from the database
			IStudent student = sp.getIPersistentStudent().readByNumero(infoStudent.getNumber(), infoStudent.getDegreeType());

			IFrequentaPersistente attendsDAO = sp.getIFrequentaPersistente();

			//Read every course the student attends to:
			List attendingCourses = attendsDAO.readByStudentId(student.getNumber());

			IDisciplinaExecucaoPersistente executionCourseDAO = sp.getIDisciplinaExecucaoPersistente();

			//Gets the database objects for the wanted courses
			List wantedAttends = new ArrayList();
			Iterator i = infoExecutionCourses.iterator();
			while (i.hasNext()) {
				Integer executionCourseId = new Integer((String) i.next());
				IDisciplinaExecucao executionCourse = new DisciplinaExecucao(executionCourseId);
				executionCourse = (IDisciplinaExecucao) executionCourseDAO.readByOId(executionCourse, false);

				if (executionCourse == null) {
					System.out.println("Execution course with ID=" + executionCourseId + " does not exist in the database!");
					throw new FenixServiceException();
				} // end of if ()
				else {
					wantedAttends.add(executionCourse);
					System.out.println("Adding to the wantedAttends: " + executionCourse.getNome());
				}
			}

			//Delete all courses the student is currently attendin to that he/she doesn't want to:
			i = attendingCourses.iterator();
			while (i.hasNext()) {
				IFrequenta attendEntry = (IFrequenta) i.next();
			
				// FIXME SO REMOVES UM ELEMENTO.... DELETE SHIFT-STUDENT ASSOCIATIONS...
				if (!wantedAttends.contains(attendEntry.getDisciplinaExecucao())) {
					System.out.println("Deleting: " + attendEntry.getIdInternal());
					attendsDAO.delete(attendEntry);
					i.remove();
				}
				wantedAttends.remove(attendEntry.getDisciplinaExecucao());
			}

			//Add new courses (without duplicates)
			i = wantedAttends.iterator();
			while (i.hasNext()) {

				IDisciplinaExecucao executionCourse = (IDisciplinaExecucao) i.next();

				System.out.println("Adding: " + executionCourse.getIdInternal() + ";" + executionCourse.getNome());

				IFrequenta attendsEntry = new Frequenta();
				//FIXME: (tdi-dev:edgar.goncalves) - lockWrite ain't working...
				attendsDAO.simpleLockWrite(attendsEntry);
				attendsEntry.setAluno(student);
				attendsEntry.setDisciplinaExecucao(executionCourse);

			} // end of while ()

			result = true;

		} catch (ExcepcaoPersistencia e) {

			e.printStackTrace();
			throw new FenixServiceException();
		}

		return new Boolean(result);

	}

}
