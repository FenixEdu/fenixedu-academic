package ServidorPersistenteJDBC;

import java.util.List;

import Dominio.Pessoa;

/**
 * @author Ivo Brandão
 */
public interface IPessoaPersistente {
    public boolean alterarPessoa(Pessoa pessoa);

    public boolean apagarPessoa(int codigoInterno);

    public boolean escreverPessoa(Pessoa pessoa);

    public boolean escreverPapelPessoa(Pessoa pessoa, int chaveRole);

    public List lerCargos(int chavePessoa);

    public List lerPapelPessoa(int codigoInterno);

    public Pessoa lerPessoa(int codigoInterno);

    public Pessoa lerPessoa(String username);

    public Pessoa lerPessoa(String numeroDocumentoIdentificacao, int tipoDocumentoIdentificacao);

    public List lerPessoasPorCargo(String string);

    public List lerTodasPessoas();

    public int ultimoCodigoInterno();

    public List lerTodosCargos();
}