/*
 * Created on 13/Jan/2004
 *  
 */
package ServidorAplicacao.Servicos.MasterDegree.administrativeOffice.gratuity;

import framework.factory.ServiceManagerServiceFactory;
import DataBeans.InfoGratuityValues;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.MasterDegree.administrativeOffice.AdministrativeOfficeBaseTest;

/**
 * @author Tânia Pousão
 *  
 */
public class ReadGratuityValuesByExecutionDegreeTest extends AdministrativeOfficeBaseTest {

    public ReadGratuityValuesByExecutionDegreeTest(String name) {
        super(name);
        this.dataSetFilePath = "etc/datasets/servicos/MasterDegree/administrativeOffice/gratuity/testReadGratuityValuesByExecutionDegreeDataSet.xml";
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadGratuityValuesByExecutionDegree";
    }

    protected Object[] getServiceArgumentsForNotAuthenticatedUser() {
        Object[] args = { new Integer(10) };

        return args;
    }

    protected Object[] getServiceArgumentsForNotAuthorizedUser() {
        Object[] args = { new Integer(10) };

        return args;
    }

    public void testSucessReadGratuityValues() {
        Object[] args = { new Integer(10) };

        InfoGratuityValues infoGratuityValues = null;
        try {
            infoGratuityValues = (InfoGratuityValues) ServiceManagerServiceFactory.executeService(
                    userView, getNameOfServiceToBeTested(), args);
        } catch (FenixServiceException e) {
            e.printStackTrace();
            fail("testSucessReadGratuityValues " + e.getMessage());
        }

        //<GRATUITY_VALUES ID_INTERNAL='1' ANUAL_VALUE='1000'
        // SCHOLARSHIP_VALUE='NULL'
        // FINAL_PROOF_VALUE='NULL'
        //COURSE_VALUE='50' CREDIT_VALUE='NULL' PROOF_REQUEST_PAYMENT='1'
        // START_PAYMENT='2003-01-01'
        // END_PAYMENT='2003-12-31' KEY_EXECUTION_DEGREE='10' />
        assertNotNull(infoGratuityValues);
        assertEquals(infoGratuityValues.getIdInternal(), new Integer(1));
        assertEquals(infoGratuityValues.getAnualValue(), new Double(1000));
        assertNull(infoGratuityValues.getScholarShipValue());
        assertNull(infoGratuityValues.getFinalProofValue());
        assertEquals(infoGratuityValues.getCourseValue(), new Double(50));
        assertNull(infoGratuityValues.getCreditValue());
        assertEquals(infoGratuityValues.getProofRequestPayment(), Boolean.TRUE);
        assertNotNull(infoGratuityValues.getInfoExecutionDegree());
        assertEquals(infoGratuityValues.getInfoExecutionDegree().getIdInternal(), new Integer(10));
    }

    public void testReadGratuityValuesThatDontExists() {
        Object[] args = { new Integer(11) };

        InfoGratuityValues infoGratuityValues = null;
        try {
            infoGratuityValues = (InfoGratuityValues) ServiceManagerServiceFactory.executeService(
                    userView, getNameOfServiceToBeTested(), args);
        } catch (FenixServiceException e) {
            e.printStackTrace();
            fail("testSucessReadGratuityValues " + e.getMessage());
        }

        assertNull((infoGratuityValues));
    }

    public void testReadGratuityValuesWithArgNULL() {
        Object[] args = { null };

        try {
            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), args);
        } catch (FenixServiceException e) {
            e.printStackTrace();
            String msg = e.getMessage().substring(e.getMessage().lastIndexOf(".") + 1,
                    e.getMessage().lastIndexOf(".") + 16);
            assertEquals(new String("noGratuityValues"), msg);
        }
    }

    public void testReadGratuityValuesWithExecutionDegreeUnExist() {
        Object[] args = { new Integer(12) };

        try {
            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), args);
        } catch (FenixServiceException e) {
            e.printStackTrace();
            String msg = e.getMessage().substring(e.getMessage().lastIndexOf(".") + 1,
                    e.getMessage().lastIndexOf(".") + 16);
            assertEquals(new String("noGratuityValues"), msg);
        }
    }
}