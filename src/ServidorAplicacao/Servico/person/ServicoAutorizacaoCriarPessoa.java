package ServidorAplicacao.Servico.person;

import Dominio.Pessoa;
import ServidorAplicacao.ServicoAutorizacao;
import ServidorAplicacao.Servico.exceptions.NotAuthorizeException;
import ServidorPersistenteJDBC.IPessoaPersistente;
import ServidorPersistenteJDBC.SuportePersistente;

public class ServicoAutorizacaoCriarPessoa extends ServicoAutorizacao {

    private Pessoa pessoa;

    public ServicoAutorizacaoCriarPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public void execute() throws NotAuthorizeException {
        IPessoaPersistente iPessoaPersistente = SuportePersistente.getInstance().iPessoaPersistente();
        if (iPessoaPersistente.lerPessoa(pessoa.getUsername()) != null)
            throw new NotAuthorizeException();
    }

    public Pessoa getPessoa() {
        return pessoa;
    }
}