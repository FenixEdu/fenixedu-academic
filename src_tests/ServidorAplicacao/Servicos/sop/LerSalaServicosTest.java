/*
 * LerSalaServicosTest.java
 * JUnit based test
 *
 * Created on 26 de Outubro de 2002, 11:23
 */

package ServidorAplicacao.Servicos.sop;

/**
 * 
 * @author tfc130
 */
import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoRoom;
import DataBeans.RoomKey;
import ServidorAplicacao.Servicos.TestCaseReadServices;
import Util.TipoSala;

public class LerSalaServicosTest extends TestCaseReadServices {
    public LerSalaServicosTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(LerSalaServicosTest.class);

        return suite;
    }

    protected void setUp() {
        super.setUp();
    }

    protected void tearDown() {
        super.tearDown();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseReadServices#getNameOfServiceToBeTested()
     */
    protected String getNameOfServiceToBeTested() {
        return "LerSala";
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseReadServices#getArgumentsOfServiceToBeTestedUnsuccessfuly()
     */
    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
        RoomKey keySala = new RoomKey("xpto");
        Object[] result = { keySala };
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseReadServices#getArgumentsOfServiceToBeTestedSuccessfuly()
     */
    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
        RoomKey keySala = new RoomKey("GA1");
        Object[] result = { keySala };
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseReadServices#getNumberOfItemsToRetrieve()
     */
    protected int getNumberOfItemsToRetrieve() {
        return 1;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseReadServices#getObjectToCompare()
     */
    protected Object getObjectToCompare() {
        InfoRoom infoRoom = new InfoRoom("Ga1", "Pavilhao Central", new Integer(0), new TipoSala(1),
                new Integer(100), new Integer(50));
        return infoRoom;
    }

}