package net.sourceforge.fenixedu.applicationTier.Servico.assiduousness;

import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.ServicoAutorizacao;
import net.sourceforge.fenixedu.applicationTier.ServicoSeguro;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotExecuteException;
import net.sourceforge.fenixedu.persistenceTierJDBC.IFuncionarioPersistente;
import net.sourceforge.fenixedu.persistenceTierJDBC.SuportePersistente;

/**
 * 
 * @author Fernanda Quitério & Tania Pousão
 */
public class ServicoSeguroLerTodosFuncionarios extends ServicoSeguro {
    private List _listaFuncionarios = null;

    public ServicoSeguroLerTodosFuncionarios(ServicoAutorizacao servicoAutorizacaoLer) {
        super(servicoAutorizacaoLer);
    }

    public void execute() throws NotExecuteException {
        IFuncionarioPersistente iFuncionarioPersistente = SuportePersistente.getInstance()
                .iFuncionarioPersistente();
        if ((_listaFuncionarios = iFuncionarioPersistente.lerTodosFuncionarios(Calendar.getInstance()
                .getTime())) == null) {
            throw new NotExecuteException("error.funcionario.naoExistem");
        }
    }

    public List getListaFuncionarios() {
        return _listaFuncionarios;
    }
}