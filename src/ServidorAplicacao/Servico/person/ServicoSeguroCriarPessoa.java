package ServidorAplicacao.Servico.person;

import Dominio.Pessoa;
import ServidorAplicacao.ServicoAutorizacao;
import ServidorAplicacao.ServicoSeguro;
import ServidorAplicacao.Servico.exceptions.NotExecuteException;
import ServidorPersistenteJDBC.IPessoaPersistente;
import ServidorPersistenteJDBC.SuportePersistente;

public class ServicoSeguroCriarPessoa extends ServicoSeguro {

    private Pessoa pessoa;

    public ServicoSeguroCriarPessoa(ServicoAutorizacao servicoAutorizacaoCriarPessoa, Pessoa pessoa) {
        super(servicoAutorizacaoCriarPessoa);
        this.pessoa = pessoa;
    }

    public void execute() throws NotExecuteException {
        IPessoaPersistente iPessoaPersistente = SuportePersistente.getInstance().iPessoaPersistente();
        if (!iPessoaPersistente.escreverPessoa(pessoa))
            throw new NotExecuteException();
    }
}