package ServidorAplicacao.Servico.student;

import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoStudent;
import Dominio.ExecutionCourse;
import Dominio.Frequenta;
import Dominio.IExecutionCourse;
import Dominio.IFrequenta;
import Dominio.IStudent;
import Dominio.Student;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/*
 * 
 * @author Fernanda Quitério 11/Fev/2004
 *  
 */
public class WriteStudentAttendingCourse implements IService
{
	public WriteStudentAttendingCourse()
	{
	}

	public Boolean run(InfoStudent infoStudent, Integer executionCourseId) throws FenixServiceException
	{
		try
		{
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentStudent persistentStudent = sp.getIPersistentStudent();
			IFrequentaPersistente persistentAttend = sp.getIFrequentaPersistente();
			IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();

			if (infoStudent == null)
			{
				return new Boolean(false);
			}
			if (executionCourseId != null)
			{
				IStudent student = findStudent(infoStudent, persistentStudent);
				
				IExecutionCourse executionCourse = findExecutionCourse(executionCourseId, persistentExecutionCourse);

				//Read every course the student attends to:
				List attends = persistentAttend.readByStudentNumber(student.getNumber());

				if (attends != null && attends.size() >= 8)
				{
					throw new FenixServiceException("reachedAteendsLimit");
				}

				IFrequenta attendsEntry =
					persistentAttend.readByAlunoAndDisciplinaExecucao(student, executionCourse);
				if (attendsEntry == null)
				{
					attendsEntry = new Frequenta();
					persistentAttend.simpleLockWrite(attendsEntry);
					attendsEntry.setAluno(student);
					attendsEntry.setDisciplinaExecucao(executionCourse);
				}
			}
		}
		catch (ExcepcaoPersistencia e)
		{
			e.printStackTrace();
			throw new FenixServiceException(e);
		}
		return new Boolean(true);
	}

	private IExecutionCourse findExecutionCourse(Integer executionCourseId, IPersistentExecutionCourse persistentExecutionCourse) throws FenixServiceException
	{
		IExecutionCourse executionCourse = new ExecutionCourse();
		executionCourse.setIdInternal(executionCourseId);
		executionCourse =
		(IExecutionCourse) persistentExecutionCourse.readByOId(executionCourse, false);

		if (executionCourse == null)
		{
			throw new FenixServiceException("noExecutionCourse");
		}
		return executionCourse;
	}

	private IStudent findStudent(InfoStudent infoStudent, IPersistentStudent persistentStudent) throws FenixServiceException
	{
		IStudent student = new Student();
		student.setIdInternal(infoStudent.getIdInternal());
		
		student = (IStudent) persistentStudent.readByOId(student, false);
		if(student == null) {
			throw new FenixServiceException("noStudent");
		}
		return student;
	}
}