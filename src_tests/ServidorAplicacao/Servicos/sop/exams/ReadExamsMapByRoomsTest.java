/*
 * Created on 2004/04/20
 *
 */
package ServidorAplicacao.Servicos.sop.exams;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoRoomExamsMap;
import DataBeans.RoomKey;
import ServidorAplicacao.Servico.manager.ReadExecutionPeriod;
import ServidorAplicacao.Servico.sop.LerSala;
import ServidorAplicacao.Servico.sop.exams.ReadExamsMapByRooms;
import ServidorAplicacao.Servicos.ServiceTestCase;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Ana e Ricardo
 *  
 */
public class ReadExamsMapByRoomsTest extends ServiceTestCase {

    /**
     * @param name
     */
    public ReadExamsMapByRoomsTest(java.lang.String testName) {
        super(testName);
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadExamsMapByRooms";
    }

    protected String getDataSetFilePath() {
        return "etc/datasets_templates/servicos/sop/testReadExamsMapByRoomsDataset.xml";
    }

    private Calendar createStartSeason1() {
        Calendar startSeason1 = Calendar.getInstance();
        startSeason1.set(Calendar.YEAR, 2004);
        startSeason1.set(Calendar.MONTH, Calendar.JANUARY);
        startSeason1.set(Calendar.DAY_OF_MONTH, 5);
        startSeason1.set(Calendar.HOUR_OF_DAY, 0);
        startSeason1.set(Calendar.MINUTE, 0);
        startSeason1.set(Calendar.SECOND, 0);
        startSeason1.set(Calendar.MILLISECOND, 0);

        return startSeason1;
    }

    private Calendar createEndSeason2() {
        Calendar endSeason2 = Calendar.getInstance();
        endSeason2.set(Calendar.YEAR, 2004);
        endSeason2.set(Calendar.MONTH, Calendar.FEBRUARY);
        endSeason2.set(Calendar.DAY_OF_MONTH, 14);
        endSeason2.set(Calendar.HOUR_OF_DAY, 0);
        endSeason2.set(Calendar.MINUTE, 0);
        endSeason2.set(Calendar.SECOND, 0);
        endSeason2.set(Calendar.MILLISECOND, 0);

        return endSeason2;
    }

    public void testSuccessfullReadExamsMapByRooms() {
        ReadExamsMapByRooms service = new ReadExamsMapByRooms();

        ISuportePersistente sp;
        List infoRoomExamMapList = null;

        try {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            InfoExecutionPeriod infoExecutionPeriod = (new ReadExecutionPeriod()).run(new Integer(80));

            List infoRooms = new ArrayList();
            infoRooms.add(LerSala.getService().run(new RoomKey("GA1")));
            infoRooms.add(LerSala.getService().run(new RoomKey("GA2")));
            infoRooms.add(LerSala.getService().run(new RoomKey("GA3")));

            infoRoomExamMapList = service.run(infoExecutionPeriod, infoRooms);

            sp.confirmarTransaccao();
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());

            Calendar startSeason1 = createStartSeason1();
            Calendar endSeason2 = createEndSeason2();

            assertNotNull(infoRoomExamMapList);
            assertEquals(infoRoomExamMapList.size(), 3);

            InfoRoomExamsMap infoRoom = (InfoRoomExamsMap) infoRoomExamMapList.get(0);

            assertEquals(startSeason1, infoRoom.getStartSeason1());
            assertEquals(null, infoRoom.getEndSeason1());
            assertEquals(null, infoRoom.getStartSeason2());
            assertEquals(endSeason2, infoRoom.getEndSeason2());

            assertEquals(infoRoom.getExams().size(), 1);
        } catch (Exception ex) {
            fail("testSuccessfullReadExamsMapByRooms - Exception " + ex);
        }
    }

    public void testSuccessfullReadExamsMapByRoomsWithNotAllRoomsWithExam() {
        ReadExamsMapByRooms service = new ReadExamsMapByRooms();

        ISuportePersistente sp;
        List infoRoomExamMapList = null;

        try {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            InfoExecutionPeriod infoExecutionPeriod = (new ReadExecutionPeriod()).run(new Integer(80));

            List infoRooms = new ArrayList();
            infoRooms.add(LerSala.getService().run(new RoomKey("GA1")));
            infoRooms.add(LerSala.getService().run(new RoomKey("C13")));
            infoRooms.add(LerSala.getService().run(new RoomKey("GA2")));

            infoRoomExamMapList = service.run(infoExecutionPeriod, infoRooms);

            sp.confirmarTransaccao();
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());

            Calendar startSeason1 = createStartSeason1();
            Calendar endSeason2 = createEndSeason2();

            assertNotNull(infoRoomExamMapList);
            assertEquals(infoRoomExamMapList.size(), 3);

            InfoRoomExamsMap infoRoom1 = (InfoRoomExamsMap) infoRoomExamMapList.get(0);
            InfoRoomExamsMap infoRoom2 = (InfoRoomExamsMap) infoRoomExamMapList.get(1);

            assertEquals(startSeason1, infoRoom1.getStartSeason1());
            assertEquals(null, infoRoom1.getEndSeason1());
            assertEquals(null, infoRoom1.getStartSeason2());
            assertEquals(endSeason2, infoRoom1.getEndSeason2());

            assertEquals(infoRoom1.getExams().size(), 1);

            assertTrue(infoRoom2.getExams().isEmpty());
        } catch (Exception ex) {
            fail("testSuccessfullReadExamsMapByRoomsWithNotAllRoomsWithExam - Exception " + ex);
        }
    }

    public void testSuccessfullReadExamsMapByRoomsWithoutRooms() {
        ReadExamsMapByRooms service = new ReadExamsMapByRooms();

        ISuportePersistente sp;
        List infoRoomExamMapList = null;

        try {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            InfoExecutionPeriod infoExecutionPeriod = (new ReadExecutionPeriod()).run(new Integer(80));

            List infoRooms = new ArrayList();

            infoRoomExamMapList = service.run(infoExecutionPeriod, infoRooms);

            sp.confirmarTransaccao();
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());

            assertNotNull(infoRoomExamMapList);
            assertTrue(infoRoomExamMapList.isEmpty());
        } catch (Exception ex) {
            fail("testSuccessfullReadExamsMapByRoomsWithoutRooms - Exception " + ex);
        }
    }

    public void testUnsuccessfullReadExamsMapByRoomsUnexistentExecutionPeriod() {
        ReadExamsMapByRooms service = new ReadExamsMapByRooms();

        ISuportePersistente sp;

        try {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            List infoRooms = new ArrayList();

            service.run(null, infoRooms);

            fail("testUnsuccessfullReadExamsMapByRoomsUnexistentExecutionPeriod");
        } catch (Exception ex) {
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
        }
    }
}