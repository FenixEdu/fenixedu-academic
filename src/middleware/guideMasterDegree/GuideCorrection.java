//NOT USED

/*
 * Created on 24/Mar/2004
 *
 */
package middleware.guideMasterDegree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;

import DataBeans.InfoGratuitySituation;
import DataBeans.InfoGuide;
import DataBeans.InfoGuideEntry;
import DataBeans.InfoGuideSituation;
import DataBeans.util.Cloner;
import Dominio.CursoExecucao;
import Dominio.GratuitySituation;
import Dominio.Guide;
import Dominio.ICursoExecucao;
import Dominio.IGratuitySituation;
import Dominio.IGuide;
import Dominio.IStudentCurricularPlan;
import Dominio.StudentCurricularPlan;
import Util.DocumentType;
import Util.SituationOfGuide;

/**
 * @author Tânia Pousão
 *  
 */
public class GuideCorrection
{

	/*
	 * Antes a aplicação de "pos-grad" funcionava com um Execution Degree para os dois anos do plano
	 * curricular. Mais tarde, para coerência do conceito de Execution Degree, acrescentaram-se dois
	 * Execution Degree para um plano curricular.
	 * 
	 * Os alunos de planos 2002/2004 têm associado as guias apenas a um Execution Degree. Deve-se
	 * descobrir quais as guias que pretencem ao segundo Execution Degre e modificá-las.
	 *  
	 */

	public static void main(String[] args)
	{
      
		try
		{
			//open linking for data base
			PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();
			broker.clearCache();
			broker.beginTransaction();

			//student's list of degree curricular plan 2002/2004
			List studentList = getStudents(broker);
			if (studentList == null || studentList.size() <= 0)
			{
				throw new Exception("Students list of 2002/2004 is invalid!");
			}

			//For each student analyze all guide
			ListIterator iterator = studentList.listIterator();
			while (iterator.hasNext())
			{

				try
				{
					IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) iterator
							.next();

					if (studentCurricularPlan == null || studentCurricularPlan.getStudent() == null
							|| studentCurricularPlan.getStudent().getPerson() == null)
					{
						throw new Exception("Students data are invalid!");
					}
					System.out
							.println("------------------------------------------------------------------------");
					System.out.println("STUDENT: number= "
							+ studentCurricularPlan.getStudent().getNumber());
					System.out.println("person id= "
							+ studentCurricularPlan.getStudent().getPerson().getIdInternal());
					System.out.println("student plan= " + studentCurricularPlan.getIdInternal());
					System.out.println("degree plan= "
							+ studentCurricularPlan.getDegreeCurricularPlan().getName());

					//read all guide of the student
					List guideList = getStudentGuides(broker, studentCurricularPlan.getStudent()
							.getPerson().getIdInternal());
					if (guideList == null || guideList.size() <= 0)
					{
						throw new Exception("Student without Guide!");
					}
					System.out.println("guias= " + guideList.size());
					//Cloner and order guides list
					List infoGuideList = clonerGuideList(guideList);
					ComparatorChain comparatorChain = new ComparatorChain();
					comparatorChain.addComparator(new BeanComparator("year"));
					comparatorChain.addComparator(new BeanComparator("creationDate"));
					comparatorChain.addComparator(new BeanComparator("number"));
					Collections.sort(infoGuideList, comparatorChain);

					//read the two gartuity situation of the student, one for 2002/2003 year and the
					// other 2003/2004 year
					List situationGratuityList = getGratuitySituation(broker, studentCurricularPlan
							.getIdInternal());
					if (situationGratuityList == null || situationGratuityList.size() != 2)
					{
						throw new Exception("Students without gratuity Situation!");
					}
					System.out.println("isenções= " + situationGratuityList.size());
					//cloner the list
					List infoSituationGratuityList = clonerSituationGratuityList(situationGratuityList);

					guideCorrection(broker, studentCurricularPlan, infoGuideList, infoSituationGratuityList);
				} catch (Exception e)
				{
					e.printStackTrace(System.out);
					continue;
				}
			}

			//close linking for data base
			broker.commitTransaction();
			broker.clearCache();
			broker.close();
		} catch (Exception e)
		{
			e.printStackTrace(System.out);
		}
		System.out.println("Guides updated : ");
		System.out.println("  Done !");

	}

	/**
	 * @param broker
	 * @return a student list that belong to degree curricular plans of 2002/2004
	 */
	private static List getStudents(PersistenceBroker broker)
	{

		Criteria criteria = new Criteria();
		criteria.addLike("degreeCurricularPlan.name", "%02/04");
		criteria.addNotNull("specialization");

		Query query = new QueryByCriteria(StudentCurricularPlan.class, criteria);

		List studentList = (List) broker.getCollectionByQuery(query);

		return studentList;
	}

	/**
	 * @param broker
	 * @param person
	 *            id
	 * @return a guide list of this student
	 */
	private static List getStudentGuides(PersistenceBroker broker, Integer personID)
	{

		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyPerson", personID);

		Query query = new QueryByCriteria(Guide.class, criteria);

		List guideList = (List) broker.getCollectionByQuery(query);

		return guideList;
	}

	/**
	 * @param broker
	 * @param studentPlanID
	 * @return a list with two gratuity situation of the student
	 */
	private static List getGratuitySituation(PersistenceBroker broker, Integer studentPlanID)
	{

		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyStudentCurricularPlan", studentPlanID);

		Query query = new QueryByCriteria(GratuitySituation.class, criteria);

		List gratuitySituationList = (List) broker.getCollectionByQuery(query);

		return gratuitySituationList;
	}

	/**
	 * @param guideList
	 * @return a list with infoGuide, ie cloner the parameter list
	 */
	private static List clonerGuideList(List guideList)
	{

		List infoGuideList = (List) CollectionUtils.collect(guideList, new Transformer()
		{

			public Object transform(Object arg0)
			{

				IGuide guide = (IGuide) arg0;
				InfoGuide infoGuide = Cloner.copyIGuide2InfoGuide(guide);
				return infoGuide;
			}
		});
		return infoGuideList;
	}

	/**
	 * @param guideList
	 * @return a list with infoGratuitySituation, ie cloner the parameter list
	 */
	private static List clonerSituationGratuityList(List situationGratuityList)
	{

		List infoGratuitySitatuationList = (List) CollectionUtils.collect(situationGratuityList,
				new Transformer()
				{

					public Object transform(Object arg0)
					{

						IGratuitySituation gratuitySituation = (IGratuitySituation) arg0;
						InfoGratuitySituation infoGratuitySituation = Cloner
								.copyIGratuitySituation2InfoGratuitySituation(gratuitySituation);

						return infoGratuitySituation;
					}
				});

		return infoGratuitySitatuationList;
	}

	/**
	 * @param broker
	 * @param guideList
	 * @param situationGratuity
	 */
	private static void guideCorrection(PersistenceBroker broker, IStudentCurricularPlan studentCurricularPlan, List infoGuideList,
			List infoSituationGratuityList)
	{

		HashMap guideByYear = new HashMap();
		guideByYear.put(new String("2002/2003"), new ArrayList());
		((List) guideByYear.get(new String("2002/2003"))).add(new Double(0.0));

		guideByYear.put(new String("2003/2004"), new ArrayList());
		((List) guideByYear.get(new String("2003/2004"))).add(new Double(0.0));

		HashMap gratuityTotalValueByYear = new HashMap();

		try
		{
			findGratuityTotalValue(infoSituationGratuityList, gratuityTotalValueByYear);

			//For each guide
			ListIterator iterator = infoGuideList.listIterator();
			while (iterator.hasNext())
			{
				InfoGuide infoGuide = (InfoGuide) iterator.next();

				//if guide is annulled that ignore it
				if (!findSituation(infoGuide).equals(SituationOfGuide.ANNULLED_TYPE))
				{
					double gratuityValueGuide = calculateGratuityValue(infoGuide);
					if (gratuityValueGuide > 0.0)
					{
						if (infoGuide.getYear().equals(new Integer(2002)))
						{
							if (!infoGuide.getInfoExecutionDegree().getInfoExecutionYear().getYear()
									.equals("2002/2003"))
							{
								System.out
										.println("--->Guide of 2002 but not linking with the year 2002/2003");
								//GUIDE: modify execution degree
							}

							((List) guideByYear.get("2002/2003")).add(infoGuide);
						} else if (infoGuide.getYear().equals(new Integer(2003)))
						{
							//to find at what year this guide must belong and adds it to the correct
							// year
							findCorrectYearOfGuide(infoGuide, gratuityValueGuide, guideByYear,
									gratuityTotalValueByYear);
						}
					}
				}
			}
			
			updateGuides(broker, studentCurricularPlan, guideByYear);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * @param infoSituationGratuityList
	 * @throws Exception
	 */
	private static void findGratuityTotalValue(List infoSituationGratuityList,
			HashMap gratuityTotalValueByYear) throws Exception
	{

		ListIterator iteratorGratuity = infoSituationGratuityList.listIterator();
		while (iteratorGratuity.hasNext())
		{
			InfoGratuitySituation infoGratuitySituation = (InfoGratuitySituation) iteratorGratuity
					.next();

			//calculate gratuity value to pay
			double exemptionDiscount = infoGratuitySituation.getInfoGratuityValues().getAnualValue()
					.doubleValue()
					* (infoGratuitySituation.getExemptionPercentage().doubleValue() / 100.0);
			double gratuityValue = infoGratuitySituation.getInfoGratuityValues().getAnualValue()
					.doubleValue()
					- exemptionDiscount;

			//put in the correct year
			if (infoGratuitySituation.getInfoGratuityValues().getInfoExecutionDegree()
					.getInfoExecutionYear().getYear().equals("2002/2003"))
			{
				gratuityTotalValueByYear.put("2002/2003", new Double(gratuityValue));
			} else if (infoGratuitySituation.getInfoGratuityValues().getInfoExecutionDegree()
					.getInfoExecutionYear().getYear().equals("2003/2004"))
			{
				gratuityTotalValueByYear.put("2003/2004", new Double(gratuityValue));
			} else
			{
				throw new Exception("Gratuity Total value is not valid!");
			}
		}
	}

	/**
	 * @param infoGuide
	 * @return
	 */
	private static SituationOfGuide findSituation(InfoGuide infoGuide)
	{

		SituationOfGuide situationOfGuide = null;
		try
		{
			List infoGuideSituations = infoGuide.getInfoGuideSituations();
			if (infoGuideSituations == null || infoGuideSituations.size() <= 0)
			{
				throw new Exception("Guides " + infoGuide.getNumber() + " without Situation!");
			}

			Collections.sort(infoGuideSituations, new BeanComparator("date"));

			situationOfGuide = ((InfoGuideSituation) infoGuideSituations
					.get(infoGuideSituations.size() - 1)).getSituation();
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return situationOfGuide;
	}

	/**
	 * @param infoGuide
	 * @return
	 */
	private static double calculateGratuityValue(InfoGuide infoGuide)
	{

		double gratuityValue = 0.0;

		try
		{
			//guide entries
			List infoGuideEntries = infoGuide.getInfoGuideEntries();
			if (infoGuideEntries == null || infoGuideEntries.size() <= 0)
			{
				throw new Exception("Guides " + infoGuide.getNumber() + " without Entries!");
			}

			ListIterator iterator = infoGuideEntries.listIterator();
			while (iterator.hasNext())
			{
				InfoGuideEntry infoGuideEntry = (InfoGuideEntry) iterator.next();

				if (infoGuideEntry.getDocumentType().equals(DocumentType.INSURANCE_TYPE)
						|| infoGuideEntry.getDocumentType().equals(DocumentType.GRATUITY_TYPE))
				{
					gratuityValue = gratuityValue + infoGuideEntry.getPrice().doubleValue();
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return gratuityValue;
	}

	/**
	 * @param broker
	 * @param infoGuide
	 */
	private static void findCorrectYearOfGuide(InfoGuide infoGuide, double gratuityValueGuide,
			HashMap guideByYear, HashMap gratuityTotalValueByYear)
	{

		try
		{
			//YEAR 2002/2003

			//guides linking with the year 2002/2003
			List guide20022003 = (List) guideByYear.get(new String("2002/2003"));
			//it keeps the value that it is sum with each guide
			Double value20022003 = (Double) guide20022003.get(0);
			//it keeps the gratuity total value that the student should payed
			Double gratuityTotalValue20022003 = (Double) gratuityTotalValueByYear.get(new String(
					"2002/2003"));

			double valueMoreGuide = value20022003.doubleValue() + gratuityValueGuide;
			if (valueMoreGuide <= (gratuityTotalValue20022003.doubleValue() + 2.5))//Insurance
			{
				((List) guideByYear.get("2002/2003")).add(infoGuide);
				guide20022003.set(0, new Double(valueMoreGuide));
			} else
			{
				//YEAR 2003/2004

				//guides linking with the year 2003/2004
				List guide20032004 = (List) guideByYear.get(new String("2003/2004"));
				//it keeps the value that it is sum with each guide
				Double value20032004 = (Double) guide20032004.get(0);
				//it keeps the gratuity total value that the student should payed
				Double gratuityTotalValue20032004 = (Double) gratuityTotalValueByYear.get(new String(
						"2003/2004"));

				valueMoreGuide = value20032004.doubleValue() + gratuityValueGuide;
				if (valueMoreGuide <= gratuityTotalValue20032004.doubleValue() + 2.5)//Insurance
				{
					((List) guideByYear.get("2003/2004")).add(infoGuide);
					guide20032004.set(0, new Double(valueMoreGuide));
				} else {
					throw new Exception("Guides " + infoGuide.getNumber() + " that can't find year!");
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private static void updateGuides(PersistenceBroker broker, IStudentCurricularPlan studentCurricularPlan, HashMap guideByYear)
	{
		//YEAR 2002/2003
		ICursoExecucao executionDegree = readExecutionDegree(broker, studentCurricularPlan.getDegreeCurricularPlan().getIdInternal(), "2002/2003");
			
		//guides linking with the year 2002/2003
		List guides = (List) guideByYear.get(new String("2002/2003"));
		guides.remove(0);//apaga valor da guia de propinas
		ListIterator iterator = guides.listIterator();
		while (iterator.hasNext())
		{
			InfoGuide infoGuide = (InfoGuide) iterator.next();
			IGuide guide = Cloner.copyInfoGuide2IGuide(infoGuide);
			
			if(!infoGuide.getInfoExecutionDegree().getIdInternal().equals(executionDegree.getIdInternal())){
				
				updateexecutionDegreeOfGuide(broker, guide, executionDegree);
			}
		}
			
		
		//YEAR 2003/2004
		executionDegree = readExecutionDegree(broker, studentCurricularPlan.getDegreeCurricularPlan().getIdInternal(), "2003/2004");
			
		//guides linking with the year 2003/2004
		guides = (List) guideByYear.get(new String("2003/2004"));
		guides.remove(0);//apaga valor da guia de propinas
		iterator = guides.listIterator();
		while (iterator.hasNext())
		{
			InfoGuide infoGuide = (InfoGuide) iterator.next();
			IGuide guide = Cloner.copyInfoGuide2IGuide(infoGuide);
			
			if(!infoGuide.getInfoExecutionDegree().getIdInternal().equals(executionDegree.getIdInternal())){
				
				updateexecutionDegreeOfGuide(broker, guide, executionDegree);
			}
		}
	}

	/**
	 * @param integer
	 * @param string
	 * @return
	 */
	private static ICursoExecucao readExecutionDegree(PersistenceBroker broker, Integer degreeCurricularPlanID, String year)
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("curricularPlan.idInternal", degreeCurricularPlanID);
		criteria.addEqualTo("executionYear.year", year);
		
		Query query = new QueryByCriteria(CursoExecucao.class, criteria);

		List resultExecutionDegree = (List) broker.getCollectionByQuery(query);			
			
		
		return (ICursoExecucao) resultExecutionDegree.get(0);
	}
	
	/**
	 * @param broker
	 * @param guide
	 * @param executionDegree
	 */
	private static void updateexecutionDegreeOfGuide(PersistenceBroker broker, IGuide guide, ICursoExecucao executionDegree)
	{
		guide.setExecutionDegree(executionDegree);
		broker.store(guide);
	}
}
