package ServidorPersistenteJDBC.Relacional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import Dominio.Funcionario;
import ServidorPersistenteJDBC.IFuncionarioPersistente;
import ServidorPersistenteJDBC.SuportePersistenteOracle;
import constants.assiduousness.Constants;

/**
 * 
 * @author Nanda & Tânia
 */
public class FuncionarioRelacionalOracle implements IFuncionarioPersistente {

	public boolean alterarFuncionario(Funcionario funcionario) {

		return false;
	}

	public boolean apagarFuncionario(int numMecanografico) {
		return false;
	}

	public boolean escreverFuncionario(Funcionario funcionario) {
		return false;
	}

	public Funcionario lerFuncionario(int codigoInterno) {
		return null;
	}

	public Funcionario lerFuncionarioPorFuncNaoDocente(int chaveFuncNaoDocente) {
		return null;
	}

	public Funcionario lerFuncionarioPorNumMecanografico(int numMecanografico) {
		return null;
	}

	public Funcionario lerFuncionarioPorPessoa(int chavePessoa) {
		//	ORACLE: acede à BD do teleponto para carregar os dados dos funcionarios
		Funcionario funcionario = null;

		try {
			SuportePersistenteOracle.getInstance().iniciarTransaccao();
			try {

				PreparedStatement sql = UtilRelacional.prepararComando("SELECT * FROM ass_FUNCIONARIO WHERE chavePessoa = ?");
				sql.setInt(1, chavePessoa);
				ResultSet resultado = sql.executeQuery();
				int numMecanografico = 0;
				if (resultado.next()) {
					numMecanografico = resultado.getInt("numeroMecanografico");
					Date antiguidade = null;
					if (resultado.getString("antiguidade") != null) {
						antiguidade = java.sql.Date.valueOf(resultado.getString("antiguidade"));
					}
					Date dataInicio = null;
					if (resultado.getString("dataInicio") != null) {
						dataInicio = java.sql.Date.valueOf(resultado.getString("dataInicio"));
					}
					Date dataFim = null;
					if (resultado.getString("dataFim") != null) {
						dataFim = java.sql.Date.valueOf(resultado.getString("dataFim"));
					}
					Timestamp dataQuando = null;
					if (resultado.getString("quando") != null) {
						dataQuando = Timestamp.valueOf(resultado.getString("quando"));
					}

					funcionario =
						new Funcionario(
							resultado.getInt("codigoInterno"),
							resultado.getInt("chavePessoa"),
							resultado.getInt("numeroMecanografico"),
							resultado.getInt("chaveHorarioActual"),
							antiguidade,
							resultado.getInt("chaveFuncResponsavel"),
							resultado.getInt("chaveCCLocalTrabalho"),
							resultado.getInt("chaveCCCorrespondencia"),
							resultado.getInt("chaveCCVencimento"),
							resultado.getString("calendario"),
							resultado.getInt("chaveStatus"),
							dataInicio,
							dataFim,
							resultado.getInt("quem"),
							dataQuando);
				} else {
					sql.close();
					SuportePersistenteOracle.getInstance().cancelarTransaccao();
					return null;
				}
				sql.close();

				//status de assiduidade
				sql = UtilRelacionalOracle.prepararComando("SELECT ASS_EMPSTATUS FROM ASS_EMPREG WHERE ASS_EMPPESSOA=?");
				sql.setInt(1, numMecanografico);
				resultado = sql.executeQuery();
				int status = 0;
				if (resultado.next()) {
					status = resultado.getInt("ASS_EMPSTATUS") + 1;
					funcionario.setChaveStatus(status);
				}
				sql.close();

				//actividade do status
				sql = UtilRelacional.prepararComando("SELECT * FROM ass_STATUS WHERE codigoInterno = ?");
				sql.setInt(1, status);
				resultado = sql.executeQuery();
				boolean isStatusInactivo = false;
				if (resultado.next()) {
					if (!resultado.getString("estado").equals(Constants.ASSIDUIDADE_ACTIVO)) {
						//status actual é activo ou pendente
						isStatusInactivo = true;
					}
				}
				sql.close();

				//data fim do status activo de assiduidade
				Date dataFimHorario = null;
				if (isStatusInactivo) {
					sql =
						UtilRelacionalOracle.prepararComando(
							"SELECT ASS_HISEMP_DHFIM FROM ASS_HISEMPREG " + "WHERE EMP_NUM = ? AND ASS_HISEMP_TIPO = 11");
					sql.setInt(1, numMecanografico);
					resultado = sql.executeQuery();
					if (resultado.next()) {
						if (resultado.getString("ASS_HISEMP_DHFIM") != null) {
							dataFimHorario =
								java.sql.Date.valueOf(
									resultado.getString("ASS_HISEMP_DHFIM").substring(0, resultado.getString("ASS_HISEMP_DHFIM").indexOf(" ")));

							funcionario.setDataFim(dataFimHorario);
						}
					}
					sql.close();
				}
			} catch (Exception e) {
				SuportePersistenteOracle.getInstance().cancelarTransaccao();
				System.out.println("FuncionarioRelacionalOrcale.lerFuncionarioPorPessoa: " + e.toString());
				return null;
			}
			SuportePersistenteOracle.getInstance().confirmarTransaccao();
		} catch (Exception e) {
			System.out.println("FuncionarioRelacionalOracle.lerFuncionarioPorPessoa: " + e.toString());
			return null;
		} finally {
			return funcionario;
		}
	} /* lerFuncionarioPorPessoa */

	public ArrayList lerDadosTodosFuncionarios() {
		return null;
	}

	public ArrayList lerTodosFuncionarios() {
		return null;
	}

	public ArrayList lerTodosFuncionariosAssiduidade() {
		return null;
	}

	public ArrayList lerTodosFuncionariosNaoDocentes() {
		return null;
	}

	public ArrayList lerTodosNumerosAssiduidade() {
		return null;
	}

	public ArrayList lerTodosNumerosAssiduidade(Timestamp dataInicioConsulta) {
		return null;
	}

	public ArrayList lerTodosNumerosFuncNaoDocentes() {
		return null;
	}

	public ArrayList lerStatusAssiduidade(int numMecanografico, Timestamp dataInicio, Timestamp dataFim) {
		return null;
	}

	public int ultimoCodigoInterno() {
		return 0;
	}

	/******************************************************************************************
		* Assiduidade do Funcionario 
		*******************************************************************************************/

	public Timestamp lerFimAssiduidade(int numeroMecanografico) {
		return null;
	}

	public Timestamp lerInicioAssiduidade(int numMecanografico) {
		return null;
	}

	public boolean verificaFimAssiduidade(int numMecanografico, Date dataFimAssiduidade) {
		return false;
	}

	public boolean escreveFimAssiduidade(int numMecanografico, Date fimAssiduidade, int status) {
		return false;
	}

	/* (non-Javadoc)
	 * @see ServidorPersistente.IFuncionarioPersistente#lerFuncionariosCCLocalTrabalho(int, java.util.Date)
	 */
	public ArrayList lerFuncionariosCCLocalTrabalho(int chaveCCLocalTrabalho, Date data) {
		// TODO Auto-generated method stub
		return null;
	}
}
