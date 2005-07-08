package net.sourceforge.fenixedu.applicationTier.Servico.assiduousness;

import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.ServicoAutorizacao;
import net.sourceforge.fenixedu.applicationTier.ServicoSeguro;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotExecuteException;
import net.sourceforge.fenixedu.domain.Funcionario;
import net.sourceforge.fenixedu.persistenceTierJDBC.IFuncionarioPersistente;
import net.sourceforge.fenixedu.persistenceTierJDBC.IJustificacaoPersistente;
import net.sourceforge.fenixedu.persistenceTierJDBC.SuportePersistente;

/**
 * 
 * @author Fernanda Quitério & Tânia Pousão
 */
public class ServicoSeguroLerJustificacoesComValidade extends ServicoSeguro {
    private int _numMecanografico;

    private Date _dataInicioEscolha = null;

    private Date _dataFimEscolha = null;

    private Funcionario _funcionario = null;

    private List _listaJustificacoes = null;

    public ServicoSeguroLerJustificacoesComValidade(ServicoAutorizacao servicoAutorizacaoLer,
            int numMecanografico, Date dataInicioEscolha, Date dataFimEscolha) {
        super(servicoAutorizacaoLer);
        _numMecanografico = numMecanografico;
        _dataInicioEscolha = dataInicioEscolha;
        _dataFimEscolha = dataFimEscolha;
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

        _listaJustificacoes = iJustificacaoPersistente.lerJustificacoesFuncionarioComValidade(
                _funcionario.getCodigoInterno(), _dataInicioEscolha, _dataFimEscolha);
    }

    public List getListaJustificacoes() {
        return _listaJustificacoes;
    }
}