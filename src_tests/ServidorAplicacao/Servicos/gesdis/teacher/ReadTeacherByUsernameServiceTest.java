/*
 * Created on 20/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorAplicacao.Servicos.gesdis.teacher;

import DataBeans.InfoPerson;
import DataBeans.InfoTeacher;
import DataBeans.util.Cloner;
import Dominio.IPessoa;
import ServidorAplicacao.Servicos.TestCaseReadServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author jmota
 * 
 * To change this generated comment go to Window>Preferences>Java>Code
 * Generation>Code and Comments
 */
public class ReadTeacherByUsernameServiceTest extends TestCaseReadServices {

    /**
     * @param testName
     */
    public ReadTeacherByUsernameServiceTest(String testName) {
        super(testName);

    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices#getNameOfServiceToBeTested()
     */
    protected String getNameOfServiceToBeTested() {
        return "ReadTeacherByUsername";
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseReadServices#getArgumentsOfServiceToBeTestedUnsuccessfuly()
     */
    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
        String[] result = { "usernameErrado" };
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseReadServices#getArgumentsOfServiceToBeTestedSuccessfuly()
     */
    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
        String[] result = { "user" };
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseReadServices#getNumberOfItemsToRetrieve()
     */
    protected int getNumberOfItemsToRetrieve() {
        return 1;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseReadServices#getObjectToCompare()
     */
    protected Object getObjectToCompare() {
        InfoPerson infoPerson = new InfoPerson();
        InfoTeacher infoTeacher = new InfoTeacher();
        ISuportePersistente sp;
        IPessoa person;
        try {
            sp = SuportePersistenteOJB.getInstance();
            IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();
            sp.iniciarTransaccao();
            person = persistentPerson.lerPessoaPorUsername("user");
            sp.confirmarTransaccao();
            infoPerson = Cloner.copyIPerson2InfoPerson(person);
            infoTeacher.setInfoPerson(infoPerson);
            infoTeacher.setTeacherNumber(new Integer(1));

        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
        }

        return infoTeacher;
    }

}