package ServidorAplicacao.Servico.assiduousness;

import Dominio.Funcionario;
import ServidorAplicacao.NotExecuteException;
import ServidorAplicacao.ServicoAutorizacao;
import ServidorAplicacao.ServicoSeguro;
import ServidorPersistenteJDBC.IFuncionarioPersistente;
import ServidorPersistenteJDBC.SuportePersistente;

/**
 *
 * @author  Fernanda Quitério & Tania Pousão
 */
public class ServicoSeguroLerFuncionario extends ServicoSeguro {
  
  private Funcionario _funcionario = null;
  private int _chavePessoa;
  
  public ServicoSeguroLerFuncionario(ServicoAutorizacao servicoAutorizacaoLerFuncionario,
  int chavePessoa) {
    super(servicoAutorizacaoLerFuncionario);
    _chavePessoa = chavePessoa;
  }
  
  public void execute() throws NotExecuteException {
    IFuncionarioPersistente iFuncionarioPersistente = SuportePersistente.getInstance().iFuncionarioPersistente();
    if((_funcionario = iFuncionarioPersistente.lerFuncionarioPorPessoa(_chavePessoa)) == null){
      throw new NotExecuteException("error.funcionario.naoExiste");
    }
  }
  
  public Funcionario getFuncionario() {
    return _funcionario;
  }
}