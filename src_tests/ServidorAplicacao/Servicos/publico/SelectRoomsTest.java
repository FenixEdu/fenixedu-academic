/*
 * LerSalasServicosTest.java
 * JUnit based test
 *
 * Created on 28 de Outubro de 2002, 11:15
 */

package net.sourceforge.fenixedu.applicationTier.Servicos.publico;

/**
 * 
 * @author tfc130
 */
import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.applicationTier.Servicos.TestCaseServicos;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

public class SelectRoomsTest extends TestCaseServicos {
    public SelectRoomsTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(SelectRoomsTest.class);

        return suite;
    }

    protected void setUp() {
        super.setUp();
    }

    protected void tearDown() {
        super.tearDown();
    }

    public void testReadAll() {
        Object argsSelectRooms[] = new Object[1];
        argsSelectRooms[0] = new InfoRoom();
        List result = null;
        try {
            result = (ArrayList) ServiceManagerServiceFactory.executeService(null, "SelectRooms",
                    argsSelectRooms);
        } catch (Exception e) {
            fail("test read all rooms");
            e.printStackTrace();
        }
        assertNotNull("test real all rooms", result);

    }

    public void testReadByName() {
        Object argsSelectRooms[] = new Object[1];
        InfoRoom room = new InfoRoom();
        room.setNome("GA1");
        argsSelectRooms[0] = room;

        List result = null;
        try {
            result = (ArrayList) ServiceManagerServiceFactory.executeService(null, "SelectRooms",
                    argsSelectRooms);
        } catch (Exception e) {
            fail("test read roomsby name ");
            e.printStackTrace();
        }
        assertNotNull("test read rooms  by name ", result);

    }

}