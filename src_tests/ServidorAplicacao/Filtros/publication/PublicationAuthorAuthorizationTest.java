/*
 * Created on 27/Out/2003
 *
 */

package ServidorAplicacao.Filtros.publication;

import junit.framework.TestCase;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import ServidorAplicacao.Filtro.publication.PublicationAuthorAuthorizationFilter;
import ServidorAplicacao.Servico.publication.DeletePublication;

/**
 * @author Carlos Pereira & Francisco Passos
 *  
 */

public class PublicationAuthorAuthorizationTest extends TestCase {

    
    protected String getNameOfFilterToBeTested() {
        return "PublicationAuthorAuthorization";
    }
    
    /**
     * @param testName
     */
    public PublicationAuthorAuthorizationTest(java.lang.String testName) {
        super(testName);
    }

    /** ********** Inicio dos testes ao filtro************* */

    /*
     * Successfully pass the filter
     */
    public void testPublicationAuthorAuthorizationOK() {
        try {
            ServiceRequest request = new ServiceRequest();
            PublicationAuthorAuthorizationFilter filter = new PublicationAuthorAuthorizationFilter();
            filter.execute(request, null);
            DeletePublication dp = new DeletePublication();
            
            System.out.println(getNameOfFilterToBeTested() + " was SUCCESSFULLY run by class: "
                    + this.getClass().getName());
        } catch (Exception e) {
            fail("Testing PublicationAuthorAuthorization Filter " + e);
        }
    }

//    public void testDeletePublicationNonExistent() {
//        try {
//            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
//            sp.iniciarTransaccao();
//            DeletePublication dp = new DeletePublication();
//            dp.run(new Integer(10));
//            sp.confirmarTransaccao();
//            
//            fail(getNameOfServiceToBeTested() + "failed since it deleted a non existant publication");
//            
//        } catch (Exception e) {
//            System.out.println(getNameOfServiceToBeTested() + " was SUCCESSFULLY run by class: "
//                    + this.getClass().getName());
//            
//        }
//    }
}