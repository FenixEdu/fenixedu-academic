package net.sourceforge.fenixedu.applicationTier.Servico.person;

import net.sourceforge.fenixedu.applicationTier.ServicoAutorizacao;
import net.sourceforge.fenixedu.applicationTier.ServicoSeguro;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotExecuteException;
import net.sourceforge.fenixedu.domain.Funcionario;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.persistenceTierJDBC.IFuncionarioPersistente;
import net.sourceforge.fenixedu.persistenceTierJDBC.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTierJDBC.SuportePersistente;

/**
 * @author Fernanda & Tânia
 *  
 */
public class ServicoSeguroLerPessoaPorNumeroMecanografico extends ServicoSeguro {
    private Person pessoa = null;

    private int numeroMecanografico;

    public ServicoSeguroLerPessoaPorNumeroMecanografico(ServicoAutorizacao servicoAutorizacao,
            int numeroMecanografico) {
        super(servicoAutorizacao);
        this.numeroMecanografico = numeroMecanografico;
    }

    public void execute() throws NotExecuteException {
        IFuncionarioPersistente iFuncionarioPersistente = SuportePersistente.getInstance()
                .iFuncionarioPersistente();
        Funcionario funcionario = null;
        if ((funcionario = iFuncionarioPersistente
                .lerFuncionarioSemHistoricoPorNumMecanografico(numeroMecanografico)) == null)
            throw new NotExecuteException();

        IPessoaPersistente iPessoaPersistente = SuportePersistente.getInstance().iPessoaPersistente();
        if ((pessoa = iPessoaPersistente.lerPessoa(funcionario.getChavePessoa())) == null)
            throw new NotExecuteException();
    }

    public Person getPessoa() {
        return pessoa;
    }
}