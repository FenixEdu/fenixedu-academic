/*
 * Created on Mar 31, 2004
 *
 */
package ServidorAplicacao.Servicos.sop.exams;

import java.util.Calendar;

import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.sop.exams.CreateExamNew;
import ServidorAplicacao.Servicos.ServiceTestCase;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.Season;

/**
 * @author Ana e Ricardo
 *
 */
public class CreateExamNewTest extends ServiceTestCase
{

    /**
     * @param name
     */
    public CreateExamNewTest(java.lang.String testName)
    {
        super(testName);
    }

    /* (non-Javadoc)
     * @see ServidorAplicacao.Servicos.sop.exams.ServiceNeedsAuthenticationTestCase#getNameOfServiceToBeTested()
     */
    protected String getNameOfServiceToBeTested()
    {
        return "CreateExamNew";
    }

    /* (non-Javadoc)
     * @see ServidorAplicacao.Servicos.sop.exams.ServiceNeedsAuthenticationTestCase#getDataSetFilePath()
     */
    protected String getDataSetFilePath()
    {
        return "etc/datasets_templates/servicos/sop/testExamsV4dataset.xml";
    }

    protected String getExpectedDataSetFilePath()
    {
        return "etc/datasets_templates/servicos/sop/testExpectedExamsV4dataset.xml";
    }

	protected String getExpectedDataSetWithNoRoomsFilePath()
	{
		return "etc/datasets_templates/servicos/sop/testExpectedWithNoRoomsExamsV4dataset.xml";
	}


    protected Calendar createExamDate()
    {
        Calendar examDate = Calendar.getInstance();

        examDate.set(Calendar.YEAR, 2004);
        examDate.set(Calendar.MONTH, Calendar.FEBRUARY);
        examDate.set(Calendar.DAY_OF_MONTH, 20);

        return examDate;
    }

    protected Calendar createExamStartTime()
    {
        Calendar examStartTime = Calendar.getInstance();

        examStartTime.set(Calendar.HOUR_OF_DAY, 9);
        examStartTime.set(Calendar.MINUTE, 0);
        examStartTime.set(Calendar.SECOND, 0);
        examStartTime.set(Calendar.MILLISECOND, 0);

        return examStartTime;
    }

    protected Calendar createExamEndTime()
    {
        Calendar examEndTime = Calendar.getInstance();

        examEndTime.set(Calendar.HOUR_OF_DAY, 9);
        examEndTime.set(Calendar.MINUTE, 20);
        examEndTime.set(Calendar.SECOND, 0);
        examEndTime.set(Calendar.MILLISECOND, 0);

        return examEndTime;
    }

    // test successfull create exam 
    public void testSuccessfullCreateExamNew()
    {
        Calendar examDate = createExamDate();
        Calendar examStartTime = createExamStartTime();
        Calendar examEndTime = createExamEndTime();
        Season season = new Season(new Integer(1));
        String[] executionCourseIDArray = { "36422", "36721" };
        String[] scopeIDArray =
            {
                "731",
                "17679",
                "17680",
                "17681",
                "17682",
                "724",
                "17222",
                "17223",
                "17224",
                "2915",
                "17868",
                "17869",
                "18276",
                "17335" };
        String[] roomIDArray = { "360", "835", "836" };

        CreateExamNew service = CreateExamNew.getService();

        ISuportePersistente sp;

        try
        {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            service.run(
                examDate,
                examStartTime,
                examEndTime,
                season,
                executionCourseIDArray,
                scopeIDArray,
                roomIDArray);

            sp.confirmarTransaccao();
            compareDataSetUsingExceptedDataSetTableColumns(getExpectedDataSetFilePath());
        }
        catch (FenixServiceException ex)
        {
            //sp.cancelarTransaccao();
            fail("testSuccessfullCreateExamNew - Fenix Service Exception " + ex);
        }
        catch (Exception ex)
        {
            //sp.cancelarTransaccao();			
            fail("testSuccessfullCreateExamNew - Exception " + ex);
        }
    }

    // test successfull create exam with no rooms
    public void testSuccessfullCreateExamNewWithNoRooms()
    {
        Calendar examDate = createExamDate();
        Calendar examStartTime = createExamStartTime();
        Calendar examEndTime = createExamEndTime();
        Season season = new Season(new Integer(1));
        String[] executionCourseIDArray = { "36422", "36721" };
        String[] scopeIDArray =
            {
                "731",
                "17679",
                "17680",
                "17681",
                "17682",
                "724",
                "17222",
                "17223",
                "17224",
                "2915",
                "17868",
                "17869",
                "18276",
                "17335" };
        String[] roomIDArray = {
        };

        CreateExamNew service = CreateExamNew.getService();

        ISuportePersistente sp;

        try
        {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            service.run(
                examDate,
                examStartTime,
                examEndTime,
                season,
                executionCourseIDArray,
                scopeIDArray,
                roomIDArray);

            sp.confirmarTransaccao();
            compareDataSetUsingExceptedDataSetTableColumns(getExpectedDataSetWithNoRoomsFilePath());
        }
        catch (FenixServiceException ex)
        {
            //sp.cancelarTransaccao();
            fail("testSuccessfullCreateExamNewWithNoRooms - Fenix Service Exception " + ex);
        }
        catch (Exception ex)
        {
            //sp.cancelarTransaccao();			
            fail("testSuccessfullCreateExamNewWithNoRooms - Exception " + ex);
        }
    }

    // one of the execution courses does not exists
    public void testUnexistingExecutionCourse()
    {
        Calendar examDate = createExamDate();
        Calendar examStartTime = createExamStartTime();
        Calendar examEndTime = createExamEndTime();
        Season season = new Season(new Integer(1));
        String[] executionCourseIDArray = { "36422", "36721", "1" };
        String[] scopeIDArray =
            {
                "731",
                "17679",
                "17680",
                "17681",
                "17682",
                "724",
                "17222",
                "17223",
                "17224",
                "2915",
                "17868",
                "17869",
                "18276",
                "17335" };
        String[] roomIDArray = { "360", "835", "836" };

        CreateExamNew service = CreateExamNew.getService();

        ISuportePersistente sp = null;

        try
        {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            service.run(
                examDate,
                examStartTime,
                examEndTime,
                season,
                executionCourseIDArray,
                scopeIDArray,
                roomIDArray);
            fail("testUnexistingExecutionCourse");
            sp.cancelarTransaccao();
        }
        catch (FenixServiceException ex)
        {
            try
            {
                sp.cancelarTransaccao();
            }
            catch (ExcepcaoPersistencia e)
            {

                fail("testUnexistingExecutionCourse - Exception cancelar transacção " + ex);
            }
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
        }
        catch (Exception ex)
        {
            //sp.cancelarTransaccao();			
            fail("testUnexistingExecutionCourse - Exception (not fenix service) " + ex);
        }

    }

    // no execution courses
    public void testNoExecutionCourse()
    {
        Calendar examDate = createExamDate();
        Calendar examStartTime = createExamStartTime();
        Calendar examEndTime = createExamEndTime();
        Season season = new Season(new Integer(1));
        String[] executionCourseIDArray = {
        };
        String[] scopeIDArray =
            {
                "731",
                "17679",
                "17680",
                "17681",
                "17682",
                "724",
                "17222",
                "17223",
                "17224",
                "2915",
                "17868",
                "17869",
                "18276",
                "17335" };
        String[] roomIDArray = { "360", "835", "836" };

        CreateExamNew service = CreateExamNew.getService();

        ISuportePersistente sp = null;

        try
        {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            service.run(
                examDate,
                examStartTime,
                examEndTime,
                season,
                executionCourseIDArray,
                scopeIDArray,
                roomIDArray);
            fail("testNoExecutionCourse");
            sp.cancelarTransaccao();
        }
        catch (FenixServiceException ex)
        {
            try
            {
                sp.cancelarTransaccao();
            }
            catch (ExcepcaoPersistencia e)
            {

                fail("testNoExecutionCourse - Exception (cancelar transacção) " + ex);
            }
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
        }
        catch (Exception ex)
        {
            //sp.cancelarTransaccao();			
            fail("testNoExecutionCourse - Exception (not fenix service) " + ex);
        }

    }

    // one of the scopes does not exists
    public void testUnexistingScope()
    {
        Calendar examDate = createExamDate();
        Calendar examStartTime = createExamStartTime();
        Calendar examEndTime = createExamEndTime();
        Season season = new Season(new Integer(1));
        String[] executionCourseIDArray = { "36422", "36721" };
        String[] scopeIDArray =
            {
                "731",
                "17679",
                "17680",
                "17681",
                "17682",
                "724",
                "17222",
                "17223",
                "17224",
                "2915",
                "17868",
                "17869",
                "18276",
                "17335",
                "1" };
        String[] roomIDArray = { "360", "835", "836" };

        CreateExamNew service = CreateExamNew.getService();

        ISuportePersistente sp = null;

        try
        {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            service.run(
                examDate,
                examStartTime,
                examEndTime,
                season,
                executionCourseIDArray,
                scopeIDArray,
                roomIDArray);
            fail("testUnexistingScope");
            sp.cancelarTransaccao();
        }
        catch (FenixServiceException ex)
        {
            try
            {
                sp.cancelarTransaccao();
            }
            catch (ExcepcaoPersistencia e)
            {

                fail("testUnexistingScope - Exception cancelar transacção " + ex);
            }
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
        }
        catch (Exception ex)
        {
            //sp.cancelarTransaccao();			
            fail("testUnexistingScope - Exception (not fenix service) " + ex);
        }

    }

    // no scopes
    public void testNoScope()
    {
        Calendar examDate = createExamDate();
        Calendar examStartTime = createExamStartTime();
        Calendar examEndTime = createExamEndTime();
        Season season = new Season(new Integer(1));
        String[] executionCourseIDArray = { "36422", "36721" };
        String[] scopeIDArray = {
        };
        String[] roomIDArray = { "360", "835", "836" };

        CreateExamNew service = CreateExamNew.getService();

        ISuportePersistente sp = null;

        try
        {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            service.run(
                examDate,
                examStartTime,
                examEndTime,
                season,
                executionCourseIDArray,
                scopeIDArray,
                roomIDArray);
            fail("testNoScope");
            sp.cancelarTransaccao();
        }
        catch (FenixServiceException ex)
        {
            try
            {
                sp.cancelarTransaccao();
            }
            catch (ExcepcaoPersistencia e)
            {

                fail("testNoScope - Exception (cancelar transacção) " + ex);
            }
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
        }
        catch (Exception ex)
        {
            //sp.cancelarTransaccao();			
            fail("testNoScope - Exception (not fenix service) " + ex);
        }

    }

    // one of the rooms does not exists
    public void testUnexistingRoom()
    {
        Calendar examDate = createExamDate();
        Calendar examStartTime = createExamStartTime();
        Calendar examEndTime = createExamEndTime();
        Season season = new Season(new Integer(1));
        String[] executionCourseIDArray = { "36422", "36721" };
        String[] scopeIDArray =
            {
                "731",
                "17679",
                "17680",
                "17681",
                "17682",
                "724",
                "17222",
                "17223",
                "17224",
                "2915",
                "17868",
                "17869",
                "18276",
                "17335" };
        String[] roomIDArray = { "360", "835", "836", "1" };

        CreateExamNew service = CreateExamNew.getService();

        ISuportePersistente sp = null;

        try
        {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            service.run(
                examDate,
                examStartTime,
                examEndTime,
                season,
                executionCourseIDArray,
                scopeIDArray,
                roomIDArray);
            fail("testUnexistingRoom");
            sp.cancelarTransaccao();
        }
        catch (FenixServiceException ex)
        {
            try
            {
                sp.cancelarTransaccao();
            }
            catch (ExcepcaoPersistencia e)
            {

                fail("testUnexistingRoom - Exception cancelar transacção " + ex);
            }
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
        }
        catch (Exception ex)
        {
            //sp.cancelarTransaccao();			
            fail("testUnexistingRoom - Exception (not fenix service) " + ex);
        }
    }

    // the exam already exists
    public void testExistingExamSeason()
    {
        Calendar examDate = createExamDate();
        Calendar examStartTime = createExamStartTime();
        Calendar examEndTime = createExamEndTime();
        Season season = new Season(new Integer(2));
        String[] executionCourseIDArray = { "36422", "36721", "36723" };
        String[] scopeIDArray =
            {
                "731",
                "17679",
                "17680",
                "17681",
                "17682",
                "724",
                "17222",
                "17223",
                "17224",
                "2915",
                "17868",
                "17869",
                "18276",
                "17335",
                "681" };
        String[] roomIDArray = { "360", "835", "836" };

        CreateExamNew service = CreateExamNew.getService();

        ISuportePersistente sp = null;

        try
        {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            service.run(
                examDate,
                examStartTime,
                examEndTime,
                season,
                executionCourseIDArray,
                scopeIDArray,
                roomIDArray);
            fail("testExistingExamSeason");
            sp.cancelarTransaccao();
        }
        catch (FenixServiceException ex)
        {
            try
            {
                sp.cancelarTransaccao();
            }
            catch (ExcepcaoPersistencia e)
            {

                fail("testExistingExamSeason - Exception cancelar transacção " + ex);
            }
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
        }
        catch (Exception ex)
        {
            //sp.cancelarTransaccao();			
            fail("testExistingExamSeason - Exception (not fenix service) " + ex);
        }
    }

    // the room is already occupied 
    public void testOccupiedRoomForSameHour()
    {
        Calendar examDate = Calendar.getInstance();
        examDate.set(Calendar.YEAR, 2004);
        examDate.set(Calendar.MONTH, Calendar.FEBRUARY);
        examDate.set(Calendar.DAY_OF_MONTH, 9);

        Calendar examStartTime = Calendar.getInstance();
        examStartTime.set(Calendar.HOUR_OF_DAY, 11);
        examStartTime.set(Calendar.MINUTE, 0);
        examStartTime.set(Calendar.SECOND, 0);
        examStartTime.set(Calendar.MILLISECOND, 0);

        Calendar examEndTime = Calendar.getInstance();
        examEndTime.set(Calendar.HOUR_OF_DAY, 12);
        examEndTime.set(Calendar.MINUTE, 0);
        examEndTime.set(Calendar.SECOND, 0);
        examEndTime.set(Calendar.MILLISECOND, 0);

        Season season = new Season(new Integer(1));
        String[] executionCourseIDArray = { "36422", "36721" };
        String[] scopeIDArray =
            {
                "731",
                "17679",
                "17680",
                "17681",
                "17682",
                "724",
                "17222",
                "17223",
                "17224",
                "2915",
                "17868",
                "17869",
                "18276",
                "17335" };
        String[] roomIDArray = { "360", "835", "836", "232" };

        CreateExamNew service = CreateExamNew.getService();

        ISuportePersistente sp = null;

        try
        {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            service.run(
                examDate,
                examStartTime,
                examEndTime,
                season,
                executionCourseIDArray,
                scopeIDArray,
                roomIDArray);
            fail("testOccupiedRoomForSameHour");
            sp.cancelarTransaccao();
        }
        catch (FenixServiceException ex)
        {
            try
            {
                sp.cancelarTransaccao();
            }
            catch (ExcepcaoPersistencia e)
            {

                fail("testOccupiedRoomForSameHour - Exception cancelar transacção " + ex);
            }
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
        }
        catch (Exception ex)
        {
            //sp.cancelarTransaccao();			
            fail("testOccupiedRoomForSameHour - Exception (not fenix service) " + ex);
        }
    }


	// the room is already occupied 
	public void testOccupiedRoomForHourBelow()
	{
		Calendar examDate = Calendar.getInstance();
		examDate.set(Calendar.YEAR, 2004);
		examDate.set(Calendar.MONTH, Calendar.FEBRUARY);
		examDate.set(Calendar.DAY_OF_MONTH, 9);

		Calendar examStartTime = Calendar.getInstance();
		examStartTime.set(Calendar.HOUR_OF_DAY, 11);
		examStartTime.set(Calendar.MINUTE, 30);
		examStartTime.set(Calendar.SECOND, 0);
		examStartTime.set(Calendar.MILLISECOND, 0);

		Calendar examEndTime = Calendar.getInstance();
		examEndTime.set(Calendar.HOUR_OF_DAY, 12);
		examEndTime.set(Calendar.MINUTE, 30);
		examEndTime.set(Calendar.SECOND, 0);
		examEndTime.set(Calendar.MILLISECOND, 0);

		Season season = new Season(new Integer(1));
		String[] executionCourseIDArray = { "36422", "36721" };
		String[] scopeIDArray =
			{
				"731",
				"17679",
				"17680",
				"17681",
				"17682",
				"724",
				"17222",
				"17223",
				"17224",
				"2915",
				"17868",
				"17869",
				"18276",
				"17335" };
		String[] roomIDArray = { "360", "835", "836", "232" };

		CreateExamNew service = CreateExamNew.getService();

		ISuportePersistente sp = null;

		try
		{
			sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();

			service.run(
				examDate,
				examStartTime,
				examEndTime,
				season,
				executionCourseIDArray,
				scopeIDArray,
				roomIDArray);
			fail("testOccupiedRoomForHourBelow");
			sp.cancelarTransaccao();
		}
		catch (FenixServiceException ex)
		{
			try
			{
				sp.cancelarTransaccao();
			}
			catch (ExcepcaoPersistencia e)
			{

				fail("testOccupiedRoomForHourBelow - Exception cancelar transacção " + ex);
			}
			compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
		}
		catch (Exception ex)
		{
			//sp.cancelarTransaccao();			
			fail("testOccupiedRoomForHourBelow - Exception (not fenix service) " + ex);
		}
	}

	// the room is already occupied 
	public void testOccupiedRoomForHourAbove()
	{
		Calendar examDate = Calendar.getInstance();
		examDate.set(Calendar.YEAR, 2004);
		examDate.set(Calendar.MONTH, Calendar.FEBRUARY);
		examDate.set(Calendar.DAY_OF_MONTH, 9);

		Calendar examStartTime = Calendar.getInstance();
		examStartTime.set(Calendar.HOUR_OF_DAY, 10);
		examStartTime.set(Calendar.MINUTE, 30);
		examStartTime.set(Calendar.SECOND, 0);
		examStartTime.set(Calendar.MILLISECOND, 0);

		Calendar examEndTime = Calendar.getInstance();
		examEndTime.set(Calendar.HOUR_OF_DAY, 11);
		examEndTime.set(Calendar.MINUTE, 30);
		examEndTime.set(Calendar.SECOND, 0);
		examEndTime.set(Calendar.MILLISECOND, 0);

		Season season = new Season(new Integer(1));
		String[] executionCourseIDArray = { "36422", "36721" };
		String[] scopeIDArray =
			{
				"731",
				"17679",
				"17680",
				"17681",
				"17682",
				"724",
				"17222",
				"17223",
				"17224",
				"2915",
				"17868",
				"17869",
				"18276",
				"17335" };
		String[] roomIDArray = { "360", "835", "836", "232" };

		CreateExamNew service = CreateExamNew.getService();

		ISuportePersistente sp = null;

		try
		{
			sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();

			service.run(
				examDate,
				examStartTime,
				examEndTime,
				season,
				executionCourseIDArray,
				scopeIDArray,
				roomIDArray);
			fail("testOccupiedRoomForHourAbove");
			sp.cancelarTransaccao();
		}
		catch (FenixServiceException ex)
		{
			try
			{
				sp.cancelarTransaccao();
			}
			catch (ExcepcaoPersistencia e)
			{

				fail("testOccupiedRoomForHourAbove - Exception cancelar transacção " + ex);
			}
			compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
		}
		catch (Exception ex)
		{
			//sp.cancelarTransaccao();			
			fail("testOccupiedRoomForHourAbove - Exception (not fenix service) " + ex);
		}
	}

	// the room is already occupied 
	public void testOccupiedRoomForHourInside()
	{
		Calendar examDate = Calendar.getInstance();
		examDate.set(Calendar.YEAR, 2004);
		examDate.set(Calendar.MONTH, Calendar.FEBRUARY);
		examDate.set(Calendar.DAY_OF_MONTH, 9);

		Calendar examStartTime = Calendar.getInstance();
		examStartTime.set(Calendar.HOUR_OF_DAY, 11);
		examStartTime.set(Calendar.MINUTE, 20);
		examStartTime.set(Calendar.SECOND, 0);
		examStartTime.set(Calendar.MILLISECOND, 0);

		Calendar examEndTime = Calendar.getInstance();
		examEndTime.set(Calendar.HOUR_OF_DAY, 11);
		examEndTime.set(Calendar.MINUTE, 40);
		examEndTime.set(Calendar.SECOND, 0);
		examEndTime.set(Calendar.MILLISECOND, 0);

		Season season = new Season(new Integer(1));
		String[] executionCourseIDArray = { "36422", "36721" };
		String[] scopeIDArray =
			{
				"731",
				"17679",
				"17680",
				"17681",
				"17682",
				"724",
				"17222",
				"17223",
				"17224",
				"2915",
				"17868",
				"17869",
				"18276",
				"17335" };
		String[] roomIDArray = { "360", "835", "836", "232" };

		CreateExamNew service = CreateExamNew.getService();

		ISuportePersistente sp = null;

		try
		{
			sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();

			service.run(
				examDate,
				examStartTime,
				examEndTime,
				season,
				executionCourseIDArray,
				scopeIDArray,
				roomIDArray);
			fail("testOccupiedRoomForHourInside");
			sp.cancelarTransaccao();
		}
		catch (FenixServiceException ex)
		{
			try
			{
				sp.cancelarTransaccao();
			}
			catch (ExcepcaoPersistencia e)
			{

				fail("testOccupiedRoomForHourInside - Exception cancelar transacção " + ex);
			}
			compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
		}
		catch (Exception ex)
		{
			//sp.cancelarTransaccao();			
			fail("testOccupiedRoomForHourInside - Exception (not fenix service) " + ex);
		}
	}
	
	// the room is already occupied 
	public void testOccupiedRoomForHourOutside()
	{
		Calendar examDate = Calendar.getInstance();
		examDate.set(Calendar.YEAR, 2004);
		examDate.set(Calendar.MONTH, Calendar.FEBRUARY);
		examDate.set(Calendar.DAY_OF_MONTH, 9);

		Calendar examStartTime = Calendar.getInstance();
		examStartTime.set(Calendar.HOUR_OF_DAY, 10);
		examStartTime.set(Calendar.MINUTE, 30);
		examStartTime.set(Calendar.SECOND, 0);
		examStartTime.set(Calendar.MILLISECOND, 0);

		Calendar examEndTime = Calendar.getInstance();
		examEndTime.set(Calendar.HOUR_OF_DAY, 12);
		examEndTime.set(Calendar.MINUTE, 30);
		examEndTime.set(Calendar.SECOND, 0);
		examEndTime.set(Calendar.MILLISECOND, 0);

		Season season = new Season(new Integer(1));
		String[] executionCourseIDArray = { "36422", "36721" };
		String[] scopeIDArray =
			{
				"731",
				"17679",
				"17680",
				"17681",
				"17682",
				"724",
				"17222",
				"17223",
				"17224",
				"2915",
				"17868",
				"17869",
				"18276",
				"17335" };
		String[] roomIDArray = { "360", "835", "836", "232" };

		CreateExamNew service = CreateExamNew.getService();

		ISuportePersistente sp = null;

		try
		{
			sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();

			service.run(
				examDate,
				examStartTime,
				examEndTime,
				season,
				executionCourseIDArray,
				scopeIDArray,
				roomIDArray);
			fail("testOccupiedRoomForHourOutside");
			sp.cancelarTransaccao();
		}
		catch (FenixServiceException ex)
		{
			try
			{
				sp.cancelarTransaccao();
			}
			catch (ExcepcaoPersistencia e)
			{

				fail("testOccupiedRoomForHourOutside - Exception cancelar transacção " + ex);
			}
			compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
		}
		catch (Exception ex)
		{
			//sp.cancelarTransaccao();			
			fail("testOccupiedRoomForHourOutside - Exception (not fenix service) " + ex);
		}
	}


}
