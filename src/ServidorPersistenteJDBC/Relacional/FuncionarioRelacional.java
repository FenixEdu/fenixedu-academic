package ServidorPersistenteJDBC.Relacional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import Dominio.Funcionario;
import Dominio.StatusAssiduidade;
import ServidorPersistenteJDBC.IFuncionarioPersistente;

/**
 *
 * @author  Fernanda Quitério e Tania Pousão
 */
public class FuncionarioRelacional implements IFuncionarioPersistente {

	public boolean alterarFuncionario(Funcionario funcionario) {
		boolean resultado = false;

		try {
			PreparedStatement sql =
				UtilRelacional.prepararComando(
					"UPDATE ass_FUNCIONARIO SET "
						+ "codigoInterno = ? , "
						+ "chavePessoa = ? , "
						+ "numeroMecanografico = ? , "
						+ "chaveHorarioActual = ?, "
						+ "antiguidade = ? ,"
						+ "chaveFuncResponsavel = ? ,"
						+ "chaveCCLocalTrabalho = ? ,"
						+ "chaveCCCorrespondencia = ? ,"
						+ "chaveCCVencimento = ? ,"
						+ "calendario = ? ,"
						+ "chaveStatus = ? ,"
						+ "dataInicio = ? ,"
						+ "dataFim = ? ,"
						+ "quem = ? ,"
						+ "quando = ?"
						+ "WHERE numeroMecanografico = ? ");

			sql.setInt(1, funcionario.getCodigoInterno());
			sql.setInt(2, funcionario.getChavePessoa());
			sql.setInt(3, funcionario.getNumeroMecanografico());
			sql.setInt(4, funcionario.getChaveHorarioActual());
			sql.setDate(5, new java.sql.Date(funcionario.getAntiguidade().getTime()));
			sql.setInt(6, funcionario.getChaveFuncResponsavel());
			sql.setInt(7, funcionario.getChaveCCLocalTrabalho());
			sql.setInt(8, funcionario.getChaveCCCorrespondencia());
			sql.setInt(9, funcionario.getChaveCCVencimento());
			sql.setString(10, funcionario.getCalendario());
			sql.setInt(11, funcionario.getChaveStatus());
			sql.setDate(12, new java.sql.Date(funcionario.getDataInicio().getTime()));
			sql.setDate(13, new java.sql.Date(funcionario.getDataFim().getTime()));
			sql.setInt(14, funcionario.getQuem());
			sql.setTimestamp(15, new Timestamp(funcionario.getQuando().getTime()));
			sql.setInt(16, funcionario.getNumeroMecanografico());

			sql.executeUpdate();
			sql.close();
			resultado = true;
		} catch (Exception e) {
			System.out.println("FuncionarioRelacional.alterarFuncionario: " + e.toString());
		} finally {
			return resultado;
		}
	} /* alterarFuncionario */

	public boolean apagarFuncionario(int numero) {
		boolean resultado = false;

		try {
			PreparedStatement sql = UtilRelacional.prepararComando("DELETE FROM ass_FUNCIONARIO WHERE numeroMecanografico = ?");

			sql.setInt(1, numero);

			sql.executeUpdate();
			sql.close();
			resultado = true;
		} catch (Exception e) {
			System.out.println("FuncionarioRelacional.apagarFuncionario: " + e.toString());
		} finally {
			return resultado;
		}
	} /* apagarFuncionario */

	public boolean escreverFuncionario(Funcionario funcionario) {
		boolean resultado = false;

		try {
			PreparedStatement sql =
				UtilRelacional.prepararComando("INSERT INTO ass_FUNCIONARIO " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

			sql.setInt(1, funcionario.getCodigoInterno());
			sql.setInt(2, funcionario.getChavePessoa());
			sql.setInt(3, funcionario.getNumeroMecanografico());
			sql.setInt(4, funcionario.getChaveHorarioActual());
			sql.setDate(5, new java.sql.Date(funcionario.getAntiguidade().getTime()));
			sql.setInt(6, funcionario.getChaveFuncResponsavel());
			sql.setInt(7, funcionario.getChaveCCLocalTrabalho());
			sql.setInt(8, funcionario.getChaveCCCorrespondencia());
			sql.setInt(9, funcionario.getChaveCCVencimento());
			sql.setString(10, funcionario.getCalendario());
			sql.setInt(11, funcionario.getChaveStatus());
			sql.setDate(12, new java.sql.Date(funcionario.getDataInicio().getTime()));
			sql.setDate(13, new java.sql.Date(funcionario.getDataFim().getTime()));
			sql.setInt(14, funcionario.getQuem());
			sql.setTimestamp(15, new Timestamp(funcionario.getQuando().getTime()));

			sql.executeUpdate();
			sql.close();
			resultado = true;
		} catch (Exception e) {
			System.out.println("FuncionarioRelacional.escreverFuncionario: " + e.toString());
		} finally {
			return resultado;
		}
	} /* escreverFuncionario */

	public Funcionario lerFuncionario(int codigoInterno) {
		Funcionario funcionario = null;

		try {
			PreparedStatement sql = UtilRelacional.prepararComando("SELECT * FROM ass_FUNCIONARIO WHERE codigoInterno = ?");

			sql.setInt(1, codigoInterno);

			ResultSet resultado = sql.executeQuery();
			if (resultado.next()) {
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
			}
			sql.close();
		} catch (Exception e) {
			System.out.println("FuncionarioRelacional.lerFuncionario: " + e.toString());
		} finally {
			return funcionario;
		}
	} /* lerFuncionario */

	public Funcionario lerFuncionarioPorFuncNaoDocente(int chaveFuncNaoDocente) {
		Funcionario funcionario = null;

		try {
			PreparedStatement sql = UtilRelacional.prepararComando("SELECT chaveFuncionario FROM ass_FUNC_NAO_DOCENTE WHERE codigoInterno = ?");
			sql.setInt(1, chaveFuncNaoDocente);
			ResultSet resultado = sql.executeQuery();
			int chaveFuncionario = 0;
			if (resultado.next()) {
				chaveFuncionario = resultado.getInt("chaveFuncionario");
			} else {
				sql.close();
				return null;
			}
			sql.close();

			sql = UtilRelacional.prepararComando("SELECT * FROM ass_FUNCIONARIO WHERE codigoInterno = ?");
			sql.setInt(1, chaveFuncionario);

			resultado = sql.executeQuery();
			if (resultado.next()) {
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
			}
			sql.close();
		} catch (Exception e) {
			System.out.println("FuncionarioRelacional.lerFuncionarioPorFuncNaoDocente: " + e.toString());
		} finally {
			return funcionario;
		}
	} /* lerFuncionarioPorFuncNaoDocente */

	public Funcionario lerFuncionarioPorNumMecanografico(int numero) {
		Funcionario funcionario = null;
		try {
			PreparedStatement sql = UtilRelacional.prepararComando("SELECT * FROM ass_FUNCIONARIO WHERE numeroMecanografico = ?");

			sql.setInt(1, numero);
			ResultSet resultado = sql.executeQuery();
			if (resultado.next()) {
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
				return null;
			}
			sql.close();
		} catch (Exception e) {
			System.out.println("FuncionarioRelacional.lerFuncionarioPorNumMecanografico: " + e.toString());
		} finally {
			return funcionario;
		}
	} /* lerFuncionarioPorNumMecanografico */

	public Funcionario lerFuncionarioPorPessoa(int chavePessoa) {
		Funcionario funcionario = null;

		try {
			PreparedStatement sql = UtilRelacional.prepararComando("SELECT * FROM ass_FUNCIONARIO WHERE chavePessoa = ?");

			sql.setInt(1, chavePessoa);

			ResultSet resultado = sql.executeQuery();
			if (resultado.next()) {
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
			}
			sql.close();
		} catch (Exception e) {
			System.out.println("FuncionarioRelacional.lerFuncionarioPorPessoa: " + e.toString());
		} finally {
			return funcionario;
		}
	} /* lerFuncionarioPorPessoa */

		public ArrayList lerStatusAssiduidade(int numMecanografico, Timestamp dataInicial, Timestamp dataFinal) {
			ArrayList listaStatusAssiduidade = null;

			try {
				PreparedStatement sql = UtilRelacional.prepararComando("SELECT chaveStatus FROM ass_FUNCIONARIO where numeroMecanografico = ?");
				sql.setInt(1, numMecanografico);
				ResultSet resultado = sql.executeQuery();

				PreparedStatement sql2 = UtilRelacional.prepararComando("SELECT * FROM ass_STATUS where codigoInterno = ?");
				ResultSet resultado2 = null;

				listaStatusAssiduidade = new ArrayList();
				while (resultado.next()) {
					sql2.setInt(1, resultado.getInt("chaveStatus"));
					resultado2 = sql2.executeQuery();

					if (resultado2.next()) {
						listaStatusAssiduidade.add(
							new StatusAssiduidade(
								resultado2.getInt("codigoInterno"),
								resultado2.getString("sigla"),
								resultado2.getString("designacao"),
								resultado2.getString("estado"),
								resultado2.getString("assiduidade")));
					}
				}
				sql2.close();
				sql.close();
			
			} catch (Exception e) {
				System.out.println("FuncionarioRelacional.lerStatusAssiduidade: " + e.toString());
				return null;
			} finally {
				return listaStatusAssiduidade;
			}
		} /* lerStatusAssiduidade */

	public ArrayList lerTodosFuncionarios() {
		ArrayList listaFuncionarios = null;

		try {
			PreparedStatement sql =
				UtilRelacional.prepararComando("SELECT * FROM ass_FUNCIONARIO ORDER BY numeroMecanografico");

			ResultSet resultado = sql.executeQuery();
			listaFuncionarios = new ArrayList();
			Date antiguidade = null;
			Date dataInicio = null;
			Date dataFim = null;
			Timestamp dataQuando = null;
			while (resultado.next()) {
				if (resultado.getString("antiguidade") != null) {
					antiguidade = java.sql.Date.valueOf(resultado.getString("antiguidade"));
				}
				if (resultado.getString("dataInicio") != null) {
					dataInicio = java.sql.Date.valueOf(resultado.getString("dataInicio"));
				}
				if (resultado.getString("dataFim") != null) {
					dataFim = java.sql.Date.valueOf(resultado.getString("dataFim"));
				}
				if (resultado.getString("quando") != null) {
					dataQuando = Timestamp.valueOf(resultado.getString("quando"));
				}

				listaFuncionarios.add(
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
						dataQuando));
			}
			sql.close();
		} catch (Exception e) {
			System.out.println("FuncionarioRelacional.lerTodosFuncionarios: " + e.toString());
			return null;
		} finally {
			return listaFuncionarios;
		}
	} /* lerTodosFuncionarios */

	public ArrayList lerTodosFuncionariosAssiduidade() {
		ArrayList listaFuncionarios = null;

		try {
			PreparedStatement sql =
				UtilRelacional.prepararComando("SELECT * FROM ass_FUNCIONARIO where chaveHorarioActual <> 0 ORDER BY numeroMecanografico");
			ResultSet resultado = sql.executeQuery();

			listaFuncionarios = new ArrayList();
			Date antiguidade = null;
			Date dataInicio = null;
			Date dataFim = null;
			Timestamp dataQuando = null;
			while (resultado.next()) {
				if (resultado.getString("antiguidade") != null) {
					antiguidade = java.sql.Date.valueOf(resultado.getString("antiguidade"));
				}
				if (resultado.getString("dataInicio") != null) {
					dataInicio = java.sql.Date.valueOf(resultado.getString("dataInicio"));
				}
				if (resultado.getString("dataFim") != null) {
					dataFim = java.sql.Date.valueOf(resultado.getString("dataFim"));
				}
				if (resultado.getString("quando") != null) {
					dataQuando = Timestamp.valueOf(resultado.getString("quando"));
				}

				listaFuncionarios.add(
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
						dataQuando));
			}
			sql.close();
		} catch (Exception e) {
			System.out.println("FuncionarioRelacional.lerTodosFuncionariosAssiduidade: " + e.toString());
			return null;
		} finally {
			return listaFuncionarios;
		}
	} /* lerTodosFuncionariosAssiduidade */

	public ArrayList lerTodosFuncionariosNaoDocentes() {
		ArrayList listaFuncionarios = null;

		try {
			PreparedStatement sql = UtilRelacional.prepararComando("SELECT * FROM ass_FUNCIONARIO ORDER BY numeroMecanografico");
			ResultSet resultado = sql.executeQuery();

			PreparedStatement sql2 = UtilRelacional.prepararComando("SELECT * FROM ass_FUNC_NAO_DOCENTE WHERE chaveFuncionario = ?");
			ResultSet resultado2 = null;

			listaFuncionarios = new ArrayList();
			Date antiguidade = null;
			Date dataInicio = null;
			Date dataFim = null;
			Timestamp dataQuando = null;
			while (resultado.next()) {
				sql2.setInt(1, resultado.getInt("codigoInterno"));
				resultado2 = sql2.executeQuery();

				if (resultado2.next()) {
					if (resultado.getString("antiguidade") != null) {
						antiguidade = java.sql.Date.valueOf(resultado.getString("antiguidade"));
					}
					if (resultado.getString("dataInicio") != null) {
						dataInicio = java.sql.Date.valueOf(resultado.getString("dataInicio"));
					}
					if (resultado.getString("dataFim") != null) {
						dataFim = java.sql.Date.valueOf(resultado.getString("dataFim"));
					}
					if (resultado.getString("quando") != null) {
						dataQuando = Timestamp.valueOf(resultado.getString("quando"));
					}
					//funcionario a cumprir assiduidade
					listaFuncionarios.add(
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
							dataQuando));
				}
			}
			sql2.close();
			sql.close();
		} catch (Exception e) {
			System.out.println("FuncionarioRelacional.lerTodosNumerosMecanograficos: " + e.toString());
			return null;
		} finally {
			return listaFuncionarios;
		}
	} /* lerTodosFuncionariosNaoDocentes */

	public ArrayList lerTodosNumerosAssiduidade() {
		ArrayList listaNumeros = null;

		try {
			PreparedStatement sql = UtilRelacional.prepararComando("SELECT DISTINCT chaveFuncionario FROM ass_HORARIO");
			ResultSet resultado = sql.executeQuery();

			PreparedStatement sqlChaveFuncionario =
				UtilRelacional.prepararComando("SELECT numeroMecanografico FROM ass_FUNCIONARIO " + "WHERE codigoInterno = ?");
			ResultSet resultadoChaveFuncionario = null;

			listaNumeros = new ArrayList();
			while (resultado.next()) {
				//chave do Funcionario
				sqlChaveFuncionario.setInt(1, resultado.getInt("chaveFuncionario"));
				resultadoChaveFuncionario = sqlChaveFuncionario.executeQuery();
				if (resultadoChaveFuncionario.next()) {
					listaNumeros.add(new Integer(resultadoChaveFuncionario.getInt("numeroMecanografico")));
				}
			}
			sqlChaveFuncionario.close();
			sql.close();
			/*
			PreparedStatement sql =
				UtilRelacional.prepararComando("SELECT numeroMecanografico FROM funcionario ORDER BY numeroMecanografico");
			ResultSet resultado = sql.executeQuery();

			PreparedStatement sqlChaveFuncionario =
				UtilRelacional.prepararComando("SELECT codigoInterno FROM funcionario " + "WHERE numeroMecanografico = ?");
			ResultSet resultadoChaveFuncionario = null;

			PreparedStatement sqlAssiduidade = UtilRelacional.prepararComando("SELECT * FROM horario " + "WHERE chaveFuncionario = ?");
			ResultSet resultadoAssiduidade = null;

			listaNumeros = new ArrayList();
			while (resultado.next()) {
				//chave do Funcionario
				sqlChaveFuncionario.setInt(1, resultado.getInt("numeroMecanografico"));
				resultadoChaveFuncionario = sqlChaveFuncionario.executeQuery();
				if (resultadoChaveFuncionario.next()) {
					//Assiduidade Funcionário
					sqlAssiduidade.setInt(1, resultadoChaveFuncionario.getInt("codigoInterno"));
					resultadoAssiduidade = sqlAssiduidade.executeQuery();
					if (resultadoAssiduidade.next()) {
						//Funcionario cumpre/cumpriu assiduidade
						listaNumeros.add(new Integer(resultado.getInt("numeroMecanografico")));
					}
				}
			}
			sqlChaveFuncionario.close();
			sqlAssiduidade.close();
			sql.close();
			*/
		} catch (Exception e) {
			System.out.println("FuncionarioRelacional.lerTodosNumerosAssiduidade: " + e.toString());
			e.printStackTrace();
			return null;
		} finally {
			return listaNumeros;
		}
	} /* lerTodosNumerosAssiduidade */

	public ArrayList lerTodosNumerosAssiduidade(Timestamp dataInicioConsulta) {
		ArrayList listaNumeros = null;

		try {
			PreparedStatement sql =
				UtilRelacional.prepararComando("SELECT numeroMecanografico FROM ass_FUNCIONARIO ORDER BY numeroMecanografico");
			ResultSet resultado = sql.executeQuery();

			PreparedStatement sqlChaveFuncionario =
				UtilRelacional.prepararComando("SELECT codigoInterno FROM ass_FUNCIONARIO " + "WHERE numeroMecanografico = ?");
			ResultSet resultadoChaveFuncionario = null;

			PreparedStatement sqlAssiduidade =
				UtilRelacional.prepararComando(
					"SELECT * FROM ass_HORARIO "
						+ "WHERE chaveFuncionario = ? AND "
						+ "((dataFim IS NOT NULL AND ? BETWEEN dataInicio AND dataFim) OR (dataFim IS NULL AND ? >= dataInicio))");
			sqlAssiduidade.setDate(2, new java.sql.Date(dataInicioConsulta.getTime()));
			sqlAssiduidade.setDate(3, new java.sql.Date(dataInicioConsulta.getTime()));
			ResultSet resultadoAssiduidade = null;

			listaNumeros = new ArrayList();
			while (resultado.next()) {
				//chave do Funcionario
				sqlChaveFuncionario.setInt(1, resultado.getInt("numeroMecanografico"));
				resultadoChaveFuncionario = sqlChaveFuncionario.executeQuery();
				if (resultadoChaveFuncionario.next()) {
					//Assiduidade Funcionário
					sqlAssiduidade.setInt(1, resultadoChaveFuncionario.getInt("codigoInterno"));
					resultadoAssiduidade = sqlAssiduidade.executeQuery();
					if (resultadoAssiduidade.next()) {
						//Funcionario a cumprir assiduidade nesta data
						listaNumeros.add(new Integer(resultado.getInt("numeroMecanografico")));
					}
				}
			}
			sqlChaveFuncionario.close();
			sqlAssiduidade.close();
			sql.close();
		} catch (Exception e) {
			System.out.println("FuncionarioRelacional.lerTodosNumerosAssiduidade: " + e.toString());
			return null;
		} finally {
			return listaNumeros;
		}
	} /* lerTodosNumerosAssiduidade */

	public ArrayList lerTodosNumerosFuncNaoDocentes() {
		ArrayList listaNumeros = null;

		try {
			PreparedStatement sql = UtilRelacional.prepararComando("SELECT * FROM ass_FUNCIONARIO ORDER BY numeroMecanografico");
			ResultSet resultado = sql.executeQuery();

			PreparedStatement sql2 = UtilRelacional.prepararComando("SELECT * FROM ass_FUNC_NAO_DOCENTE WHERE chaveFuncionario = ?");
			ResultSet resultado2 = null;

			listaNumeros = new ArrayList();
			while (resultado.next()) {
				sql2.setInt(1, resultado.getInt("codigoInterno"));
				resultado2 = sql2.executeQuery();

				if (resultado2.next()) {
					//funcionario a cumprir assiduidade
					listaNumeros.add(new Integer(resultado.getInt("numeroMecanografico")));
				}
			}
			sql2.close();
			sql.close();
		} catch (Exception e) {
			System.out.println("FuncionarioRelacional.lerTodosNumerosMecanograficos: " + e.toString());
			return null;
		} finally {
			return listaNumeros;
		}
	} /* lerTodosNumerosMecanograficos */

	/** Retorna o ultimo codigoInterno utilizado
	 * @return codigoInterno se sucedeu, 0 caso contrario
	 */
	public int ultimoCodigoInterno() {
		int ultimo = 0;

		try {
			PreparedStatement sql = UtilRelacional.prepararComando("SELECT max(codigoInterno) FROM ass_FUNCIONARIO");

			ResultSet resultado = sql.executeQuery();

			if (resultado.next())
				ultimo = resultado.getInt(1);

			sql.close();
		} catch (Exception e) {
			System.out.println("FuncionarioRelacional.ultimoCodigoInterno: " + e.toString());
		} finally {
			return ultimo;
		}
	}

	/******************************************************************************************
	* Assiduidade do Funcionario 
	*******************************************************************************************/
	public boolean escreveFimAssiduidade(int numMecanografico, Date fimAssiduidade, int status) {
		boolean resultado = false;

		try {
			PreparedStatement sql =
				UtilRelacional.prepararComando("SELECT codigoInterno FROM ass_FUNCIONARIO " + "WHERE numeroMecanografico = ?");
			sql.setInt(1, numMecanografico);
			ResultSet resultadoQuery = sql.executeQuery();
			int chaveFuncionario = 0;
			if (resultadoQuery.next()) {
				chaveFuncionario = resultadoQuery.getInt("codigoInterno");
			} else {
				sql.close();
				return resultado;
			}
			sql.close();

			//último registo de Assiduidade                       
			sql = UtilRelacional.prepararComando("SELECT MAX(dataInicio) FROM ass_HORARIO " + "WHERE chaveFuncionario = ?");
			sql.setInt(1, chaveFuncionario);
			resultadoQuery = sql.executeQuery();
			Date dataAssiduidade = null;
			if (resultadoQuery.next()) {
				dataAssiduidade = resultadoQuery.getDate("MAX(dataInicio)");
			}
			sql.close();

			//escreve a data fim de assiduidade nos actuais registos
			sql =
				UtilRelacional.prepararComando("SELECT codigoInterno FROM ass_HORARIO " + "WHERE chaveFuncionario = ? " + "AND dataInicio = ?");
			sql.setInt(1, chaveFuncionario);
			sql.setDate(2, new java.sql.Date(dataAssiduidade.getTime()));
			resultadoQuery = sql.executeQuery(); //registos actuais de assiduidade

			PreparedStatement sqlEscrita =
				UtilRelacional.prepararComando("UPDATE ass_HORARIO SET " + "dataFim = ? " + "WHERE codigoInterno = ? ");

			while (resultadoQuery.next()) {
				sqlEscrita.setDate(1, new java.sql.Date(fimAssiduidade.getTime()));
				sqlEscrita.setInt(2, resultadoQuery.getInt("codigoInterno"));

				sqlEscrita.executeUpdate();
			}
			sqlEscrita.close();
			sql.close();
			
			//TODO: completar o fim de assiduidade introduzindo um novo record de funcionario com o novo status
			resultado = true;
		} catch (Exception e) {
			System.out.println("FuncionarioRelacional.escreveFimAssiduidade: " + e.toString());
		} finally {
			return resultado;
		}
	} /* escreveFimAssiduidade */

	//Se retornar null ocorreu um erro ou o funcionário não tem fim de Assiduidade
	//Se retornar uma data pode não ser o fim de Assiduidade ou  o fim do horário
	public Date lerFimAssiduidade(int numMecanografico) {
		Date dataAssiduidade = null;

		try {
			PreparedStatement sql =
				UtilRelacional.prepararComando("SELECT codigoInterno FROM ass_FUNCIONARIO " + "WHERE numeroMecanografico = ?");
			sql.setInt(1, numMecanografico);
			ResultSet resultadoQuery = sql.executeQuery();
			int chaveFuncionario = 0;
			if (resultadoQuery.next()) {
				chaveFuncionario = resultadoQuery.getInt("codigoInterno");
			} else {
				sql.close();
				return null;
			}
			sql.close();

			sql = UtilRelacional.prepararComando("SELECT MAX(dataInicio) FROM ass_HORARIO " + "WHERE chaveFuncionario = ?");
			sql.setInt(1, chaveFuncionario);
			resultadoQuery = sql.executeQuery();
			Date dataInicioAssiduidade = null;
			if (resultadoQuery.next()) {
				dataInicioAssiduidade = resultadoQuery.getDate("MAX(dataInicio)");
			}
			sql.close();

			sql = UtilRelacional.prepararComando("SELECT dataFim FROM ass_HORARIO " + "WHERE chaveFuncionario = ? AND dataInicio = ?");
			sql.setInt(1, chaveFuncionario);
			sql.setDate(2, new java.sql.Date(dataInicioAssiduidade.getTime()));
			resultadoQuery = sql.executeQuery();
			if (resultadoQuery.next()) {
				dataAssiduidade = resultadoQuery.getDate("dataFim");
			}
			sql.close();
		} catch (Exception e) {
			System.out.println("FuncionarioRelacional.lerFimAssiduidade: " + e.toString());
		} finally {
			return dataAssiduidade;
		}
	} /* lerFimAssiduidade */

	public Date lerInicioAssiduidade(int numMecanografico) {
		Date dataAssiduidade = null;

		try {
			PreparedStatement sql =
				UtilRelacional.prepararComando("SELECT codigoInterno FROM ass_FUNCIONARIO " + "WHERE numeroMecanografico = ?");
			sql.setInt(1, numMecanografico);
			ResultSet resultadoQuery = sql.executeQuery();
			int chaveFuncionario = 0;
			if (resultadoQuery.next()) {
				chaveFuncionario = resultadoQuery.getInt("codigoInterno");
			} else {
				sql.close();
				return null;
			}
			sql.close();

			sql = UtilRelacional.prepararComando("SELECT MIN(dataInicio) FROM ass_HORARIO " + "WHERE chaveFuncionario = ?");
			sql.setInt(1, chaveFuncionario);
			resultadoQuery = sql.executeQuery();

			if (resultadoQuery.next()) {
				dataAssiduidade = resultadoQuery.getDate("MIN(dataInicio)");
			}
			sql.close();
		} catch (Exception e) {
			System.out.println("FuncionarioRelacional.lerInicioAssiduidade: " + e.toString());
		} finally {
			return dataAssiduidade;
		}
	} /* lerInicioAssiduidade */

	public boolean verificaFimAssiduidade(int numMecanografico, Date dataFimAssiduidade) {
		boolean resultado = false;

		try {
			PreparedStatement sql =
				UtilRelacional.prepararComando("SELECT codigoInterno FROM ass_FUNCIONARIO " + "WHERE numeroMecanografico = ?");
			sql.setInt(1, numMecanografico);
			ResultSet resultadoQuery = sql.executeQuery();
			int chaveFuncionario = 0;
			if (resultadoQuery.next()) {
				chaveFuncionario = resultadoQuery.getInt("codigoInterno");
			} else {
				sql.close();
				return resultado;
			}
			sql.close();

			sql = UtilRelacional.prepararComando("SELECT MAX(dataInicio) FROM ass_HORARIO " + "WHERE chaveFuncionario = ?");
			sql.setInt(1, chaveFuncionario);
			resultadoQuery = sql.executeQuery();
			Date dataAssiduidade = null;
			if (resultadoQuery.next()) {
				dataAssiduidade = resultadoQuery.getDate("MAX(dataInicio)");
			}
			sql.close();

			//verifica se a data fim de assiduidade está contida nos registos do funcionario
			//se nao então a data fim é válida
			if (!dataFimAssiduidade.before(dataAssiduidade)) {
				resultado = true;
			}
		} catch (Exception e) {
			System.out.println("FuncionarioRelacional.verificaFimAssiduidade: " + e.toString());
		} finally {
			return resultado;
		}
	} /* verificaFimAssiduidade */
}