package ServidorAplicacao.Servico.assiduousness;

import ServidorAplicacao.ServicoAutorizacao;
import ServidorAplicacao.ServicoSeguro;
import ServidorAplicacao.Servico.exceptions.NotExecuteException;

/**
 *
 * @author  Fernanda Quitério & Tania Pousão
 */
public class ServicoSeguroPortalAssiduidade extends ServicoSeguro {
  
  public ServicoSeguroPortalAssiduidade(ServicoAutorizacao servicoAutorizacaoPortalAssiduidade) {
    super(servicoAutorizacaoPortalAssiduidade);
  }
  
  public void execute() throws NotExecuteException {
  }
}