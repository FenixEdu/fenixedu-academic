package ServidorPersistenteJDBC.Relacional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.ListIterator;

import Dominio.Feriado;
import Dominio.Horario;
import ServidorPersistenteJDBC.IFeriadoPersistente;
import constants.assiduousness.Constants;

/**
 *
 * @author  Fernanda Quitério e Tania Pousão
 */
public class FeriadoRelacional implements IFeriadoPersistente {

	public boolean alterarFeriado(Feriado feriado) {
		boolean resultado = false;

		try {
			PreparedStatement sql =
				UtilRelacional.prepararComando(
					"UPDATE ass_FERIADO SET "
						+ "codigoInterno = ? , "
						+ "tipoFeriado = ? , "
						+ "descricao = ? "
						+ "data = ? "
						+ "WHERE codigoInterno = ? ");

			sql.setInt(1, feriado.getCodigoInterno());
			sql.setString(2, feriado.getTipoFeriado());
			sql.setString(3, feriado.getDescricao());
			sql.setDate(4, new java.sql.Date(feriado.getData().getTime()));
			sql.setInt(5, feriado.getCodigoInterno());

			sql.executeUpdate();
			sql.close();
			resultado = true;
		} catch (Exception e) {
			System.out.println("FeriadoRelacional.alterarFeriado: " + e.toString());
		} finally {
			return resultado;
		}
	} /* alterarFeriado */

	public boolean apagarFeriado(int codigoInterno) {
		boolean resultado = false;

		try {
			PreparedStatement sql = UtilRelacional.prepararComando("DELETE FROM ass_FERIADO WHERE codigoInterno = ?");

			sql.setInt(1, codigoInterno);

			sql.executeUpdate();
			sql.close();
			resultado = true;
		} catch (Exception e) {
			System.out.println("FeriadoRelacional.apagarFeriado: " + e.toString());
		} finally {
			return resultado;
		}
	} /* apagarFeriado */

	public boolean diaFeriado(Date dia) {
		boolean resultado = false;

		try {
			PreparedStatement sql = UtilRelacional.prepararComando("SELECT * FROM ass_FERIADO WHERE data = ?");
			sql.setDate(1, new java.sql.Date(dia.getTime()));

			ResultSet resultadoQuery = sql.executeQuery();
			if (resultadoQuery.next()) {
				resultado = true;
				sql.close();
			} else {
				sql.close();

				sql =
					UtilRelacional.prepararComando(
						"SELECT * FROM ass_FERIADO " + "WHERE SUBSTRING(data, 6, 10) = SUBSTRING(?, 6, 10) AND tipoFeriado <> 'MOVEL'");
				sql.setString(1, dia.toString().substring(0, 10));

				resultadoQuery = sql.executeQuery();
				if (resultadoQuery.next()) {
					resultado = true;
				}
				sql.close();
			}
		} catch (Exception e) {
			System.out.println("FeriadoRelacional.diaFeriado: " + e.toString());
		} finally {
			return resultado;
		}
	} /* diaFeriado*/

	public boolean escreverFeriado(Feriado feriado) {
		boolean resultado = false;

		try {
			PreparedStatement sql = UtilRelacional.prepararComando("INSERT INTO ass_FERIADO VALUES (?, ?, ?, ?)");

			sql.setInt(1, feriado.getCodigoInterno());
			sql.setString(2, feriado.getTipoFeriado());
			sql.setString(3, feriado.getDescricao());
			sql.setDate(4, new java.sql.Date(feriado.getData().getTime()));

			sql.executeUpdate();
			sql.close();
			resultado = true;
		} catch (Exception e) {
			System.out.println("FeriadoRelacional.escreverFeriado: " + e.toString());
		} finally {
			return resultado;
		}
	} /* escreverFeriado */

	public boolean escreverFeriados(ArrayList listaFeriados) {
		boolean resultado = false;

		try {
			ListIterator iterador = listaFeriados.listIterator();
			while (iterador.hasNext()) {
				Feriado feriado = (Feriado) iterador.next();
				PreparedStatement sql = UtilRelacional.prepararComando("INSERT INTO ass_FERIADO VALUES (?, ?, ?, ?)");

				sql.setInt(1, feriado.getCodigoInterno());
				sql.setString(2, feriado.getTipoFeriado());
				sql.setString(3, feriado.getDescricao());
				sql.setDate(4, new java.sql.Date(feriado.getData().getTime()));

				sql.executeUpdate();
				sql.close();
			}
			resultado = true;
		} catch (Exception e) {
			System.out.println("FeriadoRelacional.escreverFeriados: " + e.toString());
		} finally {
			return resultado;
		}
	} /* escreverFeriados */

	public Horario horarioFeriado(Date dia) {
		Horario horario = null;

		try {
			PreparedStatement sql = UtilRelacional.prepararComando("SELECT * FROM ass_FERIADO WHERE data = ?");
			sql.setDate(1, new java.sql.Date(dia.getTime()));

			ResultSet resultadoQuery = sql.executeQuery();
			if (resultadoQuery.next()) {
				horario = new Horario("FERIADO");

				Calendar calendar = Calendar.getInstance();
				calendar.set(1970, 0, 1, 00, 00, 00);
				horario.setInicioExpediente(new Timestamp(calendar.getTimeInMillis()));
				calendar.clear();
				calendar.set(1970, 0, 1, 23, 59, 00);
				horario.setFimExpediente(new Timestamp(calendar.getTimeInMillis()));
				sql.close();
			} else {
				sql.close();

				sql =
					UtilRelacional.prepararComando(
						"SELECT * FROM ass_FERIADO " + "WHERE SUBSTRING(data, 6, 10) = SUBSTRING(?, 6, 10) AND tipoFeriado <> 'MOVEL'");
				sql.setString(1, dia.toString().substring(0, 10));

				resultadoQuery = sql.executeQuery();
				if (resultadoQuery.next()) {
					horario = new Horario("FERIADO");

					Calendar calendar = Calendar.getInstance();
					calendar.set(1970, 0, 1, 00, 00, 00);
					horario.setInicioExpediente(new Timestamp(calendar.getTimeInMillis()));
					calendar.clear();
					calendar.set(1970, 0, 1, 23, 59, 00);
					horario.setFimExpediente(new Timestamp(calendar.getTimeInMillis()));
				}
				sql.close();
			}
		} catch (Exception e) {
			System.out.println("FeriadoRelacional.horarioFeriado: " + e.toString());
		} finally {
			return horario;
		}
	} /* horarioFeriado */

	public Horario horarioFeriado(int numMecanografico, Date dia) {
		Horario horario = null;
		try {
			/*
			//Descobre qual o calendario do funcionario na BD Oracle
			PreparedStatement sql =
				UtilRelacionalOracle.prepararComando(
					"SELECT ENTIDADE.CALCOD FROM EMPREGADO, ENTIDADE WHERE EMPREGADO.EMP_NUM=? " + "AND EMPREGADO.ENT_NUM = ENTIDADE.ENT_NUM");
			sql.setInt(1, numMecanografico);
			ResultSet resultadoQuery = sql.executeQuery();
			String calendario = null;
			if (resultadoQuery.next()) {
				calendario = resultadoQuery.getString("CALCOD");
			} else {
				sql.close();
				return horario;
			}
			sql.close();
			*/
			// descobre o tipo de calendario do funcionario
			PreparedStatement sql = UtilRelacional.prepararComando("SELECT calendario FROM ass_FUNCIONARIO WHERE numeroMecanografico = ?");
			sql.setInt(1, numMecanografico);
			ResultSet resultadoQuery = sql.executeQuery();
			String calendario = null;
			if(resultadoQuery.next()){
				calendario = resultadoQuery.getString("calendario");
			} else {
				sql.close();
				return null;
			}
			sql.close();
			
			sql =
				UtilRelacional.prepararComando(
					"SELECT * FROM ass_FERIADO "
						+ "WHERE (data = ? AND tipoFeriado = 'MOVEL') OR "
						+ "(SUBSTRING(data, 6, 10) = SUBSTRING(?, 6, 10) AND tipoFeriado= ?) OR "
						+ "(SUBSTRING(data, 6, 10) = SUBSTRING(?, 6, 10) AND tipoFeriado= 'FIXO')");
			sql.setDate(1, new java.sql.Date(dia.getTime()));
			sql.setString(2, dia.toString().substring(0, 10));
			sql.setString(3, calendario);
			sql.setString(4, dia.toString().substring(0, 10));

			resultadoQuery = sql.executeQuery();
			if (resultadoQuery.next()) {
				horario = new Horario(Constants.FERIADO);
				//Expediente de dioas de Descanso
				horario.setInicioExpediente(new Timestamp(Constants.EXPEDIENTE_MINIMO));
				horario.setFimExpediente(new Timestamp(Constants.EXPEDIENTE_MAXIMO));
			}
			sql.close();
		} catch (Exception e) {
			System.out.println("FeriadoRelacional.horarioFeriado: " + e.toString());
		} finally {
			return horario;
		}
	} /* horarioFeriado */

	public Feriado lerFeriado(int codigoInterno) {
		Feriado feriado = null;

		try {
			PreparedStatement sql = UtilRelacional.prepararComando("SELECT * FROM ass_FERIADO WHERE codigoInterno = ?");

			sql.setInt(1, codigoInterno);

			ResultSet resultado = sql.executeQuery();
			if (resultado.next()) {
				feriado =
					new Feriado(
						resultado.getInt("codigoInterno"),
						resultado.getString("tipoFeriado"),
						resultado.getString("descricao"),
						java.sql.Date.valueOf(resultado.getString("data")));
			}
			sql.close();
		} catch (Exception e) {
			System.out.println("FeriadoRelacional.lerFeriado: " + e.toString());
		} finally {
			return feriado;
		}
	} /* lerFeriado */
	
	public ArrayList lerTodosCalendarios(){
		ArrayList listaCalendarios = null;
		
		try{
		PreparedStatement sql = UtilRelacional.prepararComando("SELECT tipoFeriado FROM ass_FERIADO " 
		+ "WHERE tipoFeriado <>'MOVEL' and tipoFeriado<>'FIXO'");
		ResultSet resultadoQuery = sql.executeQuery();
		
		listaCalendarios = new ArrayList();
		while(resultadoQuery.next()){
			listaCalendarios.add(new String(resultadoQuery.getString("tipoFeriado")));
		}
		sql.close();
		} catch (Exception e){
			e.printStackTrace();
			System.out.println("FeriadoRelacional.lerTodosCalendarios: " + e.toString());
		} finally {
			return listaCalendarios;
		}
	} /* lerTodosCalendarios */
}