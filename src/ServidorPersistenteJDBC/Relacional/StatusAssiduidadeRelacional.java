package ServidorPersistenteJDBC.Relacional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import Dominio.StatusAssiduidade;
import ServidorPersistenteJDBC.IStatusAssiduidadePersistente;

/**
 *
 * @author Fernanda Quitério & Tânia Pousão
 */
public class StatusAssiduidadeRelacional implements IStatusAssiduidadePersistente {

	public boolean alterarStatus(StatusAssiduidade status) {
		// TODO metodo alterarStatus
		return false;
	} /* alterarStatus */

	public boolean escreverStatus(StatusAssiduidade status) {
		boolean resultado = false;
		try {
			PreparedStatement sql = UtilRelacional.prepararComando("INSERT INTO ass_STATUS VALUES(?, ?, ? ,?, ?)");
			sql.setInt(1, status.getCodigoInterno());
			sql.setString(2, status.getSigla());
			sql.setString(3, status.getDesignacao());
			sql.setString(4, status.getEstado());
			sql.setString(5, status.getAssiduidade());

			sql.executeUpdate();
			sql.close();
			
			resultado = true;
		} catch (Exception e) {
			System.out.println("StatusAssiduidadeRelacional.escreverStatus: " + e.toString());
		} finally {
			return resultado;
		}
	} /* escreverStatus */

	public StatusAssiduidade lerStatus(int codigoInterno) {
		StatusAssiduidade status = null;

		try {
			PreparedStatement sql = UtilRelacional.prepararComando("SELECT * FROM ass_STATUS WHERE codigoInterno = ?");
			sql.setInt(1, codigoInterno);
			ResultSet resultadoQuery = sql.executeQuery();

			if(resultadoQuery.next()) {
				status =
					new StatusAssiduidade(
						resultadoQuery.getInt("codigoInterno"),
						resultadoQuery.getString("sigla"),
						resultadoQuery.getString("designacao"),
						resultadoQuery.getString("estado"),
						resultadoQuery.getString("assiduidade"));
			}
		} catch (Exception e) {
			System.out.println("StatusAssiduidadeRelacional.lerStatus: " + e.toString());
		} finally {
			return status;
		}
	} /* lerStatus */

	public ArrayList lerTodosStatusActivos() {
		ArrayList listaStatus = null;

		try {
			PreparedStatement sql = UtilRelacional.prepararComando("SELECT * FROM ass_STATUS WHERE estado='activo'");
			ResultSet resultadoQuery = sql.executeQuery();

			listaStatus = new ArrayList();
			while (resultadoQuery.next()) {
				listaStatus.add(
					new StatusAssiduidade(
						resultadoQuery.getInt("codigoInterno"),
						resultadoQuery.getString("sigla"),
						resultadoQuery.getString("designacao"),
						resultadoQuery.getString("estado"),
				resultadoQuery.getString("assiduidade")));
			}
		} catch (Exception e) {
			System.out.println("StatusAssiduidadeRelacional.lerTodosStatusActivos" + e.toString());
			return null;
		} finally {
			return listaStatus;
		}
	} /* lerTodosStatusActivos */

	public ArrayList lerTodosStatusInactivos() {
		ArrayList listaStatus = null;

		try {
			PreparedStatement sql = UtilRelacional.prepararComando("SELECT * FROM ass_STATUS WHERE estado='inactivo'");
			ResultSet resultadoQuery = sql.executeQuery();

			listaStatus = new ArrayList();
			while (resultadoQuery.next()) {
				listaStatus.add(
					new StatusAssiduidade(
						resultadoQuery.getInt("codigoInterno"),
						resultadoQuery.getString("sigla"),
						resultadoQuery.getString("designacao"),
						resultadoQuery.getString("estado"),
				resultadoQuery.getString("assiduidade")));
			}
		} catch (Exception e) {
			System.out.println("StatusAssiduidadeRelacional.lerTodosStatusInactivos" + e.toString());
			return null;
		} finally {
			return listaStatus;
		}
	} /* lerTodosStatusInactivos */
}