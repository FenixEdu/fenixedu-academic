/*
 * Created on 1/Abr/2004
 *
 */
package ServidorAplicacao.Servicos.sop.exams;

import java.util.Calendar;

import DataBeans.InfoViewExam;
import ServidorAplicacao.Servico.sop.exams.ReadExamsByDate;
import ServidorAplicacao.Servicos.ServiceTestCase;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Ana e Ricardo
 *
 */
public class ReadExamsByDateTest extends ServiceTestCase
{

    /**
     * @param name
     */
    public ReadExamsByDateTest(java.lang.String testName)
    {
        super(testName);
    }

    protected String getNameOfServiceToBeTested()
    {
        return "ReadExamsByDate";
    }

    protected String getDataSetFilePath()
    {
        return "etc/datasets_templates/servicos/sop/testReadExamsByDateDataset.xml";
    }


    protected Calendar createExamDate()
    {
        Calendar examDate = Calendar.getInstance();

        examDate.set(Calendar.YEAR, 2004);
        examDate.set(Calendar.MONTH, Calendar.FEBRUARY);
        examDate.set(Calendar.DAY_OF_MONTH, 9);

        return examDate;
    }
 
    protected Calendar createNoExamDate()
    {
        Calendar examDate = Calendar.getInstance();

        examDate.set(Calendar.YEAR, 2004);
        examDate.set(Calendar.MONTH, Calendar.JANUARY);
        examDate.set(Calendar.DAY_OF_MONTH, 21);

        return examDate;
    }

    protected Calendar createExamStartTime()
    {
        Calendar examStartTime = Calendar.getInstance();

        examStartTime.set(Calendar.HOUR_OF_DAY, 10);
        examStartTime.set(Calendar.MINUTE, 0);
        examStartTime.set(Calendar.SECOND, 0);
        examStartTime.set(Calendar.MILLISECOND, 0);

        return examStartTime;
    }

    protected Calendar createExamEndTime()
    {
        Calendar examEndTime = Calendar.getInstance();

        examEndTime.set(Calendar.HOUR_OF_DAY, 11);
        examEndTime.set(Calendar.MINUTE, 0);
        examEndTime.set(Calendar.SECOND, 0);
        examEndTime.set(Calendar.MILLISECOND, 0);

        return examEndTime;
    }

	// no exams
	public void testSuccessfullReadExamsByDateNoExams()
	{
		Calendar examDate = createNoExamDate();
		Calendar examStartTime = createExamStartTime();
		Calendar examEndTime = createExamEndTime();

		ReadExamsByDate service = ReadExamsByDate.getService();

		ISuportePersistente sp;

		try
		{
			sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();

			InfoViewExam exams = service.run(examDate, examStartTime, examEndTime);
						
			sp.confirmarTransaccao();
			compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
			
			assertEquals(exams.getInfoViewExamsByDayAndShift().size(), 0);
		}
		catch (ExcepcaoPersistencia e)
		{
			fail("testSuccessfullReadExamsByDateNoExams - persistencia");            
		}
		catch (Exception e)
		{
			fail("testSuccessfullReadExamsByDateNoExams");
		}
	}
 

	// 4 exams
	public void testSuccessfullReadExamsByDate()
	{
		Calendar examDate = createExamDate();
		Calendar examStartTime = null;
		Calendar examEndTime = null;

		ReadExamsByDate service = ReadExamsByDate.getService();

		ISuportePersistente sp;

		try
		{
			sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();

			InfoViewExam exams = service.run(examDate, examStartTime, examEndTime);
						
			sp.confirmarTransaccao();
			compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
			
			assertEquals(exams.getInfoViewExamsByDayAndShift().size(), 4);
		}
		catch (ExcepcaoPersistencia e)
		{
			fail("testSuccessfullReadExamsByDate - persistencia");            
		}
		catch (Exception e)
		{
			fail("testSuccessfullReadExamsByDate");
		}
	}

	// 3 exams
	public void testSuccessfullReadExamsByDateAndStartTime()
	{
		Calendar examDate = createExamDate();
		Calendar examStartTime = createExamStartTime();
		Calendar examEndTime = null;

		ReadExamsByDate service = ReadExamsByDate.getService();

		ISuportePersistente sp;

		try
		{
			sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();

			InfoViewExam exams = service.run(examDate, examStartTime, examEndTime);
						
			sp.confirmarTransaccao();
			compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
			
			assertEquals(exams.getInfoViewExamsByDayAndShift().size(), 3);
		}
		catch (ExcepcaoPersistencia e)
		{
			fail("testSuccessfullReadExamsByDateAndStartTime - persistencia");            
		}
		catch (Exception e)
		{
			fail("testSuccessfullReadExamsByDateAndStartTime");
		}
	}

	// 2 exams
	public void testSuccessfullReadExamsByDateAndEndTime()
	{
		Calendar examDate = createExamDate();
		Calendar examStartTime = null;
		Calendar examEndTime = createExamEndTime();

		ReadExamsByDate service = ReadExamsByDate.getService();

		ISuportePersistente sp;

		try
		{
			sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();

			InfoViewExam exams = service.run(examDate, examStartTime, examEndTime);
						
			sp.confirmarTransaccao();
			compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
			
			assertEquals(exams.getInfoViewExamsByDayAndShift().size(), 2);
		}
		catch (ExcepcaoPersistencia e)
		{
			fail("testSuccessfullReadExamsByDateAndEndTime - persistencia");            
		}
		catch (Exception e)
		{
			fail("testSuccessfullReadExamsByDateAndEndTime");
		}
	}

	// 2 exams
	public void testSuccessfullReadExamsByDateAndStartTimeAndEndTime()
	{
		Calendar examDate = createExamDate();
		Calendar examStartTime = createExamStartTime();
		Calendar examEndTime = createExamEndTime();

		ReadExamsByDate service = ReadExamsByDate.getService();

		ISuportePersistente sp;

		try
		{
			sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();

			InfoViewExam exams = service.run(examDate, examStartTime, examEndTime);
						
			sp.confirmarTransaccao();
			compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
			
			assertEquals(exams.getInfoViewExamsByDayAndShift().size(), 1);
		}
		catch (ExcepcaoPersistencia e)
		{
			fail("testSuccessfullReadExamsByDateAndStartTimeAndEndTime - persistencia");            
		}
		catch (Exception e)
		{
			fail("testSuccessfullReadExamsByDateAndStartTimeAndEndTime");
		}
	}

}
