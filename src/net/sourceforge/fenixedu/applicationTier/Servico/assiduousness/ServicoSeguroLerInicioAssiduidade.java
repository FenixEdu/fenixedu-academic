package net.sourceforge.fenixedu.applicationTier.Servico.assiduousness;

import java.sql.Timestamp;

import net.sourceforge.fenixedu.applicationTier.ServicoAutorizacao;
import net.sourceforge.fenixedu.applicationTier.ServicoSeguro;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotExecuteException;
import net.sourceforge.fenixedu.persistenceTierJDBC.IFuncionarioPersistente;
import net.sourceforge.fenixedu.persistenceTierJDBC.SuportePersistente;

/**
 * 
 * @author Fernanda Quitério & Tânia Pousão
 */
public class ServicoSeguroLerInicioAssiduidade extends ServicoSeguro {
    private int _numMecanografico = 0;

    private Timestamp _dataInicioConsulta = null;

    private Timestamp _dataFimConsulta = null;

    private Timestamp _dataAssiduidade = null;

    public ServicoSeguroLerInicioAssiduidade(ServicoAutorizacao servicoAutorizacao,
            int numMecanografico, Timestamp dataInicioConsulta, Timestamp dataFimConsulta) {
        super(servicoAutorizacao);
        _numMecanografico = numMecanografico;
        _dataInicioConsulta = dataInicioConsulta;
        _dataFimConsulta = dataFimConsulta;
    }

    public void execute() throws NotExecuteException {
        IFuncionarioPersistente iFuncionarioPersistente = SuportePersistente.getInstance()
                .iFuncionarioPersistente();

        if ((_dataAssiduidade = iFuncionarioPersistente.lerInicioAssiduidade(_numMecanografico)) == null) {
            throw new NotExecuteException("error.assiduidade.naoExiste");
        }

        if (_dataAssiduidade.before(_dataInicioConsulta)) {
            _dataAssiduidade = null;
        } else if (_dataAssiduidade.after(_dataFimConsulta)) {
            throw new NotExecuteException("error.assiduidade.naoExiste");
        }
    }

    public Timestamp getDataAssiduidade() {
        return _dataAssiduidade;
    }

}