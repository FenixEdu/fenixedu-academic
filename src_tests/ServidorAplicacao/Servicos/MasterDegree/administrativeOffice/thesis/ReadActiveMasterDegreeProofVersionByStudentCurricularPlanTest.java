package ServidorAplicacao.Servicos.MasterDegree.administrativeOffice.thesis;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import DataBeans.InfoExternalPerson;
import DataBeans.InfoMasterDegreeProofVersion;
import DataBeans.InfoStudentCurricularPlan;
import DataBeans.InfoTeacher;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorAplicacao.Servico.exceptions.ScholarshipNotFinishedServiceException;
import ServidorAplicacao.Servicos.MasterDegree.administrativeOffice.AdministrativeOfficeBaseTest;
import Util.MasterDegreeClassification;
import Util.State;
import Util.TipoCurso;

/**
 * @author Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author Nadir Tarmahomed (naat@mega.ist.utl.pt)
 */
public class ReadActiveMasterDegreeProofVersionByStudentCurricularPlanTest
    extends AdministrativeOfficeBaseTest
{

    /**
	 * @param testName
	 */
    public ReadActiveMasterDegreeProofVersionByStudentCurricularPlanTest(String testName)
    {
        super(testName);
        this.dataSetFilePath =
            "etc/datasets/servicos/MasterDegree/administrativeOffice/thesis/testReadActiveMasterDegreeProofVersionByStudentCurricularPlanDataSet.xml";
    }

    protected String getNameOfServiceToBeTested()
    {
        return "ReadActiveMasterDegreeProofVersionByStudentCurricularPlan";
    }

    protected Object[] getServiceArgumentsForNotAuthenticatedUser() throws FenixServiceException
    {
        Object[] argsReadMasterDegreeProofVersion = { null };

        return argsReadMasterDegreeProofVersion;
    }

    protected Object[] getServiceArgumentsForNotAuthorizedUser() throws FenixServiceException
    {
        Object[] argsReadStudentCurricularPlan = { new Integer(142), new TipoCurso(TipoCurso.MESTRADO)};
        InfoStudentCurricularPlan infoStudentCurricularPlan =
            (InfoStudentCurricularPlan) serviceManager.executar(
                userViewNotAuthorized,
                "student.ReadActiveStudentCurricularPlanByNumberAndDegreeType",
                argsReadStudentCurricularPlan);

        Object[] argsReadMasterDegreeProofVersion = { infoStudentCurricularPlan };

        return argsReadMasterDegreeProofVersion;
    }

    public void testSuccessReadActiveMasterDegreeProofVersion()
    {
        try
        {
            Object[] argsReadStudentCurricularPlan =
                { new Integer(142), new TipoCurso(TipoCurso.MESTRADO)};
            InfoStudentCurricularPlan infoStudentCurricularPlan =
                (InfoStudentCurricularPlan) serviceManager.executar(
                    userView,
                    "student.ReadActiveStudentCurricularPlanByNumberAndDegreeType",
                    argsReadStudentCurricularPlan);

            Object[] argsReadMasterDegreeProofVersion = { infoStudentCurricularPlan };
            InfoMasterDegreeProofVersion infoMasterDegreeProofVersion =
                (InfoMasterDegreeProofVersion) serviceManager.executar(
                    userView,
                    getNameOfServiceToBeTested(),
                    argsReadMasterDegreeProofVersion);

            assertEquals(infoMasterDegreeProofVersion.getIdInternal(), new Integer(1));
            assertEquals(
                infoMasterDegreeProofVersion.getInfoMasterDegreeThesis().getIdInternal(),
                new Integer(1));
            assertEquals(
                infoMasterDegreeProofVersion.getInfoResponsibleEmployee().getIdInternal(),
                new Integer(1194));
            assertEquals(infoMasterDegreeProofVersion.getCurrentState(), new State(State.ACTIVE));
            assertEquals(infoMasterDegreeProofVersion.getAttachedCopiesNumber(), new Integer(5));
            assertEquals(
                infoMasterDegreeProofVersion.getFinalResult(),
                MasterDegreeClassification.APPROVED);
            Date proofDate = new GregorianCalendar(2003, Calendar.OCTOBER, 10).getTime();
            assertEquals(infoMasterDegreeProofVersion.getProofDate(), proofDate);
            Date thesisDeliveryDate = new GregorianCalendar(2003, Calendar.NOVEMBER, 11).getTime();
            assertEquals(infoMasterDegreeProofVersion.getThesisDeliveryDate(), thesisDeliveryDate);
            assertEquals(
                ((InfoTeacher) infoMasterDegreeProofVersion.getInfoJuries().get(0)).getIdInternal(),
                new Integer(954));
            assertEquals(
                ((InfoExternalPerson) infoMasterDegreeProofVersion.getInfoExternalJuries().get(0)).getIdInternal(),
                new Integer(1));

            //ok

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("testSuccessReadActiveMasterDegreeProofVersion " + ex.getMessage());
        }

    }

    public void testReadWithoutScholarShipFinished()
    {
        try
        {
            Object[] argsReadStudentCurricularPlan =
                { new Integer(209), new TipoCurso(TipoCurso.MESTRADO)};
            InfoStudentCurricularPlan infoStudentCurricularPlan =
                (InfoStudentCurricularPlan) serviceManager.executar(
                    userView,
                    "student.ReadActiveStudentCurricularPlanByNumberAndDegreeType",
                    argsReadStudentCurricularPlan);

            Object[] argsReadMasterDegreeProofVersion = { infoStudentCurricularPlan };

            serviceManager.executar(
                userView,
                getNameOfServiceToBeTested(),
                argsReadMasterDegreeProofVersion);

            fail("testReadWithScholarShipNotFinished did not throw ScholarShipNotFinishedServiceException");

        }
        catch (ScholarshipNotFinishedServiceException ex)
        {
            //ok

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("testReadWithScholarShipNotFinished " + ex.getMessage());
        }

    }

    public void testReadNonExistentMasterDegreeProofVersion()
    {
        try
        {
            Object[] argsReadStudentCurricularPlan =
                { new Integer(5461), new TipoCurso(TipoCurso.MESTRADO)};
            InfoStudentCurricularPlan infoStudentCurricularPlan =
                (InfoStudentCurricularPlan) serviceManager.executar(
                    userView,
                    "student.ReadActiveStudentCurricularPlanByNumberAndDegreeType",
                    argsReadStudentCurricularPlan);

            Object[] argsReadMasterDegreeProofVersion = { infoStudentCurricularPlan };

            serviceManager.executar(
                userView,
                getNameOfServiceToBeTested(),
                argsReadMasterDegreeProofVersion);

            fail("testReadNonExistentMasterDegreeProofVersion did not throw NonExistingServiceException");

        }
        catch (NonExistingServiceException ex)
        {
            //ok

        }
        catch (FenixServiceException e)
        {
            e.printStackTrace();
            fail("testReadNonExistentMasterDegreeProofVersion " + e.getMessage());
        }

    }

}
