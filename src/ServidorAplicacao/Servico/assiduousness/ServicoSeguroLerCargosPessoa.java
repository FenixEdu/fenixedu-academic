package ServidorAplicacao.Servico.assiduousness;

import java.util.ArrayList;

import ServidorAplicacao.NotExecuteException;
import ServidorAplicacao.ServicoAutorizacao;
import ServidorAplicacao.ServicoSeguro;
import ServidorPersistenteJDBC.IPessoaPersistente;
import ServidorPersistenteJDBC.SuportePersistente;

/**
 *
 * @author  Fernanda Quitério & Tania Pousão
 */
public class ServicoSeguroLerCargosPessoa extends ServicoSeguro {
  
  private int _chavePessoa;
  private ArrayList _cargos = null;
  
  public ServicoSeguroLerCargosPessoa(ServicoAutorizacao servicoAutorizacaoLer,
  int chavePessoa) {
    super(servicoAutorizacaoLer);
    _chavePessoa = chavePessoa;
  }
  
  public void execute() throws NotExecuteException {
    IPessoaPersistente iPessoaPersistente = SuportePersistente.getInstance().iPessoaPersistente();
    if((_cargos = iPessoaPersistente.lerCargos(_chavePessoa)) == null){
      throw new NotExecuteException();
    }
  }
  
  public ArrayList getCargos() {
    return _cargos;
  }
}