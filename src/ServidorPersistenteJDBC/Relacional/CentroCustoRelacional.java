package ServidorPersistenteJDBC.Relacional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import Dominio.CostCenter;
import ServidorPersistenteJDBC.ICentroCustoPersistente;

/**
 * @author Fernanda Quitério e Tania Pousão
 */
public class CentroCustoRelacional implements ICentroCustoPersistente {

    public boolean alterarCentroCusto(CostCenter centroCusto) {
        boolean resultado = false;

        try {
            PreparedStatement sql = UtilRelacional.prepararComando("UPDATE ass_CENTRO_CUSTO SET "
                    + "codigoInterno = ? , " + "sigla = ? , " + "departamento = ? ," + "seccao1 = ? ,"
                    + "seccao2 = ? " + "WHERE codigoInterno = ? ");

            sql.setInt(1, centroCusto.getIdInternal().intValue());
            sql.setString(2, centroCusto.getCode());
            sql.setString(3, centroCusto.getDepartament());
            sql.setString(4, centroCusto.getSection1());
            sql.setString(5, centroCusto.getSection2());
            sql.setInt(6, centroCusto.getIdInternal().intValue());

            sql.executeUpdate();
            sql.close();
            resultado = true;
        } catch (Exception e) {
            System.out.println("CentroCustoRelacional.alterarCentroCusto: " + e.toString());
        }
        return resultado;
    } /* alterarCentroCusto */

    public boolean escreverCentroCusto(CostCenter centroCusto) {
        boolean resultado = false;

        try {
            PreparedStatement sql = UtilRelacional
                    .prepararComando("INSERT INTO ass_CENTRO_CUSTO VALUES (?, ?, ?, ?, ?, 1)");

            sql.setInt(1, centroCusto.getIdInternal().intValue());
            sql.setString(2, centroCusto.getCode());
            sql.setString(3, centroCusto.getDepartament());
            sql.setString(4, centroCusto.getSection1());
            sql.setString(5, centroCusto.getSection2());

            sql.executeUpdate();
            sql.close();
            resultado = true;
        } catch (Exception e) {
            System.out.println("CentroCustoRelacional.escreverCentroCusto: " + e.toString());
        }

        return resultado;

    } /* escreverCentroCusto */

    public CostCenter lerCentroCusto(String sigla) {
        CostCenter centroCusto = null;

        try {
            PreparedStatement sql = UtilRelacional
                    .prepararComando("SELECT * FROM ass_CENTRO_CUSTO WHERE sigla = ?");

            sql.setString(1, sigla);

            ResultSet resultado = sql.executeQuery();

            if (resultado.next()) {
                centroCusto = new CostCenter(resultado
                        .getString("sigla"), resultado.getString("departamento"), resultado
                        .getString("seccao1"), resultado.getString("seccao2"));
                centroCusto.setIdInternal(new Integer(resultado.getInt("codigoInterno")));
            }
            sql.close();
        } catch (Exception e) {
            System.out.println("CentroCustoRelacional.lerCentroCusto: " + e.toString());
        }
        return centroCusto;

    } /* lerCentroCusto */

    public CostCenter lerCentroCusto(int codigoInterno) {
        CostCenter centroCusto = null;

        try {
            PreparedStatement sql = UtilRelacional
                    .prepararComando("SELECT * FROM ass_CENTRO_CUSTO WHERE codigoInterno = ?");

            sql.setInt(1, codigoInterno);

            ResultSet resultado = sql.executeQuery();
            if (resultado.next()) {
                centroCusto = new CostCenter(resultado
                        .getString("sigla"), resultado.getString("departamento"), resultado
                        .getString("seccao1"), resultado.getString("seccao2"));
                centroCusto.setIdInternal(new Integer(resultado.getInt("codigoInterno")));
            }
            sql.close();
        } catch (Exception e) {
            System.out.println("CentroCustoRelacional.lerCentroCusto: " + e.toString());
        }
        return centroCusto;

    } /* lerCentroCusto */

    public List lerTodosCentrosCusto() {
        List listaCentrosCusto = null;

        try {
            PreparedStatement sql = UtilRelacional
                    .prepararComando("SELECT * FROM ass_CENTRO_CUSTO ORDER BY sigla");
            ResultSet resultadoQuery = sql.executeQuery();

            listaCentrosCusto = new ArrayList();
            while (resultadoQuery.next()) {
                CostCenter costCenter = new CostCenter(resultadoQuery
                        .getString("sigla"), resultadoQuery.getString("departamento"), resultadoQuery
                        .getString("seccao1"), resultadoQuery.getString("seccao2"));
                costCenter.setIdInternal(new Integer(resultadoQuery.getInt("codigoInterno")));

                listaCentrosCusto.add(costCenter);
            }
            sql.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("CentroCustoRelacional.lerTodosCentrosCusto: " + e.toString());
        }
        return listaCentrosCusto;

    } /* lerTodosCentrosCusto */
}