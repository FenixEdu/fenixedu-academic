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

public abstract class TestCaseServicesIntranet extends TestCaseServicos {

    /**
     * @param testName
     */
    public TestCaseServicesIntranet(String testName) {
        super(testName);
    }

    public String getApplication() {
        return Autenticacao.INTRANET;
    }
}