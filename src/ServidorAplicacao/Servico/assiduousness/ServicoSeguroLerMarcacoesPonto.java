package ServidorAplicacao.Servico.assiduousness;

import java.sql.Timestamp;
import java.util.ArrayList;

import ServidorAplicacao.ServicoAutorizacao;
import ServidorAplicacao.ServicoSeguro;
import ServidorAplicacao.Servico.exceptions.NotExecuteException;
import ServidorPersistenteJDBC.IMarcacaoPontoPersistente;
import ServidorPersistenteJDBC.SuportePersistente;

/**
 *
 * @author Fernanda Quitério & Tânia Pousão
 */
public class ServicoSeguroLerMarcacoesPonto extends ServicoSeguro {
	private ArrayList _listaFuncionarios = null;
	private ArrayList _listaCartoes = null;
	private ArrayList _listaEstados = null;

	private Timestamp _dataInicio;
	private Timestamp _dataFim;

	private ArrayList _listaMarcacoesPonto = new ArrayList();

	public ServicoSeguroLerMarcacoesPonto(
		ServicoAutorizacao servicoAutorizacaoLer,
		ArrayList listaFuncionarios,
		ArrayList listaCartoes,
		ArrayList listaEstados,
		Timestamp dataInicio,
		Timestamp dataFim) {
		super(servicoAutorizacaoLer);
		_listaFuncionarios = listaFuncionarios;
		_listaCartoes = listaCartoes;
		_listaEstados = listaEstados;
		_dataInicio = dataInicio;
		_dataFim = dataFim;
	}

	public void execute() throws NotExecuteException {
		
		IMarcacaoPontoPersistente iMarcacaoPontoPersistente =
			SuportePersistente.getInstance().iMarcacaoPontoPersistente();
		if ((_listaMarcacoesPonto =
			iMarcacaoPontoPersistente.consultarMarcacoesPonto(
				_listaFuncionarios,
				_listaCartoes,
				_listaEstados,
				_dataInicio,
				_dataFim))
			== null) {
			throw new NotExecuteException("error.marcacaoPonto.naoExistem");
		}

	}

	public ArrayList getListaMarcacoesPonto() {
		return _listaMarcacoesPonto;
	}
}