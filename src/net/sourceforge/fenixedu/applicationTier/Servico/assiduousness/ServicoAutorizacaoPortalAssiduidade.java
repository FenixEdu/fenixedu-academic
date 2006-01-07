package net.sourceforge.fenixedu.applicationTier.Servico.assiduousness;

import net.sourceforge.fenixedu.applicationTier.ServicoAutorizacao;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizeException;
import net.sourceforge.fenixedu.domain.Funcionario;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.persistenceTierJDBC.IFuncionarioPersistente;
import net.sourceforge.fenixedu.persistenceTierJDBC.SuportePersistente;

/**
 * 
 * @author Fernanda Quitério & Tania Pousão
 */
public class ServicoAutorizacaoPortalAssiduidade extends ServicoAutorizacao {
    private Person _pessoa;

    public ServicoAutorizacaoPortalAssiduidade(Person pessoa) {
        _pessoa = pessoa;
    }

    public void execute() throws NotAuthorizeException {
        IFuncionarioPersistente iFuncionarioPersistente = SuportePersistente.getInstance()
                .iFuncionarioPersistente();
        Funcionario funcionario = null;
        if ((funcionario = iFuncionarioPersistente.lerFuncionarioSemHistoricoPorPessoa(_pessoa
                .getIdInternal().intValue())) == null) {
            throw new NotAuthorizeException("error.semAutorizacao");
        }
        if (funcionario.getPerson().getTeacher() != null) {
            throw new NotAuthorizeException("error.semAutorizacao");
        }

    }
}