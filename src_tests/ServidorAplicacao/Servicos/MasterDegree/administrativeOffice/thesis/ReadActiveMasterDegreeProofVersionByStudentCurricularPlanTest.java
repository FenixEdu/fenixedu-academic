package net.sourceforge.fenixedu.applicationTier.Servicos.MasterDegree.administrativeOffice.thesis;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

import net.sourceforge.fenixedu.dataTransferObject.InfoExternalPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeProofVersion;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ScholarshipNotFinishedServiceException;
import net.sourceforge.fenixedu.applicationTier.Servicos.MasterDegree.administrativeOffice.AdministrativeOfficeBaseTest;
import net.sourceforge.fenixedu.util.MasterDegreeClassification;
import net.sourceforge.fenixedu.util.State;
import net.sourceforge.fenixedu.util.TipoCurso;

/**
 * @author Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author Nadir Tarmahomed (naat@mega.ist.utl.pt)
 */
public class ReadActiveMasterDegreeProofVersionByStudentCurricularPlanTest extends
        AdministrativeOfficeBaseTest {

    /**
     * @param testName
     */
    public ReadActiveMasterDegreeProofVersionByStudentCurricularPlanTest(String testName) {
        super(testName);
        this.dataSetFilePath = "etc/datasets/servicos/MasterDegree/administrativeOffice/thesis/testReadActiveMasterDegreeProofVersionByStudentCurricularPlanDataSet.xml";
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadActiveMasterDegreeProofVersionByStudentCurricularPlan";
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

    public void testSuccessReadActiveMasterDegreeProofVersion() {
        try {
            Object[] argsReadStudentCurricularPlan = { new Integer(142),
                    new TipoCurso(TipoCurso.MESTRADO) };
            InfoStudentCurricularPlan infoStudentCurricularPlan = (InfoStudentCurricularPlan) ServiceManagerServiceFactory
                    .executeService(userView,
                            "student.ReadActiveStudentCurricularPlanByNumberAndDegreeType",
                            argsReadStudentCurricularPlan);

            Object[] argsReadMasterDegreeProofVersion = { infoStudentCurricularPlan };
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
            assertEquals(((InfoExternalPerson) infoMasterDegreeProofVersion.getInfoExternalJuries().get(
                    0)).getIdInternal(), new Integer(1));

            //ok

        } catch (Exception ex) {
            ex.printStackTrace();
            fail("testSuccessReadActiveMasterDegreeProofVersion " + ex.getMessage());
        }

    }

    public void testReadWithoutScholarShipFinished() {
        try {
            Object[] argsReadStudentCurricularPlan = { new Integer(209),
                    new TipoCurso(TipoCurso.MESTRADO) };
            InfoStudentCurricularPlan infoStudentCurricularPlan = (InfoStudentCurricularPlan) ServiceManagerServiceFactory
                    .executeService(userView,
                            "student.ReadActiveStudentCurricularPlanByNumberAndDegreeType",
                            argsReadStudentCurricularPlan);

            Object[] argsReadMasterDegreeProofVersion = { infoStudentCurricularPlan };

            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(),
                    argsReadMasterDegreeProofVersion);

            fail("testReadWithScholarShipNotFinished did not throw ScholarShipNotFinishedServiceException");

        } catch (ScholarshipNotFinishedServiceException ex) {
            //ok

        } catch (Exception ex) {
            ex.printStackTrace();
            fail("testReadWithScholarShipNotFinished " + ex.getMessage());
        }

    }

    public void testReadNonExistentMasterDegreeProofVersion() {
        try {
            Object[] argsReadStudentCurricularPlan = { new Integer(5461),
                    new TipoCurso(TipoCurso.MESTRADO) };
            InfoStudentCurricularPlan infoStudentCurricularPlan = (InfoStudentCurricularPlan) ServiceManagerServiceFactory
                    .executeService(userView,
                            "student.ReadActiveStudentCurricularPlanByNumberAndDegreeType",
                            argsReadStudentCurricularPlan);

            Object[] argsReadMasterDegreeProofVersion = { infoStudentCurricularPlan };

            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(),
                    argsReadMasterDegreeProofVersion);

            fail("testReadNonExistentMasterDegreeProofVersion did not throw NonExistingServiceException");

        } catch (NonExistingServiceException ex) {
            //ok

        } catch (FenixServiceException e) {
            e.printStackTrace();
            fail("testReadNonExistentMasterDegreeProofVersion " + e.getMessage());
        }

    }

}