package ServidorPersistenteJDBC.Relacional;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.ListIterator;

import Dominio.Horario;
import ServidorPersistenteJDBC.IHorarioPersistente;
import constants.assiduousness.Constants;

/**
 *
 * @author  Fernanda Quitério e Tania Pousão
 */
public class HorarioRelacional implements IHorarioPersistente {

	public boolean alterarHorario(Horario horario) {
		boolean resultado = false;

		try {
			PreparedStatement sql =
				UtilRelacional.prepararComando(
					"UPDATE ass_HORARIO SET "
						+ "codigoInterno = ? , "
						+ "chaveHorarioTipo = ? , "
						+ "chaveFuncionario = ? , "
						+ "sigla = ? , "
						+ "modalidade = ? , "
						+ "duracaoSemanal = ? , "
						+ "inicioPF1 = ? , "
						+ "fimPF1 = ? , "
						+ "inicioPF2 = ? , "
						+ "fimPF2 = ? , "
						+ "inicioHN1 = ? , "
						+ "fimHN1 = ? , "
						+ "inicioHN2 = ? , "
						+ "fimHN2 = ? , "
						+ "inicioRefeicao = ? , "
						+ "fimRefeicao = ? , "
						+ "descontoObrigatorio = ? , "
						+ "descontoMinimo = ? , "
						+ "inicioExpediente = ? , "
						+ "fimExpediente = ? , "
						+ "dataInicio = ? , "
						+ "dataFim = ? , "
						+ "numDias = ? , "
						+ "posicao = ? , "
						+ "quem = ? , "
						+ "quando = ? "
						+ "WHERE codigoInterno = ? ");

			sql.setInt(1, horario.getCodigoInterno());
			sql.setInt(2, horario.getChaveHorarioTipo());
			sql.setInt(3, horario.getChaveFuncionario());
			sql.setString(4, horario.getSigla());
			sql.setString(5, horario.getModalidade());
			sql.setFloat(6, horario.getDuracaoSemanal());
			sql.setTimestamp(7, horario.getInicioPF1());
			sql.setTimestamp(8, horario.getFimPF1());
			sql.setTimestamp(9, horario.getInicioPF2());
			sql.setTimestamp(10, horario.getFimPF2());
			sql.setTimestamp(11, horario.getInicioHN1());
			sql.setTimestamp(12, horario.getFimHN1());
			sql.setTimestamp(13, horario.getInicioHN2());
			sql.setTimestamp(14, horario.getFimHN2());
			sql.setTimestamp(15, horario.getInicioRefeicao());
			sql.setTimestamp(16, horario.getFimRefeicao());
			sql.setTime(17, horario.getDescontoObrigatorioRefeicao());
			sql.setTime(18, horario.getIntervaloMinimoRefeicao());
			sql.setTimestamp(19, horario.getInicioExpediente());
			sql.setTimestamp(20, horario.getFimExpediente());
			sql.setDate(21, new java.sql.Date((horario.getDataInicio()).getTime()));
			if (horario.getDataFim() != null) {
				sql.setDate(22, new java.sql.Date((horario.getDataFim()).getTime()));
			} else {
				sql.setDate(22, null);
			}
			sql.setInt(23, horario.getNumDias());
			sql.setInt(24, horario.getPosicao());
			sql.setInt(25, horario.getQuem());
			sql.setTimestamp(26, new Timestamp((horario.getQuando()).getTime()));
			sql.setInt(27, horario.getCodigoInterno());

			sql.executeUpdate();
			sql.close();
			resultado = true;
		} catch (Exception e) {

			System.out.println("HorarioRelacional.alterarHorario: " + e.toString());
		} finally {
			return resultado;
		}
	} /* alterarHorario */

	public boolean alterarDataFimHorario(Date dataFim, int numeroMecanografico) {
		boolean resultado = false;

		try {
			PreparedStatement sql =
				UtilRelacional.prepararComando("SELECT chaveHorarioActual FROM ass_FUNCIONARIO " + "WHERE numeroMecanografico = ?");
			sql.setInt(1, numeroMecanografico);
			ResultSet resultadoQuery = sql.executeQuery();
			int chaveHorarioActual = 0;
			if (resultadoQuery.next()) {
				chaveHorarioActual = resultadoQuery.getInt("chaveHorarioActual");
			} else {
				sql.close();
				return resultado;
			}
			sql.close();

			sql = UtilRelacional.prepararComando("UPDATE ass_HORARIO SET " + "dataFim = ? " + "WHERE codigoInterno = ? ");
			sql.setDate(1, new java.sql.Date(dataFim.getTime()));
			sql.setInt(2, chaveHorarioActual);
			sql.executeUpdate();
			sql.close();
			resultado = true;
		} catch (Exception e) {

			System.out.println("HorarioRelacional.alterarDataFimHorario: " + e.toString());
		} finally {
			return resultado;
		}
	} /* alterarDataFimHorario */

	public boolean apagarHorario(int codigoInterno) {
		boolean resultado = false;

		try {
			PreparedStatement sql = UtilRelacional.prepararComando("DELETE FROM ass_HORARIO " + "WHERE codigoInterno = ?");

			sql.setInt(1, codigoInterno);

			sql.executeUpdate();
			sql.close();
			resultado = true;
		} catch (Exception e) {
			System.out.println("HorarioRelacional.apagarHorario: " + e.toString());
		} finally {
			return resultado;
		}
	} /* apagarHorario */

	public boolean apagarTodosHorarios() {
		boolean resultado = false;

		try {
			PreparedStatement sql = UtilRelacional.prepararComando("DELETE FROM ass_HORARIO");
			sql.executeUpdate();
			sql.close();

			/*sql = UtilRelacional.prepararComando("ALTER TABLE ass_HORARIO auto_increment=1");
			sql.execute();
			sql.close();*/

			sql = UtilRelacional.prepararComando("DELETE FROM ass_HORARIO_EXCEPCAO");
			sql.executeUpdate();
			sql.close();

			/*sql = UtilRelacional.prepararComando("ALTER TABLE ass_HORARIO_EXCEPCAO auto_increment=1");
			sql.execute();
			sql.close();*/
						
			resultado = true;
		} catch (Exception e) {
			System.out.println("HorarioRelacional.apagarHorario: " + e.toString());
		} finally {
			return resultado;
		}
	} /* apagarTodosHorarios */

	public boolean associarExcepcaoHorarioRegime(int chaveHorario, int chaveRegime) {
		boolean resultado;

		try {
			PreparedStatement sql = UtilRelacional.prepararComando("INSERT INTO " + "ass_HORARIOEXCEPCAO_REGIME VALUES (?, ?)");

			sql.setInt(1, chaveHorario);
			sql.setInt(2, chaveRegime);

			sql.executeUpdate();
			sql.close();
			resultado = true;
		} catch (Exception e) {
			resultado = false;
			System.out.println("HorarioRelacional.associarExcepcaoHorarioRegime: " + e.toString());
		}
		return resultado;
	} /* associarExcepcaoHorarioRegime */

	public boolean associarHorarioRegime(int chaveHorario, int chaveRegime) {
		boolean resultado;

		try {
			PreparedStatement sql = UtilRelacional.prepararComando("INSERT INTO " + "ass_HORARIO_REGIME VALUES (?, ?)");

			sql.setInt(1, chaveHorario);
			sql.setInt(2, chaveRegime);

			sql.executeUpdate();
			sql.close();
			resultado = true;
		} catch (Exception e) {
			resultado = false;
			System.out.println("HorarioRelacional.associarHorarioRegime: " + e.toString());
		}
		return resultado;
	} /* associarHorarioRegime */

	public int calcularIndiceRotacao(int numDiasRotacao, java.util.Date dataInicioHorario, java.util.Date dataConsulta) {
		int indiceRotacao = 0;

		try {

			Calendar inicioHorario = Calendar.getInstance();
			inicioHorario.setTimeInMillis(dataInicioHorario.getTime());
			Calendar diaConsulta = Calendar.getInstance();
			diaConsulta.setTimeInMillis(dataConsulta.getTime());

			int numDias = 0;

			//deslocamento caso o horário não se inicie ao Domingo
			int indiceInicioHorario = inicioHorario.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY;

			if (inicioHorario.get(Calendar.YEAR) < diaConsulta.get(Calendar.YEAR)) {
				Calendar fimAnoHorario = Calendar.getInstance();
				fimAnoHorario.set(inicioHorario.get(Calendar.YEAR), Calendar.DECEMBER, 31, 00, 00, 00);
				numDias = fimAnoHorario.get(Calendar.DAY_OF_YEAR) - inicioHorario.get(Calendar.DAY_OF_YEAR) + 1;
				//numero de dias para o fim do ano do horario

				Calendar calendario = Calendar.getInstance();
				int ano = inicioHorario.get(Calendar.YEAR) + 1;
				//ano seguinte ao ano do horario
				while (ano != diaConsulta.get(Calendar.YEAR)) {
					calendario.clear();
					calendario.set(ano, Calendar.DECEMBER, 31, 00, 00, 00);

					numDias = numDias + calendario.get(Calendar.DAY_OF_YEAR);
					//adição de mais um ano

					ano++;
				}

				numDias = numDias + diaConsulta.get(Calendar.DAY_OF_YEAR);
				//adição de dias do ano da consulta

			} else if (inicioHorario.get(Calendar.YEAR) == diaConsulta.get(Calendar.YEAR)) {
				numDias =
					new BigInteger(String.valueOf((diaConsulta.get(Calendar.DAY_OF_YEAR) - inicioHorario.get(Calendar.DAY_OF_YEAR)) + 1))
						.abs()
						.intValue();

			} else if (inicioHorario.get(Calendar.YEAR) > diaConsulta.get(Calendar.YEAR)) {
				//CONSULTA POSSIVELMENTE INCORRECTA
				Calendar fimAnoHorario = Calendar.getInstance();
				fimAnoHorario.set(diaConsulta.get(Calendar.YEAR), Calendar.DECEMBER, 31, 00, 00, 00);
				numDias = fimAnoHorario.get(Calendar.DAY_OF_YEAR) - diaConsulta.get(Calendar.DAY_OF_YEAR) + 1;
				//numero de dias para o fim do ano da consulta

				Calendar calendario = Calendar.getInstance();
				int ano = diaConsulta.get(Calendar.YEAR) + 1;
				//ano seguinte ao ano da consulta
				while (ano != inicioHorario.get(Calendar.YEAR)) {
					calendario.clear();
					calendario.set(ano, Calendar.DECEMBER, 31, 00, 00, 00);

					numDias = numDias + calendario.get(Calendar.DAY_OF_YEAR);
					//adição de mais um ano

					ano++;
				}

				numDias = numDias + inicioHorario.get(Calendar.DAY_OF_YEAR);
				//adição de dias do ano da consulta
			}

			indiceRotacao =
				new BigInteger(String.valueOf(numDias + indiceInicioHorario)).mod(new BigInteger(String.valueOf(numDiasRotacao))).intValue();
			/*
			int indiceDescanso =
				new BigInteger(String.valueOf(indiceRotacao))
					.mod(new BigInteger(String.valueOf(Constants.NUM_DIAS_SEMANA)))
					.intValue();
			if (indiceDescanso == 0 || indiceDescanso == 1) {
				indiceRotacao = indiceDescanso;
			}*/
		} catch (ArithmeticException ea) {
			System.out.println("HorarioRelacional.calcularIndiceRotacao: " + ea.toString());
		} finally {
			return indiceRotacao;
		}
	} /* calcularIndiceRotacao */

	public int calculaIndiceReferenciaDescanso(int indiceRotacao, int indiceDescanso, int numDiasRotacao) {
		if (indiceDescanso == 0) { //é sábado
			indiceRotacao = indiceRotacao - 1;
			if (indiceRotacao < 0) {
				indiceRotacao = numDiasRotacao - 1;
			}
		} else if (indiceDescanso == 1) { //é domingo
			indiceRotacao = indiceRotacao + 1;
		}
		return indiceRotacao;
	} /* calculaIndiceReferenciaDescanso */

	public int calcularIndiceDescanso(int indiceRotacao) {
		int indiceDescanso = 0;

		try {
			indiceDescanso =
				new BigInteger(String.valueOf(indiceRotacao)).mod(new BigInteger(String.valueOf(Constants.NUM_DIAS_SEMANA))).intValue();
			// se é 0 entao é sábado
			// se é 1 entao é domingo
		} catch (ArithmeticException ea) {
			System.out.println("HorarioRelacional.calcularIndiceDescanso: " + ea.toString());
		} finally {
			return indiceDescanso;
		}
	} /* calcularIndiceDescanso */
	public boolean escreverExcepcaoHorario(Horario horario) {
		boolean resultado = false;

		try {
			PreparedStatement sql =
				UtilRelacional.prepararComando(
					"INSERT INTO ass_HORARIO_EXCEPCAO " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

			sql.setInt(1, horario.getCodigoInterno());
			sql.setInt(2, horario.getChaveHorarioTipo());
			sql.setInt(3, horario.getChaveFuncionario());
			sql.setString(4, horario.getSigla());
			sql.setString(5, horario.getModalidade());
			sql.setFloat(6, horario.getDuracaoSemanal());
			sql.setTimestamp(7, horario.getInicioPF1());
			sql.setTimestamp(8, horario.getFimPF1());
			sql.setTimestamp(9, horario.getInicioPF2());
			sql.setTimestamp(10, horario.getFimPF2());
			sql.setTimestamp(11, horario.getInicioHN1());
			sql.setTimestamp(12, horario.getFimHN1());
			sql.setTimestamp(13, horario.getInicioHN2());
			sql.setTimestamp(14, horario.getFimHN2());
			sql.setTimestamp(15, horario.getInicioRefeicao());
			sql.setTimestamp(16, horario.getFimRefeicao());
			sql.setTime(17, horario.getDescontoObrigatorioRefeicao());
			sql.setTime(18, horario.getIntervaloMinimoRefeicao());
			sql.setTimestamp(19, horario.getInicioExpediente());
			sql.setTimestamp(20, horario.getFimExpediente());
			if (horario.getDataInicio() != null) {
				sql.setDate(21, new java.sql.Date((horario.getDataInicio()).getTime()));
			} else {
				sql.setDate(21, null);
			}
			if (horario.getDataFim() != null) {
				sql.setDate(22, new java.sql.Date((horario.getDataFim()).getTime()));
			} else {
				sql.setDate(22, null);
			}
			sql.setInt(23, horario.getNumDias());
			sql.setInt(24, horario.getPosicao());
			sql.setInt(25, horario.getQuem());
			if (horario.getQuando() != null) {
				sql.setTimestamp(26, new Timestamp((horario.getQuando()).getTime()));
			} else {
				Calendar agora = Calendar.getInstance();
				sql.setTimestamp(26, new Timestamp(agora.getTimeInMillis()));
			}

			sql.executeUpdate();
			sql.close();
			resultado = true;
		} catch (Exception e) {
			System.out.println("HorarioRelacional.escreverExcepcaoHorario: " + e.toString());
		} finally {
			return resultado;
		}
	} /* escreverExcepcaoHorario */

	public boolean escreverHorario(Horario horario) {
		boolean resultado = false;

		try {
			PreparedStatement sql =
				UtilRelacional.prepararComando(
					"INSERT INTO ass_HORARIO " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

			sql.setInt(1, horario.getCodigoInterno());
			sql.setInt(2, horario.getChaveHorarioTipo());
			sql.setInt(3, horario.getChaveFuncionario());
			sql.setString(4, horario.getSigla());
			sql.setString(5, horario.getModalidade());
			sql.setFloat(6, horario.getDuracaoSemanal());
			sql.setTimestamp(7, horario.getInicioPF1());
			sql.setTimestamp(8, horario.getFimPF1());
			sql.setTimestamp(9, horario.getInicioPF2());
			sql.setTimestamp(10, horario.getFimPF2());
			sql.setTimestamp(11, horario.getInicioHN1());
			sql.setTimestamp(12, horario.getFimHN1());
			sql.setTimestamp(13, horario.getInicioHN2());
			sql.setTimestamp(14, horario.getFimHN2());
			sql.setTimestamp(15, horario.getInicioRefeicao());
			sql.setTimestamp(16, horario.getFimRefeicao());
			sql.setTime(17, horario.getDescontoObrigatorioRefeicao());
			sql.setTime(18, horario.getIntervaloMinimoRefeicao());
			sql.setTimestamp(19, horario.getInicioExpediente());
			sql.setTimestamp(20, horario.getFimExpediente());
			if (horario.getDataInicio() != null) {
				sql.setDate(21, new java.sql.Date((horario.getDataInicio()).getTime()));
			} else {
				sql.setDate(21, null);
			}
			if (horario.getDataFim() != null) {
				sql.setDate(22, new java.sql.Date((horario.getDataFim()).getTime()));
			} else {
				sql.setDate(22, null);
			}
			sql.setInt(23, horario.getNumDias());
			sql.setInt(24, horario.getPosicao());
			sql.setInt(25, horario.getQuem());
			if (horario.getQuando() != null) {
				sql.setTimestamp(26, new Timestamp((horario.getQuando()).getTime()));
			} else {
				Calendar agora = Calendar.getInstance();
				sql.setTimestamp(26, new Timestamp(agora.getTimeInMillis()));
			}

			sql.executeUpdate();
			sql.close();
			resultado = true;
		} catch (Exception e) {
			System.out.println("HorarioRelacional.escreverHorario: " + e.toString());
		} finally {
			return resultado;
		}
	} /* escreverHorario */

	public boolean escreverRotacoes(ArrayList rotacaoHorario) {
		boolean resultado = false;

		try {
			ListIterator iterador = rotacaoHorario.listIterator();
			Horario horario = null;
			PreparedStatement sql = null;
			ResultSet resultadoQuery = null;

			while (iterador.hasNext()) {
				horario = (Horario) iterador.next();

				sql = UtilRelacional.prepararComando("SELECT * FROM ass_HORARIO_TIPO " + "WHERE sigla = ?");
				sql.setString(1, horario.getSigla());
				resultadoQuery = sql.executeQuery();
				if (resultadoQuery.next()) {
					// é um horário tipo 
					horario.setChaveHorarioTipo(resultadoQuery.getInt("codigoInterno"));
				}
				sql.close();

				sql =
					UtilRelacional.prepararComando(
						"INSERT INTO ass_HORARIO " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

				sql.setInt(1, horario.getCodigoInterno());
				sql.setInt(2, horario.getChaveHorarioTipo());
				sql.setInt(3, horario.getChaveFuncionario());
				sql.setString(4, horario.getSigla());
				sql.setString(5, horario.getModalidade());
				sql.setFloat(6, horario.getDuracaoSemanal());
				sql.setTimestamp(7, horario.getInicioPF1());
				sql.setTimestamp(8, horario.getFimPF1());
				sql.setTimestamp(9, horario.getInicioPF2());
				sql.setTimestamp(10, horario.getFimPF2());
				sql.setTimestamp(11, horario.getInicioHN1());
				sql.setTimestamp(12, horario.getFimHN1());
				sql.setTimestamp(13, horario.getInicioHN2());
				sql.setTimestamp(14, horario.getFimHN2());
				sql.setTimestamp(15, horario.getInicioRefeicao());
				sql.setTimestamp(16, horario.getFimRefeicao());
				sql.setTime(17, horario.getDescontoObrigatorioRefeicao());
				sql.setTime(18, horario.getIntervaloMinimoRefeicao());
				sql.setTimestamp(19, horario.getInicioExpediente());
				sql.setTimestamp(20, horario.getFimExpediente());
				if (horario.getDataInicio() != null) {
					sql.setDate(21, new java.sql.Date((horario.getDataInicio()).getTime()));
				} else {
					sql.setDate(21, null);
				}
				if (horario.getDataFim() != null) {
					sql.setDate(22, new java.sql.Date((horario.getDataFim()).getTime()));
				} else {
					sql.setDate(22, null);
				}
				sql.setInt(23, horario.getNumDias());
				sql.setInt(24, horario.getPosicao());
				sql.setInt(25, horario.getQuem());
				sql.setTimestamp(26, new Timestamp((horario.getQuando()).getTime()));

				sql.executeUpdate();
				sql.close();
			}
			resultado = true;
		} catch (Exception e) {
			System.out.println("HorarioRelacional.escreverRotacoes: " + e.toString());
		} finally {
			return resultado;
		}
	} /* escreverRotacoes */

	public ArrayList lerExcepcoesHorarioPorNumMecanografico(int numMecanografico) {
		return null;
	} /* lerExcepcoesHorarioPorNumMecanografico */

	public Horario lerExcepcaoHorarioPorNumMecanografico(int numMecanografico, Timestamp dataConsulta) {
		//ATENCAO: esta função é idêntica à função lerHorarioPorNumMecanografico		
		Horario horario = null;
		try {
			PreparedStatement sql =
				UtilRelacional.prepararComando("SELECT codigoInterno FROM ass_FUNCIONARIO " + "WHERE numeroMecanografico = ?");
			sql.setInt(1, numMecanografico);
			ResultSet resultado = sql.executeQuery();
			int chaveFuncionario = 0;
			if (resultado.next()) {
				chaveFuncionario = resultado.getInt("codigoInterno");
			} else {
				sql.close();
				return null;
			}
			sql.close();

			// data de inicio do horário
			sql =
				UtilRelacional.prepararComando(
					"SELECT dataInicio FROM ass_HORARIO_EXCEPCAO "
						+ "WHERE chaveFuncionario = ? "
						+ "AND ((dataFim IS NOT NULL AND ? BETWEEN dataInicio AND dataFim) OR (dataFim IS NULL AND ? >= dataInicio))");
			sql.setInt(1, chaveFuncionario);
			sql.setDate(2, new java.sql.Date(dataConsulta.getTime()));
			sql.setDate(3, new java.sql.Date(dataConsulta.getTime()));
			resultado = sql.executeQuery();

			java.util.Date dataInicioHorario = null;
			if (resultado.next()) {
				dataInicioHorario = java.sql.Date.valueOf(resultado.getString("dataInicio"));
			} else {
				sql.close();
				return null;
			}
			sql.close();

			//número de dias da rotação
			sql =
				UtilRelacional.prepararComando(
					"SELECT MAX(numDias+posicao) FROM ass_HORARIO_EXCEPCAO " + "WHERE chaveFuncionario = ? " + "AND dataInicio = ?");
			sql.setInt(1, chaveFuncionario);
			sql.setDate(2, new java.sql.Date(dataInicioHorario.getTime()));
			resultado = sql.executeQuery();

			int numDiasRotacao = 0;
			if (resultado.next()) {
				numDiasRotacao = resultado.getInt(1);
			} else {
				sql.close();
				return null;
			}
			sql.close();
			if (numDiasRotacao == 0) {
				return null;
			}

			// indice do horário da rotação a ser cumprido agora
			Calendar agora = Calendar.getInstance();
			int indiceRotacao = calcularIndiceRotacao(numDiasRotacao, dataInicioHorario, new java.util.Date(dataConsulta.getTime()));

			// horário a ser cumprido agora
			sql =
				UtilRelacional.prepararComando(
					"SELECT * FROM ass_HORARIO_EXCEPCAO "
						+ "WHERE chaveFuncionario = ? "
						+ "AND dataInicio = ? AND (? BETWEEN posicao AND (posicao + numDias-1))");
			sql.setInt(1, chaveFuncionario);
			sql.setDate(2, new java.sql.Date(dataInicioHorario.getTime()));
			sql.setInt(3, indiceRotacao);
			resultado = sql.executeQuery();

			java.util.Date dataFim = null;
			if (resultado.next()) {
				if (resultado.getString("dataFim") != null) {
					dataFim = java.sql.Date.valueOf(resultado.getString("dataFim"));
				} else {
					dataFim = null;
				}

				horario =
					new Horario(
						resultado.getInt("codigoInterno"),
						resultado.getInt("chaveHorarioTipo"),
						resultado.getInt("chaveFuncionario"),
						resultado.getString("sigla"),
						resultado.getString("modalidade"),
						resultado.getFloat("duracaoSemanal"),
						resultado.getTimestamp("inicioPF1"),
						resultado.getTimestamp("fimPF1"),
						resultado.getTimestamp("inicioPF2"),
						resultado.getTimestamp("fimPF2"),
						resultado.getTimestamp("inicioHN1"),
						resultado.getTimestamp("fimHN1"),
						resultado.getTimestamp("inicioHN2"),
						resultado.getTimestamp("fimHN2"),
						resultado.getTimestamp("inicioRefeicao"),
						resultado.getTimestamp("fimRefeicao"),
						resultado.getTime("descontoObrigatorio"),
						resultado.getTime("descontoMinimo"),
						resultado.getTimestamp("inicioExpediente"),
						resultado.getTimestamp("fimExpediente"),
						null,
						dataInicioHorario,
						dataFim,
						resultado.getInt("numDias"),
						resultado.getInt("posicao"),
						resultado.getInt("quem"),
						resultado.getTimestamp("quando"));
			} else {
				System.out.println("---> indice de rotacao:" + indiceRotacao);
				// possivelmente é um dia de descanso
				if (indiceRotacao == 1) {
					//é um DS, ie Domingo
					horario = new Horario(Constants.DS);
				} else if (indiceRotacao == 0) {
					//é um DSC, ie Sabado
					horario = new Horario(Constants.DSC);
				}

				//Expediente de dias de Descanso
				horario.setInicioExpediente(new Timestamp(Constants.EXPEDIENTE_MINIMO));
				horario.setFimExpediente(new Timestamp(Constants.EXPEDIENTE_MAXIMO));
			}
			sql.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("HorarioRelacional.lerExcepcaoHorarioPorNumMecanografico: " + e.toString());
		} finally {
			return horario;
		}
	} /* lerExcepcaoHorarioPorNumMecanografico */

	public ArrayList lerExcepcoesHorarioPorNumMecanografico(int numMecanografico, Date dataInicio, Date dataFim) {
		ArrayList listaHorarios = null;

		try {
			PreparedStatement sql =
				UtilRelacional.prepararComando("SELECT codigoInterno FROM ass_FUNCIONARIO " + "WHERE numeroMecanografico = ?");
			sql.setInt(1, numMecanografico);
			ResultSet resultado = sql.executeQuery();
			int chaveFuncionario = 0;
			if (resultado.next()) {
				chaveFuncionario = resultado.getInt("codigoInterno");
			} else {
				sql.close();
				return null;
			}
			sql.close();

			sql =
				UtilRelacional.prepararComando(
					"SELECT * FROM ass_HORARIO_EXCEPCAO "
						+ "WHERE chaveFuncionario = ? "
						+ "AND ((dataInicio BETWEEN ? AND ?) "
						+ "OR (dataFim BETWEEN ? AND ?) OR (dataInicio <= ? AND dataFim >= ?))");

			sql.setInt(1, chaveFuncionario);
			sql.setDate(2, new java.sql.Date(dataInicio.getTime()));
			sql.setDate(3, new java.sql.Date(dataFim.getTime()));
			sql.setDate(4, new java.sql.Date(dataInicio.getTime()));
			sql.setDate(5, new java.sql.Date(dataFim.getTime()));
			sql.setDate(6, new java.sql.Date(dataInicio.getTime()));
			sql.setDate(7, new java.sql.Date(dataFim.getTime()));

			resultado = sql.executeQuery();

			listaHorarios = new ArrayList();
			while (resultado.next()) {
				listaHorarios.add(
					new Horario(
						resultado.getInt("codigoInterno"),
						resultado.getInt("chaveHorarioTipo"),
						resultado.getInt("chaveFuncionario"),
						resultado.getString("sigla"),
						resultado.getString("modalidade"),
						resultado.getFloat("duracaoSemanal"),
						resultado.getTimestamp("inicioPF1"),
						resultado.getTimestamp("fimPF1"),
						resultado.getTimestamp("inicioPF2"),
						resultado.getTimestamp("fimPF2"),
						resultado.getTimestamp("inicioHN1"),
						resultado.getTimestamp("fimHN1"),
						resultado.getTimestamp("inicioHN2"),
						resultado.getTimestamp("fimHN2"),
						resultado.getTimestamp("inicioRefeicao"),
						resultado.getTimestamp("fimRefeicao"),
						resultado.getTime("descontoObrigatorio"),
						resultado.getTime("descontoMinimo"),
						resultado.getTimestamp("inicioExpediente"),
						resultado.getTimestamp("fimExpediente"),
						null,
						java.sql.Date.valueOf(resultado.getString("dataInicio")),
						null,
						resultado.getInt("numDias"),
						resultado.getInt("posicao"),
						resultado.getInt("quem"),
						resultado.getTimestamp("quando")));
			}
			sql.close();
		} catch (Exception e) {
			System.out.println("HorarioRelacional.lerExcepcoesHorarioPorNumMecanografico: " + e.toString());
		} finally {
			return listaHorarios;
		}
	} /* lerExcepcoesHorarioPorNumMecanografico */

	public ArrayList lerHistoricoHorarioPorNumMecanografico(int numMecanografico) {
		ArrayList historicoHorario = null;

		try {
			PreparedStatement sql =
				UtilRelacional.prepararComando("SELECT codigoInterno FROM ass_FUNCIONARIO " + "WHERE numeroMecanografico = ?");
			sql.setInt(1, numMecanografico);
			ResultSet resultado = sql.executeQuery();
			int chaveFuncionario = 0;
			if (resultado.next()) {
				chaveFuncionario = resultado.getInt("codigoInterno");
			} else {
				sql.close();
				return null;
			}
			sql.close();

			//ATENCAO: atencao aos horarios rotativos no metodo lerHistoricoHorarioPorNumMecanografico
			sql = UtilRelacional.prepararComando("SELECT * FROM ass_HORARIO WHERE chaveFuncionario = ?");
			sql.setInt(1, chaveFuncionario);
			resultado = sql.executeQuery();

			historicoHorario = new ArrayList();
			java.util.Date dataFim = null;
			while (resultado.next()) {
				if (resultado.getString("dataFim") != null) {
					dataFim = java.sql.Date.valueOf(resultado.getString("dataFim"));
				} else {
					dataFim = null;
				}

				historicoHorario.add(
					new Horario(
						resultado.getInt("codigoInterno"),
						resultado.getInt("chaveHorarioTipo"),
						resultado.getInt("chaveFuncionario"),
						resultado.getString("sigla"),
						resultado.getString("modalidade"),
						resultado.getFloat("duracaoSemanal"),
						resultado.getTimestamp("inicioPF1"),
						resultado.getTimestamp("fimPF1"),
						resultado.getTimestamp("inicioPF2"),
						resultado.getTimestamp("fimPF2"),
						resultado.getTimestamp("inicioHN1"),
						resultado.getTimestamp("fimHN1"),
						resultado.getTimestamp("inicioHN2"),
						resultado.getTimestamp("fimHN2"),
						resultado.getTimestamp("inicioRefeicao"),
						resultado.getTimestamp("fimRefeicao"),
						resultado.getTime("descontoObrigatorio"),
						resultado.getTime("descontoMinimo"),
						resultado.getTimestamp("inicioExpediente"),
						resultado.getTimestamp("fimExpediente"),
						null,
						java.sql.Date.valueOf(resultado.getString("dataInicio")),
						dataFim,
						resultado.getInt("numDias"),
						resultado.getInt("posicao"),
						resultado.getInt("quem"),
						resultado.getTimestamp("quando")));

			}
			sql.close();

		} catch (Exception e) {
			System.out.println("HorarioRelacional.lerHistoricoHorarioPorNumMecanografico: " + e.toString());
		} finally {
			return historicoHorario;
		}
	} /* lerHistoricoHorarioPorNumMecanografico */

	public Horario lerHorario(int codigoInterno) {
		Horario horario = null;

		try {
			PreparedStatement sql = UtilRelacional.prepararComando("SELECT * FROM ass_HORARIO " + "WHERE codigoInterno = ?");

			sql.setInt(1, codigoInterno);

			ResultSet resultado = sql.executeQuery();
			if (resultado.next()) {
				java.util.Date dataFim = null;
				if (resultado.getString("dataFim") != null) {
					dataFim = java.sql.Date.valueOf(resultado.getString("dataFim"));
				}
				horario =
					new Horario(
						resultado.getInt("codigoInterno"),
						resultado.getInt("chaveHorarioTipo"),
						resultado.getInt("chaveFuncionario"),
						resultado.getString("sigla"),
						resultado.getString("modalidade"),
						resultado.getFloat("duracaoSemanal"),
						resultado.getTimestamp("inicioPF1"),
						resultado.getTimestamp("fimPF1"),
						resultado.getTimestamp("inicioPF2"),
						resultado.getTimestamp("fimPF2"),
						resultado.getTimestamp("inicioHN1"),
						resultado.getTimestamp("fimHN1"),
						resultado.getTimestamp("inicioHN2"),
						resultado.getTimestamp("fimHN2"),
						resultado.getTimestamp("inicioRefeicao"),
						resultado.getTimestamp("fimRefeicao"),
						resultado.getTime("descontoObrigatorio"),
						resultado.getTime("descontoMinimo"),
						resultado.getTimestamp("inicioExpediente"),
						resultado.getTimestamp("fimExpediente"),
						null,
						java.sql.Date.valueOf(resultado.getString("dataInicio")),
						dataFim,
						resultado.getInt("numDias"),
						resultado.getInt("posicao"),
						resultado.getInt("quem"),
						resultado.getTimestamp("quando"));
			}
			sql.close();
		} catch (Exception e) {
			System.out.println("HorarioRelacional.lerHorario: " + e.toString());
		} finally {
			return horario;
		}
	} /* lerHorario */

	public Horario lerHorario(int codigoInterno, Timestamp dataConsulta) {
		//TODO: codificacao a fazer no metodo lerHorario
		Horario horario = null;

		return horario;
	} /* lerHorario */

	public Horario lerHorarioPorFuncionario(int chaveFuncionario) {
		Horario horario = null;

		try {
			PreparedStatement sql = UtilRelacional.prepararComando("SELECT * FROM ass_HORARIO " + "WHERE chaveFuncionario = ?");

			sql.setInt(1, chaveFuncionario);

			ResultSet resultado = sql.executeQuery();
			if (resultado.next()) {
				java.util.Date dataFim = null;
				if (resultado.getString("dataFim") != null) {
					dataFim = java.sql.Date.valueOf(resultado.getString("dataFim"));
				}
				horario =
					new Horario(
						resultado.getInt("codigoInterno"),
						resultado.getInt("chaveHorarioTipo"),
						resultado.getInt("chaveFuncionario"),
						resultado.getString("sigla"),
						resultado.getString("modalidade"),
						resultado.getFloat("duracaoSemanal"),
						resultado.getTimestamp("inicioPF1"),
						resultado.getTimestamp("fimPF1"),
						resultado.getTimestamp("inicioPF2"),
						resultado.getTimestamp("fimPF2"),
						resultado.getTimestamp("inicioHN1"),
						resultado.getTimestamp("fimHN1"),
						resultado.getTimestamp("inicioHN2"),
						resultado.getTimestamp("fimHN2"),
						resultado.getTimestamp("inicioRefeicao"),
						resultado.getTimestamp("fimRefeicao"),
						resultado.getTime("descontoObrigatorio"),
						resultado.getTime("descontoMinimo"),
						resultado.getTimestamp("inicioExpediente"),
						resultado.getTimestamp("fimExpediente"),
						null,
						java.sql.Date.valueOf(resultado.getString("dataInicio")),
						dataFim,
						resultado.getInt("numDias"),
						resultado.getInt("posicao"),
						resultado.getInt("quem"),
						resultado.getTimestamp("quando"));
			}
			sql.close();
		} catch (Exception e) {
			System.out.println("HorarioRelacional.lerHorarioPorFuncionario" + e.toString());
		} finally {
			return horario;
		}
	} /* lerHorarioPorFuncionario */

	public ArrayList lerHorarioActualPorNumMecanografico(int numMecanografico) {
		//ATENCAO: esta função é igual à função lerRotacoesPorNumMecanografico  
		ArrayList listaHorarios = null;
		Horario horario = null;

		try {
			PreparedStatement sql =
				UtilRelacional.prepararComando("SELECT codigoInterno, chaveHorarioActual FROM ass_FUNCIONARIO " + "WHERE numeroMecanografico = ?");
			sql.setInt(1, numMecanografico);
			ResultSet resultado = sql.executeQuery();
			int chaveFuncionario = 0;
			int chaveHorario = 0;
			if (resultado.next()) {
				chaveFuncionario = resultado.getInt("codigoInterno");
				chaveHorario = resultado.getInt("chaveHorarioActual");
			} else {
				sql.close();
				return null;
			}
			sql.close();

			//TODO: verifica se o horário a cumprir não é uma excepção

			// data de inicio do horário e número de dias da rotação
			sql = UtilRelacional.prepararComando("SELECT dataInicio, numDias, posicao FROM ass_HORARIO " + "WHERE codigoInterno = ?");
			sql.setInt(1, chaveHorario);
			resultado = sql.executeQuery();
			java.util.Date dataInicioHorario = null;
			int numDiasRotacao = 0;
			if (resultado.next()) {
				dataInicioHorario = java.sql.Date.valueOf(resultado.getString("dataInicio"));
				numDiasRotacao = resultado.getInt("numDias") + resultado.getInt("posicao");
			} else {
				sql.close();
				return null;
			}
			sql.close();

			// indice do horário da rotação a ser cumprido agora
			Calendar agora = Calendar.getInstance();
			int indiceRotacao = calcularIndiceRotacao(numDiasRotacao, dataInicioHorario, agora.getTime());

			// horário a ser cumprido agora
			sql =
				UtilRelacional.prepararComando(
					"SELECT codigoInterno FROM ass_HORARIO "
						+ "WHERE chaveFuncionario = ? "
						+ "AND dataInicio = ? AND (? BETWEEN posicao AND (posicao + numDias-1))");
			sql.setInt(1, chaveFuncionario);
			sql.setDate(2, new java.sql.Date(dataInicioHorario.getTime()));
			sql.setInt(3, indiceRotacao);
			resultado = sql.executeQuery();
			int chaveHorarioAgora = 0;
			if (resultado.next()) {
				chaveHorarioAgora = resultado.getInt("codigoInterno");
			}
			sql.close();

			// todos os horarios da rotacao
			sql =
				UtilRelacional.prepararComando(
					"SELECT * FROM ass_HORARIO " + "WHERE chaveFuncionario = ? " + "AND dataInicio = ? ORDER BY posicao");
			sql.setInt(1, chaveFuncionario);
			sql.setDate(2, new java.sql.Date(dataInicioHorario.getTime()));
			resultado = sql.executeQuery();

			Calendar dataHorario = null;
			java.util.Date dataCumprir = null;
			java.util.Date dataFim = null;
			listaHorarios = new ArrayList();
			while (resultado.next()) {
				// Primeiro dia da proxima rotacao deste horario
				dataHorario = Calendar.getInstance();
				if (resultado.getInt("codigoInterno") == chaveHorarioAgora) {
					dataHorario.set(Calendar.DAY_OF_MONTH, agora.get(Calendar.DAY_OF_MONTH) - (indiceRotacao - resultado.getInt("posicao")));
				} else {
					if (indiceRotacao > resultado.getInt("posicao")) {
						dataHorario.set(
							Calendar.DAY_OF_MONTH,
							agora.get(Calendar.DAY_OF_MONTH) + ((numDiasRotacao - indiceRotacao) + resultado.getInt("posicao")));
					} else if (indiceRotacao < resultado.getInt("posicao")) {
						dataHorario.set(Calendar.DAY_OF_MONTH, agora.get(Calendar.DAY_OF_MONTH) + (resultado.getInt("posicao") - indiceRotacao));
					}
				}
				dataCumprir = dataHorario.getTime();

				if (resultado.getString("dataFim") != null) {
					dataFim = java.sql.Date.valueOf(resultado.getString("dataFim"));
				} else {
					dataFim = null;
				}

				listaHorarios.add(
					new Horario(
						resultado.getInt("codigoInterno"),
						resultado.getInt("chaveHorarioTipo"),
						resultado.getInt("chaveFuncionario"),
						resultado.getString("sigla"),
						resultado.getString("modalidade"),
						resultado.getFloat("duracaoSemanal"),
						resultado.getTimestamp("inicioPF1"),
						resultado.getTimestamp("fimPF1"),
						resultado.getTimestamp("inicioPF2"),
						resultado.getTimestamp("fimPF2"),
						resultado.getTimestamp("inicioHN1"),
						resultado.getTimestamp("fimHN1"),
						resultado.getTimestamp("inicioHN2"),
						resultado.getTimestamp("fimHN2"),
						resultado.getTimestamp("inicioRefeicao"),
						resultado.getTimestamp("fimRefeicao"),
						resultado.getTime("descontoObrigatorio"),
						resultado.getTime("descontoMinimo"),
						resultado.getTimestamp("inicioExpediente"),
						resultado.getTimestamp("fimExpediente"),
						dataCumprir,
						dataInicioHorario,
						dataFim,
						resultado.getInt("numDias"),
						resultado.getInt("posicao"),
						resultado.getInt("quem"),
						resultado.getTimestamp("quando")));
			}
			sql.close();
		} catch (Exception e) {
			System.out.println("HorarioRelacional.lerRotacoesPorNumMecanografico: " + e.toString());
		} finally {
			return listaHorarios;
		}
	} /* lerHorarioActualPorNumMecanografico */

	public Horario lerHorarioPorNumFuncionario(int numMecanografico, Timestamp dataConsulta) {
		Horario horario = null;
		try {
			PreparedStatement sql =
				UtilRelacional.prepararComando("SELECT codigoInterno FROM ass_FUNCIONARIO " + "WHERE numeroMecanografico = ?");
			sql.setInt(1, numMecanografico);
			ResultSet resultado = sql.executeQuery();
			int chaveFuncionario = 0;
			if (resultado.next()) {
				chaveFuncionario = resultado.getInt("codigoInterno");
			} else {
				sql.close();
				return null;
			}
			sql.close();

			// data de inicio do horário
			sql =
				UtilRelacional.prepararComando(
					"SELECT dataInicio FROM ass_HORARIO "
						+ "WHERE chaveFuncionario = ? "
						+ "AND ((dataFim IS NOT NULL AND ? BETWEEN dataInicio AND dataFim) OR (dataFim IS NULL AND ? >= dataInicio))");
			sql.setInt(1, chaveFuncionario);
			sql.setDate(2, new java.sql.Date(dataConsulta.getTime()));
			sql.setDate(3, new java.sql.Date(dataConsulta.getTime()));
			resultado = sql.executeQuery();

			java.util.Date dataInicioHorario = null;
			if (resultado.next()) {
				dataInicioHorario = java.sql.Date.valueOf(resultado.getString("dataInicio"));
			} else {
				sql.close();
				return null;
			}
			sql.close();

			//número de dias da rotação
			sql =
				UtilRelacional.prepararComando(
					"SELECT MAX(numDias+posicao) FROM ass_HORARIO " + "WHERE chaveFuncionario = ? " + "AND dataInicio = ?");
			sql.setInt(1, chaveFuncionario);
			sql.setDate(2, new java.sql.Date(dataInicioHorario.getTime()));
			resultado = sql.executeQuery();

			int numDiasRotacao = 0;
			if (resultado.next()) {
				numDiasRotacao = resultado.getInt(1);
			} else {
				sql.close();
				return null;
			}
			sql.close();
			if (numDiasRotacao == 0) {
				return null;
			}

			// indice do horário da rotação a ser cumprido agora
			Calendar agora = Calendar.getInstance();
			int indiceRotacao = calcularIndiceRotacao(numDiasRotacao, dataInicioHorario, new java.util.Date(dataConsulta.getTime()));
			int indiceDescanso = calcularIndiceDescanso(indiceRotacao);
			indiceRotacao = calculaIndiceReferenciaDescanso(indiceRotacao, indiceDescanso, numDiasRotacao);
			System.out.println("Indices: " + indiceRotacao + " Descanso: " + indiceDescanso);

			// horário a ser cumprido agora
			sql =
				UtilRelacional.prepararComando(
					"SELECT * FROM ass_HORARIO "
						+ "WHERE chaveFuncionario = ? "
						+ "AND dataInicio = ? AND (? BETWEEN posicao AND (posicao + numDias-1))");
			sql.setInt(1, chaveFuncionario);
			sql.setDate(2, new java.sql.Date(dataInicioHorario.getTime()));
			sql.setInt(3, indiceRotacao);
			resultado = sql.executeQuery();

			java.util.Date dataFim = null;
			if (resultado.next()) {
				if (resultado.getString("dataFim") != null) {
					dataFim = java.sql.Date.valueOf(resultado.getString("dataFim"));
				} else {
					dataFim = null;
				}

				horario =
					new Horario(
						resultado.getInt("codigoInterno"),
						resultado.getInt("chaveHorarioTipo"),
						resultado.getInt("chaveFuncionario"),
						resultado.getString("sigla"),
						resultado.getString("modalidade"),
						resultado.getFloat("duracaoSemanal"),
						resultado.getTimestamp("inicioPF1"),
						resultado.getTimestamp("fimPF1"),
						resultado.getTimestamp("inicioPF2"),
						resultado.getTimestamp("fimPF2"),
						resultado.getTimestamp("inicioHN1"),
						resultado.getTimestamp("fimHN1"),
						resultado.getTimestamp("inicioHN2"),
						resultado.getTimestamp("fimHN2"),
						resultado.getTimestamp("inicioRefeicao"),
						resultado.getTimestamp("fimRefeicao"),
						resultado.getTime("descontoObrigatorio"),
						resultado.getTime("descontoMinimo"),
						resultado.getTimestamp("inicioExpediente"),
						resultado.getTimestamp("fimExpediente"),
						null,
						dataInicioHorario,
						dataFim,
						resultado.getInt("numDias"),
						resultado.getInt("posicao"),
						resultado.getInt("quem"),
						resultado.getTimestamp("quando"));

				if (indiceDescanso == 1) {
					System.out.println("É DOMINGO - DS");
					//é um DS, ie Domingo
					horario.setSigla(Constants.DS);
					//Expediente de dias de Descanso
					horario.setInicioExpediente(new Timestamp(Constants.EXPEDIENTE_MINIMO));
					horario.setFimExpediente(new Timestamp(Constants.EXPEDIENTE_MAXIMO));
				} else if (indiceDescanso == 0) {
					System.out.println("É SÁBADO - DSC");
					//é um DSC, ie Sabado					
					horario.setSigla(Constants.DSC);
					// Expediente de dias de Descanso
					horario.setInicioExpediente(new Timestamp(Constants.EXPEDIENTE_MINIMO));
					horario.setFimExpediente(new Timestamp(Constants.EXPEDIENTE_MAXIMO));
				}
			} else {
				sql.close();
				return null;
			}
			sql.close();
		} catch (Exception e) {
			e.printStackTrace();
			//System.out.println("HorarioRelacional.lerHorarioPorNumFuncionario: " + e.toString());
		} finally {
			return horario;
		}
	} /* lerHorarioPorNumFuncionario */

	public ArrayList lerHorariosPorNumMecanografico(int numMecanografico, Date dataInicio, Date dataFim) {
		ArrayList listaHorarios = null;

		try {
			PreparedStatement sql =
				UtilRelacional.prepararComando("SELECT codigoInterno FROM ass_FUNCIONARIO " + "WHERE numeroMecanografico = ?");
			sql.setInt(1, numMecanografico);
			ResultSet resultado = sql.executeQuery();
			int chaveFuncionario = 0;
			if (resultado.next()) {
				chaveFuncionario = resultado.getInt("codigoInterno");
			} else {
				sql.close();
				return null;
			}
			sql.close();

			if (dataFim != null) {
				sql =
					UtilRelacional.prepararComando(
						"SELECT * FROM ass_HORARIO "
							+ "WHERE chaveFuncionario = ? "
							+ "AND ((dataInicio BETWEEN ? AND ?) "
							+ "OR (dataFim BETWEEN ? AND ?) OR (dataInicio <= ? AND dataFim >= ?))");

				sql.setInt(1, chaveFuncionario);
				sql.setDate(2, new java.sql.Date(dataInicio.getTime()));
				sql.setDate(3, new java.sql.Date(dataFim.getTime()));
				sql.setDate(4, new java.sql.Date(dataInicio.getTime()));
				sql.setDate(5, new java.sql.Date(dataFim.getTime()));
				sql.setDate(6, new java.sql.Date(dataInicio.getTime()));
				sql.setDate(7, new java.sql.Date(dataFim.getTime()));

				resultado = sql.executeQuery();
			} else {
				sql =
					UtilRelacional.prepararComando(
						"SELECT * FROM ass_HORARIO " + "WHERE chaveFuncionario = ? " + "AND (? BETWEEN dataInicio AND dataFim)");

				sql.setInt(1, chaveFuncionario);
				sql.setDate(2, new java.sql.Date(dataInicio.getTime()));
				sql.setDate(3, new java.sql.Date(dataInicio.getTime()));

				resultado = sql.executeQuery();
			}

			listaHorarios = new ArrayList();
			while (resultado.next()) {
				listaHorarios.add(
					new Horario(
						resultado.getInt("codigoInterno"),
						resultado.getInt("chaveHorarioTipo"),
						resultado.getInt("chaveFuncionario"),
						resultado.getString("sigla"),
						resultado.getString("modalidade"),
						resultado.getFloat("duracaoSemanal"),
						resultado.getTimestamp("inicioPF1"),
						resultado.getTimestamp("fimPF1"),
						resultado.getTimestamp("inicioPF2"),
						resultado.getTimestamp("fimPF2"),
						resultado.getTimestamp("inicioHN1"),
						resultado.getTimestamp("fimHN1"),
						resultado.getTimestamp("inicioHN2"),
						resultado.getTimestamp("fimHN2"),
						resultado.getTimestamp("inicioRefeicao"),
						resultado.getTimestamp("fimRefeicao"),
						resultado.getTime("descontoObrigatorio"),
						resultado.getTime("descontoMinimo"),
						resultado.getTimestamp("inicioExpediente"),
						resultado.getTimestamp("fimExpediente"),
						null,
						java.sql.Date.valueOf(resultado.getString("dataInicio")),
						null,
						resultado.getInt("numDias"),
						resultado.getInt("posicao"),
						resultado.getInt("quem"),
						resultado.getTimestamp("quando")));
			}
			sql.close();
		} catch (Exception e) {
			System.out.println("HorarioRelacional.lerHorariosPorNumMecanografico: " + e.toString());
		} finally {
			return listaHorarios;
		}
	} /* lerHorariosPorNumMecanografico */

	public ArrayList lerHorariosSemFimValidade(int numMecanografico) {
		ArrayList listaHorarios = null;

		try {
			PreparedStatement sql =
				UtilRelacional.prepararComando("SELECT codigoInterno FROM ass_FUNCIONARIO " + "WHERE numeroMecanografico = ?");
			sql.setInt(1, numMecanografico);
			ResultSet resultado = sql.executeQuery();
			int chaveFuncionario = 0;
			if (resultado.next()) {
				chaveFuncionario = resultado.getInt("codigoInterno");
			} else {
				sql.close();
				return null;
			}
			sql.close();

			sql = UtilRelacional.prepararComando("SELECT * FROM ass_HORARIO " + "WHERE chaveFuncionario = ? AND horario.dataFim IS NULL");
			sql.setInt(1, chaveFuncionario);

			resultado = sql.executeQuery();
			listaHorarios = new ArrayList();
			while (resultado.next()) {
				listaHorarios.add(
					new Horario(
						resultado.getInt("codigoInterno"),
						resultado.getInt("chaveHorarioTipo"),
						resultado.getInt("chaveFuncionario"),
						resultado.getString("sigla"),
						resultado.getString("modalidade"),
						resultado.getFloat("duracaoSemanal"),
						resultado.getTimestamp("inicioPF1"),
						resultado.getTimestamp("fimPF1"),
						resultado.getTimestamp("inicioPF2"),
						resultado.getTimestamp("fimPF2"),
						resultado.getTimestamp("inicioHN1"),
						resultado.getTimestamp("fimHN1"),
						resultado.getTimestamp("inicioHN2"),
						resultado.getTimestamp("fimHN2"),
						resultado.getTimestamp("inicioRefeicao"),
						resultado.getTimestamp("fimRefeicao"),
						resultado.getTime("descontoObrigatorio"),
						resultado.getTime("descontoMinimo"),
						resultado.getTimestamp("inicioExpediente"),
						resultado.getTimestamp("fimExpediente"),
						null,
						java.sql.Date.valueOf(resultado.getString("dataInicio")),
						null,
						resultado.getInt("numDias"),
						resultado.getInt("posicao"),
						resultado.getInt("quem"),
						resultado.getTimestamp("quando")));
			}
			sql.close();
		} catch (Exception e) {
			System.out.println("HorarioRelacional.lerHorariosSemFimValidade: " + e.toString());
		} finally {
			return listaHorarios;
		}
	} /* lerHorariosSemFimValidade */

	public ArrayList lerRegimes(int chaveHorario) {
		ArrayList listaRegimes = null;

		try {
			PreparedStatement sql = UtilRelacional.prepararComando("SELECT * FROM ass_HORARIO_REGIME " + "WHERE chaveHorario = ?");

			sql.setInt(1, chaveHorario);

			ResultSet resultado = sql.executeQuery();
			listaRegimes = new ArrayList();
			while (resultado.next()) {
				listaRegimes.add(new Integer(resultado.getInt("chaveRegime")));
			}
			sql.close();
		} catch (Exception e) {
			System.out.println("HorarioRelacional.lerRegimes: " + e.toString());
		} finally {
			return listaRegimes;
		}
	} /* lerRegimes */

	public HashMap lerRegimesRotacoes(ArrayList rotacaoHorario) {
		HashMap regimesRotacaoHorario = null;

		try {
			ResultSet resultadoQuery = null;
			ResultSet resultadoQuery2 = null;
			regimesRotacaoHorario = new HashMap();
			ListIterator iterador = rotacaoHorario.listIterator();
			Horario horario = null;
			ArrayList listaRegimes = null;

			PreparedStatement sql = UtilRelacional.prepararComando("SELECT * FROM ass_HORARIOTIPO_REGIME WHERE chaveHorarioTipo = ?");
			PreparedStatement sql2 = UtilRelacional.prepararComando("SELECT designacao FROM regime WHERE codigoInterno = ?");

			while (iterador.hasNext()) {
				horario = (Horario) iterador.next();

				sql.setInt(1, horario.getChaveHorarioTipo());
				resultadoQuery = sql.executeQuery();

				listaRegimes = new ArrayList();
				while (resultadoQuery.next()) {
					sql2.setInt(1, resultadoQuery.getInt("chaveRegime"));
					resultadoQuery2 = sql2.executeQuery();

					if (resultadoQuery2.next()) {
						listaRegimes.add(new String(resultadoQuery2.getString("designacao")));
					}
				}
				regimesRotacaoHorario.put(new Integer(horario.getChaveHorarioTipo()), listaRegimes.clone());
			}
			sql2.close();
			sql.close();
		} catch (Exception e) {
			System.out.println("HorarioRelacional.lerRegimes: " + e.toString());
		} finally {
			return regimesRotacaoHorario;
		}
	} /* lerRegimesRotacoes */

	public ArrayList lerRotacoesPorNumMecanografico(int numMecanografico) {
		ArrayList listaHorarios = null;
		Horario horario = null;

		try {
			PreparedStatement sql =
				UtilRelacional.prepararComando("SELECT codigoInterno, chaveHorarioActual FROM ass_FUNCIONARIO " + "WHERE numeroMecanografico = ?");
			sql.setInt(1, numMecanografico);
			ResultSet resultado = sql.executeQuery();
			int chaveFuncionario = 0;
			int chaveHorario = 0;
			if (resultado.next()) {
				chaveFuncionario = resultado.getInt("codigoInterno");
				chaveHorario = resultado.getInt("chaveHorarioActual");
			} else {
				sql.close();
				return null;
			}
			sql.close();

			//TODO: verifica se o horário a cumprir não é uma excepção

			// data de inicio do horário e número de dias da rotação
			sql = UtilRelacional.prepararComando("SELECT dataInicio, numDias, posicao FROM ass_HORARIO " + "WHERE codigoInterno = ?");
			sql.setInt(1, chaveHorario);
			resultado = sql.executeQuery();
			java.util.Date dataInicioHorario = null;
			int numDiasRotacao = 0;
			if (resultado.next()) {
				dataInicioHorario = java.sql.Date.valueOf(resultado.getString("dataInicio"));
				numDiasRotacao = resultado.getInt("numDias") + resultado.getInt("posicao");
			} else {
				sql.close();
				return null;
			}
			sql.close();

			// indice do horário da rotação a ser cumprido agora
			Calendar agora = Calendar.getInstance();
			int indiceRotacao = calcularIndiceRotacao(numDiasRotacao, dataInicioHorario, agora.getTime());

			// horário a ser cumprido agora
			sql =
				UtilRelacional.prepararComando(
					"SELECT codigoInterno FROM ass_HORARIO "
						+ "WHERE chaveFuncionario = ? "
						+ "AND dataInicio = ? AND (? BETWEEN posicao AND (posicao + numDias-1))");
			sql.setInt(1, chaveFuncionario);
			sql.setDate(2, new java.sql.Date(dataInicioHorario.getTime()));
			sql.setInt(3, indiceRotacao);
			resultado = sql.executeQuery();
			int chaveHorarioAgora = 0;
			if (resultado.next()) {
				chaveHorarioAgora = resultado.getInt("codigoInterno");
			}
			sql.close();

			// todos os horarios da rotacao
			sql =
				UtilRelacional.prepararComando(
					"SELECT * FROM ass_HORARIO " + "WHERE chaveFuncionario = ? " + "AND dataInicio = ? ORDER BY posicao");
			sql.setInt(1, chaveFuncionario);
			sql.setDate(2, new java.sql.Date(dataInicioHorario.getTime()));
			resultado = sql.executeQuery();

			Calendar dataHorario = null;
			java.util.Date dataCumprir = null;
			java.util.Date dataFim = null;
			listaHorarios = new ArrayList();
			while (resultado.next()) {
				// Primeiro dia da proxima rotacao deste horario
				dataHorario = Calendar.getInstance();
				if (resultado.getInt("codigoInterno") == chaveHorarioAgora) {
					dataHorario.set(Calendar.DAY_OF_MONTH, agora.get(Calendar.DAY_OF_MONTH) - (indiceRotacao - resultado.getInt("posicao")));
				} else {
					if (indiceRotacao > resultado.getInt("posicao")) {
						dataHorario.set(
							Calendar.DAY_OF_MONTH,
							agora.get(Calendar.DAY_OF_MONTH) + ((numDiasRotacao - indiceRotacao) + resultado.getInt("posicao")));
					} else if (indiceRotacao < resultado.getInt("posicao")) {
						dataHorario.set(Calendar.DAY_OF_MONTH, agora.get(Calendar.DAY_OF_MONTH) + (resultado.getInt("posicao") - indiceRotacao));
					}
				}
				dataCumprir = dataHorario.getTime();

				if (resultado.getString("dataFim") != null) {
					dataFim = java.sql.Date.valueOf(resultado.getString("dataFim"));
				} else {
					dataFim = null;
				}

				listaHorarios.add(
					new Horario(
						resultado.getInt("codigoInterno"),
						resultado.getInt("chaveHorarioTipo"),
						resultado.getInt("chaveFuncionario"),
						resultado.getString("sigla"),
						resultado.getString("modalidade"),
						resultado.getFloat("duracaoSemanal"),
						resultado.getTimestamp("inicioPF1"),
						resultado.getTimestamp("fimPF1"),
						resultado.getTimestamp("inicioPF2"),
						resultado.getTimestamp("fimPF2"),
						resultado.getTimestamp("inicioHN1"),
						resultado.getTimestamp("fimHN1"),
						resultado.getTimestamp("inicioHN2"),
						resultado.getTimestamp("fimHN2"),
						resultado.getTimestamp("inicioRefeicao"),
						resultado.getTimestamp("fimRefeicao"),
						resultado.getTime("descontoObrigatorio"),
						resultado.getTime("descontoMinimo"),
						resultado.getTimestamp("inicioExpediente"),
						resultado.getTimestamp("fimExpediente"),
						dataCumprir,
						dataInicioHorario,
						dataFim,
						resultado.getInt("numDias"),
						resultado.getInt("posicao"),
						resultado.getInt("quem"),
						resultado.getTimestamp("quando")));
			}
			sql.close();
		} catch (Exception e) {
			System.out.println("HorarioRelacional.lerRotacoesPorNumMecanografico: " + e.toString());
		} finally {
			return listaHorarios;
		}
	} /* lerRotacoesPorNumMecanografico */

	public int ultimoCodigoInterno() {
		int ultimo = 0;

		try {
			PreparedStatement sql = UtilRelacional.prepararComando("SELECT MAX(codigoInterno) FROM ass_HORARIO");

			ResultSet resultado = sql.executeQuery();
			if (resultado.next()) {
				ultimo = resultado.getInt(1);
			}
			sql.close();
		} catch (Exception e) {
			System.out.println("HorarioRelacional.ultimoCodigoInterno: " + e.toString());
		} finally {
			return ultimo;
		}
	} /* ultimoCodigoInterno */

	public int ultimoCodigoInternoExcepcaoHorario() {
		int ultimo = 0;

		try {
			PreparedStatement sql = UtilRelacional.prepararComando("SELECT MAX(codigoInterno) FROM ass_HORARIO_EXCEPCAO");

			ResultSet resultado = sql.executeQuery();
			if (resultado.next()) {
				ultimo = resultado.getInt(1);
			}
			sql.close();
		} catch (Exception e) {
			System.out.println("HorarioRelacional.ultimoCodigoInternoExcepcaoHorario: " + e.toString());
		} finally {
			return ultimo;
		}
	} /* ultimoCodigoInternoExcepcaoHorario */
}