/*
 * EditarSalaServicosTest.java
 * JUnit based test
 *
 * Created on 27 de Outubro de 2002, 19:58
 */

package net.sourceforge.fenixedu.applicationTier.Servicos.sop;

/**
 * 
 * @author tfc130
 */
import junit.framework.Test;
import junit.framework.TestSuite;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.RoomKey;
import net.sourceforge.fenixedu.applicationTier.Servicos.TestCaseDeleteAndEditServices;
import net.sourceforge.fenixedu.util.TipoSala;

public class EditarSalaServicosTest extends TestCaseDeleteAndEditServices {

    public EditarSalaServicosTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(EditarSalaServicosTest.class);

        return suite;
    }

    protected void setUp() {
        super.setUp();
    }

    protected void tearDown() {
        super.tearDown();
    }

    protected String getNameOfServiceToBeTested() {
        return "EditarSala";
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {

        Object argsEditarSala[] = new Object[2];
        argsEditarSala[0] = new RoomKey("Ga1");
        argsEditarSala[1] = new InfoRoom(new String("Ga1"), new String("Pavilhilhão Central"),
                new Integer(1), new TipoSala(TipoSala.ANFITEATRO), new Integer(150), new Integer(25));

        return argsEditarSala;
    }

    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {

        Object argsEditarSala[] = new Object[2];
        argsEditarSala[0] = new RoomKey("Ga4");
        argsEditarSala[1] = new InfoRoom(new String("Ga4"), new String("Pavilhilhão Central"),
                new Integer(1), new TipoSala(TipoSala.ANFITEATRO), new Integer(100), new Integer(50));

        return argsEditarSala;
    }
}