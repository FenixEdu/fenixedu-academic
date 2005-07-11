package net.sourceforge.fenixedu.persistenceTierJDBC;

import java.util.List;

import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.Person;

/**
 * @author Ivo Brandão
 */
public interface IPessoaPersistente {
    public boolean alterarPessoa(Person pessoa);

    public boolean apagarPessoa(int codigoInterno);

    public boolean escreverPessoa(Person pessoa);

    public boolean escreverPapelPessoa(IPerson pessoa, int chaveRole);

    public List lerCargos(int chavePessoa);

    public List lerPapelPessoa(int codigoInterno);

    public Person lerPessoa(int codigoInterno);

    public Person lerPessoa(String username);

    public Person lerPessoa(String numeroDocumentoIdentificacao, int tipoDocumentoIdentificacao);

    public List lerPessoasPorCargo(String string);

    public List lerTodasPessoas();

    public int ultimoCodigoInterno();

    public List lerTodosCargos();
}