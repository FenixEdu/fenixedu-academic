/*
 * Created on 17/Fev/2004
 *  
 */
package ServidorAplicacao.Servico.degreeAdministrativeOffice.enrolment.withoutRules;

import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoStudent;
import DataBeans.InfoStudentCurricularPlan;
import DataBeans.util.Cloner;
import Dominio.IEnrolment;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.strategy.enrolment.context.InfoStudentEnrolmentContext;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.EnrolmentState;
import Util.TipoCurso;

/**
 * @author Tânia Pousão
 *  
 */
public class ReadEnrollmentsWithStateEnrolledByStudent implements IService
{
	public ReadEnrollmentsWithStateEnrolledByStudent()
	{
	}

	public Object run(InfoStudent infoStudent, TipoCurso degreeType) throws FenixServiceException
	{
		System.out.println("ReadEnrollmentsWithStateEnrolledByStudent");
		InfoStudentEnrolmentContext infoStudentEnrolmentContext = null;
		try
		{
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IStudentCurricularPlanPersistente persistentStudentCurricularPlan =
				sp.getIStudentCurricularPlanPersistente();

			IStudentCurricularPlan studentCurricularPlan = null;
			if (infoStudent.getNumber() != null)
			{
				studentCurricularPlan =
					persistentStudentCurricularPlan.readActiveByStudentNumberAndDegreeType(
						infoStudent.getNumber(),
						degreeType);
			}
			if (studentCurricularPlan == null)
			{
				throw new FenixServiceException("");
			}

			IPersistentEnrolment persistentEnrolment = sp.getIPersistentEnrolment();
			List enrollments =
				persistentEnrolment.readEnrolmentsByStudentCurricularPlanAndEnrolmentState(
					studentCurricularPlan,
					EnrolmentState.ENROLED);

			infoStudentEnrolmentContext = buildResult(studentCurricularPlan, enrollments);
			
			if(infoStudentEnrolmentContext == null){
				throw new FenixServiceException("");
			}
			
		}
		catch (ExcepcaoPersistencia e)
		{
			e.printStackTrace();
			throw new FenixServiceException("");
		}

		return infoStudentEnrolmentContext;
	}

	/**
	 * @param studentCurricularPlan
	 * @param enrollments
	 * @return
	 */
	private InfoStudentEnrolmentContext buildResult(
		IStudentCurricularPlan studentCurricularPlan,
		List enrollments)
	{
		InfoStudentCurricularPlan infoStudentCurricularPlan =
			Cloner.copyIStudentCurricularPlan2InfoStudentCurricularPlan(studentCurricularPlan);

		List infoEnrollments = (List) CollectionUtils.collect(enrollments, new Transformer()
		{
			public Object transform(Object input)
			{
				IEnrolment enrolment = (IEnrolment) input;
				return Cloner.copyIEnrolment2InfoEnrolment(enrolment);
			}
		});
		Collections.sort(infoEnrollments, new BeanComparator(("infoCurricularCourse.name")));
		
		InfoStudentEnrolmentContext infoStudentEnrolmentContext = new InfoStudentEnrolmentContext();
		infoStudentEnrolmentContext.setInfoStudentCurricularPlan(infoStudentCurricularPlan);
		infoStudentEnrolmentContext.setStudentInfoEnrollmentsWithStateEnrolled(infoEnrollments);
		
		return infoStudentEnrolmentContext;
	}
}
