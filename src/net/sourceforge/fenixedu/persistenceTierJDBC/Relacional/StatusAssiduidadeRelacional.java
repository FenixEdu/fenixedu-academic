package net.sourceforge.fenixedu.persistenceTierJDBC.Relacional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.domain.StatusAssiduidade;
import net.sourceforge.fenixedu.persistenceTierJDBC.IStatusAssiduidadePersistente;

/**
 * @author Fernanda Quitério & Tânia Pousão
 */
public class StatusAssiduidadeRelacional implements IStatusAssiduidadePersistente {

    public boolean alterarStatus(StatusAssiduidade status) {
        boolean resultado = false;

        try {
            PreparedStatement sql = UtilRelacional.prepararComando("UPDATE ass_UNIDADE_MARCACAO SET "
                    + "codigoInterno = ? , " + "sigla = ? , " + "designacao = ? , " + "estado = ? , "
                    + "assiduidade = ?, " + "quem = ? , " + "quando = ? " + "WHERE sigla = ?");

            sql.setInt(1, status.getCodigoInterno());
            sql.setString(2, status.getSigla());
            sql.setString(3, status.getDesignacao());
            sql.setString(4, status.getEstado());
            sql.setString(5, status.getAssiduidade());
            sql.setInt(6, status.getQuem());
            if (status.getQuando() != null) {
                sql.setTimestamp(7, new Timestamp((status.getQuando()).getTime()));
            } else {
                sql.setTimestamp(7, null);
            }
            sql.setString(8, status.getSigla());

            sql.executeUpdate();
            sql.close();
            resultado = true;
        } catch (Exception e) {
            System.out.println("StatusAssiduidadeRelacional.alterarStatus: " + e.toString());
        }
        return resultado;

    } /* alterarStatus */

    public boolean escreverStatus(StatusAssiduidade status) {
        boolean resultado = false;
        try {
            PreparedStatement sql = UtilRelacional
                    .prepararComando("INSERT INTO ass_STATUS VALUES(?, ?, ? ,?, ?, ?, ?, null)");
            sql.setInt(1, status.getCodigoInterno());
            sql.setString(2, status.getSigla());
            sql.setString(3, status.getDesignacao());
            sql.setString(4, status.getEstado());
            sql.setString(5, status.getAssiduidade());
            sql.setInt(6, status.getQuem());
            if (status.getQuando() != null) {
                sql.setTimestamp(7, new Timestamp((status.getQuando()).getTime()));
            } else {
                Calendar agora = Calendar.getInstance();
                sql.setTimestamp(7, new Timestamp(agora.getTimeInMillis()));
            }

            sql.executeUpdate();
            sql.close();

            resultado = true;
        } catch (Exception e) {
            System.out.println("StatusAssiduidadeRelacional.escreverStatus: " + e.toString());
        }
        return resultado;

    } /* escreverStatus */

    public StatusAssiduidade lerStatus(int codigoInterno) {
        StatusAssiduidade status = null;

        try {
            PreparedStatement sql = UtilRelacional
                    .prepararComando("SELECT * FROM ass_STATUS WHERE codigoInterno = ?");
            sql.setInt(1, codigoInterno);
            ResultSet resultadoQuery = sql.executeQuery();

            if (resultadoQuery.next()) {
                status = new StatusAssiduidade(resultadoQuery.getInt("codigoInterno"), resultadoQuery
                        .getString("sigla"), resultadoQuery.getString("designacao"), resultadoQuery
                        .getString("estado"), resultadoQuery.getString("assiduidade"), resultadoQuery
                        .getInt("quem"), resultadoQuery.getTimestamp("quando"));
            }
        } catch (Exception e) {
            System.out.println("StatusAssiduidadeRelacional.lerStatus: " + e.toString());
        }
        return status;

    } /* lerStatus */

    public StatusAssiduidade lerStatus(String designacao) {
        StatusAssiduidade status = null;

        try {
            PreparedStatement sql = UtilRelacional
                    .prepararComando("SELECT * FROM ass_STATUS WHERE designacao = ?");
            sql.setString(1, designacao);
            ResultSet resultadoQuery = sql.executeQuery();

            if (resultadoQuery.next()) {
                status = new StatusAssiduidade(resultadoQuery.getInt("codigoInterno"), resultadoQuery
                        .getString("sigla"), resultadoQuery.getString("designacao"), resultadoQuery
                        .getString("estado"), resultadoQuery.getString("assiduidade"), resultadoQuery
                        .getInt("quem"), resultadoQuery.getTimestamp("quando"));
            }
        } catch (Exception e) {
            System.out.println("StatusAssiduidadeRelacional.lerStatus: " + e.toString());
        }
        return status;

    } /* lerStatus */

    public StatusAssiduidade lerStatus(String sigla, String designacao) {
        StatusAssiduidade status = null;

        try {
            PreparedStatement sql = UtilRelacional
                    .prepararComando("SELECT * FROM ass_STATUS WHERE sigla = ? OR designacao = ?");

            sql.setString(1, sigla);
            sql.setString(2, designacao);
            ResultSet resultadoQuery = sql.executeQuery();

            if (resultadoQuery.next()) {
                status = new StatusAssiduidade(resultadoQuery.getInt("codigoInterno"), resultadoQuery
                        .getString("sigla"), resultadoQuery.getString("designacao"), resultadoQuery
                        .getString("estado"), resultadoQuery.getString("assiduidade"), resultadoQuery
                        .getInt("quem"), resultadoQuery.getTimestamp("quando"));
            }
        } catch (Exception e) {
            System.out.println("StatusAssiduidadeRelacional.lerStatus: " + e.toString());
        }
        return status;

    }

    public List lerTodosStatus() {
        List listaStatus = null;

        try {
            PreparedStatement sql = UtilRelacional.prepararComando("SELECT * FROM ass_STATUS");
            ResultSet resultadoQuery = sql.executeQuery();

            listaStatus = new ArrayList();
            while (resultadoQuery.next()) {
                listaStatus.add(new StatusAssiduidade(resultadoQuery.getInt("codigoInterno"),
                        resultadoQuery.getString("sigla"), resultadoQuery.getString("designacao"),
                        resultadoQuery.getString("estado"), resultadoQuery.getString("assiduidade"),
                        resultadoQuery.getInt("quem"), resultadoQuery.getTimestamp("quando")));
            }
        } catch (Exception e) {
            System.out.println("StatusAssiduidadeRelacional.lerTodosStatus" + e.toString());
            return null;
        }
        return listaStatus;

    } /* lerTodosStatus */

    public List lerTodosStatusActivos() {
        List listaStatus = null;

        try {
            PreparedStatement sql = UtilRelacional
                    .prepararComando("SELECT * FROM ass_STATUS WHERE estado='activo'");
            ResultSet resultadoQuery = sql.executeQuery();

            listaStatus = new ArrayList();
            while (resultadoQuery.next()) {
                listaStatus.add(new StatusAssiduidade(resultadoQuery.getInt("codigoInterno"),
                        resultadoQuery.getString("sigla"), resultadoQuery.getString("designacao"),
                        resultadoQuery.getString("estado"), resultadoQuery.getString("assiduidade"),
                        resultadoQuery.getInt("quem"), resultadoQuery.getTimestamp("quando")));
            }
        } catch (Exception e) {
            System.out.println("StatusAssiduidadeRelacional.lerTodosStatusActivos" + e.toString());
            return null;
        }
        return listaStatus;

    } /* lerTodosStatusActivos */

    public List lerTodosStatusInactivos() {
        List listaStatus = null;

        try {
            PreparedStatement sql = UtilRelacional
                    .prepararComando("SELECT * FROM ass_STATUS WHERE estado='inactivo'");
            ResultSet resultadoQuery = sql.executeQuery();

            listaStatus = new ArrayList();
            while (resultadoQuery.next()) {
                listaStatus.add(new StatusAssiduidade(resultadoQuery.getInt("codigoInterno"),
                        resultadoQuery.getString("sigla"), resultadoQuery.getString("designacao"),
                        resultadoQuery.getString("estado"), resultadoQuery.getString("assiduidade"),
                        resultadoQuery.getInt("quem"), resultadoQuery.getTimestamp("quando")));
            }
        } catch (Exception e) {
            System.out.println("StatusAssiduidadeRelacional.lerTodosStatusInactivos" + e.toString());
            return null;
        }
        return listaStatus;

    } /* lerTodosStatusInactivos */

    public List lerTodosStatusPendentes() {
        List listaStatus = null;

        try {
            PreparedStatement sql = UtilRelacional
                    .prepararComando("SELECT * FROM ass_STATUS WHERE estado='pendente'");
            ResultSet resultadoQuery = sql.executeQuery();

            listaStatus = new ArrayList();
            while (resultadoQuery.next()) {
                listaStatus.add(new StatusAssiduidade(resultadoQuery.getInt("codigoInterno"),
                        resultadoQuery.getString("sigla"), resultadoQuery.getString("designacao"),
                        resultadoQuery.getString("estado"), resultadoQuery.getString("assiduidade"),
                        resultadoQuery.getInt("quem"), resultadoQuery.getTimestamp("quando")));
            }
        } catch (Exception e) {
            System.out.println("StatusAssiduidadeRelacional.lerTodosStatusPendentes" + e.toString());
            return null;
        }
        return listaStatus;

    } /* lerTodosStatusPendentes */
}