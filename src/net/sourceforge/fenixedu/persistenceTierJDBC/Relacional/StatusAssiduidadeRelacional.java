package net.sourceforge.fenixedu.persistenceTierJDBC.Relacional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.StatusAssiduidade;
import net.sourceforge.fenixedu.persistenceTierJDBC.IStatusAssiduidadePersistente;

/**
 * @author Fernanda Quitério & Tânia Pousão
 */
public class StatusAssiduidadeRelacional implements IStatusAssiduidadePersistente {

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
                        .getInt("quem"), new Date(resultadoQuery.getTimestamp("quando").getTime()));
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
                        .getInt("quem"), new Date(resultadoQuery.getTimestamp("quando").getTime()));
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
                        .getInt("quem"), new Date(resultadoQuery.getTimestamp("quando").getTime()));
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
                        resultadoQuery.getInt("quem"), new Date(resultadoQuery.getTimestamp("quando")
                                .getTime())));
            }
        } catch (Exception e) {
            System.out.println("StatusAssiduidadeRelacional.lerTodosStatus" + e.toString());
            return null;
        }
        return listaStatus;

    } /* lerTodosStatus */

}
