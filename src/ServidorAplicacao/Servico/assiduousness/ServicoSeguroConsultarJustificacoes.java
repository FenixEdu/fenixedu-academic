package ServidorAplicacao.Servico.assiduousness;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import Dominio.Funcionario;
import ServidorAplicacao.ServicoAutorizacao;
import ServidorAplicacao.ServicoSeguro;
import ServidorAplicacao.Servico.exceptions.NotExecuteException;
import ServidorPersistenteJDBC.IFuncionarioPersistente;
import ServidorPersistenteJDBC.IJustificacaoPersistente;
import ServidorPersistenteJDBC.SuportePersistente;

/**
 * 
 * @author Fernanda Quitério & Tania Pousão
 */
public class ServicoSeguroConsultarJustificacoes extends ServicoSeguro {

    private int _numMecanografico;

    private Date _dataInicioEscolha = null;

    private Date _dataFimEscolha = null;

    private Funcionario _funcionario = null;

    private List _listaJustificacoes = null;

    public ServicoSeguroConsultarJustificacoes(ServicoAutorizacao servicoAutorizacaoLer,
            int numMecanografico, Date dataInicioEscolha, Date dataFimEscolha) {
        super(servicoAutorizacaoLer);
        _numMecanografico = numMecanografico;
        _dataInicioEscolha = dataInicioEscolha;
        _dataFimEscolha = dataFimEscolha;
    }

    public void execute() throws NotExecuteException {

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

        IFuncionarioPersistente iFuncionarioPersistente = SuportePersistente.getInstance()
                .iFuncionarioPersistente();
        if ((_funcionario = iFuncionarioPersistente
                .lerFuncionarioSemHistoricoPorNumMecanografico(_numMecanografico)) == null) {
            throw new NotExecuteException("error.funcionario.naoExiste");
        }

        IJustificacaoPersistente iJustificacaoPersistente = SuportePersistente.getInstance()
                .iJustificacaoPersistente();
        _listaJustificacoes = iJustificacaoPersistente.lerJustificacoesFuncionarioComValidade(
                _funcionario.getCodigoInterno(), _dataInicioEscolha, _dataFimEscolha);
    }

    private void lerFimAssiduidade() throws NotExecuteException {
        ServicoAutorizacao servicoAutorizacao = new ServicoAutorizacao();

        ServicoSeguroLerFimAssiduidade servicoSeguroLerFimAssiduidade = new ServicoSeguroLerFimAssiduidade(
                servicoAutorizacao, _numMecanografico, new Timestamp(_dataInicioEscolha.getTime()),
                new Timestamp(_dataFimEscolha.getTime()));
        servicoSeguroLerFimAssiduidade.execute();

        if (servicoSeguroLerFimAssiduidade.getDataAssiduidade() != null) {
            _dataFimEscolha = servicoSeguroLerFimAssiduidade.getDataAssiduidade();
        }
    } /* lerFimAssiduidade */

    private void lerInicioAssiduidade() throws NotExecuteException {
        ServicoAutorizacao servicoAutorizacao = new ServicoAutorizacao();

        ServicoSeguroLerInicioAssiduidade servicoSeguroLerInicioAssiduidade = new ServicoSeguroLerInicioAssiduidade(
                servicoAutorizacao, _numMecanografico, new Timestamp(_dataInicioEscolha.getTime()),
                new Timestamp(_dataFimEscolha.getTime()));
        servicoSeguroLerInicioAssiduidade.execute();

        if (servicoSeguroLerInicioAssiduidade.getDataAssiduidade() != null) {
            _dataInicioEscolha = servicoSeguroLerInicioAssiduidade.getDataAssiduidade();
        }
    } /* lerInicioAssiduidade */

    public List getListaJustificacoes() {
        return _listaJustificacoes;
    }
}