/*
 * Created on 19/Abr/2004
 */
package ServidorAplicacao.Servicos.sop.exams;

import java.util.List;

import ServidorAplicacao.Servico.sop.exams.SearchRooms;
import ServidorAplicacao.Servicos.ServiceTestCase;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Ana e Ricardo  
 */
public class SearchRoomsTest extends ServiceTestCase
{
    /**
     * @param name
     */
    public SearchRoomsTest(java.lang.String testName)
    {
        super(testName);
    }

    protected String getNameOfServiceToBeTested()
    {
        return "SearchRooms";
    }

    protected String getDataSetFilePath()
    {
        return "etc/datasets_templates/servicos/sop/testReadAllBuildingsExamsV4.xml";
    }

    // all rooms
    public void testSuccessfullSearchRoomsAllRooms()
    {
        String name = null;
        String building = null;
        Integer floor = null;
        Integer type = null;
        Integer normal = null;
        Integer exam = null;

        SearchRooms service = SearchRooms.getService();

        ISuportePersistente sp;

        try
        {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            List rooms = service.run(name, building, floor, type, normal, exam);

            sp.confirmarTransaccao();
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());

            assertEquals(rooms.size(), 341);
        }
        catch (ExcepcaoPersistencia e)
        {
            fail("testSuccessfullSearchRoomsAllRooms - persistencia");
        }
        catch (Exception e)
        {
            fail("testSuccessfullSearchRoomsAllRooms");
        }
    }

    // search by name
    public void testSuccessfullSearchRoomsByName()
    {
        String name = "GA2";
        String building = null;
        Integer floor = null;
        Integer type = null;
        Integer normal = null;
        Integer exam = null;

        SearchRooms service = SearchRooms.getService();

        ISuportePersistente sp;

        try
        {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            List rooms = service.run(name, building, floor, type, normal, exam);

            sp.confirmarTransaccao();
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());

            assertEquals(rooms.size(), 1);
        }
        catch (ExcepcaoPersistencia e)
        {
            fail("testSuccessfullSearchRoomsByName - persistencia");
        }
        catch (Exception e)
        {
            fail("testSuccessfullSearchRoomsByName");
        }
    }

    // search by building
    public void testSuccessfullSearchRoomsByBuilding()
    {
        String name = null;
        String building = "Pavilhão Novas Licenciaturas";
        Integer floor = null;
        Integer type = null;
        Integer normal = null;
        Integer exam = null;

        SearchRooms service = SearchRooms.getService();

        ISuportePersistente sp;

        try
        {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            List rooms = service.run(name, building, floor, type, normal, exam);

            sp.confirmarTransaccao();
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());

            assertEquals(rooms.size(), 16);
        }
        catch (ExcepcaoPersistencia e)
        {
            fail("testSuccessfullSearchRoomsByBuilding - persistencia");
        }
        catch (Exception e)
        {
            fail("testSuccessfullSearchRoomsByBuilding");
        }
    }

    // search by floor
    public void testSuccessfullSearchRoomsByFloor()
    {
        String name = null;
        String building = null;
        Integer floor = new Integer(5);
        Integer type = null;
        Integer normal = null;
        Integer exam = null;

        SearchRooms service = SearchRooms.getService();

        ISuportePersistente sp;

        try
        {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            List rooms = service.run(name, building, floor, type, normal, exam);

            sp.confirmarTransaccao();
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());

            assertEquals(rooms.size(), 5);
        }
        catch (ExcepcaoPersistencia e)
        {
            fail("testSuccessfullSearchRoomsByFloor - persistencia");
        }
        catch (Exception e)
        {
            fail("testSuccessfullSearchRoomsByFloor");
        }
    }

    // search by type
    public void testSuccessfullSearchRoomsByType()
    {
        String name = null;
        String building = null;
        Integer floor = null;
        Integer type = new Integer(3);
        Integer normal = null;
        Integer exam = null;

        SearchRooms service = SearchRooms.getService();

        ISuportePersistente sp;

        try
        {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            List rooms = service.run(name, building, floor, type, normal, exam);

            sp.confirmarTransaccao();
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());

            assertEquals(rooms.size(), 124);
        }
        catch (ExcepcaoPersistencia e)
        {
            fail("testSuccessfullSearchRoomsByType - persistencia");
        }
        catch (Exception e)
        {
            fail("testSuccessfullSearchRoomsByType");
        }
    }

    // search by normal
    public void testSuccessfullSearchRoomsByNormal()
    {
        String name = null;
        String building = null;
        Integer floor = null;
        Integer type = null;
        Integer normal = new Integer(140);
        Integer exam = null;

        SearchRooms service = SearchRooms.getService();

        ISuportePersistente sp;
        try
        {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            List rooms = service.run(name, building, floor, type, normal, exam);

            sp.confirmarTransaccao();
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());

            assertEquals(rooms.size(), 10);
        }
        catch (ExcepcaoPersistencia e)
        {
            fail("testSuccessfullSearchRoomsByNormal - persistencia");
        }
        catch (Exception e)
        {
            fail("testSuccessfullSearchRoomsByNormal");
        }
    }

    // search by exam
    public void testSuccessfullSearchRoomsByExam()
    {
        String name = null;
        String building = null;
        Integer floor = null;
        Integer type = null;
        Integer normal = null;
        Integer exam = new Integer(50);

        SearchRooms service = SearchRooms.getService();

        ISuportePersistente sp;

        try
        {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            List rooms = service.run(name, building, floor, type, normal, exam);

            sp.confirmarTransaccao();
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());

            assertEquals(rooms.size(), 9);
        }
        catch (ExcepcaoPersistencia e)
        {
            fail("testSuccessfullSearchRoomsByExam - persistencia");
        }
        catch (Exception e)
        {
            fail("testSuccessfullSearchRoomsByExam");
        }
    }

    // search by all
	public void testSuccessfullSearchRoomsByAll()
	{
		String name = "GA1";
		String building = "Pavilhão Central";
		Integer floor = new Integer(0);
		Integer type = new Integer(1);
		Integer normal = new Integer(10);
		Integer exam = new Integer(10);

		SearchRooms service = SearchRooms.getService();

		ISuportePersistente sp;
		try
		{
			sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();

			List rooms = service.run(name, building, floor, type, normal, exam);

			sp.confirmarTransaccao();
			compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());

			assertEquals(rooms.size(), 1);
		}
		catch (ExcepcaoPersistencia e)
		{
			fail("testSuccessfullSearchRoomsByNormal - persistencia");
		}
		catch (Exception e)
		{
			fail("testSuccessfullSearchRoomsByNormal");
		}
	}

    // search by name no rooms
    public void testSuccessfullSearchRoomsByNameNoRooms()
    {
        String name = "xpto";
        String building = null;
        Integer floor = null;
        Integer type = null;
        Integer normal = null;
        Integer exam = null;

        SearchRooms service = SearchRooms.getService();

        ISuportePersistente sp;
        try
        {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            List rooms = service.run(name, building, floor, type, normal, exam);

            sp.confirmarTransaccao();
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());

            assertEquals(rooms.size(), 0);
        }
        catch (ExcepcaoPersistencia e)
        {
            fail("testSuccessfullSearchRoomsByNameNoRooms - persistencia");
        }
        catch (Exception e)
        {
            fail("testSuccessfullSearchRoomsByNameNoRooms");
        }
    }

    // search by building no rooms
    public void testSuccessfullSearchRoomsByBuildingNoRooms()
    {
        String name = null;
        String building = "xpto";
        Integer floor = null;
        Integer type = null;
        Integer normal = null;
        Integer exam = null;

        SearchRooms service = SearchRooms.getService();

        ISuportePersistente sp;
        try
        {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            List rooms = service.run(name, building, floor, type, normal, exam);

            sp.confirmarTransaccao();
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());

            assertEquals(rooms.size(), 0);
        }
        catch (ExcepcaoPersistencia e)
        {
            fail("testSuccessfullSearchRoomsByBuildingNoRooms - persistencia");
        }
        catch (Exception e)
        {
            fail("testSuccessfullSearchRoomsByBuildingNoRooms");
        }
    }

    // search by floor no rooms
    public void testSuccessfullSearchRoomsByFloorNoRooms()
    {
        String name = null;
        String building = null;
        Integer floor = new Integer(6);
        Integer type = null;
        Integer normal = null;
        Integer exam = null;

        SearchRooms service = SearchRooms.getService();

        ISuportePersistente sp;
        try
        {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            List rooms = service.run(name, building, floor, type, normal, exam);

            sp.confirmarTransaccao();
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());

            assertEquals(rooms.size(), 0);
        }
        catch (ExcepcaoPersistencia e)
        {
            fail("testSuccessfullSearchRoomsByFloorNoRooms - persistencia");
        }
        catch (Exception e)
        {
            fail("testSuccessfullSearchRoomsByFloorNoRooms");
        }
    }

    // search by type no rooms
    public void testSuccessfullSearchRoomsByTypeNoRooms()
    {
        String name = null;
        String building = null;
        Integer floor = null;
        Integer type = new Integer(4);
        Integer normal = null;
        Integer exam = null;

        SearchRooms service = SearchRooms.getService();

        ISuportePersistente sp;
        try
        {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            List rooms = service.run(name, building, floor, type, normal, exam);

            sp.confirmarTransaccao();
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());

            assertEquals(rooms.size(), 0);
        }
        catch (ExcepcaoPersistencia e)
        {
            fail("testSuccessfullSearchRoomsByTypeNoRooms - persistencia");
        }
        catch (Exception e)
        {
            fail("testSuccessfullSearchRoomsByTypeNoRooms");
        }
    }

    // search by normal no rooms
    public void testSuccessfullSearchRoomsByNormalNoRooms()
    {
        String name = null;
        String building = null;
        Integer floor = null;
        Integer type = null;
        Integer normal = new Integer(400);
        Integer exam = null;

        SearchRooms service = SearchRooms.getService();

        ISuportePersistente sp;
        try
        {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            List rooms = service.run(name, building, floor, type, normal, exam);

            sp.confirmarTransaccao();
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());

            assertEquals(rooms.size(), 0);
        }
        catch (ExcepcaoPersistencia e)
        {
            fail("testSuccessfullSearchRoomsByNormalNoRooms - persistencia");
        }
        catch (Exception e)
        {
            fail("testSuccessfullSearchRoomsByNormalNoRooms");
        }
    }

    // search by exam no rooms
    public void testSuccessfullSearchRoomsByExamNoRooms()
    {
        String name = null;
        String building = null;
        Integer floor = null;
        Integer type = null;
        Integer normal = null;
        Integer exam = new Integer(400);

        SearchRooms service = SearchRooms.getService();

        ISuportePersistente sp;
        try
        {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            List rooms = service.run(name, building, floor, type, normal, exam);

            sp.confirmarTransaccao();
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());

            assertEquals(rooms.size(), 0);
        }
        catch (ExcepcaoPersistencia e)
        {
            fail("testSuccessfullSearchRoomsByExamNoRooms - persistencia");
        }
        catch (Exception e)
        {
            fail("testSuccessfullSearchRoomsByExamNoRooms");
        }
    }

    // search by all no rooms
    public void testSuccessfullSearchRoomsByAllNoRooms()
    {
        String name = "GA2";
        String building = "TagusPark - Bloco A - Nascente";
        Integer floor = new Integer(0);
        Integer type = new Integer(1);
        Integer normal = new Integer(200);
        Integer exam = new Integer(200);

        SearchRooms service = SearchRooms.getService();

        ISuportePersistente sp;
        try
        {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            List rooms = service.run(name, building, floor, type, normal, exam);

            sp.confirmarTransaccao();
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());

            assertEquals(rooms.size(), 0);
        }
        catch (ExcepcaoPersistencia e)
        {
            fail("testSuccessfullSearchRoomsByAllNoRooms - persistencia");
        }
        catch (Exception e)
        {
            fail("testSuccessfullSearchRoomsByAllNoRooms");
        }
    }

    // search by exam and normal
	public void testSuccessfullSearchRoomsByNormalAndExam()
	{
		String name = null;
		String building = null;
		Integer floor = null;
		Integer type = null;
		Integer normal = new Integer(140);
		Integer exam = new Integer(50);

		SearchRooms service = SearchRooms.getService();

		ISuportePersistente sp;

		try
		{
			sp = SuportePersistenteOJB.getInstance();
			sp.iniciarTransaccao();

			List rooms = service.run(name, building, floor, type, normal, exam);

			sp.confirmarTransaccao();
			compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
			
			assertEquals(rooms.size(), 8);
		}
		catch (ExcepcaoPersistencia e)
		{
			fail("testSuccessfullSearchRoomsByNormalAndExam - persistencia");
		}
		catch (Exception e)
		{
			fail("testSuccessfullSearchRoomsByNormalAndExam");
		}
	}
}
