/*
 * Created on 10/Fev/2004
 *  
 */
package ServidorAplicacao.Servico.student;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionDegree;
import DataBeans.enrollment.shift.InfoShiftEnrollment;
import DataBeans.util.Cloner;
import Dominio.CursoExecucao;
import Dominio.Frequenta;
import Dominio.ICursoExecucao;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.IFrequenta;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoCurso;

/**
 * @author Tânia Pousão
 * 
 * This class read and prepare all information usefull for shift enrollment use case
 *  
 */
public class PrepareInfoShiftEnrollmentByUsername implements IService
{
	public PrepareInfoShiftEnrollmentByUsername()
	{

	}

	public Object run(String usernameStudent, Integer executionDegreeIdChosen)
		throws FenixServiceException
	{
		InfoShiftEnrollment infoShiftEnrollment = new InfoShiftEnrollment();
		ISuportePersistente sp = null;

		try
		{
			sp = SuportePersistenteOJB.getInstance();

			//read student logged to enroll
			IPersistentStudent persistentStudent = sp.getIPersistentStudent();
			IStudent student = persistentStudent.readByUsername(usernameStudent);
			if (student == null)
			{
				throw new FenixServiceException("errors.impossible.operation");
			}
			infoShiftEnrollment.setInfoStudent(Cloner.copyIStudent2InfoStudent(student));

			//retrieve all courses that student is currently attended in
			infoShiftEnrollment.setInfoAttendingCourses(readAttendingCourses(sp, student.getNumber()));

			//read current execution period
			IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();
			IExecutionPeriod executionPeriod = persistentExecutionPeriod.readActualExecutionPeriod();
			if (executionPeriod == null)
			{
				throw new FenixServiceException("errors.impossible.operation");
			}

			//read current execution year
			IExecutionYear executionYear = executionPeriod.getExecutionYear();
			if (executionYear == null)
			{
				throw new FenixServiceException("errors.impossible.operation");
			}

			//retrieve the list of existing degrees
			List infoExecutionDegreeList = readInfoExecutionDegrees(sp, executionYear);
			infoShiftEnrollment.setInfoExecutionDegreesList(infoExecutionDegreeList);

			//select the first execution degree or the execution degree of the student logged
			ICursoExecucao executionDegree =
				selectExecutionDegree(sp, infoExecutionDegreeList, executionDegreeIdChosen, student);
			infoShiftEnrollment.setInfoExecutionDegree(
				(InfoExecutionDegree) Cloner.get(executionDegree));

			//retrieve all courses pertaining to the selected degree
			infoShiftEnrollment.setInfoExecutionCoursesList(
				readInfoExecutionCoursesOfdegree(sp, executionPeriod, executionDegree));

		}
		catch (ExcepcaoPersistencia excepcaoPersistencia)
		{
			excepcaoPersistencia.printStackTrace();
			throw new FenixServiceException();
		}
		catch (FenixServiceException serviceException)
		{
			serviceException.printStackTrace();
			throw new FenixServiceException(serviceException.getMessage());
		}

		return infoShiftEnrollment;
	}

	private List readAttendingCourses(ISuportePersistente sp, Integer studentNumber)
		throws ExcepcaoPersistencia
	{
		List infoAttendingCourses = null;

		IFrequentaPersistente frequentaPersistente = sp.getIFrequentaPersistente();
		List attendList = frequentaPersistente.readByStudentNumber(studentNumber);
		if (attendList != null && attendList.size() > 0)
		{
			infoAttendingCourses = new ArrayList();

			Iterator iterator = attendList.iterator();
			while (iterator.hasNext())
			{
				IFrequenta attend = (Frequenta) iterator.next();
				IExecutionCourse executionCourse = attend.getDisciplinaExecucao();
				infoAttendingCourses.add(Cloner.get(executionCourse));
			}
		}

		return infoAttendingCourses;
	}

	private List readInfoExecutionDegrees(ISuportePersistente sp, IExecutionYear executionYear)
		throws ExcepcaoPersistencia, FenixServiceException
	{
		ICursoExecucaoPersistente presistentExecutionDegree = sp.getICursoExecucaoPersistente();
		List executionDegrees =
			presistentExecutionDegree.readByExecutionYearAndDegreeType(
				executionYear,
				TipoCurso.LICENCIATURA_OBJ);
		if (executionDegrees == null || executionDegrees.size() <= 0)
		{
			throw new FenixServiceException("errors.impossible.operation");
		}

		Iterator iterator = executionDegrees.iterator();
		List infoExecutionDegreeList = new ArrayList();
		while (iterator.hasNext())
		{
			ICursoExecucao executionDegree = (ICursoExecucao) iterator.next();
			infoExecutionDegreeList.add(Cloner.get(executionDegree));
		}

		return infoExecutionDegreeList;
	}

	private List readInfoExecutionCoursesOfdegree(
		ISuportePersistente sp,
		IExecutionPeriod executionPeriod,
		ICursoExecucao executionDegree)
		throws ExcepcaoPersistencia, FenixServiceException
	{
		IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();
		List executionCourseList =
			persistentExecutionCourse.readByExecutionPeriodAndExecutionDegree(
				executionPeriod,
				executionDegree);
		if (executionCourseList == null || executionCourseList.size() <= 0)
		{
			throw new FenixServiceException("errors.impossible.operation");
		}

		ListIterator listIterator = executionCourseList.listIterator();
		List infoExecutionCourseList = new ArrayList();
		while (listIterator.hasNext())
		{
			IExecutionCourse executionCourse = (IExecutionCourse) listIterator.next();
			InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) Cloner.get(executionCourse);
			infoExecutionCourseList.add(infoExecutionCourse);
		}

		return infoExecutionCourseList;
	}

	/**
	 * @param infoExecutionDegreeList
	 * @return
	 */
	private ICursoExecucao selectExecutionDegree(
		ISuportePersistente sp,
		List infoExecutionDegreeList,
		Integer executionDegreeIdChosen,
		IStudent student)
		throws ExcepcaoPersistencia
	{
		ICursoExecucao executionDegree = null;

		//read the execution degree chosen
		if(executionDegreeIdChosen != null){
			ICursoExecucaoPersistente persistentExecutionDegree = sp.getICursoExecucaoPersistente();
			
			executionDegree = new CursoExecucao();
			executionDegree.setIdInternal(executionDegreeIdChosen);
			executionDegree = (ICursoExecucao) persistentExecutionDegree.readByOId(executionDegree, false);
			if(executionDegree != null){
				return executionDegree;
			}
		}
				
		IStudentCurricularPlanPersistente persistentCurricularPlan =
			sp.getIStudentCurricularPlanPersistente();
		IStudentCurricularPlan studentCurricularPlan =
			persistentCurricularPlan.readActiveByStudentNumberAndDegreeType(
				student.getNumber(),
				TipoCurso.LICENCIATURA_OBJ);
		if (studentCurricularPlan == null
			|| studentCurricularPlan.getDegreeCurricularPlan() == null
			|| studentCurricularPlan.getDegreeCurricularPlan().getDegree() == null
			|| studentCurricularPlan.getDegreeCurricularPlan().getDegree().getNome() == null)
		{
			executionDegree =
				Cloner.copyInfoExecutionDegree2ExecutionDegree(
					(InfoExecutionDegree) infoExecutionDegreeList.get(0));
			return executionDegree;
		}

		final String degreeCode = studentCurricularPlan.getDegreeCurricularPlan().getDegree().getNome();
		List infoExecutionDegreeListWithDegreeCode =
			(List) CollectionUtils.select(infoExecutionDegreeList, new Predicate()
		{
			public boolean evaluate(Object input)
			{
				InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) input;
				return infoExecutionDegree
					.getInfoDegreeCurricularPlan()
					.getInfoDegree()
					.getSigla()
					.equals(
					degreeCode);
			}
		});
		if (!infoExecutionDegreeListWithDegreeCode.isEmpty())
		{
			executionDegree =
				Cloner.copyInfoExecutionDegree2ExecutionDegree(
					(InfoExecutionDegree) infoExecutionDegreeListWithDegreeCode.get(0));
		}
		else
		{
			executionDegree =
				Cloner.copyInfoExecutionDegree2ExecutionDegree(
					(InfoExecutionDegree) infoExecutionDegreeList.get(0));
		}

		return executionDegree;
	}
}
