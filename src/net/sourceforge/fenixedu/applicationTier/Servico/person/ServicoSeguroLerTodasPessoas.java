package net.sourceforge.fenixedu.applicationTier.Servico.person;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.ServicoAutorizacao;
import net.sourceforge.fenixedu.applicationTier.ServicoSeguro;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotExecuteException;
import net.sourceforge.fenixedu.persistenceTierJDBC.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTierJDBC.SuportePersistente;

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