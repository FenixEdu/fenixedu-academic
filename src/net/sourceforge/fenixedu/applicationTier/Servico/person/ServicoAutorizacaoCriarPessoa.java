package net.sourceforge.fenixedu.applicationTier.Servico.person;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.applicationTier.ServicoAutorizacao;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizeException;
import net.sourceforge.fenixedu.persistenceTierJDBC.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTierJDBC.SuportePersistente;

public class ServicoAutorizacaoCriarPessoa extends ServicoAutorizacao {

    private Person pessoa;

    public ServicoAutorizacaoCriarPessoa(Person pessoa) {
        this.pessoa = pessoa;
    }

    public void execute() throws NotAuthorizeException {
        IPessoaPersistente iPessoaPersistente = SuportePersistente.getInstance().iPessoaPersistente();
        if (iPessoaPersistente.lerPessoa(pessoa.getUsername()) != null)
            throw new NotAuthorizeException();
    }

    public Person getPessoa() {
        return pessoa;
    }
}