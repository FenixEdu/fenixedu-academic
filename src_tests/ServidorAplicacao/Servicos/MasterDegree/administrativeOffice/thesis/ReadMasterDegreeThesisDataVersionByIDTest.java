package net.sourceforge.fenixedu.applicationTier.Servicos.MasterDegree.administrativeOffice.thesis;

import java.util.List;

import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

import net.sourceforge.fenixedu.dataTransferObject.InfoExternalPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servicos.MasterDegree.administrativeOffice.AdministrativeOfficeBaseTest;
import net.sourceforge.fenixedu.util.State;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 */
public class ReadMasterDegreeThesisDataVersionByIDTest extends AdministrativeOfficeBaseTest {

    /**
     * @param testName
     */
    public ReadMasterDegreeThesisDataVersionByIDTest(String testName) {
        super(testName);
        this.dataSetFilePath = "etc/datasets/servicos/MasterDegree/administrativeOffice/thesis/testReadMasterDegreeThesisDataVersionByIDDataSet.xml";
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadMasterDegreeThesisDataVersionByID";
    }

    protected Object[] getServiceArgumentsForNotAuthenticatedUser() {
        Object[] argsReadMasterDegreeThesisDataVersion = { null };

        return argsReadMasterDegreeThesisDataVersion;
    }

    protected Object[] getServiceArgumentsForNotAuthorizedUser() {
        Object[] argsReadMasterDegreeThesisDataVersion = { new Integer(1) };

        return argsReadMasterDegreeThesisDataVersion;
    }

    public void testReadExistingMasterDegreeThesisDataVersion() {
        try {
            Object[] argsReadMasterDegreeThesisDataVersion = { new Integer(1) };
            InfoMasterDegreeThesisDataVersion infoMasterDegreeThesisDataVersion = (InfoMasterDegreeThesisDataVersion) ServiceManagerServiceFactory
                    .executeService(userView, getNameOfServiceToBeTested(),
                            argsReadMasterDegreeThesisDataVersion);

            assertEquals(infoMasterDegreeThesisDataVersion.getIdInternal(), new Integer(1));
            assertEquals(infoMasterDegreeThesisDataVersion.getInfoMasterDegreeThesis().getIdInternal(),
                    new Integer(1));
            assertEquals(infoMasterDegreeThesisDataVersion.getInfoResponsibleEmployee().getIdInternal(),
                    new Integer(1194));
            assertEquals(infoMasterDegreeThesisDataVersion.getDissertationTitle(), "some title");
            assertEquals(infoMasterDegreeThesisDataVersion.getCurrentState(), new State(State.ACTIVE));
            List infoGuiders = infoMasterDegreeThesisDataVersion.getInfoGuiders();
            List infoAssistentGuiders = infoMasterDegreeThesisDataVersion.getInfoAssistentGuiders();
            List infoExternalAssistentGuiders = infoMasterDegreeThesisDataVersion
                    .getInfoExternalAssistentGuiders();
            List infoExternalPersonExternalGuiders = infoMasterDegreeThesisDataVersion
                    .getInfoExternalGuiders();
            assertEquals(((InfoTeacher) infoGuiders.get(0)).getIdInternal(), new Integer(954));
            assertEquals(((InfoTeacher) infoAssistentGuiders.get(0)).getIdInternal(), new Integer(955));
            assertEquals(((InfoExternalPerson) infoExternalAssistentGuiders.get(0)).getIdInternal(),
                    new Integer(1));
            assertEquals(
                    ((InfoExternalPerson) infoExternalPersonExternalGuiders.get(0)).getIdInternal(),
                    new Integer(2));
            //ok

        } catch (Exception ex) {
            ex.printStackTrace();
            fail("testReadExistingMasterDegreeThesisDataVersion " + ex.getMessage());
        }

    }

    public void testReadNonExistentMasterDegreeThesisDataVersion() {
        try {
            Object[] argsReadMasterDegreeThesisDataVersion = { new Integer(65456) };
            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(),
                    argsReadMasterDegreeThesisDataVersion);

            fail("testReadNonExistentMasterDegreeThesisDataVersion did not throw NonExistingServiceException");

        } catch (NonExistingServiceException ex) {
            //ok

        } catch (FenixServiceException e) {
            e.printStackTrace();
            fail("testReadNonExistentMasterDegreeThesisDataVersion " + e.getMessage());
        }

    }

}