package ServidorPersistenteJDBC;

import java.util.ArrayList;

import Dominio.Pessoa;

/**
 * @author  Ivo Brandão
 */
public interface IPessoaPersistente { 
	public boolean alterarPessoa(Pessoa pessoa);
	
	public boolean apagarPessoa(int codigoInterno);
	
	public boolean escreverPessoa(Pessoa pessoa);
	public boolean escreverPapelPessoa(Pessoa pessoa, int chaveRole);	
	
	public ArrayList lerCargos(int chavePessoa);
	
	public ArrayList lerPapelPessoa(int codigoInterno);
	public Pessoa lerPessoa(int codigoInterno);
	public Pessoa lerPessoa(String username);
	public Pessoa lerPessoa(String numeroDocumentoIdentificacao, int tipoDocumentoIdentificacao);	
	public ArrayList lerPessoasPorCargo(String string);
	public ArrayList lerTodasPessoas();
	public int ultimoCodigoInterno();
  
	public ArrayList lerTodosCargos();
}