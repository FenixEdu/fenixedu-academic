package net.sourceforge.fenixedu.applicationTier.Servicos.commons.workLocation;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoWorkLocation;
import net.sourceforge.fenixedu.applicationTier.Servicos.ServiceTestCase;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author - Nadir Tarmahomed (naat@mega.ist.utl.pt)
 */
public class ReadAllWorkLocationsTest extends ServiceTestCase {

    private String dataSetFilePath;

    /**
     * @param testName
     */
    public ReadAllWorkLocationsTest(String testName) {
        super(testName);
        if (testName.equals("testReadExisting")) {
            this.dataSetFilePath = "etc/datasets/servicos/commons/workLocation/testReadAllWorkLocationsReadExistingDataSet.xml";
        } else if (testName.equals("testReadNonExisting")) {
            this.dataSetFilePath = "etc/datasets/servicos/commons/workLocation/testReadAllWorkLocationsReadNonExistingDataSet.xml";
        }

    }

    protected void setUp() {
        super.setUp();
    }

    protected String getDataSetFilePath() {
        return this.dataSetFilePath;
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadAllWorkLocations";
    }

    public void testReadExisting() {
        try {

            Object[] args = {};

            List workLocationList = (List) ServiceManagerServiceFactory.executeService(null,
                    getNameOfServiceToBeTested(), args);
            assertNotNull(workLocationList);
            assertTrue(workLocationList.size() == 2);

            // check the first Work Location
            InfoWorkLocation infoWorkLocation = (InfoWorkLocation) workLocationList.get(0);
            assertEquals(infoWorkLocation.getIdInternal(), new Integer(21));
            assertEquals(infoWorkLocation.getName(), "DEC - Faculdade de Ciências e Tecnologia /UNL");

            // check the second Work Location
            infoWorkLocation = (InfoWorkLocation) workLocationList.get(1);
            assertEquals(infoWorkLocation.getIdInternal(), new Integer(22));
            assertEquals(infoWorkLocation.getName(), "INSTITUTO DE TELECOMUNICAÇÕES");

            //ok

        } catch (Exception ex) {
            ex.printStackTrace();
            fail("testReadExisting " + ex.getMessage());
        }

    }

    public void testReadNonExisting() {
        try {

            Object[] args = {};

            List infoWorkLocationList = (List) ServiceManagerServiceFactory.executeService(null,
                    getNameOfServiceToBeTested(), args);

            assertNotNull(infoWorkLocationList);
            assertTrue(infoWorkLocationList.isEmpty());

            //ok

        } catch (Exception ex) {
            ex.printStackTrace();
            fail("testReadNonExisting " + ex.getMessage());
        }

    }

}