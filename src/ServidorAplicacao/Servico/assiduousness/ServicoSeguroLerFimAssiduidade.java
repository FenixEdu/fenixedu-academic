package ServidorAplicacao.Servico.assiduousness;

import java.sql.Timestamp;

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

    public ServicoSeguroLerFimAssiduidade(ServicoAutorizacao servicoAutorizacao, int numMecanografico,
            Timestamp dataInicioConsulta, Timestamp dataFimConsulta) {
        super(servicoAutorizacao);
        _numMecanografico = numMecanografico;
        _dataInicioConsulta = dataInicioConsulta;
        _dataFimConsulta = dataFimConsulta;
    }

    public void execute() throws NotExecuteException {
        IFuncionarioPersistente iFuncionarioPersistente = SuportePersistente.getInstance()
                .iFuncionarioPersistente();

        if ((_dataAssiduidade = iFuncionarioPersistente.lerFimAssiduidade(_numMecanografico)) != null) {
            if (_dataAssiduidade.after(_dataFimConsulta)) {
                _dataAssiduidade = null;
            } else if (_dataAssiduidade.before(_dataInicioConsulta)) {
                throw new NotExecuteException("error.assiduidade.semAssiduidade");
            }
        }
    }

    public Timestamp getDataAssiduidade() {
        return _dataAssiduidade;
    }

}