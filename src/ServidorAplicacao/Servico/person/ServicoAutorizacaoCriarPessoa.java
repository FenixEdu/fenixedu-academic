package ServidorAplicacao.Servico.person;

import Dominio.Person;
import ServidorAplicacao.ServicoAutorizacao;
import ServidorAplicacao.Servico.exceptions.NotAuthorizeException;
import ServidorPersistenteJDBC.IPessoaPersistente;
import ServidorPersistenteJDBC.SuportePersistente;

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