package ServidorAplicacao.Servico.assiduousness;

import Dominio.Cartao;
import Dominio.FuncNaoDocente;
import Dominio.Funcionario;
import ServidorAplicacao.NotExecuteException;
import ServidorAplicacao.ServicoAutorizacao;
import ServidorAplicacao.ServicoSeguro;
import ServidorPersistenteJDBC.ICartaoPersistente;
import ServidorPersistenteJDBC.IFuncionarioPersistente;
import ServidorPersistenteJDBC.SuportePersistente;

/**
 *
 * @author  Fernanda Quitério e Tania Pousão
 */
public class ServicoSeguroLerCartao extends ServicoSeguro {
  private int _numCartao = 0;
  private Cartao _cartao = null;
  private FuncNaoDocente _funcNaoDocente = null;
  private Funcionario _funcionario = null;
  
  public ServicoSeguroLerCartao(ServicoAutorizacao servicoAutorizacaoLer, int numCartao){
    super(servicoAutorizacaoLer);
    _numCartao = numCartao;
  }
  
  public void execute() throws NotExecuteException {
    ICartaoPersistente iCartaoPersistente = SuportePersistente.getInstance().iCartaoPersistente();
    if((_cartao = iCartaoPersistente.cartaoAtribuido(_numCartao)) == null){
      throw new NotExecuteException("error.cartao.naoAtribuido");
    }
    
    IFuncionarioPersistente iFuncionarioPersistente = SuportePersistente.getInstance().iFuncionarioPersistente();
    if((_funcionario = iFuncionarioPersistente.lerFuncionarioPorFuncNaoDocente(_cartao.getChaveFuncNaoDocente())) == null){
      throw new NotExecuteException("error.funcionario.naoExiste");
    }
    
    /*
    IFuncNaoDocentePersistente iFuncNaoDocentePersistente = SuportePersistente.getInstance().iFuncNaoDocentePersistente();
    if((_funcNaoDocente = iFuncNaoDocentePersistente.lerFuncNaoDocente(_cartao.getChaveFuncNaoDocente())) == null){
      throw new NotExecuteException("error.funcionario.naoExiste");
    }
    
    IFuncionarioPersistente iFuncionarioPersistente = SuportePersistente.getInstance().iFuncionarioPersistente();
    if((_funcionario = iFuncionarioPersistente.lerFuncionario(_funcNaoDocente.getChaveFuncionario())) == null){
      throw new NotExecuteException("error.funcionario.naoExiste");
    }
     */
  }
  
  public Cartao getCartao() {
    return _cartao;
  }
  
  public Funcionario getFuncionario() {
    return _funcionario;
  }
}