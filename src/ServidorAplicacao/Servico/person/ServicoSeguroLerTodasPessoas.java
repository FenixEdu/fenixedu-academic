package ServidorAplicacao.Servico.person;

import java.util.List;

import ServidorAplicacao.ServicoAutorizacao;
import ServidorAplicacao.ServicoSeguro;
import ServidorAplicacao.Servico.exceptions.NotExecuteException;
import ServidorPersistenteJDBC.IPessoaPersistente;
import ServidorPersistenteJDBC.SuportePersistente;

/**
 * @author Fernanda & Tânia
 *  
 */
public class ServicoSeguroLerTodasPessoas extends ServicoSeguro {
    private List listaPessoas = null;

    public ServicoSeguroLerTodasPessoas(ServicoAutorizacao servicoAutorizacaoLer) {
        super(servicoAutorizacaoLer);
    }

    public void execute() throws NotExecuteException {
        IPessoaPersistente iPessoaPersistente = SuportePersistente.getInstance().iPessoaPersistente();

        if ((listaPessoas = iPessoaPersistente.lerTodasPessoas()) == null) {
            throw new NotExecuteException("error.pessoa.naoExiste");
        }
    }

    public List getListaPessoas() {
        return listaPessoas;
    }
}