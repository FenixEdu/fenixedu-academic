import java.util.Iterator;
import java.util.List;

import DataBeans.InfoStudentTestQuestion;
import DataBeans.util.Cloner;
import Dominio.DistributedTest;
import Dominio.IDistributedTest;
import Dominio.IStudentTestQuestion;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudentTestQuestion;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import UtilTests.ParseQuestion;

/*
 * Created on Nov 5, 2003 by jpvl
 *  
 */

/**
 * @author jpvl
 */
public class testUpdateStudents
{

	public static void main(String[] args) throws Exception
	{

		ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
		
		IPersistentStudentTestQuestion persistentStudentTestQuestion = persistentSuport.getIPersistentStudentTestQuestion();

		Integer[] tests = { new Integer(2), new Integer(3), new Integer(23), new Integer(24), new Integer(25), new Integer(26), new Integer(27)};
		for (int i = 0; i < tests.length; i++)
		{
			
			persistentSuport.iniciarTransaccao();
			Integer test = tests[i];
			System.out.println("Test "+ test + "...");
			IDistributedTest distributedTest = new DistributedTest(test);
			distributedTest = (IDistributedTest) persistentSuport.getIPersistentDistributedTest().readByOId(distributedTest, false);
			if (distributedTest == null)
				throw new InvalidArgumentsServiceException();
			System.out.println("Reading test questions...");
			List studentsTestQuestionList = (List) persistentStudentTestQuestion.readByDistributedTest(distributedTest);
			System.out.println("I've got que test questions...");
			Iterator studentsTestQuestionIt = studentsTestQuestionList.iterator();

			ParseQuestion parse = new ParseQuestion();
			System.out.println(studentsTestQuestionList.size() + " questions...");
			persistentSuport.confirmarTransaccao();
			while (studentsTestQuestionIt.hasNext())
			{

				IStudentTestQuestion studentTestQuestion = (IStudentTestQuestion) studentsTestQuestionIt.next();
				InfoStudentTestQuestion infoStudentTestQuestion = Cloner.copyIStudentTestQuestion2InfoStudentTestQuestion(studentTestQuestion);
				try
				{
					infoStudentTestQuestion.setQuestion(
						parse.parseQuestion(infoStudentTestQuestion.getQuestion().getXmlFile(), infoStudentTestQuestion.getQuestion()));
					infoStudentTestQuestion = parse.getOptionsShuffle(infoStudentTestQuestion);
				} catch (Exception e)
				{
					throw new FenixServiceException(e);
				}

				if (infoStudentTestQuestion.getResponse().intValue() != 0)
				{
					persistentSuport.iniciarTransaccao();
					persistentStudentTestQuestion.lockWrite(studentTestQuestion);
					if (infoStudentTestQuestion.getQuestion().getCorrectResponse().contains(studentTestQuestion.getResponse()))
					{
						studentTestQuestion.setTestQuestionMark(new Double(studentTestQuestion.getTestQuestionValue().doubleValue()));
					} else
					{
						studentTestQuestion.setTestQuestionMark(
							new Double(
								- (
									infoStudentTestQuestion.getTestQuestionValue().intValue()
										* (java.lang.Math.pow(infoStudentTestQuestion.getQuestion().getOptionNumber().intValue() - 1, -1)))));
					}
					persistentSuport.confirmarTransaccao();
				}

			}
		}

	}
}
