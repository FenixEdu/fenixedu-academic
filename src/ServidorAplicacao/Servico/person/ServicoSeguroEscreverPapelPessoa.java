package ServidorAplicacao.Servico.person;

import Dominio.Person;
import ServidorAplicacao.ServicoAutorizacao;
import ServidorAplicacao.ServicoSeguro;
import ServidorAplicacao.Servico.exceptions.NotExecuteException;
import ServidorPersistenteJDBC.IFuncionarioPersistente;
import ServidorPersistenteJDBC.IPessoaPersistente;
import ServidorPersistenteJDBC.SuportePersistente;

public class ServicoSeguroEscreverPapelPessoa extends ServicoSeguro {

    private Person pessoa = null;

    public ServicoSeguroEscreverPapelPessoa(ServicoAutorizacao servicoAutorizacao, Person pessoa) {
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

    public Person getPessoa() {
        return pessoa;
    }
}