/*
 * TestCaseServices.java
 *
 * Created on 2003/04/05
 */

package ServidorAplicacao.Servicos;

import ServidorAplicacao.Servico.Autenticacao;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public abstract class TestCaseReadServicesIntranet extends TestCaseReadServices {

    /**
     * @param testName
     */
    public TestCaseReadServicesIntranet(String testName) {
        super(testName);
    }

    public String getApplication() {
        return Autenticacao.INTRANET;
    }
}