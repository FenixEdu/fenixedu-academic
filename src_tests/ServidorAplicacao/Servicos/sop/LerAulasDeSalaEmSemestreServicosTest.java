/*
 * LerAulasDeSalaEmSemestreServicosTest.java JUnit based test
 * 
 * Created on 29 de Outubro de 2002, 15:49
 */

package ServidorAplicacao.Servicos.sop;

/**
 * @author tfc130
 */
import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoRoom;
import DataBeans.util.Cloner;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import ServidorAplicacao.Servicos.TestCaseReadServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoSala;

public class LerAulasDeSalaEmSemestreServicosTest extends TestCaseReadServices {

    private InfoExecutionPeriod infoExecutionPeriod = null;

    private InfoRoom infoRoom = null;

    public LerAulasDeSalaEmSemestreServicosTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(LerAulasDeSalaEmSemestreServicosTest.class);

        return suite;
    }

    protected void setUp() {
        super.setUp();
    }

    protected void tearDown() {
        super.tearDown();
    }

    protected String getNameOfServiceToBeTested() {
        return "LerAulasDeSalaEmSemestre";
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {

        this.ligarSuportePersistente(true);

        Object argsLerAulas[] = { this.infoExecutionPeriod, this.infoRoom };

        return argsLerAulas;
    }

    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {

        this.ligarSuportePersistente(false);

        Object argsLerAulas[] = { this.infoExecutionPeriod, this.infoRoom };

        return argsLerAulas;
    }

    protected int getNumberOfItemsToRetrieve() {
        return 21;
    }

    protected Object getObjectToCompare() {
        return null;
    }

    protected boolean needsAuthorization() {
        return true;
    }

    private void ligarSuportePersistente(boolean existing) {

        ISuportePersistente sp = null;

        try {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            IPersistentExecutionYear ieyp = sp.getIPersistentExecutionYear();
            IExecutionYear iey = ieyp.readExecutionYearByName("2002/2003");

            IPersistentExecutionPeriod iepp = sp.getIPersistentExecutionPeriod();
            IExecutionPeriod iep = iepp.readByNameAndExecutionYear("2º Semestre", iey);

            if (existing) {
                this.infoRoom = new InfoRoom(new String("Ga1"), new String("Pavilhilhão Central"),
                        new Integer(0), new TipoSala(TipoSala.ANFITEATRO), new Integer(100),
                        new Integer(50));
            } else {
                this.infoRoom = new InfoRoom(new String("Ga4"), new String("Pavilhilhão Central"),
                        new Integer(1), new TipoSala(TipoSala.ANFITEATRO), new Integer(100),
                        new Integer(50));
            }

            this.infoExecutionPeriod = (InfoExecutionPeriod) Cloner.get(iep);

            sp.confirmarTransaccao();

        } catch (ExcepcaoPersistencia excepcao) {
            try {
                sp.cancelarTransaccao();
            } catch (ExcepcaoPersistencia ex) {
                fail("ligarSuportePersistente: cancelarTransaccao");
            }
            fail("ligarSuportePersistente: confirmarTransaccao");
        }
    }
}