package ServidorAplicacao.Servico.assiduousness;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import Dominio.Funcionario;
import ServidorAplicacao.NotExecuteException;
import ServidorAplicacao.ServicoAutorizacao;
import ServidorAplicacao.ServicoSeguro;
import ServidorPersistenteJDBC.IFuncionarioPersistente;
import ServidorPersistenteJDBC.IJustificacaoPersistente;
import ServidorPersistenteJDBC.SuportePersistente;

/**
 *
 * @author  Fernanda Quitério & Tania Pousão
 */
public class ServicoSeguroConsultarJustificacoes extends ServicoSeguro {

	private int _numMecanografico;
	private Date _dataInicioEscolha = null;
	private Date _dataFimEscolha = null;
	private Funcionario _funcionario = null;
	private ArrayList _listaJustificacoes = null;

	public ServicoSeguroConsultarJustificacoes(
		ServicoAutorizacao servicoAutorizacaoLer,
		int numMecanografico,
		Date dataInicioEscolha,
		Date dataFimEscolha) {
		super(servicoAutorizacaoLer);
		_numMecanografico = numMecanografico;
		_dataInicioEscolha = dataInicioEscolha;
		_dataFimEscolha = dataFimEscolha;
	}

	public void execute() throws NotExecuteException {
		System.out.println("--->No ServicoSeguroConsultarJustificacoes...");

		try {
			lerFimAssiduidade();
		} catch (NotExecuteException nee) {
			throw new NotExecuteException(nee.getMessage());
		}

		try {
			lerInicioAssiduidade();
		} catch (NotExecuteException nee) {
			throw new NotExecuteException(nee.getMessage());
		}

		IFuncionarioPersistente iFuncionarioPersistente =
			SuportePersistente.getInstance().iFuncionarioPersistente();
		if ((_funcionario =
			iFuncionarioPersistente.lerFuncionarioPorNumMecanografico(_numMecanografico))
			== null) {
			throw new NotExecuteException("error.funcionario.naoExiste");
		}
		
		IJustificacaoPersistente iJustificacaoPersistente =
			SuportePersistente.getInstance().iJustificacaoPersistente();
		_listaJustificacoes =
			iJustificacaoPersistente.lerJustificacoesFuncionarioComValidade(
				_funcionario.getCodigoInterno(),
				_dataInicioEscolha,
				_dataFimEscolha);
	}

	private void lerFimAssiduidade() throws NotExecuteException {
		ServicoAutorizacao servicoAutorizacao = new ServicoAutorizacao();
		
		ServicoSeguroLerFimAssiduidade servicoSeguroLerFimAssiduidade =
		new ServicoSeguroLerFimAssiduidade(servicoAutorizacao, _numMecanografico,
		new Timestamp(_dataInicioEscolha.getTime()), new Timestamp(_dataFimEscolha.getTime()));
		servicoSeguroLerFimAssiduidade.execute();
		
		if(servicoSeguroLerFimAssiduidade.getDataAssiduidade() != null){
		_dataFimEscolha = servicoSeguroLerFimAssiduidade.getDataAssiduidade();
		}
		
		/*IFuncionarioPersistente iFuncionarioPersistente =
			SuportePersistente.getInstance().iFuncionarioPersistente();

		Date dataAssiduidade = null;	
		if ((dataAssiduidade = iFuncionarioPersistente.lerFimAssiduidade(_numMecanografico)) != null) {
			if (dataAssiduidade.after(_dataInicioEscolha) && dataAssiduidade.before(_dataFimEscolha)) {
				_dataFimEscolha = Timestamp.valueOf(dataAssiduidade.toString() + " 00:00:00.0");
			} else if (dataAssiduidade.before(_dataInicioEscolha)) {
				throw new NotExecuteException("error.assiduidade.semAssiduidade");
			}
		}*/
	} /* lerFimAssiduidade */

	private void lerInicioAssiduidade() throws NotExecuteException {
		ServicoAutorizacao servicoAutorizacao = new ServicoAutorizacao();
		
		ServicoSeguroLerInicioAssiduidade servicoSeguroLerInicioAssiduidade =
		new ServicoSeguroLerInicioAssiduidade(servicoAutorizacao, _numMecanografico,
		new Timestamp(_dataInicioEscolha.getTime()), new Timestamp(_dataFimEscolha.getTime()));
		servicoSeguroLerInicioAssiduidade.execute();
		
		if(servicoSeguroLerInicioAssiduidade.getDataAssiduidade() != null){
		_dataInicioEscolha = servicoSeguroLerInicioAssiduidade.getDataAssiduidade();
		}
		
		/*IFuncionarioPersistente iFuncionarioPersistente =
			SuportePersistente.getInstance().iFuncionarioPersistente();

		Date dataAssiduidade = null;
		if ((dataAssiduidade = iFuncionarioPersistente.lerInicioAssiduidade(_numMecanografico)) == null) {
			throw new NotExecuteException("error.assiduidade.naoExiste");
		}

		if (dataAssiduidade.after(_dataInicioEscolha) && dataAssiduidade.before(_dataFimEscolha)) {
			_dataInicioEscolha = Timestamp.valueOf(dataAssiduidade.toString() + " 00:00:00.0");
		} else if (dataAssiduidade.after(_dataFimEscolha)) {
			throw new NotExecuteException("error.assiduidade.naoExiste");
		}*/
	} /* lerInicioAssiduidade */

	public ArrayList getListaJustificacoes() {
		return _listaJustificacoes;
	}
}