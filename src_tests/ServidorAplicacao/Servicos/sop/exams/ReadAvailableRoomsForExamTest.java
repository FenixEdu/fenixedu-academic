/*
 * Created on 1/Abr/2004
 */
package net.sourceforge.fenixedu.applicationTier.Servicos.sop.exams;

import java.util.Calendar;

import net.sourceforge.fenixedu.applicationTier.Servicos.ServiceTestCase;

/**
 * @author Ana e Ricardo
 *
 */
public class ReadAvailableRoomsForExamTest extends ServiceTestCase {

    /**
     * @param name
     */
    public ReadAvailableRoomsForExamTest(java.lang.String testName) {
        super(testName);
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadAvailableRoomsForExam";
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.sop.exams.ServiceNeedsAuthenticationTestCase#getDataSetFilePath()
     */
    protected String getDataSetFilePath() {
        return "etc/datasets_templates/servicos/sop/testReadAvailableRoomsForExamsV4Dataset.xml";
    }

    protected Calendar createStartTime() {
        Calendar examStartTime = Calendar.getInstance();

        examStartTime.set(Calendar.HOUR_OF_DAY, 10);
        examStartTime.set(Calendar.MINUTE, 0);
        examStartTime.set(Calendar.SECOND, 0);
        examStartTime.set(Calendar.MILLISECOND, 0);

        return examStartTime;
    }

    protected Calendar createEndTime() {
        Calendar examEndTime = Calendar.getInstance();

        examEndTime.set(Calendar.HOUR_OF_DAY, 11);
        examEndTime.set(Calendar.MINUTE, 0);
        examEndTime.set(Calendar.SECOND, 0);
        examEndTime.set(Calendar.MILLISECOND, 0);

        return examEndTime;
    }

/*
    // no rooms
    public void testUnexistingRooms() {
		Calendar examDate = Calendar.getInstance();
		examDate.set(Calendar.YEAR, 2004);
		examDate.set(Calendar.MONTH, Calendar.FEBRUARY);
		examDate.set(Calendar.DAY_OF_MONTH, 18);

        Calendar examStartTime = createStartTime();
        Calendar examEndTime = createEndTime();
        DiaSemana dayOfWeek = new DiaSemana(examDate.get(Calendar.DAY_OF_WEEK));
        ReadAvailableRoomsForExam service = ReadAvailableRoomsForExam.getService();

        ISuportePersistente sp = null;

        try {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            List availableRooms = service.run(examDate, examDate, examStartTime, examEndTime, dayOfWeek);
			sp.confirmarTransaccao();
			compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
            assertEquals(availableRooms.size(), 0);
        }

        catch (FenixServiceException ex) {
            fail("testUnexistingRooms - Fenix Service Exception" + ex);
        } catch (Exception ex) {
            //sp.cancelarTransaccao();			
            fail("testUnexistingRooms - Exception (not fenix service) " + ex);
        }

    }


	// 4 rooms
    public void testSuccessfulReadAvailableRoomsSamePeriod() {
		Calendar examDate = Calendar.getInstance();
		examDate.set(Calendar.YEAR, 2004);
		examDate.set(Calendar.MONTH, Calendar.JANUARY);
		examDate.set(Calendar.DAY_OF_MONTH, 12);

		Calendar examStartTime = createStartTime();
		Calendar examEndTime = createEndTime();
		DiaSemana dayOfWeek = new DiaSemana(examDate.get(Calendar.DAY_OF_WEEK));
		ReadAvailableRoomsForExam service = ReadAvailableRoomsForExam.getService();

		ISuportePersistente sp = null;

        try {
			sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();

			List availableRooms = service.run(examDate, examDate, examStartTime, examEndTime, dayOfWeek);
			sp.confirmarTransaccao();
			compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
			assertEquals(4, availableRooms.size());
			Iterator iter = availableRooms.iterator();
			List readRoomsNames = new ArrayList();
            while (iter.hasNext()) {
                InfoRoom room = (InfoRoom) iter.next();
                readRoomsNames.add(room.getNome());
            }
            List expectedRoomsNames = new ArrayList();
			expectedRoomsNames.add("GA1");
			expectedRoomsNames.add("GA2");
			expectedRoomsNames.add("F1");
			expectedRoomsNames.add("F2");
			assertEquals(expectedRoomsNames, readRoomsNames);
		}

        catch (FenixServiceException ex) {
			fail("testSuccessfulReadAvailableRoomsSamePeriod - Fenix Service Exception" + ex);
        } catch (Exception ex) {
			//sp.cancelarTransaccao();			
			fail("testSuccessfulReadAvailableRoomsSamePeriod - Exception (not fenix service) " + ex);
		}

	}
	
	// all rooms
    public void testSuccessfulReadAvailableRoomsPeriodAfter() {
		Calendar examDate = Calendar.getInstance();
		examDate.set(Calendar.YEAR, 2004);
		examDate.set(Calendar.MONTH, Calendar.FEBRUARY);
		examDate.set(Calendar.DAY_OF_MONTH, 19);

		Calendar examStartTime = createStartTime();
		Calendar examEndTime = createEndTime();
		DiaSemana dayOfWeek = new DiaSemana(examDate.get(Calendar.DAY_OF_WEEK));
		ReadAvailableRoomsForExam service = ReadAvailableRoomsForExam.getService();

		ISuportePersistente sp = null;

        try {
			sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();

			List availableRooms = service.run(examDate, examDate, examStartTime, examEndTime, dayOfWeek);
			sp.confirmarTransaccao();
			compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
			assertEquals(9, availableRooms.size());
			Iterator iter = availableRooms.iterator();
			List readRoomsNames = new ArrayList();
            while (iter.hasNext()) {
                InfoRoom room = (InfoRoom) iter.next();
                readRoomsNames.add(room.getNome());
            }
            List expectedRoomsNames = new ArrayList();
			expectedRoomsNames.add("GA1");
			expectedRoomsNames.add("GA2");
			expectedRoomsNames.add("F1");
			expectedRoomsNames.add("F2");
			expectedRoomsNames.add("VA1");
			expectedRoomsNames.add("V113");
			expectedRoomsNames.add("EA1");
            expectedRoomsNames.add("LTI");
            expectedRoomsNames.add("SN");
			assertEquals(expectedRoomsNames, readRoomsNames);
		}

        catch (FenixServiceException ex) {
			fail("testSuccessfulReadAvailableRoomsPeriodAfter - Fenix Service Exception" + ex);
        } catch (Exception ex) {
			//sp.cancelarTransaccao();			
			fail("testSuccessfulReadAvailableRoomsPeriodAfter - Exception (not fenix service) " + ex);
		}

	}
	
	// all rooms
    public void testSuccessfulReadAvailableRoomsPeriodBefore() {
		Calendar examDate = Calendar.getInstance();
		examDate.set(Calendar.YEAR, 2004);
		examDate.set(Calendar.MONTH, Calendar.FEBRUARY);
		examDate.set(Calendar.DAY_OF_MONTH, 17);

		Calendar examStartTime = createStartTime();
		Calendar examEndTime = createEndTime();
		DiaSemana dayOfWeek = new DiaSemana(examDate.get(Calendar.DAY_OF_WEEK));
		ReadAvailableRoomsForExam service = ReadAvailableRoomsForExam.getService();

		ISuportePersistente sp = null;

        try {
			sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();

			List availableRooms = service.run(examDate, examDate, examStartTime, examEndTime, dayOfWeek);
			sp.confirmarTransaccao();
			compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
			assertEquals(9, availableRooms.size());
			Iterator iter = availableRooms.iterator();
			List readRoomsNames = new ArrayList();
            while (iter.hasNext()) {
                InfoRoom room = (InfoRoom) iter.next();
                readRoomsNames.add(room.getNome());
            }
            List expectedRoomsNames = new ArrayList();
			expectedRoomsNames.add("GA1");
			expectedRoomsNames.add("GA2");
			expectedRoomsNames.add("F1");
			expectedRoomsNames.add("F2");
			expectedRoomsNames.add("VA1");
			expectedRoomsNames.add("V113");
			expectedRoomsNames.add("EA1");
            expectedRoomsNames.add("LTI");
            expectedRoomsNames.add("SN");
			assertEquals(expectedRoomsNames, readRoomsNames);
		}

        catch (FenixServiceException ex) {
			fail("testSuccessfulReadAvailableRoomsPeriodBefore - Fenix Service Exception" + ex);
        } catch (Exception ex) {
			//sp.cancelarTransaccao();			
			fail("testSuccessfulReadAvailableRoomsPeriodBefore - Exception (not fenix service) " + ex);
		}

	}
*/
}
