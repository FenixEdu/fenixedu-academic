package ServidorAplicacao.Servico.person;

import Dominio.Person;
import ServidorAplicacao.ServicoAutorizacao;
import ServidorAplicacao.ServicoSeguro;
import ServidorAplicacao.Servico.exceptions.NotExecuteException;
import ServidorPersistenteJDBC.IPessoaPersistente;
import ServidorPersistenteJDBC.SuportePersistente;

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