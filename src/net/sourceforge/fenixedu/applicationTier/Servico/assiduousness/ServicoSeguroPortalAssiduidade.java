package net.sourceforge.fenixedu.applicationTier.Servico.assiduousness;

import net.sourceforge.fenixedu.applicationTier.ServicoAutorizacao;
import net.sourceforge.fenixedu.applicationTier.ServicoSeguro;

/**
 * 
 * @author Fernanda Quitério & Tania Pousão
 */
public class ServicoSeguroPortalAssiduidade extends ServicoSeguro {

    public ServicoSeguroPortalAssiduidade(ServicoAutorizacao servicoAutorizacaoPortalAssiduidade) {
        super(servicoAutorizacaoPortalAssiduidade);
    }

    public void execute() {
    }
}