package ServidorAplicacao.Servicos.coordinator;

import DataBeans.InfoDegreeCurricularPlan;

import Dominio.DegreeCurricularPlan;
import Dominio.IDegreeCurricularPlan;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.ServiceTestCase;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Tânia Pousão Create on 7/Nov/2003
 */
public class EditDescriptionDegreeCurricularPlanTest extends ServiceTestCase
{
    public EditDescriptionDegreeCurricularPlanTest(String testName)
    {
        super(testName);
    }

    protected String getApplication()
    {
        return Autenticacao.EXTRANET;
    }

    protected String getNameOfServiceToBeTested()
    {
        return "EditDescriptionDegreeCurricularPlan";
    }

    protected String getDataSetFilePath()
    {
        return "etc/datasets_templates/servicos/coordinator/testDataSetDegreeSite.xml";
    }

    protected String[] getAuthenticatedAndAuthorizedUser()
    {
        String[] args = { "userC", "pass", getApplication()};
        return args;
    }

    protected String[] getSecondAuthenticatedAndAuthorizedUser()
    {
        String[] args = { "userC2", "pass", getApplication()};
        return args;
    }

    protected String[] getThridAuthenticatedAndAuthorizedUser()
    {
        String[] args = { "userC3", "pass", getApplication()};
        return args;
    }

    protected String[] getAuthenticatedAndAlreadyAuthorizedUser()
    {
        String[] args = { "userT", "pass", getApplication()};
        return args;
    }

    protected String[] getAuthenticatedAndUnauthorizedUser()
    {
        String[] args = { "userE", "pass", getApplication()};
        return args;
    }

    protected String[] getNotAuthenticatedUser()
    {
        String[] args = { "user", "pass", getApplication()};
        return args;
    }

    public InfoDegreeCurricularPlan getDescriptionCurricularPlanForm(Integer infoDegreeCurricularPlanId)
    {
        InfoDegreeCurricularPlan infoDegreeCurricularPlan = new InfoDegreeCurricularPlan();
        infoDegreeCurricularPlan.setIdInternal(infoDegreeCurricularPlanId);

        infoDegreeCurricularPlan.setDescription("Descrição");
        infoDegreeCurricularPlan.setDescriptionEn("Description");

        return infoDegreeCurricularPlan;
    }

    public void testSuccessfull()
    {
        try
        {
            //Service Argument
            Integer infoExecutionDegreeCode = new Integer(10);
            Integer infoDegreeCurricularPlanCode = new Integer(10);
                        
            InfoDegreeCurricularPlan infoDegreeCurricularPlan =
                getDescriptionCurricularPlanForm(infoDegreeCurricularPlanCode);

            Object[] args = { infoExecutionDegreeCode, infoDegreeCurricularPlan };

            //Valid user
            String[] argsUser = getAuthenticatedAndAuthorizedUser();
            IUserView id = (IUserView) gestor.executar(null, "Autenticacao", argsUser);

            //Service
            try
            {
                gestor.executar(id, getNameOfServiceToBeTested(), args);
            } catch (FenixServiceException e)
            {
                e.printStackTrace();
                fail("Reading a degree information" + e);
            }

            //Read the change in degree curricular plan
            SuportePersistenteOJB sp = SuportePersistenteOJB.getInstance();
            IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = sp.getIPersistentDegreeCurricularPlan();
            
            IDegreeCurricularPlan degreeCurricularPlanAck = new DegreeCurricularPlan();
			degreeCurricularPlanAck.setIdInternal(infoDegreeCurricularPlanCode);
            
            sp.iniciarTransaccao();
			degreeCurricularPlanAck = (IDegreeCurricularPlan) persistentDegreeCurricularPlan.readByOId(degreeCurricularPlanAck, false);
            sp.confirmarTransaccao();

            assertEquals(degreeCurricularPlanAck.getDescription(), infoDegreeCurricularPlan.getDescription());
            assertEquals(degreeCurricularPlanAck.getDescriptionEn(), infoDegreeCurricularPlan.getDescriptionEn());
                        
            assertNotNull(degreeCurricularPlanAck.getDegree());
            assertEquals(new Integer(10), degreeCurricularPlanAck.getDegree().getIdInternal());

            System.out.println(
                "EditDescriptionDegreeCurricularPlanTest was SUCCESSFULY runned by service: testSuccessfull");

        } catch (FenixServiceException e)
        {
            e.printStackTrace();
            fail("Reading a degree information" + e);
        } catch (Exception e)
        {
            e.printStackTrace();
            fail("Reading a degree information" + e);
        }
    }
}
