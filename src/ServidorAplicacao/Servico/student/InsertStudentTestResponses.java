/*
 * Created on 3/Set/2003
 *
 */
package ServidorAplicacao.Servico.student;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoStudentTestQuestion;
import DataBeans.comparators.CalendarDateComparator;
import DataBeans.comparators.CalendarHourComparator;
import DataBeans.util.Cloner;
import Dominio.DistributedTest;
import Dominio.IDistributedTest;
import Dominio.IStudent;
import Dominio.IStudentTestLog;
import Dominio.IStudentTestQuestion;
import Dominio.StudentTestLog;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudentTestLog;
import ServidorPersistente.IPersistentStudentTestQuestion;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TestType;
import UtilTests.ParseQuestion;

/**
 * @author Susana Fernandes
 */

public class InsertStudentTestResponses implements IService {
	
	private String path = new String();

	
	

	
	public boolean run(
		String userName,
		Integer distributedTestId,
		String[] options,
		String path)
		throws FenixServiceException {
		List infoStudentTestQuestionList = new ArrayList();
		this.path = path.replace('\\', '/');
		try
		{
			ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
			IStudent student = persistentSuport.getIPersistentStudent().readByUsername(userName);
			if (student == null)
				throw new FenixServiceException();
			IDistributedTest distributedTest = new DistributedTest(distributedTestId);
			if (distributedTest == null)
				throw new FenixServiceException();
			distributedTest =
				(IDistributedTest) persistentSuport.getIPersistentDistributedTest().readByOId(
					distributedTest,
					false);
			String event = new String("Submeter Teste;");
			for (int i = 0; i < options.length; i++)
				event = event.concat(options[i] + ";");

			if (compareDates(distributedTest.getEndDate(), distributedTest.getEndHour()))
			{
				List studentTestQuestionList =
					persistentSuport
						.getIPersistentStudentTestQuestion()
						.readByStudentAndDistributedTest(
						student,
						distributedTest);
				Iterator it = studentTestQuestionList.iterator();
				if (studentTestQuestionList.size() == 0)
					return false;
				IPersistentStudentTestQuestion persistentStudentTestQuestion =
					persistentSuport.getIPersistentStudentTestQuestion();
				ParseQuestion parse = new ParseQuestion();
				while (it.hasNext())
				{
					IStudentTestQuestion studentTestQuestion = (IStudentTestQuestion) it.next();
					if (studentTestQuestion.getResponse().intValue() != 0
						&& distributedTest.getTestType().equals(new TestType(1)))
					{
						//não pode aceitar nova resposta
					}
					else
					{
						InfoStudentTestQuestion infoStudentTestQuestion =
							Cloner.copyIStudentTestQuestion2InfoStudentTestQuestion(studentTestQuestion);
						try
						{
							infoStudentTestQuestion.setQuestion(
								parse.parseQuestion(
									infoStudentTestQuestion.getQuestion().getXmlFile(),
									infoStudentTestQuestion.getQuestion(),
									this.path));
							infoStudentTestQuestion =
								parse.getOptionsShuffle(infoStudentTestQuestion, this.path);
						}
						catch (Exception e)
						{
							throw new FenixServiceException(e);
						}
						studentTestQuestion.setResponse(
							new Integer(
								options[studentTestQuestion.getTestQuestionOrder().intValue() - 1]));
						if (studentTestQuestion.getResponse().intValue() != 0)
						{

							if (infoStudentTestQuestion
								.getQuestion()
								.getCorrectResponse()
								.contains(studentTestQuestion.getResponse()))
								studentTestQuestion.setTestQuestionMark(
									new Double(
										studentTestQuestion.getTestQuestionValue().doubleValue()));
							else
								studentTestQuestion.setTestQuestionMark(
									new Double(
										- (
											infoStudentTestQuestion.getTestQuestionValue().intValue()
												* (java
													.lang
													.Math
													.pow(
														infoStudentTestQuestion
															.getQuestion()
															.getOptionNumber()
															.intValue()
															- 1,
														-1)))));
						}
						persistentStudentTestQuestion.lockWrite(studentTestQuestion);
					}
				}
				IPersistentStudentTestLog persistentStudentTestLog =
					persistentSuport.getIPersistentStudentTestLog();

				IStudentTestLog studentTestLog = new StudentTestLog();
				studentTestLog.setDistributedTest(distributedTest);
				studentTestLog.setStudent(student);
				studentTestLog.setDate(null);
				studentTestLog.setEvent(event);

				persistentStudentTestLog.simpleLockWrite(studentTestLog);
			}
			else
				return false;
		}
		catch (ExcepcaoPersistencia e)
		{
			throw new FenixServiceException(e);
		}
		return true;
	}

	private boolean compareDates(Calendar date, Calendar hour)
	{
		Calendar calendar = Calendar.getInstance();
		CalendarDateComparator dateComparator = new CalendarDateComparator();
		CalendarHourComparator hourComparator = new CalendarHourComparator();
		if (dateComparator.compare(calendar, date) <= 0)
		{
			if (dateComparator.compare(calendar, date) == 0)
				if (hourComparator.compare(calendar, hour) <= 0)
					return true;
				else
					return false;
			return true;
		}
		return false;
	}
    /**
     * 
     */
    public InsertStudentTestResponses()
    {
     
    }

}
