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
 * @author  Fernanda Quitério & Tania Pousão
 */
public class ServicoSeguroConsultarMarcacaoPonto extends ServicoSeguro {
	private ArrayList _listaFuncionarios = null;
	private ArrayList _listaCartoes = null;
	private ArrayList _listaEstados = null;

	private Timestamp _dataInicio;
	private Timestamp _dataFim;

	private ArrayList _listaMarcacoesPonto = new ArrayList();

	public ServicoSeguroConsultarMarcacaoPonto(
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

		if (_listaFuncionarios != null && _listaFuncionarios.size() == 1) {
			//consulta de marcações de ponto de apenas um funcionario
			//assim verifica-se as datas de assiduidade
			try {
				lerFimAssiduidade(((Integer)_listaFuncionarios.get(0)).intValue());
			} catch (NotExecuteException nee) {
				throw new NotExecuteException(nee.getMessage());
			}

			try {
				lerInicioAssiduidade(((Integer)_listaFuncionarios.get(0)).intValue());
			} catch (NotExecuteException nee) {
				throw new NotExecuteException(nee.getMessage());
			}
		}
		
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

	private void lerFimAssiduidade(int numMecanografico) throws NotExecuteException {
		ServicoAutorizacao servicoAutorizacao = new ServicoAutorizacao();
		
		ServicoSeguroLerFimAssiduidade servicoSeguroLerFimAssiduidade =
		new ServicoSeguroLerFimAssiduidade(servicoAutorizacao, numMecanografico,
		_dataInicio, _dataFim);
		servicoSeguroLerFimAssiduidade.execute();
		
		
		if(servicoSeguroLerFimAssiduidade.getDataAssiduidade() != null){
			_dataFim = servicoSeguroLerFimAssiduidade.getDataAssiduidade();	
		}
	} /* lerFimAssiduidade */

	private void lerInicioAssiduidade(int numMecanografico) throws NotExecuteException {
		ServicoAutorizacao servicoAutorizacao = new ServicoAutorizacao();
		
		ServicoSeguroLerInicioAssiduidade servicoSeguroLerInicioAssiduidade =
		new ServicoSeguroLerInicioAssiduidade(servicoAutorizacao, numMecanografico,
		_dataInicio, _dataFim);
		servicoSeguroLerInicioAssiduidade.execute();
		
		if(servicoSeguroLerInicioAssiduidade.getDataAssiduidade() != null){
		_dataInicio = servicoSeguroLerInicioAssiduidade.getDataAssiduidade();		
		}
	} /* lerInicioAssiduidade */

	public ArrayList getListaMarcacoesPonto() {
		return _listaMarcacoesPonto;
	}
}