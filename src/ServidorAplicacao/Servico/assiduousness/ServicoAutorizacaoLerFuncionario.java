package ServidorAplicacao.Servico.assiduousness;

import java.util.ArrayList;

import Dominio.Funcionario;
import Dominio.Pessoa;
import ServidorAplicacao.NotAuthorizeException;
import ServidorAplicacao.ServicoAutorizacao;
import ServidorPersistenteJDBC.IFuncionarioPersistente;
import ServidorPersistenteJDBC.IPessoaPersistente;
import ServidorPersistenteJDBC.SuportePersistente;

/**
 *
 * @author  Fernanda Quitério & Tania Pousão
 */
public class ServicoAutorizacaoLerFuncionario extends ServicoAutorizacao {
  private Pessoa _pessoa;
  private int _numMecanografico;
  
  public ServicoAutorizacaoLerFuncionario(Pessoa pessoa, int numMecanografico) {
    _pessoa = pessoa;
    _numMecanografico = numMecanografico;
  }
  
  public void execute() throws NotAuthorizeException {
    IPessoaPersistente iPessoaPersistente = SuportePersistente.getInstance().iPessoaPersistente();;
    IFuncionarioPersistente iFuncionarioPersistente = SuportePersistente.getInstance().iFuncionarioPersistente();

    ArrayList cargos = iPessoaPersistente.lerCargos(_pessoa.getCodigoInterno().intValue());
    String cargo = null;
    
    if(cargos != null){
      if(!(cargos.contains("GestaoAssiduidade"))){
        Funcionario funcionario = iFuncionarioPersistente.lerFuncionarioPorPessoa(_pessoa.getCodigoInterno().intValue());
        if(funcionario.getNumeroMecanografico() != _numMecanografico){
          throw new NotAuthorizeException("error.semAutorizacao");
        }
      }
    } else {
      throw new NotAuthorizeException("error.semAutorizacao");
    }
  }
}
