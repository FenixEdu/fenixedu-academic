/*
 * Created on 22/Out/2003
 *
 */
package ServidorAplicacao.Servicos.sop.exams;

import java.util.List;

import ServidorAplicacao.Servico.commons.ReadExecutionPeriods;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.ServiceTestCase;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Ana e Ricardo
 *  
 */
public class ReadExecutionPeriodsTest extends ServiceTestCase {

    /**
     * @param name
     */
    public ReadExecutionPeriodsTest(java.lang.String testName) {
        super(testName);
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadExecutionPeriods";
    }

    protected String getDataSetFilePath() {
        return "etc/datasets_templates/servicos/sop/testReadFilteredExamsMapDataset.xml";
    }

    public void testSuccessfullReadExecutionPeriods() {

        ReadExecutionPeriods service = new ReadExecutionPeriods();

        ISuportePersistente sp;

        try {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            List executionPeriodsList = service.run();

            sp.confirmarTransaccao();
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
            assertEquals(executionPeriodsList.size(), 3);

        } catch (FenixServiceException ex) {
            fail("testSuccessfullReadExecutionPeriods" + ex);
        } catch (Exception ex) {
            fail("testSuccessfullReadExecutionPeriods error on compareDataSet" + ex);
        }
    }

}