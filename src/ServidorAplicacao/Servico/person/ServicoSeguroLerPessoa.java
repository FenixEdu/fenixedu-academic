package ServidorAplicacao.Servico.person;

import Dominio.Person;
import ServidorAplicacao.ServicoAutorizacao;
import ServidorAplicacao.ServicoSeguro;
import ServidorAplicacao.Servico.exceptions.NotExecuteException;
import ServidorPersistenteJDBC.IPessoaPersistente;
import ServidorPersistenteJDBC.SuportePersistente;

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