/*
 * Created on 21/Abr/2004
 */
package net.sourceforge.fenixedu.applicationTier.Servicos.sop.exams;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadExecutionPeriods;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servicos.ServiceTestCase;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

/**
 * @author Ana
 */
public class ReadExecutionPeriodsNoPeriodsTest extends ServiceTestCase {
    /**
     * @param name
     */
    public ReadExecutionPeriodsNoPeriodsTest(java.lang.String testName) {
        super(testName);
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadExecutionPeriods";
    }

    protected String getDataSetFilePath() {
        return "etc/datasets_templates/servicos/sop/testEmptyExamsV4.xml";
    }

    public void testSuccessfullReadExecutionPeriodsEmptyList() {
        ReadExecutionPeriods service = new ReadExecutionPeriods();

        ISuportePersistente sp;

        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            sp.iniciarTransaccao();

            List executionPeriodsList = service.run();

            sp.confirmarTransaccao();
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());

            assertEquals(executionPeriodsList.size(), 0);

        } catch (FenixServiceException ex) {
            fail("testSuccessfullReadExecutionPeriodsEmptyList" + ex);
        } catch (Exception ex) {
            fail("testSuccessfullReadExecutionPeriodsEmptyList error on compareDataSet" + ex);
        }
    }

}