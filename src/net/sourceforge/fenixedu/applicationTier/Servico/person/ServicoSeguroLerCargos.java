package net.sourceforge.fenixedu.applicationTier.Servico.person;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.ServicoAutorizacao;
import net.sourceforge.fenixedu.applicationTier.ServicoSeguro;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotExecuteException;
import net.sourceforge.fenixedu.persistenceTierJDBC.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTierJDBC.SuportePersistente;

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