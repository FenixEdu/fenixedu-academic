package ServidorAplicacao.Servicos.MasterDegree.administrativeOffice.thesis;

import java.util.ArrayList;
import java.util.List;

import framework.factory.ServiceManagerServiceFactory;

import DataBeans.InfoExternalPerson;
import DataBeans.InfoMasterDegreeThesisDataVersion;
import DataBeans.InfoStudentCurricularPlan;
import DataBeans.InfoTeacher;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.GuiderAlreadyChosenServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorAplicacao.Servicos.MasterDegree.administrativeOffice.AdministrativeOfficeBaseTest;
import Util.TipoCurso;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author - Nadir Tarmahomed (naat@mega.ist.utl.pt)
 */
public class ChangeMasterDegreeThesisDataTest extends AdministrativeOfficeBaseTest
{

    /**
	 * @param testName
	 */
    public ChangeMasterDegreeThesisDataTest(String testName)
    {
        super(testName);
        this.dataSetFilePath =
            "etc/datasets/servicos/MasterDegree/administrativeOffice/thesis/testChangeMasterDegreeThesisDataSet.xml";
    }

    protected String getNameOfServiceToBeTested()
    {
        return "ChangeMasterDegreeThesisData";
    }

    protected Object[] getServiceArgumentsForNotAuthenticatedUser()
    {
        InfoStudentCurricularPlan infoStudentCurricularPlan = new InfoStudentCurricularPlan();
        infoStudentCurricularPlan.setIdInternal(new Integer(8582));

        InfoTeacher infoTeacherGuider = new InfoTeacher();
        infoTeacherGuider.setIdInternal(new Integer(956));
        InfoTeacher infoTeacherAssistent = new InfoTeacher();
        infoTeacherAssistent.setIdInternal(new Integer(957));

        List guiders = new ArrayList();
        List assistentGuiders = new ArrayList();
        List externalAssistentGuiders = new ArrayList();
        List infoExternalPersonExternalGuiders = new ArrayList();

        guiders.add(infoTeacherGuider);
        assistentGuiders.add(infoTeacherAssistent);

        Object[] argsChangeMasterDegreeThesis =
            {
                null,
                infoStudentCurricularPlan,
                "some title",
                guiders,
                assistentGuiders,
                infoExternalPersonExternalGuiders,
                externalAssistentGuiders };

        return argsChangeMasterDegreeThesis;

    }

    protected Object[] getServiceArgumentsForNotAuthorizedUser() throws FenixServiceException
    {
        Object[] argsReadStudentCurricularPlan = { new Integer(142), new TipoCurso(TipoCurso.MESTRADO)};
        InfoStudentCurricularPlan infoStudentCurricularPlan =
            (InfoStudentCurricularPlan) ServiceManagerServiceFactory.executeService(
                userViewNotAuthorized,
                "student.ReadActiveStudentCurricularPlanByNumberAndDegreeType",
                argsReadStudentCurricularPlan);

        InfoTeacher infoTeacherGuider = new InfoTeacher();
        infoTeacherGuider.setIdInternal(new Integer(956));
        InfoTeacher infoTeacherAssistent = new InfoTeacher();
        infoTeacherAssistent.setIdInternal(new Integer(957));

        List guiders = new ArrayList();
        List assistentGuiders = new ArrayList();
        List externalAssistentGuiders = new ArrayList();
        List infoExternalPersonExternalGuiders = new ArrayList();

        guiders.add(infoTeacherGuider);
        assistentGuiders.add(infoTeacherAssistent);

        Object[] argsChangeMasterDegreeThesis =
            {
                userViewNotAuthorized,
                infoStudentCurricularPlan,
                "some title",
                guiders,
                assistentGuiders,
                infoExternalPersonExternalGuiders,
                externalAssistentGuiders };

        return argsChangeMasterDegreeThesis;
    }

    public void testSuccessfulChangeMasterDegreeThesisData()
    {
        try
        {
            Object[] argsReadStudentCurricularPlan =
                { new Integer(142), new TipoCurso(TipoCurso.MESTRADO)};
            InfoStudentCurricularPlan infoStudentCurricularPlan =
                (InfoStudentCurricularPlan) ServiceManagerServiceFactory.executeService(
                    userView,
                    "student.ReadActiveStudentCurricularPlanByNumberAndDegreeType",
                    argsReadStudentCurricularPlan);

            Object[] argsReadMasterDegreeThesis = { infoStudentCurricularPlan };
            InfoMasterDegreeThesisDataVersion infoMasterDegreeThesisDataVersion =
                (InfoMasterDegreeThesisDataVersion) ServiceManagerServiceFactory.executeService(
                    userView,
                    "ReadActiveMasterDegreeThesisDataVersionByStudentCurricularPlan",
                    argsReadMasterDegreeThesis);

            InfoTeacher infoTeacherGuider = new InfoTeacher();
            infoTeacherGuider.setIdInternal(new Integer(956));
            InfoTeacher infoTeacherAssistent = new InfoTeacher();
            infoTeacherAssistent.setIdInternal(new Integer(957));
            InfoExternalPerson infoExternalPersonExternalGuider = new InfoExternalPerson();
            infoExternalPersonExternalGuider.setIdInternal(new Integer(2));

            List guiders = infoMasterDegreeThesisDataVersion.getInfoGuiders();
            List assistentGuiders = infoMasterDegreeThesisDataVersion.getInfoAssistentGuiders();
            List externalAssistentGuiders =
                infoMasterDegreeThesisDataVersion.getInfoExternalAssistentGuiders();
            List infoExternalPersonExternalGuiders = new ArrayList();

            guiders.add(infoTeacherGuider);
            assistentGuiders.add(infoTeacherAssistent);
            infoExternalPersonExternalGuiders.add(infoExternalPersonExternalGuider);

            Object[] argsChangeMasterDegreeThesis =
                {
                    userView,
                    infoStudentCurricularPlan,
                    "some title",
                    guiders,
                    assistentGuiders,
                    infoExternalPersonExternalGuiders,
                    externalAssistentGuiders };

            ServiceManagerServiceFactory.executeService(
                this.userView,
                getNameOfServiceToBeTested(),
                argsChangeMasterDegreeThesis);
            compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets/servicos/MasterDegree/administrativeOffice/thesis/testExpectedChangeMasterDegreeThesisDataSet.xml");
            //ok

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("testSuccessfulChangeMasterDegreeThesis " + ex.getMessage());
        }
    }

    public void testChangeMasterDegreeThesisDataUsingExistingDissertationTitle()
    {
        try
        {
            Object[] argsReadStudentCurricularPlan =
                { new Integer(142), new TipoCurso(TipoCurso.MESTRADO)};
            InfoStudentCurricularPlan infoStudentCurricularPlan =
                (InfoStudentCurricularPlan) ServiceManagerServiceFactory.executeService(
                    userView,
                    "student.ReadActiveStudentCurricularPlanByNumberAndDegreeType",
                    argsReadStudentCurricularPlan);

            Object[] argsReadMasterDegreeThesis = { infoStudentCurricularPlan };
            InfoMasterDegreeThesisDataVersion infoMasterDegreeThesisDataVersion =
                (InfoMasterDegreeThesisDataVersion) ServiceManagerServiceFactory.executeService(
                    userView,
                    "ReadActiveMasterDegreeThesisDataVersionByStudentCurricularPlan",
                    argsReadMasterDegreeThesis);

            InfoTeacher infoTeacherGuider = new InfoTeacher();
            infoTeacherGuider.setIdInternal(new Integer(956));
            InfoTeacher infoTeacherAssistent = new InfoTeacher();
            infoTeacherAssistent.setIdInternal(new Integer(957));

            List guiders = infoMasterDegreeThesisDataVersion.getInfoGuiders();
            List assistentGuiders = infoMasterDegreeThesisDataVersion.getInfoAssistentGuiders();
            List externalAssistentGuiders =
                infoMasterDegreeThesisDataVersion.getInfoExternalAssistentGuiders();
            List infoExternalPersonExternalGuiders =
                infoMasterDegreeThesisDataVersion.getInfoExternalGuiders();

            guiders.add(infoTeacherGuider);
            assistentGuiders.add(infoTeacherAssistent);

            Object[] argsChangeMasterDegreeThesis =
                {
                    userView,
                    infoStudentCurricularPlan,
                    "Existing Title",
                    guiders,
                    assistentGuiders,
                    infoExternalPersonExternalGuiders,
                    externalAssistentGuiders };

            ServiceManagerServiceFactory.executeService(
                this.userView,
                getNameOfServiceToBeTested(),
                argsChangeMasterDegreeThesis);

            fail("testChangeMasterDegreeThesisDataUsingExistingDissertationTitle did not throw ExistingServiceException");

        }
        catch (ExistingServiceException ex)
        {
            //test passed
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("testChangeMasterDegreeThesisUsingExistingDissertationTitle" + ex.getMessage());
        }
    }

    public void testChangeWhenMasterDegreeThesisDoesNotExist()
    {
        try
        {
            Object[] argsReadStudentCurricularPlan =
                { new Integer(180), new TipoCurso(TipoCurso.MESTRADO)};
            InfoStudentCurricularPlan infoStudentCurricularPlan =
                (InfoStudentCurricularPlan) ServiceManagerServiceFactory.executeService(
                    userView,
                    "student.ReadActiveStudentCurricularPlanByNumberAndDegreeType",
                    argsReadStudentCurricularPlan);

            InfoTeacher infoTeacherGuider = new InfoTeacher();
            infoTeacherGuider.setIdInternal(new Integer(956));
            InfoTeacher infoTeacherAssistent = new InfoTeacher();
            infoTeacherAssistent.setIdInternal(new Integer(957));
            InfoExternalPerson infoExternalPersonExternalGuider = new InfoExternalPerson();
            infoExternalPersonExternalGuider.setIdInternal(new Integer(2));

            List guiders = new ArrayList();
            List assistentGuiders = new ArrayList();
            List externalAssistentGuiders = new ArrayList();
            List infoExternalPersonExternalGuiders = new ArrayList();

            guiders.add(infoTeacherGuider);
            assistentGuiders.add(infoTeacherAssistent);
            infoExternalPersonExternalGuiders.add(infoExternalPersonExternalGuider);

            Object[] argsChangeMasterDegreeThesis =
                {
                    userView,
                    infoStudentCurricularPlan,
                    "some title",
                    guiders,
                    assistentGuiders,
                    infoExternalPersonExternalGuiders,
                    externalAssistentGuiders };

            ServiceManagerServiceFactory.executeService(
                this.userView,
                getNameOfServiceToBeTested(),
                argsChangeMasterDegreeThesis);

            fail("testChangeWhenMasterDegreeThesisDoesNotExist did not throw NonExistingServiceException");

        }
        catch (NonExistingServiceException ex)
        {
            //ok

        }
        catch (FenixServiceException e)
        {
            e.printStackTrace();
            fail("testChangeWhenMasterDegreeThesisDoesNotExist " + e.getMessage());
        }

    }

    /*
	 * public void testChangeMasterDegreeThesisDataWithoutGuiders() { try { Object[]
	 * argsReadStudentCurricularPlan = { new Integer(142), new TipoCurso(TipoCurso.MESTRADO)};
	 * InfoStudentCurricularPlan infoStudentCurricularPlan = (InfoStudentCurricularPlan)
	 * ServiceManagerServiceFactory.executeService( userView,
	 * "student.ReadActiveStudentCurricularPlanByNumberAndDegreeType", argsReadStudentCurricularPlan);
	 * 
	 * Object[] argsReadMasterDegreeThesis = { infoStudentCurricularPlan };
	 * InfoMasterDegreeThesisDataVersion infoMasterDegreeThesisDataVersion =
	 * (InfoMasterDegreeThesisDataVersion) ServiceManagerServiceFactory.executeService( userView,
	 * "ReadActiveMasterDegreeThesisDataVersionByStudentCurricularPlan", argsReadMasterDegreeThesis);
	 * 
	 * InfoTeacher infoTeacherAssistent = new InfoTeacher(); infoTeacherAssistent.setIdInternal(new
	 * Integer(957));
	 * 
	 * List guiders = new ArrayList(); List assistentGuiders =
	 * infoMasterDegreeThesisDataVersion.getInfoAssistentGuiders(); List externalAssistentGuiders =
	 * infoMasterDegreeThesisDataVersion.getInfoExternalAssistentGuiders(); List
	 * infoExternalPersonExternalGuiders = infoMasterDegreeThesisDataVersion.getInfoExternalGuiders();
	 * 
	 * assistentGuiders.add(infoTeacherAssistent);
	 * 
	 * Object[] argsChangeMasterDegreeThesis = { userView, infoStudentCurricularPlan, "some title",
	 * guiders, assistentGuiders, infoExternalPersonExternalGuiders, externalAssistentGuiders };
	 * 
	 * ServiceManagerServiceFactory.executeService( this.userView, getNameOfServiceToBeTested(),
	 * argsChangeMasterDegreeThesis);
	 * 
	 * fail("testChangeMasterDegreeThesisDataWithoutGuiders did not throw
	 * RequiredGuidersServiceException"); } catch (RequiredGuidersServiceException e) { //ok } catch
	 * (Exception ex) { ex.printStackTrace(); fail("testChangeMasterDegreeThesisDataWithoutGuiders " +
	 * ex.getMessage()); }
	 */

    public void testChangeMasterDegreeThesisWithTeacherBeingGuiderAndAssistentGuider()
    {
        try
        {
            Object[] argsReadStudentCurricularPlan =
                { new Integer(142), new TipoCurso(TipoCurso.MESTRADO)};
            InfoStudentCurricularPlan infoStudentCurricularPlan =
                (InfoStudentCurricularPlan) ServiceManagerServiceFactory.executeService(
                    userView,
                    "student.ReadActiveStudentCurricularPlanByNumberAndDegreeType",
                    argsReadStudentCurricularPlan);

            Object[] argsReadMasterDegreeThesis = { infoStudentCurricularPlan };
            InfoMasterDegreeThesisDataVersion infoMasterDegreeThesisDataVersion =
                (InfoMasterDegreeThesisDataVersion) ServiceManagerServiceFactory.executeService(
                    userView,
                    "ReadActiveMasterDegreeThesisDataVersionByStudentCurricularPlan",
                    argsReadMasterDegreeThesis);

            InfoTeacher infoTeacherGuider = new InfoTeacher();
            infoTeacherGuider.setIdInternal(new Integer(956));
            InfoTeacher infoTeacherAssistent = new InfoTeacher();
            infoTeacherAssistent.setIdInternal(new Integer(957));

            List guiders = infoMasterDegreeThesisDataVersion.getInfoGuiders();
            List assistentGuiders = infoMasterDegreeThesisDataVersion.getInfoAssistentGuiders();
            List externalAssistentGuiders =
                infoMasterDegreeThesisDataVersion.getInfoExternalAssistentGuiders();
            List infoExternalPersonExternalGuiders =
                infoMasterDegreeThesisDataVersion.getInfoExternalGuiders();

            guiders.add(infoTeacherGuider);
            assistentGuiders.add(infoTeacherAssistent);
            assistentGuiders.add(infoTeacherGuider);

            Object[] argsChangeMasterDegreeThesis =
                {
                    userView,
                    infoStudentCurricularPlan,
                    "some title",
                    guiders,
                    assistentGuiders,
                    infoExternalPersonExternalGuiders,
                    externalAssistentGuiders };

            ServiceManagerServiceFactory.executeService(
                this.userView,
                getNameOfServiceToBeTested(),
                argsChangeMasterDegreeThesis);

            fail("testChangeMasterDegreeThesisWithTeacherBeingGuiderAndAssistentGuider did not throw GuiderAlreadyChosenServiceException");

        }
        catch (GuiderAlreadyChosenServiceException e)
        {
            //ok

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail(
                "testChangeMasterDegreeThesisWithTeacherBeingGuiderAndAssistentGuider"
                    + ex.getMessage());
        }
    }

    public void testChangeWithExternalPersonBeingExternalGuiderAndExternalAssistentGuider()
    {
        try
        {
            Object[] argsReadStudentCurricularPlan =
                { new Integer(142), new TipoCurso(TipoCurso.MESTRADO)};
            InfoStudentCurricularPlan infoStudentCurricularPlan =
                (InfoStudentCurricularPlan) ServiceManagerServiceFactory.executeService(
                    userView,
                    "student.ReadActiveStudentCurricularPlanByNumberAndDegreeType",
                    argsReadStudentCurricularPlan);

            Object[] argsReadMasterDegreeThesis = { infoStudentCurricularPlan };
            InfoMasterDegreeThesisDataVersion infoMasterDegreeThesisDataVersion =
                (InfoMasterDegreeThesisDataVersion) ServiceManagerServiceFactory.executeService(
                    userView,
                    "ReadActiveMasterDegreeThesisDataVersionByStudentCurricularPlan",
                    argsReadMasterDegreeThesis);

            InfoTeacher infoTeacherGuider = new InfoTeacher();
            infoTeacherGuider.setIdInternal(new Integer(956));
            InfoTeacher infoTeacherAssistent = new InfoTeacher();
            infoTeacherAssistent.setIdInternal(new Integer(957));
            InfoExternalPerson infoExternalPerson = new InfoExternalPerson();
            infoExternalPerson.setIdInternal(new Integer(2));

            List guiders = infoMasterDegreeThesisDataVersion.getInfoGuiders();
            List assistentGuiders = infoMasterDegreeThesisDataVersion.getInfoAssistentGuiders();
            List externalAssistentGuiders =
                infoMasterDegreeThesisDataVersion.getInfoExternalAssistentGuiders();
            List infoExternalPersonExternalGuiders =
                infoMasterDegreeThesisDataVersion.getInfoExternalGuiders();

            infoExternalPersonExternalGuiders.add(infoExternalPerson);
            externalAssistentGuiders.add(infoExternalPerson);

            Object[] argsChangeMasterDegreeThesis =
                {
                    userView,
                    infoStudentCurricularPlan,
                    "some title",
                    guiders,
                    assistentGuiders,
                    infoExternalPersonExternalGuiders,
                    externalAssistentGuiders };

            ServiceManagerServiceFactory.executeService(
                this.userView,
                getNameOfServiceToBeTested(),
                argsChangeMasterDegreeThesis);

            fail("testChangeWithExternalPersonBeingExternalGuiderAndExternalAssistentGuider did not throw  GuiderAlreadyChosenServiceException");
        }
        catch (GuiderAlreadyChosenServiceException e)
        { //ok
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail(
                "testChangeWithExternalPersonBeingExternalGuiderAndExternalAssistentGuider"
                    + ex.getMessage());
        }
    }
}
