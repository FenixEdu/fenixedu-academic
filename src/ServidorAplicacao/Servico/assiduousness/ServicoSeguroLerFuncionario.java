package ServidorAplicacao.Servico.assiduousness;

import java.util.Calendar;

import Dominio.Funcionario;
import ServidorAplicacao.ServicoAutorizacao;
import ServidorAplicacao.ServicoSeguro;
import ServidorAplicacao.Servico.exceptions.NotExecuteException;
import ServidorPersistenteJDBC.IFuncionarioPersistente;
import ServidorPersistenteJDBC.SuportePersistente;

/**
 * 
 * @author Fernanda Quitério & Tania Pousão
 */
public class ServicoSeguroLerFuncionario extends ServicoSeguro {

    private Funcionario _funcionario = null;

    private int _chavePessoa;

    public ServicoSeguroLerFuncionario(ServicoAutorizacao servicoAutorizacaoLerFuncionario,
            int chavePessoa) {
        super(servicoAutorizacaoLerFuncionario);
        _chavePessoa = chavePessoa;
    }

    public void execute() throws NotExecuteException {
        IFuncionarioPersistente iFuncionarioPersistente = SuportePersistente.getInstance()
                .iFuncionarioPersistente();
        if ((_funcionario = iFuncionarioPersistente.lerFuncionarioPorPessoa(_chavePessoa, Calendar
                .getInstance().getTime())) == null) {
            throw new NotExecuteException("error.funcionario.naoExiste");
        }
    }

    public Funcionario getFuncionario() {
        return _funcionario;
    }
}