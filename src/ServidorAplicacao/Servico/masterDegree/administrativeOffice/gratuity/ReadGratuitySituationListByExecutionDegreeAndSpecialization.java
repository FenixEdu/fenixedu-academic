/*
 * Created on 22/Jan/2004
 *  
 */
package ServidorAplicacao.Servico.masterDegree.administrativeOffice.gratuity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoEnrolment;
import DataBeans.InfoGratuitySituation;
import DataBeans.InfoGuide;
import DataBeans.InfoGuideEntry;
import DataBeans.util.Cloner;
import Dominio.CursoExecucao;
import Dominio.ExecutionYear;
import Dominio.ICursoExecucao;
import Dominio.IExecutionYear;
import Dominio.IGratuitySituation;
import Dominio.IGuide;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.IPersistentGratuitySituation;
import ServidorPersistente.IPersistentGuide;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.DocumentType;
import Util.GratuitySituationType;
import Util.Specialization;

/**
 * @author Tânia Pousão
 *  
 */
/**
 * @author Tânia Pousão
 *  
 */
public class ReadGratuitySituationListByExecutionDegreeAndSpecialization implements IService
{
	/**
	 * Constructor
	 */
	public ReadGratuitySituationListByExecutionDegreeAndSpecialization()
	{
	}

	/*
	 * Return an hash map with three objects: 1. at first position a list of infoGratuitySituation 2. in
	 * second, a double with the total of list's payed values 3. in third, a double with the total of
	 * list's remaning values
	 */
	public Object run(
		Integer executionDegreeId,
		String executionYearName,
		String specializationName,
		String gratuitySituationTypeName)
		throws FenixServiceException
	{
		//at least one of the arguments it's obligator
		if (executionDegreeId == null && executionYearName == null)
		{
			throw new FenixServiceException("error.masterDegree.gratuity.impossible.studentsGratuityList");
		}

		if (specializationName == null)
		{
			throw new FenixServiceException("error.masterDegree.gratuity.impossible.studentsGratuityList");
		}

		if (gratuitySituationTypeName == null)
		{
			throw new FenixServiceException("error.masterDegree.gratuity.impossible.studentsGratuityList");
		}

		HashMap result = null;
		ISuportePersistente sp = null;
		try
		{
			sp = SuportePersistenteOJB.getInstance();

			//EXECUTION DEGREE
			//Fill a execution degree with it's id or execution year chosen
			ICursoExecucao executionDegree = new CursoExecucao();
			if (executionDegreeId != null)
			{
				executionDegree.setIdInternal(executionDegreeId); //atention it can be null
			}
			IExecutionYear executionYear = new ExecutionYear();
			if (executionYearName != null)
			{
				IPersistentExecutionYear persistentExecutionYear = sp.getIPersistentExecutionYear();
				executionYear = persistentExecutionYear.readExecutionYearByName(executionYearName);

				executionDegree.setExecutionYear(executionYear); //atention it can be null
			}

			//SPECIALIZATION
			Specialization specialization = new Specialization(specializationName);

			//GRATUITY SITUATION
			GratuitySituationType gratuitySituationType =
				GratuitySituationType.getEnum(gratuitySituationTypeName);

			//Read all gratuity situation desired
			IPersistentGratuitySituation persistentGratuitySituation =
				sp.getIPersistentGratuitySituation();
			List gratuitySituationList = null;
			gratuitySituationList =
				persistentGratuitySituation.readGratuitySituationListByExecutionDegreeAndSpecialization(
					executionDegree,
					specialization);
						
			List infoGratuitySituationList = new ArrayList();
			double totalPayedValue = 0;
			double totalRemaingValue = 0;

			if (gratuitySituationList != null && gratuitySituationList.size() > 0)
			{				
				ListIterator listIterator = gratuitySituationList.listIterator();
				//while it is cloner each element of the list
				//it is calculate the total values of payed and remaning values.
				while (listIterator.hasNext())
				{
					IGratuitySituation gratuitySituation = (IGratuitySituation) listIterator.next();

					InfoGratuitySituation infoGratuitySituation =
						Cloner.copyIGratuitySituation2InfoGratuitySituation(gratuitySituation);

					//find gratuity's payed value
					calculateGratuityPayedValue(sp, infoGratuitySituation);

					//find gratuity's total value with exemption
					calculateGratuityTotalValue(sp, infoGratuitySituation);

					fillSituationType(infoGratuitySituation);

					if (gratuitySituationType == null || infoGratuitySituation.getSituationType().equals(gratuitySituationType))
					{
						infoGratuitySituationList.add(infoGratuitySituation);

						//add all value for find the total
						totalPayedValue =
							totalPayedValue + infoGratuitySituation.getPayedValue().doubleValue();
						totalRemaingValue =
							totalRemaingValue + infoGratuitySituation.getRemainingValue().doubleValue();
					}
				}
			} 

			//build the result that is a hash map with a list, total payed and remaining value
			result = new HashMap();
			result.put(new Integer(0), infoGratuitySituationList);
			result.put(new Integer(1), new Double(totalPayedValue));
			result.put(new Integer(2), new Double(totalRemaingValue));
		}
		catch (ExcepcaoPersistencia e)
		{
			e.printStackTrace();

			throw new FenixServiceException("error.masterDegree.gratuity.impossible.studentsGratuityList");
		}

		return result;
	}

	/**
	 * @param sp
	 * @param infoGratuitySituation
	 */
	private void calculateGratuityPayedValue(
		ISuportePersistente sp,
		InfoGratuitySituation infoGratuitySituation)
		throws ExcepcaoPersistencia
	{
		//all guides of this student that aren't annulled but payed
		IPersistentGuide persistentGuide = sp.getIPersistentGuide();
		List guideList =
			persistentGuide.readNotAnnulledAndPayedByPersonAndExecutionDegree(
				infoGratuitySituation
					.getInfoStudentCurricularPlan()
					.getInfoStudent()
					.getInfoPerson()
					.getIdInternal(),
				infoGratuitySituation.getInfoGratuityValues().getInfoExecutionDegree().getIdInternal());
		List infoGuideList = (List) CollectionUtils.collect(guideList, new Transformer()
		{

			public Object transform(Object arg0)
			{
				IGuide guide = (IGuide) arg0;
				InfoGuide infoGuide = Cloner.copyIGuide2InfoGuide(guide);
				return infoGuide;
			}
		});

		addPayedGuide(infoGratuitySituation, infoGuideList);
	}

	/**
	 * @param infoGuideList
	 * @return
	 */
	private void addPayedGuide(InfoGratuitySituation infoGratuitySituation, List infoGuideList)
	{
		double payedValue = 0;

		ListIterator iterator = infoGuideList.listIterator();
		while (iterator.hasNext())
		{
			InfoGuide infoGuide = (InfoGuide) iterator.next();

			List infoGuideEntriesList = infoGuide.getInfoGuideEntries();
			if (infoGuideEntriesList != null && infoGuideEntriesList.size() >= 0)
			{
				ListIterator iteratorGuideEntries = infoGuideEntriesList.listIterator();
				while (iteratorGuideEntries.hasNext())
				{
					InfoGuideEntry infoGuideEntry = (InfoGuideEntry) iteratorGuideEntries.next();

					if (infoGuideEntry.getDocumentType().equals(DocumentType.GRATUITY_TYPE))
					{
						payedValue = payedValue + infoGuideEntry.getPrice().doubleValue();
					}
					else if (infoGuideEntry.getDocumentType().equals(DocumentType.INSURANCE_TYPE))
					{
						infoGratuitySituation.setInsurancePayed(Boolean.TRUE);
					}
				}
			}
		}

		infoGratuitySituation.setPayedValue(new Double(payedValue));
	}

	/**
	 * @param sp
	 * @param infoGratuitySituation
	 */
	private void calculateGratuityTotalValue(
		ISuportePersistente sp,
		InfoGratuitySituation infoGratuitySituation)
	{

		if (infoGratuitySituation.getInfoStudentCurricularPlan().getSpecialization() != null
			&& (infoGratuitySituation
				.getInfoStudentCurricularPlan()
				.getSpecialization()
				.equals(Specialization.MESTRADO_TYPE)
				|| infoGratuitySituation.getInfoStudentCurricularPlan().getSpecialization().equals(
					Specialization.INTEGRADO_TYPE)))
		{
			infoGratuitySituation.setRemainingValue(
				infoGratuitySituation.getInfoGratuityValues().getAnualValue());
		}
		else if (
			infoGratuitySituation.getInfoStudentCurricularPlan().getSpecialization() != null
				&& (infoGratuitySituation
					.getInfoStudentCurricularPlan()
					.getSpecialization()
					.equals(Specialization.ESPECIALIZACAO_TYPE)))
		{
			if (infoGratuitySituation.getInfoStudentCurricularPlan().getInfoEnrolments() != null
				&& infoGratuitySituation.getInfoStudentCurricularPlan().getInfoEnrolments().size() > 0)
			{
				if (infoGratuitySituation.getInfoGratuityValues().getCourseValue() != null)
				{
					infoGratuitySituation.setRemainingValue(
						new Double(
							infoGratuitySituation.getInfoGratuityValues().getCourseValue().doubleValue()
								* infoGratuitySituation
									.getInfoStudentCurricularPlan()
									.getInfoEnrolments()
									.size()));
				}
				else
				{
					double totalToPay = 0;
					Iterator iterCourse =
						infoGratuitySituation
							.getInfoStudentCurricularPlan()
							.getInfoEnrolments()
							.iterator();
					while (iterCourse.hasNext())
					{
						InfoEnrolment infoEnrolment = (InfoEnrolment) iterCourse.next();
						totalToPay
							+= infoEnrolment.getInfoCurricularCourseScope().getCredits().doubleValue()
							* infoGratuitySituation.getInfoGratuityValues().getCreditValue().doubleValue();
					}
					infoGratuitySituation.setRemainingValue(new Double(totalToPay));
				}
			}
			else
			{
				infoGratuitySituation.setRemainingValue(new Double(0));
			}

			//discount exemption
			if (infoGratuitySituation.getExemptionPercentage() != null
				&& infoGratuitySituation.getExemptionPercentage().doubleValue() > 0)
			{
				double exemptionDiscount =
					infoGratuitySituation.getRemainingValue().doubleValue()
						* (infoGratuitySituation.getExemptionPercentage().doubleValue() / 100.0);
				infoGratuitySituation.setRemainingValue(
					new Double(
						infoGratuitySituation.getRemainingValue().doubleValue() - exemptionDiscount));
			}
		}
	}

	private void fillSituationType(InfoGratuitySituation infoGratuitySituation)
	{
		if (infoGratuitySituation.getRemainingValue().doubleValue() > 0)
		{
			infoGratuitySituation.setSituationType(GratuitySituationType.DEBTOR);
		}
		else if (infoGratuitySituation.getRemainingValue().doubleValue() == 0)
		{
			infoGratuitySituation.setSituationType(GratuitySituationType.REGULARIZED);
		}
		else if (infoGratuitySituation.getRemainingValue().doubleValue() < 0)
		{
			infoGratuitySituation.setSituationType(GratuitySituationType.CREDITOR);
		}
	}
}
