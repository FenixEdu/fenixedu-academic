package net.sourceforge.fenixedu.applicationTier.Servico.assiduousness;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.ServicoAutorizacao;
import net.sourceforge.fenixedu.applicationTier.ServicoSeguro;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotExecuteException;
import net.sourceforge.fenixedu.persistenceTierJDBC.IMarcacaoPontoPersistente;
import net.sourceforge.fenixedu.persistenceTierJDBC.SuportePersistente;
import net.sourceforge.fenixedu.persistenceTierJDBC.SuportePersistenteOracle;



/**
 *
 * @author Fernanda Quitério & Tânia Pousão
 */
public class ServicoSeguroLerMarcacoesPonto extends ServicoSeguro {
	private List _listaFuncionarios = null;
	private List _listaCartoes = null;
	private List _listaEstados = null;
	private Boolean _oracleDB = null;

	private Timestamp _dataInicio;
	private Timestamp _dataFim;

	private ArrayList _listaMarcacoesPonto = new ArrayList();

	public ServicoSeguroLerMarcacoesPonto(
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
	    IMarcacaoPontoPersistente iMarcacaoPontoPersistente = null;
	    if(_oracleDB.booleanValue()) {
		iMarcacaoPontoPersistente =
			SuportePersistenteOracle.getInstance().iMarcacaoPontoPersistente();
	    } else {
			iMarcacaoPontoPersistente =
				SuportePersistente.getInstance().iMarcacaoPontoPersistente();
	        
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

	}

	public ArrayList getListaMarcacoesPonto() {
		return _listaMarcacoesPonto;
	}
}