package net.sourceforge.fenixedu.applicationTier.Servico.person;

import net.sourceforge.fenixedu.applicationTier.ServicoAutorizacao;
import net.sourceforge.fenixedu.applicationTier.ServicoSeguro;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotExecuteException;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.persistenceTierJDBC.IFuncionarioPersistente;
import net.sourceforge.fenixedu.persistenceTierJDBC.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTierJDBC.SuportePersistente;

public class ServicoSeguroEscreverPapelPessoa extends ServicoSeguro {

    private IPerson pessoa = null;

    public ServicoSeguroEscreverPapelPessoa(ServicoAutorizacao servicoAutorizacao, IPerson pessoa) {
        super(servicoAutorizacao);
        this.pessoa = pessoa;
    }

    public void execute() throws NotExecuteException {
        IPessoaPersistente iPessoaPersistente = SuportePersistente.getInstance().iPessoaPersistente();
        // pessoa com papel de pessoa
        if (!iPessoaPersistente.escreverPapelPessoa(pessoa, 1)) { // buscar
            // estas
            // chaves da
            // base de
            // dados
            throw new NotExecuteException();
        }

        // pessoa com papel de assiduidade, caso seja funcionario
        IFuncionarioPersistente iFuncionarioPersistente = SuportePersistente.getInstance()
                .iFuncionarioPersistente();
        if (iFuncionarioPersistente.lerFuncionarioSemHistoricoPorPessoa(pessoa.getIdInternal()
                .intValue()) != null) {
            if (!iPessoaPersistente.escreverPapelPessoa(pessoa, 9)) { // buscar
                // estas
                // chaves
                // da base
                // de
                // dados
                throw new NotExecuteException();
            }
        }

    }

    public IPerson getPessoa() {
        return pessoa;
    }
}