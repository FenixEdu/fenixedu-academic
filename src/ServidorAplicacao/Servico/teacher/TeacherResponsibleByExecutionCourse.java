package ServidorAplicacao.Servico.teacher;

import Dominio.CurricularCourse;
import Dominio.ExecutionCourse;
import Dominio.ICurricularCourse;
import Dominio.IExecutionCourse;
import Dominio.IResponsibleFor;
import Dominio.ITeacher;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentResponsibleFor;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Tânia Pousão Create on 18/Dez/2003
 */
public class TeacherResponsibleByExecutionCourse implements IServico
{
	private static TeacherResponsibleByExecutionCourse service =
		new TeacherResponsibleByExecutionCourse();

	public static TeacherResponsibleByExecutionCourse getService()
	{
		return service;
	}

	public TeacherResponsibleByExecutionCourse()
	{

	}

	public final String getNome()
	{
		return "TeacherResponsibleByExecutionCourse";
	}

	public Boolean run(String teacherUserName, Integer executionCourseCode, Integer curricularCourseCode)
		throws FenixServiceException
	{
		boolean result = false;

		try
		{
			result =
				ExecutionCourseResponsibleTeacher(teacherUserName, executionCourseCode)
					&& CurricularCourseNotBasic(curricularCourseCode);

		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new FenixServiceException(e);
		}

		return new Boolean(result);
	}

	/**
	 * @param argumentos
	 * @return
	 */
	private boolean ExecutionCourseResponsibleTeacher(String teacherUserName, Integer executionCourseCode)
		throws FenixServiceException
	{
		boolean result = false;

		ITeacher teacher = null;
		IResponsibleFor responsibleFor = null;
		IExecutionCourse executionCourse = null;
		
		ISuportePersistente sp;
		try
		{
			sp = SuportePersistenteOJB.getInstance();
			
			
			IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
			teacher =
			persistentTeacher.readTeacherByUsername(teacherUserName);

			IPersistentExecutionCourse persistentExecutionCourse =
			sp.getIPersistentExecutionCourse();
			executionCourse =
			(IExecutionCourse) persistentExecutionCourse.readByOId(
					new ExecutionCourse(executionCourseCode),
					false);
			
			IPersistentResponsibleFor persistentResponsibleFor =
			sp.getIPersistentResponsibleFor();
			responsibleFor =
			persistentResponsibleFor.readByTeacherAndExecutionCoursePB(
					teacher,
					executionCourse);
			if (responsibleFor == null) {
				result =  false;
			} else {
				result = true;
			}
		}
		catch (Exception e)
		{
			throw new FenixServiceException(e);
		}
		return result;
	}

	/**
	 * @param argumentos
	 * @return
	 */
	private boolean CurricularCourseNotBasic(Integer curricularCourseCode) throws FenixServiceException
	{
		boolean result = false;
		ICurricularCourse curricularCourse = null;

		ISuportePersistente sp;
		try
		{

			sp = SuportePersistenteOJB.getInstance();
			IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();
			curricularCourse =
				(ICurricularCourse) persistentCurricularCourse.readByOId(
					new CurricularCourse(curricularCourseCode),
					false);
			result = curricularCourse.getBasic().equals(Boolean.FALSE);
		}
		catch (Exception e)
		{
			throw new FenixServiceException(e);
		}
		return result;
	}
}
