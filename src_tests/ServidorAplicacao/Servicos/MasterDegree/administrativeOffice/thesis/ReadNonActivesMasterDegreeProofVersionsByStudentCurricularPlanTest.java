package net.sourceforge.fenixedu.applicationTier.Servicos.MasterDegree.administrativeOffice.thesis;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

import net.sourceforge.fenixedu.dataTransferObject.InfoExternalPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeProofVersion;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servicos.MasterDegree.administrativeOffice.AdministrativeOfficeBaseTest;
import net.sourceforge.fenixedu.util.MasterDegreeClassification;
import net.sourceforge.fenixedu.util.State;
import net.sourceforge.fenixedu.util.TipoCurso;

/**
 * @author Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author Nadir Tarmahomed (naat@mega.ist.utl.pt)
 */
public class ReadNonActivesMasterDegreeProofVersionsByStudentCurricularPlanTest extends
        AdministrativeOfficeBaseTest {

    /**
     * @param testName
     */
    public ReadNonActivesMasterDegreeProofVersionsByStudentCurricularPlanTest(String testName) {
        super(testName);
        this.dataSetFilePath = "etc/datasets/servicos/MasterDegree/administrativeOffice/thesis/testReadNonActivesMasterDegreeProofVersionsByStudentCurricularPlanDataSet.xml";
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadNonActivesMasterDegreeProofVersionsByStudentCurricularPlan";
    }

    protected Object[] getServiceArgumentsForNotAuthenticatedUser() {
        Object[] argsReadMasterDegreeProofVersion = { null };

        return argsReadMasterDegreeProofVersion;
    }

    protected Object[] getServiceArgumentsForNotAuthorizedUser() throws FenixServiceException {
        Object[] argsReadStudentCurricularPlan = { new Integer(142), new TipoCurso(TipoCurso.MESTRADO) };
        InfoStudentCurricularPlan infoStudentCurricularPlan = (InfoStudentCurricularPlan) ServiceManagerServiceFactory
                .executeService(userViewNotAuthorized,
                        "student.ReadActiveStudentCurricularPlanByNumberAndDegreeType",
                        argsReadStudentCurricularPlan);

        Object[] argsReadMasterDegreeProofVersion = { infoStudentCurricularPlan };

        return argsReadMasterDegreeProofVersion;
    }

    public void testReadExistingNonActivesMasterDegreeProofVersions() {
        try {
            Object[] argsReadStudentCurricularPlan = { new Integer(142),
                    new TipoCurso(TipoCurso.MESTRADO) };
            InfoStudentCurricularPlan infoStudentCurricularPlan = (InfoStudentCurricularPlan) ServiceManagerServiceFactory
                    .executeService(userView,
                            "student.ReadActiveStudentCurricularPlanByNumberAndDegreeType",
                            argsReadStudentCurricularPlan);

            Object[] argsReadMasterDegreeProofVersions = { infoStudentCurricularPlan };
            List infoMasterDegreeProofVersions = (List) ServiceManagerServiceFactory.executeService(
                    userView, getNameOfServiceToBeTested(), argsReadMasterDegreeProofVersions);

            assertNotNull(infoMasterDegreeProofVersions);
            assertEquals(infoMasterDegreeProofVersions.size(), 1);

            InfoMasterDegreeProofVersion infoMasterDegreeProofVersion = (InfoMasterDegreeProofVersion) infoMasterDegreeProofVersions
                    .get(0);

            assertEquals(infoMasterDegreeProofVersion.getIdInternal(), new Integer(2));
            assertEquals(infoMasterDegreeProofVersion.getInfoMasterDegreeThesis().getIdInternal(),
                    new Integer(1));
            assertEquals(infoMasterDegreeProofVersion.getInfoResponsibleEmployee().getIdInternal(),
                    new Integer(1194));
            assertEquals(infoMasterDegreeProofVersion.getCurrentState(), new State(State.INACTIVE));
            assertEquals(infoMasterDegreeProofVersion.getAttachedCopiesNumber(), new Integer(5));
            assertEquals(infoMasterDegreeProofVersion.getFinalResult(),
                    MasterDegreeClassification.APPROVED);
            Date proofDate = new GregorianCalendar(2003, Calendar.OCTOBER, 10).getTime();
            assertEquals(infoMasterDegreeProofVersion.getProofDate(), proofDate);
            Date thesisDeliveryDate = new GregorianCalendar(2003, Calendar.NOVEMBER, 11).getTime();
            assertEquals(infoMasterDegreeProofVersion.getThesisDeliveryDate(), thesisDeliveryDate);
            assertEquals(((InfoTeacher) infoMasterDegreeProofVersion.getInfoJuries().get(0))
                    .getIdInternal(), new Integer(954));
            assertEquals(((InfoExternalPerson) infoMasterDegreeProofVersion.getInfoExternalJuries().get(
                    0)).getIdInternal(), new Integer(1));

            //ok

        } catch (Exception ex) {
            ex.printStackTrace();
            fail("testReadExistingNonActivesMasterDegreeProofVersions " + ex.getMessage());
        }

    }

    public void testReadNonExistingNonActivesMasterDegreeProofVersions() {
        try {
            Object[] argsReadStudentCurricularPlan = { new Integer(5461),
                    new TipoCurso(TipoCurso.MESTRADO) };
            InfoStudentCurricularPlan infoStudentCurricularPlan = (InfoStudentCurricularPlan) ServiceManagerServiceFactory
                    .executeService(userView,
                            "student.ReadActiveStudentCurricularPlanByNumberAndDegreeType",
                            argsReadStudentCurricularPlan);

            Object[] argsReadMasterDegreeProofVersions = { infoStudentCurricularPlan };

            List infoMasterDegreeProofVersions = (List) ServiceManagerServiceFactory.executeService(
                    userView, getNameOfServiceToBeTested(), argsReadMasterDegreeProofVersions);

            assertNotNull(infoMasterDegreeProofVersions);
            assertTrue(infoMasterDegreeProofVersions.isEmpty());

        } catch (FenixServiceException e) {
            e.printStackTrace();
            fail("testReadNonExistingNonActivesMasterDegreeProofVersions " + e.getMessage());
        }

    }

}