package net.sourceforge.fenixedu.applicationTier.Servico.person;

import net.sourceforge.fenixedu.applicationTier.ServicoAutorizacao;
import net.sourceforge.fenixedu.applicationTier.ServicoSeguro;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotExecuteException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.persistenceTierJDBC.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTierJDBC.SuportePersistente;

public class ServicoSeguroAlterarPessoa extends ServicoSeguro {

    private Person pessoa = null;

    public ServicoSeguroAlterarPessoa(ServicoAutorizacao servicoAutorizacaoAlterarPessoa, Person pessoa) {
        super(servicoAutorizacaoAlterarPessoa);
        this.pessoa = pessoa;
    }

    public void execute() throws NotExecuteException {
        IPessoaPersistente iPessoaPersistente = SuportePersistente.getInstance().iPessoaPersistente();
        if (!iPessoaPersistente.alterarPessoa(pessoa))
            throw new NotExecuteException();
    }

    public Person getPessoa() {
        return pessoa;
    }
}