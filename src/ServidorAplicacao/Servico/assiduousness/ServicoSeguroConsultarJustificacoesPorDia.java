package ServidorAplicacao.Servico.assiduousness;

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
public class ServicoSeguroConsultarJustificacoesPorDia extends ServicoSeguro {

    private int _numMecanografico;

    private Date _dataEscolha = null;

    private Funcionario _funcionario = null;

    private List _listaJustificacoes = null;

    public ServicoSeguroConsultarJustificacoesPorDia(ServicoAutorizacao servicoAutorizacaoLer,
            int numMecanografico, Date dataEscolha) {
        super(servicoAutorizacaoLer);
        _numMecanografico = numMecanografico;
        _dataEscolha = dataEscolha;
    }

    public void execute() throws NotExecuteException {

        IFuncionarioPersistente iFuncionarioPersistente = SuportePersistente.getInstance()
                .iFuncionarioPersistente();
        if ((_funcionario = iFuncionarioPersistente
                .lerFuncionarioSemHistoricoPorNumMecanografico(_numMecanografico)) == null) {
            throw new NotExecuteException("error.funcionario.naoExiste");
        }
        IJustificacaoPersistente iJustificacaoPersistente = SuportePersistente.getInstance()
                .iJustificacaoPersistente();
        if ((_listaJustificacoes = iJustificacaoPersistente.lerJustificacoes(_funcionario
                .getCodigoInterno(), _dataEscolha)) == null) {
            throw new NotExecuteException("error.justificacao.naoExiste");
        }
    }

    public List getListaJustificacoes() {
        return _listaJustificacoes;
    }
}