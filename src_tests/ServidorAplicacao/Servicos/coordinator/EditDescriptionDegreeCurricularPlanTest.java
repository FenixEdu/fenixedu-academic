package net.sourceforge.fenixedu.applicationTier.Servicos.coordinator;

import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.Autenticacao;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servicos.ServiceTestCase;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDegreeCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

/**
 * @author Tânia Pousão Create on 7/Nov/2003
 */
public class EditDescriptionDegreeCurricularPlanTest extends ServiceTestCase {
    public EditDescriptionDegreeCurricularPlanTest(String testName) {
        super(testName);
    }

    protected String getApplication() {
        return Autenticacao.EXTRANET;
    }

    protected String getNameOfServiceToBeTested() {
        return "EditDescriptionDegreeCurricularPlan";
    }

    protected String getDataSetFilePath() {
        return "etc/datasets_templates/servicos/coordinator/testDataSetDegreeSite.xml";
    }

    protected String[] getAuthenticatedAndAuthorizedUser() {
        String[] args = { "userC", "pass", getApplication() };
        return args;
    }

    protected String[] getSecondAuthenticatedAndAuthorizedUser() {
        String[] args = { "userC2", "pass", getApplication() };
        return args;
    }

    protected String[] getThridAuthenticatedAndAuthorizedUser() {
        String[] args = { "userC3", "pass", getApplication() };
        return args;
    }

    protected String[] getAuthenticatedAndAlreadyAuthorizedUser() {
        String[] args = { "userT", "pass", getApplication() };
        return args;
    }

    protected String[] getAuthenticatedAndUnauthorizedUser() {
        String[] args = { "userE", "pass", getApplication() };
        return args;
    }

    protected String[] getNotAuthenticatedUser() {
        String[] args = { "user", "pass", getApplication() };
        return args;
    }

    public InfoDegreeCurricularPlan getDescriptionCurricularPlanForm(Integer infoDegreeCurricularPlanId) {
        InfoDegreeCurricularPlan infoDegreeCurricularPlan = new InfoDegreeCurricularPlan();
        infoDegreeCurricularPlan.setIdInternal(infoDegreeCurricularPlanId);

        infoDegreeCurricularPlan.setDescription("Descrição");
        infoDegreeCurricularPlan.setDescriptionEn("Description");

        return infoDegreeCurricularPlan;
    }

    public void testSuccessfull() {
        try {
            //Service Argument
            Integer infoExecutionDegreeCode = new Integer(10);
            Integer infoDegreeCurricularPlanCode = new Integer(10);

            InfoDegreeCurricularPlan infoDegreeCurricularPlan = getDescriptionCurricularPlanForm(infoDegreeCurricularPlanCode);

            Object[] args = { infoExecutionDegreeCode, infoDegreeCurricularPlan };

            //Valid user
            String[] argsUser = getAuthenticatedAndAuthorizedUser();
            IUserView id = (IUserView) ServiceManagerServiceFactory.executeService(null, "Autenticacao",
                    argsUser);

            //Service
            try {
                ServiceManagerServiceFactory.executeService(id, getNameOfServiceToBeTested(), args);
            } catch (FenixServiceException e) {
                e.printStackTrace();
                fail("Reading a degree information" + e);
            }

            //Read the change in degree curricular plan
            SuportePersistenteOJB sp = SuportePersistenteOJB.getInstance();
            IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = sp
                    .getIPersistentDegreeCurricularPlan();

            IDegreeCurricularPlan degreeCurricularPlanAck;

            sp.iniciarTransaccao();
            degreeCurricularPlanAck = (IDegreeCurricularPlan) persistentDegreeCurricularPlan.readByOID(
                    DegreeCurricularPlan.class, infoDegreeCurricularPlanCode);
            sp.confirmarTransaccao();

            assertEquals(degreeCurricularPlanAck.getDescription(), infoDegreeCurricularPlan
                    .getDescription());
            assertEquals(degreeCurricularPlanAck.getDescriptionEn(), infoDegreeCurricularPlan
                    .getDescriptionEn());

            assertNotNull(degreeCurricularPlanAck.getDegree());
            assertEquals(new Integer(10), degreeCurricularPlanAck.getDegree().getIdInternal());

            System.out
                    .println("EditDescriptionDegreeCurricularPlanTest was SUCCESSFULY runned by service: testSuccessfull");

        } catch (FenixServiceException e) {
            e.printStackTrace();
            fail("Reading a degree information" + e);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Reading a degree information" + e);
        }
    }
}