package ServidorAplicacao.Servicos.commons.externalPerson;

import java.util.List;

import DataBeans.InfoExternalPerson;
import ServidorAplicacao.Servicos.ServiceTestCase;
import Util.TipoDocumentoIdentificacao;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Shezad Anavarali (sana@mega.ist.utl.pt) 
 * @author Nadir Tarmahomed (naat@mega.ist.utl.pt)
 */
public class ReadExternalPersonsByWorkLocationTest extends ServiceTestCase {

    private String dataSetFilePath;

    /**
     * @param testName
     */
    public ReadExternalPersonsByWorkLocationTest(String testName) {
        super(testName);
        if (testName.equals("testNonExistingExternalPersonsInAnyWorkLocation")) {
            this.dataSetFilePath = "etc/datasets/servicos/commons/externalPerson/testReadExternalPersonsByWorkLocationNonExistingExternalPersonsInAnyWorkLocationDataSet.xml";

        } else {
            this.dataSetFilePath = "etc/datasets/servicos/commons/externalPerson/testReadExternalPersonsByWorkLocationDataSet.xml";
        }
        
    }

    protected void setUp() {
        super.setUp();
    }

    protected String getDataSetFilePath() {
        return this.dataSetFilePath;
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadExternalPersonsByWorkLocation";
    }

    public void testReadExistingExternalPersonsInWorkLocation() {
        try {
            Integer workLocationID = new Integer(22);
            Object[] args = { workLocationID};

            List infoExternalPersonList = (List) ServiceManagerServiceFactory.executeService(null,
                    getNameOfServiceToBeTested(), args);

            assertNotNull(infoExternalPersonList);
            assertFalse(infoExternalPersonList.isEmpty());
            assertTrue(infoExternalPersonList.size() == 2);

            InfoExternalPerson infoExternalPerson = null;

            //check first external person
            infoExternalPerson = (InfoExternalPerson) infoExternalPersonList.get(0);
            assertEquals(infoExternalPerson.getIdInternal(), new Integer(23));
            assertNotNull(infoExternalPerson.getInfoPerson());
            assertNotNull(infoExternalPerson.getInfoWorkLocation());
            assertEquals(infoExternalPerson.getInfoWorkLocation().getIdInternal(), new Integer(22));
            assertEquals(infoExternalPerson.getInfoWorkLocation().getName(), "INSTITUTO DE TELCOS");
            assertEquals(infoExternalPerson.getInfoPerson().getIdInternal(), new Integer(17901));
            assertEquals(infoExternalPerson.getInfoPerson().getNumeroDocumentoIdentificacao(), "1002");
            assertEquals(infoExternalPerson.getInfoPerson().getNome(), "Some cool external person");
            assertEquals(infoExternalPerson.getInfoPerson().getMorada(), "Some cool address");
            assertEquals(infoExternalPerson.getInfoPerson().getTelefone(), "1234567893");
            assertEquals(infoExternalPerson.getInfoPerson().getTelemovel(), "981234565");
            assertEquals(infoExternalPerson.getInfoPerson().getEmail(), "suporte@dotist.utl.pt");
            assertEquals(infoExternalPerson.getInfoPerson().getEnderecoWeb(),
                    "http://fenix-ashes.ist.utl.pt:8080");
            assertEquals(infoExternalPerson.getInfoPerson().getUsername(), "e1002");
            assertEquals(infoExternalPerson.getInfoPerson().getTipoDocumentoIdentificacao(),
                    new TipoDocumentoIdentificacao(TipoDocumentoIdentificacao.EXTERNO));

            //check second external person
            infoExternalPerson = (InfoExternalPerson) infoExternalPersonList.get(1);
            assertEquals(infoExternalPerson.getIdInternal(), new Integer(24));
            assertNotNull(infoExternalPerson.getInfoPerson());
            assertNotNull(infoExternalPerson.getInfoWorkLocation());
            assertEquals(infoExternalPerson.getInfoWorkLocation().getIdInternal(), new Integer(22));
            assertEquals(infoExternalPerson.getInfoWorkLocation().getName(), "INSTITUTO DE TELCOS");
            assertEquals(infoExternalPerson.getInfoPerson().getIdInternal(), new Integer(17902));
            assertEquals(infoExternalPerson.getInfoPerson().getNumeroDocumentoIdentificacao(), "1003");
            assertEquals(infoExternalPerson.getInfoPerson().getNome(), "Someone external");
            assertEquals(infoExternalPerson.getInfoPerson().getMorada(), "Some address");
            assertEquals(infoExternalPerson.getInfoPerson().getTelefone(), "1234567890");
            assertEquals(infoExternalPerson.getInfoPerson().getTelemovel(), "981234567");
            assertEquals(infoExternalPerson.getInfoPerson().getEmail(), "suporte@dotist.utl.pt");
            assertEquals(infoExternalPerson.getInfoPerson().getEnderecoWeb(),
                    "http://fenix-ashes.ist.utl.pt:8080");
            assertEquals(infoExternalPerson.getInfoPerson().getUsername(), "e1003");
            assertEquals(infoExternalPerson.getInfoPerson().getTipoDocumentoIdentificacao(),
                    new TipoDocumentoIdentificacao(TipoDocumentoIdentificacao.EXTERNO));

            //ok

        } catch (Exception ex) {
            ex.printStackTrace();
            fail("testReadExistingExternalPersonsInWorkLocation " + ex.getMessage());
        }

    }

    public void testReadNonExistingExternalPersonsInWorkLocation() {
        try {
            Integer workLocationID = new Integer(24);
            Object[] args = { workLocationID};

            List infoExternalPersonList = (List) ServiceManagerServiceFactory.executeService(null,
                    getNameOfServiceToBeTested(), args);

            assertNotNull(infoExternalPersonList);
            assertTrue(infoExternalPersonList.isEmpty());

            //ok

        } catch (Exception ex) {
            ex.printStackTrace();
            fail("testReadNonExistingExternalPersonsInWorkLocation " + ex.getMessage());
        }

    }

    public void testReadExistingExternalPersonsInAnyWorkLocation() {
        try {

            Object[] args = { null};

            List infoExternalPersonList = (List) ServiceManagerServiceFactory.executeService(null,
                    getNameOfServiceToBeTested(), args);

            assertNotNull(infoExternalPersonList);
            assertFalse(infoExternalPersonList.isEmpty());
            assertTrue(infoExternalPersonList.size() == 3);

            InfoExternalPerson infoExternalPerson = null;

            //check first external person
            infoExternalPerson = (InfoExternalPerson) infoExternalPersonList.get(0);
            assertEquals(infoExternalPerson.getIdInternal(), new Integer(23));
            assertNotNull(infoExternalPerson.getInfoPerson());
            assertNotNull(infoExternalPerson.getInfoWorkLocation());
            assertEquals(infoExternalPerson.getInfoWorkLocation().getIdInternal(), new Integer(22));
            assertEquals(infoExternalPerson.getInfoWorkLocation().getName(), "INSTITUTO DE TELCOS");
            assertEquals(infoExternalPerson.getInfoPerson().getIdInternal(), new Integer(17901));
            assertEquals(infoExternalPerson.getInfoPerson().getNumeroDocumentoIdentificacao(), "1002");
            assertEquals(infoExternalPerson.getInfoPerson().getNome(), "Some cool external person");
            assertEquals(infoExternalPerson.getInfoPerson().getMorada(), "Some cool address");
            assertEquals(infoExternalPerson.getInfoPerson().getTelefone(), "1234567893");
            assertEquals(infoExternalPerson.getInfoPerson().getTelemovel(), "981234565");
            assertEquals(infoExternalPerson.getInfoPerson().getEmail(), "suporte@dotist.utl.pt");
            assertEquals(infoExternalPerson.getInfoPerson().getEnderecoWeb(),
                    "http://fenix-ashes.ist.utl.pt:8080");
            assertEquals(infoExternalPerson.getInfoPerson().getUsername(), "e1002");
            assertEquals(infoExternalPerson.getInfoPerson().getTipoDocumentoIdentificacao(),
                    new TipoDocumentoIdentificacao(TipoDocumentoIdentificacao.EXTERNO));

            //check second external person
            infoExternalPerson = (InfoExternalPerson) infoExternalPersonList.get(1);
            assertEquals(infoExternalPerson.getIdInternal(), new Integer(24));
            assertNotNull(infoExternalPerson.getInfoPerson());
            assertNotNull(infoExternalPerson.getInfoWorkLocation());
            assertEquals(infoExternalPerson.getInfoWorkLocation().getIdInternal(), new Integer(22));
            assertEquals(infoExternalPerson.getInfoWorkLocation().getName(), "INSTITUTO DE TELCOS");
            assertEquals(infoExternalPerson.getInfoPerson().getIdInternal(), new Integer(17902));
            assertEquals(infoExternalPerson.getInfoPerson().getNumeroDocumentoIdentificacao(), "1003");
            assertEquals(infoExternalPerson.getInfoPerson().getNome(), "Someone external");
            assertEquals(infoExternalPerson.getInfoPerson().getMorada(), "Some address");
            assertEquals(infoExternalPerson.getInfoPerson().getTelefone(), "1234567890");
            assertEquals(infoExternalPerson.getInfoPerson().getTelemovel(), "981234567");
            assertEquals(infoExternalPerson.getInfoPerson().getEmail(), "suporte@dotist.utl.pt");
            assertEquals(infoExternalPerson.getInfoPerson().getEnderecoWeb(),
                    "http://fenix-ashes.ist.utl.pt:8080");
            assertEquals(infoExternalPerson.getInfoPerson().getUsername(), "e1003");
            assertEquals(infoExternalPerson.getInfoPerson().getTipoDocumentoIdentificacao(),
                    new TipoDocumentoIdentificacao(TipoDocumentoIdentificacao.EXTERNO));

            //check third external person
            infoExternalPerson = (InfoExternalPerson) infoExternalPersonList.get(2);
            assertEquals(infoExternalPerson.getIdInternal(), new Integer(25));
            assertNotNull(infoExternalPerson.getInfoPerson());
            assertNotNull(infoExternalPerson.getInfoWorkLocation());
            assertEquals(infoExternalPerson.getInfoWorkLocation().getIdInternal(), new Integer(23));
            assertEquals(infoExternalPerson.getInfoWorkLocation().getName(),
                    "INSTITUTO DE SUP. ECONOMIA");
            assertEquals(infoExternalPerson.getInfoPerson().getIdInternal(), new Integer(17903));
            assertEquals(infoExternalPerson.getInfoPerson().getNumeroDocumentoIdentificacao(), "1004");
            assertEquals(infoExternalPerson.getInfoPerson().getNome(), "Someone external 2");
            assertEquals(infoExternalPerson.getInfoPerson().getMorada(), "Some address 2");
            assertEquals(infoExternalPerson.getInfoPerson().getTelefone(), "1234567891");
            assertEquals(infoExternalPerson.getInfoPerson().getTelemovel(), "981234566");
            assertEquals(infoExternalPerson.getInfoPerson().getEmail(), "suporte@dotist.utl.pt");
            assertEquals(infoExternalPerson.getInfoPerson().getEnderecoWeb(),
                    "http://fenix-ashes.ist.utl.pt:8080");
            assertEquals(infoExternalPerson.getInfoPerson().getUsername(), "e1004");
            assertEquals(infoExternalPerson.getInfoPerson().getTipoDocumentoIdentificacao(),
                    new TipoDocumentoIdentificacao(TipoDocumentoIdentificacao.EXTERNO));

            //ok

        } catch (Exception ex) {
            ex.printStackTrace();
            fail("testReadExistingExternalPersonsInAnyWorkLocation " + ex.getMessage());
        }

    }

    public void testNonExistingExternalPersonsInAnyWorkLocation() {
        try {

            Object[] args = { null};

            List infoExternalPersonList = (List) ServiceManagerServiceFactory.executeService(null,
                    getNameOfServiceToBeTested(), args);

            assertNotNull(infoExternalPersonList);
            assertTrue(infoExternalPersonList.isEmpty());

            //ok

        } catch (Exception ex) {
            ex.printStackTrace();
            fail("testReadNonExistingExternalPersonsInAnyWorkLocation " + ex.getMessage());
        }

    }

}