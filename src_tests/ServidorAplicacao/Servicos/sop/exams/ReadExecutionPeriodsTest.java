/*
 * Created on 22/Out/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servicos.sop.exams;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadExecutionPeriods;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servicos.ServiceTestCase;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

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
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
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