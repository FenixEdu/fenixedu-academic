/*
 * CriarSalaServicosTest.java
 * JUnit based test
 *
 * Created on 24 de Outubro de 2002, 12:00
 */

package ServidorAplicacao.Servicos.sop;

/**
 * 
 * @author tfc130
 */
import java.util.HashMap;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoRoom;
import ServidorAplicacao.Servicos.TestCaseCreateServices;
import Util.TipoSala;

public class CriarSalaServicosTest extends TestCaseCreateServices {

    public CriarSalaServicosTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(CriarSalaServicosTest.class);

        return suite;
    }

    protected void setUp() {
        super.setUp();
    }

    protected void tearDown() {
        super.tearDown();
    }

    protected String getNameOfServiceToBeTested() {
        return "CriarSala";
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {

        Object argsCriarSala[] = new Object[1];
        argsCriarSala[0] = new InfoRoom(new String("Ga4"), new String("Pavilhilhão Central"),
                new Integer(1), new TipoSala(TipoSala.ANFITEATRO), new Integer(100), new Integer(50));

        return argsCriarSala;
    }

    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {

        Object argsCriarSala[] = new Object[1];
        argsCriarSala[0] = new InfoRoom(new String("Ga1"), new String("Pavilhilhão Central"),
                new Integer(0), new TipoSala(TipoSala.ANFITEATRO), new Integer(100), new Integer(50));

        return argsCriarSala;
    }

    protected HashMap getArgumentListOfServiceToBeTestedUnsuccessfuly() {
        return null;
    }
}