/*
 * Created on 18/Fev/2004
 *  
 */
package ServidorAplicacao.Servico.degreeAdministrativeOffice.enrolment.withoutRules;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionYear;
import DataBeans.InfoStudent;
import DataBeans.util.Cloner;
import Dominio.CursoExecucao;
import Dominio.ICursoExecucao;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.Servico.degree.execution.ReadExecutionDegreesByExecutionYearAndDegreeType;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
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
public class PrepareDegreesListByStudentNumber implements IService
{
	public PrepareDegreesListByStudentNumber()
	{

	}

	public Object run(
		InfoStudent infoStudent,
		TipoCurso degreeType,
		Integer executionDegreeId,
		InfoExecutionYear infoExecutionYear)
	//Integer studentNumber, Integer executionDegreeIdChosen)
	throws FenixServiceException
	{		
		List result = null;
		ISuportePersistente sp = null;
		
		try
		{
			//read execution degrees by execution year and degree type
			ReadExecutionDegreesByExecutionYearAndDegreeType service =
				new ReadExecutionDegreesByExecutionYearAndDegreeType();
			List infoExecutionsDegreesList = service.run(infoExecutionYear, degreeType);
			if (infoExecutionsDegreesList == null || infoExecutionsDegreesList.size() <= 0)
			{
				throw new FenixServiceException("errors.impossible.operation");
			}
			
			//read student to enroll
			if (infoStudent == null || infoStudent.getNumber() == null)
			{
				throw new FenixServiceException("errors.impossible.operation");
			}
			
			sp = SuportePersistenteOJB.getInstance();
			IPersistentStudent persistentStudent = sp.getIPersistentStudent();
			IStudent student =
				persistentStudent.readStudentByNumberAndDegreeType(infoStudent.getNumber(), degreeType);
			if (student == null)
			{
				throw new FenixServiceException("errors.impossible.operation");
			}

	
			//select the first execution degree or the execution degree of the student logged
			InfoExecutionDegree infoExecutionDegree =
				selectExecutionDegree(sp, infoExecutionsDegreesList, executionDegreeId, student, degreeType);
			
			//it is return a list where the first element is the degree pre-select and the tail is all degrees 
			result = new ArrayList();
			result.add(infoExecutionDegree);
			result.addAll(infoExecutionsDegreesList);
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

		return result;
	}

	private InfoExecutionDegree selectExecutionDegree(
		ISuportePersistente sp,
		List infoExecutionDegreeList,
		Integer executionDegreeIdChosen,
		IStudent student, TipoCurso degreeType)
		throws ExcepcaoPersistencia
	{
		InfoExecutionDegree  infoExecutionDegree = null;

		//read the execution degree chosen
		if (executionDegreeIdChosen != null)
		{
			ICursoExecucaoPersistente persistentExecutionDegree = sp.getICursoExecucaoPersistente();

			ICursoExecucao executionDegree = new CursoExecucao();
			executionDegree.setIdInternal(executionDegreeIdChosen);
			executionDegree =
				(ICursoExecucao) persistentExecutionDegree.readByOId(executionDegree, false);
			if (executionDegree != null)
			{
				return (InfoExecutionDegree) Cloner.get(executionDegree);
			}
		}

		//read the execution degree belongs to student
		IStudentCurricularPlanPersistente persistentCurricularPlan =
			sp.getIStudentCurricularPlanPersistente();
		IStudentCurricularPlan studentCurricularPlan =
			persistentCurricularPlan.readActiveByStudentNumberAndDegreeType(
				student.getNumber(),
					degreeType);
		//execution degree isn't find, then it is chosen the list's first
		if (studentCurricularPlan == null
			|| studentCurricularPlan.getDegreeCurricularPlan() == null
			|| studentCurricularPlan.getDegreeCurricularPlan().getDegree() == null
			|| studentCurricularPlan.getDegreeCurricularPlan().getDegree().getNome() == null)
		{
				return (InfoExecutionDegree) infoExecutionDegreeList.get(0);
		}

		final Integer degreeCode =
			studentCurricularPlan.getDegreeCurricularPlan().getDegree().getIdInternal();
		List infoExecutionDegreeListWithDegreeCode =
			(List) CollectionUtils.select(infoExecutionDegreeList, new Predicate()
		{
			public boolean evaluate(Object input)
			{
				InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) input;
				return infoExecutionDegree
					.getInfoDegreeCurricularPlan()
					.getInfoDegree()
					.getIdInternal()
					.equals(
					degreeCode);
			}
		});
		if (!infoExecutionDegreeListWithDegreeCode.isEmpty())
		{
			infoExecutionDegree =
					(InfoExecutionDegree) infoExecutionDegreeListWithDegreeCode.get(0);
		}
		else
		{
			return (InfoExecutionDegree) infoExecutionDegreeList.get(0);
		}
		return infoExecutionDegree;
	}
}
