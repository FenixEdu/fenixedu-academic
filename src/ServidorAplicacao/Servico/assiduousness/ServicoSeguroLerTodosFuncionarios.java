package ServidorAplicacao.Servico.assiduousness;

import java.util.ArrayList;
import java.util.Calendar;

import ServidorAplicacao.ServicoAutorizacao;
import ServidorAplicacao.Servico.exceptions.NotExecuteException;
import ServidorPersistenteJDBC.IFuncionarioPersistente;
import ServidorPersistenteJDBC.SuportePersistente;

/**
 *
 * @author  Fernanda Quitério & Tania Pousão
 */
public class ServicoSeguroLerTodosFuncionarios  extends ServidorAplicacao.ServicoSeguro {
	private ArrayList _listaFuncionarios = null;
  
	public ServicoSeguroLerTodosFuncionarios(ServicoAutorizacao servicoAutorizacaoLer) {
		super(servicoAutorizacaoLer);    
	}
  
	public void execute() throws NotExecuteException {    
		IFuncionarioPersistente iFuncionarioPersistente = SuportePersistente.getInstance().iFuncionarioPersistente();
		if((_listaFuncionarios = iFuncionarioPersistente.lerTodosFuncionarios(Calendar.getInstance().getTime())) == null){
			throw new NotExecuteException("error.funcionario.naoExistem");
		}   
	}
  
	public ArrayList getListaFuncionarios() {
		return _listaFuncionarios;
	}
}