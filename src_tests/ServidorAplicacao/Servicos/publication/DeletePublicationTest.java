/*
 * Created on 27/Out/2003
 *
 */

package ServidorAplicacao.Servicos.publication;

import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.publication.DeletePublication;
import ServidorAplicacao.Servicos.ServiceTestCase;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Carlos Pereira & Francisco Passos
 *  
 */

public class DeletePublicationTest extends ServiceTestCase {

    /**
     * @param testName
     */
    public DeletePublicationTest(java.lang.String testName) {
        super(testName);
    }

    protected String getNameOfServiceToBeTested() {
        return "DeletePublication";
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see ServiceNeedsAuthenticationTestCase#getApplication()
     */
    protected String getApplication() {
        return Autenticacao.EXTRANET;
    }


    protected String getDataSetFilePath() {
        return "etc/testDataSetForPublications.xml";
    }

    /** ********** Inicio dos testes ao serviço************* */

    /*
     * Successfully delete a publication
     */
    public void testDeletePublicationOK() {
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();
            DeletePublication dp = new DeletePublication();
            dp.run(new Integer(1));
            sp.confirmarTransaccao();
            
            compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets_templates/servicos/publication/testDeletePublicationOKExpectedDataSet.xml");

            System.out.println(getNameOfServiceToBeTested() + " was SUCCESSFULLY run by test: "
                    + "testDeletePublicationOK");
        } catch (Exception e) {
            fail("Deleting Publication " + e);
        }
    }

    public void testDeletePublicationNonExistent() {
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();
            DeletePublication dp = new DeletePublication();
            dp.run(new Integer(10));
            sp.confirmarTransaccao();
            
            fail(getNameOfServiceToBeTested() + "failed since it deleted a non existant publication");
            
        } catch (Exception e) {
            System.out.println(getNameOfServiceToBeTested() + " was SUCCESSFULLY run by test: "
                    + "testDeletePublicationNonExistent");
            
        }
    }
}