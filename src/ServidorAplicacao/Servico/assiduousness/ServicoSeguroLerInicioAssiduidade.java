package ServidorAplicacao.Servico.assiduousness;

import java.sql.Timestamp;
import java.util.Date;

import ServidorAplicacao.ServicoAutorizacao;
import ServidorAplicacao.ServicoSeguro;
import ServidorAplicacao.Servico.exceptions.NotExecuteException;
import ServidorPersistenteJDBC.IFuncionarioPersistente;
import ServidorPersistenteJDBC.SuportePersistente;

/**
 *
 * @author Fernanda Quitério & Tânia Pousão
 */
public class ServicoSeguroLerInicioAssiduidade extends ServicoSeguro {
	private int _numMecanografico = 0;
	private Timestamp _dataInicioConsulta = null;
	private Timestamp _dataFimConsulta = null;

	private Timestamp _dataAssiduidade = null;

	public ServicoSeguroLerInicioAssiduidade(
		ServicoAutorizacao servicoAutorizacao,
		int numMecanografico,
		Timestamp dataInicioConsulta,
		Timestamp dataFimConsulta) {
		super(servicoAutorizacao);
		_numMecanografico = numMecanografico;
		_dataInicioConsulta = dataInicioConsulta;
		_dataFimConsulta = dataFimConsulta;
	}

	public void execute() throws NotExecuteException {
		IFuncionarioPersistente iFuncionarioPersistente = SuportePersistente.getInstance().iFuncionarioPersistente();
		
		Date dataAssiduidade = null;
		if ((dataAssiduidade = iFuncionarioPersistente.lerInicioAssiduidade(_numMecanografico)) == null) {
			throw new NotExecuteException("error.assiduidade.naoExiste");
		}

		if (dataAssiduidade.after(_dataInicioConsulta) && dataAssiduidade.before(_dataFimConsulta)) {
			_dataAssiduidade = new Timestamp(Timestamp.valueOf(dataAssiduidade.toString() + " 00:00:00.0").getTime());
		} else if (dataAssiduidade.after(_dataFimConsulta)) {
			throw new NotExecuteException("error.assiduidade.naoExiste");
		}
	}

	public Timestamp getDataAssiduidade() {
		return _dataAssiduidade;
	}

}
