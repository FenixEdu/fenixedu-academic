package ServidorPersistenteJDBC;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Dominio.Funcionario;

/**
 *
 * @author  Fernanda Quitério e Tania Pousão
 */
public interface IFuncionarioPersistente {
	//modificações
	public boolean alterarFuncionario(Funcionario funcionario);
	public boolean alterarFuncionarioPorCodigoInterno(Funcionario funcionario);
	public boolean alterarFuncResponsavel(Funcionario funcionario);
	public boolean alterarCCLocalTrabalhoFuncionario(Funcionario funcionario);
	public boolean alterarCCCorrespondenciaFuncionario(Funcionario funcionario);
	public boolean alterarCCVencimento(Funcionario funcionario);
	public boolean alterarCalendario(Funcionario funcionario);
	public boolean alterarStatusAssiduidade(Funcionario funcionario);
	public boolean alterarFimValidade(int codigoInterno, Date dataFimValidade);

	public boolean apagarFuncionario(int numMecanografico);

	//escritas
	public boolean escreverFuncionario(Funcionario funcionario);
	public boolean escreverHistoricoFuncionario(Funcionario funcionario);
	public boolean escreverFimFuncResponsavel(int numeroMecanografico, Date fim);
	public boolean escreverFuncResponsavel(Funcionario funcionario);
	public boolean escreverFimCCLocalTrabalho(int numeroMecanografico, Date fim);
	public boolean escreverCCLocalTrabalho(Funcionario funcionario);
	public boolean escreverFimCCCorrespondencia(int numeroMecanografico, Date fim);
	public boolean escreverCCCorrespondencia(Funcionario funcionario);
	public boolean escreverFimCCVencimento(int numeroMecanografico, Date fim);
	public boolean escreverCCVencimento(Funcionario funcionario);
	public boolean escreverFimCalendario(int numeroMecanografico, Date fim);
	public boolean escreverCalendario(Funcionario funcionario);
	public boolean escreverFimStatusAssiduidade(int numeroMecanografico, Date fim);
	public boolean escreverStatusAssiduidade(Funcionario funcionario);

	public boolean existeHistoricoFuncResponsavel(Funcionario funcionario);
	public boolean existeHistoricoCCLocalTrabalho(Funcionario funcionario);
	public boolean existeHistoricoCCCorrespondencia(Funcionario funcionario);
	public boolean existeHistoricoCCVencimento(Funcionario funcionario);
	public boolean existeHistoricoCalendario(Funcionario funcionario);
	public boolean existeHistoricoStatusAssiduidade(Funcionario funcionario);

	//leituras sem histórico		
	public Funcionario lerFuncionarioSemHistorico(int codigoInterno);
	public Funcionario lerFuncionarioSemHistoricoPorNumMecanografico(int numMecanografico);
	public Funcionario lerFuncionarioSemHistoricoPorPessoa(int chavePessoa);

	//leituras com histórico
	public Funcionario lerFuncionario(int codigoInterno, Date dataConsulta);
	public Funcionario lerFuncionarioPorFuncNaoDocente(int chaveFuncNaoDocente, Date dataConsulta);
	public Funcionario lerFuncionarioPorNumMecanografico(int numMecanografico, Date dataConsulta);
	public Funcionario lerFuncionarioPorPessoa(int chavePessoa, Date dataConsulta);

	//ler históricos 
	public Funcionario lerHistorico(int codigoInterno);
	public List lerHistoricoFuncionario(int codigoInterno, Date dataInicioConsulta);
	public ArrayList lerStatusAssiduidade(int numMecanografico, Timestamp dataInicio, Timestamp dataFim);

	//ler listas ade funcionários
	public ArrayList lerFuncionariosCCLocalTrabalho(int chaveCCLocalTrabalho, Date data);
	public ArrayList lerTodosFuncionarios(Date dataConsulta);
	public ArrayList lerTodosFuncionariosAssiduidade(Date dataConsulta);
	public ArrayList lerTodosFuncionariosNaoDocentes(Date dataConsulta);

	public ArrayList lerTodosNumeros();
	public ArrayList lerTodosNumerosAssiduidade();
	public ArrayList lerTodosNumerosAssiduidade(Timestamp dataInicioConsulta);
	public ArrayList lerTodosNumerosFuncNaoDocentes();

	//verifica sobreposições de histórico
	public boolean sobreposicaoHistoricos(int codigoInterno, Date dataInicio, Date dataFim);
	public boolean sobreposicaoFuncResponsavel(int codigoInterno, Date dataInicio, Date dataFim);
	public boolean sobreposicaoCCLocalTrabalho(int codigoInterno, Date dataInicio, Date dataFim);
	public boolean sobreposicaoCCCorrespondencia(int codigoInterno, Date dataInicio, Date dataFim);
	public boolean sobreposicaoCCVencimento(int codigoInterno, Date dataInicio, Date dataFim);
	public boolean sobreposicaoCalendario(int codigoInterno, Date dataInicio, Date dataFim);
	public boolean sobreposicaoStatusAssiduidade(int codigoInterno, Date dataInicio, Date dataFim);

	//verifica se tem histórico
	public boolean temHistorico(int numeroMecanografico);
	public boolean temHistoricoFuncResponsavel(int numeroMecanografico);
	public boolean temHistoricoCCLocalTrabalho(int numeroMecanografico);
	public boolean temHistoricoCCCorrespondencia(int numeroMecanografico);
	public boolean temHistoricoCCVencimento(int numeroMecanografico);
	public boolean temHistoricoCalendario(int numeroMecanografico);
	public boolean temHistoricoStatusAssiduidade(int numeroMecanografico);

	public int ultimoCodigoInterno();

	/******************************************************************************************
	* Assiduidade do Funcionario 
	*******************************************************************************************/
	public boolean escreverFimAssiduidade(int numMecanografico, Date fimAssiduidade);
	public Timestamp lerFimAssiduidade(int numeroMecanografico);
	public Timestamp lerInicioAssiduidade(int numMecanografico);
	public boolean verificaFimAssiduidade(int numMecanografico, Date dataFimAssiduidade);
}