/*
 * Created on Abr 1, 2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servicos.sop.exams;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.sop.exams.DeleteExamNew;
import net.sourceforge.fenixedu.applicationTier.Servicos.ServiceTestCase;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

/**
 * @author Ana e Ricardo
 *  
 */
public class DeleteExamNewTest extends ServiceTestCase {

    /**
     * @param name
     */
    public DeleteExamNewTest(java.lang.String testName) {
        super(testName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.sop.exams.ServiceNeedsAuthenticationTestCase#getNameOfServiceToBeTested()
     */

    protected String getNameOfServiceToBeTested() {
        return "DeleteExamNew";
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.sop.exams.ServiceNeedsAuthenticationTestCase#getDataSetFilePath()
     */
    protected String getDataSetFilePath() {
        return "etc/datasets_templates/servicos/sop/testDeleteExamsV4dataset.xml";
    }

    protected String getExpectedDataSetWithRoomOccupationWithNonExclusivePeriodFilePath() {
        return "etc/datasets_templates/servicos/sop/testExpectedDeleteExamsV4dataset.xml";
    }

    protected String getExpectedDataSetWithRoomOccupationWithExclusivePeriodFilePath() {
        return "etc/datasets_templates/servicos/sop/testExpectedDeleteExamsV4ExclusivePeriodDataset.xml";
    }

    protected String getExpectedDataSetWithNoRoomOccupation() {
        return "etc/datasets_templates/servicos/sop/testExpectedDeleteExamsV4NoRoomOccupationDataset.xml";
    }

    // successfull delete exam
    public void testSuccessfullDeleteExamNewWithRoomOccupationWithNonExclusivePeriod() {
        Integer examID = new Integer(4526);

        DeleteExamNew service = DeleteExamNew.getService();

        ISuportePersistente sp;

        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            sp.iniciarTransaccao();

            service.run(examID);

            sp.confirmarTransaccao();
            compareDataSetUsingExceptedDataSetTableColumns(getExpectedDataSetWithRoomOccupationWithNonExclusivePeriodFilePath());
        } catch (FenixServiceException ex) {
            fail("testSuccessfullDeleteExamNewWithRoomOccupationWithNonExclusivePeriod - Fenix Service Exception "
                    + ex);
        } catch (Exception ex) {
            fail("testSuccessfullDeleteExamNewWithRoomOccupationWithNonExclusivePeriod - Exception "
                    + ex);
        }
    }

    // successfull delete exam
    public void testSuccessfullDeleteExamNewWithRoomOccupationWithExclusivePeriod() {
        Integer examID = new Integer(4529);

        DeleteExamNew service = DeleteExamNew.getService();

        ISuportePersistente sp;

        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            sp.iniciarTransaccao();

            service.run(examID);

            sp.confirmarTransaccao();
            compareDataSetUsingExceptedDataSetTableColumns(getExpectedDataSetWithRoomOccupationWithExclusivePeriodFilePath());
        } catch (FenixServiceException ex) {
            fail("testSuccessfullDeleteExamNewWithRoomOccupationWithExclusivePeriod - Fenix Service Exception "
                    + ex);
        } catch (Exception ex) {
            fail("testSuccessfullDeleteExamNewWithRoomOccupationWithExclusivePeriod - Exception " + ex);
        }
    }

    // successfull delete exam
    public void testSuccessfullDeleteExamNewWithNoRoomOccupation() {
        Integer examID = new Integer(4527);

        DeleteExamNew service = DeleteExamNew.getService();

        ISuportePersistente sp;

        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            sp.iniciarTransaccao();

            service.run(examID);

            sp.confirmarTransaccao();
            compareDataSetUsingExceptedDataSetTableColumns(getExpectedDataSetWithNoRoomOccupation());
        } catch (FenixServiceException ex) {
            fail("testSuccessfullDeleteExamNewWithNoRoomOccupation - Fenix Service Exception " + ex);
        } catch (Exception ex) {
            fail("testSuccessfullDeleteExamNewWithNoRoomOccupation - Exception " + ex);
        }
    }

    // the exam does not exist
    public void testUnexistingExam() {
        Integer examOID = new Integer("24");

        DeleteExamNew service = DeleteExamNew.getService();

        ISuportePersistente sp = null;

        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            sp.iniciarTransaccao();

            service.run(examOID);
            fail("testUnexistingExam");
            sp.cancelarTransaccao();
        } catch (FenixServiceException ex) {
            try {
                sp.cancelarTransaccao();
            } catch (ExcepcaoPersistencia e) {

                fail("testUnexistingExam - Exception cancelar transacção " + ex);
            }
            compareDataSetUsingExceptedDataSetTableColumns(getDataSetFilePath());
        } catch (Exception ex) {
            //sp.cancelarTransaccao();
            fail("testUnexistingExam - Exception " + ex);
        }
    }

}