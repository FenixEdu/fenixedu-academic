package ServidorAplicacao.Servico.assiduousness;

import Dominio.Funcionario;
import Dominio.Pessoa;
import ServidorAplicacao.ServicoAutorizacao;
import ServidorAplicacao.Servico.exceptions.NotAuthorizeException;
import ServidorPersistenteJDBC.IFuncNaoDocentePersistente;
import ServidorPersistenteJDBC.IFuncionarioPersistente;
import ServidorPersistenteJDBC.SuportePersistente;

/**
 * 
 * @author Fernanda Quitério & Tania Pousão
 */
public class ServicoAutorizacaoPortalAssiduidade extends ServicoAutorizacao {
    private Pessoa _pessoa;

    public ServicoAutorizacaoPortalAssiduidade(Pessoa pessoa) {
        _pessoa = pessoa;
    }

    public void execute() throws NotAuthorizeException {
        IFuncionarioPersistente iFuncionarioPersistente = SuportePersistente.getInstance()
                .iFuncionarioPersistente();
        IFuncNaoDocentePersistente iFuncNaoDocentePersistente = SuportePersistente.getInstance()
                .iFuncNaoDocentePersistente();
        Funcionario funcionario = null;
        if ((funcionario = iFuncionarioPersistente.lerFuncionarioSemHistoricoPorPessoa(_pessoa
                .getIdInternal().intValue())) == null) {
            throw new NotAuthorizeException("error.semAutorizacao");
        }
        if (iFuncNaoDocentePersistente.lerFuncNaoDocentePorNumMecanografico(funcionario
                .getNumeroMecanografico()) == null) {
            throw new NotAuthorizeException("error.semAutorizacao");
        }

    }
}