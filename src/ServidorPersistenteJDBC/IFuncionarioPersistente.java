package ServidorPersistenteJDBC;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import Dominio.Funcionario;

/**
 *
 * @author  Fernanda Quitério e Tania Pousão
 */
public interface IFuncionarioPersistente {
	public boolean alterarFuncionario(Funcionario funcionario);
	public boolean apagarFuncionario(int numMecanografico);
	public boolean escreverFuncionario(Funcionario funcionario);
	public Funcionario lerFuncionario(int codigoInterno);
	public Funcionario lerFuncionarioPorFuncNaoDocente(int chaveFuncNaoDocente);
	public Funcionario lerFuncionarioPorNumMecanografico(int numMecanografico);
	public Funcionario lerFuncionarioPorPessoa(int chavePessoa);
	public ArrayList lerStatusAssiduidade(int numMecanografico, Timestamp dataInicio, Timestamp dataFim);
	public ArrayList lerTodosFuncionarios();
	public ArrayList lerTodosFuncionariosAssiduidade();
	public ArrayList lerTodosFuncionariosNaoDocentes();
	public ArrayList lerTodosNumerosAssiduidade();
	public ArrayList lerTodosNumerosAssiduidade(Timestamp dataInicioConsulta);
	public ArrayList lerTodosNumerosFuncNaoDocentes();
	public int ultimoCodigoInterno();

	/******************************************************************************************
	* Assiduidade do Funcionario 
	*******************************************************************************************/
	public boolean escreveFimAssiduidade(int numMecanografico, Date fimAssiduidade, int status);
	public Date lerFimAssiduidade(int numeroMecanografico);
	public Date lerInicioAssiduidade(int numMecanografico);
	public boolean verificaFimAssiduidade(int numMecanografico, Date dataFimAssiduidade);
}