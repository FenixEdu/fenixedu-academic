package ServidorAplicacao.Servico.person;

import java.util.ArrayList;

import ServidorAplicacao.NotExecuteException;
import ServidorAplicacao.ServicoAutorizacao;
import ServidorAplicacao.ServicoSeguro;
import ServidorPersistenteJDBC.IPessoaPersistente;
import ServidorPersistenteJDBC.SuportePersistente;

public class ServicoSeguroLerCargos extends ServicoSeguro {
    
    private ArrayList cargos;
    
    public ServicoSeguroLerCargos(ServicoAutorizacao servicoAutorizacaoLerCargos,
    ArrayList cargos) {
        super(servicoAutorizacaoLerCargos);
        this.cargos = cargos;
    }
    
    public void execute() throws NotExecuteException {
IPessoaPersistente iPessoaPersistente = SuportePersistente.getInstance().iPessoaPersistente();

		cargos = iPessoaPersistente.lerTodosCargos();
    }
    
    public ArrayList getCargos() {
        return cargos;
    }
}