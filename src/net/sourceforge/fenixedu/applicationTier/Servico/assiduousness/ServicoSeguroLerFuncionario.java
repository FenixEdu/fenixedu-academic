package net.sourceforge.fenixedu.applicationTier.Servico.assiduousness;

import java.util.Calendar;

import net.sourceforge.fenixedu.domain.Funcionario;
import net.sourceforge.fenixedu.applicationTier.ServicoAutorizacao;
import net.sourceforge.fenixedu.applicationTier.ServicoSeguro;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotExecuteException;
import net.sourceforge.fenixedu.persistenceTierJDBC.IFuncionarioPersistente;
import net.sourceforge.fenixedu.persistenceTierJDBC.SuportePersistente;

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