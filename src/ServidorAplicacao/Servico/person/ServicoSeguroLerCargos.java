package ServidorAplicacao.Servico.person;

import java.util.List;

import ServidorAplicacao.ServicoAutorizacao;
import ServidorAplicacao.ServicoSeguro;
import ServidorAplicacao.Servico.exceptions.NotExecuteException;
import ServidorPersistenteJDBC.IPessoaPersistente;
import ServidorPersistenteJDBC.SuportePersistente;

public class ServicoSeguroLerCargos extends ServicoSeguro {

    private List cargos;

    public ServicoSeguroLerCargos(ServicoAutorizacao servicoAutorizacaoLerCargos, List cargos) {
        super(servicoAutorizacaoLerCargos);
        this.cargos = cargos;
    }

    public void execute() throws NotExecuteException {
        IPessoaPersistente iPessoaPersistente = SuportePersistente.getInstance().iPessoaPersistente();

        cargos = iPessoaPersistente.lerTodosCargos();
    }

    public List getCargos() {
        return cargos;
    }
}