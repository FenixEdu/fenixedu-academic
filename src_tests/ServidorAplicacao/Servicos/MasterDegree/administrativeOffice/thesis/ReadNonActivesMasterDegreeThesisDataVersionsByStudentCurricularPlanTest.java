package ServidorAplicacao.Servicos.MasterDegree.administrativeOffice.thesis;

import java.util.List;

import framework.factory.ServiceManagerServiceFactory;

import DataBeans.InfoExternalPerson;
import DataBeans.InfoMasterDegreeThesisDataVersion;
import DataBeans.InfoStudentCurricularPlan;
import DataBeans.InfoTeacher;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.MasterDegree.administrativeOffice.AdministrativeOfficeBaseTest;
import Util.State;
import Util.TipoCurso;

/**
 * @author Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author Nadir Tarmahomed (naat@mega.ist.utl.pt)
 */
public class ReadNonActivesMasterDegreeThesisDataVersionsByStudentCurricularPlanTest extends
        AdministrativeOfficeBaseTest {

    /**
     * @param testName
     */
    public ReadNonActivesMasterDegreeThesisDataVersionsByStudentCurricularPlanTest(String testName) {
        super(testName);
        this.dataSetFilePath = "etc/datasets/servicos/MasterDegree/administrativeOffice/thesis/testReadNonActivesMasterDegreeThesisDataVersionsByStudentCurricularPlanDataSet.xml";
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadNonActivesMasterDegreeThesisDataVersionsByStudentCurricularPlan";
    }

    protected Object[] getServiceArgumentsForNotAuthenticatedUser() {
        Object[] argsReadMasterDegreeThesisDataVersion = { null };

        return argsReadMasterDegreeThesisDataVersion;
    }

    protected Object[] getServiceArgumentsForNotAuthorizedUser() throws FenixServiceException {
        Object[] argsReadStudentCurricularPlan = { new Integer(142), new TipoCurso(TipoCurso.MESTRADO) };
        InfoStudentCurricularPlan infoStudentCurricularPlan = (InfoStudentCurricularPlan) ServiceManagerServiceFactory
                .executeService(userView,
                        "student.ReadActiveStudentCurricularPlanByNumberAndDegreeType",
                        argsReadStudentCurricularPlan);

        Object[] argsReadMasterDegreeThesisDataVersion = { infoStudentCurricularPlan };

        return argsReadMasterDegreeThesisDataVersion;
    }

    public void testReadExistingNonActivesMasterDegreeThesisDataVersions() {
        try {
            Object[] argsReadStudentCurricularPlan = { new Integer(142),
                    new TipoCurso(TipoCurso.MESTRADO) };
            InfoStudentCurricularPlan infoStudentCurricularPlan = (InfoStudentCurricularPlan) ServiceManagerServiceFactory
                    .executeService(userView,
                            "student.ReadActiveStudentCurricularPlanByNumberAndDegreeType",
                            argsReadStudentCurricularPlan);

            Object[] argsReadMasterDegreeThesisDataVersion = { infoStudentCurricularPlan };
            List infoMasterDegreeThesisDataVersions = (List) ServiceManagerServiceFactory
                    .executeService(userView, getNameOfServiceToBeTested(),
                            argsReadMasterDegreeThesisDataVersion);

            assertNotNull(infoMasterDegreeThesisDataVersions);
            assertEquals(infoMasterDegreeThesisDataVersions.size(), 1);

            InfoMasterDegreeThesisDataVersion infoMasterDegreeThesisDataVersion = (InfoMasterDegreeThesisDataVersion) infoMasterDegreeThesisDataVersions
                    .get(0);

            assertEquals(infoMasterDegreeThesisDataVersion.getIdInternal(), new Integer(2));
            assertEquals(infoMasterDegreeThesisDataVersion.getInfoMasterDegreeThesis().getIdInternal(),
                    new Integer(1));
            assertEquals(infoMasterDegreeThesisDataVersion.getInfoResponsibleEmployee().getIdInternal(),
                    new Integer(1194));
            assertEquals(infoMasterDegreeThesisDataVersion.getDissertationTitle(), "Once upon a time");
            assertEquals(infoMasterDegreeThesisDataVersion.getCurrentState(), new State(State.INACTIVE));
            List infoGuiders = infoMasterDegreeThesisDataVersion.getInfoGuiders();
            List infoAssistentGuiders = infoMasterDegreeThesisDataVersion.getInfoAssistentGuiders();
            List infoExternalAssistentGuiders = infoMasterDegreeThesisDataVersion
                    .getInfoExternalAssistentGuiders();
            List infoExternalPersonExternalGuiders = infoMasterDegreeThesisDataVersion
                    .getInfoExternalGuiders();
            assertEquals(((InfoTeacher) infoGuiders.get(0)).getIdInternal(), new Integer(955));
            assertEquals(((InfoTeacher) infoAssistentGuiders.get(0)).getIdInternal(), new Integer(954));
            assertEquals(((InfoExternalPerson) infoExternalAssistentGuiders.get(0)).getIdInternal(),
                    new Integer(1));
            assertEquals(
                    ((InfoExternalPerson) infoExternalPersonExternalGuiders.get(0)).getIdInternal(),
                    new Integer(2));
            //ok

        } catch (Exception ex) {
            ex.printStackTrace();
            fail("testReadExistingNonActivesMasterDegreeThesisDataVersions " + ex.getMessage());
        }

    }

    public void testReadNonExistingNonActivesMasterDegreeThesisDataVersions() {
        try {
            Object[] argsReadStudentCurricularPlan = { new Integer(5461),
                    new TipoCurso(TipoCurso.MESTRADO) };
            InfoStudentCurricularPlan infoStudentCurricularPlan = (InfoStudentCurricularPlan) ServiceManagerServiceFactory
                    .executeService(userView,
                            "student.ReadActiveStudentCurricularPlanByNumberAndDegreeType",
                            argsReadStudentCurricularPlan);

            Object[] argsReadMasterDegreeThesisDataVersion = { infoStudentCurricularPlan };
            List infoMasterDegreeThesisDataVersions = (List) ServiceManagerServiceFactory
                    .executeService(userView, getNameOfServiceToBeTested(),
                            argsReadMasterDegreeThesisDataVersion);

            assertNotNull(infoMasterDegreeThesisDataVersions);
            assertEquals(infoMasterDegreeThesisDataVersions.isEmpty(), true);

        } catch (FenixServiceException e) {
            e.printStackTrace();
            fail("testReadNonExistingNonActivesMasterDegreeThesisDataVersions " + e.getMessage());
        }

    }

}