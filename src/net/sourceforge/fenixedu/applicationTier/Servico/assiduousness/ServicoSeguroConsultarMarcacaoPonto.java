package net.sourceforge.fenixedu.applicationTier.Servico.assiduousness;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.ServicoAutorizacao;
import net.sourceforge.fenixedu.applicationTier.ServicoSeguro;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotExecuteException;
import net.sourceforge.fenixedu.domain.Funcionario;
import net.sourceforge.fenixedu.domain.MarcacaoPonto;
import net.sourceforge.fenixedu.domain.RegularizacaoMarcacaoPonto;
import net.sourceforge.fenixedu.persistenceTierJDBC.IFuncionarioPersistente;
import net.sourceforge.fenixedu.persistenceTierJDBC.IMarcacaoPontoPersistente;
import net.sourceforge.fenixedu.persistenceTierJDBC.IRegularizacaoMarcacaoPontoPersistente;
import net.sourceforge.fenixedu.persistenceTierJDBC.SuportePersistente;
import net.sourceforge.fenixedu.persistenceTierJDBC.SuportePersistenteOracle;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;



/**
 *
 * @author  Fernanda Quitério & Tania Pousão
 */
public class ServicoSeguroConsultarMarcacaoPonto extends ServicoSeguro {
	private List _listaFuncionarios = null;
	private List _listaCartoes = null;
	private List _listaEstados = null;
	private Boolean _oracleDB = null;

	private Timestamp _dataInicio;
	private Timestamp _dataFim;

	private ArrayList _listaMarcacoesPonto = new ArrayList();
	private ArrayList _listaRegularizacoes = new ArrayList();

	public ServicoSeguroConsultarMarcacaoPonto(
		ServicoAutorizacao servicoAutorizacaoLer,
		List listaFuncionarios,
		List listaCartoes,
		List listaEstados,
		Timestamp dataInicio,
		Timestamp dataFim, 
		Boolean oracleDB) {
		super(servicoAutorizacaoLer);
		_listaFuncionarios = listaFuncionarios;
		_listaCartoes = listaCartoes;
		_listaEstados = listaEstados;
		_dataInicio = dataInicio;
		_dataFim = dataFim;
		_oracleDB = oracleDB;
	}

	public void execute() throws NotExecuteException {

		if (_listaFuncionarios != null && _listaFuncionarios.size() == 1) {
			//consulta de marcações de ponto de apenas um funcionario
			//assim verifica-se as datas de assiduidade
			try {
				lerFimAssiduidade(((Integer) _listaFuncionarios.get(0)).intValue());
			} catch (NotExecuteException nee) {
				throw new NotExecuteException(nee.getMessage());
			}

			try {
				lerInicioAssiduidade(((Integer) _listaFuncionarios.get(0)).intValue());
			} catch (NotExecuteException nee) {
				throw new NotExecuteException(nee.getMessage());
			}
		}

		IMarcacaoPontoPersistente iMarcacaoPontoPersistente = null;
		if(_oracleDB.booleanValue()) {
		    iMarcacaoPontoPersistente = SuportePersistenteOracle.getInstance().iMarcacaoPontoPersistente();
		} else {
		    iMarcacaoPontoPersistente = SuportePersistente.getInstance().iMarcacaoPontoPersistente();		    
		}
		if ((_listaMarcacoesPonto =
			(ArrayList) iMarcacaoPontoPersistente.consultarMarcacoesPonto(
				_listaFuncionarios,
				_listaCartoes,
				_listaEstados,
				_dataInicio,
				_dataFim))
			== null) {
			throw new NotExecuteException("error.marcacaoPonto.naoExistem");
		}

		ArrayList listaMarcacoesRegularizadas = new ArrayList();
		listaMarcacoesRegularizadas = (ArrayList) CollectionUtils.select(_listaMarcacoesPonto, new Predicate() {
			public boolean evaluate(Object arg0) {
				MarcacaoPonto marcacao = (MarcacaoPonto) arg0;
				if (marcacao.getEstado().equals("regularizada")) {
					return true;
				}
				return false;
			}
		});
		
		IRegularizacaoMarcacaoPontoPersistente iRegularizacaoMarcacaoPontoPersistente =
			SuportePersistente.getInstance().iRegularizacaoMarcacaoPontoPersistente();
			IFuncionarioPersistente iFuncionarioPersistente = SuportePersistente.getInstance().iFuncionarioPersistente();
		Iterator iterMarcacoes = listaMarcacoesRegularizadas.iterator();
		while (iterMarcacoes.hasNext()) {
			MarcacaoPonto marcacao = (MarcacaoPonto) iterMarcacoes.next();
			RegularizacaoMarcacaoPonto regularizacao =
				iRegularizacaoMarcacaoPontoPersistente.lerRegularizacaoMarcacaoPonto(marcacao.getCodigoInterno());
			if (regularizacao != null) {
				Funcionario funcionarioQuem = iFuncionarioPersistente.lerFuncionarioSemHistorico(regularizacao.getQuem());
				if(funcionarioQuem!=null){
					regularizacao.setQuem(funcionarioQuem.getNumeroMecanografico());
				}
				_listaRegularizacoes.add(regularizacao);
			}
		}
	}

	private void lerFimAssiduidade(int numMecanografico) throws NotExecuteException {
		ServicoAutorizacao servicoAutorizacao = new ServicoAutorizacao();

		ServicoSeguroLerFimAssiduidade servicoSeguroLerFimAssiduidade =
			new ServicoSeguroLerFimAssiduidade(servicoAutorizacao, numMecanografico, _dataInicio, _dataFim);
		servicoSeguroLerFimAssiduidade.execute();

		if (servicoSeguroLerFimAssiduidade.getDataAssiduidade() != null) {
			_dataFim = servicoSeguroLerFimAssiduidade.getDataAssiduidade();
		}
	} /* lerFimAssiduidade */

	private void lerInicioAssiduidade(int numMecanografico) throws NotExecuteException {
		ServicoAutorizacao servicoAutorizacao = new ServicoAutorizacao();

		ServicoSeguroLerInicioAssiduidade servicoSeguroLerInicioAssiduidade =
			new ServicoSeguroLerInicioAssiduidade(servicoAutorizacao, numMecanografico, _dataInicio, _dataFim);
		servicoSeguroLerInicioAssiduidade.execute();

		if (servicoSeguroLerInicioAssiduidade.getDataAssiduidade() != null) {
			_dataInicio = servicoSeguroLerInicioAssiduidade.getDataAssiduidade();
		}
	} /* lerInicioAssiduidade */

	public ArrayList getListaMarcacoesPonto() {
		return _listaMarcacoesPonto;
	}
	
	public ArrayList getListaRegularizacoes(){
		return _listaRegularizacoes;
	}
}