package ServidorAplicacao.Servicos.MasterDegree.administrativeOffice.guide;

/**
 * @author Nuno Nunes & Joana Mota
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import DataBeans.InfoGuide;
import DataBeans.InfoGuideEntry;
import DataBeans.InfoRole;
import DataBeans.util.Cloner;
import Dominio.IContributor;
import Dominio.ICursoExecucao;
import Dominio.IExecutionYear;
import Dominio.IPessoa;
import Dominio.IPrice;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.TestCaseServicos;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.DocumentType;
import Util.GraduationType;
import Util.GuideRequester;
import Util.PaymentType;
import Util.RoleType;
import Util.SituationOfGuide;
import Util.Specialization;
import framework.factory.ServiceManagerServiceFactory;

public class CreateGuideTest extends TestCaseServicos
{

    public CreateGuideTest(java.lang.String testName)
    {
        super(testName);
    }

    public static void main(java.lang.String[] args)
    {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite()
    {
        TestSuite suite = new TestSuite(CreateGuideTest.class);

        return suite;
    }

    protected void setUp()
    {
        super.setUp();

    }

    protected void tearDown()
    {
        super.tearDown();
    }
    public void testCreateGuide()
    {
        System.out.println("- Test 1 : Create Guide");

        UserView userView = this.getUserViewToBeTested("nmsn", true);

        SuportePersistenteOJB sp = null;
        IPessoa person = null;
        IContributor contributor = null;
        IPrice price = null;
        ICursoExecucao executionDegree = null;
        IExecutionYear executionYear = null;

        try
        {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();
            person = sp.getIPessoaPersistente().lerPessoaPorUsername("nmsn");
            assertNotNull(person);

            contributor = sp.getIPersistentContributor().readByContributorNumber(new Integer(123));
            assertNotNull(contributor);

            price =
                sp.getIPersistentPrice().readByGraduationTypeAndDocumentTypeAndDescription(
                    GraduationType.MASTER_DEGREE_TYPE,
                    DocumentType.APPLICATION_EMOLUMENT_TYPE,
                    Specialization.MESTRADO_STRING);
            assertNotNull(price);

            executionYear = sp.getIPersistentExecutionYear().readExecutionYearByName("2002/2003");
            assertNotNull(executionYear);
            executionDegree =
                sp
                    .getICursoExecucaoPersistente()
                    .readByDegreeInitialsAndNameDegreeCurricularPlanAndExecutionYear(
                    "MEEC",
                    "plano2",
                    executionYear);

            assertNotNull(executionDegree);

            sp.confirmarTransaccao();
        }
        catch (Exception e)
        {
            fail(e.toString());
        }

        InfoGuide infoGuide = new InfoGuide();
        infoGuide.setYear(new Integer(2003));
        infoGuide.setVersion(new Integer(1));

        infoGuide.setInfoContributor(Cloner.copyIContributor2InfoContributor(contributor));
        infoGuide.setInfoPerson(Cloner.copyIPerson2InfoPerson(person));

        infoGuide.setTotal(price.getPrice());

        infoGuide.setCreationDate(Calendar.getInstance().getTime());

        infoGuide.setInfoExecutionDegree(
            Cloner.copyIExecutionDegree2InfoExecutionDegree(executionDegree));

        InfoGuideEntry infoGuideEntry = new InfoGuideEntry();
        infoGuideEntry.setDescription(price.getDescription());
        infoGuideEntry.setDocumentType(price.getDocumentType());
        infoGuideEntry.setGraduationType(price.getGraduationType());
        infoGuideEntry.setInfoGuide(infoGuide);
        infoGuideEntry.setPrice(price.getPrice());
        infoGuideEntry.setQuantity(new Integer(1));

        List infoGuideEntries = new ArrayList();
        infoGuideEntries.add(infoGuideEntry);

        infoGuide.setInfoGuideEntries(infoGuideEntries);
        infoGuide.setGuideRequester(GuideRequester.CANDIDATE_TYPE);

        Object[] args =
            {
                infoGuide,
                "Selos",
                new Double(10),
                "observacoes",
                SituationOfGuide.PAYED_TYPE,
                PaymentType.ATM_STRING };

        InfoGuide result = null;
        try
        {
            result =
                (InfoGuide) ServiceManagerServiceFactory.executeService(userView, "CreateGuide", args);
        }
        catch (FenixServiceException ex)
        {
            fail("Fenix Service Exception" + ex);
        }
        catch (Exception ex)
        {
            fail("Eception");
        }

        assertNotNull(result);
        assertEquals(result.getVersion(), new Integer(1));
        assertEquals(result.getNumber(), new Integer(4));
        assertEquals(result.getYear(), new Integer(2003));
        assertEquals(result.getInfoGuideEntries().size(), 2);
        assertEquals(result.getTotal(), new Double(price.getPrice().intValue() + 10));

    }

    private UserView getUserViewToBeTested(String username, boolean withRole)
    {
        Collection roles = new ArrayList();
        InfoRole infoRole = new InfoRole();
        if (withRole)
            infoRole.setRoleType(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE);
        else
            infoRole.setRoleType(RoleType.PERSON);
        roles.add(infoRole);
        UserView userView = new UserView(username, roles);
        return userView;
    }

}