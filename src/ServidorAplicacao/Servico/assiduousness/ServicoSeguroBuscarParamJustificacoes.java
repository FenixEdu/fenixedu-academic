package ServidorAplicacao.Servico.assiduousness;

import java.util.ArrayList;

import ServidorAplicacao.ServicoAutorizacao;
import ServidorAplicacao.ServicoSeguro;
import ServidorAplicacao.Servico.exceptions.NotExecuteException;
import ServidorPersistenteJDBC.IParamJustificacaoPersistente;
import ServidorPersistenteJDBC.SuportePersistente;

/**
 *
 * @author  Fernanda Quitério & Tania Pousão
 */
public class ServicoSeguroBuscarParamJustificacoes extends ServicoSeguro {
  
  private ArrayList _listaJustificacoes = null;
  private ArrayList _listaParamJustificacoes = null;
  
  public ServicoSeguroBuscarParamJustificacoes(ServicoAutorizacao servicoAutorizacaoLer, ArrayList listaJustificacoes) {
    super(servicoAutorizacaoLer);
    _listaJustificacoes = listaJustificacoes;
  }
  
  public void execute() throws NotExecuteException {
    System.out.println("--->No ServicoSeguroBuscarParamJustificacoes...");
    
    IParamJustificacaoPersistente iParamJustificacaoPersistente = SuportePersistente.getInstance().iParamJustificacaoPersistente();
    if((_listaParamJustificacoes = iParamJustificacaoPersistente.lerParamJustificacoes(_listaJustificacoes)) == null){
      throw new NotExecuteException("error.tiposJustificacao.naoExistem");
    }
  }
  
  public ArrayList getListaJustificacoes() {
    return _listaParamJustificacoes;
  }
}