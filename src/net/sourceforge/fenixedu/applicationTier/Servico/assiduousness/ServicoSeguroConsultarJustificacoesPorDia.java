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