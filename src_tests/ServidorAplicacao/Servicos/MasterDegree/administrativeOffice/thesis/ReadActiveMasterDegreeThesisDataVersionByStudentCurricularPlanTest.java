package ServidorAplicacao.Servicos.MasterDegree.administrativeOffice.thesis;

import java.util.List;

import framework.factory.ServiceManagerServiceFactory;

import DataBeans.InfoExternalPerson;
import DataBeans.InfoMasterDegreeThesisDataVersion;
import DataBeans.InfoStudentCurricularPlan;
import DataBeans.InfoTeacher;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorAplicacao.Servicos.MasterDegree.administrativeOffice.AdministrativeOfficeBaseTest;
import Util.State;
import Util.TipoCurso;

/**
 * 
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 */
public class ReadActiveMasterDegreeThesisDataVersionByStudentCurricularPlanTest extends
        AdministrativeOfficeBaseTest {

    /**
     * @param testName
     */
    public ReadActiveMasterDegreeThesisDataVersionByStudentCurricularPlanTest(String testName) {
        super(testName);
        this.dataSetFilePath = "etc/datasets/servicos/MasterDegree/administrativeOffice/thesis/testReadActiveMasterDegreeThesisDataVersionByStudentCurricularPlanDataSet.xml";
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadActiveMasterDegreeThesisDataVersionByStudentCurricularPlan";
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

    public void testSuccessReadActiveMasterDegreeThesisDataVersion() {
        try {
            Object[] argsReadStudentCurricularPlan = { new Integer(142),
                    new TipoCurso(TipoCurso.MESTRADO) };
            InfoStudentCurricularPlan infoStudentCurricularPlan = (InfoStudentCurricularPlan) ServiceManagerServiceFactory
                    .executeService(userView,
                            "student.ReadActiveStudentCurricularPlanByNumberAndDegreeType",
                            argsReadStudentCurricularPlan);

            Object[] argsReadMasterDegreeThesisDataVersion = { infoStudentCurricularPlan };
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
            fail("testSuccessReadActiveMasterDegreeThesis " + ex.getMessage());
        }

    }

    public void testReadWhenMasterDegreeThesisDoesNotExist() {
        try {
            Object[] argsReadStudentCurricularPlan = { new Integer(209),
                    new TipoCurso(TipoCurso.MESTRADO) };
            InfoStudentCurricularPlan infoStudentCurricularPlan = (InfoStudentCurricularPlan) ServiceManagerServiceFactory
                    .executeService(userView,
                            "student.ReadActiveStudentCurricularPlanByNumberAndDegreeType",
                            argsReadStudentCurricularPlan);

            Object[] argsReadMasterDegreeThesisDataVersion = { infoStudentCurricularPlan };
            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(),
                    argsReadMasterDegreeThesisDataVersion);

            fail("testReadWhenMasterDegreeThesisDoesNotExist did not throw NonExistingServiceException");

        } catch (NonExistingServiceException ex) {
            //ok

        } catch (FenixServiceException e) {
            e.printStackTrace();
            fail("testReadNonExistingActiveMasterDegreeThesisDataVersion " + e.getMessage());
        }

    }

}