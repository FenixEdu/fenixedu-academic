package net.sourceforge.fenixedu.applicationTier.Servico.assiduousness;

import java.util.Date;

import net.sourceforge.fenixedu.applicationTier.ServicoAutorizacao;
import net.sourceforge.fenixedu.applicationTier.ServicoSeguro;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotExecuteException;
import net.sourceforge.fenixedu.domain.Funcionario;
import net.sourceforge.fenixedu.persistenceTierJDBC.IFuncionarioPersistente;
import net.sourceforge.fenixedu.persistenceTierJDBC.SuportePersistente;

/**
 *
 * @author  Fernanda Quitério & Tania Pousão
 */
public class ServicoSeguroLerFuncionarioPorDia extends ServicoSeguro {
  
  private Funcionario _funcionario = null;
  private int _numMecanografico;
  private Date _dataConsulta;
  
  public ServicoSeguroLerFuncionarioPorDia(ServicoAutorizacao servicoAutorizacaoLerFuncionario,
  int numMecanografico,Date dataConsulta) {
    super(servicoAutorizacaoLerFuncionario);
    _numMecanografico = numMecanografico;
    _dataConsulta= dataConsulta;
  }
  
  public void execute() throws NotExecuteException {
    IFuncionarioPersistente iFuncionarioPersistente = SuportePersistente.getInstance().iFuncionarioPersistente();
    if((_funcionario = iFuncionarioPersistente.lerFuncionarioPorNumMecanografico(_numMecanografico,_dataConsulta)) == null){
      throw new NotExecuteException("error.funcionario.naoExiste");
    }
  }
  
  public Funcionario getFuncionario() {
    return _funcionario;
  }
}