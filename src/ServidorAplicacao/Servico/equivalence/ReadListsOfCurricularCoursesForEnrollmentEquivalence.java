package ServidorAplicacao.Servico.equivalence;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.equivalence.InfoEquivalenceContext;
import DataBeans.util.Cloner;
import Dominio.ICurricularCourse;
import Dominio.IEnrolment;
import Dominio.IStudentCurricularPlan;
import Dominio.StudentCurricularPlan;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoCurso;

/**
 * @author David Santos in May 12, 2004
 */

public class ReadListsOfCurricularCoursesForEnrollmentEquivalence extends EnrollmentEquivalenceServiceUtils implements IService
{
	public ReadListsOfCurricularCoursesForEnrollmentEquivalence()
	{
	}

	public InfoEquivalenceContext run(Integer studentNumber, TipoCurso degreeType, Integer fromStudentCurricularPlanID,
		Integer toStudentCurricularPlanID) throws FenixServiceException
	{
		List args = new ArrayList();
		args.add(0, fromStudentCurricularPlanID);
		args.add(1, toStudentCurricularPlanID);
		args.add(2, studentNumber);
		args.add(3, degreeType);
		
		List result1 = (List) convertDataInput(args);
		List result2 = (List) execute(result1);
		return (InfoEquivalenceContext) convertDataOutput(result2);
	}

	/**
	 * @see ServidorAplicacao.Servico.Service#convertDataInput(java.lang.Object)
	 * This method converts this service DataBeans input objects to their respective Domain objects.
	 * These Domain objects are to be used by the service's logic.
	 */
	protected Object convertDataInput(Object object)
	{
		return object;
	}

	/**
	 * @param List
	 * @return InfoEquivalenceContext
	 * @see ServidorAplicacao.Servico.Service#convertDataInput(java.lang.Object)
	 * This method converts this service output Domain objects to their respective DataBeans.
	 * These DataBeans are the result of executing this service logic and are to be passed on to the uper layer of the architecture.
	 */
	protected Object convertDataOutput(Object object)
	{
		List elements = (List) object;
		
		List enrollmentsThatMayGiveEquivalences = (List) elements.get(0);
		List curricularCoursesThatMayGetEquivalences = (List) elements.get(1);
		IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) elements.get(2);

		InfoEquivalenceContext infoEquivalenceContext = new InfoEquivalenceContext();

		infoEquivalenceContext
			.setInfoEnrolmentsToGiveEquivalence(cloneEnrolmentsToInfoEnrolments(
				enrollmentsThatMayGiveEquivalences));

		infoEquivalenceContext
			.setInfoCurricularCoursesToGetEquivalence(cloneCurricularCoursesToInfoCurricularCourses(
				curricularCoursesThatMayGetEquivalences));

		infoEquivalenceContext.setInfoStudentCurricularPlan(Cloner
			.copyIStudentCurricularPlan2InfoStudentCurricularPlan(studentCurricularPlan));

		return infoEquivalenceContext;
	}

	/**
	 * @param List
	 * @return List
	 * @throws FenixServiceException
	 * @see ServidorAplicacao.Servico.Service#convertDataInput(java.lang.Object)
	 * This method implements the buisiness logic of this service.
	 */
	protected Object execute(Object object) throws FenixServiceException
	{
		List input = (List) object;
		
		Integer fromStudentCurricularPlanID = (Integer) input.get(0);
		Integer toStudentCurricularPlanID = (Integer) input.get(1);
		Integer studentNumber = (Integer) input.get(2);
		TipoCurso degreeType = (TipoCurso) input.get(3);
		
		List output = new ArrayList();

		try
		{
			ISuportePersistente persistenceDAO = SuportePersistenteOJB.getInstance();
			IStudentCurricularPlanPersistente studentCurricularPlanDAO = persistenceDAO.getIStudentCurricularPlanPersistente();

			final IStudentCurricularPlan fromStudentCurricularPlan = (IStudentCurricularPlan) studentCurricularPlanDAO.readByOID(
				StudentCurricularPlan.class, fromStudentCurricularPlanID);

			final IStudentCurricularPlan toStudentCurricularPlan = (IStudentCurricularPlan) studentCurricularPlanDAO.readByOID(
				StudentCurricularPlan.class, toStudentCurricularPlanID);

			List enrollmentsThatMayGiveEquivalences = (List) CollectionUtils.select(fromStudentCurricularPlan.getEnrolments(),
				new Predicate()
			{
				public boolean evaluate(Object obj)
				{
					IEnrolment enrollment = (IEnrolment) obj;
					return (isAnAprovedEnrollment(enrollment) && isAnEnrollmentWithNoEquivalences(enrollment,
							toStudentCurricularPlan.getDegreeCurricularPlan(), fromStudentCurricularPlan));
				}
			});

			List aprovedAndEnrolledEnrollments = (List) CollectionUtils.select(
				toStudentCurricularPlan.getEnrolments(), new Predicate()
			{
				public boolean evaluate(Object obj)
				{
					IEnrolment enrollment = (IEnrolment) obj;
					return (isAnAprovedEnrollment(enrollment) || isAnEnroledEnrollment(enrollment));
				}
			});

			final List aprovedAndEnrolledCurricularCourses = (List) CollectionUtils.collect(
				aprovedAndEnrolledEnrollments, new Transformer()
			{
				public Object transform(Object obj)
				{
					IEnrolment enrollment = (IEnrolment) obj;
					return enrollment.getCurricularCourse();
				}
			});

			List curricularCoursesThatMayGetEquivalences = (List) CollectionUtils.select(
				toStudentCurricularPlan.getDegreeCurricularPlan().getCurricularCourses(), new Predicate()
			{
				public boolean evaluate(Object obj)
				{
					ICurricularCourse curricularCourse = (ICurricularCourse) obj;
					return !aprovedAndEnrolledCurricularCourses.contains(curricularCourse);
				}
			});

			output.add(0, enrollmentsThatMayGiveEquivalences);
			output.add(1, curricularCoursesThatMayGetEquivalences);
			output.add(2, getActiveStudentCurricularPlan(studentNumber, degreeType));

		} catch (ExcepcaoPersistencia e)
		{
			throw new FenixServiceException(e);
		}

		return output;
	}

}