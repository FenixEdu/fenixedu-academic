/*
 * Created on 16/Set/2003, 12:18:28
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorAplicacao.Servico.teacher;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import DataBeans.InfoFrequenta;
import DataBeans.InfoStudent;
import DataBeans.util.Cloner;
import Dominio.IFrequenta;
import Dominio.IStudent;
import Dominio.Student;
import ServidorAplicacao.IServico;
import ServidorApresentacao.Action.Seminaries.Exceptions.BDException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 *
 * 
 * Created at 16/Set/2003, 12:18:28
 * 
 */
public class GetAttendaciesByStudentList implements IServico {
	private static GetAttendaciesByStudentList service =
		new GetAttendaciesByStudentList();
	/**
	 * The singleton access method of this class.
	 **/
	public static GetAttendaciesByStudentList getService() {
		return service;
	}
	/**
	 * The actor of this class.
	 **/
	private GetAttendaciesByStudentList() {
	}
	/**
	 * Returns The Service Name */
	public final String getNome() {
		return "teacher.GetAttendaciesByStudentList";
	}
	public List run(Integer executionCourseID, List infoStudents)
		throws BDException {
			List attendacies = new LinkedList();
		try {
			ISuportePersistente persistenceSupport =
				SuportePersistenteOJB.getInstance();
			IFrequentaPersistente persistentAttendacy =
				persistenceSupport.getIFrequentaPersistente();
			IPersistentStudent persistentStudent =
				persistenceSupport.getIPersistentStudent();
			List students = new LinkedList();
			for (Iterator infoStudentsIterator = infoStudents.iterator();
				infoStudentsIterator.hasNext();
				) {
				InfoStudent infoStudent =
					(InfoStudent) infoStudentsIterator.next();
				IStudent student = new Student(infoStudent.getIdInternal());
				student =
					(IStudent) persistentStudent.readByOId(student, false);
				if (student!=null) {
					students.add(student);
				}	
				
			}
			
			for (Iterator studentsIterator = students.iterator();
				studentsIterator.hasNext();
				) {
				IStudent student = (IStudent) studentsIterator.next();
				IFrequenta attendacy =
					persistentAttendacy.readByAlunoIdAndDisciplinaExecucaoId(
						student.getIdInternal(),
						executionCourseID);
				InfoFrequenta infoFrequenta =
					Cloner.copyIFrequenta2InfoFrequenta(attendacy);
				attendacies.add(infoFrequenta);
			}
		} catch (ExcepcaoPersistencia ex) {
			throw new BDException(
				"Got an error while trying to get info about a student's work group",
				ex);
		}
		return attendacies;
	}
}
