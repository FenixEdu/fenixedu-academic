package ServidorPersistenteJDBC.Relacional;

import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import Dominio.Funcionario;
import Dominio.StatusAssiduidade;
import ServidorPersistenteJDBC.IFuncionarioPersistente;

/**
 * @author Fernanda Quitério e Tania Pousão
 */
public class FuncionarioRelacional implements IFuncionarioPersistente
{

	public boolean alterarFuncionario(Funcionario funcionario)
	{
		boolean resultado = false;

		try
		{
			PreparedStatement sql =
				UtilRelacional.prepararComando(
					"UPDATE ass_FUNCIONARIO SET "
						+ "codigoInterno = ? , "
						+ "chavePessoa = ? , "
						+ "numeroMecanografico = ? , "
						+ "chaveHorarioActual = ?, "
						+ "antiguidade = ? "
						+ "WHERE numeroMecanografico = ? ");

			sql.setInt(1, funcionario.getCodigoInterno());
			sql.setInt(2, funcionario.getChavePessoa());
			sql.setInt(3, funcionario.getNumeroMecanografico());
			sql.setInt(4, funcionario.getChaveHorarioActual());
			sql.setDate(5, new java.sql.Date(funcionario.getAntiguidade().getTime()));
			sql.setInt(6, funcionario.getNumeroMecanografico());
			//devido à alteração por numero mecanografico

			sql.executeUpdate();
			sql.close();
			resultado = true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("FuncionarioRelacional.alterarFuncionario: " + e.toString());
		}
		return resultado;
	} /* alterarFuncionario */

	public boolean alterarFuncionarioPorCodigoInterno(Funcionario funcionario)
	{
		boolean resultado = false;

		try
		{
			PreparedStatement sql =
				UtilRelacional.prepararComando(
					"UPDATE ass_FUNCIONARIO SET "
						+ "chavePessoa = ? , "
						+ "numeroMecanografico = ? , "
						+ "chaveHorarioActual = ?, "
						+ "antiguidade = ? "
						+ "WHERE codigoInterno = ? ");

			sql.setInt(1, funcionario.getChavePessoa());
			sql.setInt(2, funcionario.getNumeroMecanografico());
			sql.setInt(3, funcionario.getChaveHorarioActual());
			sql.setDate(4, new java.sql.Date(funcionario.getAntiguidade().getTime()));
			sql.setInt(5, funcionario.getCodigoInterno());

			sql.executeUpdate();
			sql.close();
			resultado = true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("FuncionarioRelacional.alterarFuncionario: " + e.toString());
		}
		return resultado;
	} /* alterarFuncionario */

	public boolean alterarHistoricoFuncionario(Funcionario funcionario)
	{
		boolean resultado = false;

		try
		{
			//chave do funcionário
			PreparedStatement sql =
				UtilRelacional.prepararComando(
					"SELECT * FROM ass_FUNCIONARIO WHERE numeroMecanografico = ?");
			sql.setInt(1, funcionario.getNumeroMecanografico());
			ResultSet resultadoQuery = sql.executeQuery();
			int chaveFuncionario = 0;
			if (resultadoQuery.next())
			{
				chaveFuncionario = resultadoQuery.getInt("codigoInterno");
			}
			else
			{
				sql.close();
				return resultado;
			}
			sql.close();

			//alteracao da ultima informação do histórico do funcionário
			sql =
				UtilRelacional.prepararComando(
					"SELECT * FROM ass_FUNCIONARIO_HISTORICO WHERE chaveFuncionario = ?");
			sql.setInt(1, chaveFuncionario);
			resultadoQuery = sql.executeQuery();

			if (resultadoQuery.last())
			{ //ultimo record deste funcionário, porque contem a informação actual

				PreparedStatement sql2 =
					UtilRelacional.prepararComando(
						"UPDATE ass_FUNCIONARIO_HISTORICO SET "
							+ "chaveFuncResponsavel = ? ,"
							+ "chaveCCLocalTrabalho = ? ,"
							+ "chaveCCCorrespondencia = ? ,"
							+ "chaveCCVencimento = ? ,"
							+ "calendario = ? ,"
							+ "chaveStatus = ? ,"
							+ "dataInicio = ? ,"
							+ "dataFim = ? ,"
							+ "quem = ? ,"
							+ "quando = ? "
							+ "WHERE chaveFuncionario = ? ");

				sql2.setObject(1, funcionario.getChaveFuncResponsavel());
				sql2.setObject(2, funcionario.getChaveCCLocalTrabalho());
				sql2.setObject(3, funcionario.getChaveCCCorrespondencia());
				sql2.setObject(4, funcionario.getChaveCCVencimento());
				sql2.setString(5, funcionario.getCalendario());
				sql2.setObject(6, funcionario.getChaveStatus());
				if (funcionario.getDataInicio() != null)
				{
					sql2.setDate(7, new java.sql.Date(funcionario.getDataInicio().getTime()));
				}
				else
				{
					sql2.setDate(7, null);
				}
				if (funcionario.getDataFim() != null)
				{
					sql2.setDate(8, new java.sql.Date(funcionario.getDataFim().getTime()));
				}
				else
				{
					sql2.setDate(8, null);
				}
				sql2.setInt(9, funcionario.getQuem());
				sql2.setTimestamp(10, new Timestamp(funcionario.getQuando().getTime()));
				sql2.setInt(11, chaveFuncionario);

				sql2.executeUpdate();
				sql2.close();
			}
			sql.close();
			resultado = true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("FuncionarioRelacional.alterarHistoricoFuncionario: " + e.toString());
		}
		return resultado;
	} /* alterarHistoricoFuncionario */

	public boolean alterarFuncResponsavel(Funcionario funcionario)
	{
		boolean resultado = false;

		try
		{
			//alteracao em todos os historicos do funcionário
			PreparedStatement sql =
				UtilRelacional.prepararComando(
					"SELECT * FROM ass_FUNCIONARIO WHERE numeroMecanografico = ?");
			sql.setInt(1, funcionario.getNumeroMecanografico());
			ResultSet resultadoQuery = sql.executeQuery();
			int chaveFuncionario = 0;
			if (resultadoQuery.next())
			{
				chaveFuncionario = resultadoQuery.getInt("codigoInterno");
			}
			else
			{
				sql.close();
				return resultado;
			}
			sql.close();

			sql =
				UtilRelacional.prepararComando(
					"SELECT * FROM ass_FUNCIONARIO_HISTORICO WHERE chaveFuncionario = ? AND chaveFuncResponsavel IS NOT NULL");
			sql.setInt(1, chaveFuncionario);
			resultadoQuery = sql.executeQuery();

			if (resultadoQuery.last())
			{ //ultimo record deste funcionário, porque contem a informação actual
				PreparedStatement sql2 =
					UtilRelacional.prepararComando(
						"UPDATE ass_FUNCIONARIO_HISTORICO SET "
							+ "chaveFuncResponsavel = ? ,"
							+ "dataInicio = ? ,"
							+ "dataFim = ? ,"
							+ "quem = ? ,"
							+ "quando = ? "
							+ "WHERE codigoInterno = ? ");

				sql2.setObject(1, funcionario.getChaveFuncResponsavel());
				if (funcionario.getDataInicio() != null)
				{
					sql2.setDate(2, new java.sql.Date(funcionario.getDataInicio().getTime()));
				}
				else
				{
					sql2.setDate(2, null);
				}
				if (funcionario.getDataFim() != null)
				{
					sql2.setDate(3, new java.sql.Date(funcionario.getDataFim().getTime()));
				}
				else
				{
					sql2.setDate(3, null);
				}
				sql2.setInt(4, funcionario.getQuem());
				sql2.setTimestamp(5, new Timestamp(funcionario.getQuando().getTime()));
				sql2.setInt(6, resultadoQuery.getInt("codigoInterno"));

				sql2.executeUpdate();
				sql2.close();
			}
			sql.close();
			resultado = true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("FuncionarioRelacional.alterarFuncResponsavel: " + e.toString());
		}
		return resultado;
	} /* alterarFuncResponsavel */

	public boolean alterarCCCorrespondenciaFuncionario(Funcionario funcionario)
	{
		boolean resultado = false;

		try
		{
			//alteracao em todos os historicos do funcionário
			PreparedStatement sql =
				UtilRelacional.prepararComando(
					"SELECT * FROM ass_FUNCIONARIO WHERE numeroMecanografico = ?");
			sql.setInt(1, funcionario.getNumeroMecanografico());
			ResultSet resultadoQuery = sql.executeQuery();
			int chaveFuncionario = 0;
			if (resultadoQuery.next())
			{
				chaveFuncionario = resultadoQuery.getInt("codigoInterno");
			}
			else
			{
				sql.close();
				return resultado;
			}
			sql.close();

			sql =
				UtilRelacional.prepararComando(
					"SELECT * FROM ass_FUNCIONARIO_HISTORICO WHERE chaveFuncionario = ?  AND chaveCCCorrespondencia IS NOT NULL");
			sql.setInt(1, chaveFuncionario);
			resultadoQuery = sql.executeQuery();

			if (resultadoQuery.last())
			{ //ultimo record deste funcionário, porque contem a informação actual
				PreparedStatement sql2 =
					UtilRelacional.prepararComando(
						"UPDATE ass_FUNCIONARIO_HISTORICO SET "
							+ "chaveCCCorrespondencia = ? ,"
							+ "dataInicio = ? ,"
							+ "dataFim = ? ,"
							+ "quem = ? ,"
							+ "quando = ? "
							+ "WHERE codigoInterno = ? ");

				sql2.setObject(1, funcionario.getChaveCCCorrespondencia());
				if (funcionario.getDataInicio() != null)
				{
					sql2.setDate(2, new java.sql.Date(funcionario.getDataInicio().getTime()));
				}
				else
				{
					sql2.setDate(2, null);
				}
				if (funcionario.getDataFim() != null)
				{
					sql2.setDate(3, new java.sql.Date(funcionario.getDataFim().getTime()));
				}
				else
				{
					sql2.setDate(3, null);
				}
				sql2.setInt(4, funcionario.getQuem());
				sql2.setTimestamp(5, new Timestamp(funcionario.getQuando().getTime()));
				sql2.setInt(6, resultadoQuery.getInt("codigoInterno"));

				sql2.executeUpdate();
				sql2.close();
			}
			sql.close();
			resultado = true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println(
				"FuncionarioRelacional.alterarCCCorrespondenciaFuncionario: " + e.toString());
		}
		return resultado;
	} /* alterarCCCorrespondenciaFuncionario */

	public boolean alterarCCLocalTrabalhoFuncionario(Funcionario funcionario)
	{
		boolean resultado = false;

		try
		{
			//alteracao em todos os historicos do funcionário
			PreparedStatement sql =
				UtilRelacional.prepararComando(
					"SELECT * FROM ass_FUNCIONARIO WHERE numeroMecanografico = ?");
			sql.setInt(1, funcionario.getNumeroMecanografico());
			ResultSet resultadoQuery = sql.executeQuery();
			int chaveFuncionario = 0;
			if (resultadoQuery.next())
			{
				chaveFuncionario = resultadoQuery.getInt("codigoInterno");
			}
			else
			{
				sql.close();
				return resultado;
			}
			sql.close();

			sql =
				UtilRelacional.prepararComando(
					"SELECT * FROM ass_FUNCIONARIO_HISTORICO WHERE chaveFuncionario = ? AND chaveCCLocalTrabalho IS NOT NULL");
			sql.setInt(1, chaveFuncionario);
			resultadoQuery = sql.executeQuery();

			if (resultadoQuery.last())
			{ //ultimo record deste funcionário, porque contem a informação actual
				PreparedStatement sql2 =
					UtilRelacional.prepararComando(
						"UPDATE ass_FUNCIONARIO_HISTORICO SET "
							+ "chaveCCLocalTrabalho = ? ,"
							+ "dataInicio = ? ,"
							+ "dataFim = ? ,"
							+ "quem = ? ,"
							+ "quando = ? "
							+ "WHERE codigoInterno = ? ");

				sql2.setObject(1, funcionario.getChaveCCLocalTrabalho());
				if (funcionario.getDataInicio() != null)
				{
					sql2.setDate(2, new java.sql.Date(funcionario.getDataInicio().getTime()));
				}
				else
				{
					sql2.setDate(2, null);
				}
				if (funcionario.getDataFim() != null)
				{
					sql2.setDate(3, new java.sql.Date(funcionario.getDataFim().getTime()));
				}
				else
				{
					sql2.setDate(3, null);
				}
				sql2.setInt(4, funcionario.getQuem());
				sql2.setTimestamp(5, new Timestamp(funcionario.getQuando().getTime()));
				sql2.setInt(6, resultadoQuery.getInt("codigoInterno"));

				sql2.executeUpdate();
				sql2.close();
			}
			sql.close();
			resultado = true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println(
				"FuncionarioRelacional.alterarCCLocalTrabalhoFuncionario: " + e.toString());
		}
		return resultado;
	} /* alterarCCLocalTrabalhoFuncionario */

	public boolean alterarCCVencimento(Funcionario funcionario)
	{
		boolean resultado = false;

		try
		{
			//alteracao em todos os historicos do funcionário
			PreparedStatement sql =
				UtilRelacional.prepararComando(
					"SELECT * FROM ass_FUNCIONARIO WHERE numeroMecanografico = ?");
			sql.setInt(1, funcionario.getNumeroMecanografico());
			ResultSet resultadoQuery = sql.executeQuery();
			int chaveFuncionario = 0;
			if (resultadoQuery.next())
			{
				chaveFuncionario = resultadoQuery.getInt("codigoInterno");
			}
			else
			{
				sql.close();
				return resultado;
			}
			sql.close();

			sql =
				UtilRelacional.prepararComando(
					"SELECT * FROM ass_FUNCIONARIO_HISTORICO WHERE chaveFuncionario = ? AND chaveCCVencimento IS NOT NULL");
			sql.setInt(1, chaveFuncionario);
			resultadoQuery = sql.executeQuery();

			if (resultadoQuery.last())
			{ //ultimo record deste funcionário, porque contem a informação actual
				PreparedStatement sql2 =
					UtilRelacional.prepararComando(
						"UPDATE ass_FUNCIONARIO_HISTORICO SET "
							+ "chaveCCVencimento = ? ,"
							+ "dataInicio = ? ,"
							+ "dataFim = ? ,"
							+ "quem = ? ,"
							+ "quando = ? "
							+ "WHERE codigoInterno = ? ");

				sql2.setObject(1, funcionario.getChaveCCVencimento());
				if (funcionario.getDataInicio() != null)
				{
					sql2.setDate(2, new java.sql.Date(funcionario.getDataInicio().getTime()));
				}
				else
				{
					sql2.setDate(2, null);
				}
				if (funcionario.getDataFim() != null)
				{
					sql2.setDate(3, new java.sql.Date(funcionario.getDataFim().getTime()));
				}
				else
				{
					sql2.setDate(3, null);
				}
				sql2.setInt(4, funcionario.getQuem());
				sql2.setTimestamp(5, new Timestamp(funcionario.getQuando().getTime()));
				sql2.setInt(6, resultadoQuery.getInt("codigoInterno"));

				sql2.executeUpdate();
				sql2.close();
			}
			sql.close();
			resultado = true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("FuncionarioRelacional.alterarCCVencimento: " + e.toString());
		}
		return resultado;
	} /* alterarCCVencimento */

	public boolean alterarCalendario(Funcionario funcionario)
	{
		boolean resultado = false;

		try
		{
			//alteracao em todos os historicos do funcionário
			PreparedStatement sql =
				UtilRelacional.prepararComando(
					"SELECT * FROM ass_FUNCIONARIO WHERE numeroMecanografico = ?");
			sql.setInt(1, funcionario.getNumeroMecanografico());
			ResultSet resultadoQuery = sql.executeQuery();
			int chaveFuncionario = 0;
			if (resultadoQuery.next())
			{
				chaveFuncionario = resultadoQuery.getInt("codigoInterno");
			}
			else
			{
				sql.close();
				return resultado;
			}
			sql.close();

			sql =
				UtilRelacional.prepararComando(
					"SELECT * FROM ass_FUNCIONARIO_HISTORICO WHERE chaveFuncionario = ? AND calendario IS NOT NULL");
			sql.setInt(1, chaveFuncionario);
			resultadoQuery = sql.executeQuery();

			if (resultadoQuery.last())
			{ //ultimo record deste funcionário, porque contem a informação actual
				PreparedStatement sql2 =
					UtilRelacional.prepararComando(
						"UPDATE ass_FUNCIONARIO_HISTORICO SET "
							+ "calendario = ? ,"
							+ "dataInicio = ? ,"
							+ "dataFim = ? ,"
							+ "quem = ? ,"
							+ "quando = ? "
							+ "WHERE codigoInterno = ? ");

				sql2.setString(1, funcionario.getCalendario());
				if (funcionario.getDataInicio() != null)
				{
					sql2.setDate(2, new java.sql.Date(funcionario.getDataInicio().getTime()));
				}
				else
				{
					sql2.setDate(2, null);
				}
				if (funcionario.getDataFim() != null)
				{
					sql2.setDate(3, new java.sql.Date(funcionario.getDataFim().getTime()));
				}
				else
				{
					sql2.setDate(3, null);
				}
				sql2.setInt(4, funcionario.getQuem());
				sql2.setTimestamp(5, new Timestamp(funcionario.getQuando().getTime()));
				sql2.setInt(6, resultadoQuery.getInt("codigoInterno"));

				sql2.executeUpdate();
				sql2.close();
			}
			sql.close();
			resultado = true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("FuncionarioRelacional.alterarCalendario: " + e.toString());
		}
		return resultado;
	} /* alterarCalendario */

	public boolean alterarStatusAssiduidade(Funcionario funcionario)
	{
		boolean resultado = false;

		try
		{
			//alteracao em todos os historicos do funcionário
			PreparedStatement sql =
				UtilRelacional.prepararComando(
					"SELECT * FROM ass_FUNCIONARIO WHERE numeroMecanografico = ?");
			sql.setInt(1, funcionario.getNumeroMecanografico());
			ResultSet resultadoQuery = sql.executeQuery();
			int chaveFuncionario = 0;
			if (resultadoQuery.next())
			{
				chaveFuncionario = resultadoQuery.getInt("codigoInterno");
			}
			else
			{
				sql.close();
				return resultado;
			}
			sql.close();

			sql =
				UtilRelacional.prepararComando(
					"SELECT * FROM ass_FUNCIONARIO_HISTORICO WHERE chaveFuncionario = ? AND chaveStatus IS NOT NULL");
			sql.setInt(1, chaveFuncionario);
			resultadoQuery = sql.executeQuery();

			if (resultadoQuery.last())
			{ //ultimo record deste funcionário, porque contem a informação actual
				PreparedStatement sql2 =
					UtilRelacional.prepararComando(
						"UPDATE ass_FUNCIONARIO_HISTORICO SET "
							+ "chaveStatus = ? ,"
							+ "dataInicio = ? ,"
							+ "dataFim = ? ,"
							+ "quem = ? ,"
							+ "quando = ? "
							+ "WHERE codigoInterno = ? ");

				sql2.setObject(1, funcionario.getChaveStatus());
				if (funcionario.getDataInicio() != null)
				{
					sql2.setDate(2, new java.sql.Date(funcionario.getDataInicio().getTime()));
				}
				else
				{
					sql2.setDate(2, null);
				}
				if (funcionario.getDataFim() != null)
				{
					sql2.setDate(3, new java.sql.Date(funcionario.getDataFim().getTime()));
				}
				else
				{
					sql2.setDate(3, null);
				}
				sql2.setInt(4, funcionario.getQuem());
				sql2.setTimestamp(5, new Timestamp(funcionario.getQuando().getTime()));
				sql2.setInt(6, resultadoQuery.getInt("codigoInterno"));

				sql2.executeUpdate();
				sql2.close();
			}
			sql.close();
			resultado = true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("FuncionarioRelacional.alterarStatusAssiduidade: " + e.toString());
		}
		return resultado;
	} /* alterarStatusAssiduidade */

	public boolean alterarFimValidade(int codigoInterno, Date dataFim)
	{
		boolean fimValidade = false;

		try
		{
			PreparedStatement sql =
				UtilRelacional.prepararComando(
					"UPDATE ass_FUNCIONARIO_HISTORICO SET "
						+ "dataFim = ? "
						+ " WHERE chaveFuncionario = ? AND dataFim IS NULL");

			sql.setDate(1, new java.sql.Date(dataFim.getTime()));
			sql.setInt(2, codigoInterno);

			sql.executeUpdate();
			sql.close();
			fimValidade = true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("FuncionarioRelacional.alterarFimValidade: " + e.toString());
		}
		return fimValidade;
	} /* alterarFimValidade */

	public boolean apagarFuncionario(int numero)
	{
		boolean resultado = false;

		try
		{
			PreparedStatement sql =
				UtilRelacional.prepararComando(
					"SELECT codigoInterno FROM ass_FUNCIONARIO WHERE numeroMecanografico = ?");

			sql.setInt(1, numero);
			ResultSet resultadoQuery = sql.executeQuery();
			if (resultadoQuery.next())
			{

				PreparedStatement sql2 =
					UtilRelacional.prepararComando(
						"DELETE FROM ass_FUNCIONARIO_HISTORICO WHERE chaveFuncionario = ?");

				sql2.setInt(1, resultadoQuery.getInt("codigoInterno"));
				sql2.executeUpdate();
				sql2.close();

				sql2 =
					UtilRelacional.prepararComando(
						"DELETE FROM ass_FUNCIONARIO WHERE numeroMecanografico = ?");

				sql2.setInt(1, numero);
				sql2.executeUpdate();
				sql2.close();

				resultado = true;
			}
			sql.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("FuncionarioRelacional.apagarFuncionario: " + e.toString());
		}
		return resultado;
	} /* apagarFuncionario */

	public boolean escreverFuncionario(Funcionario funcionario)
	{
		boolean resultado = false;

		try
		{
			PreparedStatement sql =
				UtilRelacional.prepararComando(
					"INSERT INTO ass_FUNCIONARIO "
						+ "(codigoInterno, chavePessoa, numeroMecanografico, chaveHorarioActual, antiguidade) "
						+ "VALUES (?, ?, ?, ?, ?)");

			sql.setInt(1, funcionario.getCodigoInterno());
			sql.setInt(2, funcionario.getChavePessoa());
			sql.setInt(3, funcionario.getNumeroMecanografico());
			sql.setInt(4, funcionario.getChaveHorarioActual());
			if (funcionario.getAntiguidade() != null)
			{
				sql.setDate(5, new java.sql.Date(funcionario.getAntiguidade().getTime()));
			}
			else
			{
				sql.setDate(5, null);
			}

			sql.executeUpdate();
			sql.close();

			resultado = true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("FuncionarioRelacional.escreverFuncionario: " + e.toString());
		}
		return resultado;
	} /* escreverFuncionario */

	public boolean escreverHistoricoFuncionario(Funcionario funcionario)
	{
		boolean resultado = false;

		try
		{
			PreparedStatement sql =
				UtilRelacional.prepararComando(
					"INSERT INTO ass_FUNCIONARIO_HISTORICO "
						+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

			sql.setInt(1, 0);
			sql.setInt(2, funcionario.getCodigoInterno());
			sql.setObject(3, funcionario.getChaveFuncResponsavel());
			sql.setObject(4, funcionario.getChaveCCLocalTrabalho());
			sql.setObject(5, funcionario.getChaveCCCorrespondencia());
			sql.setObject(6, funcionario.getChaveCCVencimento());
			sql.setString(7, funcionario.getCalendario());
			sql.setObject(8, funcionario.getChaveStatus());
			if (funcionario.getDataInicio() != null)
			{
				sql.setDate(9, new java.sql.Date(funcionario.getDataInicio().getTime()));
			}
			else
			{
				sql.setDate(9, null);
			}
			if (funcionario.getDataFim() != null)
			{
				sql.setDate(10, new java.sql.Date(funcionario.getDataFim().getTime()));
			}
			else
			{
				sql.setDate(10, null);
			}
			sql.setInt(11, funcionario.getQuem());
			if (funcionario.getQuando() != null)
			{
				sql.setTimestamp(12, new Timestamp(funcionario.getQuando().getTime()));
			}
			else
			{
				sql.setDate(12, null);
			}

			sql.executeUpdate();
			sql.close();

			resultado = true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("FuncionarioRelacional.escreverHistoricoFuncionario: " + e.toString());
		}
		return resultado;
	} /* escreverHistoricoFuncionario */

	public boolean escreverFimFuncResponsavel(int numMecanografico, Date fim)
	{
		boolean resultado = false;

		try
		{
			PreparedStatement sql =
				UtilRelacional.prepararComando(
					"SELECT codigoInterno FROM ass_FUNCIONARIO " + "WHERE numeroMecanografico = ?");
			sql.setInt(1, numMecanografico);

			ResultSet resultadoQuery = sql.executeQuery();

			int chaveFuncionario = 0;
			if (resultadoQuery.next())
			{
				chaveFuncionario = resultadoQuery.getInt("codigoInterno");
			}
			else
			{
				sql.close();
				return resultado;
			}
			sql.close();

			//escreve a data fim de assiduidade nos actuais registos do histórico
			sql =
				UtilRelacional.prepararComando(
					"UPDATE ass_FUNCIONARIO_HISTORICO SET "
						+ "dataFim = ? "
						+ " WHERE chaveFuncionario = ? AND dataFim IS NULL AND chaveFuncResponsavel IS NOT NULL");
			sql.setDate(1, new java.sql.Date(fim.getTime()));
			sql.setInt(2, chaveFuncionario);

			sql.executeUpdate();
			sql.close();

			resultado = true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("FuncionarioRelacional.escreverFimFuncResponsavel: " + e.toString());
		}
		return resultado;
	} /* escreverFimFuncResponsavel */

	public boolean escreverFuncResponsavel(Funcionario funcionario)
	{
		boolean resultado = false;

		try
		{
			PreparedStatement sql =
				UtilRelacional.prepararComando(
					"INSERT INTO ass_FUNCIONARIO_HISTORICO "
						+ "VALUES (?, ?, ?, null, null, null, null, null, ?, ?, ?, ?)");

			sql.setInt(1, 0);
			sql.setInt(2, funcionario.getCodigoInterno());
			sql.setObject(3, funcionario.getChaveFuncResponsavel());
			if (funcionario.getDataInicio() != null)
			{
				sql.setDate(4, new java.sql.Date(funcionario.getDataInicio().getTime()));
			}
			else
			{
				sql.setDate(4, null);
			}
			if (funcionario.getDataFim() != null)
			{
				sql.setDate(5, new java.sql.Date(funcionario.getDataFim().getTime()));
			}
			else
			{
				sql.setDate(5, null);
			}
			sql.setInt(6, funcionario.getQuem());
			if (funcionario.getQuando() != null)
			{
				sql.setTimestamp(7, new Timestamp(funcionario.getQuando().getTime()));
			}
			else
			{
				sql.setDate(7, null);
			}

			sql.executeUpdate();
			sql.close();

			resultado = true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("FuncionarioRelacional.escreverFuncResponsavel: " + e.toString());
		}
		return resultado;
	} /* escreverFuncResponsavel */

	public boolean escreverFimCCLocalTrabalho(int numMecanografico, Date fim)
	{
		boolean resultado = false;

		try
		{
			PreparedStatement sql =
				UtilRelacional.prepararComando(
					"SELECT codigoInterno FROM ass_FUNCIONARIO " + "WHERE numeroMecanografico = ?");
			sql.setInt(1, numMecanografico);

			ResultSet resultadoQuery = sql.executeQuery();

			int chaveFuncionario = 0;
			if (resultadoQuery.next())
			{
				chaveFuncionario = resultadoQuery.getInt("codigoInterno");
			}
			else
			{
				sql.close();
				return resultado;
			}
			sql.close();

			//escreve a data fim de assiduidade nos actuais registos do histórico
			sql =
				UtilRelacional.prepararComando(
					"UPDATE ass_FUNCIONARIO_HISTORICO SET "
						+ "dataFim = ? "
						+ " WHERE chaveFuncionario = ? AND dataFim IS NULL AND chaveCCLocalTrabalho IS NOT NULL");
			sql.setDate(1, new java.sql.Date(fim.getTime()));
			sql.setInt(2, chaveFuncionario);

			sql.executeUpdate();
			sql.close();

			resultado = true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("FuncionarioRelacional.escreverFimCCLocalTrabalho: " + e.toString());
		}
		return resultado;
	} /* escreverFimCCLocalTrabalho */

	public boolean escreverCCLocalTrabalho(Funcionario funcionario)
	{
		boolean resultado = false;

		try
		{
			PreparedStatement sql =
				UtilRelacional.prepararComando(
					"INSERT INTO ass_FUNCIONARIO_HISTORICO "
						+ "VALUES (?, ?, null, ?, null, null, null, null, ?, ?, ?, ?)");

			sql.setInt(1, 0);
			sql.setInt(2, funcionario.getCodigoInterno());
			sql.setObject(3, funcionario.getChaveCCLocalTrabalho());
			if (funcionario.getDataInicio() != null)
			{
				sql.setDate(4, new java.sql.Date(funcionario.getDataInicio().getTime()));
			}
			else
			{
				sql.setDate(4, null);
			}
			if (funcionario.getDataFim() != null)
			{
				sql.setDate(5, new java.sql.Date(funcionario.getDataFim().getTime()));
			}
			else
			{
				sql.setDate(5, null);
			}
			sql.setInt(6, funcionario.getQuem());
			if (funcionario.getQuando() != null)
			{
				sql.setTimestamp(7, new Timestamp(funcionario.getQuando().getTime()));
			}
			else
			{
				sql.setDate(7, null);
			}

			sql.executeUpdate();
			sql.close();

			resultado = true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("FuncionarioRelacional.escreverCCLocalTrabalho: " + e.toString());
		}
		return resultado;
	} /* escreverCCLocalTrabalho */

	public boolean escreverFimCCCorrespondencia(int numMecanografico, Date fim)
	{
		boolean resultado = false;

		try
		{
			PreparedStatement sql =
				UtilRelacional.prepararComando(
					"SELECT codigoInterno FROM ass_FUNCIONARIO " + "WHERE numeroMecanografico = ?");
			sql.setInt(1, numMecanografico);

			ResultSet resultadoQuery = sql.executeQuery();

			int chaveFuncionario = 0;
			if (resultadoQuery.next())
			{
				chaveFuncionario = resultadoQuery.getInt("codigoInterno");
			}
			else
			{
				sql.close();
				return resultado;
			}
			sql.close();

			//escreve a data fim de assiduidade nos actuais registos do histórico
			sql =
				UtilRelacional.prepararComando(
					"UPDATE ass_FUNCIONARIO_HISTORICO SET "
						+ "dataFim = ? "
						+ " WHERE chaveFuncionario = ? AND dataFim IS NULL AND chaveCCCorrespondencia IS NOT NULL");
			sql.setDate(1, new java.sql.Date(fim.getTime()));
			sql.setInt(2, chaveFuncionario);

			sql.executeUpdate();
			sql.close();

			resultado = true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("FuncionarioRelacional.escreverFimCCCorrespondencia: " + e.toString());
		}
		return resultado;
	} /* escreverFimCCCorrespondencia */

	public boolean escreverCCCorrespondencia(Funcionario funcionario)
	{
		boolean resultado = false;

		try
		{
			PreparedStatement sql =
				UtilRelacional.prepararComando(
					"INSERT INTO ass_FUNCIONARIO_HISTORICO "
						+ "VALUES (?, ?, null, null, ?, null, null, null, ?, ?, ?, ?)");

			sql.setInt(1, 0);
			sql.setInt(2, funcionario.getCodigoInterno());
			sql.setObject(3, funcionario.getChaveCCCorrespondencia());
			if (funcionario.getDataInicio() != null)
			{
				sql.setDate(4, new java.sql.Date(funcionario.getDataInicio().getTime()));
			}
			else
			{
				sql.setDate(4, null);
			}
			if (funcionario.getDataFim() != null)
			{
				sql.setDate(5, new java.sql.Date(funcionario.getDataFim().getTime()));
			}
			else
			{
				sql.setDate(5, null);
			}
			sql.setInt(6, funcionario.getQuem());
			if (funcionario.getQuando() != null)
			{
				sql.setTimestamp(7, new Timestamp(funcionario.getQuando().getTime()));
			}
			else
			{
				sql.setDate(7, null);
			}

			sql.executeUpdate();
			sql.close();

			resultado = true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("FuncionarioRelacional.escreverCCCorrespondencia: " + e.toString());
		}
		return resultado;
	} /* escreverCCCorrespondencia */

	public boolean escreverFimCCVencimento(int numMecanografico, Date fim)
	{
		boolean resultado = false;

		try
		{
			PreparedStatement sql =
				UtilRelacional.prepararComando(
					"SELECT codigoInterno FROM ass_FUNCIONARIO " + "WHERE numeroMecanografico = ?");
			sql.setInt(1, numMecanografico);

			ResultSet resultadoQuery = sql.executeQuery();

			int chaveFuncionario = 0;
			if (resultadoQuery.next())
			{
				chaveFuncionario = resultadoQuery.getInt("codigoInterno");
			}
			else
			{
				sql.close();
				return resultado;
			}
			sql.close();

			//escreve a data fim de assiduidade nos actuais registos do histórico
			sql =
				UtilRelacional.prepararComando(
					"UPDATE ass_FUNCIONARIO_HISTORICO SET "
						+ "dataFim = ? "
						+ " WHERE chaveFuncionario = ? AND dataFim IS NULL AND chaveCCVencimento IS NOT NULL");
			sql.setDate(1, new java.sql.Date(fim.getTime()));
			sql.setInt(2, chaveFuncionario);

			sql.executeUpdate();
			sql.close();

			resultado = true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("FuncionarioRelacional.escreverFimCCVencimento: " + e.toString());
		}
		return resultado;
	} /* escreverFimCCVencimento */

	public boolean escreverCCVencimento(Funcionario funcionario)
	{
		boolean resultado = false;

		try
		{
			PreparedStatement sql =
				UtilRelacional.prepararComando(
					"INSERT INTO ass_FUNCIONARIO_HISTORICO "
						+ "VALUES (?, ?, null, null, null, ?, null, null, ?, ?, ?, ?)");

			sql.setInt(1, 0);
			sql.setInt(2, funcionario.getCodigoInterno());
			sql.setObject(3, funcionario.getChaveCCVencimento());
			if (funcionario.getDataInicio() != null)
			{
				sql.setDate(4, new java.sql.Date(funcionario.getDataInicio().getTime()));
			}
			else
			{
				sql.setDate(4, null);
			}
			if (funcionario.getDataFim() != null)
			{
				sql.setDate(5, new java.sql.Date(funcionario.getDataFim().getTime()));
			}
			else
			{
				sql.setDate(5, null);
			}
			sql.setInt(6, funcionario.getQuem());
			if (funcionario.getQuando() != null)
			{
				sql.setTimestamp(7, new Timestamp(funcionario.getQuando().getTime()));
			}
			else
			{
				sql.setDate(7, null);
			}

			sql.executeUpdate();
			sql.close();

			resultado = true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("FuncionarioRelacional.escreverCCVencimento: " + e.toString());
		}
		return resultado;
	}

	public boolean escreverFimCalendario(int numMecanografico, Date fim)
	{
		boolean resultado = false;

		try
		{
			PreparedStatement sql =
				UtilRelacional.prepararComando(
					"SELECT codigoInterno FROM ass_FUNCIONARIO " + "WHERE numeroMecanografico = ?");
			sql.setInt(1, numMecanografico);

			ResultSet resultadoQuery = sql.executeQuery();

			int chaveFuncionario = 0;
			if (resultadoQuery.next())
			{
				chaveFuncionario = resultadoQuery.getInt("codigoInterno");
			}
			else
			{
				sql.close();
				return resultado;
			}
			sql.close();

			//escreve a data fim de assiduidade nos actuais registos do histórico
			sql =
				UtilRelacional.prepararComando(
					"UPDATE ass_FUNCIONARIO_HISTORICO SET "
						+ "dataFim = ? "
						+ " WHERE chaveFuncionario = ? AND dataFim IS NULL AND calendario IS NOT NULL");
			sql.setDate(1, new java.sql.Date(fim.getTime()));
			sql.setInt(2, chaveFuncionario);

			sql.executeUpdate();
			sql.close();

			resultado = true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("FuncionarioRelacional.escreverFimCalendario: " + e.toString());
		}
		return resultado;
	} /* escreverFimCalendario */

	public boolean escreverCalendario(Funcionario funcionario)
	{
		boolean resultado = false;

		try
		{
			PreparedStatement sql =
				UtilRelacional.prepararComando(
					"INSERT INTO ass_FUNCIONARIO_HISTORICO "
						+ "VALUES (?, ?, null, null, null, null, ?, null, ?, ?, ?, ?)");

			sql.setInt(1, 0);
			sql.setInt(2, funcionario.getCodigoInterno());
			sql.setString(3, funcionario.getCalendario());
			if (funcionario.getDataInicio() != null)
			{
				sql.setDate(4, new java.sql.Date(funcionario.getDataInicio().getTime()));
			}
			else
			{
				sql.setDate(4, null);
			}
			if (funcionario.getDataFim() != null)
			{
				sql.setDate(5, new java.sql.Date(funcionario.getDataFim().getTime()));
			}
			else
			{
				sql.setDate(5, null);
			}
			sql.setInt(6, funcionario.getQuem());
			if (funcionario.getQuando() != null)
			{
				sql.setTimestamp(7, new Timestamp(funcionario.getQuando().getTime()));
			}
			else
			{
				sql.setDate(7, null);
			}

			sql.executeUpdate();
			sql.close();

			resultado = true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("FuncionarioRelacional.escreverCalendario: " + e.toString());
		}
		return resultado;
	} /* escreverCalendario */

	public boolean escreverFimStatusAssiduidade(int numMecanografico, Date fim)
	{
		boolean resultado = false;

		try
		{
			PreparedStatement sql =
				UtilRelacional.prepararComando(
					"SELECT codigoInterno FROM ass_FUNCIONARIO " + "WHERE numeroMecanografico = ?");
			sql.setInt(1, numMecanografico);

			ResultSet resultadoQuery = sql.executeQuery();

			int chaveFuncionario = 0;
			if (resultadoQuery.next())
			{
				chaveFuncionario = resultadoQuery.getInt("codigoInterno");
			}
			else
			{
				sql.close();
				return resultado;
			}
			sql.close();

			//escreve a data fim de assiduidade nos actuais registos do histórico
			sql =
				UtilRelacional.prepararComando(
					"UPDATE ass_FUNCIONARIO_HISTORICO SET "
						+ "dataFim = ? "
						+ " WHERE chaveFuncionario = ? AND dataFim IS NULL AND chaveStatus IS NOT NULL");
			sql.setDate(1, new java.sql.Date(fim.getTime()));
			sql.setInt(2, chaveFuncionario);

			sql.executeUpdate();
			sql.close();

			resultado = true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("FuncionarioRelacional.escreverFimStatusAssiduidade: " + e.toString());
		}
		return resultado;
	} /* escreverFimStatusAssiduidade */

	public boolean escreverStatusAssiduidade(Funcionario funcionario)
	{
		boolean resultado = false;

		try
		{
			PreparedStatement sql =
				UtilRelacional.prepararComando(
					"INSERT INTO ass_FUNCIONARIO_HISTORICO "
						+ "VALUES (?, ?, null, null, null, null, null, ?, ?, ?, ?, ?)");

			sql.setInt(1, 0);
			sql.setInt(2, funcionario.getCodigoInterno());
			sql.setObject(3, funcionario.getChaveStatus());
			if (funcionario.getDataInicio() != null)
			{
				sql.setDate(4, new java.sql.Date(funcionario.getDataInicio().getTime()));
			}
			else
			{
				sql.setDate(4, null);
			}
			if (funcionario.getDataFim() != null)
			{
				sql.setDate(5, new java.sql.Date(funcionario.getDataFim().getTime()));
			}
			else
			{
				sql.setDate(5, null);
			}
			sql.setInt(6, funcionario.getQuem());
			if (funcionario.getQuando() != null)
			{
				sql.setTimestamp(7, new Timestamp(funcionario.getQuando().getTime()));
			}
			else
			{
				sql.setDate(7, null);
			}

			sql.executeUpdate();
			sql.close();

			resultado = true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("FuncionarioRelacional.escreverStatusAssiduidade: " + e.toString());
		}
		return resultado;
	}

	public boolean existeHistoricoFuncResponsavel(Funcionario funcionario)
	{
		boolean existeHistorico = false;
		try
		{
			PreparedStatement sql =
				UtilRelacional.prepararComando(
					"SELECT * FROM ass_FUNCIONARIO WHERE numeroMecanografico = ?");
			sql.setInt(1, funcionario.getNumeroMecanografico());

			ResultSet resultado = sql.executeQuery();

			if (resultado.next())
			{
				PreparedStatement sql2 =
					UtilRelacional.prepararComando(
						"SELECT * FROM ass_FUNCIONARIO_HISTORICO "
							+ "WHERE chaveFuncionario = ? AND chaveFuncResponsavel = ? "
							+ "AND dataInicio = ? AND dataFim = ?");
				sql2.setInt(1, resultado.getInt("codigoInterno"));
				sql2.setObject(2, funcionario.getChaveFuncResponsavel());
				if (funcionario.getDataInicio() != null)
				{
					sql2.setDate(3, new java.sql.Date(funcionario.getDataInicio().getTime()));
				}
				else
				{
					sql2.setDate(3, null);
				}
				if (funcionario.getDataFim() != null)
				{
					sql2.setDate(4, new java.sql.Date(funcionario.getDataFim().getTime()));
				}
				else
				{
					sql2.setDate(4, null);
				}

				ResultSet resultado2 = sql2.executeQuery();

				if (resultado2.next())
				{
					existeHistorico = true;
				}
				sql2.close();
			}
			sql.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("FuncionarioRelacional.existeHistoricoFuncResponsavel: " + e.toString());
		}
		return existeHistorico;

	} /* existeHistoricoFuncResponsavel */

	public boolean existeHistoricoCCLocalTrabalho(Funcionario funcionario)
	{
		boolean existeHistorico = false;
		try
		{
			PreparedStatement sql =
				UtilRelacional.prepararComando(
					"SELECT * FROM ass_FUNCIONARIO WHERE numeroMecanografico = ?");
			sql.setInt(1, funcionario.getNumeroMecanografico());

			ResultSet resultado = sql.executeQuery();

			if (resultado.next())
			{
				PreparedStatement sql2 =
					UtilRelacional.prepararComando(
						"SELECT * FROM ass_FUNCIONARIO_HISTORICO "
							+ "WHERE chaveFuncionario = ? AND chaveCCLocalTrabalho = ? "
							+ "AND dataInicio = ? AND dataFim = ?");
				sql2.setInt(1, resultado.getInt("codigoInterno"));
				sql2.setObject(2, funcionario.getChaveCCLocalTrabalho());
				if (funcionario.getDataInicio() != null)
				{
					sql2.setDate(3, new java.sql.Date(funcionario.getDataInicio().getTime()));
				}
				else
				{
					sql2.setDate(3, null);
				}
				if (funcionario.getDataFim() != null)
				{
					sql2.setDate(4, new java.sql.Date(funcionario.getDataFim().getTime()));
				}
				else
				{
					sql2.setDate(4, null);
				}

				ResultSet resultado2 = sql2.executeQuery();

				if (resultado2.next())
				{
					existeHistorico = true;
				}
				sql2.close();
			}
			sql.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("FuncionarioRelacional.existeHistoricoCCLocalTrabalho: " + e.toString());
		}
		return existeHistorico;
	} /* existeHistoricoCCLocalTrabalho */

	public boolean existeHistoricoCCCorrespondencia(Funcionario funcionario)
	{
		boolean existeHistorico = false;
		try
		{
			PreparedStatement sql =
				UtilRelacional.prepararComando(
					"SELECT * FROM ass_FUNCIONARIO WHERE numeroMecanografico = ?");
			sql.setInt(1, funcionario.getNumeroMecanografico());

			ResultSet resultado = sql.executeQuery();

			if (resultado.next())
			{
				PreparedStatement sql2 =
					UtilRelacional.prepararComando(
						"SELECT * FROM ass_FUNCIONARIO_HISTORICO "
							+ "WHERE chaveFuncionario = ? AND chaveCCCorrespondencia = ? "
							+ "AND dataInicio = ? AND dataFim = ?");
				sql2.setInt(1, resultado.getInt("codigoInterno"));
				sql2.setObject(2, funcionario.getChaveCCCorrespondencia());
				if (funcionario.getDataInicio() != null)
				{
					sql2.setDate(3, new java.sql.Date(funcionario.getDataInicio().getTime()));
				}
				else
				{
					sql2.setDate(3, null);
				}
				if (funcionario.getDataFim() != null)
				{
					sql2.setDate(4, new java.sql.Date(funcionario.getDataFim().getTime()));
				}
				else
				{
					sql2.setDate(4, null);
				}

				ResultSet resultado2 = sql2.executeQuery();

				if (resultado2.next())
				{
					existeHistorico = true;
				}
				sql2.close();
			}
			sql.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println(
				"FuncionarioRelacional.existeHistoricoCCCorrespondencia: " + e.toString());
		}
		return existeHistorico;

	} /* existeHistoricoCCCorrespondencia */

	public boolean existeHistoricoCCVencimento(Funcionario funcionario)
	{
		boolean existeHistorico = false;
		try
		{
			PreparedStatement sql =
				UtilRelacional.prepararComando(
					"SELECT * FROM ass_FUNCIONARIO WHERE numeroMecanografico = ?");
			sql.setInt(1, funcionario.getNumeroMecanografico());

			ResultSet resultado = sql.executeQuery();

			if (resultado.next())
			{
				PreparedStatement sql2 =
					UtilRelacional.prepararComando(
						"SELECT * FROM ass_FUNCIONARIO_HISTORICO "
							+ "WHERE chaveFuncionario = ? AND chaveCCVencimento = ? "
							+ "AND dataInicio = ? AND dataFim = ?");
				sql2.setInt(1, resultado.getInt("codigoInterno"));
				sql2.setObject(2, funcionario.getChaveCCVencimento());
				if (funcionario.getDataInicio() != null)
				{
					sql2.setDate(3, new java.sql.Date(funcionario.getDataInicio().getTime()));
				}
				else
				{
					sql2.setDate(3, null);
				}
				if (funcionario.getDataFim() != null)
				{
					sql2.setDate(4, new java.sql.Date(funcionario.getDataFim().getTime()));
				}
				else
				{
					sql2.setDate(4, null);
				}

				ResultSet resultado2 = sql2.executeQuery();

				if (resultado2.next())
				{
					existeHistorico = true;
				}
				sql2.close();
			}
			sql.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("FuncionarioRelacional.existeHistoricoCCVencimento: " + e.toString());
		}
		return existeHistorico;
	} /* existeHistoricoCCVencimento */

	public boolean existeHistoricoCalendario(Funcionario funcionario)
	{
		boolean existeHistorico = false;
		try
		{
			PreparedStatement sql =
				UtilRelacional.prepararComando(
					"SELECT * FROM ass_FUNCIONARIO WHERE numeroMecanografico = ?");
			sql.setInt(1, funcionario.getNumeroMecanografico());

			ResultSet resultado = sql.executeQuery();

			if (resultado.next())
			{
				PreparedStatement sql2 =
					UtilRelacional.prepararComando(
						"SELECT * FROM ass_FUNCIONARIO_HISTORICO "
							+ "WHERE chaveFuncionario = ? AND calendario = ?"
							+ " AND dataInicio = ? AND dataFim = ? ");
				sql2.setInt(1, resultado.getInt("codigoInterno"));
				sql2.setString(2, funcionario.getCalendario());
				if (funcionario.getDataInicio() != null)
				{
					sql2.setDate(3, new java.sql.Date(funcionario.getDataInicio().getTime()));
				}
				else
				{
					sql2.setDate(3, null);
				}
				if (funcionario.getDataFim() != null)
				{
					sql2.setDate(4, new java.sql.Date(funcionario.getDataFim().getTime()));
				}
				else
				{
					sql2.setDate(4, null);
				}

				ResultSet resultado2 = sql2.executeQuery();
				if (resultado2.next())
				{
					existeHistorico = true;
				}
				sql2.close();
			}
			sql.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("FuncionarioRelacional.existeHistoricoCalendario: " + e.toString());
		}
		return existeHistorico;
	} /* existeHistoricoCalendario */

	public boolean existeHistoricoStatusAssiduidade(Funcionario funcionario)
	{
		boolean existeHistorico = false;
		try
		{
			PreparedStatement sql =
				UtilRelacional.prepararComando(
					"SELECT * FROM ass_FUNCIONARIO WHERE numeroMecanografico = ?");
			sql.setInt(1, funcionario.getNumeroMecanografico());

			ResultSet resultado = sql.executeQuery();

			if (resultado.next())
			{
				PreparedStatement sql2 =
					UtilRelacional.prepararComando(
						"SELECT * FROM ass_FUNCIONARIO_HISTORICO "
							+ "WHERE chaveFuncionario = ? AND chaveStatus = ? "
							+ "AND dataInicio = ? AND dataFim = ?");
				sql2.setInt(1, resultado.getInt("codigoInterno"));
				sql2.setObject(2, funcionario.getChaveStatus());
				if (funcionario.getDataInicio() != null)
				{
					sql2.setDate(3, new java.sql.Date(funcionario.getDataInicio().getTime()));
				}
				else
				{
					sql2.setDate(3, null);
				}
				if (funcionario.getDataFim() != null)
				{
					sql2.setDate(4, new java.sql.Date(funcionario.getDataFim().getTime()));
				}
				else
				{
					sql2.setDate(4, null);
				}

				ResultSet resultado2 = sql2.executeQuery();
				if (resultado2.next())
				{
					existeHistorico = true;
				}
				sql2.close();
			}
			sql.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println(
				"FuncionarioRelacional.existeHistoricoStatusAssiduidade: " + e.toString());
		}
		{
			return existeHistorico;
		}
	} /* existeHistoricoStatusAssiduidade */

	public Funcionario lerFuncionarioSemHistorico(int codigoInterno)
	{
		Funcionario funcionario = null;

		try
		{
			PreparedStatement sql =
				UtilRelacional.prepararComando("SELECT * FROM ass_FUNCIONARIO WHERE codigoInterno = ?");

			sql.setInt(1, codigoInterno);

			ResultSet resultado = sql.executeQuery();
			if (resultado.next())
			{
				Date antiguidade = null;
				if (resultado.getString("antiguidade") != null)
				{
					antiguidade = java.sql.Date.valueOf(resultado.getString("antiguidade"));
				}

				funcionario =
					new Funcionario(
						resultado.getInt("codigoInterno"),
						resultado.getInt("chavePessoa"),
						resultado.getInt("numeroMecanografico"),
						resultado.getInt("chaveHorarioActual"),
						antiguidade);
			}
			sql.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println(
				"FuncionarioRelacional.lerFuncionarioSemHistoricoPorNumMecanografico: " + e.toString());
		}
		return funcionario;
	} /* lerFuncionarioSemHistoricoPorNumMecanografico */

	public Funcionario lerFuncionarioSemHistoricoPorNumMecanografico(int numMecanografico)
	{
		Funcionario funcionario = null;

		try
		{
			PreparedStatement sql =
				UtilRelacional.prepararComando(
					"SELECT * FROM ass_FUNCIONARIO WHERE numeroMecanografico = ?");

			sql.setInt(1, numMecanografico);

			ResultSet resultado = sql.executeQuery();
			if (resultado.next())
			{
				Date antiguidade = null;
				if (resultado.getString("antiguidade") != null)
				{
					antiguidade = java.sql.Date.valueOf(resultado.getString("antiguidade"));
				}

				funcionario =
					new Funcionario(
						resultado.getInt("codigoInterno"),
						resultado.getInt("chavePessoa"),
						resultado.getInt("numeroMecanografico"),
						resultado.getInt("chaveHorarioActual"),
						antiguidade);
			}
			sql.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println(
				"FuncionarioRelacional.lerFuncionarioSemHistoricoPorNumMecanografico: " + e.toString());
		}
		return funcionario;
	} /* lerFuncionarioSemHistoricoPorNumMecanografico */

	public Funcionario lerFuncionarioSemHistoricoPorPessoa(int chavePessoa)
	{
		Funcionario funcionario = null;
		try
		{
			PreparedStatement sql =
				UtilRelacional.prepararComando("SELECT * FROM ass_FUNCIONARIO WHERE chavePessoa = ?");
			sql.setInt(1, chavePessoa);
			ResultSet resultado = sql.executeQuery();
			if (resultado.next())
			{
				Date antiguidade = null;
				if (resultado.getString("antiguidade") != null)
				{
					antiguidade = java.sql.Date.valueOf(resultado.getString("antiguidade"));
				}

				//Funcionario
				funcionario =
					new Funcionario(
						resultado.getInt("codigoInterno"),
						resultado.getInt("chavePessoa"),
						resultado.getInt("numeroMecanografico"),
						resultado.getInt("chaveHorarioActual"),
						antiguidade);
			}
			sql.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("FuncionarioRelacional.lerFuncionarioPorPessoa: " + e.toString());
		}
		return funcionario;
	} /* lerFuncionarioPorPessoa */

	private Funcionario constroirFuncionario(ResultSet resultado, Funcionario funcionario)
	{
		try
		{
			while (resultado.next())
			{
				resultado.getInt("chaveFuncResponsavel");
				if (!resultado.wasNull())
				{
					funcionario.setChaveFuncResponsavel(
						new Integer(resultado.getInt("chaveFuncResponsavel")));
				}

				resultado.getInt("chaveCCLocalTrabalho");
				if (!resultado.wasNull())
				{
					funcionario.setChaveCCLocalTrabalho(
						new Integer(resultado.getInt("chaveCCLocalTrabalho")));
				}

				resultado.getInt("chaveCCCorrespondencia");
				if (!resultado.wasNull())
				{
					funcionario.setChaveCCCorrespondencia(
						new Integer(resultado.getInt("chaveCCCorrespondencia")));
				}

				resultado.getInt("chaveCCVencimento");
				if (!resultado.wasNull())
				{
					funcionario.setChaveCCVencimento(new Integer(resultado.getInt("chaveCCVencimento")));
				}

				resultado.getString("calendario");
				if (!resultado.wasNull())
				{
					funcionario.setCalendario(resultado.getString("calendario"));
				}

				resultado.getInt("chaveStatus");
				if (!resultado.wasNull())
				{
					funcionario.setChaveStatus(new Integer(resultado.getInt("chaveStatus")));
				}
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return funcionario;
	} /* constroirFuncionario */

	private ArrayList constroirListaFuncionarios(ResultSet resultado)
	{
		ArrayList listaFuncionarios = new ArrayList();
		Funcionario funcionario = null;

		try
		{
			while (resultado.next())
			{
				Date antiguidade = null;
				if (resultado.getString("antiguidade") != null)
				{
					antiguidade = java.sql.Date.valueOf(resultado.getString("antiguidade"));
				}

				//Funcionario
				funcionario =
					new Funcionario(
						resultado.getInt("codigoInterno"),
						resultado.getInt("chavePessoa"),
						resultado.getInt("numeroMecanografico"),
						resultado.getInt("chaveHorarioActual"),
						antiguidade);

				resultado.getInt("chaveFuncResponsavel");
				if (!resultado.wasNull())
				{
					funcionario.setChaveFuncResponsavel(
						new Integer(resultado.getInt("chaveFuncResponsavel")));
				}

				resultado.getInt("chaveCCLocalTrabalho");
				if (!resultado.wasNull())
				{
					funcionario.setChaveCCLocalTrabalho(
						new Integer(resultado.getInt("chaveCCLocalTrabalho")));
				}

				resultado.getInt("chaveCCCorrespondencia");
				if (!resultado.wasNull())
				{
					funcionario.setChaveCCCorrespondencia(
						new Integer(resultado.getInt("chaveCCCorrespondencia")));
				}

				resultado.getInt("chaveCCVencimento");
				if (!resultado.wasNull())
				{
					funcionario.setChaveCCVencimento(new Integer(resultado.getInt("chaveCCVencimento")));
				}

				resultado.getString("calendario");
				if (!resultado.wasNull())
				{
					funcionario.setCalendario(resultado.getString("calendario"));
				}

				resultado.getInt("chaveStatus");
				if (!resultado.wasNull())
				{
					funcionario.setChaveStatus(new Integer(resultado.getInt("chaveStatus")));
				}

			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return listaFuncionarios;
	} /* constroirFuncionario */

	public Funcionario lerFuncionario(int codigoInterno, Date dataConsulta)
	{
		Funcionario funcionario = null;
		try
		{
			PreparedStatement sql =
				UtilRelacional.prepararComando("SELECT * FROM ass_FUNCIONARIO WHERE codigoInterno = ?");
			sql.setInt(1, codigoInterno);
			ResultSet resultado = sql.executeQuery();
			if (resultado.next())
			{
				Date antiguidade = null;
				if (resultado.getString("antiguidade") != null)
				{
					antiguidade = java.sql.Date.valueOf(resultado.getString("antiguidade"));
				}

				//Funcionario
				funcionario =
					new Funcionario(
						resultado.getInt("codigoInterno"),
						resultado.getInt("chavePessoa"),
						resultado.getInt("numeroMecanografico"),
						resultado.getInt("chaveHorarioActual"),
						antiguidade);

				//Histórico de funcionário
				PreparedStatement sql2 =
					UtilRelacional.prepararComando(
						"SELECT * FROM ass_FUNCIONARIO_HISTORICO "
							+ "WHERE chaveFuncionario = ? AND "
							+ "((dataInicio <= ? AND dataFim IS NULL) OR (dataInicio <= ? AND dataFim IS NOT NULL AND dataFim >= ?)) "
							+ "ORDER BY dataInicio");
				sql2.setInt(1, resultado.getInt("codigoInterno"));
				sql2.setDate(2, new java.sql.Date(dataConsulta.getTime()));
				sql2.setDate(3, new java.sql.Date(dataConsulta.getTime()));
				sql2.setDate(4, new java.sql.Date(dataConsulta.getTime()));

				ResultSet resultado2 = sql2.executeQuery();

				funcionario = constroirFuncionario(resultado2, funcionario);

				sql2.close();
			}
			sql.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("FuncionarioRelacional.lerFuncionario: " + e.toString());
		}
		return funcionario;
	} /* lerFuncionario */

	public Funcionario lerFuncionarioPorFuncNaoDocente(int chaveFuncNaoDocente, Date dataConsulta)
	{
		Funcionario funcionario = null;
		try
		{
			PreparedStatement sql =
				UtilRelacional.prepararComando(
					"SELECT chaveFuncionario FROM ass_FUNC_NAO_DOCENTE WHERE codigoInterno = ?");
			sql.setInt(1, chaveFuncNaoDocente);
			ResultSet resultado = sql.executeQuery();
			int chaveFuncionario = 0;
			if (resultado.next())
			{
				chaveFuncionario = resultado.getInt("chaveFuncionario");
			}
			else
			{
				sql.close();
				return null;
			}
			sql.close();

			sql =
				UtilRelacional.prepararComando("SELECT * FROM ass_FUNCIONARIO WHERE codigoInterno = ?");
			sql.setInt(1, chaveFuncionario);
			resultado = sql.executeQuery();
			if (resultado.next())
			{
				Date antiguidade = null;
				if (resultado.getString("antiguidade") != null)
				{
					antiguidade = java.sql.Date.valueOf(resultado.getString("antiguidade"));
				}

				//Funcionario
				funcionario =
					new Funcionario(
						resultado.getInt("codigoInterno"),
						resultado.getInt("chavePessoa"),
						resultado.getInt("numeroMecanografico"),
						resultado.getInt("chaveHorarioActual"),
						antiguidade);

				//Histórico de funcionário
				PreparedStatement sql2 =
					UtilRelacional.prepararComando(
						"SELECT * FROM ass_FUNCIONARIO_HISTORICO "
							+ "WHERE chaveFuncionario = ? AND "
							+ "((dataInicio <= ? AND dataFim IS NULL) OR (dataInicio <= ? AND dataFim IS NOT NULL AND dataFim >= ?))");
				sql2.setInt(1, chaveFuncionario);
				sql2.setDate(2, new java.sql.Date(dataConsulta.getTime()));
				sql2.setDate(3, new java.sql.Date(dataConsulta.getTime()));
				sql2.setDate(4, new java.sql.Date(dataConsulta.getTime()));
				ResultSet resultado2 = sql2.executeQuery();

				funcionario = constroirFuncionario(resultado2, funcionario);

				sql2.close();
			}
			sql.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("FuncionarioRelacional.lerFuncionarioPorFuncNaoDocente: " + e.toString());
		}
		return funcionario;
	} /* lerFuncionarioPorFuncNaoDocente */

	public Funcionario lerFuncionarioPorNumMecanografico(int numero, Date dataConsulta)
	{
		Funcionario funcionario = null;
		try
		{
			PreparedStatement sql =
				UtilRelacional.prepararComando(
					"SELECT * FROM ass_FUNCIONARIO WHERE numeroMecanografico = ?");
			sql.setInt(1, numero);
			ResultSet resultado = sql.executeQuery();
			if (resultado.next())
			{
				Date antiguidade = null;
				if (resultado.getString("antiguidade") != null)
				{
					antiguidade = java.sql.Date.valueOf(resultado.getString("antiguidade"));
				}

				//Funcionario
				funcionario =
					new Funcionario(
						resultado.getInt("codigoInterno"),
						resultado.getInt("chavePessoa"),
						resultado.getInt("numeroMecanografico"),
						resultado.getInt("chaveHorarioActual"),
						antiguidade);

				//Histórico de funcionário
				PreparedStatement sql2 =
					UtilRelacional.prepararComando(
						"SELECT * FROM ass_FUNCIONARIO_HISTORICO "
							+ "WHERE chaveFuncionario = ? AND "
							+ "((dataInicio <= ? AND dataFim IS NULL) OR (dataInicio <= ? AND dataFim IS NOT NULL AND dataFim >= ?))");

				sql2.setInt(1, resultado.getInt("codigoInterno"));
				sql2.setDate(2, new java.sql.Date(dataConsulta.getTime()));
				sql2.setDate(3, new java.sql.Date(dataConsulta.getTime()));
				sql2.setDate(4, new java.sql.Date(dataConsulta.getTime()));

				ResultSet resultado2 = sql2.executeQuery();
				funcionario = constroirFuncionario(resultado2, funcionario);

				sql2.close();
				sql.close();
			}
			else
			{
				sql.close();
				return null;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println(
				"FuncionarioRelacional.lerFuncionarioPorNumMecanografico: " + e.toString());
		}
		return funcionario;
	} /* lerFuncionarioPorNumMecanografico */

	public Funcionario lerFuncionarioPorPessoa(int chavePessoa, Date dataConsulta)
	{
		Funcionario funcionario = null;
		try
		{
			PreparedStatement sql =
				UtilRelacional.prepararComando("SELECT * FROM ass_FUNCIONARIO WHERE chavePessoa = ?");
			sql.setInt(1, chavePessoa);
			ResultSet resultado = sql.executeQuery();
			if (resultado.next())
			{
				Date antiguidade = null;
				if (resultado.getString("antiguidade") != null)
				{
					antiguidade = java.sql.Date.valueOf(resultado.getString("antiguidade"));
				}

				//Funcionario
				funcionario =
					new Funcionario(
						resultado.getInt("codigoInterno"),
						resultado.getInt("chavePessoa"),
						resultado.getInt("numeroMecanografico"),
						resultado.getInt("chaveHorarioActual"),
						antiguidade);

				//Histórico de funcionário
				PreparedStatement sql2 =
					UtilRelacional.prepararComando(
						"SELECT * FROM ass_FUNCIONARIO_HISTORICO "
							+ "WHERE chaveFuncionario = ? AND "
							+ "((dataInicio <= ? AND dataFim IS NULL) OR (dataInicio <= ? AND dataFim IS NOT NULL AND dataFim >= ?)) "
							+ "ORDER BY dataInicio");
				sql2.setInt(1, resultado.getInt("codigoInterno"));
				sql2.setDate(2, new java.sql.Date(dataConsulta.getTime()));
				sql2.setDate(3, new java.sql.Date(dataConsulta.getTime()));
				sql2.setDate(4, new java.sql.Date(dataConsulta.getTime()));

				ResultSet resultado2 = sql2.executeQuery();
				funcionario = constroirFuncionario(resultado2, funcionario);

				sql2.close();
			}
			sql.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("FuncionarioRelacional.lerFuncionarioPorPessoa: " + e.toString());
		}
		return funcionario;
	} /* lerFuncionarioPorPessoa */

	public Funcionario lerHistorico(int codigoInterno)
	{
		Funcionario funcionario = null;

		try
		{
			PreparedStatement sql =
				UtilRelacional.prepararComando(
					"SELECT * FROM ass_FUNCIONARIO_HISTORICO " + "WHERE codigoInterno = ?");
			sql.setInt(1, codigoInterno);

			ResultSet resultado = sql.executeQuery();
			if (resultado.next())
			{
				PreparedStatement sql2 =
					UtilRelacional.prepararComando(
						"SELECT * FROM ass_FUNCIONARIO WHERE codigoInterno = ?");
				sql2.setInt(1, resultado.getInt("chaveFuncionario"));

				ResultSet resultado2 = sql2.executeQuery();
				if (resultado2.next())
				{
					Date dataInicio = null;
					if (resultado.getString("dataInicio") != null)
					{
						dataInicio = java.sql.Date.valueOf(resultado.getString("dataInicio"));
					}
					Date dataFim = null;
					if (resultado.getString("dataFim") != null)
					{
						dataFim = java.sql.Date.valueOf(resultado.getString("dataFim"));
					}
					Timestamp dataQuando = null;
					if (resultado.getString("quando") != null)
					{
						dataQuando = Timestamp.valueOf(resultado.getString("quando"));
					}

					Date antiguidade = null;
					if (resultado2.getString("antiguidade") != null)
					{
						antiguidade = java.sql.Date.valueOf(resultado2.getString("antiguidade"));
					}

					funcionario =
						new Funcionario(
							resultado2.getInt("codigoInterno"),
							resultado2.getInt("chavePessoa"),
							resultado2.getInt("numeroMecanografico"),
							resultado2.getInt("chaveHorarioActual"),
							antiguidade,
							codigoInterno,
							(Integer) resultado.getObject("chaveFuncResponsavel"),
							(Integer) resultado.getObject("chaveCCLocalTrabalho"),
							(Integer) resultado.getObject("chaveCCCorrespondencia"),
							(Integer) resultado.getObject("chaveCCVencimento"),
							resultado.getString("calendario"),
							(Integer) resultado.getObject("chaveStatus"),
							dataInicio,
							dataFim,
							resultado.getInt("quem"),
							dataQuando);
				}
				sql2.close();
			}
			sql.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("FuncionarioRelacional.lerHistoricoFuncionario: " + e.toString());
		}
		return funcionario;
	} /* lerHistoricoFuncionario */

	public List lerHistoricoFuncionario(int codigoInterno, Date data)
	{
		List listaHistoricosFuncionario = null;

		try
		{
			PreparedStatement sql =
				UtilRelacional.prepararComando("SELECT * FROM ass_FUNCIONARIO WHERE codigoInterno = ?");
			sql.setInt(1, codigoInterno);

			ResultSet resultado = sql.executeQuery();
			if (resultado.next())
			{
				PreparedStatement sql2 =
					UtilRelacional.prepararComando(
						"SELECT * FROM ass_FUNCIONARIO_HISTORICO "
							+ "WHERE chaveFuncionario = ? AND "
							+ "((dataInicio <= ? AND dataFim IS NULL) OR (dataInicio <= ? AND dataFim IS NOT NULL AND dataFim >= ?)) "
							+ "ORDER BY dataInicio");
				sql2.setInt(1, resultado.getInt("codigoInterno"));
				sql2.setDate(2, new java.sql.Date(data.getTime()));
				sql2.setDate(3, new java.sql.Date(data.getTime()));
				sql2.setDate(4, new java.sql.Date(data.getTime()));

				ResultSet resultado2 = sql2.executeQuery();
				listaHistoricosFuncionario = new ArrayList();
				while (resultado2.next())
				{
					Date dataInicio = null;
					if (resultado2.getString("dataInicio") != null)
					{
						dataInicio = java.sql.Date.valueOf(resultado2.getString("dataInicio"));
					}
					Date dataFim = null;
					if (resultado2.getString("dataFim") != null)
					{
						dataFim = java.sql.Date.valueOf(resultado2.getString("dataFim"));
					}
					Timestamp dataQuando = null;
					if (resultado2.getString("quando") != null)
					{
						dataQuando = Timestamp.valueOf(resultado2.getString("quando"));
					}

					Date antiguidade = null;
					if (resultado.getString("antiguidade") != null)
					{
						antiguidade = java.sql.Date.valueOf(resultado.getString("antiguidade"));
					}

					listaHistoricosFuncionario.add(
						new Funcionario(
							resultado.getInt("codigoInterno"),
							resultado.getInt("chavePessoa"),
							resultado.getInt("numeroMecanografico"),
							resultado.getInt("chaveHorarioActual"),
							antiguidade,
							resultado2.getInt("codigoInterno"),
							(Integer) resultado2.getObject("chaveFuncResponsavel"),
							(Integer) resultado2.getObject("chaveCCLocalTrabalho"),
							(Integer) resultado2.getObject("chaveCCCorrespondencia"),
							(Integer) resultado2.getObject("chaveCCVencimento"),
							resultado2.getString("calendario"),
							(Integer) resultado2.getObject("chaveStatus"),
							dataInicio,
							dataFim,
							resultado2.getInt("quem"),
							dataQuando));
				}
				sql2.close();
			}
			sql.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("FuncionarioRelacional.lerHistoricoFuncionario: " + e.toString());
		}
		return listaHistoricosFuncionario;
	} /* lerHistoricoFuncionario */

	public ArrayList lerStatusAssiduidade(
		int numMecanografico,
		Timestamp dataInicial,
		Timestamp dataFinal)
	{
		ArrayList listaStatusAssiduidade = null;
		try
		{
			PreparedStatement sql =
				UtilRelacional.prepararComando(
					"SELECT * FROM ass_FUNCIONARIO where numeroMecanografico = ?");
			sql.setInt(1, numMecanografico);
			ResultSet resultado = sql.executeQuery();
			if (resultado.next())
			{

				PreparedStatement sql2 =
					UtilRelacional.prepararComando(
						"SELECT DISTINCT chaveStatus FROM ass_FUNCIONARIO_HISTORICO "
							+ "WHERE chaveFuncionario = ? AND ((dataInicio BETWEEN ? AND ?) "
							+ "OR (dataFim IS NOT NULL AND dataFim BETWEEN ? AND ?) "
							+ "OR (dataInicio <= ? AND dataFim IS NOT NULL AND dataFim >= ?) "
							+ "OR (dataInicio <= ? AND dataFim IS NULL))");
				sql2.setInt(1, resultado.getInt("codigoInterno"));
				sql2.setDate(2, new java.sql.Date(dataInicial.getTime()));
				sql2.setDate(3, new java.sql.Date(dataFinal.getTime()));
				sql2.setDate(4, new java.sql.Date(dataInicial.getTime()));
				sql2.setDate(5, new java.sql.Date(dataFinal.getTime()));
				sql2.setDate(6, new java.sql.Date(dataInicial.getTime()));
				sql2.setDate(7, new java.sql.Date(dataFinal.getTime()));
				sql2.setDate(8, new java.sql.Date(dataInicial.getTime()));
				ResultSet resultado2 = sql2.executeQuery();
				PreparedStatement sql3 =
					UtilRelacional.prepararComando("SELECT * FROM ass_STATUS where codigoInterno = ?");
				ResultSet resultado3 = null;
				listaStatusAssiduidade = new ArrayList();
				while (resultado2.next())
				{
					sql3.setInt(1, resultado2.getInt("chaveStatus"));
					resultado3 = sql3.executeQuery();
					if (resultado3.next())
					{
						listaStatusAssiduidade.add(
							new StatusAssiduidade(
								resultado3.getInt("codigoInterno"),
								resultado3.getString("sigla"),
								resultado3.getString("designacao"),
								resultado3.getString("estado"),
								resultado3.getString("assiduidade")));
					}
				}
				sql3.close();
				sql2.close();
			}
			sql.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("FuncionarioRelacional.lerStatusAssiduidade: " + e.toString());
			return null;
		}
		return listaStatusAssiduidade;
	} /* lerStatusAssiduidade */

	public ArrayList lerFuncionariosCCLocalTrabalho(int chaveCCLocalTrabalho, Date data)
	{
		//TODO: REVER
		ArrayList listaFuncionarios = null;
		try
		{
			PreparedStatement sql =
				UtilRelacional.prepararComando(
					"SELECT * FROM ass_FUNCIONARIO_HISTORICO "
						+ "WHERE chaveCCLocalTrabalho = ? "
						+ "AND ((dataInicio <= ? AND dataFim IS NULL) OR (dataInicio <= ? AND dataFim IS NOT NULL AND dataFim >= ?)) "
						+ "ORDER BY chaveFuncionario");
			sql.setInt(1, chaveCCLocalTrabalho);
			sql.setDate(2, new java.sql.Date(data.getTime()));
			sql.setDate(3, new java.sql.Date(data.getTime()));
			sql.setDate(4, new java.sql.Date(data.getTime()));
			ResultSet resultado = sql.executeQuery();

			PreparedStatement sql2 =
				UtilRelacional.prepararComando(
					"SELECT * FROM ass_FUNCIONARIO " + "WHERE codigoInterno=? ");
			ResultSet resultado2 = null;
			listaFuncionarios = new ArrayList();
			Date dataInicio = null;
			Date dataFim = null;
			Timestamp dataQuando = null;
			while (resultado.next())
			{
				sql2.setInt(1, resultado.getInt("chaveFuncionario"));
				resultado2 = sql2.executeQuery();
				if (resultado2.next())
				{
					Date antiguidade = null;
					if (resultado2.getString("antiguidade") != null)
					{
						antiguidade = java.sql.Date.valueOf(resultado2.getString("antiguidade"));
					}
					if (resultado.getString("dataInicio") != null)
					{
						dataInicio = java.sql.Date.valueOf(resultado.getString("dataInicio"));
					}
					if (resultado.getString("dataFim") != null)
					{
						dataFim = java.sql.Date.valueOf(resultado.getString("dataFim"));
					}
					if (resultado.getString("quando") != null)
					{
						dataQuando = Timestamp.valueOf(resultado.getString("quando"));
					}

					listaFuncionarios.add(
						new Funcionario(
							resultado2.getInt("codigoInterno"),
							resultado2.getInt("chavePessoa"),
							resultado2.getInt("numeroMecanografico"),
							resultado2.getInt("chaveHorarioActual"),
							antiguidade,
							(Integer) resultado.getObject("chaveFuncResponsavel"),
							(Integer) resultado.getObject("chaveCCLocalTrabalho"),
							(Integer) resultado.getObject("chaveCCCorrespondencia"),
							(Integer) resultado.getObject("chaveCCVencimento"),
							resultado.getString("calendario"),
							(Integer) resultado.getObject("chaveStatus"),
							dataInicio,
							dataFim,
							resultado.getInt("quem"),
							dataQuando));
				}
			}
			sql2.close();
			sql.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("FuncionarioRelacional.lerFuncionariosCCLocalTrabalho: " + e.toString());
			return null;
		}
		return listaFuncionarios;
	} /* lerFuncionariosCCLocalTrabalho */

	public ArrayList lerTodosFuncionarios(Date dataConsulta)
	{
		//	TODO: REVER
		ArrayList listaFuncionarios = null;
		try
		{
			PreparedStatement sql =
				UtilRelacional.prepararComando(
					"SELECT * FROM ass_FUNCIONARIO ORDER BY numeroMecanografico");
			ResultSet resultado = sql.executeQuery();
			PreparedStatement sql2 =
				UtilRelacional.prepararComando(
					"SELECT * FROM ass_FUNCIONARIO_HISTORICO "
						+ "WHERE chaveFuncionario = ? AND "
						+ "((dataInicio <= ? AND dataFim IS NULL) OR (dataInicio <= ? AND dataFim IS NOT NULL AND dataFim >= ?))");
			sql2.setDate(2, new java.sql.Date(dataConsulta.getTime()));
			sql2.setDate(3, new java.sql.Date(dataConsulta.getTime()));
			sql2.setDate(4, new java.sql.Date(dataConsulta.getTime()));
			ResultSet resultado2 = null;
			listaFuncionarios = new ArrayList();
			Date dataInicio = null;
			Date dataFim = null;
			Timestamp dataQuando = null;
			while (resultado.next())
			{

				sql2.setInt(1, resultado.getInt("codigoInterno"));
				resultado2 = sql2.executeQuery();
				if (resultado2.last())
				{
					Date antiguidade = null;
					if (resultado.getString("antiguidade") != null)
					{
						antiguidade = java.sql.Date.valueOf(resultado.getString("antiguidade"));
					}
					if (resultado2.getString("dataInicio") != null)
					{
						dataInicio = java.sql.Date.valueOf(resultado2.getString("dataInicio"));
					}
					if (resultado2.getString("dataFim") != null)
					{
						dataFim = java.sql.Date.valueOf(resultado2.getString("dataFim"));
					}
					if (resultado2.getString("quando") != null)
					{
						dataQuando = Timestamp.valueOf(resultado2.getString("quando"));
					}

					listaFuncionarios.add(
						new Funcionario(
							resultado.getInt("codigoInterno"),
							resultado.getInt("chavePessoa"),
							resultado.getInt("numeroMecanografico"),
							resultado.getInt("chaveHorarioActual"),
							antiguidade,
							(Integer) resultado2.getObject("chaveFuncResponsavel"),
							(Integer) resultado2.getObject("chaveCCLocalTrabalho"),
							(Integer) resultado2.getObject("chaveCCCorrespondencia"),
							(Integer) resultado2.getObject("chaveCCVencimento"),
							resultado2.getString("calendario"),
							(Integer) resultado2.getObject("chaveStatus"),
							dataInicio,
							dataFim,
							resultado2.getInt("quem"),
							dataQuando));
				}
			}
			sql2.close();
			sql.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("FuncionarioRelacional.lerTodosFuncionarios: " + e.toString());
			return null;
		}
		return listaFuncionarios;
	} /* lerTodosFuncionarios */

	public ArrayList lerTodosFuncionariosAssiduidade(Date dataConsulta)
	{
		//	TODO: REVER
		ArrayList listaFuncionarios = null;
		try
		{
			PreparedStatement sql =
				UtilRelacional.prepararComando(
					"SELECT * FROM ass_FUNCIONARIO where chaveHorarioActual <> 0 ORDER BY numeroMecanografico");
			ResultSet resultado = sql.executeQuery();
			PreparedStatement sql2 =
				UtilRelacional.prepararComando(
					"SELECT * FROM ass_FUNCIONARIO_HISTORICO "
						+ "WHERE chaveFuncionario = ? AND "
						+ "((dataInicio <= ? AND dataFim IS NULL) OR (dataInicio <= ? AND dataFim IS NOT NULL AND dataFim >= ?))");
			sql2.setDate(2, new java.sql.Date(dataConsulta.getTime()));
			sql2.setDate(3, new java.sql.Date(dataConsulta.getTime()));
			sql2.setDate(4, new java.sql.Date(dataConsulta.getTime()));
			ResultSet resultado2 = null;
			listaFuncionarios = new ArrayList();
			Date dataInicio = null;
			Date dataFim = null;
			Timestamp dataQuando = null;
			while (resultado.next())
			{

				sql2.setInt(1, resultado.getInt("codigoInterno"));
				resultado2 = sql2.executeQuery();
				if (resultado2.last())
				{
					Date antiguidade = null;
					if (resultado.getString("antiguidade") != null)
					{
						antiguidade = java.sql.Date.valueOf(resultado.getString("antiguidade"));
					}
					if (resultado2.getString("dataInicio") != null)
					{
						dataInicio = java.sql.Date.valueOf(resultado2.getString("dataInicio"));
					}
					if (resultado2.getString("dataFim") != null)
					{
						dataFim = java.sql.Date.valueOf(resultado2.getString("dataFim"));
					}
					if (resultado2.getString("quando") != null)
					{
						dataQuando = Timestamp.valueOf(resultado2.getString("quando"));
					}

					listaFuncionarios.add(
						new Funcionario(
							resultado.getInt("codigoInterno"),
							resultado.getInt("chavePessoa"),
							resultado.getInt("numeroMecanografico"),
							resultado.getInt("chaveHorarioActual"),
							antiguidade,
							(Integer) resultado2.getObject("chaveFuncResponsavel"),
							(Integer) resultado2.getObject("chaveCCLocalTrabalho"),
							(Integer) resultado2.getObject("chaveCCCorrespondencia"),
							(Integer) resultado2.getObject("chaveCCVencimento"),
							resultado2.getString("calendario"),
							(Integer) resultado2.getObject("chaveStatus"),
							dataInicio,
							dataFim,
							resultado2.getInt("quem"),
							dataQuando));
				}
			}
			sql2.close();
			sql.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("FuncionarioRelacional.lerTodosFuncionariosAssiduidade: " + e.toString());
			return null;
		}
		return listaFuncionarios;
	} /* lerTodosFuncionariosAssiduidade */

	public ArrayList lerTodosFuncionariosNaoDocentes(Date dataConsulta)
	{
		//	TODO: REVER
		ArrayList listaFuncionarios = null;
		try
		{
			PreparedStatement sql =
				UtilRelacional.prepararComando(
					"SELECT * FROM ass_FUNCIONARIO ORDER BY numeroMecanografico");
			ResultSet resultado = sql.executeQuery();
			PreparedStatement sql2 =
				UtilRelacional.prepararComando(
					"SELECT * FROM ass_FUNC_NAO_DOCENTE WHERE chaveFuncionario = ?");
			ResultSet resultado2 = null;
			PreparedStatement sql3 =
				UtilRelacional.prepararComando(
					"SELECT * FROM ass_FUNCIONARIO_HISTORICO "
						+ "WHERE chaveFuncionario = ? AND "
						+ "((dataInicio <= ? AND dataFim IS NULL) OR (dataInicio <= ? AND dataFim IS NOT NULL AND dataFim >= ?))");
			sql3.setDate(2, new java.sql.Date(dataConsulta.getTime()));
			sql3.setDate(3, new java.sql.Date(dataConsulta.getTime()));
			sql3.setDate(4, new java.sql.Date(dataConsulta.getTime()));
			ResultSet resultado3 = null;
			listaFuncionarios = new ArrayList();
			Date dataInicio = null;
			Date dataFim = null;
			Timestamp dataQuando = null;
			while (resultado.next())
			{
				sql2.setInt(1, resultado.getInt("codigoInterno"));
				resultado2 = sql2.executeQuery();
				if (resultado2.next())
				{
					sql3.setInt(1, resultado.getInt("codigoInterno"));
					resultado3 = sql3.executeQuery();
					if (resultado3.last())
					{
						Date antiguidade = null;
						if (resultado.getString("antiguidade") != null)
						{
							antiguidade = java.sql.Date.valueOf(resultado.getString("antiguidade"));
						}
						if (resultado3.getString("dataInicio") != null)
						{
							dataInicio = java.sql.Date.valueOf(resultado3.getString("dataInicio"));
						}
						if (resultado3.getString("dataFim") != null)
						{
							dataFim = java.sql.Date.valueOf(resultado3.getString("dataFim"));
						}
						if (resultado3.getString("quando") != null)
						{
							dataQuando = Timestamp.valueOf(resultado3.getString("quando"));
						}

						listaFuncionarios.add(
							new Funcionario(
								resultado.getInt("codigoInterno"),
								resultado.getInt("chavePessoa"),
								resultado.getInt("numeroMecanografico"),
								resultado.getInt("chaveHorarioActual"),
								antiguidade,
								(Integer) resultado3.getObject("chaveFuncResponsavel"),
								(Integer) resultado3.getObject("chaveCCLocalTrabalho"),
								(Integer) resultado3.getObject("chaveCCCorrespondencia"),
								(Integer) resultado3.getObject("chaveCCVencimento"),
								resultado3.getString("calendario"),
								(Integer) resultado3.getObject("chaveStatus"),
								dataInicio,
								dataFim,
								resultado3.getInt("quem"),
								dataQuando));
					}
				}
			}
			sql3.close();
			sql2.close();
			sql.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("FuncionarioRelacional.lerTodosFuncionariosNaoDocentes: " + e.toString());
			return null;
		}
		return listaFuncionarios;
	} /* lerTodosFuncionariosNaoDocentes */

	public ArrayList lerTodosNumeros()
	{
		ArrayList listaNumeros = null;
		try
		{
			PreparedStatement sql =
				UtilRelacional.prepararComando("SELECT numeroMecanografico FROM ass_FUNCIONARIO");
			ResultSet resultado = sql.executeQuery();
			listaNumeros = new ArrayList();
			while (resultado.next())
			{
				listaNumeros.add(new Integer(resultado.getInt("numeroMecanografico")));
			}
			sql.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("FuncionarioRelacional.lerTodosNumeros: " + e.toString());
			e.printStackTrace();
			return null;
		}
		return listaNumeros;
	} /* lerTodosNumeros */

	public ArrayList lerTodosNumerosAssiduidade()
	{
		ArrayList listaNumeros = null;
		try
		{
			PreparedStatement sql =
				UtilRelacional.prepararComando("SELECT DISTINCT chaveFuncionario FROM ass_HORARIO");
			ResultSet resultado = sql.executeQuery();
			PreparedStatement sqlChaveFuncionario =
				UtilRelacional.prepararComando(
					"SELECT numeroMecanografico FROM ass_FUNCIONARIO " + "WHERE codigoInterno = ?");
			ResultSet resultadoChaveFuncionario = null;
			listaNumeros = new ArrayList();
			while (resultado.next())
			{ //chave do Funcionario
				sqlChaveFuncionario.setInt(1, resultado.getInt("chaveFuncionario"));
				resultadoChaveFuncionario = sqlChaveFuncionario.executeQuery();
				if (resultadoChaveFuncionario.next())
				{
					listaNumeros.add(
						new Integer(resultadoChaveFuncionario.getInt("numeroMecanografico")));
				}
			}
			sqlChaveFuncionario.close();
			sql.close();
		}
		catch (Exception e)
		{
			System.out.println("FuncionarioRelacional.lerTodosNumerosAssiduidade: " + e.toString());
			e.printStackTrace();
			return null;
		}
		return listaNumeros;
	} /* lerTodosNumerosAssiduidade */

	public ArrayList lerTodosNumerosAssiduidade(Timestamp dataInicioConsulta)
	{
		ArrayList listaNumeros = null;
		try
		{
			PreparedStatement sql =
				UtilRelacional.prepararComando(
					"SELECT numeroMecanografico FROM ass_FUNCIONARIO ORDER BY numeroMecanografico");
			ResultSet resultado = sql.executeQuery();
			PreparedStatement sqlChaveFuncionario =
				UtilRelacional.prepararComando(
					"SELECT codigoInterno FROM ass_FUNCIONARIO " + "WHERE numeroMecanografico = ?");
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
			while (resultado.next())
			{ //chave do Funcionario
				sqlChaveFuncionario.setInt(1, resultado.getInt("numeroMecanografico"));
				resultadoChaveFuncionario = sqlChaveFuncionario.executeQuery();
				if (resultadoChaveFuncionario.next())
				{ //Assiduidade Funcionário
					sqlAssiduidade.setInt(1, resultadoChaveFuncionario.getInt("codigoInterno"));
					resultadoAssiduidade = sqlAssiduidade.executeQuery();
					if (resultadoAssiduidade.next())
					{ //Funcionario a cumprir assiduidade nesta data
						listaNumeros.add(new Integer(resultado.getInt("numeroMecanografico")));
					}
				}
			}
			sqlChaveFuncionario.close();
			sqlAssiduidade.close();
			sql.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("FuncionarioRelacional.lerTodosNumerosAssiduidade: " + e.toString());
			return null;
		}
		return listaNumeros;
	} /* lerTodosNumerosAssiduidade */

	public ArrayList lerTodosNumerosFuncNaoDocentes()
	{
		ArrayList listaNumeros = null;
		try
		{
			PreparedStatement sql =
				UtilRelacional.prepararComando(
					"SELECT * FROM ass_FUNCIONARIO ORDER BY numeroMecanografico");
			ResultSet resultado = sql.executeQuery();
			PreparedStatement sql2 =
				UtilRelacional.prepararComando(
					"SELECT * FROM ass_FUNC_NAO_DOCENTE WHERE chaveFuncionario = ?");
			ResultSet resultado2 = null;
			listaNumeros = new ArrayList();
			while (resultado.next())
			{
				sql2.setInt(1, resultado.getInt("codigoInterno"));
				resultado2 = sql2.executeQuery();
				if (resultado2.next())
				{ //funcionario a cumprir assiduidade
					listaNumeros.add(new Integer(resultado.getInt("numeroMecanografico")));
				}
			}
			sql2.close();
			sql.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("FuncionarioRelacional.lerTodosNumerosMecanograficos: " + e.toString());
			return null;
		}
		return listaNumeros;
	} /* lerTodosNumerosMecanograficos */

	public boolean sobreposicaoHistoricos(int codigoInterno, Date dataInicio, Date dataFim)
	{
		boolean existeSobreposicao = false;
		try
		{
			PreparedStatement sql = null;
			if (dataFim == null)
			{
				sql =
					UtilRelacional.prepararComando(
						"SELECT * FROM ass_FUNCIONARIO_HISTORICO "
							+ "WHERE chaveFuncionario = ? AND "
							+ "((dataInicio >= ? AND dataFim IS NULL) OR (dataInicio <= ? AND dataFim IS NOT NULL AND dataFim >= ?))");
				sql.setInt(1, codigoInterno);
				sql.setDate(2, new java.sql.Date(dataInicio.getTime()));
				sql.setDate(3, new java.sql.Date(dataInicio.getTime()));
				sql.setDate(4, new java.sql.Date(dataInicio.getTime()));
			}
			else
			{
				sql =
					UtilRelacional.prepararComando(
						"SELECT * FROM ass_FUNCIONARIO_HISTORICO "
							+ "WHERE chaveFuncionario = ? AND ((dataInicio BETWEEN ? AND ?) "
							+ "OR (dataFim IS NOT NULL AND dataFim BETWEEN ? AND ?) "
							+ "OR (dataInicio <= ? AND dataFim IS NOT NULL AND dataFim >= ?) "
							+ "OR (dataInicio >= ? AND dataFim IS NULL))");
				sql.setInt(1, codigoInterno);
				sql.setDate(2, new java.sql.Date(dataInicio.getTime()));
				sql.setDate(3, new java.sql.Date(dataFim.getTime()));
				sql.setDate(4, new java.sql.Date(dataInicio.getTime()));
				sql.setDate(5, new java.sql.Date(dataFim.getTime()));
				sql.setDate(6, new java.sql.Date(dataInicio.getTime()));
				sql.setDate(7, new java.sql.Date(dataFim.getTime()));
			}

			ResultSet resultado = sql.executeQuery();
			if (resultado.next())
			{
				existeSobreposicao = true;
			}

			sql.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("FuncionarioRelacional.sobreposicaoHistoricos: " + e.toString());
		}
		return existeSobreposicao;
	} /* sobreposicaoHistoricos */

	public boolean sobreposicaoFuncResponsavel(int codigoInterno, Date dataInicio, Date dataFim)
	{
		boolean existeSobreposicao = false;
		try
		{
			PreparedStatement sql = null;
			if (dataFim == null)
			{
				sql =
					UtilRelacional.prepararComando(
						"SELECT * FROM ass_FUNCIONARIO_HISTORICO "
							+ "WHERE chaveFuncionario = ? AND "
							+ "((dataInicio >= ? AND dataFim IS NULL) OR (dataInicio <= ? AND dataFim IS NOT NULL AND dataFim >= ?)) "
							+ "AND chaveFuncResponsavel IS NOT NULL");
				sql.setInt(1, codigoInterno);
				sql.setDate(2, new java.sql.Date(dataInicio.getTime()));
				sql.setDate(3, new java.sql.Date(dataInicio.getTime()));
				sql.setDate(4, new java.sql.Date(dataInicio.getTime()));
			}
			else
			{
				sql =
					UtilRelacional.prepararComando(
						"SELECT * FROM ass_FUNCIONARIO_HISTORICO "
							+ "WHERE chaveFuncionario = ? AND ((dataInicio BETWEEN ? AND ?) "
							+ "OR (dataFim IS NOT NULL AND dataFim BETWEEN ? AND ?) "
							+ "OR (dataInicio <= ? AND dataFim IS NOT NULL AND dataFim >= ?) "
							+ "OR (dataInicio >= ? AND dataInicio <= ? AND dataFim IS NULL)) "
							+ "AND chaveFuncResponsavel IS NOT NULL");
				sql.setInt(1, codigoInterno);
				sql.setDate(2, new java.sql.Date(dataInicio.getTime()));
				sql.setDate(3, new java.sql.Date(dataFim.getTime()));
				sql.setDate(4, new java.sql.Date(dataInicio.getTime()));
				sql.setDate(5, new java.sql.Date(dataFim.getTime()));
				sql.setDate(6, new java.sql.Date(dataInicio.getTime()));
				sql.setDate(7, new java.sql.Date(dataFim.getTime()));
				sql.setDate(8, new java.sql.Date(dataInicio.getTime()));
				sql.setDate(9, new java.sql.Date(dataFim.getTime()));
			}

			ResultSet resultado = sql.executeQuery();
			if (resultado.next())
			{
				existeSobreposicao = true;
			}

			sql.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("FuncionarioRelacional.sobreposicaoFuncResponsavel: " + e.toString());
		}
		return existeSobreposicao;
	} /* sobreposicaoFuncResponsavel */

	public boolean sobreposicaoCCLocalTrabalho(int codigoInterno, Date dataInicio, Date dataFim)
	{
		boolean existeSobreposicao = false;
		try
		{
			PreparedStatement sql = null;
			if (dataFim == null)
			{
				sql =
					UtilRelacional.prepararComando(
						"SELECT * FROM ass_FUNCIONARIO_HISTORICO "
							+ "WHERE chaveFuncionario = ? AND "
							+ "((dataInicio >= ? AND dataFim IS NULL) OR (dataInicio <= ? AND dataFim IS NOT NULL AND dataFim >= ?)) "
							+ "AND chaveCCLocalTrabalho IS NOT NULL");
				sql.setInt(1, codigoInterno);
				sql.setDate(2, new java.sql.Date(dataInicio.getTime()));
				sql.setDate(3, new java.sql.Date(dataInicio.getTime()));
				sql.setDate(4, new java.sql.Date(dataInicio.getTime()));
			}
			else
			{
				sql =
					UtilRelacional.prepararComando(
						"SELECT * FROM ass_FUNCIONARIO_HISTORICO "
							+ "WHERE chaveFuncionario = ? AND ((dataInicio BETWEEN ? AND ?) "
							+ "OR (dataFim IS NOT NULL AND dataFim BETWEEN ? AND ?) "
							+ "OR (dataInicio <= ? AND dataFim IS NOT NULL AND dataFim >= ?) "
							+ "OR (dataInicio >= ? AND dataInicio <= ? AND dataFim IS NULL)) "
							+ "AND chaveCCLocalTrabalho IS NOT NULL");
				sql.setInt(1, codigoInterno);
				sql.setDate(2, new java.sql.Date(dataInicio.getTime()));
				sql.setDate(3, new java.sql.Date(dataFim.getTime()));
				sql.setDate(4, new java.sql.Date(dataInicio.getTime()));
				sql.setDate(5, new java.sql.Date(dataFim.getTime()));
				sql.setDate(6, new java.sql.Date(dataInicio.getTime()));
				sql.setDate(7, new java.sql.Date(dataFim.getTime()));
				sql.setDate(8, new java.sql.Date(dataInicio.getTime()));
				sql.setDate(9, new java.sql.Date(dataFim.getTime()));
			}

			ResultSet resultado = sql.executeQuery();
			if (resultado.next())
			{
				existeSobreposicao = true;
			}

			sql.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("FuncionarioRelacional.sobreposicaoCCLocalTrabalho: " + e.toString());
		}
		return existeSobreposicao;
	} /* sobreposicaoCCLocalTrabalho */

	public boolean sobreposicaoCCCorrespondencia(int codigoInterno, Date dataInicio, Date dataFim)
	{
		boolean existeSobreposicao = false;
		try
		{
			PreparedStatement sql = null;
			if (dataFim == null)
			{
				sql =
					UtilRelacional.prepararComando(
						"SELECT * FROM ass_FUNCIONARIO_HISTORICO "
							+ "WHERE chaveFuncionario = ? AND "
							+ "((dataInicio >= ? AND dataFim IS NULL) OR (dataInicio <= ? AND dataFim IS NOT NULL AND dataFim >= ?)) "
							+ "AND chaveCCCorrespondencia IS NOT NULL");
				sql.setInt(1, codigoInterno);
				sql.setDate(2, new java.sql.Date(dataInicio.getTime()));
				sql.setDate(3, new java.sql.Date(dataInicio.getTime()));
				sql.setDate(4, new java.sql.Date(dataInicio.getTime()));
			}
			else
			{
				sql =
					UtilRelacional.prepararComando(
						"SELECT * FROM ass_FUNCIONARIO_HISTORICO "
							+ "WHERE chaveFuncionario = ? AND ((dataInicio BETWEEN ? AND ?) "
							+ "OR (dataFim IS NOT NULL AND dataFim BETWEEN ? AND ?) "
							+ "OR (dataInicio <= ? AND dataFim IS NOT NULL AND dataFim >= ?) "
							+ "OR (dataInicio >= ? AND dataInicio <= ? AND dataFim IS NULL)) "
							+ "AND chaveCCCorrespondencia IS NOT NULL");
				sql.setInt(1, codigoInterno);
				sql.setDate(2, new java.sql.Date(dataInicio.getTime()));
				sql.setDate(3, new java.sql.Date(dataFim.getTime()));
				sql.setDate(4, new java.sql.Date(dataInicio.getTime()));
				sql.setDate(5, new java.sql.Date(dataFim.getTime()));
				sql.setDate(6, new java.sql.Date(dataInicio.getTime()));
				sql.setDate(7, new java.sql.Date(dataFim.getTime()));
				sql.setDate(8, new java.sql.Date(dataInicio.getTime()));
				sql.setDate(9, new java.sql.Date(dataFim.getTime()));
			}

			ResultSet resultado = sql.executeQuery();
			if (resultado.next())
			{
				existeSobreposicao = true;
			}

			sql.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("FuncionarioRelacional.sobreposicaoCCCorrespondencia: " + e.toString());
		}
		return existeSobreposicao;
	} /* sobreposicaoCCCorrespondencia */

	public boolean sobreposicaoCCVencimento(int codigoInterno, Date dataInicio, Date dataFim)
	{
		boolean existeSobreposicao = false;
		try
		{
			PreparedStatement sql = null;
			if (dataFim == null)
			{
				sql =
					UtilRelacional.prepararComando(
						"SELECT * FROM ass_FUNCIONARIO_HISTORICO "
							+ "WHERE chaveFuncionario = ? AND "
							+ "((dataInicio >= ? AND dataFim IS NULL) OR (dataInicio <= ? AND dataFim IS NOT NULL AND dataFim >= ?)) "
							+ "AND chaveCCVencimento IS NOT NULL");
				sql.setInt(1, codigoInterno);
				sql.setDate(2, new java.sql.Date(dataInicio.getTime()));
				sql.setDate(3, new java.sql.Date(dataInicio.getTime()));
				sql.setDate(4, new java.sql.Date(dataInicio.getTime()));
			}
			else
			{
				sql =
					UtilRelacional.prepararComando(
						"SELECT * FROM ass_FUNCIONARIO_HISTORICO "
							+ "WHERE chaveFuncionario = ? AND ((dataInicio BETWEEN ? AND ?) "
							+ "OR (dataFim IS NOT NULL AND dataFim BETWEEN ? AND ?) "
							+ "OR (dataInicio <= ? AND dataFim IS NOT NULL AND dataFim >= ?) "
							+ "OR (dataInicio >= ? AND dataInicio <= ? AND dataFim IS NULL)) "
							+ "AND chaveCCVencimento IS NOT NULL");
				sql.setInt(1, codigoInterno);
				sql.setDate(2, new java.sql.Date(dataInicio.getTime()));
				sql.setDate(3, new java.sql.Date(dataFim.getTime()));
				sql.setDate(4, new java.sql.Date(dataInicio.getTime()));
				sql.setDate(5, new java.sql.Date(dataFim.getTime()));
				sql.setDate(6, new java.sql.Date(dataInicio.getTime()));
				sql.setDate(7, new java.sql.Date(dataFim.getTime()));
				sql.setDate(8, new java.sql.Date(dataInicio.getTime()));
				sql.setDate(9, new java.sql.Date(dataFim.getTime()));
			}

			ResultSet resultado = sql.executeQuery();
			if (resultado.next())
			{
				existeSobreposicao = true;
			}

			sql.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("FuncionarioRelacional.sobreposicaoCCVencimento: " + e.toString());
		}
		return existeSobreposicao;
	} /* sobreposicaoCCVencimento */

	public boolean sobreposicaoCalendario(int codigoInterno, Date dataInicio, Date dataFim)
	{
		boolean existeSobreposicao = false;
		try
		{
			PreparedStatement sql = null;
			if (dataFim == null)
			{
				sql =
					UtilRelacional.prepararComando(
						"SELECT * FROM ass_FUNCIONARIO_HISTORICO "
							+ "WHERE chaveFuncionario = ? AND "
							+ "((dataInicio >= ? AND dataFim IS NULL) OR (dataInicio <= ? AND dataFim IS NOT NULL AND dataFim >= ?)) "
							+ "AND calendario IS NOT NULL");
				sql.setInt(1, codigoInterno);
				sql.setDate(2, new java.sql.Date(dataInicio.getTime()));
				sql.setDate(3, new java.sql.Date(dataInicio.getTime()));
				sql.setDate(4, new java.sql.Date(dataInicio.getTime()));
			}
			else
			{
				sql =
					UtilRelacional.prepararComando(
						"SELECT * FROM ass_FUNCIONARIO_HISTORICO "
							+ "WHERE chaveFuncionario = ? AND ((dataInicio BETWEEN ? AND ?) "
							+ "OR (dataFim IS NOT NULL AND dataFim BETWEEN ? AND ?) "
							+ "OR (dataInicio <= ? AND dataFim IS NOT NULL AND dataFim >= ?) "
							+ "OR (dataInicio >= ? AND dataInicio <= ? AND dataFim IS NULL)) "
							+ "AND calendario IS NOT NULL");
				sql.setInt(1, codigoInterno);
				sql.setDate(2, new java.sql.Date(dataInicio.getTime()));
				sql.setDate(3, new java.sql.Date(dataFim.getTime()));
				sql.setDate(4, new java.sql.Date(dataInicio.getTime()));
				sql.setDate(5, new java.sql.Date(dataFim.getTime()));
				sql.setDate(6, new java.sql.Date(dataInicio.getTime()));
				sql.setDate(7, new java.sql.Date(dataFim.getTime()));
				sql.setDate(8, new java.sql.Date(dataInicio.getTime()));
				sql.setDate(9, new java.sql.Date(dataFim.getTime()));
			}

			ResultSet resultado = sql.executeQuery();
			if (resultado.next())
			{
				existeSobreposicao = true;
			}

			sql.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("FuncionarioRelacional.sobreposicaoCalendario: " + e.toString());
		}
		return existeSobreposicao;
	} /* sobreposicaoCalendario */

	public boolean sobreposicaoStatusAssiduidade(int codigoInterno, Date dataInicio, Date dataFim)
	{
		boolean existeSobreposicao = false;
		try
		{
			PreparedStatement sql = null;
			if (dataFim == null)
			{
				sql =
					UtilRelacional.prepararComando(
						"SELECT * FROM ass_FUNCIONARIO_HISTORICO "
							+ "WHERE chaveFuncionario = ? AND "
							+ "((dataInicio >= ? AND dataFim IS NULL) OR (dataInicio <= ? AND dataFim IS NOT NULL AND dataFim >= ?)) "
							+ "AND chaveStatus IS NOT NULL");
				sql.setInt(1, codigoInterno);
				sql.setDate(2, new java.sql.Date(dataInicio.getTime()));
				sql.setDate(3, new java.sql.Date(dataInicio.getTime()));
				sql.setDate(4, new java.sql.Date(dataInicio.getTime()));
			}
			else
			{
				sql =
					UtilRelacional.prepararComando(
						"SELECT * FROM ass_FUNCIONARIO_HISTORICO "
							+ "WHERE chaveFuncionario = ? AND ((dataInicio BETWEEN ? AND ?) "
							+ "OR (dataFim IS NOT NULL AND dataFim BETWEEN ? AND ?) "
							+ "OR (dataInicio <= ? AND dataFim IS NOT NULL AND dataFim >= ?) "
							+ "OR (dataInicio >= ? AND dataInicio <= ? AND dataFim IS NULL)) "
							+ "AND chaveStatus IS NOT NULL");
				sql.setInt(1, codigoInterno);
				sql.setDate(2, new java.sql.Date(dataInicio.getTime()));
				sql.setDate(3, new java.sql.Date(dataFim.getTime()));
				sql.setDate(4, new java.sql.Date(dataInicio.getTime()));
				sql.setDate(5, new java.sql.Date(dataFim.getTime()));
				sql.setDate(6, new java.sql.Date(dataInicio.getTime()));
				sql.setDate(7, new java.sql.Date(dataFim.getTime()));
				sql.setDate(8, new java.sql.Date(dataInicio.getTime()));
				sql.setDate(9, new java.sql.Date(dataFim.getTime()));
			}

			ResultSet resultado = sql.executeQuery();
			if (resultado.next())
			{
				existeSobreposicao = true;
			}

			sql.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("FuncionarioRelacional.sobreposicaoStatusAssiduidade: " + e.toString());
		}
		return existeSobreposicao;
	} /* sobreposicaoStatusAssiduidade */

	public boolean temHistorico(int numeroMecanografico)
	{
		boolean temHistorico = false;
		try
		{
			PreparedStatement sql =
				UtilRelacional.prepararComando(
					"SELECT * FROM ass_FUNCIONARIO WHERE numeroMecanografico = ?");
			sql.setInt(1, numeroMecanografico);
			ResultSet resultado = sql.executeQuery();
			if (resultado.next())
			{
				PreparedStatement sql2 =
					UtilRelacional.prepararComando(
						"SELECT * FROM ass_FUNCIONARIO_HISTORICO " + "WHERE chaveFuncionario = ? ");
				sql2.setInt(1, resultado.getInt("codigoInterno"));
				ResultSet resultado2 = sql2.executeQuery();
				if (resultado2.next())
				{
					temHistorico = true;
				}
				sql2.close();
			}
			sql.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("FuncionarioRelacional.temHistorico: " + e.toString());
		}
		return temHistorico;
	} /* temHistorico */

	public boolean temHistoricoFuncResponsavel(int numeroMecanografico)
	{
		boolean temHistorico = false;
		try
		{
			PreparedStatement sql =
				UtilRelacional.prepararComando(
					"SELECT * FROM ass_FUNCIONARIO WHERE numeroMecanografico = ?");
			sql.setInt(1, numeroMecanografico);
			ResultSet resultado = sql.executeQuery();
			if (resultado.next())
			{
				PreparedStatement sql2 =
					UtilRelacional.prepararComando(
						"SELECT * FROM ass_FUNCIONARIO_HISTORICO "
							+ "WHERE chaveFuncionario = ? AND chaveFuncResponsavel IS NOT NULL");
				sql2.setInt(1, resultado.getInt("codigoInterno"));
				ResultSet resultado2 = sql2.executeQuery();
				if (resultado2.next())
				{
					temHistorico = true;
				}
				sql2.close();
			}
			sql.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("FuncionarioRelacional.temHistoricoFuncResponsavel: " + e.toString());
		}
		return temHistorico;
	} /* temHistoricoFuncResponsavel */

	public boolean temHistoricoCCLocalTrabalho(int numeroMecanografico)
	{
		boolean temHistorico = false;
		try
		{
			PreparedStatement sql =
				UtilRelacional.prepararComando(
					"SELECT * FROM ass_FUNCIONARIO WHERE numeroMecanografico = ?");
			sql.setInt(1, numeroMecanografico);
			ResultSet resultado = sql.executeQuery();
			if (resultado.next())
			{
				PreparedStatement sql2 =
					UtilRelacional.prepararComando(
						"SELECT * FROM ass_FUNCIONARIO_HISTORICO "
							+ "WHERE chaveFuncionario = ? AND chaveCCLocalTrabalho IS NOT NULL");
				sql2.setInt(1, resultado.getInt("codigoInterno"));
				ResultSet resultado2 = sql2.executeQuery();
				if (resultado2.next())
				{
					temHistorico = true;
				}
				sql2.close();
			}
			sql.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("FuncionarioRelacional.temHistoricoCCLocalTrabalho: " + e.toString());
		}
		return temHistorico;
	} /* temHistoricoCCLocalTrabalho */

	public boolean temHistoricoCCCorrespondencia(int numeroMecanografico)
	{
		boolean temHistorico = false;
		try
		{
			PreparedStatement sql =
				UtilRelacional.prepararComando(
					"SELECT * FROM ass_FUNCIONARIO WHERE numeroMecanografico = ?");
			sql.setInt(1, numeroMecanografico);
			ResultSet resultado = sql.executeQuery();
			if (resultado.next())
			{
				PreparedStatement sql2 =
					UtilRelacional.prepararComando(
						"SELECT * FROM ass_FUNCIONARIO_HISTORICO "
							+ "WHERE chaveFuncionario = ? AND chaveCCCorrespondencia IS NOT NULL");
				sql2.setInt(1, resultado.getInt("codigoInterno"));
				ResultSet resultado2 = sql2.executeQuery();
				if (resultado2.next())
				{
					temHistorico = true;
				}
				sql2.close();
			}
			sql.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("FuncionarioRelacional.temHistoricoCCCorrespondencia: " + e.toString());
		}
		return temHistorico;
	} /* temHistoricoCCCorrespondencia */

	public boolean temHistoricoCCVencimento(int numeroMecanografico)
	{
		boolean temHistorico = false;
		try
		{
			PreparedStatement sql =
				UtilRelacional.prepararComando(
					"SELECT * FROM ass_FUNCIONARIO WHERE numeroMecanografico = ?");
			sql.setInt(1, numeroMecanografico);
			ResultSet resultado = sql.executeQuery();
			if (resultado.next())
			{
				PreparedStatement sql2 =
					UtilRelacional.prepararComando(
						"SELECT * FROM ass_FUNCIONARIO_HISTORICO "
							+ "WHERE chaveFuncionario = ? AND chaveCCVencimento IS NOT NULL");
				sql2.setInt(1, resultado.getInt("codigoInterno"));
				ResultSet resultado2 = sql2.executeQuery();
				if (resultado2.next())
				{
					temHistorico = true;
				}
				sql2.close();
			}
			sql.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("FuncionarioRelacional.temHistoricoCCVencimento: " + e.toString());
		}
		return temHistorico;
	} /* temHistoricoCCVencimento */

	public boolean temHistoricoCalendario(int numeroMecanografico)
	{
		boolean temHistorico = false;
		try
		{
			PreparedStatement sql =
				UtilRelacional.prepararComando(
					"SELECT * FROM ass_FUNCIONARIO WHERE numeroMecanografico = ?");
			sql.setInt(1, numeroMecanografico);
			ResultSet resultado = sql.executeQuery();
			if (resultado.next())
			{
				PreparedStatement sql2 =
					UtilRelacional.prepararComando(
						"SELECT * FROM ass_FUNCIONARIO_HISTORICO "
							+ "WHERE chaveFuncionario = ? AND calendario IS NOT NULL");
				sql2.setInt(1, resultado.getInt("codigoInterno"));
				ResultSet resultado2 = sql2.executeQuery();
				if (resultado2.next())
				{
					temHistorico = true;
				}
				sql2.close();
			}
			sql.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("FuncionarioRelacional.temHistoricoCalendario: " + e.toString());
		}
		{
			return temHistorico;
		}
	} /* temHistoricoCalendario */

	public boolean temHistoricoStatusAssiduidade(int numeroMecanografico)
	{
		boolean temHistorico = false;
		try
		{
			PreparedStatement sql =
				UtilRelacional.prepararComando(
					"SELECT * FROM ass_FUNCIONARIO WHERE numeroMecanografico = ?");
			sql.setInt(1, numeroMecanografico);
			ResultSet resultado = sql.executeQuery();
			if (resultado.next())
			{
				PreparedStatement sql2 =
					UtilRelacional.prepararComando(
						"SELECT * FROM ass_FUNCIONARIO_HISTORICO "
							+ "WHERE chaveFuncionario = ? AND chaveStatus IS NOT NULL");
				sql2.setInt(1, resultado.getInt("codigoInterno"));
				ResultSet resultado2 = sql2.executeQuery();
				if (resultado2.next())
				{
					temHistorico = true;
				}
				sql2.close();
			}
			sql.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("FuncionarioRelacional.temHistoricoStatusAssiduidade: " + e.toString());
		}
		return temHistorico;
	} /* temHistoricoStatusAssiduidade */

	public int ultimoCodigoInterno()
	{
		int ultimo = 0;
		try
		{
			PreparedStatement sql =
				UtilRelacional.prepararComando("SELECT max(codigoInterno) FROM ass_FUNCIONARIO");
			ResultSet resultado = sql.executeQuery();
			if (resultado.next())
				ultimo = resultado.getInt(1);
			sql.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("FuncionarioRelacional.ultimoCodigoInterno: " + e.toString());
		}
		return ultimo;

	} /* ultimoCodigoInterno */

	/****************************************************************************************************
	 * Assiduidade do Funcionario
	 ***************************************************************************************************/
	public boolean escreverFimAssiduidade(int numMecanografico, Date fimAssiduidade)
	{
		boolean resultado = false;
		try
		{
			PreparedStatement sql =
				UtilRelacional.prepararComando(
					"SELECT codigoInterno FROM ass_FUNCIONARIO " + "WHERE numeroMecanografico = ?");
			sql.setInt(1, numMecanografico);

			ResultSet resultadoQuery = sql.executeQuery();

			int chaveFuncionario = 0;
			if (resultadoQuery.next())
			{
				chaveFuncionario = resultadoQuery.getInt("codigoInterno");
			}
			else
			{
				sql.close();
				return resultado;
			}
			sql.close();

			//descobre último registo de assiduidade sobre horário
			sql =
				UtilRelacional.prepararComando(
					"SELECT MAX(dataInicio) FROM ass_HORARIO " + "WHERE chaveFuncionario = ?");
			sql.setInt(1, chaveFuncionario);

			resultadoQuery = sql.executeQuery();
			Date dataAssiduidade = null;
			if (resultadoQuery.next())
			{
				System.out.println("--> data inicio do horario");
				dataAssiduidade = resultadoQuery.getDate("MAX(dataInicio)");
			}
			sql.close();

			if (dataAssiduidade != null)
			{
				//escreve a data fim de assiduidade nos actuais registos do horário
				sql =
					UtilRelacional.prepararComando(
						"UPDATE ass_HORARIO SET "
							+ "dataFim = ? "
							+ "WHERE chaveFuncionario = ? "
							+ "AND dataInicio = ?");
				sql.setDate(1, new java.sql.Date(fimAssiduidade.getTime()));
				sql.setInt(2, chaveFuncionario);
				sql.setDate(3, new java.sql.Date(dataAssiduidade.getTime()));

				sql.executeUpdate();
				sql.close();
			}

			//escreve a data fim de assiduidade nos actuais registos do histórico
			sql =
				UtilRelacional.prepararComando(
					"UPDATE ass_FUNCIONARIO_HISTORICO SET "
						+ "dataFim = ? "
						+ " WHERE chaveFuncionario = ? "
						+ "AND ((dataFim IS NULL) OR (dataFim IS NOT NULL AND dataFim > ?))");
			sql.setDate(1, new java.sql.Date(fimAssiduidade.getTime()));
			sql.setInt(2, chaveFuncionario);
			sql.setDate(3, new java.sql.Date(fimAssiduidade.getTime()));

			sql.executeUpdate();
			sql.close();

			//escreve a data fim de assiduidade no registo de cartão de funcionário
			sql =
				UtilRelacional.prepararComando(
					"UPDATE ass_CARTAO SET "
						+ "dataFim = ? "
						+ " WHERE chaveFuncionario = ? "
						+ " AND dataFim > ?");
			sql.setDate(1, new java.sql.Date(fimAssiduidade.getTime()));
			sql.setInt(2, chaveFuncionario);
			sql.setDate(3, new java.sql.Date(fimAssiduidade.getTime()));

			sql.executeUpdate();
			sql.close();

			resultado = true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("FuncionarioRelacional.escreverFimAssiduidade: " + e.toString());
		}
		{
			return resultado;
		}
	} /* escreverFimAssiduidade */

	//Se retornar null ocorreu um erro ou o funcionário não tem fim de Assiduidade
	//Se retornar uma data pode não ser o fim de Assiduidade ou o fim do horário
	public Timestamp lerFimAssiduidade(int numMecanografico)
	{
		Timestamp dataAssiduidade = null;
		try
		{
			PreparedStatement sql =
				UtilRelacional.prepararComando(
					"SELECT codigoInterno FROM ass_FUNCIONARIO " + "WHERE numeroMecanografico = ?");
			sql.setInt(1, numMecanografico);
			ResultSet resultadoQuery = sql.executeQuery();
			int chaveFuncionario = 0;
			if (resultadoQuery.next())
			{
				chaveFuncionario = resultadoQuery.getInt("codigoInterno");
			}
			else
			{
				sql.close();
				return null;
			}
			sql.close();
			sql =
				UtilRelacional.prepararComando(
					"SELECT MAX(dataInicio) FROM ass_HORARIO " + "WHERE chaveFuncionario = ?");
			sql.setInt(1, chaveFuncionario);
			resultadoQuery = sql.executeQuery();
			Date dataInicioAssiduidade = null;
			if (resultadoQuery.next())
			{
				dataInicioAssiduidade = resultadoQuery.getDate("MAX(dataInicio)");
			}
			sql.close();
			sql =
				UtilRelacional.prepararComando(
					"SELECT dataFim FROM ass_HORARIO "
						+ "WHERE chaveFuncionario = ? AND dataInicio = ?");
			sql.setInt(1, chaveFuncionario);
			sql.setDate(2, new java.sql.Date(dataInicioAssiduidade.getTime()));
			resultadoQuery = sql.executeQuery();
			if (resultadoQuery.next())
			{
				if (resultadoQuery.getString("dataFim") != null)
				{
					dataAssiduidade =
						new Timestamp(
							Timestamp
								.valueOf(resultadoQuery.getString("dataFim") + " 23:59:59.0")
								.getTime());
				}
			}
			sql.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("FuncionarioRelacional.lerFimAssiduidade: " + e.toString());
		}
		{
			return dataAssiduidade;
		}
	} /* lerFimAssiduidade */

	public Timestamp lerInicioAssiduidade(int numMecanografico)
	{
		Timestamp dataAssiduidade = null;
		try
		{
			//chave do funcionário
			PreparedStatement sql =
				UtilRelacional.prepararComando(
					"SELECT codigoInterno FROM ass_FUNCIONARIO " + "WHERE numeroMecanografico = ?");
			sql.setInt(1, numMecanografico);

			ResultSet resultadoQuery = sql.executeQuery();

			int chaveFuncionario = 0;
			if (resultadoQuery.next())
			{
				chaveFuncionario = resultadoQuery.getInt("codigoInterno");
			}
			else
			{
				sql.close();
				return null;
			}
			sql.close();

			//data de inicio de assiduidade pelo horário
			sql =
				UtilRelacional.prepararComando(
					"SELECT MIN(dataInicio) FROM ass_HORARIO " + "WHERE chaveFuncionario = ?");
			sql.setInt(1, chaveFuncionario);

			resultadoQuery = sql.executeQuery();
			if (resultadoQuery.next())
			{
				resultadoQuery.getDate("MIN(dataInicio)");
				if (!resultadoQuery.wasNull())
				{
					dataAssiduidade = new Timestamp(resultadoQuery.getDate("MIN(dataInicio)").getTime());
				}
			}
			sql.close();

			//data de inicio de assiduidade pelo histórico
			if (dataAssiduidade == null)
			{
				sql =
					UtilRelacional.prepararComando(
						"SELECT MIN(dataInicio) FROM ass_FUNCIONARIO_HISTORICO "
							+ "WHERE chaveFuncionario = ? AND chaveStatus IS NOT NULL");
				sql.setInt(1, chaveFuncionario);

				resultadoQuery = sql.executeQuery();
				if (resultadoQuery.next())
				{
					resultadoQuery.getDate("MIN(dataInicio)");
					if (!resultadoQuery.wasNull())
					{
						dataAssiduidade =
							new Timestamp(resultadoQuery.getDate("MIN(dataInicio)").getTime());
					}
				}
				sql.close();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("FuncionarioRelacional.lerInicioAssiduidade: " + e.toString());
		}
		{
			return dataAssiduidade;
		}
	} /* lerInicioAssiduidade */

	public boolean verificaFimAssiduidade(int numMecanografico, Date dataFimAssiduidade)
	{
		boolean resultado = false;
		try
		{
			PreparedStatement sql =
				UtilRelacional.prepararComando(
					"SELECT codigoInterno FROM ass_FUNCIONARIO " + "WHERE numeroMecanografico = ?");
			sql.setInt(1, numMecanografico);

			ResultSet resultadoQuery = sql.executeQuery();

			int chaveFuncionario = 0;
			if (resultadoQuery.next())
			{
				chaveFuncionario = resultadoQuery.getInt("codigoInterno");
			}
			else
			{
				sql.close();
				return resultado;
			}
			sql.close();

			sql =
				UtilRelacional.prepararComando(
					"SELECT MAX(dataInicio) FROM ass_HORARIO " + "WHERE chaveFuncionario = ?");
			sql.setInt(1, chaveFuncionario);

			resultadoQuery = sql.executeQuery();

			Date dataAssiduidade = null;
			if (resultadoQuery.next())
			{
				dataAssiduidade = resultadoQuery.getDate("MAX(dataInicio)");
			}
			sql.close();

			if (dataAssiduidade == null)
			{
				sql =
					UtilRelacional.prepararComando(
						"SELECT MAX(dataInicio) FROM ass_FUNCIONARIO_HISTORICO "
							+ "WHERE chaveFuncionario = ?");
				sql.setInt(1, chaveFuncionario);

				resultadoQuery = sql.executeQuery();

				if (resultadoQuery.next())
				{
					dataAssiduidade = resultadoQuery.getDate("MAX(dataInicio)");
				}
				sql.close();
			}

			//verifica se a data fim de assiduidade está contida nos registos do funcionario
			//se não então a data fim é válida
			if (dataFimAssiduidade.after(dataAssiduidade))
			{
				resultado = true;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("FuncionarioRelacional.verificaFimAssiduidade: " + e.toString());
		}
		return resultado;

	} /* verificaFimAssiduidade */
}