package ServidorAplicacao.Servico.assiduousness;

import java.util.ArrayList;

import ServidorAplicacao.ServicoAutorizacao;
import ServidorAplicacao.ServicoSeguro;
import ServidorAplicacao.Servico.exceptions.NotExecuteException;
import ServidorPersistenteJDBC.IFuncionarioPersistente;
import ServidorPersistenteJDBC.SuportePersistente;

/**
 *
 * @author  Fernanda Quitério & Tania Pousão
 */
public class ServicoSeguroLerTodosFuncionarios extends ServicoSeguro {
	private ArrayList _listaFuncionarios = null;

	public ServicoSeguroLerTodosFuncionarios(ServicoAutorizacao servicoAutorizacaoLer) {
		super(servicoAutorizacaoLer);
	}

	public void execute() throws NotExecuteException {
		IFuncionarioPersistente iFuncionarioPersistente = SuportePersistente.getInstance().iFuncionarioPersistente();
		if ((_listaFuncionarios = iFuncionarioPersistente.lerTodosFuncionarios()) == null) {
			throw new NotExecuteException("error.funcionario.naoExistem");
		}
	}

	public ArrayList getListaFuncionarios() {
		return _listaFuncionarios;
	}
}