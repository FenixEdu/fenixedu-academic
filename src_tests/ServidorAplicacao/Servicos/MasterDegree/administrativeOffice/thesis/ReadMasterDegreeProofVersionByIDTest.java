package net.sourceforge.fenixedu.applicationTier.Servicos.MasterDegree.administrativeOffice.thesis;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeProofVersion;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servicos.MasterDegree.administrativeOffice.AdministrativeOfficeBaseTest;
import net.sourceforge.fenixedu.util.MasterDegreeClassification;
import net.sourceforge.fenixedu.util.State;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 */
public class ReadMasterDegreeProofVersionByIDTest extends AdministrativeOfficeBaseTest {

    /**
     * @param testName
     */
    public ReadMasterDegreeProofVersionByIDTest(String testName) {
        super(testName);
        this.dataSetFilePath = "etc/datasets/servicos/MasterDegree/administrativeOffice/thesis/testReadMasterDegreeProofVersionByIDDataSet.xml";
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadMasterDegreeProofVersionByID";
    }

    protected Object[] getServiceArgumentsForNotAuthenticatedUser() {
        Object[] argsReadMasterDegreeProofVersion = { null };

        return argsReadMasterDegreeProofVersion;
    }

    protected Object[] getServiceArgumentsForNotAuthorizedUser() {
        Object[] argsReadMasterDegreeProofVersion = { new Integer(1) };

        return argsReadMasterDegreeProofVersion;
    }

    public void testReadExistingMasterDegreeProofVersion() {
        try {
            Object[] argsReadMasterDegreeProofVersion = { new Integer(1) };
            InfoMasterDegreeProofVersion infoMasterDegreeProofVersion = (InfoMasterDegreeProofVersion) ServiceManagerServiceFactory
                    .executeService(userView, getNameOfServiceToBeTested(),
                            argsReadMasterDegreeProofVersion);

            assertEquals(infoMasterDegreeProofVersion.getIdInternal(), new Integer(1));
            assertEquals(infoMasterDegreeProofVersion.getInfoMasterDegreeThesis().getIdInternal(),
                    new Integer(1));
            assertEquals(infoMasterDegreeProofVersion.getInfoResponsibleEmployee().getIdInternal(),
                    new Integer(1194));
            assertEquals(infoMasterDegreeProofVersion.getCurrentState(), new State(State.ACTIVE));
            assertEquals(infoMasterDegreeProofVersion.getAttachedCopiesNumber(), new Integer(5));
            assertEquals(infoMasterDegreeProofVersion.getFinalResult(),
                    MasterDegreeClassification.APPROVED);
            Date proofDate = new GregorianCalendar(2003, Calendar.OCTOBER, 10).getTime();
            assertEquals(infoMasterDegreeProofVersion.getProofDate(), proofDate);
            Date thesisDeliveryDate = new GregorianCalendar(2003, Calendar.NOVEMBER, 11).getTime();
            assertEquals(infoMasterDegreeProofVersion.getThesisDeliveryDate(), thesisDeliveryDate);
            assertEquals(((InfoTeacher) infoMasterDegreeProofVersion.getInfoJuries().get(0))
                    .getIdInternal(), new Integer(954));

            //ok

        } catch (Exception ex) {
            ex.printStackTrace();
            fail("testReadExistingMasterDegreeProofVersion " + ex.getMessage());
        }

    }

    public void testReadNonExistingMasterDegreeProofVersion() {
        try {
            Object[] argsReadMasterDegreeProofVersion = { new Integer(50) };

            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(),
                    argsReadMasterDegreeProofVersion);

            fail("testReadNonExistingMasterDegreeProofVersion did not throw NonExistingServiceException");

        } catch (NonExistingServiceException ex) {
            //ok

        } catch (FenixServiceException e) {
            e.printStackTrace();
            fail("testReadNonExistingMasterDegreeProofVersion " + e.getMessage());
        }

    }

}