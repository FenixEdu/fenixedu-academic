/*
 * 
 * Created on 27/Out/2003
 *
 */

package ServidorAplicacao.Filtros.publication;

import java.util.ArrayList;
import java.util.Collection;

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.logic.filterManager.exceptions.FilterException;
import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoRole;
import ServidorAplicacao.Filtro.exception.NotAuthorizedFilterException;
import ServidorAplicacao.Filtro.publication.PublicationAuthorAuthorizationFilter;
import ServidorAplicacao.Filtros.FilterTestCase;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.publication.DeletePublication;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;

/**
 * @author <a href="mailto:cgmp@mega.ist.utl.pt">Carlos Pereira </a>& <a
 *         href="mailto:fmmp@mega.ist.utl.pt">Francisco Passos </a>
 *  
 */

public class PublicationAuthorFilterTest extends FilterTestCase {

    protected String getDataSetFilePath() {
        return "etc/testDataSetForPublications.xml";
    }

    protected String getNameOfFilterToBeTested() {
        return "PublicationAuthorAuthorization";
    }

    /**
     * @param testName
     */
    public PublicationAuthorFilterTest(java.lang.String testName) {
        super(testName);
    }

    private ServiceRequest createRequest(UserView user, IService service, Object[] args) {
        ServiceRequest request = new ServiceRequest();
        request.setRequester(user);
        request.setService(service);
        request.setArguments(args);
        return request;
    }

    protected ServiceRequest createWorkingRequest() {
        //Creation of the authorized role
        InfoRole infoRole = new InfoRole();
        infoRole.setIdInternal(new Integer(3));
        infoRole.setPage("/index.do");
        infoRole.setPageNameProperty("portal.teacher");
        infoRole.setPortalSubApplication("/teacher");
        infoRole.setRoleType(RoleType.TEACHER);

        Collection infoRoles = new ArrayList();
        infoRoles.add(infoRole);

        //Creation of the authorized User View
        UserView user = new UserView("bom", infoRoles);
        //        user.setFullName("Teste do utilizador bom");
        user.setCandidateView(null);

        Object[] args = { new Integer(1) };

        //Criar um servico que use o filtro que pretendemos testar
        DeletePublication service = new DeletePublication();

        return createRequest(user, service, args);
    }

    protected ServiceRequest createNonWorkingRequest() {
        //Creation of the authorized role
        InfoRole infoRole = new InfoRole();
        infoRole.setIdInternal(new Integer(3));
        infoRole.setPage("/index.do");
        infoRole.setPageNameProperty("portal.teacher");
        infoRole.setPortalSubApplication("/teacher");
        infoRole.setRoleType(RoleType.TEACHER);

        Collection infoRoles = new ArrayList();
        infoRoles.add(infoRole);

        //Creation of the not authorized User View
        UserView user = new UserView("medio", infoRoles);
        user.setFullName("Teste do utilizador medio");
        user.setCandidateView(null);

        Object[] args = { new Integer(1) };

        //Criar um servico que use o filtro que pretendemos testar
        DeletePublication service = new DeletePublication();

        return createRequest(user, service, args);
    }

    /***************************************************************************
     * ********** Inicio dos testes ao filtro
     * 
     * @throws FilterException
     * @throws Exception
     *             Successfully pass the filter
     */
    public void testPublicationAuthorFilterOK() throws FilterException, Exception {

        PublicationAuthorAuthorizationFilter filter = new PublicationAuthorAuthorizationFilter();

        SuportePersistenteOJB.getInstance().iniciarTransaccao();
        filter.execute(createWorkingRequest(), null);
        SuportePersistenteOJB.getInstance().confirmarTransaccao();

        System.out.println(getNameOfFilterToBeTested() + " was SUCCESSFULLY run by test: "
                + "testPublicationAuthorFilterOK");
    }

    /**
     * @throws FilterException
     * @throws Exception
     *             Test that tries to chage a publication with a non-author
     *             teacher
     */
    public void testPublicationAuthorFilterWithNonAuthorTeacher() throws Exception {
        try {

            PublicationAuthorAuthorizationFilter filter = new PublicationAuthorAuthorizationFilter();

            SuportePersistenteOJB.getInstance().iniciarTransaccao();
            filter.execute(createNonWorkingRequest(), null);
            SuportePersistenteOJB.getInstance().confirmarTransaccao();

            fail(getNameOfFilterToBeTested()
                    + "failed since a teacher was able to modify a publication for wich he wasn't an author");

        } catch (NotAuthorizedFilterException nafe) {
            System.out.println(getNameOfFilterToBeTested() + " was SUCCESSFULLY run by test: "
                    + "testPublicationAuthorFilterWithNonAuthorTeacher");
        }
    }

}