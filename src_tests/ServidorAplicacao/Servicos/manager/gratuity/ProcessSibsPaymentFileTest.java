package ServidorAplicacao.Servicos.manager.gratuity;

import java.util.ArrayList;
import java.util.List;

import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.gratuity.masterDegree.DuplicateSibsPaymentFileProcessingServiceException;
import ServidorAplicacao.Servicos.manager.ManagerBaseTest;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author - Nadir Tarmahomed (naat@mega.ist.utl.pt)
 */
public class ProcessSibsPaymentFileTest extends ManagerBaseTest {

    /**
     * @param testName
     */
    public ProcessSibsPaymentFileTest(String testName) {

        super(testName);

        if (testName.equals("testDuplicateFileProcessing")) {
            this.dataSetFilePath = "etc/datasets/servicos/manager/gratuity/testProcessSibsPaymentFileDuplicateFileProcessingDataSet.xml";
        } else if (testName.equals("testDuplicateGratuityPaymentInsideSystem")) {
            this.dataSetFilePath = "etc/datasets/servicos/manager/gratuity/testProcessSibsPaymentFileDuplicateGratuityPaymentInsideSystemDataSet.xml";
        } else if (testName.equals("testDuplicateInsurancePaymentInsideSystem")) {
            this.dataSetFilePath = "etc/datasets/servicos/manager/gratuity/testProcessSibsPaymentFileDuplicateInsurancePaymentInsideSystemDataSet.xml";
        } else if (testName.equals("testInvalidExecutionDegree")) {
            this.dataSetFilePath = "etc/datasets/servicos/manager/gratuity/testProcessSibsPaymentFileInvalidExecutionDegreeDataSet.xml";
        } else if (testName.equals("testSuccessfullProcessing")) {
            this.dataSetFilePath = "etc/datasets/servicos/manager/gratuity/testProcessSibsPaymentFileSuccessfullProcessingDataSet.xml";
        } else {
            this.dataSetFilePath = "etc/datasets/servicos/manager/gratuity/testProcessSibsPaymentFileDataSet.xml";
        }

    }

    protected String getNameOfServiceToBeTested() {
        return "ProcessSibsPaymentFile";
    }

    protected Object[] getServiceArgumentsForNotAuthenticatedUser() {

        String filename = "testFile.txt";
        List fileEntries = new ArrayList();

        Object args[] = { filename, fileEntries };

        return args;

    }

    protected Object[] getServiceArgumentsForNotAuthorizedUser() throws FenixServiceException {

        String filename = "testFile.txt";
        List fileEntries = new ArrayList();

        Object args[] = { filename, fileEntries };

        return args;
    }

    public void testDuplicateFileProcessing() {

        try {

            String filename = "meps_20040329.txt";
            List fileEntries = new ArrayList();
            fileEntries
                    .add("20400180002465320040301144600000500000006305000000306500000000000000000000030497040000000000000000  ");
            fileEntries
                    .add("20400530047157420040312194600001500000006301003305500158384Benfica        030497040000000000000000  ");

            Object args[] = { filename, fileEntries };

            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), args);

        } catch (DuplicateSibsPaymentFileProcessingServiceException e) {
            //compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets/servicos/manager/gratuity/testProcessSibsPaymentFileDuplicateFileProcessingDataSet.xml");
            //ok

        } catch (Exception ex) {
            ex.printStackTrace();
            fail("testDuplicateFileProcessing " + ex.getMessage());
        }
    }

    public void testDuplicateGratuityPaymentInsideFile() {
        try {

            String filename = "meps_20040329.txt";
            List fileEntries = new ArrayList();
            fileEntries
                    .add("20400180002465320040301144600000500000006305000000306500000000000000000000030497040000000000000000  ");
            fileEntries
                    .add("20400530047157420040312194600001500000006301003305500158384Benfica        030497040000000000000000  ");

            Object args[] = { filename, fileEntries };

            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), args);

            compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets/servicos/manager/gratuity/testExpectedProcessSibsPaymentFileDuplicateGratuityPaymentInsideFileDataSet.xml");
            //ok

        } catch (Exception ex) {
            ex.printStackTrace();
            fail("testDuplicateFileProcessing " + ex.getMessage());
        }
    }

    public void testDuplicateGratuityPaymentInsideSystem() {
        try {

            String filename = "meps_20040330.txt";
            List fileEntries = new ArrayList();
            fileEntries
                    .add("20400180002465320040301144600000500000006305000000306500000000000000000000030497040000000000000000  ");
            fileEntries
                    .add("20400530047157420040312194600001500000006301003305500158384Benfica        030497040000000000000000  ");

            Object args[] = { filename, fileEntries };

            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), args);

            compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets/servicos/manager/gratuity/testExpectedProcessSibsPaymentFileDuplicateGratuityPaymentInsideSystemDataSet.xml");
            //ok

        } catch (Exception ex) {
            ex.printStackTrace();
            fail("testDuplicateFileProcessing " + ex.getMessage());
        }
    }

    public void testDuplicateInsurancePaymentInsideFile() {
        try {

            String filename = "meps_20040329.txt";
            List fileEntries = new ArrayList();
            fileEntries
                    .add("20400180002465320040301144600000500000006305000000306500000000000000000000030497020000000000000000  ");
            fileEntries
                    .add("20400530047157420040312194600001500000006301003305500158384Benfica        030497020000000000000000  ");

            Object args[] = { filename, fileEntries };

            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), args);

            compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets/servicos/manager/gratuity/testExpectedProcessSibsPaymentFileDuplicateInsurancePaymentInsideFileDataSet.xml");
            //ok

        } catch (Exception ex) {
            ex.printStackTrace();
            fail("testDuplicateFileProcessing " + ex.getMessage());
        }
    }

    public void testDuplicateInsurancePaymentInsideSystem() {
        try {

            String filename = "meps_20040330.txt";
            List fileEntries = new ArrayList();
            fileEntries
                    .add("20400180002465320040301144600000500000006305000000306500000000000000000000030497020000000000000000  ");

            Object args[] = { filename, fileEntries };

            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), args);

            compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets/servicos/manager/gratuity/testExpectedProcessSibsPaymentFileDuplicateInsurancePaymentInsideSystemDataSet.xml");
            //ok

        } catch (Exception ex) {
            ex.printStackTrace();
            fail("testDuplicateFileProcessing " + ex.getMessage());
        }
    }

    /**
     * 
     * SIBS prevents this kind of situations to happen, because it only accepts
     * the codes we send. Although this is unecessary, helps us to detect errors
     * regarding to file generation
     */
    public void testInvalidExecutionYear() {
        try {

            String filename = "meps_20040329.txt";
            List fileEntries = new ArrayList();
            fileEntries
                    .add("20400180002465320040301144600000500000006305000000306500000000000000000000000497040000000000000000  ");

            Object args[] = { filename, fileEntries };

            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), args);

            compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets/servicos/manager/gratuity/testExpectedProcessSibsPaymentFileInvalidExecutionYearDataSet.xml");
            //ok

        } catch (Exception ex) {
            ex.printStackTrace();
            fail("testDuplicateFileProcessing " + ex.getMessage());
        }
    }

    /**
     * 
     * SIBS prevents this kind of situations to happen, because it only accepts
     * the codes we send. Although this is unecessary, helps us to detect errors
     * regarding to file generation. Its particulary usefull when student is
     * doing a master degree and a specialization
     */
    public void testInvalidExecutionDegree() {
        try {

            String filename = "meps_20040329.txt";
            List fileEntries = new ArrayList();
            fileEntries
                    .add("20400180002465320040301144600000500000006305000000306500000000000000000000030497040000000000000000  ");

            Object args[] = { filename, fileEntries };

            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), args);

            compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets/servicos/manager/gratuity/testExpectedProcessSibsPaymentFileInvalidExecutionDegreeDataSet.xml");
            //ok

        } catch (Exception ex) {
            ex.printStackTrace();
            fail("testDuplicateFileProcessing " + ex.getMessage());
        }
    }

    public void testSuccessfullProcessing() {
        try {

            String filename = "meps_20040329.txt";
            List fileEntries = new ArrayList();
            fileEntries
                    .add("20400180002465320040301144600000500000006305000000306500000000000000000000030497040000000000000000  ");
            fileEntries
                    .add("20400530047157420040312194600001500000006301003305500158384Benfica        030497020000000000000000  ");
            fileEntries
                    .add("20400180002465320040301144600000500000006305000000306500000000000000000000030018630000000000000000  ");

            Object args[] = { filename, fileEntries };

            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), args);

            compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets/servicos/manager/gratuity/testExpectedProcessSibsPaymentFileSuccessfullProcessingDataSet.xml");
            //ok

        } catch (Exception ex) {
            ex.printStackTrace();
            fail("testDuplicateFileProcessing " + ex.getMessage());
        }
    }

}