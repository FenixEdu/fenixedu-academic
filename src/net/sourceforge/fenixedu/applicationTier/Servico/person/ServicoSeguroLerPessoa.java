package net.sourceforge.fenixedu.applicationTier.Servico.person;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.applicationTier.ServicoAutorizacao;
import net.sourceforge.fenixedu.applicationTier.ServicoSeguro;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotExecuteException;
import net.sourceforge.fenixedu.persistenceTierJDBC.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTierJDBC.SuportePersistente;

public class ServicoSeguroLerPessoa extends ServicoSeguro {

    private Person pessoa = null;

    private String username;

    public ServicoSeguroLerPessoa(ServicoAutorizacao servicoAutorizacaoLogin, String username) {
        super(servicoAutorizacaoLogin);
        this.username = username;
    }

    public void execute() throws NotExecuteException {
        IPessoaPersistente iPessoaPersistente = SuportePersistente.getInstance().iPessoaPersistente();
        if ((pessoa = iPessoaPersistente.lerPessoa(username)) == null)
            throw new NotExecuteException();
    }

    public Person getPessoa() {
        return pessoa;
    }
}