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
public class ServicoSeguroLerFimAssiduidade extends ServicoSeguro {
	private int _numMecanografico = 0;
	private Timestamp _dataInicioConsulta = null;
	private Timestamp _dataFimConsulta = null;

	private Timestamp _dataAssiduidade = null;

	public ServicoSeguroLerFimAssiduidade(
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
		IFuncionarioPersistente iFuncionarioPersistente =
			SuportePersistente.getInstance().iFuncionarioPersistente();

		Date dataAssiduidade = null;
		if ((dataAssiduidade = iFuncionarioPersistente.lerFimAssiduidade(_numMecanografico)) != null) {
			if (dataAssiduidade.after(_dataInicioConsulta) && dataAssiduidade.before(_dataFimConsulta)) {
				_dataAssiduidade = Timestamp.valueOf(dataAssiduidade.toString() + " 23:59:59.0");
			} else if (dataAssiduidade.before(_dataInicioConsulta)) {
				throw new NotExecuteException("error.assiduidade.semAssiduidade");
			}
		}
	}

	public Timestamp getDataAssiduidade() {
		return _dataAssiduidade;
	}

}
