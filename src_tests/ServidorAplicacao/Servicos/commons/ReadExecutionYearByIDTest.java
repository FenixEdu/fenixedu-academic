package net.sourceforge.fenixedu.applicationTier.Servicos.commons;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.applicationTier.Servicos.ServiceTestCase;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali</a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed</a>
 *
 */
public class ReadExecutionYearByIDTest extends ServiceTestCase {

    private String dataSetFilePath;

    /**
     * @param testName
     */
    public ReadExecutionYearByIDTest(String testName) {
        super(testName);
        this.dataSetFilePath = "etc/datasets/servicos/commons/testReadExecutionYearByIDDataSet.xml";
    }

    protected String getDataSetFilePath() {
        return this.dataSetFilePath;
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadExecutionYearByID";
    }

    public void testReadExisting() {
        try {
            Integer executionYearID = new Integer(1);
            Object[] args = { executionYearID };

            InfoExecutionYear infoExecutionYear = (InfoExecutionYear) ServiceManagerServiceFactory
                    .executeService(null, getNameOfServiceToBeTested(), args);

            assertNotNull(infoExecutionYear);
            assertEquals(infoExecutionYear.getIdInternal(), executionYearID);

        } catch (Exception ex) {
            ex.printStackTrace();
            fail("testReadExisting " + ex.getMessage());
        }

    }

    public void testReadNonExisting() {
        try {
            Integer executionYearID = new Integer(50);
            Object[] args = { executionYearID };

            InfoExecutionYear infoExecutionYear = (InfoExecutionYear) ServiceManagerServiceFactory
                    .executeService(null, getNameOfServiceToBeTested(), args);

            assertNull(infoExecutionYear);

        } catch (Exception ex) {
            ex.printStackTrace();
            fail("testReadNonExisting " + ex.getMessage());
        }

    }

}